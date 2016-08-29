package SocialMedia;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Paging;

import com.google.gson.*;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	String post_id=null;
	String cr_melisecond=null;
	public String getPost_id() {
		return post_id;
	}
	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}
	public String getCr_melisecond() {
		return cr_melisecond;
	}
	public void setCr_melisecond(String cr_melisecond) {
		this.cr_melisecond = cr_melisecond;
	}
	public static String formatcreateddate(String sDate) throws Exception{
		//String dateStr = "Mon Jun 18 00:00:00 IST 2012";
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		
		Date date = (Date)formatter.parse(sDate);
		System.out.println(date);        

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +cal.get(Calendar.YEAR);
		System.out.println("formatedDate : " + formatedDate); 
		return formatedDate;
		   
	
}
}
public class twitterApp {
	

	
public static void main(String[] args) {
    // gets Twitter instance with default credentials
    Twitter twitter = new TwitterFactory().getInstance();
   //Select query From table Social Media
    DBconnection database=new DBconnection();
	Connection connection=null;
	ResultSet rs = null;
	Date currentDate=new Date();
	PreparedStatement st = null,st_alter=null;
	ArrayList<social_media> socialMedia= new ArrayList<social_media>();
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
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
		String Myjson=new String();
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("C:\\jsonForR.txt")), "UTF8"));
		
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
       
        List<Status> statuses=new ArrayList<>();
          
        Status alltweet;
        int pageno = 1;
       

        while (true) {

          try {

            int size = statuses.size(); 
            Paging page = new Paging(pageno++, 100);
            statuses.addAll(twitter1.getUserTimeline(socialMedia.get(a).getTwitter_handle(),page));
            
            if (statuses.size() == size)
              break;
            if(twitter1.getUserTimeline().getRateLimitStatus().getRemaining()>0)
            Thread.sleep((twitter1.getUserTimeline().getRateLimitStatus().getSecondsUntilReset()*1000)/(twitter1.getUserTimeline().getRateLimitStatus().getRemaining()));
          }
          catch(TwitterException e) {

            e.printStackTrace();
          }
        }
       
       
           
            //statuses = twitter1.getUserTimeline(socialMedia.get(a).getTwitter_handle());
        String message="UPDATE CANDIDATE_DETAILS SET MESSAGE='No Suspicious Post Found',MODIFIED_AT=? WHERE EMAIL=?";
        st_alter=connection.prepareStatement(message);
        st_alter.setString(1, format.format(currentDate));
        st_alter.setString(2,socialMedia.get(a).getEmail());
        st_alter.executeUpdate();
        st_alter.close();
           
           
           for(int i=0; i<statuses.size();i++)
            {
        	   uprofile uf=new uprofile();
        	   alltweet=statuses.get(i);
            	///Filtering Done here
        	   if(!(alltweet.getText().startsWith("RT"))){
            	uf.setEmail(socialMedia.get(a).getEmail());
            	uf.setPost_id(String.valueOf(alltweet.getId()));
            	//SimpleDateFormat ddmmmyyy=);
            	uf.setCr_date(uprofile.formatcreateddate(alltweet.getCreatedAt().toString()));
            	uf.setPost_text(alltweet.getText());
            	//uf.setSource();
            	uf.setUrl("https://twitter.com/"+socialMedia.get(a).getTwitter_handle()+"/status/"+uf.getPost_id());
            	list_uf.add(uf);
        	   }
            }
           
		}
		
		Myjson=new Gson().toJson(list_uf);
		out.write(Myjson);
		//fw.write(MyjsonUtf8);
       	
          /* if(a!=statuses.size()-1)
        	   fw.write(",");*/
            out.close();
           //fw.close();
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
			
			e.printStackTrace();
		}
    }
}
}
