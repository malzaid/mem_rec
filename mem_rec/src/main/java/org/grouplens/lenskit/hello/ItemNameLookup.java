package org.grouplens.lenskit.hello;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.lenskit.data.dao.ItemNameDAO;
import org.lenskit.results.Results;

public class ItemNameLookup implements ItemNameDAO {

	private Connection cxn;
	private int i;
	private String userColumn = "title";
	private String tableName = "movies";
	private String constraintColumn = "movieid";
	private Statement query;

	public ItemNameLookup(Connection cxn, int i) throws SQLException {
		this.cxn = cxn;
		this.i = i;
		if (i == 0){// basic 100k
			this.tableName = "movies_100k";
		} else if (i == 1) {// 1 million 
			this.tableName = "movies_1m";
		}else if (i == 2) {// 20 million 
			this.tableName = "movies_20m";
		}
		query = this.cxn.createStatement();
	}

	@Override
	public String getItemName(long item) {
		
		
		String sql = String.format("SELECT %s FROM %s WHERE %s = %s", userColumn, tableName,constraintColumn,item);
		
		String title = null;
		try {
			ResultSet results = query.executeQuery(sql);
			results.next();
			title  =  results.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return title;
	}

}
