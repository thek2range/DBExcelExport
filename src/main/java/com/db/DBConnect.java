package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnect {
	static Connection connection = null;

	public static ResultSet getResultSet( String dbURL, String username, String password,String query ) {
		try {
			// registers Oracle JDBC driver - though this is no longer required
			// since JDBC 4.0, but added here for backward compatibility
			Class.forName("oracle.jdbc.OracleDriver");
			connection = DriverManager.getConnection(dbURL, username, password);
			if (connection != null) {
				System.out.println("INFO : DB Connection Open");
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( query );
			return rs;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	public static void main(String s[])
	{
		String reportFilePath = "C:\\Users\\phalg\\eclipse-workspace\\com.db\\generatedReports";
		ResultSet rs = getResultSet("jdbc:oracle:thin:@localhost:1521/orcl", "your_username", "your_password", "select * from persons");
		try {
			String reportFileName = DataExportService.getReport(rs, reportFilePath, "1234567890999899" );
			if (!reportFileName.isEmpty()) 
				System.out.println("SUCCESS : Report generated at - " + reportFileName);
			else 
				System.out.println("ERROR : Report Not Generated,check configuration." );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("INFO : DB Connection Closed");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
