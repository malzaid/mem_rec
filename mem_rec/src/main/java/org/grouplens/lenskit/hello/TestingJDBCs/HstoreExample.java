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
package org.grouplens.lenskit.hello.TestingJDBCs;

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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstration app for LensKit. This application builds an item-item CF model
 * from a CSV file, then generates recommendations for a user.
 *
 * Usage: java org.grouplens.lenskit.hello.HelloLenskit ratings.csv user
 */
public class HstoreExample implements Runnable {
	public static void main(String[] args) {
		args[0] = "72";

		HstoreExample hello = new HstoreExample(args);
		try {
			hello.run();
		} catch (RuntimeException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	private String delimiter = "\t";
	private File inputFile = new File("data/sampledata/ratings.csv");
	private File movieFile = new File("data/sampledata/movies.csv");
	private List<Long> users;

	public HstoreExample(String[] args) {
		users = new ArrayList<Long>(args.length);
		for (String arg : args) {
			users.add(Long.parseLong(arg));
		}
	}

	public void run() {
		// We first need to configure the data access.
		// We will use a simple delimited file; you can use something else like
		// a database (see JDBCRatingDAO).
		EventDAO dao = TextEventDAO.create(inputFile, Formats.movieLensLatest());
		
//		Connection cxn = /* open JDBC connection */;
//		JDBCRatingDAO dao2 = new JDBCRatingDAO(cxn, new BasicStatementFactory());
//		
		
		ItemNameDAO names;
		try {
			names = MapItemNameDAO.fromCSVFile(movieFile, 1);
		} catch (IOException e) {
			throw new RuntimeException("cannot load names", e);
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
