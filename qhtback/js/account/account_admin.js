
var mapType = {
		0:"全部",
		1:"超级账号",
		2:"代理账号",
		3:"App账号"
};
var mapStatus = {
		0:"全部",
		1:"有效",
		2:"禁用"
};


$(function(){
	for(var index in mapType){
		$("#type").append("<option value='"+index+"'>"+mapType[index]+"</option>");
	}
	for(var index in mapStatus){
		$("#status").append("<option value='"+index+"'>"+mapStatus[index]+"</option>");
	}
	
	for(var index in mapType){
		if (index!=0 && index!=1)
			$("#add_type").append("<option value='"+index+"'>"+mapType[index]+"</option>");
	}
	for(var index in mapStatus){
		if (index!=0)
			$("#add_status").append("<option value='"+index+"'>"+mapStatus[index]+"</option>");
	}
	
	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-ddThh:mm"));	//默认提前一周
	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
	mySelect();
});


//查询
function mySelect() {
	var username = $("#username").val();
	var nickname = $("#nickname").val();
	var type = $("#type option:selected").val();
	var status = $("#status option:selected").val();
	if (type=='')
		type = 0;
	else
		type = parseInt(type);
	if (status=='')
		status = 0;
	else
		status = parseInt(status);
	
	var begintime = $("#startcreatetime").val();
	var endtime = $("#endcreatetime").val();
	if (	begintime==null || begintime=='' ||
			endtime==null || endtime=='' ) {
		alert("请选择时间");
		return;
	}
	begintime = formatDatatime_local(begintime);
	endtime = formatDatatime_local(endtime);
	if (new Date(begintime)>new Date(endtime)) {
		alert("开始时间不能大于结束时间");
		return;
	}
	
	var param = {
			"username":username,
			"nickname":nickname,
			"type":type,
			"status":status,
			"begintime":begintime,
			"endtime":endtime
			};
	_init(getURL("admin_selectlist"),8,param,cbOk,baseCBErr,cbClear);
}

//添加记录
function addShow() {
	$('#addAccount').modal('show');
}
function myAdd() {
	var username = $("#add_username").val();
	if (username=='') {
		alert("用户名不能为空");
		return;
	}
	var nickname = $("#add_nickname").val();
	if (nickname=='') {
		alert("昵称不能为空");
		return;
	}
	var password = $("#add_password").val();
	if (password=='') {
		alert("密码不能为空");
		return;
	}
	var password_again = $("#add_password_again").val();
	if (password_again=='') {
		alert("确认密码不能为空");
		return;
	}
	var type = $("#add_type option:selected").val();
	var status = $("#add_status option:selected").val();
	
	if (password!=password_again) {
		alert("两次密码不一致");
		return;
	}
	
	var param = {
			"username":username,
			"nickname":nickname,
			"password":password,
			"type":type,
			"status":status
			};
	_send(getURL("admin_insertone"),param,cbAdd,baseCBErr);
}
function cbAdd(data) {
	$('#addAccount').modal('hide');
	alert("添加成功");
	flushPage();
}


function showResetPassword(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	$("#reset_id").val(record.id);
	$("#reset_username").val(record.username);
	$("#reset_nickname").val(record.nickname);
	$("#reset_new_password").val('');
	$("#reset_new_password_again").val('');
	
	$('#reset_id').attr('disabled',true);
	$('#reset_username').attr('disabled',true);
	$('#reset_nickname').attr('disabled',true);
	
	$('#resetPassword').modal('show');
}
function resetPassword() {
	var id = $("#reset_id").val();
	id = parseInt(id);
	
	var newPassword = $("#reset_new_password").val();
	if (newPassword=='') {
		alert("密码不能为空");
		return;
	}
	
	var newPasswordAgain = $("#reset_new_password_again").val();
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
			"newpassword":newPassword
			};
	_send(getURL("admin_resetpassword"),parameter,cbReset,baseCBErr);
}
function cbReset(data) {
	$('#resetPassword').modal('hide');
	alert("重置成功");
}

//修改昵称
function showEditNickname(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	$("#edit_id").val(record.id);
	$("#edit_username").val(record.username);
	$("#edit_nickname").val(record.nickname);
	$("#edit_new_nickname").val('');
	
	$('#edit_id').attr('disabled',true);
	$('#edit_username').attr('disabled',true);
	$('#edit_nickname').attr('disabled',true);
	
	$('#editNickname').modal('show');
}
function editNickname() {
	var id = $("#edit_id").val();
	id = parseInt(id);
	
	var newnickname = $("#edit_new_nickname").val();
	if (newnickname=='') {
		alert("昵称不能为空");
		return;
	}
	
	var parameter = {
			"id":id,
			"newnickname":newnickname
			};
	_send(getURL("admin_editnickname"),parameter,cbEdit,baseCBErr);
}
function cbEdit(data) {
	$('#editNickname').modal('hide');
	alert("修改成功");
	flushPage();
}


//设置
function setStatus(id,status) {
	var text;
	if (status==1) {
		text = '禁用';
		status = 2;
	} else {
		text = '启用';
		status = 1;
	}
	if (!confirm("确定要"+text+"吗？"))
		return;
	var parameter = {
			"id":id,
			"status":status
	};
	_send(getURL("admin_setstatus"),parameter,cbSetStatus,baseCBErr);
}
function cbSetStatus(data) {
	alert("成功");
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
		var indexType = data[i].type;
		var strType = mapType[indexType];
		var indexStatus = data[i].status;
		var strStatus = mapStatus[indexStatus];
		
		var vSetStatus = 1;
		var strButtonStatus = '禁用';
		if (indexStatus==2) {
			vSetStatus = 2;
			strButtonStatus = '启用';
		}
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].nickname+"</td>"+
							"<td>"+strType+"</td>"+
							"<td>"+strStatus+"</td>"+
							"<td>"+formatTime(data[i].createtime)+"</td>"+
							"<td>"+
								"<span><a onclick='setStatus("+data[i].id+","+vSetStatus+");' href='#'>"+strButtonStatus+"</a></span>"+
								"<span>&nbsp;</span>"+
								"<span><a onclick='showResetPassword("+data[i].id+");' href='#'>重置密码</a></span>"+
								"<span>&nbsp;</span>"+
								"<span><a onclick='showEditNickname("+data[i].id+");' href='#'>修改昵称</a></span>"+
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

