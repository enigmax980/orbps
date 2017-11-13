com.common.insuredCount = 0;
com.common.insuredList = new Array();
com.common.insuredType = -1;

com.common.oranLevelCount = 0;
com.common.oranLevelType = -1;
com.common.oranLevelList = new Array();

$(function(){
	//datagrid组件初始化
	$("*").datagridsInitLoad();
	// 上一步下一步控件初始化
	$("*").stepInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	// 文件上传下载插件初始化
	$("#fileTal").fileinput({
		'allowedFileExtensions' : [ 'jpg', 'png', 'gif', 'xlsx' ],
		'showUpload' : true,
		'showPreview' : false,
		'showCaption' : true,
		'browseClass' : 'btn btn-success',
	});
	$("#fbp-editDataGrid").editDatagridsLoadById();
	// 初始化edittable组件
	// combo组件初始化
	$("*").comboInitLoad();

//	// 表单提交
 $("#btnSubmit").click(function () {
		// 获取要约信息下面的所有险种信息
		var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		// 提交方法
		var renewalEntryVo = {};
		renewalEntryVo.applInfo = com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.applInfo.serializeObject();
		renewalEntryVo.atherInfo = com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.atherInfo.serializeObject();
		renewalEntryVo.insuredGroupModalVos = com.common.insuredList;										
		renewalEntryVo.organizaHierarModalVos = com.common.oranLevelList;
		renewalEntryVo.busiPrdVos = getAddRowsData;
		var responseVos = new Array();
		for (var i = 0; i < com.orbps.common.list.length; i++) {
			var array_element = com.orbps.common.list[i];
			for (var j = 0; j < array_element.length; j++) {
				var array_elements = array_element[j];
				responseVos.push(array_elements);
			}
		}
		renewalEntryVo.responseVos = responseVos;
		alert(JSON.stringify(renewalEntryVo));
		lion.util.postjson('/orbps/web/orbps/contractEntry/renew/submit',renewalEntryVo,successSubmit,null,null);
	}); 
});


//被保人分组
$("#btnGoup").click(function() {
	  com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.addDialog.empty();
	  com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.addDialog.load("/orbps/orbps/contractEntry/modal/html/insuredGroupModal.html",function(){
		  $(this).modal('toggle');
		  $(this).comboInitLoad();
		  $(this).combotreeInitLoad();
		// 刷新table
	  });
	  setTimeout(function(){
		  // 回显
		  reloadPublicInsuredModalTable();
	  },100);
});


//组织层次架构 
$("#btnOranLevel").click(function() {
	com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.addDialog.empty();
	com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.addDialog.load("/orbps/orbps/contractEntry/modal/html/organizaHierarModal.html",function(){
		$(this).modal('toggle');
		// combo组件初始化
		$(this).comboInitLoad();
		$(this).combotreeInitLoad();
	});
	setTimeout(function(){
		// 回显
		reloadPublicOranLevelModalTable();
	},100);
});


//添加成功回调函数
function successSubmit(obj,data,arg){
	lion.util.info("提示","提交团单录入信息成功");
	// 成功后刷新页面
	setTimeOut(function(){
		window.location.reload();
	},500);
}

