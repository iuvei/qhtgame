package com.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
 
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
 
public class HttpsUtil {
 
    private static class TrustAnyTrustManager implements X509TrustManager {
 
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
 
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
 
    /**
     * post��ʽ���������(httpsЭ��)
     * 
     * @param url
     *            �����ַ
     * @param content
     *            ����
     * @param charset
     *            ����
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public static byte[] _post(String url, String content, String charset)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                new java.security.SecureRandom());
 
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setDoOutput(true);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // ˢ�¡��ر�
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        if (is != null) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            return outStream.toByteArray();
        }
        return null;
    }
    
    public static String post(String url, String content)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
    	byte[] arrByte = _post(url,content,"utf-8");
    	if (arrByte!=null)
    		return new String(arrByte);
    	return null;
    }
    
    public static String getPostData(HttpServletRequest request) {
    	
        byte[] buffer = null;
        try {
        	request.setCharacterEncoding("UTF-8");
        	StringBuffer data = new StringBuffer();
        	String line = null;
        	BufferedReader reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
            if (data!=null) 
            	buffer = data.toString().getBytes();
        } catch (Exception e) {
        	buffer = null;
        } finally {
        }
        
        return new String(buffer);
    }
    
    public static String getPostDataByBinary(HttpServletRequest request) {
    	
    	byte[] buffer = null;
		try {
			request.setCharacterEncoding("UTF-8");
			int len = request.getContentLength();
	    	ServletInputStream iii = request.getInputStream();
			buffer = new byte[len];
	    	iii.read(buffer, 0, len);
	    	//buffer = new String(buffer).toString().getBytes("UTF-8");
		} catch (IOException e) {
			buffer = null;
		}
    	
		return new String(buffer);
	}
 
}