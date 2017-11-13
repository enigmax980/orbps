
//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.insurInfoBaseValidateForm= function (vform) {
	    var error2 = $('.alert-danger', vform);
	    var success2 = $('.alert-success', vform);
	    vform.validate({
			errorElement: 'span',
	        errorClass: 'help-block help-block-error', 
	        focusInvalid: false, 
	        onkeyup:false,
	        ignore: '', 
			rules : {  
				relToHldr : {
					required : true
				},
				salesBranchNo : {
					required : true
				},
				customerNo : {
					required : true,
					isIntGteZero:true
				},
				Name : {
					required : true
				},
				partTimeCode: {
					required : true
				},
				sex : {
					required : true
				},
				birthDate : {
					required : true,					
				},
				age : {
					required : true,
					isIntGteZero : true
				},
				marType : {
					required : true
				},
				idNo : {
					required : true
				},
				idTerm : {
					required : true
				},
				companyBankClass : {
					required : true
				},
				occupationType : {
					required : true,
				},
				ptJobType:{
					required : true,
				},
				preparePhone : {
					required : true,
				},
				zipCode : {
					required : true,
					isZipCode :true
				},
				emergencyPhone : {
					required : true,
					isIdCardNo : true
				},
				mobilePhone : {
					required : true,
					isMobile: true
				},
				officePhone : {
					required : true,
					isTel : "请输入正确的固定电话"
				},
				email : {
					required : true,
					isMobile :true
				},
				idType : {
					required : true,
					
				},
				nationality: {
					required : true,
					
				},
				societyInsurance: {
					required : true,
					
				},
				supplementMedical: {
					required : true,					
				},
				shiftPerson: {
					required : true,					
				},
				posAddress: {
					required : true,					
				},
				emergencyPeople: {
					required : true,					
				},
				
			},
			messages : {
				relToHldr : {
					required : "请选择与投保人关系"
				},
				zipCode :{
					required : "邮箱不能为空",
					isZipCode : "请输入正确邮箱格式"
				},
				customerNo : {
					required : "客户号不能为空",
					isIntGteZero:"请输入大于0的数字"
				},				
				Name : {
					required : "姓名不能为空"
				},
				sex : {
					required : "请选性别"
				},
				birthDate : {
					required : "出生日期不能为空",
				},
				age : {
					required : "年龄不能为空",
					isIntGteZero : "请输入>=0的整数"
				},
				marType:{
					required : "请选择婚姻状况",					
				},	
				paperType:{
					required : "请选择证件类型",					
				},
				idNo : {
					required : "证件号码不能为空",
					isIdCardNo : "请您核对并输入联系人正确的证件号码"
				},
				idTerm : {
					required : "请选择证件有效期"
				},
				occupationType : {
					required : "职业类别不能为空"
				},
				companyBankCode : {
					required : "请选择职业代码"
				},
				nationality : {
					required : "请选择国籍"
				},
				ptJobType:{
					required : "兼职类别不能为空"
				},
				partTimeCode:{
					required : "兼职代码不能为空"
				},
				preparePhone:{
					required : "备用移动电话不能为空",
					isMobile : "请输入正确的电话号码"
				},
				emergencyPeople:{
					required : "紧急联系人不能为空"
				},
				emergencyPhone:{
					required : "紧急联系人电话不能为空",
					isMobile : "请输入正确的电话号码"
				},
				posAddress:{
					required : "通讯地址不能为空"
				},
				mobilePhone:{
					required : "移动电话不能为空",
					isMobile : "请输入正确的电话号码"
				},
				email: {
					required : "电子邮箱不能为空"	,
					email : "请输入正确格式的邮箱"
				},
				shiftPerson: {
					required : "请选择转移人员"					
				},
				supplementMedical: {
					required : "请选择补充医疗"					
				},				
				societyInsurance : {
					required : "请选择社会保险/公费医疗"
				},				
				officePhone : {
					required : "固定电话不能为空",
					isTel : "请输入正确的固定电话"
				},
			},
			invalidHandler: function (event, validator) {
	            Metronic.scrollTo(error2, -200);
	        },
	        
			errorPlacement:function(error,element){
				var icon = $(element).parent('.input-icon').children('i');
	            icon.removeClass('fa-check').addClass("fa-warning");
	            if (icon.attr('title') || typeof icon.attr('data-original-title') != 'string') {
	            	icon.attr('data-original-title', icon.attr('title') || '').attr('title', error.text())
	            }
	        },
		    
		    highlight: function (element) {
	            $(element).closest('.col-md-2').removeClass("has-success").addClass('has-error');
	        },
	        
	        submitHandler: function (form) {
	            success2.show();
	            error2.hide();
	            form[0].submit();
	        },
	        
	        success: function (label, element) {
	        	var icon = $(element).parent('.input-icon').children('i');
	            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
	            icon.removeClass("fa-warning").addClass("fa-check");
	        }
		});
	}


$(function() {	
	// 校验函数初始化
	com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.insurInfoBaseValidateForm(com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.insurInfoForm);
	
});

$("#insurInfo #customerNo").blur(function () {  
	var customerNo = $("#customerNo").val();
	if(customerNo==null||""==customerNo){
		lion.util.info("警告","顾客号不能为空");
		return false;
	}
});

