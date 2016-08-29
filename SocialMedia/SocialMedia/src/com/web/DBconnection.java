package com.web;

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
	connection = DriverManager.getConnection("jdbc:hsqldb:http://10.18.7.9", "sa", "");
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
