package SocialMedia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
	
	public Connection getConnection() throws Exception
	{
	try
	{
	Connection connection = null;
	Class.forName("org.hsqldb.jdbcDriver");
	connection = DriverManager.getConnection("jdbc:hsqldb:http://localhost", "sa", "");
	return connection;
	}
	catch (SQLException e)
	{
	throw e;
	}
	catch (Exception e)
	{
	throw e;
	}

	}
}
