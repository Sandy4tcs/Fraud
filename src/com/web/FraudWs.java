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

import com.beans.Benefit;
import com.beans.Fraud;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/fraud")
public class FraudWs {

	String query="";
	@GET
	@Path("details")
	@Produces("text/plain")
	@WebMethod(operationName = "details")
	public String getFraudDetails(@QueryParam("email") String email,@QueryParam("startday") int sday,@QueryParam("endday") int eday)
	{
		String json="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Fraud> fraudList=new ArrayList<Fraud>();
		Date currentDate=new Date();
		double score=0;
		
		try {
			connection = database.getConnection();
			query="SELECT POST,CATEGORY,SCORE,EVIDENCE_DATE,EVIDENCE_LINK FROM FRAUD_SCORE WHERE EMAIL=?";
			st=connection.prepareStatement(query);
			st.setString(1, email);
			rs=st.executeQuery();
			while(rs.next())
			{
				Date evidenceDate=format.parse(rs.getString("EVIDENCE_DATE"));
				long startdifftime=(long) sday*24 * 60 * 60 * 1000;
				long enddifftime=(long) eday*24 * 60 * 60 * 1000;
				long startcompTime=currentDate.getTime()-startdifftime;
				long endcompTime=currentDate.getTime()-enddifftime;
				long evidencetime=evidenceDate.getTime();
				if(startcompTime<evidencetime && evidencetime<endcompTime)
				{
					Fraud f= new Fraud();
					f.setFraudCategory(rs.getString("CATEGORY"));
					f.setPost(rs.getString("POST"));
					score=rs.getDouble("SCORE");
					if(score>=50 && score <=100)
					{
						f.setScore("Low");
					}
					if(score>=101 && score <=200)
					{
						f.setScore("High");
					}
					f.setFraudLink(rs.getString("EVIDENCE_LINK"));
					fraudList.add(f);
				}	
			}
			json=new Gson().toJson(fraudList);
			
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
	@Path("chart")
	@Produces("text/plain")
	@WebMethod(operationName = "details")
	public String getFraudChart(@QueryParam("email") String email)
	{
		String json="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null,st_alter=null;
		Date currentDate=new Date();
		ArrayList<Fraud> fraudList=new ArrayList<Fraud>();
		int sday=7,eday=0;
		double score=0;
		double monthscore=0;
		try {
			connection = database.getConnection();
			for(int i=0;i<4;i++)
			{
				query1="SELECT SCORE,EVIDENCE_DATE FROM FRAUD_SCORE WHERE EMAIL=?";
				st=connection.prepareStatement(query1);
				st.setString(1, email);
				rs=st.executeQuery();
				while(rs.next())
				{
					Date evidenceDate=format.parse(rs.getString("EVIDENCE_DATE"));
					long startdifftime=(long) sday*24 * 60 * 60 * 1000;
					long enddifftime=(long) eday*24 * 60 * 60 * 1000;
					long startcompTime=currentDate.getTime()-startdifftime;
					long endcompTime=currentDate.getTime()-enddifftime;
					long evidencetime=evidenceDate.getTime();
					if(startcompTime<evidencetime && evidencetime<endcompTime)
					{
						score+=rs.getDouble("SCORE");
						monthscore+=rs.getDouble("SCORE");
					}
					
				}
				Fraud f= new Fraud();
				f.setFraudScore(score);
				fraudList.add(f);
				sday+=7;
				eday+=7;
				score=0;
				st.close();
				rs.close();
			}
			json=new Gson().toJson(fraudList);
			if(monthscore>0)
			{
				String message="UPDATE CANDIDATE_DETAILS SET MESSAGE='Suspicious Post Found',MODIFIED_AT=? WHERE EMAIL=?";
				st_alter=connection.prepareStatement(message);
				st_alter.setString(1, currentDate.toString());
				st_alter.setString(2, email);
				st_alter.executeUpdate();
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
	
	String query2="";
	@GET
	@Path("message")
	@Produces("text/plain")
	@WebMethod(operationName = "details")
	public String getChartMessage(@QueryParam("email") String email)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st=null;
		ArrayList<Fraud> fraudList=new ArrayList<Fraud>();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			connection = database.getConnection();
			query2="SELECT MESSAGE,MODIFIED_AT FROM CANDIDATE_DETAILS WHERE EMAIL=?";
			st=connection.prepareStatement(query2);
			st.setString(1, email);
			rs=st.executeQuery();
			while(rs.next())
			{
				Fraud f=new Fraud();
				f.setMessage(rs.getString("MESSAGE"));
				f.setM_time(format.format(rs.getTimestamp("MODIFIED_AT")));
				fraudList.add(f);
			}
			json=new Gson().toJson(fraudList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
