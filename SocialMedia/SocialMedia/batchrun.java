package SocialMedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class batchrun {
	public static void main(String args[])
    {
        String filePath = "C:/Users/121610/Desktop/AAAAAA/Fraud.bat";
        try {
             
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
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }

}
