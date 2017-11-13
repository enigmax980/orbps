com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm=$("#specialInsurAddInfoForm");

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddValidateForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	$('#specialInsurAddInfoForm').validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
            projectName : {
                required : true
            },
            projectAddr : {
                required : true
            },
            totalCost : {
                required : true,
                isFloatGteZero : true
            },
            totalArea : {
                required : true,
                isFloatGteZero : true
            },
            floorHeight: {
            	required : true,
            	isFloatGteZero : true
            },
            diseaDeathNums: {
            	required : true,
            	isIntNum: true
            },
            diseaDisableNums: {
            	required : true,
            	isIntNum: true
            },
            acdntDeathNums: {
            	required : true,
            	isIntNum: true
            },
            acdntDisableNums: {
            	required : true,
            	isIntNum: true
            }
        },
		messages : {
            projectName : {
                required : "工程名称不能为空"
            },
            projectAddr : {
                required : "工程地址不能为空"
            },
            totalCost : {
                required : "总造价不能为空",
                isFloatGteZero : '请输入>=0的总造价'
            },
            totalArea : {
                required : "总面积不能为空",
                isFloatGteZero : '请输入>=0的总面积'
            },
            floorHeight: {
            	required : "层高不能为空",
            	isFloatGteZero : "请输入>=0的层高米数"
            },
            diseaDeathNums: {
            	required : "疾病死亡人数不能为空",
            	isIntNum: "请输入>=0的疾病死亡人数"
            },
            diseaDisableNums: {
            	required : "疾病伤残人数不能为空",
            	isIntNum: "请输入>=0的疾病伤残人数"
            },
            acdntDeathNums: {
            	required :"意外死亡人数不能为空",
            	isIntNum: "请输入>=0的意外死亡人数"
            },
            acdntDisableNums: {
            	required :"意外伤残人数不能为空",
            	isIntNum: "请输入>=0的意外伤残人数"
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
	// 初始化校验信息
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl
			.specialInsurAddValidateForm(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm);
 // 设置总天数只读
    $("#totalDays").attr("readOnly",true);
    
    // 丢失焦点计算施工总天数
    $("#constructionDur").change(function() {
        $("#totalDays").val("");
        var constructionDur = $("#constructionDur").val();
        var until = $("#until").val();
        if(constructionDur!=="" && until!==""){
        	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.DateDiff(constructionDur,until);
        }
    });
    // 丢失焦点计算施工总天数
    $("#until").change(function() {
        $("#totalDays").val("");
        var constructionDur = $("#constructionDur").val();
        var until = $("#until").val();
        if(constructionDur!=="" && until!==""){
        	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.DateDiff(constructionDur,until);
        }
    });
 // 计算天数差的函数
    com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.DateDiff = function(sDate1, sDate2) { // sDate1和sDate2是2006-12-18格式
    	var Date1 = new Date(sDate1);
    	var Date2 = new Date(sDate2);
    	var a = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000) + 1;
    	$("#totalDays").val(a);
    }
 // "根据保费收取方式确定是否必录：
 // 1、按面积计算，总面积为必录，总造价为非必录
 // 2、按造价计算，总造价为必录，总面积为非必录
 // 3、按人数计算，总面积、总造价为非必录"
 $("#specialInsurAddInfoForm #cpnstMioType").change(function() {
 	var cpnstMioType = $("#specialInsurAddInfoForm #cpnstMioType").val();
 	if (cpnstMioType === "S") {
 		// 展示是否必录的红* 标识
 		$("#specialInsurAddInfoForm #totalCostIs").hide();
 		$("#specialInsurAddInfoForm #totalAreaIs").show();
 	} else if (cpnstMioType === "B") {
 		$("#specialInsurAddInfoForm #totalCostIs").show();
 		// 展示是否必录的红* 标识
 		$("#specialInsurAddInfoForm #totalAreaIs").hide();
 	} else {
 		$("#specialInsurAddInfoForm #totalCostIs").hide();
 		$("#specialInsurAddInfoForm #totalAreaIs").hide();
 	}
 });
//按面积计算，总面积为必录，总造价为非必录
 $("#specialInsurAddInfoForm #totalArea").blur(function() {
 	var cpnstMioType = $("#specialInsurAddInfoForm #cpnstMioType").val();
 	if ("S" === cpnstMioType) {
 		var totalArea = $("#specialInsurAddInfoForm #totalArea").val();
 		if (totalArea === "") {
 			lion.util.info("提示", "总面积必录");
 		}
 	}
 });

 // 按造价计算，总造价为必录，总面积为非必录
 $("#specialInsurAddInfoForm #totalCost").blur(function() {
 	var cpnstMioType = $("#specialInsurAddInfoForm #cpnstMioType").val();
 	if ("B" === cpnstMioType) {
 		var totalCost = $("#specialInsurAddInfoForm #totalCost").val();
 		if (totalCost === "") {
 			lion.util.info("提示", "总造价必录");
 		}
 	}
 });
 $("#projectAddr").blur(function() {
		var projectAddr = $("#projectAddr").val();
		if (projectAddr === null || "" === projectAddr) {
			lion.util.info("警告", "工程地址不能为空");
			return false;
		}
	});

	$("#diseaDeathNums").blur(function() {
		var diseaDeathNums = $("#diseaDeathNums").val();
		if (diseaDeathNums === null || "" === diseaDeathNums) {
			lion.util.info("警告", "疾病死亡人数不能为空");
			return false;
		}
	});
	$("#diseaDisableNums").blur(function() {
		var diseaDisableNums = $("#diseaDisableNums").val();
		if (diseaDisableNums === null || "" === diseaDisableNums) {
			lion.util.info("警告", "疾病伤残人数不能为空");
			return false;
		}
	});
	$("#acdntDeathNums").blur(function() {
		var acdntDeathNums = $("#acdntDeathNums").val();
		if (acdntDeathNums === null || "" === acdntDeathNums) {
			lion.util.info("警告", "意外死亡人数不能为空");
			return false;
		}
	});
	$("#acdntDisableNums").blur(function() {
		var acdntDisableNums = $("#acdntDisableNums").val();
		if (acdntDisableNums === null || "" === acdntDisableNums) {
			lion.util.info("警告", "意外伤残人数不能为空");
			return false;
		}
	});
	
});
//层高校验规则
$("#specialInsurAddInfoForm #floorHeight").on(
		'keyup',
		function(event) {
			if(commevent(this)){
				return;
			}
			if (!isNaN(this.value)) {
				if (this.value >= 100) {
					this.value = ''
				}
				this.value = /^\d+\.?\d{0,2}$/.test(this.value) ? this.value
						: this.value.substring(0, this.value.length - 1)
			} else {
				this.value = ''
			}
			;
			$("#specialInsurAddInfoForm #floorHeight").val(this.value);
		});

