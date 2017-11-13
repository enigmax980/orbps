//定义form
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.submitForm = $('#submitForm');

$(function() {	
	// 表单提交
	$("#submitForm #btnSubmit").click(function () {
		if(com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.applInfoForm.validate().form()
				&& com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.vatInfoForm.validate().form()
					&& com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.hldrInfoForm.validate().form()
						&& com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.ipsnInfoForm.validate().form()
							&& com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.arbitrationInfoForm.validate().form()
								&& com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.chargeInfoForm.validate().form()){
									if(validateSelectVal()){
										// 获取要约信息下面的所有险种信息
										var getAddRowsData = $("#polListTb").editDatagrids("getRowsData");
										// 提交方法
										var LoanAddApplVo = {};
										LoanAddApplVo.LoanAddApplInfoVo = com.orbps.contractEntry.grpInsurAppl.applInfoForm.serializeObject();
										LoanAddApplVo.LoanAddVatInfoVo = com.orbps.contractEntry.grpInsurAppl.vatInfoForm.serializeObject();
										LoanAddApplVo.LoanAddHldrInfoVo = com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm.serializeObject();
										LoanAddApplVo.LoanAddIpsnInfoVo = com.orbps.contractEntry.grpInsurAppl.proposalInfoForm.serializeObject();
										LoanAddApplVo.LoanAddBeneficiaryVo = com.orbps.contractEntry.grpInsurAppl.payInfoForm.serializeObject();
										LoanAddApplVo.LoanAddArbitrationInfoVo = com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
										LoanAddApplVo.LoanAddBsInfoVo = com.orbps.contractEntry.grpInsurAppl.printInfoForm.serializeObject();
										LoanAddApplVo.LoanAddChargeInfoVo = com.orbps.contractEntry.grpInsurAppl.printInfoForm.serializeObject();
										LoanAddApplVo.insuredGroupModalVos = com.orbps.contractEntry.modal.insuredList;
										LoanAddApplVo.organizaHierarModalVos = com.orbps.contractEntry.modal.oranLevelList;
										LoanAddApplVo.busiPrdVos = getAddRowsData;
										var responseVos = new Array();
										for (var i = 0; i < com.orbps.contractEntry.modal.benesList.length; i++) {
											var array_element = com.orbps.contractEntry.modal.benesList[i];
											for (var j = 0; j < array_element.length; j++) {
												var array_elements = array_element[j];
												responseVos.push(array_elements);
											}
										}
										grpInsurApplVo.responseVos = responseVos;
										alert(JSON.stringify(LoanAddApplVo));
										lion.util.postjson('/orbps/web/orbps/contractEntry/grp/submit',LoanAddApplVo,successSubmit,null,null);
									}
		}
	});
	
	// 通过投保单号查询险种信息
	$("#btnQuery").click(function() {
		// 取出投保单号
		var applNo = $("#applInfoForm #applNo").val();
		// 校验投保单号
		if(applNo!=null||"".equals(applNo)||applNo=="undefined"){
			var insurApplVo = {};
			insurApplVo.applInfoVo = com.orbps.contractEntry.grpInsurAppl.applInfoForm.serializeObject();
			alert(JSON.stringify(insurApplVo));
			lion.util.postjson('/orbps/web/orbps/contractEntry/grp/query',insurApplVo,successQuery,null,com.orbps.contractEntry.grpInsurAppl.applInfoForm);
		}else{
			lion.util.info('提示', '请输入正确投保单号');
		}
	});
 
	// 校验选择信息
		function validateSelectVal(){
			
			var applDate = $("#applInfoForm #applDate").val();
			if(applDate==null||""==applDate){
				lion.util.info("警告","请选择投保日期");
				return false;
			}
			var salesChannel = $("#applInfoForm #salesChannel").val();
			if(salesChannel==null||""==salesChannel){
				lion.util.info("警告","请选择销售渠道");
				return false;
			}
			var bankCode = $("#vatInfoForm #bankCode").val();
			if(bankCode==null||""==bankCode){
				lion.util.info("警告","请选择开户银行");
				return false;
			}
			var sex = $("#hldrInfoForm #sex").val();
			if(sex==null||""==sex){
				lion.util.info("警告","请选择投保人性别");
				return false;
			}
			var birthDate = $("#hldrInfoForm #birthDate").val();
			if(birthDate==null||""==birthDate){
				lion.util.info("警告","请选择投保人出生日期");
				return false;
			}
			var idType = $("#hldrInfoForm #idType").val();
			if(idType==null||""==idType){
				lion.util.info("警告","请选择投保人证件类型");
				return false;
			}
			var occupationalCodes = $("#hldrInfoForm #occupationalCodes").val();
			if(occupationalCodes==null||""==occupationalCodes){
				lion.util.info("警告","请输入投保人职业代码");
				return false;
			}
			var coverInfoFlag = $("#hldrInfoForm #coverInfoFlag").val();
			if(coverInfoFlag==null||""==coverInfoFlag){
				lion.util.info("警告","请选择是否覆盖以往投保人信息");
				return false;
			}
			var relToHldr = $("#ipsnInfoForm #relToHldr").val();
			if(relToHldr==null||""==relToHldr){
				lion.util.info("警告","请选择与投保人关系");
				return false;
			}
			var sex = $("#ipsnInfoForm #sex").val();
			if(sex==null||""==sex){
				lion.util.info("警告","请选择被保人性别");
				return false;
			}
			var birthDate = $("#ipsnInfoForm #birthDate").val();
			if(birthDate==null||""==birthDate){
				lion.util.info("警告","请选择被保人出生日期");
				return false;
			}
			var idType = $("#ipsnInfoForm #idType").val();
			if(idType==null||""==idType){
				lion.util.info("警告","请选择被保人证件类型");
				return false;
			}
			var occupationalCodes = $("#ipsnInfoForm #occupationalCodes").val();
			if(occupationalCodes==null||""==occupationalCodes){
				lion.util.info("警告","请输入被保人职业代码");
				return false;
			}
			var coverInfoFlag = $("#ipsnInfoForm #coverInfoFlag").val();
			if(coverInfoFlag==null||""==coverInfoFlag){
				lion.util.info("警告","请选择是否覆盖以往被保人信息");
				return false;
			}
			var cntrNature = $("#arbitrationInfoForm #cntrNature").val();
			if(cntrNature==null||""==cntrNature){
				lion.util.info("警告","请选择保单性质");
				return false;
			}
			var manualUwFlag = $("#arbitrationInfoForm #manualUwFlag").val();
			if(manualUwFlag==null||""==manualUwFlag){
				lion.util.info("警告","请选择是否人工核保");
				return false;
			}
			var disputeHandType = $("#arbitrationInfoForm #disputeHandType").val();
			if(disputeHandType==null||""==disputeHandType){
				lion.util.info("警告","请选择争议处理方式");
				return false;
			}
			var moneyinItrvl = $("#chargeInfoForm #moneyinItrvl").val();
			if(moneyinItrvl==null||""==moneyinItrvl){
				lion.util.info("警告","请选择交费方式");
				return false;
			}
			return true;
		}
	
});


