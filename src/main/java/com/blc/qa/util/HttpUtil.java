package com.blc.qa.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
	
	public class HttpUtil {
	    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
	    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";

	   
	    public static String sendGet(String url) {
	        String result = null;
	        CloseableHttpResponse response = null;
	        try {
	            HttpGet httpGet = new HttpGet(url);
	            httpGet.setHeader("User-Agent", userAgent);
	            response = httpclient.execute(httpGet);
	            HttpEntity entity = response.getEntity();
	            if (entity != null) {
	                result = EntityUtils.toString(entity);
	            }
	        } catch (Exception e) {
	            log.error("����ʧ�� {}" + e);
	            e.printStackTrace();
	        } finally {
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException e) {
	                    log.error(e.getMessage());
	                }
	            }
	        }
	        return result;
	    }
	    
	   
	    public static String sendGet(String url, Map<String, String> params) {  
	        StringBuilder result = new StringBuilder();  
	        BufferedReader in = null; 
	        JSONObject ob=null;
	        try {  
	            String urlNameString = url + "?" + getUrlParamsFromMap(params);  
	            URL realUrl = new URL(urlNameString);  
	           
	            URLConnection connection = realUrl.openConnection();  
	            
	            connection.setRequestProperty("accept", "*/*");  
	            connection.setRequestProperty("connection", "Keep-Alive");  
	            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
	            
	            connection.connect();  
	            
	            Map<String, List<String>> map = connection.getHeaderFields();  
	             
	            
	            String tokenValue=null; 
	             for (String key : map.keySet()) {  
	            	 if ("Set-Cookie".equals(key)) {
	            		 String str=map.get("Set-Cookie").get(1);
	            		 tokenValue=str.substring(str.indexOf("=")+1, str.indexOf(";"));
	            	 }
	                  
	            }  
	            
	            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
	            String line;  
	            while ((line = in.readLine()) != null) {  
	                result.append(line);  
	            }  
	            ob= JSONObject.parseObject(result.toString());
	            if (tokenValue!=null) {
	            	ob.fluentPut("token", tokenValue);
				}
		        
		        //System.out.println("tooooooooooooo"+ob.toJSONString());
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }finally {
	            try {  
	                if (in != null) {  
	                    in.close();  
	                }  
	            } catch (Exception e2) {  
	                e2.printStackTrace();  
	            }  
	        }  
	        if (ob==null) {
				return result.toString();
			}
	        return ob.toString();  
	    }  

	    
	    public static String sendPost(String url, Map<String, String> map) {
	       
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        for (Map.Entry<String, String> entry : map.entrySet()) {
	            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	        }
	        
	        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
	        
	        HttpPost httpPost = new HttpPost(url);
	        
	        httpPost.setHeader("User-Agent", userAgent);
	        
	        httpPost.setEntity(formEntity);
	        CloseableHttpResponse response = null;
	        String result = null;
	        try {
	          
	            response = httpclient.execute(httpPost);
	            
	            HttpEntity entity = response.getEntity();
	            
	            result = EntityUtils.toString(entity);
	        } catch (IOException e) {
	            log.error(e.getMessage());
	        } finally {
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException e) {
	                    log.error(e.getMessage());
	                }
	            }
	        }
	        return result;
	    }

	   
	    public static String sendPost(String url, File file) {
	        String result = null;
	        HttpPost httpPost = new HttpPost(url);
	        
	        httpPost.setHeader("User-Agent", userAgent);
	        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
	        multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        multipartEntity.addPart("media", new FileBody(file));
	        httpPost.setEntity(multipartEntity.build());
	        CloseableHttpResponse response = null;
	        try {
	            response = httpclient.execute(httpPost);
	            HttpEntity entity = response.getEntity();
	            result = EntityUtils.toString(entity);
	        } catch (IOException e) {
	            log.error(e.getMessage());
	        } finally {
	            
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException e) {
	                    log.error(e.getMessage());
	                }
	            }
	        }
	        return result;

	    }

	    
	    public static String sendPost(String url, String jsonStr) {
	        String result = null;
	       
	        StringEntity entity = new StringEntity(jsonStr, Consts.UTF_8);
	        
	        entity.setContentType("application/json");
	        HttpPost httpPost = new HttpPost(url);
	        
	        httpPost.setHeader("User-Agent", userAgent);
	        
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setEntity(entity);
	        CloseableHttpResponse response = null;
	        try {
	            response = httpclient.execute(httpPost);
	            HttpEntity httpEntity = response.getEntity();
	            result = EntityUtils.toString(httpEntity);
	        } catch (IOException e) {
	            log.error(e.getMessage());
	        } finally {
	            
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException e) {
	                    log.error(e.getMessage());
	                }
	            }
	        }
	        return result;
	    }

	    
	    public static String sendPost(String url) {
	        String result = null;
	        
	        HttpPost httpPost = new HttpPost(url);
	        
	        httpPost.setHeader("User-Agent", userAgent);
	        CloseableHttpResponse response = null;
	        try {
	           
	            response = httpclient.execute(httpPost);
	            
	            HttpEntity entity = response.getEntity();
	           
	            result = EntityUtils.toString(entity);
	        } catch (IOException e) {
	            log.error(e.getMessage());
	        } finally {
	            
	            if (response != null) {
	                try {
	                    response.close();
	                } catch (IOException e) {
	                    log.error(e.getMessage());
	                }
	            }
	        }
	        return result;
	    }
	 /**
	     * Send a POST request to itcuties.com
	     * @param query
	     * @throws IOException
	     */
	    public String echoCuties(String query) throws IOException {
	        // Encode the query
	        String encodedQuery = URLEncoder.encode(query, "UTF-8");
	        // This is the data that is going to be send to itcuties.com via POST request
	        // 'e' parameter contains data to echo
	        String postData = "e=" + encodedQuery;
	         
	        // Connect to google.com
	        URL url = new URL("http://echo.itcuties.com");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        connection.setRequestProperty("Content-Length",  String.valueOf(postData.length()));
	         
	        // Write data
	        OutputStream os = connection.getOutputStream();
	        os.write(postData.getBytes());
	         
	        // Read response
	        StringBuilder responseSB = new StringBuilder();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	          
	        String line;
	        while ( (line = br.readLine()) != null)
	            responseSB.append(line);
	                 
	        // Close streams
	        br.close();
	        os.close();
	         
	        return responseSB.toString();
	         
	    }
	    
	    /**  
	     * description:��mapת����url������ʽ: name1=value1&name2=value2  
	     *   
	     * @param map  
	     * @return  
	     */  
	    public static String getUrlParamsFromMap(Map<String, String> map) {  
	        try {  
	            if (null != map) {  
	                StringBuilder stringBuilder = new StringBuilder();  
	                for (Map.Entry<String, String> entry : map.entrySet()) {  
	                    stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=")  
	                            .append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");  
	                }  
	                String content = stringBuilder.toString();  
	                if (content.endsWith("&")) {  
	                    content = StringUtils.substringBeforeLast(content, "&");  
	                }  
	                return content;  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	     
	    // Run this example
	    public static void main(String[] args) {
//	        try {
//	             
//	           // System.out.println(new POSTSenderExample().echoCuties("Hi there!"));
//	             
//	        } catch (IOException ioe) {
//	            ioe.printStackTrace();
//	        }
	    }


}
