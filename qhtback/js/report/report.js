
var mapType = {
		1:"真实",
		2:"虚拟"
};
var map_Type = {
		1:"投注",
		2:"已结算",
		3:"取消",
		4:"飞单"
};

$(function(){
		
	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-dd"));	//默认提前一周
	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-dd"));	//默认当前时间
//	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-ddThh:mm"));	//默认提前一周
//	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
	mySelect();
});


//查询
function mySelect() {
	
	//显示  隐藏一个div
        var oDiv_one = document.getElementById("show_toRuleone");
        if(oDiv_one.style.display == "block"){
            oDiv_one.style.display = "none";
        }else{
            oDiv_one.style.display = 'block';
        }
        
        var oDiv_two = document.getElementById("show_toRuletwo");
        if(oDiv_two.style.display == "none"){
            oDiv_two.style.display = "block";
        }else{
            oDiv_two.style.display = 'none';
        }
        
	var username = $("#select_username").val();
	if (username==null) {
		alert("出错");
		return;
	}
	var loginname = $("#select_loginname").val();
	if (loginname==null) {
		alert("出错");
		return;
	}
	
	var begindate = $("#startcreatetime").val();
	var enddate = $("#endcreatetime").val();
	if (	begindate==null || begindate=='' ||
			enddate==null || enddate=='' ) {
		alert("请选择时间");
		return;
	}
			
    begindate = formatDataInt(begindate);
	enddate = formatDataInt(enddate);
	if (begindate>enddate) {
		alert("开始时间不能大于结束时间");
		return;
//	begindate = formatDatatime_local(begindate);
//	enddate = formatDatatime_local(enddate);
//	if (new Date(begindate)>new Date(enddate)) {
//		alert("开始时间不能大于结束时间");
//		return;
	}

	var param = {
			"username":username,
			"loginname":loginname,
			"begindate":begindate,
			"enddate":enddate
			};
	_init(getURL("record_breportselectlist"),8,param,cbOk,baseCBErr,cbClear);
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
		var typeid = mapType[data[i].typeid];
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].id+"</td>"+
							"<td>"+data[i].date+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].loginname+"</td>"+
							"<td>"+typeid+"</td>"+
							"<td>"+data[i].recharge_amount+"</td>"+
							"<td>"+data[i].withdrawals_amount+"</td>"+
							"<td>"+data[i].bet_amount+"</td>"+
							"<td>"+data[i].paid_amount+"</td>"+
							"<td>"+data[i].recharge_count+"</td>"+
							"<td>"+data[i].withdrawals_count+"</td>"+
							"<td>"+data[i].game_count+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
							"<td>"+"<span><button onclick='my_Select("+data[i].id+");' href='#'>查看</button></span>"+"</td>"+
						"</tr>");
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}

//查看 细账
//查询
function my_Select(id) {
	
	//显示  隐藏一个div
	var oDiv_two = document.getElementById("show_toRuletwo");
        if(oDiv_two.style.display == "none"){
            oDiv_two.style.display = "block";
        }else{
            oDiv_two.style.display = 'none';
        }
        
	var oDiv_one = document.getElementById("show_toRuleone");
        if(oDiv_one.style.display == "none"){
            oDiv_one.style.display = "block";
        }else{
            oDiv_one.style.display = 'none';
        }
        
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	$('#show_into').val(record.recharge_amount);
	$('#show_into').attr('disabled',true);
	$('#show_outto').val(record.withdrawals_amount);
	$('#show_outto').attr('disabled',true);
	$('#show_date').val(record.date);
	$('#show_date').attr('disabled',true);
	$('#show_username').val(record.username);
	$('#show_loginname').val(record.loginname);
	$('#show_loginname').attr('disabled',true);
	$('#show_username').attr('disabled',true);
	$('#show_toRule').modal('show');
	
	var param = {
			"selectdate":record.date,
			"username":record.username,
			};
//			_send(getURL("report_scheme"),param,showSchemes,baseCBErr);
	_init(getURL("report_scheme"),8,param,showSchemes,baseCBErr,cb_Clear);
}