// 层高校验规则(防止输入0.就给清空的现象，所以丢失焦点在进行校验)
$("#specialInsurAddInfoForm #floorHeight").blur(
		function(event) {
			if (!isNaN(this.value)) {
				if (this.value <= 0 || this.value >= 100) {
					this.value = ''
				}
				this.value = /^\d+\.?\d{0,2}$/.test(this.value) ? this.value
						: this.value.substring(0, this.value.length - 1)
			} else {
				this.value = ''
			}
			;
			$("#specialInsurAddInfoForm #floorHeight").val(this.value);
		});
//公共保额使用范围change事件
$("#specialInsurAddInfoForm #comInsurAmntUse").change(function(){
	var comInsurAmntUse = $("#specialInsurAddInfoForm #comInsurAmntUse").val();
	if(comInsurAmntUse==="0"){
		$("#specialInsurAddInfoForm #comInsurAmntType").combo("clear");
		$("#specialInsurAddInfoForm #comInsurAmntType").attr("disabled", true);
		$("#specialInsurAddInfoForm #commPremium").val("");
		$("#specialInsurAddInfoForm #fixedComAmnt").val("");
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").val("");
		$("#specialInsurAddInfoForm #ipsnFloatPct").val("");
		$("#specialInsurAddInfoForm #commPremium").attr("disabled",true);
		$("#specialInsurAddInfoForm #fixedComAmnt").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatPct").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").hide();
		$("#specialInsurAddInfoForm #commPremiumIs").hide();
		$("#specialInsurAddInfoForm #fixedComAmntIs").hide();
		//显示所有隐藏的div块。
		$("#specialInsurAddInfoForm #commPremiumtype").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").show();
	}else{
		$("#specialInsurAddInfoForm #comInsurAmntType").attr("disabled", false);
		$("#specialInsurAddInfoForm #commPremium").attr("disabled",false);
		$("#specialInsurAddInfoForm #fixedComAmnt").attr("disabled",false);
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").attr("disabled",false);
		$("#specialInsurAddInfoForm #ipsnFloatPct").attr("disabled",false);
	}
});