//添加成功回调函数
function successSubmit(obj,data,arg){
lion.util.info("提示","提交团单录入信息成功");
// 成功后刷新页面
setTimeOut(function(){
	window.location.reload();
},500);
}

//查询成功回调函数
function successQuery(data,obj,arg){  
alert(JSON.stringify(data));
var applicant = data.applicantVo;
var insured = data.insuredVo;
insuredList = insured;
reloadInsuredTable();
var product = data.productsVos;
busiProdList = product;
reloadProductsTable();
setTimeout(function(){
	// 回显投保人
	var jsonStrApplicant = JSON.stringify(applicant);
	var objApplicant = eval("("+jsonStrApplicant+")");
	var key,value,tagName,type,arr;
	for(x in objApplicant){
		key = x;
		value = objApplicant[x];
		$("[name='"+key+"'],[name='"+key+"[]']").each(function(){
			tagName = $(this)[0].tagName;
			type = $(this).attr('type');
			
			if(tagName=='INPUT'){
				if(type=='radio'){
					$(this).attr('checked',$(this).val()==value);
				}else if(type=='checkbox'){
					arr = value.split(',');
					for(var i =0;i<arr.length;i++){
						if($(this).val()==arr[i]){
							$(this).attr('checked',true);
							break;
						}
					}
				}else{
					$("#applicantSaveForm #"+key).val(value);
				}
			}else if(tagName=='SELECT' || tagName=='TEXTAREA'){
				$("#applicantSaveForm #"+key).combo("val", value);
			}
			
		});
	}
	
	$("#applicantSaveForm input[type='text']").attr("readonly", true);
	$("#applicantSaveForm select").attr("readonly", true);
	$("#applicantSaveForm #appNo").attr("readonly", false);
	
	flag = 1;
	
},100);
}

