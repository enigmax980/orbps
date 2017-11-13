//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insurInfoBaseValidateForm= function (vform) {
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
					required : true,
					isIntGteZero :true
				},
				idTerm : {
					required : true
				},
				companyBankClass : {
					required : true
				},
				occupationType : {
					required : true
				},
				ptJobType:{
					required : true
				},
				preparePhone : {
					required : true
				},
				zipCode : {
					required : true,
					isZipCode :true
				},
				emergency : {
					required : true,
					isIdCardNo : true
				},
				mobilePhone : {
					required : true,
					isMobile: true
				},
				officePhone : {
					required : true,
					isTel : true
				},
				email : {
					required : true,
					isMobile :true
				},
				idType : {
					required : true
					
				},
				nationality: {
					required : true					
				},
				societyInsurance: {
					required : true					
				},
				supplementMedical: {
					required : true					
				},
				shiftPerson: {
					required : true					
				},
				posAddress: {
					required : true					
				},
				emergency: {
					required : true					
				},
				lanPhone:{
					required : true,
					isTel:true
				},
				emergencyPhone:{
					required : true,
					isMobile: true
				},
				paperType:{
					required : true			
			
				},
				workUnit:{
					required : true				
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
					required : "出生日期不能为空"
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
					isIntGteZero : "格式不正确"
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
				preparePhone:{
					required : "备用移动电话不能为空",
					isMobile : "请输入正确的电话号码"
				},
				emergency:{
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
				lanPhone :{
					required : "办公室电话不能为空",
					isTel : "请输入正确的固定电话"
				},
				workUnit :{
					required : "工作单位不能为空"
				}
			},
			
			
			invalidHandler: function (event, validator) {
	            Metronic.scrollTo(error2, -200);
	        },
	        
			errorPlacement:function(error,element){
				var icon = $(element).parent('.input-icon').children('i');
	            icon.removeClass('fa-check').addClass("fa-warning");
	            icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
		    },
		    
		    highlight: function (element) {
	            $(element).closest('.col-md-2').removeClass("has-success").addClass('has-error');
	        },
	        
	        unhighlight: function (element) {

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
	com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insurInfoBaseValidateForm(com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insurInfoForm);
});

$("#insuredInfo #customerNo").blur(function () {  
	var customerNo = $("#customerNo").val();
	if(customerNo==null||""==customerNo){
		lion.util.info("警告","客户号不能为空");
		return false;
	}
});

//当联系人证件类型改变的 时候，将五要素只读变成可写
$("#insuredInfo #paperType").change(function() {
	com.orbps.contractEntry.longInsuranceEntry.appConnIdTypeValue = $("#insurInfo #paperType").val();
	if("UW"!=com.orbps.contractEntry.grpInsurAppl.appConnIdTypeValue){
		$("#insurInfo #birthDate").attr("readonly", false);
		$("#insurInfo #sex").attr("readonly", false);
	}
});



$("#insuredInfo #salesChannel").blur(function () {  
	var salesChannel = $("#salesChannel").val();
	if(salesChannel==null||""==salesChannel){
		lion.util.info("警告","投保渠道不能为空");
		return false;
	}
}); 


$("#insuredInfo #lanPhone").blur(function () {  
	var lanPhone = $("#lanPhone").val();
	if(lanPhone==null||""==lanPhone){
		lion.util.info("警告","固定电话不能为空");
		return false;
	}
});

$("#insuredInfo #emergencyPhone").blur(function () {  
	var emergencyPhone = $("#emergencyPhone").val();
	if(emergencyPhone==null||""==emergencyPhone){
		lion.util.info("警告","紧急联系人电话不能为空");
		return false;
	}
});

$("#insuredInfo #salesBranchNo").blur(function () {  
	var salesBranchNo = $("#salesBranchNo").val();
	if(salesBranchNo==null||""==salesBranchNo){
		lion.util.info("警告","销售不能为空");
		return false;
	}
}); 


$("#insuredInfo #shiftPerson").blur(function () {  
	var shiftPerson = $("#shiftPerson").val();
	if(ashiftPerson==null||""==shiftPerson){
		lion.util.info("警告","转移不能为空");
		return false;
	}
});

