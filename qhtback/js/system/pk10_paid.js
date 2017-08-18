
$(function(){
 mySelect();
 myBetSelect();
});


//查询  赔率设置
function mySelect() {
	var id = 1;
	id = parseInt(id);
//	if (id==null) {
//		alert("出错");
//		return;
//	}
//	
//	if (id!='') {
//		id = parseInt(id);
//		if (id<=0) {
//			alert("请重新输入编号 ");
//			return;
//		}
//	} else {
//		id = 0;
//	}
	
	var param = {
			"id":id
			};
	_send(getURL("pk10Prid_pridSelectlist"),param,cbSelect,baseCBErr);
}
function cbSelect(data) {
	$("#edit_id").val(data.id);
	$("#edit_n_number").val(data.n_number);
	$("#edit_n_big").val(data.n_big);
	$("#edit_n_small").val(data.n_small);
	$("#edit_n_single").val(data.n_single);
	$("#edit_n_double").val(data.n_double);
	$("#edit_n_dragon").val(data.n_dragon);
	$("#edit_n_tiger").val(data.n_tiger);
	$("#edit_s_big").val(data.s_big);
	$("#edit_s_small").val(data.s_small);
	$("#edit_s_single").val(data.s_single);
	$("#edit_s_double").val(data.s_double);
	$("#edit_s_number_341819").val(data.s_number_341819);
	$("#edit_s_number_561617").val(data.s_number_561617);
	$("#edit_s_number_781415").val(data.s_number_781415);
	$("#edit_s_number_9101213").val(data.s_number_9101213);
	$("#edit_s_number_11").val(data.s_number_11);
}


//查询  下注设置
function myBetSelect() {
	var id = 1;
	id = parseInt(id);
	
	var param = {
			"id":id
			};
	_send(getURL("pk10_Bet_Select"),param,BETSelect,baseCBErr);
}
function BETSelect(data) {
	$("#edit_ID").val(data.id);
	$("#edit_big_small_low").val(data.big_small_low);
	$("#edit_big_small_high").val(data.big_small_high);
	$("#edit_single_double_low").val(data.single_double_low);
	$("#edit_single_double_high").val(data.single_double_high);
	$("#edit_dragon_tiger_low").val(data.dragon_tiger_low);
	$("#edit_dragon_tiger_high").val(data.dragon_tiger_high);
	$("#edit_n_number_low").val(data.n_number_low);
	$("#edit_n_number_high").val(data.n_number_high);
	$("#edit_s_number_low").val(data.s_number_low);
	$("#edit_s_number_high").val(data.s_number_high);
}

//<!--id big_small_low  big_small_high  single_double_low single_double_high  dragon_tiger_low dragon_tiger_high n_number_low n_number_high s_number_low s_number_high 
//修改 下注			-->
function editBet() {
	var id = $("#edit_ID").val();
	id = parseInt(id);
	
	var big_small_low = $("#edit_big_small_low").val();
	if (big_small_low=='') {
		alert("大小最低不能为空");
		return;
	}
	
	var big_small_high = $("#edit_big_small_high").val();
	if (big_small_high=='') {
		alert("大小最高不能为空");
		return;
	}
	
	var single_double_low = $("#edit_single_double_low").val();
	if (single_double_low=='') {
		alert("单双最低不能为空");
		return;
	}
	
	var single_double_high = $("#edit_single_double_high").val();
	if (single_double_high=='') {
		alert("单双最高不能为空");
		return;
	}
	var dragon_tiger_low = $("#edit_dragon_tiger_low").val();
	if (dragon_tiger_low=='') {
		alert("龙虎最低不能为空");
		return;
	}
	var dragon_tiger_high = $("#edit_dragon_tiger_high").val();
	if (dragon_tiger_high=='') {
		alert("龙虎最高不能为空");
		return;
	}
	var n_number_low = $("#edit_n_number_low").val();
	if (n_number_low=='') {
		alert("特码最低不能为空");
		return;
	}
	var n_number_high = $("#edit_n_number_high").val();
	if (n_number_high=='') {
		alert("特码最高不能为空");
		return;
	}
	var s_number_low = $("#edit_s_number_low").val();
	if (s_number_low=='') {
		alert("和值最低不能为空");
		return;
	}
	var s_number_high = $("#edit_s_number_high").val();
	if (s_number_high=='') {
		alert("和值最高不能为空");
		return;
	}
	//<!--id bing_small_low  big_small_high  single_double_low single_double_high  dragon_tiger_low dragon_tiger_high n_number_low n_number_high s_number_low s_number_high 
//修改 下注			-->
	var parameter = {
			"id":id,
			"big_small_low":big_small_low,
			"big_small_high":big_small_high,
			"single_double_low":single_double_low,
			"single_double_high":single_double_high,
			"dragon_tiger_low":dragon_tiger_low,
			"dragon_tiger_high":dragon_tiger_high,
			"n_number_low":n_number_low,
			"n_number_high":n_number_high,
			"s_number_low":s_number_low,
			"s_number_high":s_number_high
			};
	_send(getURL("pk10_Bet_edit"),parameter,betEdit,baseCBErr);
}
function betEdit(data) {
	$('#editInfo').modal('hide');
	alert("下注修改成功");
	flushPage();
}


