package SocialMedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import com.google.gson.*;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
//import com.web.DBconnection;


import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
class table_name {
	private String id = null;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String typeName = null;
	private String userName = null;
	private String type = null;
	private String typeId = null;
	private String url = null;
}

public class BusinessFruad {

	public static void main(String[] args) throws Exception {
		URL url = null;
		DBconnection database = new DBconnection();
		Connection connection = null;
		// connection.setAutoCommit(false);
		ResultSet rs = null, rs_insert = null;
		PreparedStatement st = null, st_insert = null,st_alter=null,st_batch=null;
		ArrayList<String> candidate_email = new ArrayList<String>();
		PrintWriter out = null;
		Date currentDate=new Date();
		BufferedWriter out_status = null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// SELECT QUERY

		try {
			out_status = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File("D:\\status_communication.txt"))));
			connection = database.getConnection();
			String query_insert = "INSERT INTO SOCIAL_MEDIA(EMAIL,TWITTER_HANDLE) VALUES(?,?)";
			st_insert = connection.prepareStatement(query_insert);
			String query_insert_batch="INSERT INTO BATCH_JOB(STATUS,JOB_TIME) VALUES(?,?)";
			st_batch = connection.prepareStatement(query_insert_batch);
			st_batch.setString(1, "Initiated");
			st_batch.setString(2,format.format(currentDate));
			st_batch.executeUpdate();
			String query_select = "SELECT CANDIDATE_DETAILS.EMAIL AS EMAIL FROM CANDIDATE_DETAILS WHERE CANDIDATE_DETAILS.EMAIL NOT IN (SELECT EMAIL FROM SOCIAL_MEDIA)";
			st = connection.prepareStatement(query_select);
			rs = st.executeQuery();
			 
			while (rs.next()) {
				candidate_email.add(rs.getString("EMAIL"));
			}
			
			// insert query
			JSONParser parser = new JSONParser();
			for (int j = 0; j < candidate_email.size(); j++) {
				String MyURL = "https://api.fullcontact.com/v2/person.json"
						+ "?" + "email=" + candidate_email.get(j) + "&"
						+ "apiKey=7110a172fd94bce";

				try {
					out = new PrintWriter("userdata.json");
					url = new URL(MyURL);
					// url = new
					// URL("https://api.fullcontact.com/v2/person.json?email=itsmemurad@gmail.com&apiKey=7110a172fd94bce");

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(url.openStream(), "UTF-8"));

					for (String line; (line = reader.readLine()) != null;) {
						out.println(line);
					}
					out.close();
					Object obj = parser.parse(new FileReader("userdata.json"));
					JSONObject jsonObj = (JSONObject) obj;

					JSONArray socialProfiles = (JSONArray) jsonObj
							.get("socialProfiles");

					if(socialProfiles.size()==0)
					{
						String altermessage="UPDATE CANDIDATE_DETAILS SET MESSAGE='Social Media profile not found',MODIFIED_AT=? WHERE EMAIL=?";
						st_alter=connection.prepareStatement(altermessage);
						st_alter.setString(1, format.format(currentDate));
						st_alter.setString(2, candidate_email.get(j));
						st_alter.executeUpdate();
						st_alter.close();
					}
					for (int i = 0; i < socialProfiles.size(); i++) {

						JSONObject jsonObject = (JSONObject) socialProfiles
								.get(i);
						if ((boolean) jsonObject.get("type").equals("twitter")) {
							Gson g = new Gson();
							table_name myjavaObject = g.fromJson(
									jsonObject.toString(), table_name.class);
							st_insert.setString(1, candidate_email.get(j));
							st_insert.setString(2, myjavaObject.getUrl()
									.substring(20));
							st_insert.executeUpdate();
							break;
						}

					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					//out.close();

				}finally{
				 out.close();
				 out_status.close();
				}

			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		finally {
			connection.commit();
			connection.close();

		}

	}

}
