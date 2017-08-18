package com.tls.sigcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.util.HttpsUtil;

public class TlsSig {
	
	public static final String APP_GROUP_ID = "IM_GAME";//APP群组ID
	public static final String APP_GROUP_ADMIN = "app_admin";//APP群组管理员账号
	public static final String APP_GROUP_CUSTOM = "app_custom";//APP群组客服
	public static final String PK10_GROUP_ID = "IM_PK10";//PK10群组ID
	public static final String PK10_GROUP_ADMIN = "pk10_admin";//PK10群组管理员账号
	public static final String PK10_GROUP_CUSTOM = "pk10_custom";//PK10群组客服
	
	public static final String SDK_APP_ID = "1400026181";	//1400026181我 1400027969服 1400029123张 1400026315其 
	public static final String ADMIN_IDENTIFIER = "admin";
	public static final String ACCOUNT_TYPE = "11097";		//11097我 	11698服 	11698张 11097其 
	private static String URL_PARAM = "";

	public static final int account_import = 1;	//账号导入
	public static final int create_group = 2;	//创建群
	public static final int add_group_member = 3;	//增加群成员 
	public static final int send_group_msg = 4;	//发送群消息
	public static final int friend_add = 5;	//添加好友
	public static final int group_msg_get_simple = 6;	//获取群历史消息
	public static final int group_msg_system = 7;	//在群组中发送系统消息
	
	
	private static tls_sigcheck tlsSig;
	private static String priKey;
	private static String pubKey;

	public static boolean init() {
		boolean result = false;
		try {
			tlsSig = new tls_sigcheck();
			URL url = Thread.currentThread().getContextClassLoader().getResource("");
			if (url==null)
				return false;
			String path = url.getPath();
			if (path==null)
				return false;
			tlsSig.loadJniLib(path+"jnisigcheck.dll");
			
			//读取私钥
			File priKeyFile = new File(path+"private_key_"+SDK_APP_ID);
	        StringBuilder strPriBuilder = new StringBuilder();
	        String priS = "";
	        BufferedReader priBr = new BufferedReader(new FileReader(priKeyFile));
	        while ((priS = priBr.readLine()) != null) {
	        	strPriBuilder.append(priS + '\n');
	        }
	        priBr.close();
	        priKey = strPriBuilder.toString();
	        
	        //读取公钥
	        File pubKeyFile = new File(path+"public_key_"+SDK_APP_ID);
	        StringBuilder strPubBuilder = new StringBuilder();
	        String pubS = "";
	        BufferedReader pubBr = new BufferedReader(new FileReader(pubKeyFile));
	        while ((pubS = pubBr.readLine()) != null) {
	        	strPubBuilder.append(pubS + '\n');
	        }
	        pubBr.close();
	        pubKey = strPubBuilder.toString();  
	        
	        String sig = generatorUsersig(ADMIN_IDENTIFIER);
	        if (sig==null)
	        	return false;
	        URL_PARAM = "?usersig="+sig+"&identifier="+ADMIN_IDENTIFIER+"&sdkappid="+SDK_APP_ID+"&random=99999999&contenttype=json";

	        result = true;
		} catch(Exception e) {
			result = false;
		}
        return result;
	}
	
	public static String generatorUsersig(String username) {
		String result = null;
		try {
	        if (tlsSig.tls_gen_signature_ex2(SDK_APP_ID, username, priKey)==0) {
	        	if (tlsSig.tls_check_signature_ex2(tlsSig.getSig(), pubKey, SDK_APP_ID, username)==0)
	        	result = tlsSig.getSig();
	        }
		} catch (Exception e) {
			result = null;
		}
		return result;
	}
	
	public static boolean checkUsersig(String username, String sig) {
		boolean result = false;
		try {
	        if (tlsSig.tls_check_signature_ex2(sig, pubKey, SDK_APP_ID, username)==0)
	        	result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public static String doPost(int type, String data) {
		String url = null;
		switch (type) {
		case account_import:
			url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import" + URL_PARAM;
			break;
		case create_group:
			url = "https://console.tim.qq.com/v4/group_open_http_svc/create_group" + URL_PARAM;
			break;
		case add_group_member:
			url = "https://console.tim.qq.com/v4/group_open_http_svc/add_group_member" + URL_PARAM;
			break;
		case friend_add:
			url = "https://console.tim.qq.com/v4/sns/friend_add" + URL_PARAM;
			break;
		case send_group_msg:
			url = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg" + URL_PARAM;
			break;
		case group_msg_get_simple:
			url = "https://console.tim.qq.com/v4/group_open_http_svc/group_msg_get_simple" + URL_PARAM;
			break;
		case group_msg_system:
			url = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_system_notification" + URL_PARAM;//?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json";
			break;
		default:
			url = null;
			break;
		}
		
		if (url==null)
			return null;
		
		String result = null;
		try {
			result = HttpsUtil.post(url, data);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			result = null;
			System.out.println(e);
		}
		if (result==null)
			return null;
		
		return result;
	}
}
