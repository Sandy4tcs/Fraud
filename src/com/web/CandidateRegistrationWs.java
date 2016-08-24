package com.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.web.DBconnection;

@WebService()
@Entity
@Path("/submitcandidate")
public class CandidateRegistrationWs {

	 	String query="";
	    @GET
	    @Path("submitdata") 
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    @Produces("text/plain")
	    @WebMethod(operationName = "submitdata")
	    public void submitCandidate(@QueryParam("title") String title,
	    		@QueryParam("fname") String fname,
	    		@QueryParam("lname") String lname,
	    		@QueryParam("email") String email,
	    		@QueryParam("dob") String dob,
	    		@QueryParam("reportedby") String reportedby,
	    		@QueryParam("month") String month,
	    		@QueryParam("year") String year,
	    		@QueryParam("address1") String address1,
	    		@QueryParam("address2") String address2,
	    		@QueryParam("town") String town,
	    		@QueryParam("postalcode") String code,
	    		@QueryParam("country") String country,
	    		@QueryParam("country") String phone)
	    {
	    	
	    	if(month.equals("0"))
	    	{
	    		month="January";
	    	}
	    	if(month.equals("1"))
	    	{
	    		month="February";
	    	}
	    	if(month.equals("2"))
	    	{
	    	month="March";	
	    	}
	    	if(month.equals("3"))
	    	{
	    		month="April";
	    	}
	    	if(month.equals("4"))
	    	{
	    		month="May";
	    	}
	    	if(month.equals("5"))
	    	{
	    		month="June";
	    	}
	    	if(month.equals("6"))
	    	{
	    		month="July";
	    	}
	    	if(month.equals("7"))
	    	{
	    		month="August";
	    	}
	    	if(month.equals("8"))
	    	{
	    		month="September";	
	    	}
	    	if(month.equals("9"))
	    	{
	    		month="October";
	    	}
	    	if(month.equals("10"))
	    	{
	    		month="November";
	    	}
	    	if(month.equals("11"))
	    	{
	    		month="December";
	    	}
	    	PreparedStatement st = null;
	    	Connection connection=null;
			DBconnection database= new DBconnection();
			String message="Data Scrolling has not done yet";
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate=new Date();
			try {
				connection = database.getConnection();
				//fetchSocialData(connection, email);
				query="INSERT INTO CANDIDATE_DETAILS(TITLE,EMAIL,FIRST_NAME,LAST_NAME,DOB,REPORTED_BY,RESIDENT_SINCE_MONTH,RESIDENT_SINCE_YEAR,ADDRESS_LINE1,ADDRESS_LINE2,POSTAL_TOWN,POSTAL_CODE,COUNTRY,PHONENO,MESSAGE,MODIFIED_AT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st=connection.prepareStatement(query);
				st.setString(1,title.toUpperCase());
				st.setString(2, email);
				st.setString(3, fname.toUpperCase());
				st.setString(4, lname.toUpperCase());
				st.setString(5, dob);
				st.setString(6, reportedby.toUpperCase());
				st.setString(7, month.toUpperCase());
				st.setString(8, year);
				st.setString(9, address1.toUpperCase());
				st.setString(10, address2.toUpperCase());
				st.setString(11, town.toUpperCase());
				st.setString(12, code);
				st.setString(13, country.toUpperCase());
				st.setString(14, phone);
				st.setString(15, message);
				st.setString(16, format.format(currentDate));
				st.executeUpdate();
				System.out.println(st.toString());
				st.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				
				try {
					connection.commit();
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
/*public void fetchSocialData(java.sql.Connection conn,String email){
	
	
		URL url = null;
		JSONParser parser =  new JSONParser();
		String MyURL = "https://api.fullcontact.com/v2/person.json"+"?email="+email+"&"+"apiKey=7110a172fd94bce";
		try {
			
			PrintWriter out = new PrintWriter("userdata.json");
			url = new URL(MyURL);//("https://api.fullcontact.com/v2/person.json?email=sumanta22@gmail.com&apiKey=7110a172fd94bce");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			for (String line; (line = reader.readLine()) != null;) {
		        out.println(line);
		    }
			out.close();
			Object obj =  parser.parse(new FileReader("userdata.json"));
			JSONObject jsonObj = (JSONObject) obj;
			
			JSONArray socialProfiles = (JSONArray) jsonObj.get("socialProfiles");
			
			
			for (int i=0; i<socialProfiles.size(); i++) {

				JSONObject jsonObject= (JSONObject)socialProfiles.get(i);
				if((boolean) jsonObject.get("type").equals("facebook")){
					Gson g =new Gson();
					table_name myjavaObject=g.fromJson(jsonObject.toString(), table_name.class);
					break;
				}

///Insert query;			

}
			
			
		    
			
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 

	
}*/
}
