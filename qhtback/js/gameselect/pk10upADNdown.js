var mapType = {
	    0:"全部",
		1:"已处理",
		2:"未处理",
		3:"已拒绝"
};

$(function(){
	for(var index in mapType){
		$("#select_type").append("<option value='"+index+"'>"+mapType[index]+"</option>");
		$("#_select_type").append("<option value='"+index+"'>"+mapType[index]+"</option>");
	}
	
	$("#startcreatetime").val(new Date(new Date().getTime()).Format("yyyy-MM-dd"));	//默认提前一周
	$("#endcreatetime").val(new Date(new Date().getTime()).Format("yyyy-MM-dd"));	//默认当前时间
	up_mySelect();
	$("#_startcreatetime").val(new Date(new Date().getTime()).Format("yyyy-MM-dd"));	//默认提前一周
	$("#_endcreatetime").val(new Date(new Date().getTime()).Format("yyyy-MM-dd"));	//默认当前时间
	down_mySelect();
});


//查询  上分
function up_mySelect() {
	var username = $("#select_username").val();
	var tag = $("#select_type").val();
	
	var begintime = $("#startcreatetime").val();
	var endtime = $("#endcreatetime").val();
	if (	begintime==null || begintime=='' ||
			endtime==null || endtime=='' ) {
		alert("请选择时间");
		return;
	}
			//将时间控件格式转换成YYYY-MM-DD 00:00:00
	begintime = formatDatatime_localone(begintime);
	
	        //将时间控件格式转换成YYYY-MM-DD 23:59:59
	endtime = formatDatatime_localtwo(endtime);
	if (new Date(begintime)>new Date(endtime)) {
		alert("开始时间不能大于结束时间");
		return;
	}
	
	var param = {
		    "username":username,
		    "tag":tag,
			"begintime":begintime,
			"endtime":endtime
			};
	_init(getURL("record_upfinanceselectlist"),6,param,cbOk,baseCBErr,cbClear);
}
//查询  下分
function down_mySelect() {
	var username = $("#_select_username").val();
	var tag = $("#_select_type").val();
	
	var begintime = $("#_startcreatetime").val();
	var endtime = $("#_endcreatetime").val();
	if (	begintime==null || begintime=='' ||
			endtime==null || endtime=='' ) {
		alert("请选择时间");
		return;
	}
	begintime = formatDatatime_localone(begintime);
	endtime = formatDatatime_localtwo(endtime);
	if (new Date(begintime)>new Date(endtime)) {
		alert("开始时间不能大于结束时间");
		return;
	}
	
	var param = {
		    "username":username,
		    "tag":tag,
			"begintime":begintime,
			"endtime":endtime
			};
	_init_(getURL("record_downfinanceselectlist"),6,param,_cbOk,baseCBErr,_cbClear);
}


//清空查询数据 上分
function cbClear(data) {
	if (data!=null) {
		for (var i=0; i < data.length; i++) {
			$("tr[id='"+data[i].id+"']").remove();
		}
	}
	$("#select_AMOUNT").val(0.00);
	$("#size").html(0);
	$("#total").html(0);
	$("#pageNum").html(0);
	$("#pages").html(0);
	$("#change").val("");
}
//清空查询数据 下分
function _cbClear(data) {
	if (data!=null) {
		for (var i=0; i < data.length; i++) {
			$("tr[id='"+data[i].id+"']").remove();
		}
	}
	$("#_select_AMOUNT").val(0.00);
	$("#_size").html(0);
	$("#_total").html(0);
	$("#_pageNum").html(0);
	$("#_pages").html(0);
	$("#_change").val("");
}

//查询成功回调函数   上分 查询
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
    $("#select_AMOUNT").val(amdata.amup);
	for (var i=0; i < data.length; i++) {
		

		        $("#myup").append(
		      				"<tr id="+data[i].id+">"+
							    "<td>"+data[i].username+"</td>"+
							    "<td>"+data[i].amount+"</td>"+
							    "<td>"+formatTime(data[i].updatetime)+"</td>"+
							    "<td>"+mapType[data[i].tag]+"</td>"+
						    "</tr>");
			
		
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}

//查询成功回调函数   下分 查询
function _cbOk(size,total,pageNum,pages,data,amdata) {
	$("#_error").html("");
	if (data==null || data.length==0) {
		$("#_error").html('no data');
		$("#_size").html(0);
		$("#_total").html(0);
		$("#_pageNum").html(0);
		$("#_pages").html(0);
		$("#_change").val("");
		return;
	}
    $("#_select_AMOUNT").val(amdata.amdown);
    
	for (var i=0; i < data.length; i++) {
		

		       $("#mydown").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].amount+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
							"<td>"+mapType[data[i].tag]+"</td>"+
						"</tr>");
			
		
	}
	
	        $("#_size").html(size);
	        $("#_total").html(total);
	        $("#_pageNum").html(pageNum);
	        $("#_pages").html(pages);
	        $("#_change").val("");
}
//// 流水量
//function synToday() {
//	var today = new Date().Format("yyyy-MM-dd");
//	if (today==null || today=='') {
//		alert("获取当前时间出错");
//		return;
//	}
//	today = formatDataInt(today);
//	
//	var param = {
//			"date":today
//	};
//	_send(getURL("record_bgetbetpaidbydate"),param,cbSynToday,baseCBErr);
//}
//function cbSynToday(data) {
//	$('#water_today').val(data.bet);
//	$('#profit_today').val(data.bet-data.paid);
//}

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

//下分  分页
//刷新
function flushPage_() {
	_sendPage_(cbOk,baseCBErr,cbClear);
}

//首页
function firstPage_() {
	_firstPage_(cbOk,baseCBErr,cbClear);
}

//尾页
function endPage_() {
	_endPage_(cbOk,baseCBErr,cbClear);
}

//上一页
function upPage_() {
	_upPage_(cbOk,baseCBErr,cbClear);
}

//下一页
function downPage_() {
	_downPage_(cbOk,baseCBErr,cbClear);
}

//跳转
function changePage_() {
	var index = $("#_change").val();
	_changePage_(index,cbOk,baseCBErr,cbClear);
}

