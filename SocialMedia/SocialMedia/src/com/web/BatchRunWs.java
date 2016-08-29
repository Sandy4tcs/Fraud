package com.web;

import java.io.BufferedReader;
import java.io.FileReader;

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
	public void batchRun()
	{
		
		 String filePath = "C:/Windows/Notepad.exe";
	        try {
	        	 Runtime.getRuntime().exec(filePath);
	        	 Thread.sleep(5000);
	        	 BufferedReader br = new BufferedReader(new FileReader("D:\\status_communication.txt"));
	             while(true)
	             {
	            	 if(br.readLine()!=null)
	            	 {
	            		 break;
	            	 }
	            	
	             }
	            
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
