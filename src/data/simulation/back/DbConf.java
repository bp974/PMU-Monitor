package data.simulation.back;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 * Immutable data structure that contains database configuration information.
 *
 * Has a method getPropValues that will return a new DbConf object with
 * the values obtained from the .properties file or null if something went wrong
 *
 * @author Brett Pelkey
 *
 */
public class DbConf {

	public final String url;
	public final String userName;
	public final String password;

	/**
	 * Constructor to set all values to null until the config file has been read
	 */
	public DbConf() {
		url = null;
		userName = null;
		password = null;
	}

	/**
	 * @param url
	 *            the string to connect to the database
	 * @param userName
	 *            the db username
	 * @param password
	 *            the db password
	 */
	public DbConf(final String url, final String userName, final String password) {
		assert (url != null);
		assert (userName != null);
		assert (password != null);

		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * @param filename
	 *            name of the config file which contains all the info for
	 *            connection
	 * @return a new DatabaseConf object if successful; otherwise null
	 */
	public DbConf getPropValues(String filename) {
		String url = null;
		String userName = null;
		String password = null;
		InputStream is = null;
		try {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("<password to decrpyt .properties file>");
			Properties prop = new EncryptableProperties(encryptor);
			// Properties prop = new Properties();
			is = new URL(filename).openStream();
			prop.load(is);
			url = prop.getProperty("url");
			userName = prop.getProperty("username");
			password = prop.getProperty("password");
		} catch (Exception e) {
			System.out.println(e);
			return null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
				return null;
			}
		}
		System.out.println("Finished loading database configuration file...");
		// System.out.println("connectionString: " + url);
		// System.out.println("userName: " + userName);
		// System.out.println("password: " + password); // comment out for
		// security

		if (url == null || userName == null || password == null) {
			System.out.println("Invalid database configuration file.");
			return null;
		}
		return new DbConf(url, userName, password);
	}
}
