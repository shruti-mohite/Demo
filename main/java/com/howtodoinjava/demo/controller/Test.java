package com.howtodoinjava.demo.controller;

import java.sql.*;
import java.util.Properties;

import com.howtodoinjava.demo.model.EmployeeVO;

public class Test {

	public static void main (String[] args)  throws Exception
	{

		// Initialize connection variables.
		String host = "mcspostgres.postgres.database.azure.com";
		String database = "postgres";
		String user = "mcsadmin@mcspostgres";
		String password = "P@ssw0rd";

		// check that the driver is installed
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			throw new ClassNotFoundException("PostgreSQL JDBC driver NOT detected in library path.", e);
		}

		System.out.println("PostgreSQL JDBC driver detected in library path.");

		Connection connection = null;

		// Initialize connection object
		try
		{
			/*
			 * String url = String.format("jdbc:postgresql://%s/%s", host, database);
			 * 
			 * String url = String.format(
			 * "jdbc:mysql://fabrikamysql.mysql.database.azure.com:3306/fabrikamdb?verifyServerCertificate=true&useSSL=true&requireSSL=false"
			 * );
			 * 
			 * // set up the connection properties Properties properties = new Properties();
			 * properties.setProperty("user", user); properties.setProperty("password",
			 * password); properties.setProperty("ssl", "disable");
			 */

			// get connection
			
			//String url ="jdbc:mysql://preproject.mysql.database.azure.com:3306/players?useSSL=true&requireSSL=false"; 
			
			String url = "jdbc:mysql://team2sourcemysqlvm.westus.cloudapp.azure.com:3306/players?useSSL=true&requireSSL=false";
			
			//connection = DriverManager.getConnection(url, "mcs@preproject","test@1234");
			connection = DriverManager.getConnection(url, "root","Mysqlsrc@123");
			if (null != connection) {
				System.out.println("Connection established successfully");
				try {

					Statement statement = connection.createStatement();
					ResultSet results = statement.executeQuery("SELECT * from players;");
					while (results.next()) {
						EmployeeVO vo = new EmployeeVO();
						vo.setId(results.getInt("id"));
						vo.setFirstName(results.getString("firstname"));
						vo.setLastName(results.getString("lastname"));
						String outputString = String.format("Data row = (%s, %s, %s)", results.getString(1),
								results.getString(2), results.getString(3));
						System.out.println(outputString);
					}
				} catch (SQLException e) {
					throw new SQLException("Encountered an error when executing given sql statement.", e);
				}
			} else {
				System.out.println("Failed to establish connection to the database");
			}
			
			System.out.println(connection);
		}
		catch (SQLException e)
		{
			throw new SQLException("Failed to create connection to database.", e);
		}
		if (connection != null) 
		{
			/*
			 * System.out.println("Successfully created connection to database.");
			 * 
			 * // Perform some SQL queries over the connection. try { // Drop previous table
			 * of same name if one exists. Statement statement =
			 * connection.createStatement();
			 * statement.execute("DROP TABLE IF EXISTS employee;");
			 * System.out.println("Finished dropping table (if existed).");
			 * 
			 * // Create table. statement.
			 * execute("CREATE TABLE employee (id serial PRIMARY KEY, firstname VARCHAR(50), lastname VARCHAR(50));"
			 * ); System.out.println("Created table.");
			 * 
			 * // Insert some data into table. int nRowsInserted = 0; PreparedStatement
			 * preparedStatement = connection.
			 * prepareStatement("INSERT INTO employee (firstname, lastname) VALUES (?, ?);"
			 * ); preparedStatement.setString(1, "Kevin"); preparedStatement.setString(2,
			 * "Durant"); nRowsInserted += preparedStatement.executeUpdate();
			 * 
			 * preparedStatement.setString(1, "Stephen"); preparedStatement.setString(2,
			 * "Curry"); nRowsInserted += preparedStatement.executeUpdate();
			 * 
			 * preparedStatement.setString(1, "Paul"); preparedStatement.setString(2,
			 * "George"); nRowsInserted += preparedStatement.executeUpdate();
			 * 
			 * preparedStatement.setString(1, "Kawhi"); preparedStatement.setString(2,
			 * "Leonard"); nRowsInserted += preparedStatement.executeUpdate();
			 * System.out.println(String.format("Inserted %d row(s) of data.",
			 * nRowsInserted));
			 * 
			 * // NOTE No need to commit all changes to database, as auto-commit is enabled
			 * by default.
			 * 
			 * } catch (SQLException e) { throw new
			 * SQLException("Encountered an error when executing given sql statement.", e);
			 * }
			 */}
		else {
			System.out.println("Failed to create connection to database.");
		}
		System.out.println("Execution finished.");
	}
}