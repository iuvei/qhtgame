var mapType = {
	    0:"全部",
		1:"使用",
		2:"停用",
		3:"登入"
};
var mapStatus = {
		1:"全部",
		2:"未同步",
		3:"已同步"
};
var mapTYPE = {
		1:"未同步",
		2:"同步成功",
		3:"同步失败"
};

var gdata;
function _myget(id) {
	for (var i=0; i < gdata.length; i++) {
		if (gdata[i].id==id)
			return gdata[i];
	}
}

$(function(){
	


	
	for(var index in mapStatus){
		$("#select_type").append("<option value='"+index+"'>"+mapStatus[index]+"</option>");
	}
	
	var today = new Date().Format("yyyy-MM-dd");
	if (today==null || today=='') {
		alert("获取当前时间出错");
		return;
	}
	today = formatDataInt(today);
	$('#now_time').val(today);
	
	$('#now_time').attr('disabled',true);
	$('#url_username').attr('disabled',true);
	$('#url_period').attr('disabled',true);
	$('#url_balance').attr('disabled',true);
	$('#url_amount').attr('disabled',true);
	$('#url_sealtime').attr('disabled',true);
	$('#url_opentime').attr('disabled',true);
	
	getuserinfo();
	mySelect();
	urlSelect();
	//getusertime();
	window.setInterval(urlSelect,9000);	//10s更新一次
	window.setInterval(flushPage,5000);	//10s更新一次
	window.setInterval(getuserinfo,15000);	//10s更新一次
	window.setInterval(timerr,1000);	//1s更新一次
});

function timerr(){
	
	   		var _seal = document.getElementById("url_sealtime").value;
	   		var _open = document.getElementById("url_opentime").value;
	   		
	   		if (_seal<=0 && _open<=0) {
	   			getuserinfo2();
	   		} else {
		   		_seal -= 1;
		   		if (_seal>=0)
		       	document.getElementById("url_sealtime").value = _seal;
		   		_open -= 1;
		   		if (_open>=0)
		       	document.getElementById("url_opentime").value = _open;
		      }
           }

//定时刷新页面
//function myrefresh()
//{
//     window.location.reload();
//}

//获取玩家及盘口信息
function getuserinfo2() {
	var param = {};
	_send(getHandicapURL("user_info"),param,cbUserInfo2,baseCBErr);
}
function cbUserInfo2(data) {
	var today = new Date().Format("yyyy-MM-dd");
		if (today==null || today=='') {
			alert("获取当前时间出错");
			return;
		}
	today = formatDataInt(today);
	$('#now_time').val(today);
	
	if(data==null){
		$('#url_username').val('');
		$('#url_period').val('');
		$('#url_balance').val('');
		$('#url_amount').val('');
		$('#url_sealtime').val('');
		$('#url_opentime').val('');
	
	} else {
//		$('#url_username').val(data.username);
//		$('#url_period').val(data.period);
//		$('#url_balance').val(data.balance);
//		$('#url_amount').val(data.amount);
		$('#url_sealtime').val(data.sealtime);
		$('#url_opentime').val(data.opentime);
	}
}

//获取玩家及盘口信息
function getuserinfo() {
	var param = {};
	_send(getHandicapURL("user_info"),param,cbUserInfo,baseCBErr);
}
function cbUserInfo(data) {
	var today = new Date().Format("yyyy-MM-dd");
		if (today==null || today=='') {
			alert("获取当前时间出错");
			return;
		}
	today = formatDataInt(today);
	$('#now_time').val(today);
	
	if(data==null){
		$('#url_username').val('');
		$('#url_period').val('');
		$('#url_balance').val('');
		$('#url_amount').val('');
		$('#url_sealtime').val('');
		$('#url_opentime').val('');
	
	} else {
		$('#url_username').val(data.username);
		$('#url_period').val(data.period);
		$('#url_balance').val(data.balance);
		$('#url_amount').val(data.amount);
//		$('#url_sealtime').val(data.sealtime);
//		$('#url_opentime').val(data.opentime);
	}
}

  
//获取判断验证码192.168.1.114:8180/handicap

