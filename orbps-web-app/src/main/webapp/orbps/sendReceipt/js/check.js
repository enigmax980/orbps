/* 新建命名空间 - 包级 */
com.orbps.sendReceipt = {};
/* 新建命名空间 - 页面级 */
com.orbps.sendReceipt.importw = {};
/* 新建命名空间 - 属性(进度条的增加值) */
com.orbps.sendReceipt.importw.barWidth = 0;
/* 新建命名空间 - 定时器命名 */
//com.orbps.sendReceipt.importw.showImp;
/* 新建命名空间 - 属性(上传文件的formData) */
com.orbps.sendReceipt.importw.formData;
/* 新建命名空间 - 属性(文件创建的时间) */
com.orbps.sendReceipt.importw.createDate = false;
/* 新建命名空间 - 属性（是否解析文件名） */
com.orbps.sendReceipt.importw.fileNameFlag = true;
//进度条
com.orbps.sendReceipt.importw.pct = 0;
com.orbps.sendReceipt.importw.clearFlag = 0;

com.orbps.sendReceipt.importw.lists = [];
// 正确的list
com.orbps.sendReceipt.importw.rightlists = [];

//分页设置(正确数据)
var theTable = document.getElementById("table2");
var totalPage = document.getElementById("spanTotalPage");
var pageNum = document.getElementById("spanPageNum");

var spanPre = document.getElementById("spanPre");
var spanNext = document.getElementById("spanNext");
var spanFirst = document.getElementById("spanFirst");
var spanLast = document.getElementById("spanLast");

var numberRowsInTable = theTable.rows.length;
var pageSize = 3;
var page = 1;


$(function (){
	// 时间控件初始化
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	
	// 文件上传参数设置
	$("#fileupload").fileinput({
        'allowedFileExtensions' : ['xlsm', 'xlt','xls','xlsx'],
        'showUpload':false,
        'showPreview':false,
        'showCaption':true,
        'browseClass':'btn btn-success',
    });
	
	//参数设置，若用默认值可以省略以下面代
    toastr.options = {
        'closeButton': true, //是否显示关闭按钮
        'debug': false, //是否使用debug模式
        'positionClass': 'toast-top-center', //弹出窗的位置
        'showDuration': '300', //显示的动画时间
        'hideDuration': '1000', //消失的动画时间
        'timeOut': '5000', //展现时间
        'extendedTimeOut': '1000', //加长展示时间
        'showEasing': 'swing', //显示时的动画缓冲方式
        'hideEasing': 'linear', //消失时的动画缓冲方式
        'showMethod': 'fadeIn', //显示时的动画方式
        'hideMethod': 'fadeOut' //消失时的动画方式
    };
    
	// 投保人姓名只读
	$("#applName").attr("readonly",true);
	
	// radio选中事件
	var str="applNo";
	$("input:radio[name=radio]").change(function() {
        var chkObjs = document.getElementsByName("radio");
        for(var i=0;i<chkObjs.length;i++){
        	var chk ;
            if(chkObjs[i].checked){
            	chk = i;
            	if(chk === 0){
            		$("#"+str).attr("id","applNo");
            		str = "applNo"
            		$("#"+str).attr("name","applNo");
            		document.getElementById('lable').innerHTML = '投保单号';
            		$(":input").val("");
            	}else if(chk === 1){
            		$("#"+str).attr("id","cgNo");
            		str = "cgNo"
            		$("#"+str).attr("name","cgNo");
            		document.getElementById('lable').innerHTML = '合同号';
            		$(":input").val("");
            	}
//            	else if(chk === 3){
//            		$("#"+str).attr("id","cntrNo");
//            		str = "cntrNo"
//            		$("#"+str).attr("name","cntrNo");
//            		document.getElementById('lable').innerHTML = '保单号';
//            		
//            	}else{
//            		$("#"+str).attr("id","applNo");
//            		str = "applNo"
//            		$("#"+str).attr("name","applNo");
//            		document.getElementById('lable').innerHTML = '汇交事件号';
//            	}
                break;
            }
        }
    });
	
	// 文件上传回调函数
//	$('#fileupload').fileupload({
//        dataType: 'json',
//        done: function (e, data) {
//        	com.orbps.sendReceipt.reloadBatchCheckTable(data.result);
//        }
//    });
	
});





