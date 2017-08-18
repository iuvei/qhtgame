package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpUtil {
    /**
     * 鍚戞寚瀹歎RL鍙戦�丟ET鏂规硶鐨勮姹�
     * 
     * @param url
     *            鍙戦�佽姹傜殑URL
     * @param param
     *            璇锋眰鍙傛暟锛岃姹傚弬鏁板簲璇ユ槸 name1=value1&name2=value2 鐨勫舰寮忋��
     * @return URL 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
     */
    public static String sendGet(String url, String param,int timeout) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if (param!=null)
            	urlNameString += "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            //System.out.println(e);
            //e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 鍚戞寚瀹� URL 鍙戦�丳OST鏂规硶鐨勮姹�
     * 
     * @param url
     *            鍙戦�佽姹傜殑 URL
     * @param param
     *            璇锋眰鍙傛暟锛岃姹傚弬鏁板簲璇ユ槸 name1=value1&name2=value2 鐨勫舰寮忋��
     * @return 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
     */
    public static String sendPost(String url, String param,int timeout) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            conn.setRequestProperty("Accept-Charset","UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            //System.out.println("鍙戦�� POST 璇锋眰鍑虹幇寮傚父锛�" + e);
            //e.printStackTrace();
        	//System.out.println("鏇存柊鏁版嵁澶辫触");
        	result = null;
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    
    /**
     * 鐢ˋpache 灏佽濂界殑HttpClient鍚戞寚瀹歎RL鍙戦�丟ET鏂规硶鐨勮姹�
     * 
     * @param url
     *            鍙戦�佽姹傜殑URL
     * @param headMap
     *            璇锋眰澶村弬鏁帮紝鍔犲埌http head涓殑鍙傛暟璁剧疆
     * @param bodyMap
     *            璇锋眰鍙傛暟
     * @return URL 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
     */
    public static String httpGet(String url,Map<String,String> headMap, Map<String,String> bodyMap) throws Exception{
    	
    	String param = "";
        if (bodyMap!=null) {
        	for (Map.Entry<String,String> entry : bodyMap.entrySet()) {
        		if (param.length()==0)
        			param += "?";
        		else
        			param += "&";
        		param += entry.getKey();
        		param += "=";
        		param += entry.getValue();
        	}
        }
        
        
        @SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url+param);

        if (headMap!=null) {
        	for (Map.Entry<String,String> entry : headMap.entrySet()) {
        		httpGet.addHeader(entry.getKey(),entry.getValue());
        	}
        }
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpResponse response = httpClient.execute(httpGet);

        return EntityUtils.toString(response.getEntity(), "utf-8");
    }
    
    /**
     * 鐢ˋpache 灏佽濂界殑HttpClient鍚戞寚瀹歎RL鍙戦�丳OST鏂规硶鐨勮姹�
     * 
     * @param url
     *            鍙戦�佽姹傜殑URL
     * @param headMap
     *            璇锋眰澶村弬鏁帮紝鍔犲埌http head涓殑鍙傛暟璁剧疆
     * @param bodyMap
     *            璇锋眰鍙傛暟
     * @return URL 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
     */
    @SuppressWarnings("resource")
	public static String httpPost(String url,Map<String,String> headMap, Map<String,String> bodyMap) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        if (headMap!=null) {
        	for (Map.Entry<String,String> entry : headMap.entrySet()) {
        		httpPost.addHeader(entry.getKey(),entry.getValue());
        	}
        }
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (bodyMap!=null) {
        	for (Map.Entry<String,String> entry : bodyMap.entrySet()) {
        		nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        	}
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        HttpResponse response = httpClient.execute(httpPost);

        return EntityUtils.toString(response.getEntity(), "utf-8");
    }
    
    
    
    
    
    public static boolean getFile(String url, String filePathName) {
    	
    	boolean flag = false;
    	
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		//httpclient = wrapClient(httpclient);
		try {
			HttpGet httpget = new HttpGet(url);
			
			httpget.setHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			// Execute HTTP request
			System.out.println("executing request " + httpget.getURI());
			HttpResponse response = httpclient.execute(httpget);
			
			File storeFile = new File(filePathName);
			FileOutputStream output = new FileOutputStream(storeFile);
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte b[] = new byte[1024];
					int j = 0;
					while( (j = instream.read(b))!=-1){
					output.write(b,0,j);
					}
					output.flush();
					output.close();
					
					flag = true;
				} catch (IOException ex) {
					// In case of an IOException the connection will be released
					// back to the connection manager automatically
					throw ex;
				} catch (RuntimeException ex) {
					// In case of an unexpected exception you may want to abort
					// the HTTP request in order to shut down the underlying
					// connection immediately.
					httpget.abort();
					throw ex;
				} finally {
					// Closing the input stream will trigger connection release
					try { instream.close(); } catch (Exception ignore) {}
				}
			}
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
			System.out.println(e);
			System.out.println(e.getMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return flag;
	}
    
    
    public static HttpClient wrapClient(org.apache.http.client.HttpClient base) {  
        try {  
            SSLContext ctx = SSLContext.getInstance("TLS");  
            X509TrustManager tm = new X509TrustManager() {  
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
                //public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
                //public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
                @Override  
                public void checkClientTrusted(  
                        java.security.cert.X509Certificate[] chain,  
                        String authType)  
                        throws java.security.cert.CertificateException {  
                    // TODO Auto-generated method stub  
                      
                }  
                @Override  
                public void checkServerTrusted(  
                        java.security.cert.X509Certificate[] chain,  
                        String authType)  
                        throws java.security.cert.CertificateException {  
                    // TODO Auto-generated method stub  
                      
                }  
            };  
            ctx.init(null, new TrustManager[] { tm }, null);  
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
            SchemeRegistry registry = new SchemeRegistry();  
            registry.register(new Scheme("https", 443, ssf));  
            ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);  
            return new DefaultHttpClient(mgr, base.getParams());  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return null;  
        }  
    }
}
