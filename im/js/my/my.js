//var SERVER = "119.23.125.241:8080";
//var SERVER = "192.168.1.113:8080";
var SERVER = "localhost:8080";

var GAME = "/qhtgame/";
var mapURL = {
	"player_register":"player/register.do",
	"app_customlogin":"app/customlogin.do",
	"player_login":"player/login.do",
	"app_getiminfo":"app/getiminfo.do"
};
function getURL(url) {
	return "http://"+SERVER+GAME+mapURL[url];
}

function myShowRegister() {
	$('#my_login_dialog').modal('hide');
	$('#my_register_dialog').modal('show');
}

function myShowLogin() {
	$('#my_register_dialog').modal('hide');
	$('#my_login_dialog').modal('show');
}

function myRegister() {
	var appcode = 'GS1001';
	var agent = 'stt';
	var username = $("#my_register_account").val();
	var password = $("#my_register_pwd").val();
	var passwordconfirm = $("#my_register_pwd_confirm").val();
	var qq = $("#my_register_qq").val();
	var weixin = $("#my_register_weixin").val();
	var telephone = $("#my_register_telephone").val();
	
	if (username.length == 0) {
        alert('请输入用户名');
        return;
    }
	if (password.length == 0) {
        alert('请输入密码');
        return;
    }
	if (passwordconfirm.length == 0) {
        alert('请输入确认密码');
        return;
    }
	if (qq.length == 0) {
        alert('请输入QQ');
        return;
    }
	if (weixin.length == 0) {
        alert('请输入微信');
        return;
    }
	if (telephone.length == 0) {
        alert('请输入手机号');
        return;
    }
	
	if (password!=passwordconfirm) {
		alert('两次密码不一致');
        return;
	}
	
	var data = {
		"appcode":appcode,
		"agent":agent,
		"username":username,
		"password":password,
		"qq":qq,
		"weixin":weixin,
		"telephone":telephone
	};
	
	send(getURL("player_register"),data,registerOK,comError);
}

var registerOK = function(data) {
	if (data.code==1000) {
		alert("注册成功");
		$('#my_register_dialog').modal('hide');
		$('#my_login_dialog').modal('show');
	} else {
		alert("注册失败："+data.desc);
	}
}


//---------------------//

function myCustom() {
	var username = $("#my_login_account").val();
	var password = $("#my_login_pwd").val();
	if (username.length == 0) {
        alert('请输入用户名');
        return;
    }
    if (password.length == 0) {
        alert('请输入密码');
        return;
    }
    
	var data = {
		"username":username,
		"password":password
	};
	
	loginInfo.identifier = username;
	send(getURL("app_customlogin"),data,customOk,comError);
}
var customOk = function (data) {
	if (data.code!=1000) {
		alert("登入失败");
		return;
	}
	var info = data.info;
	loginInfo.userSig = info.userSig;
    loginInfo.sdkAppID = info.sdkAppId;
    loginInfo.accountType = info.accountType;
    $('#my_login_dialog').modal('hide');
    webimLogin();
}



function myLogin() {
	var appcode = 'GS1001';
	var username = $("#my_login_account").val();
	var password = $("#my_login_pwd").val();
	if (username.length == 0) {
        alert('请输入用户名');
        return;
    }
    if (password.length == 0) {
        alert('请输入密码');
        return;
    }
    
	var data = {
		"appcode":appcode,
		"username":username,
		"password":password
	};
	
	//loginInfo.identifier = username;
	send(getURL("player_login"),data,loginOk,comError);
}
var loginOk = function (data) {
	if (data.code!=1000) {
		alert("登入失败");
		return;
	}
	loginInfo.identifier = data.info.username;
	getInfo();
}

function getInfo() {
	send(getURL("app_getiminfo"),{},infoOk,comError);
}
var infoOk = function(data) {
	if (data.code!=1000) {
		alert("获取信息失败");
		return;
	}
	var info = data.info;
    loginInfo.userSig = info.userSig;
    loginInfo.sdkAppID = info.sdkAppId;
    loginInfo.accountType = info.accountType;
    $('#my_login_dialog').modal('hide');
    webimLogin();
}




//-----------------------//
var comError= function(data) {
	alert("error");
}
function send(url,data,cbOK,cbError) {
	$.ajax({
		data:data,
		type:"POST",
		dataType: 'json',
		url:url,
		xhrFields:{withCredentials: true},
		error:cbError,
		success:cbOK
	});
}
