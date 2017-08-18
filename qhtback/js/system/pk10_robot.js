
$(function(){
	myCount();
	mySelect();
});

function myCount() {
	var parameter = {};
	_send(getURL("pk10robot_getcount"),parameter,cbCount,baseCBErr);
}
function cbCount(data) {
	$("#onlineCount").html(data);
}

function mySummit() {
	var count = $("#set_robot").val();
	if (set_robot==null) {
		alert("出错");
		return;
	}
	count = parseInt(count);
	if (count<0) {
		alert("出错");
		return;
	}
	
	var parameter = {
			"count":count
			};
	_send(getURL("pk10robot_setcount"),parameter,cbSummit,baseCBErr);
}
function cbSummit(data) {
	alert("设置成功");
	$("#onlineCount").html(data);
}

function mySelect() {
	var param = {};
	_init(getURL("pk10robot_selectlist"),8,param,cbOk,baseCBErr,cbClear);
}

//添加记录
function addShow() {
	$("#add_username").val("");
	$('#addPlayer').modal('show');
}
function myAdd() {
	var username = $("#add_username").val();
	if (username=='') {
		alert("玩家名不能为空");
		return;
	}
	
	var parameter = {
			"appcode":"GS1001",
			"username":username
			};
	_send(getURL("pk10robot_insertone"),parameter,cbAdd,baseCBErr);
}
function cbAdd(data) {
	$('#addPlayer').modal('hide');
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
							"<td>"+data[i].appcode+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+data[i].balance+"</td>"+
							"<td>"+formatTime(data[i].updatetime)+"</td>"+
							"<td>"+"我是机器人："+(i+1)+"号"+"</td>"+
							"<td>"+
							    "<span><a onclick='showEdit("+data[i].id+");' href='#'>编辑</a></span>"+"<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
								"<span><a onclick='showDel("+data[i].id+");' href='#'>删除</a></span>"+
							"</td>"+
						"</tr>");
	}

	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}

//编辑 机器人
function showEdit(id) {
    var record = _get(id);
    if (record==null) {
        alert("出错");
        return;
    }

    $("#edit_appid ").val(record.id);
    $("#edit_appcode ").val(record.appcode);
    $("#edit_username").val(record.username);
    $("#edit_balance").val(record.balance);

    $('#edit_appid').attr('disabled',true);
    $('#edit_appcode').attr('disabled',true);

    $('#editRobot').modal('show');
}
function myEdit() {
    var appcode = $("#edit_appcode").val();
    var id = $("#edit_appid").val();

    var username = $("#edit_username").val();
    if (username=='') {
        alert("玩家名不能为空");
        return;
    }
//  text_id = parseInt(text_id);

    var balance = $("#edit_balance").val();
    if (balance=='') {
        alert("金额不能为空");
        return;
    }
    balance = parseInt(balance);

    var parameter = {
        "id":id,
        "appcode":appcode,
        "username":username,
        "balance":balance
    };
    _send(getURL("pk10robot_editone"),parameter,cbEdit,baseCBErr);
}
function cbEdit() {
    $('#editRobot').modal('hide');
    alert("修改成功");
    flushPage();
}


//删除 机器人
function showDel(id) {
    if (!confirm("确定要删除【id="+id+"】吗？"))
        return;
    var record = _get(id);
    if (record==null) {
        alert("出错");
        return;
    }

    var parameter = {
        "username":record.username,
        "id":record.id
    };
    _send(getURL("pk10robot_delone"),parameter,cbDel,baseCBErr);
}
function cbDel() {
    alert("删除成功");
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

