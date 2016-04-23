package org.grouplens.lenskit.hello.TestingJDBCs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	         c.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = c.createStatement();
	         
	         ResultSet rs = stmt.executeQuery( "SELECT * FROM ratings" );
	         while ( rs.next() ) {
	            String id = rs.getString("user_id");
	            System.out.println( "ID = " + id );
	            System.out.println();
	         }
	         rs.close();
	         stmt.close();
	         c.close();
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	      
	   }
}
