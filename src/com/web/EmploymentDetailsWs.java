package com.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.beans.Employment;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/employment")
public class EmploymentDetailsWs {

	String query="";
	@GET
	@Path("previous")
	@Produces("text/plain")
	@WebMethod(operationName = "previous")
	public String getPreviousDetails(@QueryParam("email") String email)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Employment> accountList= new ArrayList<Employment>();
		try {
			connection = database.getConnection();
			query="SELECT EMPLOYMENT_START_DATE,EMPLOYMENT_END_DATE,INCOME,EMPLOYER FROM EMPLOYMENT_DETAILS WHERE EMPLOYMENT_END_DATE IS NOT NULL AND EMAIL=?";
			st=connection.prepareStatement(query);
			st.setString(1, email);
			rs=st.executeQuery();
			while (rs.next()) {
				Employment ac=new Employment();
				ac.setStartDate(rs.getString("EMPLOYMENT_START_DATE"));
				ac.setEndDate(rs.getString("EMPLOYMENT_END_DATE"));
				ac.setIncome("£"+rs.getString("INCOME"));
				ac.setEmployer(rs.getString("EMPLOYER"));
				accountList.add(ac);
				json=new Gson().toJson(accountList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			try {
				connection.commit();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}
	
	String query1="";
	@GET
	@Path("current")
	@Produces("text/plain")
	@WebMethod(operationName = "current")
	public String getCurrentDetails(@QueryParam("email") String email)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Employment> accountList= new ArrayList<Employment>();
		try {
			connection = database.getConnection();
			query1="SELECT EMPLOYMENT_START_DATE,INCOME,EMPLOYER FROM EMPLOYMENT_DETAILS WHERE EMPLOYMENT_END_DATE IS NULL AND EMAIL=?";
			st=connection.prepareStatement(query1);
			st.setString(1, email);
			rs=st.executeQuery();
			while (rs.next()) {
				Employment ac=new Employment();
				ac.setStartDate(rs.getString("EMPLOYMENT_START_DATE"));
				ac.setType("Current");
				ac.setIncome("£"+rs.getString("INCOME"));
				ac.setEmployer(rs.getString("EMPLOYER"));
				accountList.add(ac);
				json=new Gson().toJson(accountList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			try {
				connection.commit();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}
	
}
