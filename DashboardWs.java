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
import com.beans.ChartData;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/dashboard")
public class DashboardWs {

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
}
