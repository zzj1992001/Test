/**
 * 依赖说明：
 * AjaxF.iframeSubmit 依赖 jquery.ajaxsubmit.js
 * form.vilidator 依赖 validator.js  https://github.com/1000hz/bootstrap-validator
 * initAjaxProgress 依赖 nprogress.js   nprogress.css
 * alert dialog 依赖 bootstrap3-dialog  http://nakupanda.github.io/bootstrap3-dialog/
 * LoadingBtn 依赖 ladda.js
 * Toast 依赖 toastr.js
 */

var _Ajax = {
	_ajax: function(type, url, data, dataType, callback){
		$.ajax({
			type: type,
			url: url,
			data: data,
			dataType: dataType,
			success: function(result){
				if(callback != undefined){
					callback(result);
				}
			}
		});
	},
	getHtml: function(url, callback){
		_Ajax._ajax("get", url, {}, "html", callback);
	}
};

var FormFilter = {
	before: function($obj){
		/*
		if($obj.attr(CBAttr.SHOW) != undefined){
			var $target = $('#' + $obj.attr(CBAttr.SHOW));
			$target.html('');
		}*/
	}	
};

var AdminJS = {
	init: function(container){
		var targetC = container || $("body");
		
		AjaxA.init(targetC);
		ValidForm.init(targetC);
		_Pagination.init(targetC);
		Toast.init();
	},
	initAjaxProgress: function(){
		NProgress.configure({ easing: 'ease', speed: 1000, showSpinner: false});
		$(document).ajaxStart(function(){
			NProgress.start();
		});
		$(document).ajaxStop(function(){
			NProgress.done();
		});
	}
};

var Dialog = {
	current: null,
	close: function(){
		Dialog.current.close();
	},
	open: function($a){
		var $This = $a;
		var url = $This.attr('href');
		var title = $This.attr('data-title') || '请设置data-title属性';
		var cssClass = $This.attr('data-class') || '';
		var width = $This.attr('data-size') || BootstrapDialog.SIZE_NORMAL;
		var dialogId = $This.attr('data-id') || 'dialog_main';
		
		var $dialog = $('<div id="' + dialogId + '"><div class="dialog-loading"></div></div>');
		
		Dialog.current = BootstrapDialog.show({
			title: title,
			size: width,
			cssClass: cssClass,
			message: $dialog
	    });
		
		_Ajax.getHtml(url, function(html){
			setTimeout(function(){
				$dialog.html(html);
				AdminJS.init($dialog);
			}, 800);
		});
	},
	alertExpired: function(msg){
		BootstrapDialog.show({
            title: '登录失效',
            message: msg || '当前会话失效，请重新登录',
            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
            buttons:[{
            	label: '重新登录',
            	action: function(dialog){
            		location.href = "login.jsp";
            	}
            }]
        });
	},
	alertError: function(msg){
		BootstrapDialog.show({
            title: '操作失败',
            message: msg || '操作失败',
            closable: true,
            type: BootstrapDialog.TYPE_DANGER,
            cssClass: 'dialog-opacity'
        });
		var $backdrop = $('div.modal-backdrop');
		$backdrop.hide();
	},
	alertSuccess: function(msg){
		var d = BootstrapDialog.show({
            title: '操作成功',
            message: msg || '操作成功',
            type: BootstrapDialog.TYPE_SUCCESS,
            cssClass: 'dialog-opacity'
        });
		var $backdrop = $('div.modal-backdrop');
		$backdrop.hide();
		setTimeout(function(){
			d.close();
		}, 1500);
	}
};

var Toast = {
	init: function(){
		toastr.options = {
		  "closeButton": true,
		  "debug": false,
		  "newestOnTop": true,
		  "progressBar": false,
		  "positionClass": "toast-top-center",
		  "preventDuplicates": false,
		  "onclick": null,
		  "showDuration": "300",
		  "hideDuration": "1000",
		  "timeOut": "3000",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		};
	},
	success: function(msg){
		toastr.success(msg);
	},
	error: function(msg){
		toastr.error(msg);
	}
};

