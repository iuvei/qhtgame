


function myLogin(){
		$("#errname").text("");
		$("#errpwd").text("");
		
		var username = $("#username").val();
		if (username=="") {
			$("#errname").attr("class","error");
			$("#errname").text("用户名不能为空");
			return;
		}
		
		var password = $("#password").val();
		if (password=="") {
			$("#errpwd").attr("class","error");
			$("#errpwd").text("密码不能为空");
			return;
		}
		
		var parameter = {
				"username":username,
				"password":password
				};
		_send(getURL("login"),parameter,cbLogin,baseCBErr);//"../admin/login.do"
		return;
}
function cbLogin(data) {
	localStorage.setItem("username",data);
	window.location.href="main.html";
}
/*
function cbErr(code,desc) {
	alert(code+"-"+desc);
}*/