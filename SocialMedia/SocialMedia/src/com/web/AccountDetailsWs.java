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

import com.beans.Account;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/account")
public class AccountDetailsWs {

	String query="";
	@GET
	@Path("details")
	@Produces("text/plain")
	@WebMethod(operationName = "details")
	public String getAccountDetails(@QueryParam("email") String email)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Account> accountList= new ArrayList<Account>();
		int i=1;
		try {
			connection = database.getConnection();
			query="SELECT ACCOUNT_TYPE,BALANCE FROM ACCOUNT_DETAILS WHERE EMAIL=?";
			st=connection.prepareStatement(query);
			st.setString(1, email);
			rs=st.executeQuery();
			while(rs.next())
			{
				Account ac=new Account();
				ac.setCount(i);
				ac.setAccountType(rs.getString("ACCOUNT_TYPE"));
				ac.setBalance("£"+rs.getString("BALANCE"));
				accountList.add(ac);
				json=new Gson().toJson(accountList);
				i++;
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