var AjaxA = {
	init: function(targetContainer){
		var target = targetContainer || $("body");
		$(target).find("a[target=div],a[target=json],a[target=dialog]").each(function(){
			var $a = $(this);
			$a.click(function(event){
				event.preventDefault();
				
				if($a.attr("disabled") != undefined && $a.attr("disabled") == "disabled") {
					return false;
				}
				
				if($a.attr("target") == "dialog"){
					Dialog.open($a);
				}else{
					var msg = $a.attr("data-confirm-msg");
					if(msg) {
						swal({
							  title: msg,
							  type: "warning",
							  showCancelButton: true,
							  confirmButtonColor: "#DD6B55",
							  confirmButtonText: "确定",
							  cancelButtonText: "取消",
							  closeOnConfirm: false
							},
							function(){
								swal.close();
								AjaxA.ajax($a);
							});
					}else{
						AjaxA.ajax($a);
					}
				}
				
				return false;
			});
		});
		
	},
	ajax: function($a){
		var url = $a.attr("href");
		if (url == undefined || url == '') {
			url = $a.attr("data-url");
		}
		
		var data = [];
		var dataPreUrl = $a.attr("data-pre-url");
		if (dataPreUrl != undefined && dataPreUrl != '') {
			data.push({name: 'dataPreUrl', value: dataPreUrl});
			//data['dataPreUrl'] = dataPreUrl;
		}
		
		var dataPreForm = $a.attr("data-pre-form");
		if (dataPreForm != undefined && dataPreForm != '') {
			var $dataForm = $('#' + dataPreForm).find('#pager');
			if ($dataForm) {
				var formData = $dataForm.serializeArray();
				var formDataStr = JSON.stringify(formData);
				//data['dataPreFormData'] = formDataStr;
				data.push({name: 'dataPreFormData', value: formDataStr});
			}
		}
		
		var postJsonStr = $a.attr("data-post-json");
		if (postJsonStr != undefined && postJsonStr != '') {
			var postJson = JSON.parse(postJsonStr);
			for (var i = 0; i < postJson.length; i++) {
				data.push(postJson[i]);
			}
		}
		
		//loading 按钮
		var $loadingBtn = new LoadingBtn($a);
		$loadingBtn.start();
		
		setTimeout(function(){
			_Ajax._ajax('post', url, data, 'html', function(result){
				_CB.doCallback($a, result);
				$loadingBtn.stop();
			});
		}, 200);
	}
};

var AjaxF = {
	normalSubmit: function(form){
		var $form = $(form);
		var url = $form.attr("action");
		var data = $form.serializeArray();
		
		FormFilter.before($form);
		
		//loading 按钮
		var $btn = $(document.activeElement);
		var $loadingBtn;
		if($btn && $btn.attr("type") == 'submit') {
			$loadingBtn = new LoadingBtn($btn);
			$loadingBtn.start();
		}
		
		setTimeout(function(){
			_Ajax._ajax("post", url, data, "html", function(data){
				_CB.doCallback($form, data);
				if ($loadingBtn) {
					$loadingBtn.stop();
				}
			});
		}, 200);
		return false;
	},
	iframeSubmit: function(form){
		var $form = $(form);
		
		FormFilter.before($form);
		
		//loading 按钮
		var $loadingBtn = new LoadingBtn($(document.activeElement));
		$loadingBtn.start();
		
		setTimeout(function(){
			$form.ajaxSubmit(function(data, textStatus, xhr){
		    	_CB.doCallback($form, data);
		    	$loadingBtn.stop();
		    });
		}, 200);
		return false;
	},
	refresh: function(containerId){
		var $form = $("#" + containerId).find("#pager:first");
		AjaxF.normalSubmit($form);
	}
};

function LoadingBtn($selector){
	if ($selector != undefined && $selector.length > 0 && $selector.attr("data-style") != undefined && $selector.attr("data-style") != '') {
		this.ladda = Ladda.create($selector[0]);
	}
}

LoadingBtn.prototype.start = function(){
	if(this.ladda != undefined) {
		this.ladda.start();
	}
}

