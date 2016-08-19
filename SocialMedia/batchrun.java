package SocialMedia;

public class batchrun {
	public static void main(String args[])
    {
        String filePath = "C:/Windows/Notepad.exe";
        try {
             
             Runtime.getRuntime().exec(filePath);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }

}