// 公共保额类型change事件
$("#specialInsurAddInfoForm #comInsurAmntType").change(function(){
	var comInsurAmntType = $("#specialInsurAddInfoForm #comInsurAmntType").val();
	if(comInsurAmntType==="0"){
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").show();
		$("#specialInsurAddInfoForm #commPremiumIs").hide();
		$("#specialInsurAddInfoForm #fixedComAmntIs").hide();
		//隐藏显示div
		$("#specialInsurAddInfoForm #commPremiumtype").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").show();
	}else if(comInsurAmntType==="1"){
		$("#specialInsurAddInfoForm #commPremiumIs").show();
		$("#specialInsurAddInfoForm #fixedComAmntIs").show();
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").hide();
		//隐藏显示div
		$("#specialInsurAddInfoForm #commPremiumtype").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").hide();
	}
});
//管理费比例离开焦点，计算首期管理费金额
$("#specialInsurAddInfoForm #adminPcent").blur(function(){
	var sumPrem = $("#sumPrem").val();//总保费
	var adminCalType = $("#adminCalType").val();//管理费提取方式
	var adminPcent = $("#adminPcent").val();//管理费比例
	if(adminPcent === ""){
		lion.util.info("警告","请输入管理费比例。");
		return false;
	}
	if(sumPrem === ""){
		lion.util.info("警告","请输入总保费。");
		return false;
	}
	if(adminCalType === "P" || adminCalType === "A"){
		$("#accSumAdminBalance").val(parseInt(sumPrem) * parseInt(adminPcent)/100);
	}else{
		lion.util.info("警告","请输入管理费计提方式。");
		return false;
	}
});
//个人账户金额 
$("#specialInsurAddInfoForm #ipsnAccAmnt").blur(function(){
	var adminCalType = $("#adminCalType").val();//管理费提取方式
	var ipsnAccAmnt = $("#ipsnAccAmnt").val();//个人账户金额 
	var adminPcent = $("#adminPcent").val();//管理费比例
	var adminCalType = $("#adminCalType").val();//管理费提取方式
	if(adminPcent === ""){ lion.util.info("警告","请输入管理费比例。"); return false; }
	if(ipsnAccAmnt !== ""){
		var adminPcent = parseInt(adminPcent)/100;
		if(adminCalType === "P"){
			$("#inclIpsnAccAmnt").val(parseInt(ipsnAccAmnt)*(1-adminPcent));
		}else if(adminCalType === "A"){
			$("#inclIpsnAccAmnt").val(parseInt(ipsnAccAmnt)/(1+adminPcent));
		}
	}
});

//公共账户交费金额
$("#specialInsurAddInfoForm #sumPubAccAmnt").blur(function(){
	var adminCalType = $("#adminCalType").val();//管理费提取方式
	var sumPubAccAmnt = $("#sumPubAccAmnt").val();//公共账户交费金额
	var adminPcent = $("#adminPcent").val();//管理费比例
	var adminCalType = $("#adminCalType").val();//管理费提取方式
	if(adminPcent === ""){ lion.util.info("警告","请输入管理费比例。"); return false; }
	if(sumPubAccAmnt !== ""){
		var adminPcent = parseInt(adminPcent)/100;
		if(adminCalType === "P"){
			$("#inclSumPubAccAmnt").val(parseInt(sumPubAccAmnt)*(1-adminPcent));
		}else if(adminCalType === "A"){
			$("#inclSumPubAccAmnt").val(parseInt(sumPubAccAmnt)/(1+adminPcent));
		}
	}
});
