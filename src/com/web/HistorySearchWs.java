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
import com.beans.Candidate;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/history")
public class HistorySearchWs {

	String query="";
	@GET
	@Path("search")
	@Produces("text/plain")
	@WebMethod(operationName = "search")
	public String getHistory()
	{
		String json="[{\"email\":\"No Match Found\"}]";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Candidate> candidateList= new ArrayList<Candidate>();
		try {
			connection = database.getConnection();
			query="SELECT DISTINCT EMAIL,FIRST_NAME,LAST_NAME FROM HISTORY";
			st=connection.prepareStatement(query);
			rs=st.executeQuery();
			while(rs.next())
			{
				Candidate c=new Candidate();
				c.setEmail(rs.getString("EMAIL"));
				c.setFirstname(rs.getString("FIRST_NAME"));
				c.setLastname(rs.getString("LAST_NAME"));
				/*c.setSearchDate(rs.getDate("SEARCH_DATE"));*/
				candidateList.add(c);
			}
			if(!candidateList.isEmpty()){
				json=new Gson().toJson(candidateList);
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
