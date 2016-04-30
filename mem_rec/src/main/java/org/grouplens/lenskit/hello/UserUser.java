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
import org.lenskit.LenskitConfiguration;
import org.lenskit.config.ConfigHelpers;
import org.lenskit.data.dao.EventDAO;
import org.lenskit.data.dao.ItemNameDAO;
import org.lenskit.data.dao.MapItemNameDAO;
import org.lenskit.data.dao.UserDAO;
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

import it.unimi.dsi.fastutil.longs.LongSet;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Demonstration app for LensKit. This application builds an item-item CF model
 * from a CSV file, then generates recommendations for a user.
 *
 * Usage: java org.grouplens.lenskit.hello.HelloLenskit ratings.csv user
 */

public class UserUser implements Runnable {
	public static void main(String[] args) throws SQLException, WriteException, IOException {

		 WriteExcel test = new WriteExcel();
		    test.setOutputFile("c:/temp/lars.xls");
		    test.write();
		    System.out
		        .println("Please check the result file under c:/temp/lars.xls ");
		 
		    
		
		
		UserUser rec = new UserUser(args);

		try {
			rec.run();
		} catch (RuntimeException e) {
			cxn.close();
			System.err.println(e.toString());
			e.printStackTrace(System.err);
			System.exit(1);
		} finally {
			cxn.close();
		}
	}

	private String delimiter = "\t";
	private File inputFile = new File("data/sampledata/ratings.csv");
	private File movieFile = new File("data/sampledata/movies.csv");
	private List<Long> users;
	private static Connection cxn;
	private int datasetType;
	private static Connection cxn2;
	
	public UserUser(String[] args) {
		users = new ArrayList<Long>(args.length);
		
		config();
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

	public void test()
	{
		UserIdLookup Ids = null;
		
		try {
			Connection cxn2 = ConnectionManager.getConnectionPostGresql();
			Ids = new UserIdLookup(cxn2,datasetType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		users = Ids.getUserIds();
		
		System.out.println("There are "+ users.size()+" users in this model,,");

		for (Long long1 : users) {
			System.out.println(long1);
		}
	}
	
	
	public void run() {
		
		JDBCRatingDAO dao = new JDBCRatingDAO(this.cxn, new BasicStatementFactory_Postgresql(datasetType));

		

		// EventDAO dao = TextEventDAO.create(inputFile,
		// Formats.movieLensLatest());

		ItemNameDAO names;
		test();
		
		try {
			names = new ItemNameLookup(cxn2, datasetType);
		} catch ( SQLException e) {
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

		LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);

		// Finally, get the recommender and use it.
		try (LenskitRecommender rec = engine.createRecommender()) {
			// we want to recommend items
			ItemRecommender irec = rec.getItemRecommender();
			assert irec != null; // not null because we configured one

			double sum = 0;
			// for users
			for (long user : users) {
				// get 10 recommendation for the user
				long startTime = System.nanoTime();
				ResultList recs = irec.recommendWithDetails(user, 10, null, null);
				System.out.format("Recommendations for user %d:\n", user);
				for (Result item : recs) {
					String name = names.getItemName(item.getId());
					System.out.format("\t%d (%s): %.2f\n", item.getId(), name, item.getScore());

				}
				long endTime = System.nanoTime();
				

				long duration = (endTime - startTime) / 1000000; 
				sum+=duration;
				System.out.println("--------------------------------------------");
				System.out.println("User " + user + " recommendition generated in " + duration + " ms");
				System.out.println("--------------------------------------------");
			}
			
			System.out.println("--------------------------------------------");
			System.out.println("Avg User time to recommendition generated in " + sum/users.size() + " ms");
			System.out.println("--------------------------------------------");
		
		}
		
	}
	
	
}
