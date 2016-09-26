package com.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.beans.Candidate;
import com.beans.ChartData;
import com.beans.Fraud;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/dashboard")
public class DashboardWs implements Comparator{

	String query="",query1="";
	@GET
	@Path("pichart")
	@Produces("text/plain")
	@WebMethod(operationName = "chart")
	public String getChartScore()
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null,rs_count=null;
		PreparedStatement st = null,st_count=null;
		ArrayList<ChartData> chartList= new ArrayList<ChartData>();
		ArrayList<Candidate> candidateList=new ArrayList<Candidate>();
		int ingf=0,kpm=0,nar=0;
		double count=0;
		try {
			connection = database.getConnection();
			query="SELECT DISTINCT EMAIL FROM FRAUD_SCORE";
			st=connection.prepareStatement(query);
			rs=st.executeQuery();
			while(rs.next())
			{
				Candidate c=new Candidate();
				c.setEmail(rs.getString("EMAIL"));
				candidateList.add(c);
			}
			for(int i=0;i<candidateList.size();i++)
			{
				query1="SELECT SCORE FROM FRAUD_SCORE WHERE EMAIL=?";
				st_count=connection.prepareStatement(query1);
				st_count.setString(1, candidateList.get(i).getEmail());
				rs_count=st_count.executeQuery();
				while(rs_count.next())
				{
					count += rs_count.getDouble("SCORE");
				}
				if(count>=1000)
				{
					ingf++;
				}
				else if(count<=999 && count>=500)
				{
					kpm++;
				}
				else
				{
					nar++;
				}
				count=0;
				st_count.close();
			}
			ChartData cd1=new ChartData();
			cd1.setCategory("Investigate Fraud");
			cd1.setCount(ingf);
			chartList.add(cd1);
			
			ChartData cd2=new ChartData();
			cd2.setCategory("Keep Monitoring");
			cd2.setCount(kpm);
			chartList.add(cd2);
			
			ChartData cd3=new ChartData();
			cd3.setCategory("No Action Required");
			cd3.setCount(nar);
			chartList.add(cd3);
			
			json=new Gson().toJson(chartList);
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
	
	
	String query2="",query3="";
	@GET
	@Path("category")
	@Produces("text/plain")
	@WebMethod(operationName = "category")
	public String getCategory()
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<ChartData> cdList=new ArrayList<ChartData>();
		try {
			connection = database.getConnection();
			query2="SELECT CATEGORY,COUNT(CATEGORY) AS COUNT1 FROM FRAUD_SCORE GROUP BY CATEGORY";
			st = connection.prepareStatement(query2);
			rs = st.executeQuery();
			while(rs.next())
			{
				ChartData cd=new ChartData();
				cd.setCategory(rs.getString("CATEGORY"));
				cd.setCount(rs.getInt("COUNT1"));
				cdList.add(cd);
			}
			json=new Gson().toJson(cdList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
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
	
	
	String query5="",query4="";
	@GET
	@Path("graphchart")
	@Produces("text/plain")
	@WebMethod(operationName = "graphchart")
	public String getGraphChart(){
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null,rs_count=null;
		PreparedStatement st = null,st_count=null;
		ArrayList<ChartData> chartList= new ArrayList<ChartData>();
		ArrayList<Candidate> candidateList=new ArrayList<Candidate>();
		int ingf=0,kpm=0,nar=0;
		int ifa[]= new int[6];
		int kpma[]= new int[6];
		int nara[]= new int[6];
		double count=0;
		int d=150,ed=180;
		try {
			connection = database.getConnection();
			query4="SELECT DISTINCT EMAIL FROM FRAUD_SCORE";
			st=connection.prepareStatement(query4);
			rs=st.executeQuery();
			while(rs.next())
			{
				Candidate c=new Candidate();
				c.setEmail(rs.getString("EMAIL"));
				candidateList.add(c);
			}
			for(int j=0;j<6;j++)
			{
				for(int i=0;i<candidateList.size();i++)
				{
					query5="SELECT SCORE FROM FRAUD_SCORE WHERE EVIDENCE_DATE >= DATEADD('day',-"+ed+", CURRENT_DATE - "+d+" DAY) AND EVIDENCE_DATE <= CURRENT_DATE - "+d+" DAY AND EMAIL=?";
					st_count=connection.prepareStatement(query5);
					st_count.setString(1, candidateList.get(i).getEmail());
					rs_count=st_count.executeQuery();
					while(rs_count.next())
					{
						count += rs_count.getDouble("SCORE");
					}
					if(count>=1000)
					{
						ingf++;
					}
					else if(count<=999 && count>=500)
					{
						kpm++;
					}
					else
					{
						nar++;
					}
					count=0;
					st_count.close();
					rs_count.close();
				}
				d-=30;
				ed-=30;
				ifa[j]=ingf;
				kpma[j]=kpm;
				nara[j]=nar;
				ingf=0;
				kpm=0;
				nar=0;
			}
			ChartData cd1=new ChartData();
			cd1.setName("Investigate Fraud");
			cd1.setData(ifa);
			chartList.add(cd1);
			
			ChartData cd2=new ChartData();
			cd2.setName("Keep Monitoring");
			cd2.setData(kpma);
			chartList.add(cd2);
			
			ChartData cd3=new ChartData();
			cd3.setName("No Action Required");
			cd3.setData(nara);
			chartList.add(cd3);
			
			json=new Gson().toJson(chartList);
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
	
	String query6="",query10="";
	@GET
	@Path("detailschart")
	@Produces("text/plain")
	@WebMethod(operationName = "detailschart")
	public String getDetailsChart(@QueryParam("category") String category)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null,rs_count=null;
		PreparedStatement st = null,st_count=null;
		ArrayList<Fraud> fraudList=new ArrayList<Fraud>();
		try {
			connection = database.getConnection();
			query6="SELECT EMAIL,SUM(SCORE) AS TOTAL FROM FRAUD_SCORE WHERE CATEGORY=? GROUP BY EMAIL";
			st=connection.prepareStatement(query6);
			st.setString(1, category);
			rs=st.executeQuery();
			while(rs.next())
			{
				query10="SELECT FIRST_NAME,LAST_NAME FROM CANDIDATE_DETAILS WHERE EMAIL=?";
				st_count=connection.prepareStatement(query10);
				st_count.setString(1, rs.getString("EMAIL"));
				rs_count=st_count.executeQuery();
				while(rs_count.next())
				{
					Fraud f=new Fraud();
					f.setFname(rs_count.getString("FIRST_NAME"));
					f.setLname(rs_count.getString("LAST_NAME"));
					f.setFraudScore(rs.getDouble("TOTAL"));
					fraudList.add(f);
				}
			}
			Collections.sort(fraudList, new Comparator<Fraud>(){
				
				@Override
				public int compare(Fraud f2, Fraud f1) {
					// TODO Auto-generated method stub
					return f1.getFraudScore().compareTo(f2.getFraudScore());
				}
			});
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
	
	String query7="",query8="",query9="";
	@GET
	@Path("categorydetails")
	@Produces("text/plain")
	@WebMethod(operationName = "categorydetails")
	public String getCategoryDetails(@QueryParam("key") int key)
	{
		String json="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null,rs_count=null,rs_data=null;
		PreparedStatement st = null,st_count=null,st_data=null;
		ArrayList<Fraud> fraudList=new ArrayList<Fraud>();
		ArrayList<Candidate> candidateList=new ArrayList<Candidate>();
		double count=0;
		try {
			connection = database.getConnection();
			query7="SELECT DISTINCT EMAIL FROM FRAUD_SCORE";
			st=connection.prepareStatement(query7);
			rs=st.executeQuery();
			while(rs.next())
			{
				Candidate c=new Candidate();
				c.setEmail(rs.getString("EMAIL"));
				candidateList.add(c);
			}
			for(int i=0;i<candidateList.size();i++)
			{
				query8="SELECT SUM(SCORE) AS TOTALSCORE FROM FRAUD_SCORE WHERE EMAIL=?";
				st_count=connection.prepareStatement(query8);
				st_count.setString(1, candidateList.get(i).getEmail());
				rs_count=st_count.executeQuery();
				while(rs_count.next())
				{
					count=rs_count.getDouble("TOTALSCORE");
				}
				if(key==1)
				{
					if(count>=1000)
					{
						query9="SELECT FIRST_NAME,LAST_NAME FROM CANDIDATE_DETAILS WHERE EMAIL=?";
						st_data=connection.prepareStatement(query9);
						st_data.setString(1, candidateList.get(i).getEmail());
						rs_data=st_data.executeQuery();
						while(rs_data.next())
						{
							Fraud f=new Fraud();
							f.setFname(rs_data.getString("FIRST_NAME"));
							f.setLname(rs_data.getString("LAST_NAME"));
							f.setFraudScore(count);
							fraudList.add(f);
						}
					}
				}
				if(key==2)
				{
					if(count<=999 && count>=500)
					{
						query9="SELECT FIRST_NAME,LAST_NAME FROM CANDIDATE_DETAILS WHERE EMAIL=?";
						st_data=connection.prepareStatement(query9);
						st_data.setString(1, candidateList.get(i).getEmail());
						rs_data=st_data.executeQuery();
						while(rs_data.next())
						{
							Fraud f=new Fraud();
							f.setFname(rs_data.getString("FIRST_NAME"));
							f.setLname(rs_data.getString("LAST_NAME"));
							f.setFraudScore(count);
							fraudList.add(f);
						}
					}
				}
				if(key==3)
				{
					if(count<=499)
					{
						query9="SELECT FIRST_NAME,LAST_NAME FROM CANDIDATE_DETAILS WHERE EMAIL=?";
						st_data=connection.prepareStatement(query9);
						st_data.setString(1, candidateList.get(i).getEmail());
						rs_data=st_data.executeQuery();
						while(rs_data.next())
						{
							Fraud f=new Fraud();
							f.setFname(rs_data.getString("FIRST_NAME"));
							f.setLname(rs_data.getString("LAST_NAME"));
							f.setFraudScore(count);
							fraudList.add(f);
						}
					}
				}
				count=0;
			}
			Collections.sort(fraudList, new Comparator<Fraud>(){
				
				@Override
				public int compare(Fraud f2, Fraud f1) {
					// TODO Auto-generated method stub
					return f1.getFraudScore().compareTo(f2.getFraudScore());
				}
			});
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
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