$("#insuredInfo #Name").blur(function () {  
	var Name = $("#Name").val();
	if(Name==null||""==Name){
		lion.util.info("警告","姓名不能为空");
		return false;
	}
}); 

$("#insuredInfo #paperType").blur(function () {  
	var paperType = $("#paperType").val();
	if(paperType==null||""==paperType){
		lion.util.info("警告","证件类型不能为空");
		return false;
	}
}); 

$("#insuredInfo #age").blur(function () {  
	var age = $("#age").val();
	if(age==null||""==age){
		lion.util.info("警告","年龄不能为空");
		return false;
	}
}); 

$("#insuredInfo #idNo").blur(function () {  
	var idNo = $("#idNo").val();
	if(idNo==null||""==idNo){
		lion.util.info("警告","证件号码不能为空");
		return false;
	}
}); 

$("#insuredInfo #occupationType").blur(function () {  
	var occupationType = $("#occupationType").val();
	if(occupationType==null||""==occupationType){
		lion.util.info("警告","职业类别不能为空");
		return false;
	}
}); 

$("#insuredInfo #ptJobType").blur(function () {  
	var ptJobType = $("#ptJobType").val();
	if(ptJobType==null||""==ptJobType){
		lion.util.info("警告","兼职类别不能为空");
		return false;
	}
}); 

$("#insuredInfo #preparePhone").blur(function () {  
	var preparePhone = $("#preparePhone").val();
	if(preparePhone==null||""==preparePhone){
		lion.util.info("警告","备用电话不能为空");
		return false;
	}
}); 

$("#insuredInfo #workUnit").blur(function () {  
	var workUnit = $("#workUnit").val();
	if(workUnit==null||""==workUnit){
		lion.util.info("警告","工作单位不能为空");
		return false;
	}
}); 


$("#insuredInfo #zipCode").blur(function () {  
	var zipCode = $("#zipCode").val();
	if(zipCode==null||""==zipCode){
		lion.util.info("警告","邮编不能为空");
		return false;
	}
}); 

$("#insuredInfo #emergency").blur(function () {  
	var emergency = $("#emergency").val();
	if(emergency==null||""==emergency){
		lion.util.info("警告","紧急联系人不能为空");
		return false;
	}
}); 

$("#insuredInfo #posAddress").blur(function () {  
	var posAddress = $("#posAddress").val();
	if(posAddress==null||""==posAddress){
		lion.util.info("警告","通讯地址不能为空");
		return false;
	}
}); 

$("#insuredInfo #email").blur(function () {  
	var email = $("#email").val();
	if(email==null||""==email){
		lion.util.info("警告","电子邮箱不能为空");
		return false;
	}
}); 

$("#insuredInfo #mobilePhone").blur(function () {  
	var mobilePhone = $("#mobilePhone").val();
	if(mobilePhone==null||""==mobilePhone){
		lion.util.info("警告","移动电话不能为空");
		return false;
	}
}); 

$("#insuredInfo #appHomeFax").blur(function () {  
	var appHomeFax = $("#appHomeFax").val();
	if(appHomeFax==null||""==appHomeFax){
		lion.util.info("警告","传真号码不能为空");
		return false;
	}
}); 

$("#insuredInfo #supplementMedical").blur(function () {  
	var supplementMedical = $("#supplementMedical").val();
	if(supplementMedical==null||""==supplementMedical){
		lion.util.info("警告","补充医疗不能为空");
		return false;
	}
}); 
$("#insuredInfo #societyInsurance").blur(function () {  
	var societyInsurance = $("#societyInsurance").val();
	if(societyInsurance==null||""==societyInsurance){
		lion.util.info("警告","社会保险不能为空");
		return false;
	}
}); 

$("#insuredInfo #officePhone").blur(function () {  
	var officePhone = $("#officePhone").val();
	if(officePhone==null||""==officePhone){
		lion.util.info("警告","办公室电话不能为空");
		return false;
	}
}); 
