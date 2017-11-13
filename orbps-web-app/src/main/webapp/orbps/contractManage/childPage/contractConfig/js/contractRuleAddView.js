// 新建datagrid命名空间
com.orbps.contractManage.childPage={};
com.orbps.contractManage.childPage.contractConfig={};

$(function(){
	//日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});	
	
	// 初始化edittable组件
	$("#cntrComponentList").editDatagridsLoadById();

	// 文件上传下载插件初始化
//	$("#file").fileinput({
//        'allowedFileExtensions' : ['jpg', 'png','gif','xlsx'],
//        'showUpload':true,
//        'showPreview':false,
//        'showCaption':true,
//        'browseClass':'btn btn-success',
//	});
	
	//增加表格
	$("#btnAdd").click(function() {
		$("#cntrComponentList").editDatagrids("addRows");
		return false;
	});
	
	//删除表格
	$("#btnDel").click(function () {  
		$("#cntrComponentList").editDatagrids("delRows");
    }); 

});