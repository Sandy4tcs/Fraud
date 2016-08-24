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

import com.beans.Candidate;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/address")
public class AddressDetailsWs {

	String query="";
	@GET
	@Path("previous")
	@Produces("text/plain")
	@WebMethod(operationName = "previous")
	public String getPreviousAddressDetails(@QueryParam("email") String email)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Candidate> addressList=new ArrayList<Candidate>();
		try {
			connection = database.getConnection();
			query="SELECT ADDRESS_LINE1,ADDRESS_LINE2,POSTAL_TOWN,POSTAL_CODE,COUNTRY,RESIDENT_STARTDATE,RESIDENT_ENDDATE FROM ADDRESS_DETAILS WHERE RESIDENT_ENDDATE IS NOT NULL AND EMAIL=?";
			st=connection.prepareStatement(query);
			st.setString(1, email);
			rs=st.executeQuery();
			while(rs.next())
			{
				Candidate c=new Candidate();
				c.setAddress1(rs.getString("ADDRESS_LINE1"));
				c.setAddress2(rs.getString("ADDRESS_LINE2"));
				c.setTown(rs.getString("POSTAL_TOWN"));
				c.setPostalcode(rs.getString("POSTAL_CODE"));
				c.setCountry(rs.getString("COUNTRY"));
				c.setResidentStartDate(rs.getDate("RESIDENT_STARTDATE"));
				c.setResidentEndDate(rs.getDate("RESIDENT_ENDDATE"));
				addressList.add(c);
			}
			json=new Gson().toJson(addressList);
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
