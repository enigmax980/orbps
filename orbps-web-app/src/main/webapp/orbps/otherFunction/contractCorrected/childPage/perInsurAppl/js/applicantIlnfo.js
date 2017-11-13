// 新建contractEntry命名空间
com.orbps.otherFunction.contractCorrected.childPage={};
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl={};
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.perInsurAppl=$("#applicantIlnfo");
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addTaxInfoForm=$("#addedTaxInfo");
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.beneficiaryInfo=$("#beneficiaryInfo");
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insurInfoForm=$("#insuredInfo");
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.offerInfoForm=$("#offerInfo");

//编辑或添加对话框
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.benesList =[];
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insuredList = new Array();
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.oranLevelList = new Array();
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addDialog = $('#btnModel');
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.insuredList = new Array();
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.oranLevelList = new Array();
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.grpInsurApplList = new Array();


//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.handleForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		messages : {
			occupationType :{
				required : '请输入投保单号'				
			},			
			age: {
				required : '请输入投保单号',
				isIntGteZero:"格式不正确"
			},
			applNo : {
				required : '请输入投保单号',
				isIntGteZero:"格式不正确"
			},
			applDate : {
				required : '请选择投保日期'
			},
			salesChannel : {
				required : "请输入销售渠道"
			},
			salesBranchNo : {
				required : "请输入销售机构",
				isIntGteZero : "请输入大于0 的数字"
			},
			salesPersonNo : {
				required : "请输入销售员号"
			},
			customerNo: {
				required : "请输入客户号"
			},
			Name: {
				required : "请输入姓名"
			},
			sex: {
				required : "请输入性别"
			},
			idNo: {
				required : "请输入证件号码",
				isIdCardNo:"请输入正确证件号码"
			},
			idTerm: {
				required : "请输入证件有效期"
			},
			zipCode: {
				required : "请输入邮编",
				isZipCode:"格式不正确"
			},
			posAddress: {
				required : "请输入通讯地址"
			},
			mobilePhone: {
				required : "请输入移动电话",
				isMobile : "格式不正确"
			},
			lanPhone: {
				required : "请输入固定电话",
				isTel : "格式不正确"
			},
			email: {
				required : "请输入电子邮箱",
				email : "请输入正确格式的邮箱"
			}
			
		},
		rules : {
			occupationType:{
				required : true
			},
			applNo : {
				required : true,
				isIntGteZero:true
			},
			applDate : {
				required : true
			},
			salesChannel : {
				required : true
			},
			salesBranchNo : {
				required : true,
				isIntGteZero :true
			},
			salesPersonNo : {
				required : true
			},
			customerNo: {
				required : true
			},
			Name: {
				required : true
			},
			age: {
				required : true,
				isIntGteZero : true
			},
			idNo: {
				required : true,
				isIdCardNo : true
			},
			idTerm: {
				required : true
			},
			companyBankClass: {
				required : true
			},
			zipCode: {
				required : true,
				isZipCode:true
			},
			posAddress: {
				required : true
			},
			mobilePhone: {
				required : true,
				isMobile :true
			},
			lanPhone: {
				required : true,
				isIntGteZero :true
			},
			email: {
				required : true,
				email : true
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

$(function(){
	com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.handleForm(com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.perInsurAppl);	
	 
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	// 初始化edittable组件
	$("#fbp-editDataGrid-one").editDatagridsLoadById();
	// combo组件初始化
	$("*").comboInitLoad();
	
	//身份证号验证
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {
		com.orbps.contractEntry.grpInsurAppl.appIdTypeValue = $("#applicantIlnfo #idType").val();
		// 先判断证件类型是否为身份证再去校验,appIdTypeValue(证件类型的值)再各自需要的js里设置为全局变量,
		if(com.orbps.contractEntry.grpInsurAppl.appIdTypeValue=="UW"){
			return this.optional(element) || idCardNoUtil.checkIdCardNo(value);     
		}else{
			return true;
		}
	}, "请正确输入您的身份证号码！");  
});


$("#email").blur(function () {  
	var email = $("#email").val();
	if(email==null||""==email){
		lion.util.info("警告","电子邮箱不能为空");
		return false;
	}
});

$("#lanPhone").blur(function () {  
	var lanPhone = $("#lanPhone").val();
	if(lanPhone==null||""==lanPhone){
		lion.util.info("警告","固定电话不能为空");
		return false;
	}
});	

$("#mobilePhone").blur(function () {  
	var mobilePhone = $("#mobilePhone").val();
	if(mobilePhone==null||""==mobilePhone){
		lion.util.info("警告","移动电话不能为空");
		return false;
	}
});	

$("#posAddress").blur(function () {  
	var posAddress = $("#posAddress").val();
	if(posAddress==null||""==posAddress){
		lion.util.info("警告","通讯地址不能为空");
		return false;
	}
});

$("#zipCode").blur(function () {  
	var zipCode = $("#zipCode").val();
	if(zipCode==null||""==zipCode){
		lion.util.info("警告","邮编不能为空");
		return false;
	}
});

$("#occupationType").blur(function () {  
	var occupationType = $("#occupationType").val();
	if(occupationType==null||""==occupationType){
		lion.util.info("警告","职业类别不能为空");
		return false;
	}
});

$("#idNo").blur(function () {  
	var idNo = $("#idNo").val();
	if(idNo==null||""==idNo){
		lion.util.info("警告","证件号码不能为空");
		return false;
	}
});

$("#sex").blur(function () {  
	var sex = $("#sex").val();
	if(sex==null||""==sex){
		lion.util.info("警告","性别不能为空");
		return false;
	}
});

$("#applNo").blur(function () {  
	var applNo = $("#applNo").val();
	if(applNo==null||""==applNo){
		lion.util.info("警告","投保单号不能为空");
		return false;
	}
}); 

$("#age").blur(function () {  
	var age = $("#age").val();
	if(age==null||""==age){
		lion.util.info("警告","年龄不能为空");
		return false;
	}
});

$("#Name").blur(function () {  
	var Name = $("#Name").val();
	if(Name==null||""==Name){
		lion.util.info("警告","名称不能为空");
		return false;
	}
}); 

$("#customerNo").blur(function () {  
	var customerNo = $("#customerNo").val();
	if(customerNo==null||""==customerNo){
		lion.util.info("警告","客户号不能为空");
		return false;
	}
}); 

$("#salesChannel").blur(function () {  
	var salesChannel = $("#salesChannel").val();
	if(salesChannel==null||""==salesChannel){
		lion.util.info("警告","销售渠道不能为空");
		return false;
	}
}); 

$("#salesBranchNo").blur(function () {  
	var salesBranchNo = $("#salesBranchNo").val();
	if(salesBranchNo==null||""==salesBranchNo){
		lion.util.info("警告","销售机构号不能为空");
		return false;
	}
}); 

$("#salesPersonNo").blur(function () {  
	var salesPersonNo = $("#salesPersonNo").val();
	if(salesPersonNo==null||""==salesPersonNo){
		lion.util.info("警告","销售员号不能为空");
		return false;
	}
});

