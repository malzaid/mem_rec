package org.grouplens.lenskit.hello;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.lenskit.data.dao.ItemNameDAO;
import org.lenskit.data.dao.UserDAO;
import org.lenskit.results.Results;

import it.unimi.dsi.fastutil.longs.LongSet;

public class UserIdLookup implements UserDAO {

	private Connection cxn;
	private int i;
	private String userColumn = "userId";
	private String tableName = "ratings";
	private Statement query;

	
	
	public UserIdLookup(Connection cxn, int i) throws SQLException {
		this.cxn = cxn;
		this.i = i;
		query = this.cxn.createStatement();
		
		
		if (i == 0){// basic 100k
			this.tableName = "ratings_100k";
		} else if (i == 1) {// 1 million 
			this.tableName = "ratings_1m";
		}else if (i == 2) {// 20 million 
			this.tableName = "ratings_20m";
		}

	}

	@Override
	public LongSet getUserIds() {
		// TODO Auto-generated method stub
		String sql = String.format("SELECT DISTINCT %s FROM %s", userColumn, tableName);
		//String sql2 = String.format("SELECT Count(DISTINCT %s) FROM %s", userColumn, tableName);
		
		LongSet users = null;
		try {
			int i=0;
			ResultSet results = query.executeQuery(sql);
			
			//results = query.executeQuery(sql);
			while(! results.isAfterLast())
			{
				users.add(Long.parseLong(results.getString(1)));
				results.next();

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return users;
	}

}
