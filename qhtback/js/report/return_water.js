var mapStatus = {
		0:"未返水",
		1:"当日盈亏",
		2:"当日流水",
		3:"当日上分",
		4:"当日下分"
};
var map_Status = {
		0:"未处理",
		1:"已处理",
		2:"已处理",
		3:"已处理",
		4:"已处理"
};
var mapType = {
		0:"请选择返水类型",
		1:"根据用户当日盈亏返水",
		2:"根据用户当日流水返水",
		3:"根据用户当日上分返水",
		4:"根据用户当日下分返水"
};

//  var _change_point = $("#change_point").val();
//  var _change_status = $("#status").val();
    
$(function(){
	
	for(var index in mapType){
		$("#status").append("<option value='"+index+"'>"+mapType[index]+"</option>");
	}
	
	$("#datetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-dd"));	//当天
	
	mySelectTime();
});



//返水报表（按日期）
function mySelectTime() {
	var selectdate = $("#datetime").val();
	
	if (	selectdate==null || selectdate==''  ) {
		alert("请选择时间");
		return;
	}
//	selectdate = formatDatatime_local(selectdate);

//  int b = Conver.ToInt32(selectdate.ToString("yyyyMMdd"));//mm小写是minute

             var date = selectdate;
             //替换“-”
             var dateStr = date.replace(/\-/g, "");

             var a = parseInt(dateStr);
    
	var paraterm = {
			"date":a
	};
	_init(getURL("return_water"),6,paraterm,cbOK,baseCBErr,cbClear);
}



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
function cbOK(size,total,pageNum,pages,data) {
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
		
		var strStatus = mapStatus[data[i].status];
		var str_Status = map_Status[data[i].status];
		
		
		var _status = data[i].status;
		var return_amount = data[i].return_amount;
		if(_status==0) {
			
				return_amount = "等待计算";
				
			$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].date+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].profit_amount+"</td>"+
							"<td>"+data[i].water_amount+"</td>"+
							"<td>"+data[i].up_amount+"</td>"+
							"<td>"+data[i].down_amount+"</td>"+
							"<td>"+return_amount+"</td>"+
							"<td>"+strStatus+"</td>"+
							"<td>"+str_Status+"</td>"+
//							"<td>"+
//								"<span><a onclick='editReturn("+data[i].id+");' href='#'>计算返水</a></span>"+"<br />"+
//							"</td>"+
						"</tr>");
		} else {
					$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].date+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].profit_amount+"</td>"+
							"<td>"+data[i].water_amount+"</td>"+
							"<td>"+data[i].up_amount+"</td>"+
							"<td>"+data[i].down_amount+"</td>"+
							"<td>"+data[i].return_amount+"</td>"+
							"<td>"+strStatus+"</td>"+
							"<td>"+str_Status+"</td>"+
//							"<td>"+
//								"<span><a onclick='editReturn("+data[i].id+");' href='#'>计算返水</a></span>"+"<br />"+
//							"</td>"+
						"</tr>");
		}
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}
//==============================================
//计算单条返水  (暂时不用)
function editReturn(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	$("#return_id").val(record.id);
	$("#return_date ").val(record.username);
	$("#return_status").val(record.balance);
	$("#return_point").val(record.balance);
	
	$('#return_id').attr('disabled',true);
	$('#return_date').attr('disabled',true);
	$('#return_status').attr('disabled',true);
	$('#return_point').attr('disabled',true);
	
	$('#return_one').modal('show');
}

function myReturnone() {
	var id = $("#return_id").val();
	var date = $("#return_date").val();
	var status = $("#return_status").val();
	var point = $("#return_point").val();
	
	var parameter = {
			"id":id,
			"date":date,
			"status":status,
			"point":point
			};
	_send(getURL("return_change"),parameter,cbOprFinance,baseCBErr);
}
function cbOprFinance(data) {
	$('#return_one').modal('hide');
	alert("返水成功");
	flushPage();
}


