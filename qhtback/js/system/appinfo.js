
$(function(){
	mySelect();
});


//查询
function mySelect() {
	var appcode = $("#select_appcode").val();
	if (appcode==null) {
		alert("出错");
		return;
	}
	
	var param = {
			"appcode":appcode
			};
	_init(getURL("system_appinfoselectlist"),8,param,cbOk,baseCBErr,cbClear);
}

//设置时间
function showSetTime(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	var actiontime = formatTime(record.actiontime)
	
	$("#set_appcode ").val(record.appcode);
	$("#set_time").val(actiontime);
	$("#set_new_time").val("");
	
	$('#set_appcode').attr('disabled',true);
	$('#set_time').attr('disabled',true);
	
	$('#setTime').modal('show');
}
function setTime() {
	var appcode = $("#set_appcode").val();
	if (appcode==null || appcode=='') {
		alert("appcode不能为空");
		return;
	}
	
	var actiontime = $("#set_new_time").val();
	if (actiontime==null || actiontime=='') {
		alert("请输入有效时间");
		return;
	}
	actiontime = actiontime+" 23:59:59";
	
	var parameter = {
			"appcode":appcode,
			"actiontime":actiontime
			};
	_send(getURL("system_appinfosettime"),parameter,cbSetTime,baseCBErr);
}
function cbSetTime() {
	$('#setTime').modal('hide');
	alert("修改成功");
	flushPage();
}


//编辑 微信信息
function showEditWC(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	var actiontime = formatTime(record.actiontime)
	
	$("#edit_appcode").val(record.appcode);
	$("#edit_wechat_code").val(record.wechat_code);
	$("#edit_wechat_p").val(record.wechat_p);
	$("#editnew_wechat_code").val("");
	$("#editnew_wechat_p").val("");
	
//	$('#edit_wechat_code').attr('disabled',true);
//	$('#edit_wechat_p').attr('disabled',true);
	$('#edit_appcode').attr('disabled',true);
	
	$('#editWC').modal('show');
}
function editWC() {
	var appcode = $("#edit_appcode").val();
	if (appcode==null || appcode=='') {
		alert("appcode不能为空");
		return;
	}
	var wechat_code = $("#edit_wechat_code").val();
	if (wechat_code==null || wechat_code=='') {
		alert("微信账号不能为空");
		return;
	}
	
	var wechat_p = $("#edit_wechat_p").val();
	if (wechat_p==null || wechat_p=='') {
		alert("图片地址不能为空");
		return;
	}
	
	var parameter = {
			"appcode":appcode,
			"wechat_code":wechat_code,
			"wechat_p":wechat_p
			};
	_send(getURL("system_appinfoeditwc"),parameter,cbEditWC,baseCBErr);
}
function cbEditWC() {
	$('#editWC').modal('hide');
	alert("修改成功");
	flushPage();
}


//添加记录
function addShow() {
	$('#addApp').modal('show');
}
function myAdd() {
//			$("#slideimg").focus();
//			$("#slidePic").focus();
	var appcode = $("#add_appcode").val();
	if (appcode=='') {
		alert("APP编号不能为空");
		return;
	}
	var agent = $("#add_agent").val();
	if (agent=='') {
		alert("代理不能为空");
		return;
	}
	var appname = $("#add_name").val();
	if (appname=='') {
		alert("app名称不能为空");
		return;
	}
	var appcompany = $("#add_appcompany").val();
	if (appcompany=='') {
		alert("隶属公司不能为空");
		return;
	}
	var actiontime = $("#add_actiontime").val();
	if (actiontime=='') {
		alert("有效时间不能为空");
		return;
	}
	var wechat_code = $("#add_wechat_code").val();
	if (wechat_code=='') {
		alert("微信账号不能为空");
		return;
	}
	var wechat_p = $("#slideimg").val();
	if (wechat_p=='') {
		alert("图片地址不能为空");
		return;
	}else{
		
	$("#slideimg").focus();
//			return false;
		}	
	var wechat_img = $("#slidePic").val();
	if (wechat_img=='') {
		alert("图片地址不能为空");
		return;
	}else{
	
	$("#slidePic").focus();
//			return false;
		}
	actiontime = actiontime+" 23:59:59";
	
	
	var parameter = {
			"appcode":appcode,
			"agent":agent,
			"appname":appname,
			"appcompany":appcompany,
			"actiontime":actiontime,
			"wechat_code":wechat_code,
			"wechat_p":wechat_p,
			"wechat_img":wechat_img
			};
	_send(getURL("system_appinfoinsertone"),parameter,cbAdd,baseCBErr);
}
function cbAdd(data) {
	$('#addApp').modal('hide');
	alert("添加成功");
	flushPage();
}


//清空查询数据
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

//查询成功回调函数
function cbOk(size,total,pageNum,pages,data) {
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
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].appcode+"</td>"+
							"<td>"+data[i].agent+"</td>"+
							"<td>"+data[i].appname+"</td>"+
							"<td>"+data[i].appcompany+"</td>"+
							"<td>"+formatTime(data[i].actiontime)+"</td>"+
							"<td>"+formatTime(data[i].createtime)+"</td>"+
							"<td>"+data[i].wechat_code+"</td>"+
							"<td>"+data[i].wechat_p+"</td>"+
							"<td>"+
							      "<span><a href="+data[i].wechat_img+"target='_blank'></a><img src="+data[i].wechat_img+" width='100' /></span>"+
							"</td>"+
							"<td>"+
							    "<span><a onclick='showSetTime("+data[i].id+");' href='#'>设置时间</a></span>"+"<br />"+
								"<span><a onclick='showEditWC("+data[i].id+");' href='#'>编辑微信信息</a></span>"+
							"</td>"+
						"</tr>");
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}

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

