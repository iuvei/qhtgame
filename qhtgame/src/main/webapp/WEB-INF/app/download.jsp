<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String appcode = request.getParameter("appcode");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!--禁止浏览器缩放-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="application/xhtml+xml;charset=UTF-8" http-equiv="Content-Type"/>
    <!--清除浏览器缓存-->
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <meta http-equiv="expires" content="Wed, 26 Feb 1997 08:21:57 GMT">
    <!--禁止格式检测，不设置的话 如果写了一个Email 点击就会跳转到邮箱-->
    <meta name="format-detection" content="telephone=no,email=no,date=no,address=no">
    <!--屏幕顶部条的颜色-->
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
    <title>下载</title>
    <script src="js/jquery-2.1.4.js"></script>
    
    <style type="text/css">
        body{
        margin: 0px;
        padding: 0px;
        background: #0f83c7;
        text-align: center;
        }

        .bg{
        display: block;
        margin: 0px;
        padding: 0px;
        border: none;
        width: 100%;
        position: relative;
        }
        .bottom{
        position: relative;
        display: inline-block;
        width: 40%;
        border-style: solid;
        border-width:1px;
        border-color: #909090;
        margin: 0px 6px;
        }
        img{
        display: block;
        width: 100%;
        }
        .mask{
        position: fixed;
        display: none;
        width: 100%;
        height: 100%;
        z-index: 1000;
        top: 0px;
        left: 0px;
        }
    </style>

</head>
<body>
	
	
    <img class="bg"  src="img/bg.png">
	<%-- <h1 style="font-size:70%;text-indent: 2em; padding-left: 3em;text-align:left;padding-right: 3em;color:white">
	<%=appname%>率先为千亿无卡支付市场推出国内顶级移动支付APP，是目前移动支付中，交易量最大，人气活跃度最高，也是中国互联网+支付领域的创新企业。</h1> --%>
    <a href="android/app-<%=appcode%>-download.apk" class="bottom android" id="bottom1">
    
    <img src="img/android.jpg" >
	</a>
	<a href="https://www.pgyer.com/CarGameGS1001" class="bottom ios" id="bottom2">
    <img src="img/ios.jpg" >
	</a>


<img id="mask" class="mask" src="img/mask.jpg">


<script type="text/javascript">

<%-- window.onload = function()
{
	
	document.title="<%=appname%>下载";
	u = navigator.userAgent ;
	var isWeixin = !!u.match(/MicroMessenger/);
	if(isWeixin){
		 document.getElementById("mask").style.display = "block";
	}
	if('<%=appcode%>'=='B004'){
		document.getElementById("bottom2").href='';
		}
}

function iosJump(){
	if('<%=appcode%>'=='B004'){
		document.getElementById("bottom2").href='https://itunes.apple.com/us/app/%E7%94%A8%E5%91%97/id1216127077?mt=8';
		}
	} --%>


function isWeiXin()
{
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}

</script>
</body>
</html>