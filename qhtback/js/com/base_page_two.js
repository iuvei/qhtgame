var _url_ = null;
var _count_ = 0;
var _param_ = null;

var _total_ = 0;	//总共有几条
var _pages_ = 0;	//总共有几页
var _pageNum_ = 0;	//当前第几页
var _size_ = 0;	//当前页实际有几条
var _arrData_;	//列表数据
var _amData_;	//列表数据

function _get_(id) {
	for (var i=0; i < _arrData_.length; i++) {
		if (_arrData_[i].id==id)
			return _arrData_[i];
	}
}

function _init_(url,count,param,cbOk,cbErr,cbClear) {
	_url_ = url;
	_count_ = count;
	_pageNum_ = 1;
	_param_ = {
			"page":_pageNum_,
			"count":count
			};
	if(param!=null) {
		for(index in param) {
			_param_[index] = param[index];
		}
	}
	
	_sendPage_(cbOk,cbErr,cbClear);
}


function _firstPage_(cbOk,cbErr,cbClear) {
	if (_pages_ < 1)
		return;
	if (_pageNum_>1) {
		_pageNum_ = 1;
		_param_["page"] = _pageNum_;
		_sendPage_(cbOk,cbErr,cbClear);
	}
}

function _endPage_(cbOk,cbErr,cbClear) {
	if (_pages_<=0)
		return;
	if (_pageNum_ < _pages_) {
		_pageNum_ = _pages_;
		_param_["page"] = _pageNum_;
		_sendPage_(cbOk,cbErr,cbClear);
	}
}

function _upPage_(cbOk,cbErr,cbClear) {
	if (_pages_<=0)
		return;
	
	if (_pageNum_>1) {
		_pageNum_ -= 1;
		_param_["page"] = _pageNum_;
		_sendPage_(cbOk,cbErr,cbClear);
	}
}


function _downPage_(cbOk,cbErr,cbClear) {
	if (_pages_<=0)
		return;
	if (_pageNum_ < _pages_) {
		_pageNum_ += 1;		
		_param_["page"] = _pageNum_;
		_sendPage_(cbOk,cbErr,cbClear);
	}
}


function _changePage_(page,cbOk,cbErr,cbClear) {

	if (page>0 && page!=_pageNum_ && page<=_pages_) {
		_pageNum_ = page;		
		_param_["page"] = _pageNum_;
		_sendPage_(cbOk,cbErr,cbClear);
	}
}



function _sendPage_(cbOk,cbErr,cbClear) {
	$.ajax({
		data:_param_,
		type:"POST",
		dataType:"json",
		url:_url_,
		xhrFields:{withCredentials: true},
		error:function(data){
			cbClear(_arrData_);	//回调
			_arrData_ = null;
			cbErr(-1,"select error");	//回调
		},
		success:function(data) {
			var code = data.code;
			var desc = data.desc;
			if (code!=1000) {
				cbClear(_arrData_);	//回调
				_arrData_ = null;
				cbErr(code,desc);	//回调
				return;
			}
			
			var info = data.info;
			
			_total_ = info.total;
			_pages_ = info.pages;
			_pageNum_ = info.pageNum;
			_size_ = info.size;
			
			cbClear(_arrData_);	//回调
			_arrData_ = info.obj;
			_amData_ = info.abj;
			cbOk(_size_,_total_,_pageNum_,_pages_,_arrData_,_amData_);	//回调
		}
	});
}