var _gdata;
var get_data;
function _myget(id) {
		for (var i=0; i < _gdata.length; i++) {
		if (_gdata[i].id==id)
			return _gdata[i];
	}
}
function _mysend(id) {
		for (var i=0; i < get_data.length; i++) {
		if ((i+1)==id)
			return get_data[i];
	}
}

$(function(){
	mySelect();
});


//查询
function mySelect() {
	
	var param = {		};
	_send(getURL("set_scheme"),param,cbOk,baseCBErr);
}

//设置粮草信息
function setscheme(id) {
	var record = _myget(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#set_id").val(record.id);
	$("#set_name").val(record.name);
	$("#set_low_amount").val(record.low_amount);
	$("#set_sendup_amount").val(record.sendup_amount);
	$("#set_stop_amount").val(record.stop_amount);
	$("#set_up_amount").val(record.up_amount);
	$("#set_senddown_amount").val(record.senddown_amount);
	
	$('#set_id').attr('disabled',true);
	$('#set_name').attr('disabled',true);
	
	$('#setTO').modal('show');
}
function setTOscheme() {
	var id = $("#set_id").val();
	if (id=='')
		id = 0;
	else
		id = parseInt(id);
	
	var name = $("#set_name").val();
	if (name==''){
		alert("名称不能为空！");
		return;
	}
	var low_amount = $("#set_low_amount").val();
	if (low_amount==''){
		alert("上粮下限不能为空！");
		return;
	}
	var sendup_amount = $("#set_sendup_amount").val();
	if (sendup_amount==''){
		alert("上粮信息不能为空！");
		return;
	}
	var stop_amount = $("#set_stop_amount").val();
	if (stop_amount==''){
		alert("停攻粮食不能为空！");
		return;
	}
	var up_amount = $("#set_up_amount").val();
	if (up_amount==''){
		alert("下粮上限不能为空！");
		return;
	}
	var senddown_amount = $("#set_senddown_amount").val();
	if (senddown_amount==''){
		alert("下粮信息不能为空！");
		return;
	}
	
	var parameter = {
			"id":id,
			"name":name,
			"low_amount":low_amount,
			"sendup_amount":sendup_amount,
			"stop_amount":stop_amount,
			"up_amount":up_amount,
			"senddown_amount":senddown_amount
			};
	_send(getURL("set_setscheme"),parameter,cbsetscheme,baseCBErr);
}
function cbsetscheme() {
	$('#setTO').modal('hide');
	alert("设置成功");
	mySelect();
}

//查看方案内容
var _gid;
var _goid;
function showRule(id) {
	
	_gid = id;
	cbOddClear();
	var record = _myget(id);
	if (record==null) {
		alert("出错");
		return;
	}
	$('#show_name').val(record.name);
	$('#show_name').attr('disabled',true);
	var send_text = record.send_text;
	
	for (var i=0; i < send_text.length; i++) {
		_goid = i+1;
		var tmpid = i+999999999;
		$("#mytext").append(
						"<tr id="+tmpid+">"+
							"<td>"+(i+1)+"</td>"+
							"<td>"+send_text[i]+"</td>"+
							"<td>"+
							    "<span><a onclick='showDel("+(i+1)+");' href='#'>删除</a></span>"+
							"</td>"+
						"</tr>");
	}
//	$("#showto_lotteryid").val(record.id);
//	$("#showto_lotteryname").val(record.name);
//	$("#showto_lotteryrule").val(record.rule);
	
	$('#showtoRule').modal('show');
	get_data=send_text;
}
function cbOddClear() {
	if (get_data==null)
		return;
	
	if (get_data!=null) {
		for (var i=0; i < get_data.length; i++) {
			var tmpid = i+999999999;
			$("tr[id='"+tmpid+"']").remove();
		}
	}
	get_data=null;
}
//编辑游戏规则
function editShow(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#edit_lotteryid").val(record.id);
	$("#edit_lotteryname").val(record.name);
	$("#edit_lotteryrule").val(record.rule);
	
	$('#edit_lotteryid').attr('disabled',true);
	$('#edit_lotteryname').attr('disabled',true);
	
	$('#editInfo').modal('show');
	
}
function editInfo() {
	
	var lottery_id = $("#edit_lotteryid").val();
	if (lottery_id=='')
		lottery_id = 0;
	else
		lottery_id = parseInt(lottery_id);
	
	//var lotteryrule = document.getElementById('edit_lotteryrule');
	var lotteryrule = document.all.edit_lotteryrule.value;
	if (lotteryrule==''){
		alert("游戏规则内容不能为空！");
		return;
	}
	var parameter = {
			"id":lottery_id,
			"rule":lotteryrule
			};
	_send(getURL("system_lotteryrule"),parameter,cbSetRule,baseCBErr);
}
function cbSetRule() {
	$('#editInfo').modal('hide');
	alert("编辑成功");
	flushPage();
}

//增加 粮食方案
function addShow() {
	$('#add_name').val("");
	$('#add_low_amount').val("");
	$('#add_sendup_amount').val("");
	$('#add_stop_amount').val("");
	$('#add_up_amount').val("");
	$('#add_senddown_amount').val("");
	
	$('#addAccount').modal('show');
}
function myAdd() {
//	var id = $("#add_id").val();
//	if (id=='') {
//		alert("彩种编号不能为空");
//		return;add_name add_low_amount add_sendup_amount add_stop_amount add_up_amount add_senddown_amount
//	}
	var name = $("#add_name").val();
	if (name=='') {
		alert("方案名称不能为空");
		return;
	}
	var low_amount = $("#add_low_amount").val();
	if (low_amount=='') {
		alert("上粮下限不能为空");
		return;
	}
	var sendup_amount = $("#add_sendup_amount").val();
	if (sendup_amount=='') {
		alert("上粮信息不能为空");
		return;
	}

	var stop_amount = $("#add_stop_amount").val();
	if (stop_amount=='') {
		alert("停攻粮草不能为空");
		return;
	}
	var up_amount = $("#add_up_amount").val();
	if (up_amount=='') {
		alert("下粮上限不能为空");
		return;
	}
	var senddown_amount = $("#add_senddown_amount").val();
	if (senddown_amount=='') {
		alert("下粮信息不能为空");
		return;
	}
	var parameter = {
			"name":name,
			"low_amount":low_amount,
			"sendup_amount":sendup_amount,
			"up_amount":up_amount,
			"senddown_amount":senddown_amount,
			"stop_amount":stop_amount
			};
	_send(getURL("set_addrobot"),parameter,cbAdd,baseCBErr);
}
function cbAdd(data) {
	$('#addAccount').modal('hide');
	alert("添加成功");
	mySelect();
}


//添加记录  投注
function addtext() {
	$('#add_sendtext').val("");
	
	$('#showtoRule').modal('hide');
	$('#addtext').modal('show');
	
	var record = _myget(_gid);
	if (record==null || record==''){
		alert("出错");
		return;
	}
	$('#add_sendname').val(record.name);
	$('#add_sendname').attr('disabled',true);
}
function myAddtext() {
//	var id = $("#add_id").val();
//	if (id=='') {
//		alert("彩种编号不能为空");
//		return;
//	}
	var name = $("#add_sendname").val();
	if (name=='') {
		alert("投注名称不能为空");
		return;
	}
	var text = $("#add_sendtext").val();
	if (text=='') {
		alert("投注内容不能为空");
		return;
	}
	
	var parameter = {
			"name":name,
			"text":text
			};
	_send(getURL("set_addscheme"),parameter,cbAdd02,baseCBErr);
}
function cbAdd02(data) {
	$('#addtext').modal('hide');
	alert("添加成功");
	mySelect();
	//showRule(_gid);
	
}

//清空查询数据
function cbClear() {
	if (_gdata!=null) {
		for (var i=0; i < _gdata.length; i++) {
			$("tr[id='"+_gdata[i].id+"']").remove();
		}
	}
	_gdata = null;
}

//查询成功回调函数

function cbOk(data) {
	cbClear();
	for (var i=0; i < data.length; i++) {
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].name+"</td>"+
							"<td>"+data[i].low_amount+"</td>"+
							"<td>"+data[i].sendup_amount+"</td>"+
							"<td>"+data[i].stop_amount+"</td>"+
							"<td>"+data[i].up_amount+"</td>"+
							"<td>"+data[i].senddown_amount+"</td>"+
							"<td>"+"<span><a onclick='showRule("+data[i].id+");' href='#'>查看详细</a></span>"+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
							"<td>"+
							    "<span><a onclick='setscheme("+data[i].id+");' href='#'>设置粮食</a></span>"+
							"</td>"+
						"</tr>");
	}
	_gdata = data;
}

//删除 投注 内容
function showDel(_goid) {
    if (!confirm("确定要删除【id="+_goid+"】吗？"))
        return;
    var record = _mysend(_goid);
    if (record==null) {
        alert("出错");
        return;
    }
     var name = $('#show_name').val();

    var parameter = {
        "name":name,
        "text":record
    };
    _send(getURL("set_delscheme"),parameter,cbDel,baseCBErr);
}
function cbDel() {
	$('#showtoRule').modal('hide');
    alert("删除成功");
//  showRule(_gid);
    mySelect();
}
