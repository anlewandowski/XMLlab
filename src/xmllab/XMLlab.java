package xmllab;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.w3c.dom.*;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.util.*;

public class XMLlab {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		XMLlab http = new XMLlab();

		System.out.println("Testing 1 - Send Http GET request");
		System.out.println("Type your tuID and press enter to see your grade: ");
                Scanner reader = new Scanner(System.in);
                String tuid = reader.nextLine();
                http.sendGet(tuid);


	}

	// HTTP GET request
	private void sendGet(String tuid) throws Exception {

		String url = "http://129.32.92.49:8080/xml_lab/getgrade?xml=<tuid>";
                url = url.concat(tuid);
                url = url.concat("</tuid>");

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
                
                String input = response.toString();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new InputSource(new StringReader(input)));
                doc.getDocumentElement().normalize();
                String grade = doc.getElementsByTagName("content").item(0).getTextContent();
                System.out.println("Grade for " + tuid + ": " + grade);
	}

	// HTTP POST request
	private void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}

}