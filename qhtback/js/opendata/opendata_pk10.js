
$(function(){
	mySelect();
});


//查询
function mySelect() {
	var period = $("#period").val();
	var datetime = $("#date").val();
	if (datetime!='')
		datetime = formatData(datetime);
	
	var param = {
			"period":period,
			"date":datetime
			};
	_init(getURL("record_bpk10opencodeselectlist"),8,param,cbOk,baseCBErr,cbClear);
}


//清空查询数据
function cbClear(data) {
	if (data!=null) {
		for (var i=0; i < data.length; i++) {
			$("tr[id='"+i+"']").remove();
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
		var opencode = "";
		var obj = data[i].opencode;
		for (var j=0; j<obj.length; j++) {
			var tmp = obj[j];
			if (tmp<10)
				opencode += "0"+tmp+" ";
			else
				opencode += tmp+" ";
		}
		$("#mydb").append(
						"<tr id="+i+">"+
							"<td>"+data[i].period+"</td>"+
							"<td>"+opencode+"</td>"+
							"<td>"+formatTime(data[i].opentime)+"</td>"+
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

