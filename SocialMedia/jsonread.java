package SocialMedia;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class jsonread  {
     public static void main(String[] args) throws SQLException {
    	URL url = null;
 		DBconnection database=new DBconnection();
 		Connection connection=null;
 		ResultSet rs = null,rs_insert=null;
 		PreparedStatement st = null,st_insert=null;
	JSONParser parser = new JSONParser();

	try {

		Object obj = parser.parse(new FileReader("D:\\FRAUDSCORE_java.json"));

		JSONArray Ja = (JSONArray) obj;
		
		connection = database.getConnection();
		connection.setAutoCommit(false);
		
		String query_insert="INSERT INTO FRAUD_SCORE(EVIDENCE_DATE,POST,EVIDENCE_LINK,EMAIL,CATEGORY,EVIDENCE_SCORE,SCORE) VALUES(?,?,?,?,?,?,?)";
		st_insert=connection.prepareStatement(query_insert);
		
		for(int i=0;i<Ja.size();i++){
			st_insert.setString(1,((JSONObject)Ja.get(i)).get("EVIDENCE_DATE").toString());
			st_insert.setString(2, ((JSONObject)Ja.get(i)).get("POST").toString());
			st_insert.setString(3, ((JSONObject)Ja.get(i)).get("EVIDENCE_LINK").toString());
			st_insert.setString(4, ((JSONObject)Ja.get(i)).get("EMAIL").toString());
			st_insert.setString(5, ((JSONObject)Ja.get(i)).get("CATEGORY").toString());
			st_insert.setString(6, ((JSONObject)Ja.get(i)).get("EVIDENCE_SCORE").toString());
			st_insert.setString(7, ((JSONObject)Ja.get(i)).get("SCORE").toString());
			st_insert.executeUpdate();
		}
		
		connection.commit();

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	} catch(Exception sq){
		connection.rollback();
		sq.printStackTrace();
	}finally{
		connection.close();

     }
     }
}