function validateSelectVal(){ 
	   
	//职业类型
	var occupationType = $("#applInfo #occupationType").val();	
	if(occupationType==null||""==occupationType){
		lion.util.info("警告","请选择职业类型");
		return false;
	}
	
	//证件类型
	var idType = $("#applInfo #idType").val();	
	if(idType==null||""==idType){
		lion.util.info("警告","请选择证件类型");
		return false;
	}
	
	//联系人证件类型
	var contactsIdType = $("#applInfo #contactsIdType").val();	
	if(contactsIdType==null||""==contactsIdType){
		lion.util.info("警告","请选择联系人证件类型");
		return false;
	}
	
	//联系人性别
	var contactsSex = $("#applInfo #contactsSex").val();	
	if(contactsSex==null||""==contactsSex){
		lion.util.info("警告","请选择联系人性别");
		return false;
	}
	
	//联系人出生日期
	var contactsBirthlDate = $("#applInfo #contactsBirthlDate").val();	
	if(contactsBirthlDate==null||""==contactsBirthlDate){
		lion.util.info("警告","联系人出生日期不能为空");
		return false;
	}
	
	
	//有效日期
	var Term = $("#atherInfo #Term").val();	
	if(Term==null||""==Term){
		lion.util.info("警告","有效日期不能为空");
		return false;
	}
	
	
	//争议处理方式
	var disputeHandling = $("#atherInfo #disputeHandling").val();	
	if(disputeHandling==null||""==disputeHandling){
		lion.util.info("警告","请选择争议处理方式");
		return false;
	}
	
	//结算方式
	var settlementMethod = $("#atherInfo #settlementMethod").val();	
	if(settlementMethod==null||""==settlementMethod){
		lion.util.info("警告","请选择结算方式");
		return false;
	}
	
	//结算日期
	var settlementlDate = $("#atherInfo #settlementlDate").val();	
	if(settlementlDate==null||""==settlementlDate){
		lion.util.info("警告","结算日期不能为空");
		return false;
	}
	
	//缴费形式
	var paymentForm = $("#atherInfo #paymentForm").val();	
	if(paymentForm==null||""==paymentForm){
		lion.util.info("警告","请选择缴费形式");
		return false;
	}
	
	
	//缴费方式
	var paymentMethod = $("#atherInfo #paymentMethod").val();	
	if(paymentMethod==null||""==paymentMethod){
		lion.util.info("警告","请选择缴费方式");
		return false;
	}
	
	//首期扣款日
	var initialStart = $("#atherInfo #initialStart").val();	
	if(initialStart==null||""==initialStart){
		lion.util.info("警告","首期扣款和截止日不能为空");
		return false;
	}
	
	//截止日
	var deadlineDay = $("#atherInfo #deadlineDay").val();	
	if(deadlineDay==null||""==deadlineDay){
		lion.util.info("警告","首期扣款和截止日不能为空");
		return false;
	}
	
	//保费来源
	var premiumSource = $("#atherInfo #premiumSource").val();	
	if(premiumSource==null||""==premiumSource){
		lion.util.info("警告","请选择保费来源");
		return false;
	}
	
	//银行代码
	var bankCode = $("#atherInfo #bankCode").val();	
	if(bankCode==null||""==bankCode){
		lion.util.info("警告","请选择银行代码");
		return false;
	}
	
	//异常告知
	var exceptionReport = $("#atherInfo #exceptionReport").val();	
	if(exceptionReport==null||""==exceptionReport){
		lion.util.info("警告","请选择异常告知");
		return false;
	}
	
	//清单标志
	var listFlag = $("#atherInfo #listFlag").val();	
	if(listFlag==null||""==listFlag){
		lion.util.info("警告","请选择清单标志");
		return false;
	}
	
	//清单打印
	var listPrint = $("#atherInfo #listPrint").val();	
	if(listPrint==null||""==listPrint){
		lion.util.info("警告","请选择清单打印");
		return false;
	}
	
	//个人凭证
	var personalDigitalID = $("#atherInfo #personalDigitalID").val();	
	if(personalDigitalID==null||""==personalDigitalID){
		lion.util.info("警告","请选择个人凭证");
		return false;
	}
	
	//保单类型
	var policyType = $("#atherInfo #policyType").val();	
	if(policyType==null||""==policyType){
		lion.util.info("警告","请选择保单类型");
		return false;
	}
	
	//赠送保险标志
	var giftSign = $("#atherInfo #giftSign").val();	
	if(giftSign==null||""==giftSign){
		lion.util.info("警告","请选择赠送保险标志");
		return false;
	}
	
	return true;
}

