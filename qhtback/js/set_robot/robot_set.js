
var mapStatus = {
		0:"全部",
		1:"有效",
		2:"无效"
};

var get_data;
function _myget(id) {
	for (var i=0; i < get_data.length; i++) {
		if (get_data[i].id==id)
			return get_data[i];
	}
}
$(function(){
	for(var index in mapStatus){
		$("#select_status").append("<option value='"+index+"'>"+mapStatus[index]+"</option>");
	}
	
	$("#edit_status").append("<option value='0'>请选择</option>");
	$("#edit_status").append("<option value='1'>有效</option>");
	$("#edit_status").append("<option value='2'>无效</option>");
	
	

//	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-ddThh:mm"));	//默认提前一周
//	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
	mySelect();
	
});

var _sdata;
	//查询 方案名
function selectschemenames() {
	
	var s_username = $('#show_username').val();
	var param = {
		"name":s_username
	};
	_send(getURL("robot_schemenames"),param,cbnameOk,baseCBErr);
}
function cbnameOk(data) {
	   $("#add_scheme").append("<option value=''>请选择</option>");
	   
	for (var i=0; i < data.length; i++) {
		var d = data[i];
		$("#add_scheme").append("<option value=" + d +">"+d+"</option>");
	}
	_sdata = data;
}
	

//查询
function mySelect() {
	var status = $("#select_status option:selected").val();
	if (status=='')
		status = 0;
	else
		status = parseInt(status);
		
	var nickname = $("#select_nickname").val();
	
//	var begintime = $("#startcreatetime").val();
//	var endtime = $("#endcreatetime").val();
//	if (	begintime==null || begintime=='' ||
//			endtime==null || endtime=='' ) {
//		alert("请选择时间");
//		return;
//	}
//	begintime = formatDatatime_local(begintime);
//	endtime = formatDatatime_local(endtime);
//	if (new Date(begintime)>new Date(endtime)) {
//		alert("开始时间不能大于结束时间");
//		return;
//	}
	var param = {
			"status":status,
			"nickname":nickname
			};
	_init(getURL("robot_selectinfo"),8,param,cbOk,baseCBErr,cbClear);
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
		var status = mapStatus[data[i].status];
		
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].appcode+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].nickname+"</td>"+
							"<td>"+data[i].balance+"</td>"+
							"<td>"+"<span><a onclick='showSchemes("+data[i].id+");' href='#'>查看详细</a></span>"+"</td>"+
							"<td>"+status+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
							"<td>"+
							    "<span><a onclick='editShow("+data[i].id+");' href='#'>编辑信息</a></span>"+
							"</td>"+
						"</tr>");
	}
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}

//查看方案内容
var _getid;
function showSchemes(id) {
	_getid=id;
	cbOddClear();
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	$('#show_nickname').val(record.nickname);
	$('#show_nickname').attr('disabled',true);
	$('#show_username').val(record.username);
	$('#show_username').attr('disabled',true);

	var schemes = record.schemes;
	for (var i=0; i < schemes.length; i++) {
		var tmpid = schemes[i].id+999999999;
		$("#mytext").append(
						"<tr id="+tmpid+">"+
						    "<td>"+schemes[i].id+"</td>"+
							"<td>"+schemes[i].scheme+"</td>"+
							"<td>"+schemes[i].time+"</td>"+
							"<td>"+schemes[i].probabi+"</td>"+
							"<td>"+
							    "<span><a onclick='showInfoDel("+schemes[i].id+");' href='#'>删除</a></span>"+
							"</td>"+
						"</tr>");
	}
	$('#showtoRule').modal('show');
	get_data=schemes;
}

function cbOddClear() {
	if (get_data==null)
		return;
	
	if (get_data!=null) {
		for (var i=0; i < get_data.length; i++) {
			var tmpid = get_data[i].id+999999999;
			$("tr[id='"+tmpid+"']").remove();
		}
	}
	get_data=null;
}