LoadingBtn.prototype.stop = function(){
	if(this.ladda != undefined) {
		this.ladda.stop();
	}
}
var _CB = {
	doCallback: function($form, data){
		var result = JsonKit.tryJson(data);
		
		/** custom callback **/
		if($form.attr(CBAttr.CUSTOM) != undefined){
			_CB.custom($form, result);
		}
		
		if(!JsonKit.isJson(result)){
			/** HTML callback **/
			if($form.attr(CBAttr.SHOW) != undefined){
				_CB.show($form, result);
			}
			if($form.attr(CBAttr.DIALOG) == "open"){
				_CB.openDialog($form);
			}
			return;
		}
		
		/** JSON callback **/
		var json = result;
		
		/** CBCode.UNAUTH || CBCode.EXPIRED callback**/
		if(json.status_code == CBCode.EXPIRED){
			_CB.alertExpiredDialog(json);
			return;
		}
		
		if(json.status_code == CBCode.UNAUTH){
			_CB.alertUnAuthDialog(json);
			return;
		}
		
		if($form.attr(CBAttr.ALERT) == 'yes'){
			_CB.alert(json);
		}
		
		if(json.status_code != CBCode.SUCCESS){
			return;
		}
		
		/** CBCode.SUCCESS callback **/
		
		if($form.attr(CBAttr.CLEAR_INPUT) == 'yes'){
			_CB.clearInput($form, json);
		}
		
		if($form.attr(CBAttr.REFRESH) != undefined){
			_CB.refresh($form);
		}
		
		if($form.attr(CBAttr.DIALOG) == 'close'){
			_CB.closeDialog();
		}
		
		if ($form.attr(CBAttr.REMOVE) != undefined) {
			_CB.remove($form);
		}
	},
	alert: function(json){
		if (json.status_code == 200) {
			Toast.success(json.status_msg);
		}else{
			Toast.error(json.status_msg);
		}
	},
	clearInput: function($form, json){
		if (json.status_code == 200) {
			$form.find("input").each(function(){
				var $input = $(this);
				var inputType = $input.attr("type");
				if (inputType == 'checkbox') {
					$input.attr("checked", false);
				}else if (inputType == 'radio' || inputType == 'submit') {
				
				}else {
					$input.val("");
				}
			});
			$form.find("textArea").val("");
		}
	},
	show: function($form, html){
		var $targetC = $('#' + $form.attr(CBAttr.SHOW));
		
		setTimeout(function(){
			$targetC.html(html);
			AdminJS.init($targetC);
		}, 500);
	},
	refresh: function($form){
		var refreshId = $form.attr(CBAttr.REFRESH);
		AjaxF.refresh(refreshId);
	},
	openDialog: function($form){
		
	},
	closeDialog: function(){
		Dialog.close();
	},
	custom: function($form, result){
		var funObj = $form.attr(CBAttr.CUSTOM);
		var fun = FunKit.getFun(funObj);
		fun(result);
	},
	remove: function($form){
		var selector = $form.attr(CBAttr.REMOVE);
		if (selector == 'this') {
			$form.remove();
		}else {
			$(selector).remove();
		}
	},
	alertExpiredDialog: function(json){
		//Dialog.alertExpired(json.status_msg);
		alert(json.status_msg);
	},
	alertUnAuthDialog: function(json){
		//Dialog.alertError(json.status_msg);
		alert(json.status_msg);
	}
};

var CBCode = function(){};

CBCode.SUCCESS = 200;  //操作成功
CBCode.FAIL = 300;     //操作失败
CBCode.EXPIRED = 301;  //登录失效
CBCode.UNAUTH = 302;   //没有权限

var CBAttr = function(){};
CBAttr.SHOW = "data-cb-show";
CBAttr.ALERT = "data-cb-alert";
CBAttr.REFRESH = "data-cb-refresh";
CBAttr.CLEAR_INPUT = "data-cb-clearInput";
CBAttr.DIALOG = "data-cb-dialog";
CBAttr.CUSTOM = "data-cb-custom";
CBAttr.REMOVE = "data-cb-remove";

var JsonKit = {
	isJson: function(obj){
		return typeof(obj) == "object"  && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
	},
	tryJson: function(result){
		try{
			if(typeof(result) == "string"){
				var json = JSON.parse(result);
				return json;
			}
			return result;
		}catch(e){
			return result;
		}
	}
};

var FunKit = {
	getFun: function(fun){
		if($.isFunction(fun)){
			return fun;
		}
		if(fun != undefined && typeof(fun) == "string"){
			var funObj = eval("(" + fun + ")");
			return funObj;
		}
	}
};

/**
 * 表单校验
 */
var ValidForm = {
	init: function(target){
		var targetC = target || $('body');
		$(targetC).find('form[data-valid]').each(function(){
			var $form = $(this);
			$form.validator();
			$form.on('submit', function (e) {
				if (!e.isDefaultPrevented()) {
					if($form.attr("enctype") == 'multipart/form-data'){
						AjaxF.iframeSubmit($form);
					}else{
						AjaxF.normalSubmit($form);
					}
				}
				return false;
			});
		});
	}
};


/**
 * 分页
 */