// 点击核销
$("#btnCheck").click(function(){
	var receiptCvTaskVo = $("#pendingApprovalForm").serializeObject();
	var a = $("#applNo").val();
	var b = $("#sgNo").val();
	var c = $("#cgNo").val();
	if(lion.util.isNotEmpty(a) || lion.util.isNotEmpty(b) || lion.util.isNotEmpty(c)){
		lion.util.postjson('/orbps/web/orbps/sendreceipt/CvTask/implement',receiptCvTaskVo,com.orbps.sendReceipt.importw.successCheck,null,null);
	}else{
		lion.util.info("请输入核销数据！");
	}
});


// 导出批量核销模板
$('#btnDownload').click(function(){
	window.location.href="/orbps/web/orbps/sendreceipt/CvTask/downloadTemplate";
});



var batchNo="";
//提示信息标识
var flag = 0;
// 批量核销
$("#btnBatchCheck").click(function(){
	//清空提示标记的flag
	flag = 0;
	var path = $('#fileupload').val(); 
	if("" === path){
		lion.util.info("请选择要上传的文件");
		return false;
	};
	var myDate = new Date();
	 batchNo = myDate.getTime();
	 var formData = new FormData($('#uploadFile')[0]);
	lion.util.postfile("/orbps/web/orbps/sendreceipt/CvTask/uploads/"+batchNo,formData,com.orbps.sendReceipt.successChecks,null,null);
	clearInterval(com.orbps.sendReceipt.importw.clearFlag);
	com.orbps.sendReceipt.importw.clearFlag = setInterval("com.orbps.sendReceipt.importw.showImp()",1000);
});
//定时器进度查询
com.orbps.sendReceipt.importw.showImp = function(){
	console.log("定时器查询的flag是："+flag);
	lion.util.postjson("/orbps/web/orbps/sendreceipt/CvTask/queryProg",batchNo,com.orbps.sendReceipt.importw.schedule,null,null);
	if(100 === com.orbps.sendReceipt.importw.pct){
		lion.util.info("批量核销完成");
		clearInterval(com.orbps.sendReceipt.importw.clearFlag);
	}
}

//批量核销进度回调函数
com.orbps.sendReceipt.importw.schedule = function(data,args) {
	var batchVerSucc = data.succNum;
	var batchVerErr = data.failNum;
	var batchVerSum =data.sum;
	var handSum = batchVerSucc + batchVerErr;
	$("#spanPct").remove();
	com.orbps.sendReceipt.importw.pct=parseInt((handSum/batchVerSum)*100);
	$("#progressBar").attr("style","width: "+com.orbps.sendReceipt.importw.pct+"%;");
	$("#progressBar").append("<span id='spanPct'>"+com.orbps.sendReceipt.importw.pct+"%</span>");
	$("#batchVerFail").val(data.failNum);
	$("#batchVerSucc").val(data.succNum);
	$("#batchVerSum").val(data.sum);
	//如果返回的结果中有fail字段，就关闭定时器，提示错误信息。
		if("fail" in data){
			//错误提示标示
			flag++;
			console.log(flag);
			//如果错误提示标示等于1，就代表第一次进入提示信息，其他的就不在提示。
			if(flag === 1){
				//关闭定时器
				clearInterval(com.orbps.sendReceipt.importw.clearFlag);
				lion.util.info("提示",data.fail);
				return false;
			}
		}
};



// 投保单号丢失焦点回显投保人姓名
$("#applNo").blur(function(){
	var chkObjs = document.getElementsByName("radio");
	var applNo='';
    for(var i=0;i<chkObjs.length;i++){
    	var chk ;
        if(chkObjs[i].checked){
        	  if(chkObjs[i].checked){
              	chk = i;
              	if(chk === 0){
              		if(""===$("#pendingApprovalForm #applNo").val()){
              			$("#applName").val("");
              			lion.util.info("提示","请输入投保单号");
              			return;
              		}
              		 applNo = "applNo,"+$("#pendingApprovalForm #applNo").val();
              	}
            	if(chk === 1){
            		if(""=== $("#pendingApprovalForm #cgNo").val()){
            			$("#applName").val("");
              			lion.util.info("提示","请输入合同号");
              			return;
              		}
            		 applNo ="cgNo,"+ $("#pendingApprovalForm #cgNo").val();
              	}
        	  }
        }
    }
	if(lion.util.isNotEmpty(applNo)) {	
		lion.util.postjson('/orbps/web/orbps/sendreceipt/CvTask/queryApplName',applNo,com.orbps.sendReceipt.importw.successQueryName,null,null);
	}
});


