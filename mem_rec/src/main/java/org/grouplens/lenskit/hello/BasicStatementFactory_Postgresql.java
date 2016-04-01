package org.grouplens.lenskit.hello;

import org.grouplens.lenskit.data.sql.BasicSQLStatementFactory;
import org.grouplens.lenskit.data.sql.SQLStatementFactory;
import org.lenskit.data.dao.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicStatementFactory_Postgresql extends BasicSQLStatementFactory {
	
	private static final Logger logger =
            LoggerFactory.getLogger(BasicSQLStatementFactory.class);
	
	private String tableName = "ratings";
	private String userColumn = "userId";
	private String itemColumn = "movieId";

	@Override
	public String prepareUsers() {
		return String.format("SELECT DISTINCT %s FROM %s", userColumn, tableName);
	}

	@Override
	public String prepareItems() {
		return String.format("SELECT DISTINCT %s FROM %s", itemColumn, tableName);
	}

	@Override
	public String prepareEvents(SortOrder order) {
		// TODO Auto-generated method stub
		StringBuilder query = new StringBuilder();
		rqAddSelectFrom(query);
		rqAddOrder(query, order);
		rqFinish(query);
		logger.debug("Rating query: {}", query);
		return query.toString();
	}

	@Override
	public String prepareUserEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prepareItemEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prepareItemUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}
