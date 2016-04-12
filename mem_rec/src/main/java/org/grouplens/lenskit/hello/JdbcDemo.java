package org.grouplens.lenskit.hello;

import java.sql.*;
import java.io.*;

public class JdbcDemo {

    public static void main(String[] args) {
 
        String driver = "org.voltdb.jdbc.Driver";
        String url = "jdbc:voltdb://en4102945l.cidse.dhcp.asu.edu:21212";
        String sql = "SELECT * from movies";
        
        try {
                // Load driver. Create connection.
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url);
            
                // create a statement
            Statement query = conn.createStatement();
            ResultSet results = query.executeQuery(sql);
            while (results.next()) {
                System.out.println("Language is " + results.getString(1));
            }
            
//                // call a stored procedure
//            CallableStatement proc = conn.prepareCall("{call Select(?)}");
//            proc.setString(1, "French");
//            results = proc.executeQuery();
//            while (results.next()) {
//                System.out.printf("%s, %s!\n", results.getString(1), 
//                                               results.getString(2));
//            }
            
                 //Close statements, connections, etc.
            query.close(); 
//            proc.close();
            results.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}