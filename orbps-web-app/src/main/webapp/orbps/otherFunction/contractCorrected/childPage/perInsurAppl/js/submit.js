

//$(function(){
	
//	// 初始化校验函数
//	com.orbps.contractEntry.grpInsurAppl.printValidateForm(com.orbps.contractEntry.grpInsurAppl.printInfoForm);
//	alert("wwwwww");
//	// 表单提交
 $("#btnSubmit").click(function () {
	
////		// jquery validate 校验
//////		if(com.orbps.contractEntry.grpInsurAppl.applInfoForm.validate().form()
//////			||com.orbps.contractEntry.grpInsurAppl.vatInfoForm.validate().form()
//////				||com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm.validate().form()
//////					||com.orbps.contractEntry.grpInsurAppl.proposalInfoForm.validate().form()
//////						||com.orbps.contractEntry.grpInsurAppl.payInfoForm.validate().form()
//////							||com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.validate().form()
//////								||com.orbps.contractEntry.grpInsurAppl.printInfoForm.validate().form()){
//////									// select校验
//////									if(validateSelectVal()){
////										// 获取要约信息下面的所有险种信息
									var getAddRowsData = $("#fbp-editDataGrids").editDatagrids("getAddRowsData");
////										// 提交方法
										var perInsurApplVo = {};
										perInsurApplVo.addTaxInfoVo =com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addTaxInfoForm.serializeObject();					
										perInsurApplVo.applIlnfoVo = com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.perInsurAppl.serializeObject();
										//perInsurApplVo.beneficiaryInfoVo =com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.beneficiaryInfo.serializeObject();
										perInsurApplVo.offerInfoVo = com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.offerInfoForm.serializeObject();																					
										perInsurApplVo.beneficiaryInfoVo = com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insurInfoForm.serializeObject();										
										perInsurApplVo.insuredGroupModalVos = com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insuredList;										
										perInsurApplVo.organizaHierarModalVos = com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.oranLevelList;										
										perInsurApplVo.busiPrdVos = getAddRowsData;
										var responseVos = new Array();
							
										for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.benesList.length; i++) {
											alert(com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.benesList.length);
											var array_element = com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.benesList[i];
											alert(array_element);
											for (var j = 0; j < array_element.length; j++) {
												var array_elements = array_element[j];
												responseVos.push(array_elements);
											}											
										}
										perInsurApplVo.responseVos = responseVos;
										alert(JSON.stringify(perInsurApplVo));
										lion.util.postjson('/orbps/web/orbps/contractEntry/per/submit',perInsurApplVo,successSubmit,null,com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insurInfo);
////									}
////								}
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
	
	//被保人分组
	 $("#btnGoup").click(function() {
		 alert(1);
		  com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addDialog.empty();
		  alert(2);
		  com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addDialog.load("/orbps/orbps/otherFunction/contractCorrected/childPage/modal/html/insuredGroupModal.html",function(){
			  $(this).modal('toggle');
			  alert(3);
			  $(this).comboInitLoad();
			  $(this).combotreeInitLoad();
			// 刷新table
		  });
		  setTimeout(function(){
			  // 回显
			  reloadInsuredModalTable();
		  },100);
		 
	 });
	 
	 //组织层次架构 
	 $("#btnOranLevel").click(function() {
		 alert(4);
		com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addDialog.empty();
		alert(5);
		com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addDialog.load("/orbps/orbps/otherFunction/contractCorrected/childPage/modal/html/organizaHierarModal.html",function(){
			$(this).modal('toggle');
			// combo组件初始化
			$(this).comboInitLoad();
			$(this).combotreeInitLoad();
		});
		
//		setTimeout(function(){
//			// 回显
//			reloadOranLevelModalTable();
//		},100);
	 });
	
//});

// 添加成功回调函数
function successSubmit(obj,data,arg){
	lion.util.info("提示","提交团单录入信息成功");
	// 成功后刷新页面
	setTimeOut(function(){
		window.location.reload();
	},500);
}

// 查询成功回调函数
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