//===========================================
//计算同一类型返水(批量)
function myReturnNow() {

	var selectdate = $("#datetime").val();
	
	if (	selectdate==null || selectdate==''  ) {
		alert("请选择时间");
		return;
	}

             var date = selectdate;
             //替换“-”
             var dateStr = date.replace(/\-/g, "");
	var date = parseInt(dateStr);
	
	var status = $("#status").val();
	if (	status==0 || status==''  ) {
		alert("请选择返水类型");
		return;
	}
	var point = $("#change_point").val();
	if (	point==null || point==''  ) {
		alert("请填写比例");
		return;
	}
	
	var msg = "您真的确定要<立即返水>吗？\n\n请确认！";
	if(confirm(msg) == true) {
		$("#status").val(0);
	    $("#change_point").val("");
//	    $("#datetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-dd"));	//当天
	} else {
		$("#status").val(0);
	    $("#change_point").val("");
//	    $("#datetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-dd"));	//当天
        return;
	}
	
	
	var parameter = {
			"date":date,
			"status":status,
			"point":point
			};
	_send(getURL("return_change"),parameter,cbOprFinance,baseCBErr);
}
function cbOprFinance(data) {
	$('#return_one').modal('hide');
	$("#status").val(0);
	$("#change_point").val("");
	alert("返水成功");
	flushPage();
}

// 预计 结果**************************************
function myRequest() {
	var selectdate = $("#datetime").val();
	
	if (	selectdate==null || selectdate==''  ) {
		alert("请选择时间");
		return;
	}
//	selectdate = formatDatatime_local(selectdate);

//  int b = Conver.ToInt32(selectdate.ToString("yyyyMMdd"));//mm小写是minute

             var date = selectdate;
             //替换“-”
             var dateStr = date.replace(/\-/g, "");

             var a = parseInt(dateStr);
    
	var paraterm = {
			"date":a
	};
	_init(getURL("return_water"),6,paraterm,cbChange,baseCBErr,cbClearTO);
}



function cbClearTO(data) {
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

//function cbChange() {
//	var record = _getall();
//	if (record==null) {
//		alert("出错");
//		return;
//	}
function cbChange(size,total,pageNum,pages,data) {
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
	
	var _change_status = $("#status").val();
	if (_change_status==0 || _change_status==''  ) {
		alert("请选择返水类型");
		return;
	}
	var _change_point = $("#change_point").val();
	if (_change_point==null || _change_point==''  ) {
		alert("请填写比例");
		return;
	}
	
	for (var i=0; i < data.length; i++) {
		
		var _status = data[i].status;
		var strStatus = mapStatus[_status];
		
		var return_amount = data[i].return_amount;

		if(_status==0) {
			
			var _amount = 0;
			
			if (_change_status==1)
				_amount = data[i].profit_amount;
			else if (_change_status==2)
				_amount = data[i].water_amount;
			else if (_change_status==3)
				_amount = data[i].up_amount;
			else if (_change_status==4)
				_amount = data[i].down_amount;
			
			var return_amount = _amount*(_change_point/100);
			
			$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].date+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].profit_amount+"</td>"+
							"<td>"+data[i].water_amount+"</td>"+
							"<td>"+data[i].up_amount+"</td>"+
							"<td>"+data[i].down_amount+"</td>"+
							"<td>"+return_amount+"</td>"+
							"<td>"+strStatus+"</td>"+
//							"<td>"+
//								"<span><a onclick='editReturn("+data[i].id+");' href='#'>计算返水</a></span>"+"<br />"+
//							"</td>"+
						"</tr>");
		}else{
			
			$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].date+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].profit_amount+"</td>"+
							"<td>"+data[i].water_amount+"</td>"+
							"<td>"+data[i].up_amount+"</td>"+
							"<td>"+data[i].down_amount+"</td>"+
							"<td>"+return_amount+"</td>"+
							"<td>"+strStatus+"</td>"+
//							"<td>"+
//								"<span><a onclick='editReturn("+data[i].id+");' href='#'>计算返水</a></span>"+"<br />"+
//							"</td>"+
						"</tr>");
		}
		
		
	}
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");

}

//刷新
function flushPage() {
	_sendPage(cbOK,baseCBErr,cbClear);
}

//首页
function firstPage() {
	_firstPage(cbOK,baseCBErr,cbClear);
}

//尾页
function endPage() {
	_endPage(cbOK,baseCBErr,cbClear);
}

//上一页
function upPage() {
	_upPage(cbOK,baseCBErr,cbClear);
}

//下一页
function downPage() {
	_downPage(cbOK,baseCBErr,cbClear);
}

//跳转
function changePage() {
	var index = $("#change").val();
	_changePage(index,cbOK,baseCBErr,cbClear);
}

