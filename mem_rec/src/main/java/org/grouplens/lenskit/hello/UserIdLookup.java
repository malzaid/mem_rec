package org.grouplens.lenskit.hello;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


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

		//System.out.println(sql);
		ArrayList<Long> users = new ArrayList<Long>();

		try {
			ResultSet results = query.executeQuery(sql);
			while (results.next()) {
				users.add(Long.parseLong(results.getString(1)));
				//System.out.println(results.getString(1));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

}
