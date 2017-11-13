com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl={};
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseInfoForm=$("#applBaseInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.appIdTypeValue;
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.appConnIdTypeValue;
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acount = 0;
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acounts = 0;

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseValidateForm= function (vform) {
	    var error2 = $('.alert-danger', vform);
	    var success2 = $('.alert-success', vform);
	    vform.validate({
			errorElement: 'span',
	        errorClass: 'help-block help-block-error', 
	        focusInvalid: false, 
	        onkeyup:false,
	        ignore: '', 
			rules : {
				companyName : {
					required : true
				},
				idNo : {
					required : true
				},
				applNum : {
					required : true
				},
				numOfEmp : {
					required : true
				},
				ojEmpNum : {
					required : true
				},
				appAddrProv : {
					required : true
				},
				appAddrCity : {
					required : true
				},
				appAddrCountry : {
					required : true
				},
				appAddrHome : {
					required : true
				},
				appPost : {
					required : true,
					isZipCode : true
				},
				connName : {
					required : true
				},
				connIdNo : {
					required : true,
					isIdCardNo : true
				},
				connPhone : {
					isMobile : true
				},
				connPostcode : {
					email : true
				}
			},
			messages : {
				companyName : {
					required : "单位/团体名称不能为空"
				},
				idNo : {
					required : "证件号码不能为空"
				},
				applNum : {
					required : "投保人数不能为空"
				},
				numOfEmp : {
					required : "企业员工总数不能为空"
				},
				ojEmpNum : {
					required : "在职人数不能为空"
				},
				appAddrProv : {
					required : "省/直辖市不能为空"
				},
				appAddrCity : {
					required : "市/城区不能为空"
				},
				appAddrCountry : {
					required : "县/地级市不能为空"
				},
				appAddrHome : {
					required : "详细地址不能为空"
				},
				appPost : {
					required : "邮编不能为空",
					isZipCode : "请输入正确的邮编"
				},
				connName : {
					required : "联系人姓名不能为空"
				},
				connIdNo : {
					required : "联系人证件号码不能为空",
					isIdCardNo : "请输入正确的身份证号"
				},
				connPhone : {
					isMobile : "请输入正确的手机号码"
				},
				connPostcode : {
					email : "请输入正确格式的邮箱"
				}
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
	        
	        success: function (label, element) {
	        	var icon = $(element).parent('.input-icon').children('i');
	            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
	            icon.removeClass("fa-warning").addClass("fa-check");
	        }
		});
	}


$(function() {
	
	$("#citySelect").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
//        prov:"北京",
//        city:"北京",
//        town:"朝阳区",
        /* nodata:"none", */
	    required:true
    });
	
	// 校验函数初始化
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseValidateForm(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseInfoForm);

	//身份证号验证
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {
		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.appIdTypeValue = $("#applBaseInfoForm #connIdType").val();
		// 先判断证件类型是否为身份证再去校验,appIdTypeValue(证件类型的值)再各自需要的js里设置为全局变量,
		if(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.appIdTypeValue==="I"){
			return this.optional(element) || idCardNoUtil.checkIdCardNo(value);     
		}else{
			return true;
		}
	}, "请正确输入您的身份证号码！");
	
	// 显示回退按钮
	if (com.orbps.otherFunction.contractCorrected.parentPage.backFlag === "Y") {
		$("#submit #hideKong").hide();
		$("#submit #btnSubmitDiv").hide();
	    $("#submit #selectDiv").show();
	    $("#submit #btnBackDiv").show();
	    $("#submit #backReasonDiv").show();
	} else {
	    $("#submit #selectDiv").hide();
	    $("#submit #btnBackDiv").hide();
	    $("#submit #backReasonDiv").hide();
	    $("#submit #hideKong").show();
	    $("#submit #btnSubmitDiv").show();
	}
    //根据争议处理方式调整页面样式
    $("#disputePorcWay").change(function() {
        if($("#disputePorcWay").val()==="1"){
            $("#arbOrgName").attr("disabled",true);
            $("#arbOrgName").val("");
        }else{
            $("#arbOrgName").attr("disabled",false);
        }
    });
    
    $("#arbOrgName").blur(function() {
    	var arbOrgName = $("#arbOrgName").val();
    	if (arbOrgName === null || "" === arbOrgName) {
    		lion.util.info("警告", "仲裁机构名称不能为空");
    		return false;
    	}
    	// 字符间只能有一个空格
    	arbOrgName = arbOrgName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
    	$("#arbOrgName").val(arbOrgName);
    });
	
});

