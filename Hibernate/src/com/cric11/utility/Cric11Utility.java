//$Id$
package com.cric11.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;


public class Cric11Utility {
	
	private static final Logger LOGGER = Logger.getLogger(Cric11Utility.class.getName());
	
	public static String getResponsefromUrl(String url,String method,HashMap<String, String> headers, HashMap<String, String> params,JSONObject body) {
		String response = null;
		HttpURLConnection httpcon = null;
		String requestParams = "";
		try {
			URL httpurl = new URL(url);
			InputStream ins;
			httpcon = (HttpURLConnection) httpurl.openConnection();
			if(headers!=null) {
				for(Map.Entry<String, String> header:headers.entrySet()) {
					httpcon.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			if(params!=null) {
				int len = 1;
				for(Map.Entry<String, String> entry:params.entrySet()) {
					requestParams+=entry.getKey()+"="+entry.getValue();
					if(len<params.size()) {
						requestParams+="&";
					}
					len++;
				}
			}
			if(method.equals("post")) {
				httpcon.setRequestMethod("POST");		
				httpcon.setDoOutput(true);
				httpcon.setDoInput(true);
				httpcon.setRequestProperty("Connection", "Keep-Alive");	//No I18N
			}
			else if(method.equals("get")) {
				httpcon.setRequestMethod("GET");		
				//httpcon.setDoInput(true);
				httpcon.setRequestProperty("Connection", "Keep-Alive");	//No I18N
				if(requestParams!="") {
					url = url+"?"+requestParams;
					httpurl = new URL(url);
				}
			}
			else if(method.equals("put")) {
				httpcon.setRequestMethod("PUT");		
				httpcon.setDoOutput(true);
				httpcon.setDoInput(true);
				httpcon.setRequestProperty("Connection", "Keep-Alive");	//No I18N
			}
			else if(method.equals("delete")) {
				httpcon.setRequestMethod("DELETE");		
				httpcon.setDoOutput(true);
				httpcon.setDoInput(true);
				httpcon.setRequestProperty("Connection", "Keep-Alive");	//No I18N
			}
			if(method.equals("post")||method.equals("put")||method.equals("delete")) {
				OutputStream os = httpcon.getOutputStream();
				if(requestParams!="") {
					os.write(requestParams.getBytes("UTF-8"));          //No I18N
				}
				else if(body!=null){
					os.write(body.toString().getBytes("UTF-8"));          //No I18N
				}
			}
			 httpcon.setConnectTimeout(120000);
			 int responseCode = httpcon.getResponseCode();
		        if(responseCode == 200||responseCode == 201){
		        	httpcon.connect();
		            ins = httpcon.getInputStream();
		        } else{
		            ins = httpcon.getErrorStream();
		        }
		      response = readContents(ins);
		}
		catch(Exception ex) {
			LOGGER.log(Level.SEVERE, "Exception in fetching data:"+ex);
		}
		return response;
	}
	
	public static String readContents(InputStream in) throws IOException {
	      char[] buf = new char[500000]; //even number so should not cut off unicode bytes
	      //int navailable;
	      StringBuilder sb = new StringBuilder();
	      InputStreamReader isr = null;
	      try{ 
	          isr = new InputStreamReader(in, "UTF-8");//No internationalization
	          synchronized (in) {
	              int readBytes = 0;
	              while ((readBytes = isr.read(buf, 0, buf.length)) >= 0) {
	                  sb.append(buf, 0, readBytes);
	              }
	          }
	      }catch(Exception ex){
	          LOGGER.log(Level.SEVERE, "Exception in StreamUtil readContents method",ex);
	      }finally{
	          if(isr!=null){
	              isr.close();
	          }
	      }
	      return sb.toString();
	}

}