var _Pagination = {
	init: function(targetC){
		var target = targetC || $('body');
		$(target).find(".c_pagination").each(function(){
			var $This = $(this);
			var pageNum = parseInt($This.attr("data-pageNum"));
			var pageSize = parseInt($This.attr("data-pageSize"));
			var totalRow = parseInt($This.attr("data-totalRow"));
			var showNum = $This.attr("data-showNum") == undefined ? 6 : parseInt($This.attr("data-showNum"));
			var rel = $This.attr("data-pageFormContainer");
			
			if(totalRow <= 0){
				return;
			}
			
			var preNum = pageNum - 1;
			var pre = '<li class="paginate_button previous"><a href="javascript:;" target="pagination" num="' + preNum + '">&laquo;&nbsp;前一页</a></li>';
			if(pageNum == 1){
				pre = '<li class="paginate_button previous disabled"><a href="javascript:;">&laquo;&nbsp;前一页</a></li>';
			}
			$This.append(pre);
			var totalPage = parseInt(totalRow / pageSize) + (totalRow % pageSize == 0 ? 0 : 1);
			for(var i = 1; i <= totalPage; i++){
				
				if(showNum < totalPage){
					
					if(i == (totalPage - 1)){
						$This.append('<li class="paginate_button disabled"><a href="javascript:;">...</a></li>');
						continue;
					}else if(i > showNum - 1 && i != totalPage ){
						continue;
					}
				}
				
				var li = '<li class="paginate_button"><a href="javascript:;" target="pagination" num="' + i + '">' + i + '</a></li>';
				if(pageNum == i){
					li = '<li class="paginate_button active"><a href="javascript:;" target="pagination" num="' + i + '">' + i + '</a></li>';
				}
				$This.append(li);
			}
			
			var nextNum = pageNum + 1;
			var next = '<li class="paginate_button next"><a href="javascript:;" target="pagination" num="' + nextNum +'">后一页&nbsp;&raquo;</a></li>';
			
			if(pageNum == totalPage){
				next = '<li class="paginate_button disabled next"><a href="javascript:;">后一页&nbsp;&raquo;</a></li>';
			}
			$This.append(next);
			
			var customText = $This.attr("data-customText");
			customText = customText == undefined ? '' : customText;
			var extra = "<span style='margin-left:10px; line-height:34px;'>第 <span class='text-danger'>" + pageNum + "</span>/<span class='text-primary'>" + totalPage + "</span> 页" + customText + "</span>";
			$This.append(extra);
			
			
			
			_Pagination._initClick($This, rel);
		});
	},
	_initClick: function(container, rel){
		var $form = $("#" + rel).find("#pager:first");
		
		$(container).find("a[target=pagination]").each(function(){
			var $This = $(this);
			var pageNum = $This.attr("num");
			$This.click(function(e){
				e.preventDefault();
				var $input = $form.find("input[name=pageNum]:first");
				$input.val(pageNum);
				AjaxF.normalSubmit($form);
			});
		});
	}
};


var LookUp = {
	initSelect: function(containerId){
		var $Div = containerId == undefined ? $(document) : $("#" + containerId);
		var existedVals;
		$Div.find("[bringBack]").each(function(){
			var $This = $(this);
			var attrVal = $This.attr("bringBack");
			var target = attrVal.split("=")[0];
			var val = attrVal.split("=")[1];
			if(existedVals == undefined){
				var inputVal = $("[lookUp=" + target + "]").val();
				existedVals = "," + inputVal + ",";
			}
			if(existedVals.indexOf("," + val + ",") >= 0){
				$This.addClass("am-btn-success am-disabled");
			}
		});
	},
	bringBack: function(option, lookUpName){
		for(var key in option){
			var selector = lookUpName + "_" + key;
			var val = option[key];
			var $input = $("[lookUp=" + selector + "]");
			if ($input.length > 0) {
				if($input[0].tagName.toLowerCase() == 'input'){
					$input.val(val);
				}else{
					$input.html(val);
				}			
			}
		}
		Dialog.close();
	},
	bringBackAppend: function(option, lookUpName, obj){
		var $This = $(obj);
		$This.addClass("am-btn-success am-disabled");
		for(var key in option){
			var selector = lookUpName + "_" + key;
			var val = option[key];
			var $input = $("[lookUp=" + selector + "]");
			
			if($input[0].tagName.toLowerCase() == 'input'){
				var originVal = $input.val();
				if(originVal != ""){
					originVal = originVal + ",";
				}
				$input.val(originVal+ val);
			}else{
				var originVal = $input.html();
				if(originVal != ""){
					originVal = originVal + ",";
				}
				$input.html(originVal+ val);
			}
		}
	},
	clear: function(targets){
		var targetArr = targets.split(",");
		for(var i = 0; i < targetArr.length; i++){
			var target = targetArr[i];
			var $Target = $("[lookUp=" + target + "]");
			
			if($Target[0].tagName.toLowerCase() == 'input'){
				$Target.val("");
			}else{
				$Target.html("");
			}
		}
	}
};