//由于后台没有保存联系人性别、出生日期，注掉联系人身份证号自动带出信息功能，误删除
//当联系人证件类型改变的 时候，将五要素只读变成可写
$("#applBaseInfoForm #connIdType").change(function() {
	if(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acount === 0 || com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acount === 1){
		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acount++;
		return false;
	}
    $("#applBaseInfoForm #connIdNofa").removeClass("fa-warning");
    $("#applBaseInfoForm #connIdNofa").removeClass("fa-check");
    $("#applBaseInfoForm #connIdNo").val("");
});
$("#applBaseInfoForm #idType").change(function() {
	//页面回显需要两次change
	if(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acounts === 0 || com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acounts === 1){
		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.acounts++;
		return false;
	}
    $("#applBaseInfoForm #idNofa").removeClass("fa-warning");
    $("#applBaseInfoForm #idNofa").removeClass("fa-check");
    $("#applBaseInfoForm #idNo").val("");
	
});
$("#companyName").blur(function () {  
	var companyName = $("#companyName").val();
	if(companyName==null||""==companyName){
		lion.util.info("警告","单位/团体名称不能为空");
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

$("#applNum").blur(function () {  
	var applNum = $("#applNum").val();
	if(applNum==null||""==applNum){
		lion.util.info("警告","投保人数不能为空");
		return false;
	}
}); 

$("#appAddrProv").blur(function () {  
	var appAddrProv = $("#appAddrProv").val();
	if(appAddrProv==null||""==appAddrProv){
		lion.util.info("警告","省/直辖市不能为空");
		return false;
	}
}); 

$("#appAddrCity").blur(function () {  
	var appAddrCity = $("#appAddrCity").val();
	if(appAddrCity==null||""==appAddrCity){
		lion.util.info("警告","市/城区不能为空");
		return false;
	}
}); 

$("#appAddrCountry").blur(function () {  
	var appAddrCountry = $("#appAddrCountry").val();
	if(appAddrCountry==null||""==appAddrCountry){
		lion.util.info("警告","县/地级市不能为空");
		return false;
	}
}); 

$("#appPost").blur(function () {  
	var appPost = $("#appPost").val();
	if(appPost==null||""==appPost){
		lion.util.info("警告","邮编不能为空");
		return false;
	}
}); 

$("#connName").blur(function () {  
	var connName = $("#connName").val();
	if(connName==null||""==connName){
		lion.util.info("警告","联系人姓名不能为空");
		return false;
	}
}); 


$("#connIdNo").blur(function () {  
	var connIdNo = $("#connIdNo").val();
	if(connIdNo==null||""==connIdNo){
		lion.util.info("警告","联系人证件号码不能为空");
		return false;
	}
}); 
//联系人固定电话、移动电话不能同时为空
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.isPhoneOrTel = function(){
	var phone = $("#applBaseInfoForm #connPhone").val();
	var tel =  $("#applBaseInfoForm #appHomeTel").val();
	if((""!==phone && null!==phone) || (""!==tel && null!==tel)){
		return true;
	}else{
		lion.util.info("警告", "联系人移动电话和联系人固定电话不能同时为空！");
		$(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
		return false;
	}
};
//丢失焦点校验联系人移动电话和固定电话是否同时为空
$("#applBaseInfoForm #connPhone").blur(function(){
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.isPhoneOrTel();
});
$("#applBaseInfoForm #appHomeTel").blur(function(){
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.isPhoneOrTel();
});



