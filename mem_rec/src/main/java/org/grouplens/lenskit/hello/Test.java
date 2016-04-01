package org.grouplens.lenskit.hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Test {
	 public static void main(String args[]) {
	      Connection c = null;
	      
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://en4102945l.cidse.dhcp.asu.edu:5432/data_mnist",
	            "postgres", "12akil");
	         
	         System.out.println("Opened database successfully");
	         stmt = c.createStatement();
	         
	         
	         String sql = "CREATE TABLE COMPANY " +
	                      "(ID INT PRIMARY KEY     NOT NULL," +
	                      " NAME           TEXT    NOT NULL, " +
	                      " AGE            INT     NOT NULL, " +
	                      " ADDRESS        CHAR(50), " +
	                      " SALARY         REAL)";
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.close();
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	      
	   }
}