// 查询 明细 回调
function showSchemes(size,total,pageNum,pages,data) {
	
	$("#error").html("");
	if (data==null || data.length==0) {
//		$("#error").html('no data');
		$("#size").html(0);
		$("#total").html(0);
		$("#pageNum").html(0);
		$("#pages").html(0);
		$("#change").val("");
		return;
	}
        
//  $('#show_toRuletwo').modal('show');
//	$('#show_toRuleone').modal('hide');
	for (var i=0; i < data.length; i++) {
		
		var odd = data[i].odd;
		var period = data[i].period;
		var appcode = data[i].appcode;
		var username = data[i].username;
		var runway = data[i].runway;
		var bettype = data[i].bettype;
		var betamount = data[i].betamount;
		var paidamount = data[i].paidamount;
		var bettime = data[i].bettime;
		var paidtime = data[i].paidtime;
		
		var status = data[i].status;
		var type = map_Type[status];
		
		var context = "";
		var way = "";
		
		if (runway>=1 && runway<=10) {
			way += "第&nbsp;"+runway+"&nbsp;道"+"<br>";
			if (bettype<11) {
			context += bettype+"号&nbsp;"+betamount+"<br>";
		}
		}
		
		if (runway==11) {
			way += "冠亚"+"<br>";
			if (bettype<20) {
			context += "特&nbsp;"+bettype+"号&nbsp;"+betamount+"<br>";
		}
		}
		
		if (bettype==20) {
			context += "大&nbsp;"+betamount+"<br>";
		}
		if (bettype==21) {
			context += "小&nbsp;"+betamount+"<br>";
		}
		if (bettype==22) {
			context += "单&nbsp;"+betamount+"<br>";
		}
		if (bettype==23) {
			context += "双&nbsp;"+betamount+"<br>";
		}
		if (bettype==24) {
			context += "龙&nbsp;"+betamount+"<br>";
		}
		if (bettype==25) {
			context += "虎&nbsp;"+betamount+"<br>";
		}

		var context = "<tr id="+data[i].id+">"+
			"<td>"+odd+"</td>"+
			"<td>"+period+"</td>"+
			"<td>"+username+"</td>"+
			"<td>"+way+"</td>"+
			"<td>"+context+"</td>"+
			"<td>"+betamount+"</td>"+
			"<td>"+paidamount+"</td>"+
			"<td>"+formatTime(bettime)+"</td>"+
			"<td>"+formatTime(paidtime)+"</td>"+
			"<td>"+type+"</td>"+
			
		 "</tr>";
		
		$("#my_text").append(context);
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}
//var _getid;
//function showSchemes() {
//	_getid=id;
//	cb_Clear();
//	var record = _get(id);
//	if (record==null) {
//		alert("出错");
//		return;
//	}
//	$('#show_nickname').val(record.nickname);
//	$('#show_nickname').attr('disabled',true);
//	$('#show_username').val(record.username);
//	$('#show_username').attr('disabled',true);
//
//	var schemes = record.schemes;
//	for (var i=0; i < schemes.length; i++) {
//		var tmpid = schemes[i].id+999999999;
//		$("#mytext").append(
//						"<tr id="+tmpid+">"+
//						    "<td>"+schemes[i].id+"</td>"+
//							"<td>"+schemes[i].scheme+"</td>"+
//							"<td>"+schemes[i].time+"</td>"+
//							"<td>"+schemes[i].probabi+"</td>"+
//							"<td>"+
//							    "<span><a onclick='showInfoDel("+schemes[i].id+");' href='#'>删除</a></span>"+
//							"</td>"+
//						"</tr>");
//	}
//	$('#showtoRule').modal('show');
//	get_data=schemes;
//}

function cb_Clear(data) {
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
//function cb_Clear() {
//	if (get_data==null)
//		return;
//	
//	if (get_data!=null) {
//		for (var i=0; i < get_data.length; i++) {
//			var tmpid = get_data[i].id+999999999;
//			$("tr[id='"+tmpid+"']").remove();
//		}
//	}
//	get_data=null;
//}

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

