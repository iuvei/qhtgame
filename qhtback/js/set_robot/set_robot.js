
var mapStatus = {
		1:"有效",
		2:"无效"
};

$(function(){
//	for(var index in mapStatus){
//		$("#select_type").append("<option value='"+index+"'>"+mapType[index]+"</option>");
//	}
	
	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-ddThh:mm"));	//默认提前一周
	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
	mySelect();
});

//查询
function mySelect() {
	var period = $("#select_period").val();
	var nickname = $("#select_nickname").val();
	
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
			"period":period,
			"nickname":nickname,
			"begintime":begintime,
			"endtime":endtime
			};
	_init(getURL("set_robot"),8,param,cbOk,baseCBErr,cbClear);
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
							"<td>"+data[i].period+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].nickname+"</td>"+
							"<td>"+data[i].text+"</td>"+
							"<td>"+status+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
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

