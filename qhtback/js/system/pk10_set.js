
var mapType = {
		1:'空格',
		2:'逗号',
		3:'点号',
		4:'分号',
		5:'斜号',
		6:'大于号',
		7:'小于号',
		8:'和号'
}

var gdata;

$(function(){
	mySelect();	
});

function mySelect() {
	var parameter = {};
	_send(getURL("pk10_betiniselectlist"),parameter,cbSelect,baseCBErr);
}
//查询成功回调函数
function cbSelect(data) {
	for (var i=0; i < data.length; i++) {
		var obj = "#type"+(i+1);
		if (data[i].tag==1) {
			$(obj).attr("checked",true);
		} else {
			$(obj).attr("checked",false);
		}
	}
	gdata = data;
}


function mySummit(index) {
	var obj = "#type"+index;
	var isCheck = $(obj).is(':checked');
	var tag = 2;
	if (isCheck)
		tag = 1;
	
	if (gdata[index-1].tag==tag) {
		alert("您没更改状态，不能提交");
		return;
	}
	
	var strTag = "删除";
	if (tag==1)
		strTag = "添加";
	
	if (!confirm("您确认要"+strTag+"'"+mapType[index]+"'吗"))
		return;
	
	var parameter = {
			"id":index,
			"tag":tag
	};
	_send(getURL("pk10_betiniupdate"),parameter,cbSummit,baseCBErr);
}
function cbSummit(data) {
	alert("成功");
	mySelect();
}