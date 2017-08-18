/*
 * 手机短信管理
 */
package com.logic.mgr;

import com.node.AuthCode;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/11.
 */
public class AuthCodeMgr {
    private static Map<String, AuthCode> authCodes = new HashMap<String, AuthCode>();    //Map<tel,AuthCode>

    public static synchronized void put(String tel, AuthCode authCode) {
        authCodes.put(tel, authCode);
    }

    public static synchronized boolean remove(String tel) {
        AuthCode authCode = authCodes.remove(tel);
        if (authCode == null) {
            return false;
        }
        return true;
    }

    public static synchronized AuthCode get(String tel) {
        return authCodes.get(tel);
    }

    //------------------------------------------------------------------------------------------------啊啊啊

    public static synchronized Map<String,String> getAuthCode(String APP_CODE, String phone, int type) throws Exception {
        // 生成6位随机数
        char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String randomCode = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);// random.nextInt(10));
            code.append(randomCode);
        }
//        logger.info("验证码发送:" + phone + "验证码内容" + code.toString());
        // 发送短信
        String content = "";
        if (type == 0) {// 注册
            content = "尊敬的会员，感谢您注册会员，您的验证码是" + code + "，如非本人操作，可不用理会！";
        } else if (type == 1) {// 找回密码
            content = "尊敬的会员，您正在进行找回密码操作，您的验证码是" + code + "。";
        }
        String account = "";
        String password = "";
        if(APP_CODE.equals("GS1001")){
            account = "qianht888_kz";
            password = "Kunze18866";
        }
        //创蓝短信
        Map<String,String> resultMap = getResp(account, password, phone, content);
//        logger.info("创蓝短信:"+resultMap.get("respstatus")+"-"+resultMap.get("respstatusMsg"));
        if (resultMap.get("respstatus").equals("0")) {
//            logger.info("短信发送成功");
            // 保存验证码到缓存中
            AuthCode authCode = new AuthCode();
            authCode.setCode(code.toString());
            authCode.setTel(phone);
            authCode.setTime(System.currentTimeMillis());
            authCode.setType(type);
            put(phone,authCode);
            return null;
        }
        return resultMap;
    }

    public static final String url = "http://222.73.117.158/msg/";// 应用地址
    public static final boolean needstatus = true;// 是否需要状态报告，需要true，不需要false

	/*	平台地址：http://222.73.117.156/msg/index.jsp
     行业帐号IP为222.73.117.158，PORT默认为80.
	 营销帐号IP为222.73.117.169，PORT默认为80.
	账号：qianht123
	密码：Tch888888	*/

    /**
     * 发送短信
     *
     * @param account 账号
     * @param pswd    密码
     * @param mobile  手机号码，多个号码使用","分割
     * @param msg     短信内容
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */

    public static String batchSend(String account, String pswd, String mobile, String msg) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        try {
            URI base = new URI(url, false);
            method.setURI(new URI(base, "HttpBatchSendSM", false));
            method.setQueryString(new NameValuePair[]{
                    new NameValuePair("account", account),
                    new NameValuePair("pswd", pswd),
                    new NameValuePair("mobile", mobile),
                    new NameValuePair("needstatus", String.valueOf(needstatus)),
                    new NameValuePair("msg", msg),
            });
            int result = client.executeMethod(method);
            if (result == HttpStatus.SC_OK) {
                InputStream in = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return URLDecoder.decode(baos.toString(), "UTF-8");
            } else {
                throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
            }
        } finally {
            method.releaseConnection();
        }
    }

    public static Map<String,String> getResp(String account, String pswd, String mobile, String msg) throws Exception {
        String lines = batchSend(account, pswd, mobile, msg);
        lines = lines.replace("\n", ",");
        String[] str = lines.split(",");
        String respstatus = str[1];  //状态码
        String respstatusMsg = getRespstatusMsg(respstatus); //状态码对应的说明
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("respstatus", respstatus);
        resultMap.put("respstatusMsg", respstatusMsg);
        return resultMap;
    }

    /**
     * 获取状态码对应说明
     *
     * @param respstatus 状态码
     * @return
     */
    public static String getRespstatusMsg(String respstatus) {
        String respstatusMsg = null;
        String respstatusMsgs[][] = {
                {"0", "101", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120"},
                {"提交成功", "无此用户", "密码错", "提交过快（提交速度超过流速限制）", "系统忙（因平台侧原因，暂时无法处理提交的短信）", "敏感短信（短信内容包含敏感词）", "消息长度错（>536或<=0）"
                        , "包含错误的手机号码", "手机号码个数错（群发>50000或<=0;单发>200或<=0）", "无发送额度（该用户可用短信数已使用完）", "不在发送时间内", "超出该账户当月发送额度限制", "无此产品，用户没有订购该产品"
                        , "extno格式错（非数字或者长度不对）", "自动审核驳回", "签名不合法，未带签名（用户必须带签名的前提下）", "IP地址认证错,请求调用的IP地址不是系统登记的IP地址", "用户没有相应的发送权限", "用户已过期", "测试内容不是白名单"}};

        for (int i = 0; i < respstatusMsgs.length; i++) {
            for (int j = 0; j < respstatusMsgs[i].length; j++) {
                if (respstatusMsgs[0][j].equals(respstatus)) {
                    respstatusMsg = respstatusMsgs[1][j];
//                    log.info("respstatusMsg："+respstatusMsg);
                    continue;
                }
            }
        }
        return respstatusMsg;
    }

}
