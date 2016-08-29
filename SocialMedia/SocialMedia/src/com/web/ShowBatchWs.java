package com.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.beans.Batch;
import com.beans.Benefit;
import com.google.gson.Gson;

@WebService()
@Entity
@Path("/showbatch")
public class ShowBatchWs {

	String query="";
	@GET
	@Path("batch")
	@Produces("text/plain")
	@WebMethod(operationName = "batch")
	public String getJobDetails()
	{
	String json="";
	DBconnection database=new DBconnection();
	Connection connection=null;
	ResultSet rs = null;
	PreparedStatement st = null;
	ArrayList<Batch> batchList=new ArrayList<Batch>();
	try {
		connection = database.getConnection();
		query="SELECT JOB_ID,STATUS,JOB_TIME FROM BATCH_JOB ORDER BY JOB_ID DESC";
		st = connection.prepareStatement(query);
		rs=st.executeQuery();
		while(rs.next())
		{
			Batch b= new Batch();
			b.setBatchId(rs.getInt("JOB_ID"));
			b.setStatus(rs.getString("STATUS"));
			b.setJobTime(rs.getString("JOB_TIME"));
			batchList.add(b);
		}
		json=new Gson().toJson(batchList);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return json;
	}
}
