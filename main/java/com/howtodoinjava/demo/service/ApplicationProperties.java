package com.howtodoinjava.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mcs-dev.properties")
public class ApplicationProperties {

	@Value( "${db.cosmos.uri}" )
	private String consmosDbUri;

	@Value( "${db.cosmos.key}" )
	private String cosmosDbKey;
	
	@Value( "${cassandra_host}" )
	private String cassandraHost;
	
	@Value( "${cassandra_port}" )
	private String cassandraPort;
	
	@Value( "${cassandra_username}" )
	private String cassandraUserName;
	
	@Value( "${cassandra_password}" )
	private String cassandraKey;
	
	@Value( "${mysql.database.url}" )
	private String mysqlurl;
	
	@Value( "${mysql.database.username}" )
	private String mysqlusername;
	
	@Value( "${mysql.database.password}" )
	private String mysqlpassword;
	
	@Value( "${oracle.database.url}" )
	private String oracleurl;
	
	@Value( "${oracle.database.username}" )
	private String oracleUserName;
	
	@Value( "${oracle.database.password}" )
	private String oraclePassword;
	
	

	public String getOracleurl() {
		return oracleurl;
	}
	public String getOracleUserName() {
		return oracleUserName;
	}
	public String getOraclePassword() {
		return oraclePassword;
	}
	public String getMysqlusername() {
		return mysqlusername;
	}
	public String getMysqlurl() {
		return mysqlurl;
	}

	public String getMysqlpassword() {
		return mysqlpassword;
	}

	public String getCassandraHost() {
		return cassandraHost;
	}

	public String getCassandraPort() {
		return cassandraPort;
	}

	public String getCassandraUserName() {
		return cassandraUserName;
	}

	public String getCassandraKey() {
		return cassandraKey;
	}

	public String getConsmosDbUri() {
		return consmosDbUri;
	}

	public String getCosmosDbKey() {
		return cosmosDbKey;
	}
	
	
}