// 回执查询
$("#applicantSaveForm #query").click(function() {	
	var insurApplCvTaskVo = $("#applicantSaveForm").serializeObject();
	if(insurApplCvTaskVo!==null){			
		// 添加查询参数
		$("#apTaskListTab").datagrids({
			querydata : insurApplCvTaskVo		    
		});
        // 重新加载数据
		$("#apTaskListTab").datagrids('reload');
	}else{
		lion.util.info("提示","请选择规则类型");
		return false;
	}
});


//批量回执核销导出错误数据
$("#errorExport").click(function(){
	lion.util.info("开始导出,请稍等！");
	lion.util
	.postjson(
			'/orbps/web/orbps/sendreceipt/CvTask/exists',
			batchNo,
			com.orbps.sendReceipt.importw.errData,
			null, null);
});

//导出错误数据回调函数
com.orbps.sendReceipt.importw.errData = function(data,obj){
	if (data === "1"){
		window.location.href="/orbps/web/orbps/sendreceipt/CvTask/download/"+batchNo;
	}else{
		lion.util.info("提示", "导出失败，失败原因：错误清单文件不存在");
	}
};

// 核销成功回调函数
 com.orbps.sendReceipt.importw.successCheck = function(data,obj){
	if("1"===data.retCode){
	   lion.util.info("提示","核销成功!");
	}else{
	   lion.util.info("提示",data.errMsg);
	}
}


// 查询投保人姓名
 com.orbps.sendReceipt.importw.successQueryName = function(data,obj){
	if(null === data.applName || undefined===data.applName || ''===data.applName){
		$("#applName").val("");
		lion.util.info("提示","查询不到投保人信息");
	}else{
	   var applName = data.applName;
        $("#applName").val(applName);
	}
}

//重新加载表格
com.orbps.sendReceipt.reloadBatchCheckTable = function(data) {
	com.orbps.sendReceipt.importw.lists = data;
	$('#batchList').find("tbody").empty();
	var c = 0;
	var f = 0;
	if (data !== null && data.length > 0) {
		for (var i = 0; i < data.length; i++) {
			var applNo = data[i].applNo;
			var signDate = data[i].signDate;
			var oDate1 = new Date(signDate);
			var oDate2 = new Date();
			var a = applNo.split("");
					com.orbps.sendReceipt.importw.rightlists.push(data[i]);
					numberRowsInTable = com.orbps.sendReceipt.importw.rightlists.length;
		}
		//遍历正确数据
		if(com.orbps.sendReceipt.importw.rightlists.length>0){
			if(com.orbps.sendReceipt.importw.rightlists.length<=pageSize){
				for (var i = 0; i < com.orbps.sendReceipt.importw.rightlists.length; i++) {
					var array_element = com.orbps.sendReceipt.importw.rightlists[i];
					$('#batchList').find("tbody")
					.append("<tr><td >"+(i+1)+"</td><td >"
							+ array_element.applNo
							+ "</td><td >"
							+ array_element.signDate
							+ "</td></tr>");
				}
			}else{
				for (var i = 0; i < pageSize; i++) {
					var array_element = com.orbps.sendReceipt.importw.rightlists[i];
					$('#batchList').find("tbody")
					.append("<tr><td >"+(i+1)+"</td><td >"
							+ array_element.applNo
							+ "</td><td >"
							+ array_element.signDate
							+ "</td></tr>");
				}
			}
		}
		totalPage.innerHTML = pageCount();
		pageNum.innerHTML = page;
		nextLink();
		lastLink();
	}
}

//下一页
function next() {
	hideTable();
	currentRow = pageSize * page;
	maxRow = currentRow + pageSize;
	if (maxRow > numberRowsInTable)
		maxRow = numberRowsInTable;
	for (var i = currentRow; i < maxRow; i++) {
		$('#batchList').find("tbody").empty();
	}
	page++;
	if (maxRow == numberRowsInTable) {
		nextText();
		lastText();
	}
	showPage();
	preLink();
	firstLink();
	hide();
}

//上一页
function pre() {
	hideTable();
	page--;
	currentRow = pageSize * page;
	maxRow = currentRow - pageSize;
	if (currentRow > numberRowsInTable)
		currentRow = numberRowsInTable;
	for (var i = maxRow; i < currentRow; i++) {
		$('#batchList').find("tbody").empty();
	}
	if (maxRow == 0) {
		preText();
		firstText();
	}
	showPage2();
	nextLink();
	lastLink();
	hide();
}

