package com.howtodoinjava.demo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.JdkSSLOptions;
import com.datastax.driver.core.RemoteEndpointAwareJdkSSLOptions;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.howtodoinjava.demo.model.EmployeeVO;
import com.howtodoinjava.demo.service.ApplicationProperties;

/**
 * Cassandra utility class to handle the Cassandra Sessions
 */
@Component
public class CassandraUtils {

	private Cluster cluster;

	@Autowired
	ApplicationProperties properties;

	private String cassandraHost = "127.0.0.1";
	private int cassandraPort = 10350;
	private String cassandraUsername = "localhost";
	private String cassandraPassword = "defaultpassword";
	private File sslKeyStoreFile = null;
	private String sslKeyStorePassword = "changeit";

	public static void main(String args[]) throws Exception {
		CassandraUtils utils = new CassandraUtils();
		// System.out.println(utils.getSession());
		final String query = "SELECT * FROM team1.players";

		/*
		 * Session session = Cluster.builder().addContactPoint(
		 * "mcs-preprojc-cosmosdb.cassandra.cosmos.azure.com") .withPort(10350)
		 * .withCredentials("mcs-preprojc-cosmosdb",
		 * "TpNv824jjMohYqGrhp8vfwQ0dKM9HRhpSOrjQ1GjE6Nmgxl0xhicGHo2RfyjXGF3pSCSNVitVilJOHSbIU4lGw=="
		 * ).build().connect();
		 */

		List<Row> rows = utils.getSession().execute(query).all();

		for (Row row : rows) {
			EmployeeVO vo = new EmployeeVO();
			vo.setId(row.getInt("id"));
			vo.setFirstName(row.getString("firstname"));
			vo.setLastName(row.getString("lastname"));
			System.out.println(vo.getLastName());
		}

	}

	/**
	 * This method creates a Cassandra Session based on the the end-point details
	 * given in config.properties. This method validates the SSL certificate based
	 * on ssl_keystore_file_path & ssl_keystore_password properties. If
	 * ssl_keystore_file_path & ssl_keystore_password are not given then it uses
	 * 'cacerts' from JDK.
	 * 
	 * @return Session Cassandra Session
	 * @throws Exception 
	 */
	public Session getSession() throws Exception {

			// Load cassandra endpoint details from config.properties

			loadCassandraConnectionDetails();

			final KeyStore keyStore = KeyStore.getInstance("JKS");
			try (final InputStream is = new FileInputStream(sslKeyStoreFile)) {
				keyStore.load(is, sslKeyStorePassword.toCharArray());
			}

			final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, sslKeyStorePassword.toCharArray());
			final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);

			// Creates a socket factory for HttpsURLConnection using JKS contents. final
			SSLContext sc = SSLContext.getInstance("TLSv1.2");
			sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

			JdkSSLOptions sslOptions = RemoteEndpointAwareJdkSSLOptions.builder().withSSLContext(sc).build();

			cluster = Cluster.builder().addContactPoint("mcs-preprojc-cosmosdb.cassandra.cosmos.azure.com")
					.withPort(10350)
					.withCredentials("mcs-preprojc-cosmosdb",
							"TpNv824jjMohYqGrhp8vfwQ0dKM9HRhpSOrjQ1GjE6Nmgxl0xhicGHo2RfyjXGF3pSCSNVitVilJOHSbIU4lGw==")
					.withSSL(sslOptions).build();

			Session session = cluster.connect();

			System.out.println(session.isClosed());

			return session;
			
	}

	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * Closes the cluster and Cassandra session
	 */
	public void close() {
		cluster.close();
	}

	/**
	 * Loads Cassandra end-point details from config.properties.
	 * 
	 * @throws Exception
	 */
	private void loadCassandraConnectionDetails() throws Exception {

		/*
		 * cassandraHost = properties.getCassandraHost(); cassandraPort =
		 * Integer.parseInt(properties.getCassandraPort()); cassandraUsername =
		 * properties.getCassandraUserName(); cassandraPassword =
		 * properties.getCassandraKey();
		 */

		cassandraHost = "mcs-preprojc-cosmosdb.cassandra.cosmos.azure.com";
		cassandraPort = Integer.parseInt("10350");
		cassandraUsername = "mcs-preprojc-cosmosdb";
		cassandraPassword = "TpNv824jjMohYqGrhp8vfwQ0dKM9HRhpSOrjQ1GjE6Nmgxl0xhicGHo2RfyjXGF3pSCSNVitVilJOHSbIU4lGw==";

		String ssl_keystore_file_path = null;
		String ssl_keystore_password = null;

		// If ssl_keystore_file_path, build the path using JAVA_HOME directory.
		if (ssl_keystore_file_path == null || ssl_keystore_file_path.isEmpty()) {
			String javaHomeDirectory = System.getenv("JAVA_HOME");
			if (javaHomeDirectory == null || javaHomeDirectory.isEmpty()) {
				throw new Exception("JAVA_HOME not set");
			}
			ssl_keystore_file_path = new StringBuilder(javaHomeDirectory).append("/lib/security/cacerts")
					.toString();
		}

		sslKeyStorePassword = (ssl_keystore_password != null && !ssl_keystore_password.isEmpty())
				? ssl_keystore_password
				: sslKeyStorePassword;

		sslKeyStoreFile = new File(ssl_keystore_file_path);

		if (!sslKeyStoreFile.exists() || !sslKeyStoreFile.canRead()) {
			throw new Exception(
					String.format("Unable to access the SSL Key Store file from %s", ssl_keystore_file_path));
		}
	}
}