package net.mindoverflow.hubthat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UpdateChecker {

	public static Main plugin;
	public static String version;
	public static String link;
	public UpdateChecker (Main plugin){
		UpdateChecker.plugin = plugin;
	}
	
	public boolean linksValid() {
		
		try{
			HttpsURLConnection.setFollowRedirects(true);
			HttpsURLConnection con = (HttpsURLConnection) new URL("https://mind-overflow.tk/").openConnection();
			con.setConnectTimeout(3000);
			con.setReadTimeout(3000);
			con.setRequestMethod("HEAD");
			
			if(con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				final URL url1 = new URL("https://mind-overflow.tk/htuv");
				HttpsURLConnection huc1 = (HttpsURLConnection) url1.openConnection();
				int responseCode1 = huc1.getResponseCode();
				final URL url2 = new URL("https://mind-overflow.tk/htlnk");
				HttpsURLConnection huc2 = (HttpsURLConnection) url2.openConnection();
				int responseCode2 = huc2.getResponseCode(); 	
				if (responseCode1 == 200 && responseCode2 == 200) {
					return true;
				} else {
		    		System.out.println("HubThat Updates Server codes: " + responseCode1 + "; " + responseCode2);
		    	return false;
				}
			} else {
				System.out.println("HubThat Updates Server is unreachable. Check your Internet Connection and Firewall.");
				return false;
		      }
		} catch(IOException e){
			e.printStackTrace();
			System.out.println("HubThat Updates Server is unreachable. Check your Internet Connection and Firewall.");
			return false;
		}
		        
	}
	

	public static String updateText() throws IOException {
		URL htexturl = new URL("https://mind-overflow.tk/htext");
		HttpsURLConnection htextconnection = (HttpsURLConnection) htexturl.openConnection();
		int responseCode = htextconnection.getResponseCode();
		if (responseCode == 200) {
			String htext = getStringFromUrl("https://mind-overflow.tk/htext");
			return htext;
		} else
		{
			return "";
		}
	}

	public static String warning() throws IOException {
		String htwarningboolean = getStringFromUrl("https://mind-overflow.tk/htwarningboolean");
		String htwarningtext = getStringFromUrl("https://mind-overflow.tk/htwarning");
		
		if (htwarningboolean.contains("true"))
		{
			return htwarningtext;
		} else 
		{
			return "";
		}
	}

	public static boolean updateNeeded() throws MalformedURLException, IOException{

		version = getStringFromUrl("https://mind-overflow.tk/htuv");
		link = getStringFromUrl("https://mind-overflow.tk/htlnk");
	
			if(!plugin.getDescription().getVersion().equals(version)){
					return true;
			}
		return false;
		}
	
	public static String getVersion(){
		return version;
	}
	public static String getLink(){
		return link;
	}
	
	static String getStringFromUrl(String urlString)
	{
		try {
			URL url = new URL(urlString);
			HttpsURLConnection httpsConnection = (HttpsURLConnection) url.openConnection();
			InputStream connectionStream = httpsConnection.getInputStream();
			InputStreamReader streamReader = new InputStreamReader(connectionStream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			String text = bufferedReader.readLine();
			connectionStream.close();
			return text;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

	}
}