function mypp(){
	var param = {};
	_send(getHandicapURL("url_mypp"),param,urlcheck,baseCBErr);
}
function urlcheck() {
//	$("#Img").attr("src","http://192.168.1.113:8180/handicap/check.png?rnd=" + Math.random());
//	$("#Img").attr("src","http://119.23.125.241:8180/handicap/check.png?rnd=" + Math.random());
	$("#Img").attr("src",getChackURL());
}

//登入
function mylg(){
	var check = $("#tocheck").val();
	if (check==null || check=='') {
		alert("验证码不能为空");
		return;
	}
	var param = {
		"check":check
	};
	_send(getHandicapURL("url_lg"),param,urllg,baseCBErr);
}
function urllg() {
	$('#mylg').modal('hide');
	$("#tocheck").val("");
	$("#Img").attr("src","#");
	alert("登入成功");
//	$("#cid").hide();
	urlSelect();
	getuserinfo();
//	flushPage();
}

//获取站点信息
function urlSelect() {
	var param = {};
	_send(getHandicapURL("url_select"),param,cbURLSelect,baseCBErr);
}

//清除
function cbOddClear() {
	if (gdata==null)
		return;
	
	if (gdata!=null) {
		for (var i=0; i < gdata.length; i++) {
			var tmpid = gdata[i].id+999999999;
			$("tr[id='"+tmpid+"']").remove();
		}
	}
	gdata=null;
}

function cbURLSelect(data) {
	cbOddClear();
	var bresult = 0;
	for (var i=0; i < data.length; i++) {
		var tmpid = data[i].id+999999999;
		var _type = data[i].status;
		var strType = mapType[data[i].status];
		
		if(bresult==0 && _type==1){
			bresult = 1;
		}
		
		$("#mytwo").append(
						"<tr id="+tmpid+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].name+"</td>"+
							"<td>"+data[i].url+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+strType+"</td>"+
							"<td>"+
								"<span><a onclick='useweb("+data[i].id+");' href='#'>启用</a></span>"+"<br />"+
								"<span><a onclick='dontweb("+data[i].id+");' href='#'>停用</a></span>"+"<br />"+
								"<span><a onclick='editweb("+data[i].id+");' href='#'>编辑</a></span>"+"<br />"+
							"</td>"+
						"</tr>");
	}

	if(bresult==1){
		$("#cid").show();
	} else {
		$("#cid").hide();
	}

	gdata = data;
}

//启用
function useweb(id) {
	var type = 1;
	type = parseInt(type);
	id = parseInt(id);
	var parameter = {
		    "id":id,
			"type":type
			};
	_send(getHandicapURL("use_web"),parameter,cbUseWeb,baseCBErr);
}
function cbUseWeb() {
	$('#useweb').modal('hide');
	alert("启用成功");
	urlSelect();
//	flushPage();
}

//禁用
function dontweb(id) {
	
	var type = 2;
	type = parseInt(type);
	id = parseInt(id);
	var parameter = {
		    "id":id,
			"type":type
			};
	_send(getHandicapURL("dont_web"),parameter,cbDownWeb,baseCBErr);
}
function cbDownWeb() {
	$('#dontweb').modal('hide');
	alert("禁用成功");
	urlSelect();
	getuserinfo();
	flushPage();
}

