
$(function(){
	mySelect();
});


//查询
function mySelect() {
	var lottery_id = $("#select_lotteryid").val();
	if (lottery_id==null) {
		alert("出错");
		return;
	}
	if (lottery_id!='') {
		lottery_id = parseInt(lottery_id);
		if (lottery_id<=0) {
			alert("请重新输入编号 ");
			return;
		}
	} else {
		lottery_id = 0;
	}
	
	var param = {
			"lottery_id":lottery_id
			};
	_init(getURL("system_lotteryselectlist"),8,param,cbOk,baseCBErr,cbClear);
}

//设置封盘时间
function showSetTime(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#set_lotteryid").val(record.id);
	$("#set_lotteryname").val(record.name);
	$("#set_spacetime").val((record.spacetime)/1000);
	$("#set_opentime").val((record.opentime)/1000);
	$("#set_notbettime").val((record.notbettime)/1000);
	
	$('#set_lotteryid').attr('disabled',true);
	$('#set_lotteryname').attr('disabled',true);
	
	$('#setTime').modal('show');
}
function setTime() {
	var lottery_id = $("#set_lotteryid").val();
	if (lottery_id=='')
		lottery_id = 0;
	else
		lottery_id = parseInt(lottery_id);

	var open_time = ($("#set_opentime").val())*1000;
	if (open_time=='')
		open_time = 0;
	else
		open_time = parseInt(open_time);
	
	var lottery_time = ($("#set_spacetime").val())*1000;
	if (lottery_time=='')
		lottery_time = 0;
	else
		lottery_time = parseInt(lottery_time);

	var notbettime = ($("#set_notbettime").val())*1000;
	if (notbettime=='')
		notbettime = 0;
	else
		notbettime = parseInt(notbettime);
	
	var parameter = {
			"id":lottery_id,
			"opentime":open_time,
			"spacetime":lottery_time,
			"notbettime":notbettime
			};
	_send(getURL("system_lotterysettime"),parameter,cbSetTime,baseCBErr);
}
function cbSetTime() {
	$('#setTime').modal('hide');
	alert("修改成功");
	flushPage();
}

//查看游戏规则
function showRule(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#showto_lotteryid").val(record.id);
	$("#showto_lotteryname").val(record.name);
	$("#showto_lotteryrule").val(record.rule);
	
	$('#showtoRule').modal('show');
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

//新增彩种

//添加记录
function addShow() {
	$('#addAccount').modal('show');
}
function myAdd() {
	var id = $("#add_id").val();
	if (id=='') {
		alert("彩种编号不能为空");
		return;
	}
	var name = $("#add_name").val();
	if (name=='') {
		alert("彩种名称不能为空");
		return;
	}
	var shutname = $("#add_shutname").val();
	if (shutname=='') {
		alert("彩种简称不能为空");
		return;
	}
	var opentime = $("#add_opentime").val();
	if (opentime=='') {
		alert("开盘时间设置不能为空");
		return;
	}
	opentime = opentime*1000;
	var spacetime = $("#add_spacetime").val();
	if (spacetime=='') {
		alert("封盘时间设置不能为空");
		return;
	}
	spacetime = spacetime*1000;
	var notbettime = $("#add_notbettime").val();
	if (notbettime=='') {
		alert("禁下时间设置不能为空");
		return;
	}
	notbettime = notbettime*1000;
	
	var rule = document.all.add_rule.value;
	if (rule=='') {
		alert("游戏规则不能为空");
		return;
	}
	
	var parameter = {
		    "id":id,
			"name":name,
			"shutname":shutname,
			"opentime":opentime,
			"spacetime":spacetime,
			"notbettime":notbettime,
			"rule":rule
			};
	_send(getURL("system_lotteryinsert"),parameter,cbAdd,baseCBErr);
}
function cbAdd(data) {
	$('#addAccount').modal('hide');
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
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].name+"</td>"+
							"<td>"+data[i].shutname+"</td>"+
							"<td>"+(data[i].opentime)/1000+"</td>"+
							"<td>"+(data[i].spacetime)/1000+"</td>"+
							"<td>"+(data[i].notbettime)/1000+"</td>"+
							"<td>"+"<span><a onclick='showRule("+data[i].id+");' href='#'>查看详细</a></span>"+"</td>"+
							"<td>"+formatTime(data[i].createtime)+"</td>"+
							"<td>"+
							    "<span><a onclick='showSetTime("+data[i].id+");' href='#'>设置时间</a></span>"+
								"<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
								"<span><a onclick='editShow("+data[i].id+");' href='#'>编辑规则</a></span>"+
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

