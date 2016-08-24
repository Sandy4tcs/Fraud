package com.web;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/benefit")
public class BenefitWs {

	String query="";
	@GET
	@Path("current")
	@Produces("text/plain")
	@WebMethod(operationName = "current")
	public String getCurrentBenefitDetails(@QueryParam("email") String email)
	{
		String json="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Benefit> benefitList=new ArrayList<Benefit>();
		Date currentDate=new Date();
		int i=1;
		try {
			connection = database.getConnection();
			query="SELECT BENEFIT_TYPE,ENTITLE_AMOUNT_PER_WEEK,BENEFIT_END_DATE FROM BENEFIT WHERE EMAIL=?";
			st=connection.prepareStatement(query);
			st.setString(1, email);
			rs=st.executeQuery();
			while(rs.next())
			{
				Date benefit_date=format.parse(rs.getString("BENEFIT_END_DATE"));
				long diffDate=benefit_date.getTime()-currentDate.getTime();
				if(diffDate>0)
				{
					Benefit b=new Benefit();
					b.setCount(i);
					b.setType(rs.getString("BENEFIT_TYPE"));
					b.setAmmountPerWeek("£"+rs.getString("ENTITLE_AMOUNT_PER_WEEK"));
					benefitList.add(b);
				}
				json=new Gson().toJson(benefitList);
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
	
	String query1="";
	@GET
	@Path("details")
	@Produces("text/plain")
	@WebMethod(operationName = "details")
	public String getBenefitDetails(@QueryParam("email") String email,@QueryParam("days") int days)
	{
		String json="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<Benefit> benefitList=new ArrayList<Benefit>();
		Date currentDate=new Date();
		try {
			connection = database.getConnection();
			query="SELECT BENEFIT_TYPE,BENEFIT_START_DATE,BENEFIT_END_DATE FROM BENEFIT WHERE EMAIL=?";
			st=connection.prepareStatement(query);
			st.setString(1, email);
			rs=st.executeQuery();
			while(rs.next())
			{
				Date startDate=format.parse(rs.getString("BENEFIT_START_DATE"));
				long difftime=(long) days*24 * 60 * 60 * 1000;
				long compDate=currentDate.getTime()-difftime;
				if(compDate>startDate.getTime())
				{
					Benefit b=new Benefit();
					b.setType(rs.getString("BENEFIT_TYPE"));
					benefitList.add(b);
				}
				
			}
			json=new Gson().toJson(benefitList);
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