//编辑
function editweb(id) {
	var record = _myget(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#edit_name ").val(record.name);
	$("#edit_id ").val(record.id);
	$("#edit_username ").val(record.username);
	$("#edit_url").val(record.url);
	$("#edit_password").val(record.password);
	$("#edit_password_again").val('');
	
	$('#edit_name').attr('disabled',true);
	
	$('#seteditweb').modal('show');
}
function seteditweb() {
	var name = $("#edit_name").val();
	var id = $("#edit_id").val();
	
	if (id=='')
		id = 0;
	else
		id = parseInt(id);
		
	var username = $("#edit_username").val();
	if (username==null || username=='') {
		alert("用户名不能为空");
		return;
	}
	var url = $("#edit_url").val();
	if (url==null || url=='') {
		alert("服务器地址不能为空");
		return;
	}
	
	var newPassword = $("#edit_password").val();
	if (newPassword=='') {
		alert("密码不能为空");
		return;
	}
	
	var newPasswordAgain = $("#edit_password_again").val();
	if (newPasswordAgain=='') {
		alert("确认密码不能为空");
		return;
	}
	
	if (newPassword!=newPasswordAgain) {
		alert("两次密码不一致");
		return;
	}
	
	var parameter = {
			"id":id,
			"username":username,
			"url":url,
			"password":newPassword
			};
	_send(getHandicapURL("edit_web"),parameter,cbEditweb,baseCBErr);
}
function cbEditweb() {
	$('#seteditweb').modal('hide');
	alert("修改成功");
	flushPage();
}



//查询  飞盘明细
function mySelect() {
	var type = $("#select_type option:selected").val();
	if (type=='')
		type = 1;
	else
		type = parseInt(type);
	var param = {
			"type":type
			};
	_init(getHandicapURL("select_web"),6,param,cbOk,baseCBErr,cbClear);
}


//清空查询数据 飞盘明细
function cbClear(data) {
	if (data!=null) {
		for (var i=0; i < data.length; i++) {
			$("tr[id='"+data[i].id+"']").remove();
		}
	}
	$("#size").html(0);
	$("#total").html(0);
	$("#pageNum").html(0);
	$("#pages").html(0);
	$("#change").val("");
}

//查询成功回调函数   飞盘明细
function cbOk(size,total,pageNum,pages,data,amdata) {
	$("#error").html("");
	if (data==null || data.length==0) {
		$("#error").html('no data');
		$("#size").html(0);
		$("#total").html(0);
		$("#pageNum").html(0);
		$("#pages").html(0);
		$("#change").val("");
		return;
	}
	for (var i=0; i < data.length; i++) {
		
		var odd = data[i].odd;
		var period = data[i].period;
		var runway = data[i].runway;
		var bettype = data[i].bettype;
		var betamount = data[i].betamount;
		
		var status = mapTYPE[data[i].status];
		
		var context = "";
		var way = "";
		
		if (runway>=1 && runway<=10) {
			way += "第&nbsp;"+runway+"&nbsp;道"+"<br>";
			if (bettype<11) {
			context += bettype+"&nbsp;"+"<br>";
		}
		}
		
		if (runway==11) {
			way += "冠亚"+"<br>";
			if (bettype<20) {
			context += "&nbsp;"+bettype+"&nbsp;"+"<br>";
		}
		}
		
		if (bettype==20) {
			context += "大&nbsp;"+"<br>";
		}
		if (bettype==21) {
			context += "小&nbsp;"+"<br>";
		}
		if (bettype==22) {
			context += "单&nbsp;"+"<br>";
		}
		if (bettype==23) {
			context += "双&nbsp;"+"<br>";
		}
		if (bettype==24) {
			context += "龙&nbsp;"+"<br>";
		}
		if (bettype==25) {
			context += "虎&nbsp;"+"<br>";
		}

		var context = "<tr id="+data[i].id+">"+
			"<td>"+odd+"</td>"+
			"<td>"+period+"</td>"+
			"<td>"+betamount+"</td>"+
			"<td>"+way+"</td>"+
			"<td>"+context+"</td>"+
			"<td>"+status+"</td>"+
			
		 "</tr>";
		
		$("#mythree").append(context);
		

		
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}


//上分  分页
//刷新
function flushPage() {
	_sendPage(cbOk,baseCBErr,cbClear);
}

//首页
function firstPage() {
	_firstPage(cbOk,baseCBErr,cbClear);
}

//尾页
function endPage() {
	_endPage(cbOk,baseCBErr,cbClear);
}

//上一页
function upPage() {
	_upPage(cbOk,baseCBErr,cbClear);
}

//下一页
function downPage() {
	_downPage(cbOk,baseCBErr,cbClear);
}

//跳转
function changePage() {
	var index = $("#change").val();
	_changePage(index,cbOk,baseCBErr,cbClear);
}