//删除 投注 内容
function showInfoDel(id) {
    if (!confirm("确定要删除【id="+id+"】吗？"))
        return;
    var record = _myget(id);
    if (record==null) {
        alert("出错");
        return;
    }
    var username = $('#show_username').val();

    var parameter = {
        "username":username,
        "id":record.id
    };
    _send(getURL("robot_deltext"),parameter,cbDel,baseCBErr);
}
function cbDel() {
	$('#showtoRule').modal('hide');
    alert("删除成功");
//  showSchemes(_getid);
    mySelect();
}


//添加 机器人
function addRobot() {
	$("#add_nickname").val("");
	
	$('#addrobot').modal('show');
	$('#add_appcode').attr('disabled',true);
}
function myAddrobot() {
//	var id = $("#add_id").val();
//	if (id=='') {
//		alert("彩种编号不能为空");
//		return;
//	}
	var appcode = $("#add_appcode").val();
	var nickname = $("#add_nickname").val();
	if (nickname=='') {
		alert("昵称不能为空");
		return;
	}
	
	var parameter = {
			"appcode":appcode,
			"nickname":nickname
			};
	_send(getURL("robot_addinfo"),parameter,cbAdd002,baseCBErr);
}
function cbAdd002(data) {
	$('#addrobot').modal('hide');
	alert("添加成功");
	
	flushPage();
}

//清空 下拉框 数据
//function cbClear_s() {
//	if (_sdata!=null) {
//		$("#add_scheme").empty();
//	}
//	_sdata = null;
//}

//添加 投注记录
function addtext() {
	//清空 下拉框 数据
	$("#add_scheme").empty();
	
	$("#add_id").val("");
	$("#add_time").val("");
	$("#add_probabi").val("");
	
	$('#showtoRule').modal('hide');
	$('#_addtext').modal('show');
	selectschemenames();
	var record = _get(_getid);
	if (record==null || record==''){
		alert("出错");
		return;
	}
	$('#add_username').val(record.username);
	$('#add_username').attr('disabled',true);

}
function myAddtext() {
	
	var username = $("#add_username").val();
	if (username=='') {
		alert("用户名不能为空");
		return;
	}
	var id = $("#add_id").val();
	if (id=='') {
		alert("方案编号不能为空");
		return;
	}
	var time = $("#add_time").val();
	if (time=='') {
		alert("时间不能为空");
		return;
	}
	var scheme = $("#add_scheme").val();
	if (scheme=='') {
		alert("请选择方案名");
		return;
	}
	var probabi = $("#add_probabi").val();
	if (probabi=='') {
		alert("概率不能为空");
		return;
	}
	
	var parameter = {
			"username":username,
			"id":id,
			"time":time,
			"scheme":scheme,
			"probabi":probabi
			};
	_send(getURL("robot_addtext"),parameter,cbAdd,baseCBErr);
}
function cbAdd(data) {
	$('#_addtext').modal('hide');
	alert("添加成功");
//	showSchemes(_getid);
	mySelect();
}


//编辑机器人
function editShow(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#edit_id").val(record.id);
	$("#edit_username").val(record.username);
	$("#edit_nickname").val(record.nickname);
//	$("#edit_status").val(record.status);
	
	$('#edit_id').attr('disabled',true);
	$('#edit_username').attr('disabled',true);
	
	$('#editInfo').modal('show');
	
}
function editInfo() {
	
	var status = $("#edit_status option:selected").val();
	if (status==''||status == 0){
		alert("请选择状态!");
		return;
	}
	else
		status = parseInt(status);
	var username = $("#edit_username").val();
	
	var nickname = $("#edit_nickname").val();
	
	if (nickname=='') {
		alert("昵称不能为空");
		return;
	}
	var parameter = {
			"username":username,
			"nickname":nickname,
			"status":status
			};
	_send(getURL("robot_setinfo"),parameter,cbSetRule,baseCBErr);
}
function cbSetRule() {
	$('#editInfo').modal('hide');
	alert("编辑成功");
	flushPage();
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