//当联系人证件类型改变的 时候，将五要素只读变成可写
$("#insurInfo #paperType").change(function() {
	com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.appConnIdTypeValue = $("#insurInfo #paperType").val();
	if("UW"!=com.orbps.contractEntry.grpInsurAppl.appConnIdTypeValue){
		$("#insurInfo #birthDate").attr("readonly", false);
		$("#insurInfo #sex").attr("readonly", false);
	}
});

$("#insurInfo #recognizeeRelation").blur(function () {  
	var recognizeeRelation = $("#recognizeeRelation").val();
	if(recognizeeRelation==null||""==recognizeeRelation){
		lion.util.info("警告","投保单号不能为空");
		return false;
	}
}); 

$("#insurInfo #salesChannel").blur(function () {  
	var salesChannel = $("#salesChannel").val();
	if(salesChannel==null||""==salesChannel){
		lion.util.info("警告","投保渠道不能为空");
		return false;
	}
}); 

$("#insurInfo #salesBranchNo").blur(function () {  
	var salesBranchNo = $("#salesBranchNo").val();
	if(salesBranchNo==null||""==salesBranchNo){
		lion.util.info("警告","销售不能为空");
		return false;
	}
}); 


$("#insurInfo #shiftPerson").blur(function () {  
	var shiftPerson = $("#shiftPerson").val();
	if(ashiftPerson==null||""==shiftPerson){
		lion.util.info("警告","转移不能为空");
		return false;
	}
});

$("#insurInfo #Name").blur(function () {  
	var Name = $("#Name").val();
	if(Name==null||""==Name){
		lion.util.info("警告","姓名不能为空");
		return false;
	}
}); 

$("#insurInfo #paperType").blur(function () {  
	var paperType = $("#paperType").val();
	if(paperType==null||""==paperType){
		lion.util.info("警告","证件类型不能为空");
		return false;
	}
}); 

$("#insurInfo #age").blur(function () {  
	var age = $("#age").val();
	if(age==null||""==age){
		lion.util.info("警告","年龄不能为空");
		return false;
	}
}); 

$("#insurInfo #idNo").blur(function () {  
	var idNo = $("#idNo").val();
	if(idNo==null||""==idNo){
		lion.util.info("警告","证件号码不能为空");
		return false;
	}
}); 

$("#insurInfo #occupationType").blur(function () {  
	var occupationType = $("#occupationType").val();
	if(occupationType==null||""==occupationType){
		lion.util.info("警告","职业类别不能为空");
		return false;
	}
}); 

$("#insurInfo #ptJobType").blur(function () {  
	var ptJobType = $("#ptJobType").val();
	if(ptJobType==null||""==ptJobType){
		lion.util.info("警告","兼职类别不能为空");
		return false;
	}
}); 

$("#insurInfo #preparePhone").blur(function () {  
	var preparePhone = $("#preparePhone").val();
	if(preparePhone==null||""==preparePhone){
		lion.util.info("警告","备用电话不能为空");
		return false;
	}
}); 

$("#insurInfo #workUnit").blur(function () {  
	var workUnit = $("#workUnit").val();
	if(workUnit==null||""==workUnit){
		lion.util.info("警告","工作单位不能为空");
		return false;
	}
}); 


$("#insurInfo #zipCode").blur(function () {  
	var zipCode = $("#zipCode").val();
	if(zipCode==null||""==zipCode){
		lion.util.info("警告","邮编不能为空");
		return false;
	}
}); 

$("#insurInfo #emergencyPeople").blur(function () {  
	var emergencyPeople = $("#emergencyPeople").val();
	if(emergencyPeople==null||""==emergencyPeople){
		lion.util.info("警告","紧急联系人不能为空");
		return false;
	}
}); 

$("#insurInfo #posAddress").blur(function () {  
	var posAddress = $("#posAddress").val();
	if(posAddress==null||""==posAddress){
		lion.util.info("警告","通讯地址不能为空");
		return false;
	}
}); 

$("#insurInfo #email").blur(function () {  
	var email = $("#email").val();
	if(email==null||""==email){
		lion.util.info("警告","电子邮箱不能为空");
		return false;
	}
}); 
$("#insurInfo #mobilePhone").blur(function () {  
	var mobilePhone = $("#mobilePhone").val();
	if(mobilePhone==null||""==mobilePhone){
		lion.util.info("警告","移动电话不能为空");
		return false;
	}
}); 
$("#insurInfo #appHomeFax").blur(function () {  
	var appHomeFax = $("#appHomeFax").val();
	if(appHomeFax==null||""==appHomeFax){
		lion.util.info("警告","传真号码不能为空");
		return false;
	}
}); 
$("#insurInfo #supplementMedical").blur(function () {  
	var supplementMedical = $("#supplementMedical").val();
	if(supplementMedical==null||""==supplementMedical){
		lion.util.info("警告","补充医疗不能为空");
		return false;
	}
}); 
$("#insurInfo #societyInsurance").blur(function () {  
	var societyInsurance = $("#societyInsurance").val();
	if(societyInsurance==null||""==societyInsurance){
		lion.util.info("警告","社会保险不能为空");
		return false;
	}
}); 
$("#insurInfo #officePhone").blur(function () {  
	var officePhone = $("#officePhone").val();
	if(officePhone==null||""==officePhone){
		lion.util.info("警告","办公室电话不能为空");
		return false;
	}
}); 
