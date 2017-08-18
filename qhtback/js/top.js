$(function(){
	$("#span").html(localStorage.getItem("username"));
oddSelect();
	window.setInterval(oddSelect,10000);	//60s更新一次
});

function oddSelect() {
	var param = {};
	_send(getURL("finance_countselect"),param,cbOddSelect,baseCBErr);
}

function cbOddSelect(data) {
	$('#num').val(data.num);
	var tsyx = document.getElementById("tsyx");
	if(data.num != 0){
		tsyx.play();
	}
	$('#num').attr('disabled',true);
}


