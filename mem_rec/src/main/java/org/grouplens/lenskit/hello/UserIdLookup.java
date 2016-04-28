package org.grouplens.lenskit.hello;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.lenskit.data.dao.ItemNameDAO;
import org.lenskit.data.dao.UserDAO;
import org.lenskit.results.Results;

import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;

public class UserIdLookup {

	private Connection cxn;
	private int i;
	private String tableName = "ratings";
	private String userColumn = "userid";
	private Statement query;

	public UserIdLookup(Connection cxn, int i) throws SQLException {
		this.cxn = cxn;
		this.i = i;
		query = this.cxn.createStatement();

		if (i == 0) {// basic 100k
			this.tableName = "ratings_100k";
		} else if (i == 1) {// 1 million
			this.tableName = "ratings_1m";
		} else if (i == 2) {// 20 million
			this.tableName = "ratings_20m";
		}

	}

	public ArrayList<Long> getUserIds() {
		// TODO Auto-generated method stub
		String sql = String.format("SELECT DISTINCT %s FROM %s", userColumn, tableName);
		// String sql2 = String.format("SELECT COUNT(DISTINCT %s) FROM %s",
		// userColumn, tableName);

		ArrayList<Long> users = new ArrayList<Long>();

		try {
			ResultSet results = query.executeQuery(sql);
			results.next();
			while (!results.isAfterLast()) {
				users.add(Long.parseLong(results.getString(1)));
				results.next();
				System.out.println(users.size());

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

}
