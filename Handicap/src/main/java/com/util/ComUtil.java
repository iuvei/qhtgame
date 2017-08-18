package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ComUtil {

	public static String get32UUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}
	/*yyyyMMddHHMMSS*/
	public static String getOrderNo() {
		String orderNo = "";
		String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
		String sdf = new SimpleDateFormat("SS").format(new Date());
		orderNo = trandNo.toString().substring(0, 4);
		orderNo = orderNo + sdf ;
		return orderNo ;    
	}
	
	/* 把16进制字符串转换成字节数组  
	    * @param hex  
	    * @return  
	    */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length()/2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i=0;i<len;i++) {
			int pos = i*2;
			result[i]=(byte)(toByte(achar[pos]) << 4 | toByte(achar[pos+1]));
		}
		return result;
	}
	
	private static byte toByte(char c) {
		return (byte)("0123456789ABCDEF".indexOf(c));
	}

	/** *//**  
	    * 把字节数组转换成16进制字符串  
	    * @param bArray  
	    * @return  
	    */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
		if (sTemp.length() < 2)
			sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
}
