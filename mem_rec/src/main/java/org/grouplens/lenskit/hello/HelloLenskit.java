/*
 * Copyright 2011 University of Minnesota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.grouplens.lenskit.hello;

import org.lenskit.LenskitRecommenderEngine;
import org.lenskit.api.RecommenderBuildException;
import org.lenskit.LenskitConfiguration;
import org.lenskit.config.ConfigHelpers;
import org.lenskit.data.dao.EventDAO;
import org.lenskit.data.dao.ItemNameDAO;
import org.lenskit.data.dao.MapItemNameDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.text.Formats;
import org.grouplens.lenskit.data.text.TextEventDAO;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;
import org.lenskit.LenskitRecommender;
import org.lenskit.api.*;
import org.lenskit.baseline.BaselineScorer;
import org.lenskit.baseline.ItemMeanRatingItemScorer;
import org.lenskit.baseline.UserMeanBaseline;
import org.lenskit.baseline.UserMeanItemScorer;
import org.lenskit.knn.MinNeighbors;
import org.lenskit.knn.item.ItemItemScorer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

/**
 * Demonstration app for LensKit. This application builds an item-item CF model
 * from a CSV file, then generates recommendations for a user.
 *
 * Usage: java org.grouplens.lenskit.hello.HelloLenskit ratings.csv user
 */

public class HelloLenskit implements Runnable {
	public static void main(String[] args) throws SQLException {

		HelloLenskit hello = new HelloLenskit(args);

		try {
			hello.run();
		} catch (RuntimeException e) {
			cxn.close();
			System.err.println(e.toString());
			e.printStackTrace(System.err);
			System.exit(1);
		} finally {
			cxn.close();
		}
	}

	private List<Long> users;
	private int datasetType;
	private static Connection cxn;
	private static Connection cxn2;
	
	public HelloLenskit(String[] args) {
		users = new ArrayList<Long>(args.length);

		config();

		for (String arg : args) {
			users.add(Long.parseLong(arg));
		}
	}

	private void config() {
		// postgres connections
		try {
			cxn = ConnectionManager.getConnectionPostGresql();
			// cxn = ConnectionManager.getConnectionMonetDb();
			// cxn = ConnectionManager.getConnectionVoltDB();
			
			
			cxn2 = ConnectionManager.getConnectionPostGresql();
			// cxn2 = ConnectionManager.getConnectionMonetDb();
			// cxn2 = ConnectionManager.getConnectionVoltDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 0 - basic 100k
		// 1 - 1 million
		// 2 - 20 million
		this.datasetType = 0;

	}

	public void run() {
		// We first need to configure the data access.
		// We will use a simple delimited file; you can use something else like
		// a database (see JDBCRatingDAO).

		JDBCRatingDAO dao = new JDBCRatingDAO(this.cxn, new BasicStatementFactory_Postgresql(datasetType));

		// EventDAO dao = TextEventDAO.create(inputFile,
		// Formats.movieLensLatest());

		ItemNameDAO names = null;
		try {
			names = new ItemNameLookup(cxn2, datasetType);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Next: load the LensKit algorithm configuration
		LenskitConfiguration config = null;
		try {
			config = ConfigHelpers.load(new File("etc/item-item.groovy"));
		} catch (IOException e) {
			throw new RuntimeException("could not load configuration", e);
		}
		// Add our data component to the configuration
		config.addComponent(dao);

		/*
		 * 
		 * LenskitConfiguration config = new LenskitConfiguration();
		 * config.addComponent(dao); // Use item-item CF to score items
		 * config.bind(ItemScorer.class) .to(ItemItemScorer.class); // let's use
		 * personalized mean rating as the baseline/fallback predictor. //
		 * 2-step process: // First, use the user mean rating as the baseline
		 * scorer config.bind(BaselineScorer.class, ItemScorer.class)
		 * .to(UserMeanItemScorer.class); // Second, use the item mean rating as
		 * the base for user means config.bind(UserMeanBaseline.class,
		 * ItemScorer.class) .to(ItemMeanRatingItemScorer.class); // and
		 * normalize ratings by baseline prior to computing similarities
		 * config.bind(UserVectorNormalizer.class)
		 * .to(BaselineSubtractingUserVectorNormalizer.class);
		 * config.bind(MinNeighbors.class);
		 */

		// There are more parameters, roles, and components that can be set. See
		// the
		// JavaDoc for each recommender algorithm for more information.

		// Now that we have a configuration, build a recommender engine from the
		// configuration
		// and data source. This will compute the similarity matrix and return a
		// recommender
		// engine that uses it.
		LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);

		// Finally, get the recommender and use it.
		try (LenskitRecommender rec = engine.createRecommender()) {
			// we want to recommend items
			ItemRecommender irec = rec.getItemRecommender();
			assert irec != null; // not null because we configured one

			// for users
			for (long user : users) {
				// get 10 recommendation for the user
				ResultList recs = irec.recommendWithDetails(user, 10, null, null);
				System.out.format("Recommendations for user %d:\n", user);
				for (Result item : recs) {
					String name = names.getItemName(item.getId());
					System.out.format("\t%d (%s): %.2f\n", item.getId(), name, item.getScore());
				}
			}
		}

	}

}