/*多级联动*/
var SelectLevel = {
	init: function(url, firstSelectId, topListId, defaultOpt) {
		var $This = SelectLevel;
		$.ajax({
			type: 'post',
			url: url,
			dataType: 'json',
			success: function(json){
				var data = json.data;
				var list = data[topListId];
				
				$("select[data-next-id]").change(function(){
					var $Me = $(this);
					var value = $Me.val();
					var nextId = $Me.attr("data-next-id");
					if (nextId == undefined || nextId == '') {
						return;
					}
					var dataList = data[value];
					
					$This._initList(nextId, dataList, defaultOpt.value, defaultOpt.text);
					var $nextSelect = $("#" + nextId);
					$nextSelect.change();
				});
				
				$This._initList(firstSelectId, list, defaultOpt.value, defaultOpt.text);
			}
		});
	},
	_initList: function(selectId, list, defaultValue, defaultText) {
		var $select = $("#" + selectId);
		var selectVal = $select.attr("data-select-value");
		$select.html("");
		$select.append('<option value="' + defaultValue +'">' + defaultText + '</option>');
		if (list == undefined) {
			return;
		}
		for (var i = 0; i < list.length; i++) {
			var obj = list[i];
			/*
			var option = '';
			if (selectVal != undefined && selectVal != '' && selectVal == obj.value) {
				option = '<option value="' + obj.value + '" selected>' + obj.descs + '</option>';
			}else {
				option = '<option value="' + obj.value + '">' + obj.descs + '</option>';
			}
			*/
			var option = '<option value="' + obj.value + '">' + obj.descs + '</option>';
			$select.append(option);
			if (selectVal != undefined && selectVal != '' && selectVal == obj.value) {
				$select.val(selectVal);
				$select.change();
			}
		}
		
	}
};

/***********************
** 远程加载下拉选择列表 **
************************/
var SingleSelect = {
	init: function(selectId){
		var $This = SingleSelect;
		var $select = $("#" + selectId);
		var url = $select.attr("data-url");
		var listKey = $select.attr("data-options-key");
		var $option = $select.find("option:first");
		var defaultVal = $option.val();
		var defaultText = $option.html();
		var selectVal = $select.attr("data-select-value");
		$.ajax({
			type: 'get',
			url: url,
			dataType: 'json',
			success: function(json) {
				var list = json[listKey];
				$This._initOptions(selectId, list, defaultVal, defaultText, selectVal);
			}
		});
	},
	_initOptions: function(selectId, list, defaultValue, defaultText, selectVal) {
		var $select = $("#" + selectId);
		$select.html("");
		$select.append('<option value="' + defaultValue +'">' + defaultText + '</option>');
		if (list == undefined) {
			return;
		}
		for (var i = 0; i < list.length; i++) {
			var obj = list[i];
			var option = '';
			if (selectVal != undefined && selectVal != '' && selectVal == obj.value) 
			{
				option = '<option value="' + obj.value + '" selected>' + obj.text + '</option>';
			}else {
				option = '<option value="' + obj.value + '">' + obj.text + '</option>';
			}
			$select.append(option);
		}
	}
};

var BeautifyImg = {
	init: function(selector){
		$(selector).on('load', function(){
			var This = BeautifyImg;
			This.doBeauity($(this));
		});
	},
	initOperate: function(imgItemSelector, optBarSelector){
		$(imgItemSelector).hover(function(){
			var $me = $(this);
			$me.find(optBarSelector).show();
		},
		function(){
			var $me = $(this);
			$me.find(optBarSelector).hide();
		});
	},
	doBeauity: function(obj){
		var $me = $(obj);
		var w = $me.width();
		var h = $me.height();
		if (w > h) {
			$me.css({"width": "auto", "height": "100%"});
			w = $me.width();
			h = $me.height();
			var marginL = -(w - h) / 2;
			var marginLPx = marginL + "px";
			$me.css("margin-left", marginLPx);
		}else {
			/*
			var marginT = -(h - 2) / 2;
			var marginTPx = marginT + "px";
			$me.css({"margin-top": marginTPx});
			*/
		}
	}
};