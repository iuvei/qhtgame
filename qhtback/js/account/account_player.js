var mapStatus = {
		0:"全部",
		1:"有效",
		2:"禁用"
};
var mapType = {
		1:"真实",
		2:"虚拟"
};

$(function(){
	for(var index in mapStatus){
		$("#status").append("<option value='"+index+"'>"+mapStatus[index]+"</option>");
	}
	
	for(var index in mapStatus){
		if (index!=0)
			$("#add_status").append("<option value='"+index+"'>"+mapStatus[index]+"</option>");
	}
	
	$("#finance_type").append("<option value='1'>充值</option>");
	$("#finance_type").append("<option value='2'>提现</option>");
	
	$("#typeid").append("<option value='0'>请选择</option>");
	$("#typeid").append("<option value='1'>真实</option>");
	$("#typeid").append("<option value='2'>虚拟</option>");
	
	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-ddThh:mm"));	//默认提前一周
	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
	mySelect();
});


//查询
function mySelect() {
	var username = $("#username").val();
	var nickname = $("#nickname").val();
	var status = $("#status option:selected").val();
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
			"status":status,
			"begintime":begintime,
			"endtime":endtime
			};
	_init(getURL("player_selectlist"),8,param,cbOk,baseCBErr,cbClear);
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
	var status = $("#add_status option:selected").val();
	
	var qq = $("#add_qq").val();
	if (qq=='') {
		alert("QQ号不能为空");
		return;
	}
	
	var weixin = $("#add_weixin").val();
	if (weixin=='') {
		alert("微信号不能为空");
		return;
	}

	var telephone = $("#add_telephone").val();
	if (telephone=='') {
		alert("手机号不能为空");
		return;
	}
	
	if (password!=password_again) {
		alert("两次密码不一致");
		return;
	}
	
	var parameter = {
			"loginname":username,
			"nickname":nickname,
			"password":password,
			"status":status,
			"qq":qq,
			"weixin":weixin,
			"telephone":telephone
			};
	_send(getURL("player_insertone"),parameter,cbAdd,baseCBErr);
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
	_send(getURL("player_resetpassword"),parameter,cbReset,baseCBErr);
}
function cbReset(data) {
	$('#resetPassword').modal('hide');
	alert("重置成功");
}

//设置
function setStatus(id,status) {
	if (!confirm("确定要设置成'"+mapStatus[status]+"'吗？"))
		return;
	var parameter = {
			"id":id,
			"status":status
	};
	_send(getURL("player_setstatus"),parameter,cbSetStatus,baseCBErr);
}
function cbSetStatus(data) {
	alert("成功");
	flushPage();
}


//编辑
function showEdit(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	$("#edit_id").val(record.id);
	$("#edit_username").val(record.username);
	$("#edit_nickname").val(record.nickname);
	$("#edit_qq").val(record.qq);
	$("#edit_weixin").val(record.weixin);
	$("#edit_telephone").val(record.telephone);
	
	
	$('#edit_username').attr('disabled',true);
	
	$('#editInfo').modal('show');
}
function editInfo() {
	var id = $("#edit_id").val();
	id = parseInt(id);
	
	var nickname = $("#edit_nickname").val();
	if (nickname=='') {
		alert("昵称不能为空");
		return;
	}
	
	var qq = $("#edit_qq").val();
	if (qq=='') {
		alert("QQ号不能为空");
		return;
	}
	
	var weixin = $("#edit_weixin").val();
	if (weixin=='') {
		alert("微信号不能为空");
		return;
	}
	
	var telephone = $("#edit_telephone").val();
	if (telephone=='') {
		alert("手机号不能为空");
		return;
	}
	
	var parameter = {
			"id":id,
			"nickname":nickname,
			"qq":qq,
			"weixin":weixin,
			"telephone":telephone
			};
	_send(getURL("player_editinfo"),parameter,cbEdit,baseCBErr);
}
function cbEdit(data) {
	$('#editInfo').modal('hide');
	alert("修改成功");
	flushPage();
}

//身份切换 typeid
function showTypeID(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#type_username").val(record.username);
	$("#type_id").val(record.id);
	$("#type_nickname").val(record.nickname);
	
	$('#type_username').attr('disabled',true);
	$('#type_id').attr('disabled',true);
	$('#type_nickname').attr('disabled',true);
	
	$('#type_hide').modal('show');
}

function type_hide() {
	var id = $("#type_id").val();
	var typeid = $("#typeid option:selected").val();
	if (typeid==''||typeid == 0){
		alert("请选择身份!");
		return;
	}
	else
		typeid = parseInt(typeid);
	var username = $("#type_username").val();
	
	var parameter = {
			"username":username,
			"typeid":typeid
			};
	_send(getURL("player_typeid"),parameter,cbOprType,baseCBErr);
}
function cbOprType(data) {
	$('#type_hide').modal('hide');
	alert("切换成功");
	flushPage();
}


//充值提现
function showFinance(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#finance_id").val(record.id);
	$("#finance_username ").val(record.username);
	$("#finance_balance").val(record.balance);
	
	$('#finance_id').attr('disabled',true);
	$('#finance_username').attr('disabled',true);
	$('#finance_balance').attr('disabled',true);
	
	$('#financeModel').modal('show');
}

function oprFinance() {
	var id = $("#finance_id").val();
	var type = $("#finance_type option:selected").val();
	if (type=='')
		type = 0;
	else
		type = parseInt(type);
	var username = $("#finance_username").val();
	var amount = $("#finance_amount").val();
	
	var parameter = {
			"username":username,
			"type":type,
			"amount":amount
			};
	_send(getURL("finance_brequest"),parameter,cbOprFinance,baseCBErr);
}
function cbOprFinance(data) {
	$('#financeModel').modal('hide');
	alert("修改成功");
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
		var indexStatus = data[i].status;
		var strStatus = mapStatus[indexStatus];
		var typeid = mapType[data[i].typeid];
		
		var vSetStatus;
		var strButtonStatus;
		if (indexStatus==1) {
			vSetStatus = 2;
			strButtonStatus = mapStatus[vSetStatus];
		} else {
			vSetStatus = 1;
			strButtonStatus = mapStatus[vSetStatus];
		}
		
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].loginname+"</td>"+
							"<td>"+data[i].nickname+"</td>"+
							"<td>"+typeid+"</td>"+
							"<td>"+strStatus+"</td>"+
							"<td>"+data[i].balance+"</td>"+
//							"<td>"+data[i].frozen_bal+"</td>"+
//							"<td>"+data[i].integral+"</td>"+
							"<td>"+data[i].qq+"</td>"+
							"<td>"+data[i].weixin+"</td>"+
							"<td>"+data[i].telephone+"</td>"+
							"<td>"+formatTime(data[i].createtime)+"</td>"+
							"<td>"+
								"<p><a onclick='setStatus("+data[i].id+","+vSetStatus+");' href='#'>"+strButtonStatus+"</a></p>"+
								"<p><a onclick='showEdit("+data[i].id+");' href='#'>编辑</a></p>"+
								"<p><a onclick='showResetPassword("+data[i].id+");' href='#'>重置密码</a></p>"+
								"<p><a onclick='showFinance("+data[i].id+");' href='#'>充值提现</a></p>"	+
								"<p><a onclick='showTypeID("+data[i].id+");' href='#'>身份切换</a></p>"+
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