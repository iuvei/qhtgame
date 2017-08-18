var LotteryType = {
    0:"全部",
    1:"北京赛车"
};

$(function(){
    for(var index in LotteryType){
        $("#gameid").append("<option value='"+index+"'>"+LotteryType[index]+"</option>");
    }
    for(var index in LotteryType) {
        if (index != 0)
            $("#add_gameid").append("<option value='" + index + "'>" + LotteryType[index] + "</option>");
    }
    for(var index in LotteryType) {
        if (index != 0)
            $("#edit_gameid").append("<option value='" + index + "'>" + LotteryType[index] + "</option>");
    }
    mySelect();
});


//查询
function mySelect() {
    var lottery_id = $("#gameid option:selected").val();
    if (lottery_id=='')
        lottery_id = 0;
    else
        lottery_id = parseInt(lottery_id);

    var param = {
        "lottery_id":lottery_id
    };
    _init(getURL("pk10_pointoutselectlist"),8,param,cbOk,baseCBErr,cbClear);
}

function showDel(id) {
    if (!confirm("确定要删除【id="+id+"】吗？"))
        return;
    var record = _get(id);
    if (record==null) {
        alert("出错");
        return;
    }

    var parameter = {
        "lottery_id":record.lottery_id,
        "text_id":record.text_id
    };
    _send(getURL("pk10_pointoutdelete"),parameter,cbDel,baseCBErr);
}
function cbDel() {
    alert("删除成功");
    flushPage();
}

function showEdit(id) {
    var record = _get(id);
    if (record==null) {
        alert("出错");
        return;
    }

    $("#edit_gameid ").val(record.lottery_id);
    $("#edit_textid").val(record.text_id);
    $("#edit_spacetime").val(record.spacetime);
    $("#edit_text").val(record.text);

    $('#edit_gameid').attr('disabled',true);
    $('#edit_textid').attr('disabled',true);

    $('#editPointout').modal('show');
}
function myEdit() {
    var lottery_id = $("#edit_gameid option:selected").val();
    if (lottery_id=='') {
        alert("彩种不能为空");
        return;
    }
    lottery_id = parseInt(lottery_id);

    var text_id = $("#edit_textid").val();
    if (text_id=='') {
        alert("编号不能为空");
        return;
    }
    text_id = parseInt(text_id);

    var spacetime = $("#edit_spacetime").val();
    if (spacetime=='') {
        alert("离开奖时间不能为空");
        return;
    }
    spacetime = parseInt(spacetime);

    var text = $("#edit_text").val();
    if (text=='') {
        alert("内容不能为空");
        return;
    }

    var parameter = {
        "lottery_id":lottery_id,
        "text_id":text_id,
        "spacetime":spacetime,
        "text":text
    };
    _send(getURL("pk10_pointoutedit"),parameter,cbEdit,baseCBErr);
}
function cbEdit() {
    $('#editPointout').modal('hide');
    alert("修改成功");
    flushPage();
}


//添加记录
function showAdd() {
    $('#addPointout').modal('show');
}
function myAdd() {
    var lottery_id = $("#add_gameid option:selected").val();
    if (lottery_id=='') {
        alert("彩种不能为空");
        return;
    }
    lottery_id = parseInt(lottery_id);

    var text_id = $("#add_textid").val();
    if (text_id=='') {
        alert("编号不能为空");
        return;
    }
    text_id = parseInt(text_id);

    var spacetime = $("#add_spacetime").val();
    if (spacetime=='') {
        alert("离开奖时间不能为空");
        return;
    }
    spacetime = parseInt(spacetime);

    var text = $("#add_text").val();
    if (text=='') {
        alert("内容不能为空");
        return;
    }

    var parameter = {
        "lottery_id":lottery_id,
        "text_id":text_id,
        "spacetime":spacetime,
        "text":text
    };
    _send(getURL("pk10_pointoutinsertone"),parameter,cbAdd,baseCBErr);
}
function cbAdd(data) {
    $('#addPointout').modal('hide');
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
            "<td>"+LotteryType[data[i].lottery_id]+"</td>"+
            "<td>"+data[i].text_id+"</td>"+
            "<td>"+data[i].spacetime+"</td>"+
            "<td>"+data[i].text+"</td>"+
            "<td>" +
                "<span><a onclick='showEdit("+data[i].id+");' href='#'>编辑</a></span>" +
                "<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
                "<span><a onclick='showDel("+data[i].id+");' href='#'>删除</a></span>" +
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

