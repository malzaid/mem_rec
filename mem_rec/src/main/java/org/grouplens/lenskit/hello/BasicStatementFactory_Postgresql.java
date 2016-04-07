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
	private String userColumn = "user_id";
	private String itemColumn = "movie_id";
	private String ratingColumn = "rating";
	
	private String timestampColumn = "timestamp";
	
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
		StringBuilder query = new StringBuilder();
        rqAddSelectFrom(query);
        rqAddOrder(query, order);
        rqFinish(query);
        logger.debug("Rating query: {}", query);
        return query.toString();
        
	}

	@Override
	public String prepareUserEvents() {
		StringBuilder query = new StringBuilder();
        rqAddSelectFrom(query);
        query.append(" WHERE ").append(userColumn).append(" = ?");
        rqFinish(query);
        logger.debug("User rating query: {}", query);
        return query.toString();
	}

	@Override
	public String prepareItemEvents() {
		StringBuilder query = new StringBuilder();
        rqAddSelectFrom(query);
        query.append(" WHERE ").append(itemColumn).append(" = ?");
        rqFinish(query);
        logger.debug("Item rating query: {}", query);
        return query.toString();
	}

	@Override
	public String prepareItemUsers() {
		StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT ").append(userColumn)
             .append(" FROM ").append(tableName)
             .append(" WHERE ").append(itemColumn).append(" = ?");
        rqFinish(query);
        logger.debug("Item user query: {}", query);
        return query.toString();
	}

	/**
     * Add the SELECT and FROM clauses to the query.
     *
     * @param query The query accumulator.
     */
    protected void rqAddSelectFrom(StringBuilder query) {
        query.append("SELECT ");
        query.append(userColumn);
        query.append(", ");
        query.append(itemColumn);
        query.append(", ");
        query.append(ratingColumn);
        if (timestampColumn != null) {
            query.append(", ");
            query.append(timestampColumn);
        }
        query.append(" FROM ");
        query.append(tableName);
    }

    /**
     * Add an ORDER BY clause to a query.
     *
     * @param query The query accumulator
     * @param order The sort order.
     */
    protected void rqAddOrder(StringBuilder query, SortOrder order) {
        switch (order) {
        case ANY:
            break;
        case ITEM:
            query.append(" ORDER BY ");
            query.append(itemColumn);
            if (timestampColumn != null) {
                query.append(", ");
                query.append(timestampColumn);
            }
            break;
        case USER:
            query.append(" ORDER BY ").append(userColumn);
            if (timestampColumn != null) {
                query.append(", ");
                query.append(timestampColumn);
            }
            break;
        case TIMESTAMP:
            /* If we don't have timestamps, we return in any order. */
            if (timestampColumn != null) {
                query.append(" ORDER BY ").append(timestampColumn);
            }
            break;
        default:
            throw new IllegalArgumentException("unknown sort order " + order);
        }
    }

    /**
     * Finish a query (append a semicolon).
     *
     * @param query The query accumulator
     */
    protected void rqFinish(StringBuilder query) {
    	
    	
    }

    
}
