package com.ipass.api.sample.report;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Sample Code to demonstrate how Login API can be used to 
 *  + Authenticate
 *  + Obtain the Cookies
 *  + Attach the Cookies to all the future API calls
 *  
 * == PLEASE NOTE ==
 * iPass provide this sample code just to demonstrate how our API works
 * iPass will not provide any support for these samples
 * 
 * iPass may change API attributes and resources, and our policies regarding 
 * access and use of APIs at any time, without advance notice. iPass will try 
 * its best to notify you of any modifications to the API or policies on Open Mobile Help.
 * 
 * @author iPass.com (March, 2014)
 * @version 1.0
 * 
 */
public class Custom {
	
	/**
	 * The following variables must be configured with your own account settings
	 */
	String BASE_URL = "https://openmobile.ipass.com";
	String username = "johnsmith@acme.com";
	String password = "changeme";
	String companyId = "987654";
	String clientUserVersion = "ClientUserVersionReport.html";
	String clientUserDomain = "ClientUserDomainReport.html";
	String month = "2014-02";
	
	HttpClient httpClient = null;
	
	private X509TrustManager tm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
		public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};
	
	public void downloadClientUserVersion() throws ClientProtocolException, IOException { 
		// Prepare the Custom Report API call to download ClientUserVersion Reports for ALL child companies (in ZIP)
		String reportURI = "/moservices/rest/api/ipass/" + companyId + "/mo/reports/monthly/custom?User-Agent=apiuser&month=" + month + "&filename=" + clientUserVersion;
		HttpGet reportGet = new HttpGet(BASE_URL + reportURI);
		
		// Make the Custom Report API call to obtain the ClientUserVersion Reports for ALL child companies (in ZIP)
		HttpResponse reportGetResponse = httpClient.execute(reportGet);
		HttpEntity httpEntity = reportGetResponse.getEntity();
		
		// Process the HTTP Response to be output to the screen
		// String reportOutput = EntityUtils.toString(httpEntity);
		// System.out.println(reportOutput);
		
		// Process the HTTP Response to be saved to C:\Temp\ClientUserVersionReport.zip (This contains ClientUserVersions report for ALL child companies)
		byte[] reportByteOutput = EntityUtils.toByteArray(httpEntity);
		Path path = FileSystems.getDefault().getPath("C:\\Temp", clientUserVersion.replaceFirst("html", "zip"));
		Files.write(path, reportByteOutput, StandardOpenOption.CREATE);
	}

	public void downloadClientUserDomain() throws ClientProtocolException, IOException { 
		// Prepare the Custom Report API call to download ClientUserDomain Reports for ALL child companies (in ZIP)
		String reportURI = "/moservices/rest/api/ipass/" + companyId + "/mo/reports/monthly/custom?User-Agent=apiuser&month=" + month + "&filename=" + clientUserDomain;
		HttpGet reportGet = new HttpGet(BASE_URL + reportURI);
		
		// Make the Custom Report API call to obtain the ClientUserDomain Reports for ALL child companies (in ZIP)
		HttpResponse reportGetResponse = httpClient.execute(reportGet);
		HttpEntity httpEntity = reportGetResponse.getEntity();
		
		// Process the HTTP Response to be output to the screen
		// String reportOutput = EntityUtils.toString(httpEntity);
		// System.out.println(reportOutput);
		
		// Process the HTTP Response to be saved to C:\Temp\ClientUserDomainReport.zip (This contains ClientUserDomain report for ALL child companies)
		byte[] reportByteOutput = EntityUtils.toByteArray(httpEntity);
		Path path = FileSystems.getDefault().getPath("C:\\Temp", clientUserDomain.replaceFirst("html", "zip"));
		Files.write(path, reportByteOutput, StandardOpenOption.CREATE);
	}

	public Custom() throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException { 
		httpClient = new DefaultHttpClient();
		
		// Set up necessary for SSL (HTTPS) stuff
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, new TrustManager[]{tm}, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = httpClient.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", ssf, 443));		
		httpClient = new DefaultHttpClient(ccm);
		
		// Prepare the Login API Call with the appropriate parameters 
		String loginURI = "/moservices/rest/api/login?User-Agent=apiuser&username=" + username + "&password=" + password;
		HttpGet loginGet = new HttpGet(BASE_URL + loginURI);
		
		// Make the Login API call to authenticate 
		HttpResponse loginResponse = httpClient.execute(loginGet);

		// Consume content so the HTTPClient can be re-used
		loginResponse.getEntity().consumeContent();
					
		// Get Cookies from the Login API call
		CookieStore cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		
		// Set cookies for future API calls
		httpClient.getParams().setParameter("Cookie", cookies);	
	}
	
	public static void main(String[] args) {
		try { 
			Custom atr = new Custom();
			atr.downloadClientUserDomain();
			atr.downloadClientUserVersion();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
