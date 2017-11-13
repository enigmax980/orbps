com.orbps.contractEntry = {};
com.orbps.contractEntry.imageAudit = {};
com.orbps.contractEntry.imageAudit.applNo;
com.orbps.contractEntry.imageAudit.taskId;
//查询清汇信息，得到销售员信息。
com.orbps.contractEntry.imageAudit.successQuery = function(data,arg){
	com.orbps.contractEntry.imageAudit.applNo = data.applInfoVo.applNo;
	com.orbps.contractEntry.imageAudit.taskId = data.taskInfo.taskId;
	//查询审批轨迹。
	lion.util.postjson('/orbps/web/orbps/otherfunction/imageAudit/query',com.orbps.contractEntry.imageAudit.applNo,function(data,arg){
		$('#imageAuditList').editDatagrids("bodyInit", data);
	});
	com.orbps.contractEntry.imageAudit.grpSalesListFormVos =  data.applInfoVo.grpSalesListFormVos;
	if(data.applInfoVo.quotaEaNo !== undefined){
		com.orbps.contractEntry.imageAudit.quotaEaNoId = data.applInfoVo.quotaEaNo;
	}else{
		com.orbps.contractEntry.imageAudit.quotaEaNoId = "";
	}
	com.orbps.contractEntry.imageAudit.relodmessage();
}
$(function(){
	$("*").comboInitLoad();
	var dataCipher=com.ipbps.getDataCipher();
	var url= '/orbps/web/authSupport/check?serviceName=imageAuditServiceImpl&dataCipher='+dataCipher;
	lion.util.postjson(url,null,com.orbps.contractEntry.imageAudit.successQuery);
//	var applNo = "2017032455550005";
//	lion.util.postjson('/orbps/web/orbps/otherfunction/imageAudit/serch',
//			applNo,com.orbps.contractEntry.imageAudit.successQuery);
	$("#auditFlag").change(function(){
		var auditFlag = $("#auditFlag").val();
		if(auditFlag === "N"){
			$("#auditNotReasondiv").show();
		}else if(auditFlag === "Y"){
			$("#auditNotReasondiv").hide();
		}
	});
	//查询方式改变时，根据条件改变投保单号或者报建审批号
	$("#queryForm").change(function(){
		var queryForm = $("#queryForm").val();
		if(queryForm === "applNo"){
			$("#applNoType").show();
			$("#quotaEaNoType").hide();
			var applNo = $("#applNo").val();
			$("#imageAuditForm #tbdh").val(applNo);
		}else if(queryForm === "quotaEaNo"){
			$("#quotaEaNoType").show();
			$("#applNoType").hide();
			//报价审批号
			$("#imageAuditForm #tbdh").val(com.orbps.contractEntry.imageAudit.quotaEaNoId);
		}
	});
	
	
	
		// 影像采集方法
	com.orbps.contractEntry.imageAudit.acquisition = function(mode) {
			var btnCollection = $("#imageAuditForm").serializeObject();
			var inputparams = {};
			inputparams.mode = mode;
			var indexinfos = new Array();
			var indexinfo = {};
			var field = new Array();
	
			if(lion.util.isNotEmpty(btnCollection)){
				if(btnCollection.appid!==""){
					var fieldVo1 = {};
					fieldVo1.name = "appid";
					fieldVo1.value = btnCollection.appid;
					field.push(fieldVo1);
				}else{
					lion.util.info("提示","请选择类型")
					return false;
				}
				
				if(btnCollection.mngdeptid!==""){
					var fieldVo5 = {};
					fieldVo5.name = "mngdeptid";
					fieldVo5.value = btnCollection.mngdeptid;
					field.push(fieldVo5);
				}else{
					lion.util.info("提示","请填写管理机构代码")
					return false;
				}
	
				var queryForm = $("#queryForm").val();
				if(queryForm==="applNo"){
					if(btnCollection.tbdh!==""){
						var fieldVo2 = {};
						fieldVo2.name = "TBDH";
						fieldVo2.value = com.orbps.contractEntry.imageAudit.applNo;
					}else{
						lion.util.info("提示","请填写投保单号")
						return false;
					}
					field.push(fieldVo2);
				}else if (queryForm==="quotaEaNo"){
					if(btnCollection.tbdh!==""){
						var fieldVo2 = {};
						fieldVo2.name = "TBDH";
						fieldVo2.value = $("#imageAuditForm #tbdh").val();
					}else{
						lion.util.info("提示","请填写报价审批号")
						return false;
					}
					field.push(fieldVo2);
				}
				
				if(btnCollection.batchid!==""){
					var fieldVo3 = {};
					fieldVo3.name = "batchid";
					fieldVo3.value = btnCollection.batchid;
					field.push(fieldVo3);
				}else{
					lion.util.info("提示","请填写机构代码")
					return false;
				}
				
					var fieldVo4 = {};
					fieldVo4.name = "sysid";
					fieldVo4.value = "ORBPS";
					field.push(fieldVo4);
			}
	
			indexinfo.field = field;
			indexinfos.push(indexinfo);
			inputparams.indexinfos = indexinfos;
			lion.util.postjson("/orbps/web/cms/appurlrs/getUrl",inputparams,function(
					url, args) {
				// 跳转页面
				if (lion.util.isEmpty(url)) {
					lion.util.info("调用影像采集服务错误！");
					return;
				}
				window.open(url);
			});
		}
});


	//点击采集
	$("#btnCollection").click(function(){
		com.orbps.contractEntry.imageAudit.acquisition(10);
	});
	
	// 点击本次影像展现
	$("#btnImageShow").click(function(){
		com.orbps.contractEntry.imageAudit.acquisition(20);
	});
	
	// 点击影像批次流水号调阅
	$("#btnImageSerialNoSum").click(function(){
		com.orbps.contractEntry.imageAudit.acquisition(21);
	});
	//加载影像信息
	com.orbps.contractEntry.imageAudit.relodmessage = function(){
		var salesBranchNo ="";
		if(com.orbps.contractEntry.imageAudit.grpSalesListFormVos.length===1){
			salesBranchNo = com.orbps.contractEntry.imageAudit.grpSalesListFormVos[0].salesBranchNo;
		}else{
			for (var i = 0; i < com.orbps.contractEntry.imageAudit.grpSalesListFormVos.length; i++) {
				var jointFieldWorkFlag = com.orbps.contractEntry.imageAudit.grpSalesListFormVos[i].jointFieldWorkFlag;
				if(jointFieldWorkFlag==="Y"){
					salesBranchNo = com.orbps.contractEntry.imageAudit.grpSalesListFormVos[i].salesBranchNo;
					break;
				}
			}
		}
		if (salesBranchNo !== "") {
			salesBranchNo = salesBranchNo.substring(0, 2);
			salesBranchNo = salesBranchNo + "0000";
		}
		//管理机构代码
		$("#imageAuditForm #mngdeptid").val(salesBranchNo);
		//机构代码
		$("#imageAuditForm #batchid").val(salesBranchNo);
		//报价审批号
		$('#applNo').val(com.orbps.contractEntry.imageAudit.applNo);
		$("#imageAuditForm #tbdh").val(com.orbps.contractEntry.imageAudit.applNo);
		$("#imageAuditForm #quotaEaNoId").val(com.orbps.contractEntry.imageAudit.quotaEaNoId);
	}
	$("#btnsubmit").click(function(){
		var auditFlag = $('#auditFlag').val();
		var auditNotReason = $('#auditNotReason').val();
		if(auditFlag === "" ){
			lion.util.info("提示","请选择审批是否通过！");
			return false;
		}
		if(auditFlag === "N" ){
			if(auditNotReason === ""){
				lion.util.info("提示","请录入审批不通过原因！");
				return false;
			}
		}
		var imageAuditSubmitVo = {};
		imageAuditSubmitVo.applNo = $('#applNo').val();
		imageAuditSubmitVo.notice = auditNotReason;
		imageAuditSubmitVo.procFlag = auditFlag;
		imageAuditSubmitVo.taskId = com.orbps.contractEntry.imageAudit.taskId;
		 lion.util.postjson('/orbps/web/orbps/otherfunction/imageAudit/submit',imageAuditSubmitVo,function(data,arg){
			 if (data.retCode === "1") {
					lion.util.info("提示", "审批成功");
				} else {
					lion.util.info("提示", "审批失败，失败原因：" + data.errMsg);
				}
		 },null,null);
		
	});