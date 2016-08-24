package com.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.beans.Candidate;
import com.google.gson.Gson;
import com.web.DBconnection;

@WebService()
@Entity
@Path("/searchcandidate")
public class SearchWs {

	String query="";
	@GET
	@Path("search")
	@Produces("text/plain")
	@WebMethod(operationName = "search")
	public String getCandidate(@QueryParam("email") String email,
			@QueryParam("fname") String fname,
    		@QueryParam("lname") String lname,
    		@QueryParam("dob") String dob,
    		@QueryParam("reportedby") String reportedby,
    		@QueryParam("phoneno") String phoneno)
	{
		String json="[{\"title\":\"No Match Found\"}]";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Candidate> candidateList= new ArrayList<Candidate>();
		try {
			connection = database.getConnection();
			query="SELECT TITLE,EMAIL,FIRST_NAME,LAST_NAME,DOB,REPORTED_BY,RESIDENT_SINCE_MONTH,RESIDENT_SINCE_YEAR,ADDRESS_LINE1,ADDRESS_LINE2,POSTAL_TOWN,POSTAL_CODE,COUNTRY,PHONENO FROM CANDIDATE_DETAILS"+
			" WHERE EMAIL= COALESCE(?,EMAIL)"+
			" AND FIRST_NAME = COALESCE(?,FIRST_NAME)"+
			" AND LAST_NAME = COALESCE(?,LAST_NAME)"+
			" AND DOB = COALESCE(?,DOB)"+
			" AND REPORTED_BY =COALESCE(?,REPORTED_BY)"+
			" AND PHONENO = COALESCE(?,PHONENO)";
			st=connection.prepareStatement(query);
			st.setString(1, (email!=null && email.length()!=0 ? email:null));
			st.setString(2, (fname!=null && fname.length()!=0 ? fname.toUpperCase():null));
			st.setString(3, (lname!=null && lname.length()!=0 ? lname.toUpperCase():null));
			st.setString(4, (dob!=null && dob.length()!=0 ? dob:null));
			st.setString(5, (reportedby!=null && reportedby.length()!=0 ? reportedby.toUpperCase():null));
			st.setString(6, (phoneno!=null && phoneno.length()!=0? phoneno:null));
			System.out.println(st.toString());
			rs=st.executeQuery();
			while (rs.next()) {
				Candidate c= new Candidate();
				c.setTitle(rs.getString("TITLE"));
				c.setFirstname(rs.getString("FIRST_NAME"));
				c.setLastname(rs.getString("LAST_NAME"));
				c.setEmail(rs.getString("EMAIL"));
				/*c.setDob(rs.getDate("DOB"));
				c.setReportedby(rs.getString("REPORTED_BY"));
				c.setResidentmonth(rs.getString("RESIDENT_SINCE_MONTH"));
				c.setResidentyear(rs.getString("RESIDENT_SINCE_YEAR"));
				c.setAddress1(rs.getString("ADDRESS_LINE1"));
				c.setAddress2(rs.getString("ADDRESS_LINE2"));
				c.setTown(rs.getString("POSTAL_TOWN"));
				c.setPostalcode(rs.getString("POSTAL_CODE"));
				c.setCountry(rs.getString("COUNTRY"));*/
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
	
	String query1="";
	String query2="";
	@GET
	@Path("details")
	@Produces("text/plain")
	@WebMethod(operationName = "details")
	public String getDetails(@QueryParam("email") String email)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Candidate> candidateList= new ArrayList<Candidate>();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		try {
			connection = database.getConnection();
			query1="SELECT TITLE,EMAIL,FIRST_NAME,LAST_NAME,DOB,REPORTED_BY,RESIDENT_SINCE_MONTH,RESIDENT_SINCE_YEAR,ADDRESS_LINE1,ADDRESS_LINE2,POSTAL_TOWN,POSTAL_CODE,COUNTRY,PHONENO FROM CANDIDATE_DETAILS WHERE EMAIL=?";
			st=connection.prepareStatement(query1);
			st.setString(1, email);
			rs=st.executeQuery();
			Candidate c= new Candidate();
			while(rs.next())
			{
				
				c.setTitle(rs.getString("TITLE"));
				c.setFirstname(rs.getString("FIRST_NAME"));
				c.setLastname(rs.getString("LAST_NAME"));
				c.setEmail(rs.getString("EMAIL"));
				c.setDob(rs.getDate("DOB"));
				c.setReportedby(rs.getString("REPORTED_BY"));
				c.setResidentmonth(rs.getString("RESIDENT_SINCE_MONTH"));
				c.setResidentyear(rs.getString("RESIDENT_SINCE_YEAR"));
				c.setAddress1(rs.getString("ADDRESS_LINE1"));
				c.setAddress2(rs.getString("ADDRESS_LINE2"));
				c.setTown(rs.getString("POSTAL_TOWN"));
				c.setPostalcode(rs.getString("POSTAL_CODE"));
				c.setCountry(rs.getString("COUNTRY"));
				c.setPhoneno(rs.getString("PHONENO"));
				candidateList.add(c);
				json=new Gson().toJson(candidateList);
			}
			st.close();
			query2="INSERT INTO HISTORY (EMAIL,FIRST_NAME,LAST_NAME,SEARCH_DATE) VALUES(?,?,?,?)";
			st=connection.prepareStatement(query2);
			st.setString(1, c.getEmail());
			st.setString(2, c.getFirstname());
			st.setString(3, c.getLastname());
			Date d= new Date();
			String date = format.format(d);
			st.setString(4, date);
			st.executeUpdate();
			
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
