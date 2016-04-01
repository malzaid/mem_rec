package org.grouplens.lenskit.hello;

import org.grouplens.lenskit.data.sql.SQLStatementFactory;
import org.lenskit.data.dao.SortOrder;

public class BasicStatementFactory implements SQLStatementFactory {

	@Override
	public String prepareUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prepareItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prepareEvents(SortOrder order) {
		// TODO Auto-generated method stub
		return null;
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
