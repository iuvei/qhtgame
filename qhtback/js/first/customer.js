
$(function(){
	mySelect();
});


//查询
function mySelect() {
	var cus_id = $("#select_cusid").val();
	if (cus_id==null) {
		alert("出错");
		return;
	}
	var cus_num = $("#select_cusnum").val();
	
	if (cus_id!='') {
		cus_id = parseInt(cus_id);
		if (cus_id<=0) {
			alert("请重新输入编号 ");
			return;
		}
	} else {
		cus_id = 0;
	}
	
	var param = {
			"id":cus_id,
			"cus_num":cus_num
			};
	_init(getURL("customer_selectlist"),8,param,cbOk,baseCBErr,cbClear);
}

//更改客服密码
function showEditCus(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#edit_cusid").val(record.id);
	$("#edit_cusnum").val(record.cus_num);
	$("#edit_cuspwd").val(record.cus_pwd);
	$("#edit_cusname").val(record.cus_name);
	
	$('#edit_cusid').attr('disabled',true);
	$('#edit_cusnum').attr('disabled',true);
	$('#edit_cusname').attr('disabled',true);
	
	$('#editCus').modal('show');
}
function editCus() {
	var cus_id = $("#edit_cusid").val();
	if (cus_id=='')
		cus_id = 0;
	else
		cus_id = parseInt(cus_id);
	
	var cus_num = $("#edit_cusnum").val();
	var cus_pwd = $("#edit_cuspwd").val();
	var cus_name = $("#edit_cusname").val();
	if (cus_pwd==''){
		alert("客服密码不能为空！");
		return;
	}
	
	var parameter = {
			"id":cus_id,
			"cus_num":cus_num,
			"cus_name":cus_name,
			"cus_pwd":cus_pwd
			};
	_send(getURL("customer_editcus"),parameter,cbEditCus,baseCBErr);
}
function cbEditCus() {
	$('#editCus').modal('hide');
	alert("修改成功");
	flushPage();
}

//查看游戏规则
//function showRule(id) {
//	var record = _get(id);
//	if (record==null) {
//		alert("出错");
//		return;
//	}
//	
//	$("#showto_lotteryid").val(record.id);
//	$("#showto_lotteryname").val(record.name);
//	$("#showto_lotteryrule").val(record.rule);
//	
//	$('#showtoRule').modal('show');
//}
//
////编辑游戏规则
//function editShow(id) {
//	var record = _get(id);
//	if (record==null) {
//		alert("出错");
//		return;
//	}
//	
//	$("#edit_lotteryid").val(record.id);
//	$("#edit_lotteryname").val(record.name);
//	$("#edit_lotteryrule").val(record.rule);
//	
//	$('#edit_lotteryid').attr('disabled',true);
//	$('#edit_lotteryname').attr('disabled',true);
//	
//	$('#editInfo').modal('show');
//	
//}
//function editInfo() {
//	
//	var lottery_id = $("#edit_lotteryid").val();
//	if (lottery_id=='')
//		lottery_id = 0;
//	else
//		lottery_id = parseInt(lottery_id);
//	
//	//var lotteryrule = document.getElementById('edit_lotteryrule');
//	var lotteryrule = document.all.edit_lotteryrule.value;
//	if (lotteryrule==''){
//		alert("游戏规则内容不能为空！");
//		return;
//	}
//	var parameter = {
//			"id":lottery_id,
//			"rule":lotteryrule
//			};
//	_send(getURL("system_lotteryrule"),parameter,cbSetRule,baseCBErr);
//}
//function cbSetRule() {
//	$('#editInfo').modal('hide');
//	alert("编辑成功");
//	flushPage();
//}
//
////新增彩种
//
////添加记录
//function addShow() {
//	$('#addAccount').modal('show');
//}
//function myAdd() {
//	var id = $("#add_id").val();
//	if (id=='') {
//		alert("彩种编号不能为空");
//		return;
//	}
//	var name = $("#add_name").val();
//	if (name=='') {
//		alert("彩种名称不能为空");
//		return;
//	}
//	var shutname = $("#add_shutname").val();
//	if (shutname=='') {
//		alert("彩种简称不能为空");
//		return;
//	}
//	var spacetime = $("#add_spacetime").val();
//	if (spacetime=='') {
//		alert("间隔时间设置不能为空");
//		return;
//	}
//
//	var notbetime = $("#add_notbettime").val();
//	if (notbetime=='') {
//		alert("间隔时间设置不能为空");
//		return;
//	}
//	
//	var rule = document.all.add_rule.value;
//	if (rule=='') {
//		alert("游戏规则不能为空");
//		return;
//	}
//	
//	var parameter = {
//		    "id":id,
//			"name":name,
//			"shutname":shutname,
//			"spacetime":spacetime,
//			"notbetime":notbetime,
//			"rule":rule
//			};
//	_send(getURL("system_lotteryinsert"),parameter,cbAdd,baseCBErr);
//}
//function cbAdd(data) {
//	$('#addAccount').modal('hide');
//	alert("添加成功");
//	flushPage();
//}

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
							"<td>"+data[i].cus_num+"</td>"+
							"<td>"+data[i].cus_pwd+"</td>"+
							"<td>"+data[i].cus_name+"</td>"+
							"<td>"+formatTime(data[i].createtime)+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
							"<td>"+
							    "<span><a onclick='showEditCus("+data[i].id+");' href='#'>更改密码</a></span>"+
								"<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
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

