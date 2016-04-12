/*
 * Connection manager that will handle all the DB connections. 
 * 
 * 
 * 
 */
package org.grouplens.lenskit.hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection con;
   
    //VoltDB
    public static Connection getConnectionVoltDB() throws SQLException {
    	String driver = "org.voltdb.jdbc.Driver";
		String url = "jdbc:voltdb://en4102945l.cidse.dhcp.asu.edu:21212";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		con = DriverManager.getConnection(url);
		
        return con;
    }
    // postgres
    public static Connection getConnectionPostGresql() throws SQLException {
    	String url = "jdbc:postgresql://en4102945l.cidse.dhcp.asu.edu:5432/data_mnist";
		// postgres connections
		
		con = DriverManager
	            .getConnection(url,
	            "postgres", "12akil");
        return con;
    }
    
}