package com.howtodoinjava.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.Row;
import com.howtodoinjava.demo.model.EmployeeVO;
import com.howtodoinjava.demo.service.ApplicationProperties;
import com.microsoft.azure.documentdb.Database;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

	@Autowired
	public DataSource myDataSource;

	@Autowired
	ApplicationProperties properties;
	/*
	 * @Autowired public DataSource oracleDataSource;
	 */
	@Autowired
	CassandraUtils cassandraUtils;

	@Autowired
	public DataSource mySQLDataSource;

	@Autowired
	public DataSource cosmosDataSource;

	public List<EmployeeVO> getAllEmployees() {
		List<EmployeeVO> employees = new ArrayList<EmployeeVO>();
		Connection connection = null;

		try {

			connection = myDataSource.getConnection();

			if (null != connection) {
				System.out.println("Connection established successfully");
				try {

					Statement statement = connection.createStatement();
					ResultSet results = statement.executeQuery("SELECT * from employee;");
					while (results.next()) {
						EmployeeVO vo = new EmployeeVO();
						vo.setId(results.getInt("id"));
						vo.setFirstName(results.getString("firstname"));
						vo.setLastName(results.getString("lastname"));
						employees.add(vo);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return employees;
	}

	public Object getOraclePlayers() {
		List<EmployeeVO> employees = new ArrayList<EmployeeVO>();
		Connection connection = null;

		try {

			Class.forName("oracle.jdbc.OracleDriver");

			connection = DriverManager.getConnection(properties.getOracleurl(), properties.getOracleUserName(),
					properties.getOraclePassword());

			if (null != connection) {
				System.out.println("Connection established successfully");
				try {

					Statement statement = connection.createStatement();
					ResultSet results = statement.executeQuery("SELECT * from players order by id");
					while (results.next()) {
						EmployeeVO vo = new EmployeeVO();
						vo.setId(results.getInt("id"));
						vo.setFirstName(results.getString("firstname"));
						vo.setLastName(results.getString("lastname"));
						employees.add(vo);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return employees;

	}

	public Object getMySQLPlayers() {

		List<EmployeeVO> employees = new ArrayList<EmployeeVO>();
		Connection connection = null;

		try {

			Class.forName("org.mariadb.jdbc.Driver");

			connection = DriverManager.getConnection(properties.getMysqlurl(), properties.getMysqlusername(),
					properties.getMysqlpassword());

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
						employees.add(vo);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return employees;

	}

	/*
	 * public Object getCosmosPlayers() { List<EmployeeVO> employees = new
	 * ArrayList<EmployeeVO>(); //String host =
	 * "https://1169cosmosdbsql.documents.azure.com:443/"; //String MASTER_KEY =
	 * "uW8iHnCwj7OOEdLctj5CZqYJ4I30KiOfQoeGdOnjIM1fLhIkqOwcuu8HF6PvjxTQRfjf9BtZa0LZ50AhN3KVAQ==";
	 * 
	 * DocumentClient documentClient = new
	 * DocumentClient(properties.getConsmosDbUri(), properties.getCosmosDbKey(),
	 * ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
	 * 
	 * if(null!=documentClient) {
	 * System.out.println("EmployeeDAOImpl.getCosmosPlayers() connection successful"
	 * );
	 * 
	 * List<Database> databaseList = documentClient .queryDatabases(
	 * "SELECT * FROM root r" , null).getQueryIterable().toList();
	 * 
	 * if(null!=databaseList) {
	 * System.out.println("EmployeeDAOImpl.getCosmosPlayers() "+
	 * databaseList.size()); }
	 * 
	 * Iterator<Database> iterator = databaseList.iterator();
	 * while(iterator.hasNext()) { Database d = iterator.next();
	 * System.out.println("database id "+d.getId()); }
	 * 
	 * }
	 * 
	 * return employees;
	 * 
	 * }
	 */

	// Cassandra Table API
	public Object getCosmosPlayers() throws Exception {
		List<EmployeeVO> employees = new ArrayList<EmployeeVO>();

		final String query = "SELECT * FROM team1.players";
		if(null!=cassandraUtils) {
			List<Row> rows = cassandraUtils.getSession().execute(query).all();
			for (Row row : rows) {
				EmployeeVO vo = new EmployeeVO();
				vo.setId(row.getInt("id"));
				vo.setFirstName(row.getString("firstname"));
				vo.setLastName(row.getString("lastname"));
				employees.add(vo);
			}
		}
		

		

		return employees;

	}
}