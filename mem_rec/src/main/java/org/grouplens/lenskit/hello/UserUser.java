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
	private int databaseType;
	private int algorithm;
	private String algo;
	private static Connection cxn2;

	public UserUser(String[] args) {
		users = new ArrayList<Long>(args.length);

		config();
	}

	private void config() {

		// 1 - PostGresql
		// 2 - MonetDB
		// 3 - VoltDB
		this.databaseType = 2;

		// 0 - basic 100k
		// 1 - 1 million
		// 2 - 20 million
		this.datasetType = 0;

		// 1 - item-item
		// 2 - user-user
		this.algorithm = 1;
		//this.algo = "etc/item-item.groovy";
		//this.algo = "etc/User-User.groovy";
		this.algo = "etc/SVD.groovy";
		
		// postgres connections
		try {

			switch (databaseType) {
			case 1:
				cxn = ConnectionManager.getConnectionPostGresql();
				cxn2 = ConnectionManager.getConnectionPostGresql();
				break;
			case 2:
				cxn = ConnectionManager.getConnectionMonetDb();
				cxn2 = ConnectionManager.getConnectionMonetDb();
				break;
			case 3:
				cxn = ConnectionManager.getConnectionVoltDB();
				cxn2 = ConnectionManager.getConnectionVoltDB();
				break;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void test() {
		UserIdLookup Ids = null;

		try {
			// Connection cxn2 = ConnectionManager.getConnectionPostGresql();
			Ids = new UserIdLookup(cxn2, datasetType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		users = Ids.getUserIds();

		System.out.println("There are " + users.size() + " users in this model,,");

		// testing
		//users.clear();
		//users.add((long) 438);
		//users.add((long) 72);
		//users.add((long) 20);
		// for (Long long1 : users) {
		// System.out.println(long1);
		// }
	}

	public void run() {

		JDBCRatingDAO dao = new JDBCRatingDAO(this.cxn, new BasicStatementFactory_Postgresql(datasetType));

		// EventDAO dao = TextEventDAO.create(inputFile,
		// Formats.movieLensLatest());

		ItemNameDAO names;
		test();

		try {
			names = new ItemNameLookup(cxn2, datasetType);
		} catch (SQLException e) {
			throw new RuntimeException("cannot load names", e);
		}

		// Next: load the LensKit algorithm configuration
		LenskitConfiguration config = null;
		try {
			config = ConfigHelpers.load(new File(algo));
		} catch (IOException e) {
			throw new RuntimeException("could not load configuration", e);
		}
		// Add our data component to the configuration
		config.addComponent(dao);
		long startTime = System.nanoTime();

		LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);
		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000;

		System.out.println("time to build model: " + duration);
		// Finally, get the recommender and use it.
		try (LenskitRecommender rec = engine.createRecommender()) {
			// we want to recommend items

			ItemRecommender irec = rec.getItemRecommender();

			assert irec != null; // not null because we configured one

			double sum = 0;
			long[] userID = new long[users.size()];
			long[] time = new long[users.size()];
			int i = 0;
			// for users
			
			
			long startThroughputTime = System.nanoTime();

			for (long user : users) {
				// get 10 recommendation for the user
				startTime = System.nanoTime();
				ResultList recs = irec.recommendWithDetails(user, 10, null, null);
				//System.out.format("Recommendations for user %d:\n", user);
				for (Result item : recs) {
					//String name = names.getItemName(item.getId());
					//System.out.format("\t%d (%s): %.2f\n", item.getId(), name, item.getScore());
				}
				endTime = System.nanoTime();

				duration = (endTime - startTime) / 1000000;
				sum += duration;
				userID[i] = user;
				time[i] = duration;
				i++;
				
				System.out.print(".");
				//limit the size for 1000
				//if(i==999)
				//	break;
				
				
				//throughput break
				if((((System.nanoTime() - startThroughputTime))/1000000000)/60 >=5){
					System.out.println("Times Up");
					break;
					}
				//System.out.println("--------------------------------------------");
				//System.out.println("User " + user + " recommendition generated in " + duration + " ms");
				//System.out.println("--------------------------------------------");
			}

			long endThroughputTime = System.nanoTime();

			duration = (endThroughputTime - startThroughputTime) / 1000000;
			
			WriteExcel test = new WriteExcel();

			test.setOutputFile(filename());
			test.write(userID, time, datasetType, databaseType, algorithm);
			System.out.println("Please check the result file under results.xls ");

			
			//limit the size to 1000
			//System.out.println("--------------------------------------------");
			//System.out.println("Avg User time to recommendition generated in " + sum / 1000 + " ms");
			//System.out.println("--------------------------------------------");
			
			
			System.out.println("--------------------------------------------");
			System.out.println("Avg User time to recommendition generated in " + sum / i + " ms");
			System.out.println("--------------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String filename() {
		String name = "results_";
		if (algorithm == 1)
			name += "Item_";
		else
			name += "User_";

		switch (databaseType) {
		case 1:
			name += "Postgre_";
			break;

		case 2:
			name += "MonetDB_";

			break;

		case 3:
			name += "VoltDB_";
			break;

		default:
			break;
		}

		switch (datasetType) {
		case 0:
			name += "100K_";
			break;

		case 1:
			name += "1 Mil_";
			break;
		case 2:
			name += "20 Mil_";
			break;
		default:
			break;
		}

		name += "limited5min_Run2.xls";
		return name;
	}
}