//<!--id bing_small_low n_big n_small n_single n_double n_dragon n_tiger s_big s_small s_single s_double
//				s_number_341819 s_number_561617 s_number_781415 s_number_9101213 s_number_11
//	赔率 修改		-->
function editInfo() {
	var id = $("#edit_id").val();
	id = parseInt(id);
	
	var n_number = $("#edit_n_number").val();
	if (n_number=='') {
		alert("特码赔付不能为空");
		return;
	}
	
	var n_big = $("#edit_n_big").val();
	if (n_big=='') {
		alert("大赔付不能为空");
		return;
	}
	
	var n_small = $("#edit_n_small").val();
	if (n_small=='') {
		alert("小赔付不能为空");
		return;
	}
	
	var n_single = $("#edit_n_single").val();
	if (n_single=='') {
		alert("单赔付不能为空");
		return;
	}
	var n_double = $("#edit_n_double").val();
	if (n_double=='') {
		alert("双赔付不能为空");
		return;
	}
	var n_dragon = $("#edit_n_dragon").val();
	if (n_dragon=='') {
		alert("龙赔付不能为空");
		return;
	}
	var n_tiger = $("#edit_n_tiger").val();
	if (n_tiger=='') {
		alert("虎赔付不能为空");
		return;
	}
	var s_big = $("#edit_s_big").val();
	if (s_big=='') {
		alert("冠亚和大赔付不能为空");
		return;
	}
	var s_small = $("#edit_s_small").val();
	if (s_small=='') {
		alert("冠亚和小赔付不能为空");
		return;
	}
	var s_single = $("#edit_s_single").val();
	if (s_single=='') {
		alert("冠亚和单赔付不能为空");
		return;
	}
	var s_double = $("#edit_s_double").val();
	if (s_double=='') {
		alert("冠亚和双赔付不能为空");
		return;
	}
	var s_number_341819 = $("#edit_s_number_341819").val();
	if (s_number_341819=='') {
		alert("冠亚和数字341819赔付不能为空");
		return;
	}
	var s_number_561617 = $("#edit_s_number_561617").val();
	if (s_number_561617=='') {
		alert("冠亚和数字561617赔付不能为空");
		return;
	}
	var s_number_781415 = $("#edit_s_number_781415").val();
	if (s_number_781415=='') {
		alert("冠亚和数字781415赔付不能为空");
		return;
	}
	var s_number_9101213 = $("#edit_s_number_9101213").val();
	if (s_number_9101213=='') {
		alert("冠亚和数字9101213赔付不能为空");
		return;
	}
	var s_number_11 = $("#edit_s_number_11").val();
	if (s_number_11=='') {
		alert("冠亚和数字11赔付不能为空");
		return;
	}
	//<!--id n_number n_big n_small n_single n_double n_dragon n_tiger s_big s_small s_single s_double
//				s_number_341819 s_number_561617 s_number_781415 s_number_9101213 s_number_11
//			-->
	var parameter = {
			"id":id,
			"n_number":n_number,
			"n_big":n_big,
			"n_small":n_small,
			"n_single":n_single,
			"n_double":n_double,
			"n_dragon":n_dragon,
			"n_tiger":n_tiger,
			"s_big":s_big,
			"s_small":s_small,
			"s_single":s_single,
			"s_double":s_double,
			"s_number_341819":s_number_341819,
			"s_number_561617":s_number_561617,
			"s_number_781415":s_number_781415,
			"s_number_9101213":s_number_9101213,
			"s_number_11":s_number_11
			};
	_send(getURL("pk10Prid_editinfo"),parameter,cbEdit,baseCBErr);
}
function cbEdit(data) {
	$('#editInfo').modal('hide');
	alert("赔率修改成功");
	flushPage();
}


