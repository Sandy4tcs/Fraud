package com.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@WebService()
@Entity
@Path("/batch")
public class BatchRunWs {

	@GET
	@Path("run")
	@Produces("text/plain")
	@WebMethod(operationName = "run")
	public String batchRun()
	{
		String message="";
		DBconnection database=new DBconnection();
		Connection connection=null;
		ResultSet rs = null;
		PreparedStatement st = null;
		int size=0;
		 String filePath = "C:/Users/121610/Desktop/AAAAAA/Fraud.bat";
		 try {
             connection = database.getConnection();
             String query="SELECT * FROM BATCH_JOB WHERE STATUS='Initiated'";
             st=connection.prepareStatement(query);
             rs=st.executeQuery();
             while(rs.next())
             {
            	 size++;
             }
             if(size==0)
             {
            	 message="Batch Job Started";
            	 /*Process p = Runtime.getRuntime().exec("cmd.exe /c start "+filePath);*/
            	 Process p = Runtime.getRuntime().exec(filePath);
 	        	InputStream in = p.getInputStream();
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	             
	            int c = -1;
	            while((c = in.read()) != -1)
	            {
	                baos.write(c);
	            }
	             
	            String response = new String(baos.toByteArray());
	            System.out.println("Response From Exe : "+response);
	            
             }
             else
             {
            	 message="Please wait. Batch Job is Already Running";
             }
             /*  Process p = Runtime.getRuntime().exec("cmd.exe /c start "+filePath);
	        	Thread.sleep(5000);
	        	BufferedReader br = new BufferedReader(new FileReader("C:\\status_communication.txt"));
	             while(true)
	             {
	            	 if(br.readLine()!=null)
	            	 {
	            		 break;
	            	 }
	            	
	             }*/
	        	
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return message;
	}
}
