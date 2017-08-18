var _url = null;
var _count = 0;
var _param = null;

var _total = 0;	//总共有几条
var _pages = 0;	//总共有几页
var _pageNum = 0;	//当前第几页
var _size = 0;	//当前页实际有几条
var _arrData;	//列表数据
var _amData;	//列表数据

function _get(id) {
	for (var i=0; i < _arrData.length; i++) {
		if (_arrData[i].id==id)
			return _arrData[i];
	}
}

function _getall() {
	return _arrData;
}

function _init(url,count,param,cbOk,cbErr,cbClear) {
	_url = url;
	_count = count;
	_pageNum = 1;
	_param = {
			"page":_pageNum,
			"count":count
			};
	if(param!=null) {
		for(index in param) {
			_param[index] = param[index];
		}
	}
	
	_sendPage(cbOk,cbErr,cbClear);
}


function _firstPage(cbOk,cbErr,cbClear) {
	if (_pages < 1)
		return;
	if (_pageNum>1) {
		_pageNum = 1;
		_param["page"] = _pageNum;
		_sendPage(cbOk,cbErr,cbClear);
	}
}

function _endPage(cbOk,cbErr,cbClear) {
	if (_pages<=0)
		return;
	if (_pageNum < _pages) {
		_pageNum = _pages;
		_param["page"] = _pageNum;
		_sendPage(cbOk,cbErr,cbClear);
	}
}

function _upPage(cbOk,cbErr,cbClear) {
	if (_pages<=0)
		return;
	
	if (_pageNum>1) {
		_pageNum -= 1;
		_param["page"] = _pageNum;
		_sendPage(cbOk,cbErr,cbClear);
	}
}


function _downPage(cbOk,cbErr,cbClear) {
	if (_pages<=0)
		return;
	if (_pageNum < _pages) {
		_pageNum += 1;		
		_param["page"] = _pageNum;
		_sendPage(cbOk,cbErr,cbClear);
	}
}


function _changePage(page,cbOk,cbErr,cbClear) {

	if (page>0 && page!=_pageNum && page<=_pages) {
		_pageNum = page;		
		_param["page"] = _pageNum;
		_sendPage(cbOk,cbErr,cbClear);
	}
}



function _sendPage(cbOk,cbErr,cbClear) {
	$.ajax({
		data:_param,
		type:"POST",
		dataType:"json",
		url:_url,
		xhrFields:{withCredentials: true},
		error:function(data){
			cbClear(_arrData);	//回调
			_arrData = null;
			cbErr(-1,"select error");	//回调
		},
		success:function(data) {
			var code = data.code;
			var desc = data.desc;
			if (code!=1000) {
				cbClear(_arrData);	//回调
				_arrData = null;
				cbErr(code,desc);	//回调
				return;
			}
			
			var info = data.info;
			
			_total = info.total;
			_pages = info.pages;
			_pageNum = info.pageNum;
			_size = info.size;
			
			cbClear(_arrData);	//回调
			_arrData = info.obj;
			_amData = info.abj;
			cbOk(_size,_total,_pageNum,_pages,_arrData,_amData);	//回调
		}
	});
}