//第一页
function first() {
	hideTable();
	page = 1;
	for (var i = 0; i < pageSize; i++) {
		$('#batchList').find("tbody").empty();
	}
	for (var i = 0; i < pageSize; i++) {
		var array_element = com.orbps.sendReceipt.importw.rightlists[i];
		$('#batchList').find("tbody")
		.append("<tr><td >"+(i+1)+"</td><td >"
				+ array_element.applNo
				+ "</td><td >"
				+ array_element.signDate
				+ "</td></tr>");
	}
	preText();
	nextLink();
	lastLink();
	hide();
}

//最后一页
function last() {
	hideTable();
	page = pageCount();
	currentRow = pageSize * (page - 1);
	for (var i = currentRow; i < numberRowsInTable; i++) {
		$('#batchList').find("tbody").empty();
	}
	for (var i = currentRow; i < numberRowsInTable; i++) {
		var array_element = com.orbps.sendReceipt.importw.rightlists[i];
		$('#batchList').find("tbody")
		.append("<tr><td >"+(i+1)+"</td><td >"
				+ array_element.applNo
				+ "</td><td >"
				+ array_element.signDate
				+ "</td></tr>");
	}
	preLink();
	nextText();
	firstLink();
	hide();
}

function hideTable() {
	for (var i = 0; i < numberRowsInTable; i++) {
		$('#batchList').find("tbody").empty();
	}
}

function showPage() {
	for (var i = currentRow; i < maxRow; i++) {
		var array_element = com.orbps.sendReceipt.importw.rightlists[i];
		$('#batchList').find("tbody")
		.append("<tr><td >"+(i+1)+"</td><td >"
				+ array_element.applNo
				+ "</td><td >"
				+ array_element.signDate
				+ "</td></tr>");
	}
}

function showPage2() {
	for (var i = maxRow; i < currentRow; i++) {
		var array_element = com.orbps.sendReceipt.importw.rightlists[i];
		$('#batchList').find("tbody")
		.append("<tr><td >"+(i+1)+"</td><td >"
				+ array_element.applNo
				+ "</td><td >"
				+ array_element.signDate
				+ "</td></tr>");
	}
}

//总共页数
function pageCount() {
	var count = 0;
	numberRowsInTable = com.orbps.sendReceipt.importw.rightlists.length;
	if (numberRowsInTable % pageSize != 0)
		count = 1;
	return parseInt(numberRowsInTable / pageSize) + count;
}

//显示链接
function preLink() {
	spanPre.innerHTML = "<a href='javascript:pre();'>上一页</a>";
}
function preText() {
	spanPre.innerHTML = "上一页";
}

function nextLink() {
	spanNext.innerHTML = "<a href='javascript:next();'>下一页</a>";
}
function nextText() {
	spanNext.innerHTML = "下一页";
}

function firstLink() {
	spanFirst.innerHTML = "<a href='javascript:first();'>第一页</a>";
}
function firstText() {
	spanFirst.innerHTML = "第一页";
}

function lastLink() {
	spanLast.innerHTML = "<a href='javascript:last();'>最后一页</a>";
}
function lastText() {
	spanLast.innerHTML = "最后一页";
}

//隐藏表格
function hide() {
	totalPage.innerHTML = pageCount();
	pageNum.innerHTML = page;
	if(totalPage.innerHTML===pageNum.innerHTML+1){
		nextLink();
	}
	lastLink();
}

hide();

var idTmr;
// 兼容不同浏览器的内核
function getExplorer() {
	var explorer = window.navigator.userAgent;
	//ie
	if (explorer.indexOf("MSIE") >= 0) {
		return 'ie';
	}
	//firefox
	else if (explorer.indexOf("Firefox") >= 0) {
		return 'Firefox';
	}
	//Chrome
	else if (explorer.indexOf("Chrome") >= 0) {
		return 'Chrome';
	}
	//Opera
	else if (explorer.indexOf("Opera") >= 0) {
		return 'Opera';
	}
	//Safari
	else if (explorer.indexOf("Safari") >= 0) {
		return 'Safari';
	}
}


// 编译
function Cleanup() {
	window.clearInterval(idTmr);
	CollectGarbage();
}






