package SociaMediaScrolling;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import com.google.gson.*;
import com.web.DBconnection;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
class social_media{
	
	public String getTwitter_handle() {
		return Twitter_handle;
	}
	public void setTwitter_handle(String twitter_handle) {
		Twitter_handle = twitter_handle;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	String Email=null;
	String Twitter_handle=null;
}
class uprofile {
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCr_date() {
		return cr_date;
	}
	public void setCr_date(String cr_date) {
		this.cr_date = cr_date;
	}
	public String getPost_text() {
		return post_text;
	}
	public void setPost_text(String post_text) {
		this.post_text = post_text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	String source=null;
	String cr_date=null;
	String post_text=null;
	String url=null;
	String email=null;
	
}
public class twitterApp {
	

	
public static void main(String[] args) {
    // gets Twitter instance with default credentials
    Twitter twitter = new TwitterFactory().getInstance();
   //Select query From table Social Media
    DBconnection database=new DBconnection();
	Connection connection=null;
	ResultSet rs = null;
	PreparedStatement st = null,st_alter=null;
	ArrayList<social_media> socialMedia= new ArrayList<social_media>();
	Date currentDate=new Date();
   
    try {
    	connection = database.getConnection();
    	String query_select="SELECT TWITTER_HANDLE,EMAIL FROM SOCIAL_MEDIA";
		st=connection.prepareStatement(query_select);
		rs=st.executeQuery();
		while(rs.next())
		{
			social_media sm=new social_media();
		sm.setTwitter_handle(rs.getString("TWITTER_HANDLE"));
		sm.setEmail(rs.getString("EMAIL"));
		socialMedia.add(sm);
		}
		String Myjson=null;
		FileWriter fw=new FileWriter("C:\\jsonForR.txt", true);
		List <uprofile> list_uf=new ArrayList<>();
		for(int a=0;a<socialMedia.size();a++)
		{
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("s5BtpyRc81XS6SGPzNmnpeYDg") //tRgDnBVbfX8zmfpzLmvwSOnIF
          .setOAuthConsumerSecret("sUTMAdCOPAaEWS7DKcp1osY6IBlfHPSpJJuWLKH72rFB8n1TRp") //MuEizrPR5Na0Aqiuy86tWME0CalAb0BHinJqfE47FwWywS0MIZ
          .setOAuthAccessToken("754333416095703042-6a5LEPd47UIPMLwKQ46HPZdEhRdun7l") //754333416095703042-6a5LEPd47UIPMLwKQ46HPZdEhRdun7l
          .setOAuthAccessTokenSecret("3cH2HQBnLoUZBGiqTYPgxFXMuj3j7r8kfpp2SdJ3H5L5T"); //3cH2HQBnLoUZBGiqTYPgxFXMuj3j7r8kfpp2SdJ3H5L5T
        
        
	        TwitterFactory tf = new TwitterFactory(cb.build());
	        Twitter twitter1 = tf.getInstance();
	        List<Status> statuses;
	          
	        Status alltweet;
	        statuses = twitter1.getUserTimeline(socialMedia.get(a).getTwitter_handle());
            
           //fw.write("[");
           String message="UPDATE CANDIDATE_DETAILS SET MESSAGE='No Suspicious Post Found',MODIFIED_AT=? WHERE EMAIL=?";
           st_alter=connection.prepareStatement(message);
           st_alter.setString(1, currentDate.toString());
           st_alter.setString(2,socialMedia.get(a).getEmail());
           st_alter.executeUpdate();
           st_alter.close();
           for(int i=0; i<statuses.size();i++)
            {
        	   uprofile uf=new uprofile();
        	   alltweet=statuses.get(i);
            	
            	uf.setEmail(socialMedia.get(a).getEmail());
            	uf.setCr_date(alltweet.getCreatedAt().toString());
            	uf.setPost_text(alltweet.getText());
            	//uf.setSource(alltweet.getSource());
            	//uf.setUrl(alltweet.getURLEntities().toString());
            	list_uf.add(uf);
          
            }
           
		}
		
		Myjson=new Gson().toJson(list_uf);
       	fw.write(Myjson);
          /* if(a!=statuses.size()-1)
        	   fw.write(",");*/
            
           fw.close();
    } catch (TwitterException te) {
        te.printStackTrace();
        System.out.println("Failed to get timeline: " + te.getMessage());
        System.exit(-1);
    }catch(Exception ex){
    	System.out.println(ex);
    }
    finally {
    	try {
    		st.close();
    		connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
}