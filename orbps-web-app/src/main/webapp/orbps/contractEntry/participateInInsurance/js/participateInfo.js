// 新建contractEntry命名空间
com.orbps.contractEntry = {};
com.orbps.contractEntry.participateInInsurance = {};
com.orbps.contractEntry.participateInInsurance.insuredList = new Array();
com.orbps.contractEntry.participateInInsurance.benList = [];
com.orbps.contractEntry.participateInInsurance.participateInfoForm = $("#participateInfoForm");
com.orbps.contractEntry.participateInInsurance.policyInfoForm = $("#policyInfoForm");
com.orbps.contractEntry.participateInInsurance.insuredCount = 0;
com.orbps.contractEntry.participateInInsurance.insuredType = -1;
var insuredVo;
com.orbps.contractEntry.participateInInsurance.insured = $('#applListForm');

// 基本信息校验规则
com.orbps.contractEntry.participateInInsurance.insuredForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            agreementNo : {
                required : true
            },
            num : {
                required : true,
                isIntGteZero:true
            },
            startPeriod : {
                required : true
            },
            endPeriod : {
                required : true
            },
            transferAmnt : {
                required : true,
            },
            insurNum : {
                required : true,
                isIntGteZero:true
            },
            salesChannel : {
                required : true
            },
            salesBranchNo : {
                required : true,
                isBranchNo : true
            },
            salesCode : {
                required : true
            },
            saleName : {
                required : true
            },
        },
        messages : {
            agreementNo : {
                required : '请输入协议号',
            },
            num : {
                required : '请输入交接批次总人数',
                isIntGteZero:'请输入正整数位交接批次人数'	
            },
            startPeriod : {
                required : '请选择交接开始时间'
            },
            endPeriod : {
                required : '请选择交接截止时间'
            },
            transferAmnt : {
                required : "请输入交接金额",
            },
            insurNum : {
                required : "请输入交接件数",
                isIntGteZero:'请输入正整数位的交接件数'
            },
            salesChannel : {
                required : "请选择销售渠道"
            },
            salesBranchNo : {
                required : "请输入销售机构",
                isBranchNo : '机构为6位正整数！'
            },
            salesCode : {
                required : "请输入销售员代码"
            },
            saleName : {
                required : "请输入销售员姓名"
            },
        },

        invalidHandler : function(event, validator) {
            Metronic.scrollTo(error2, -200);
        },

        errorPlacement : function(error, element) {
            var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            if (icon.attr('title')
                    || typeof icon.attr('data-original-title') != 'string') {
                icon.attr('data-original-title', icon.attr('title') || '')
                        .attr('title', error.text())
            }
        },

        highlight : function(element) {
            $(element).closest('.col-md-2').removeClass("has-success")
                    .addClass('has-error');
        },

        unhighlight : function(element) {

        },

        success : function(label, element) {
            var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass(
                    'has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
    });
}

// 初始化函数
$(function() {
	$("#agreementName").attr("readOnly",true);
    // combo组件初始化
    $("*").comboInitLoad();
    // 日期初始化插件
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
       
	// 根据销售渠道更改页面样式，默认隐藏网点信息。
//	$("#worksiteHideDiv").hide();
//	$("#participateInfoForm #salesChannel").change(function() {
//		if ("OA" === $("#salesChannel").val()) {
//			$("#worksiteHideDiv").show();
//			$("#salesHideDiv").hide();
//			$("#participateInfoForm #salesCode").val("");
//		} else {
//			$("#worksiteHideDiv").hide();
//			$("#salesHideDiv").show();
//			$("#participateInfoForm #worksiteNo").val("");
//		}
//	});
    
    // 初始化校验信息
    com.orbps.contractEntry.participateInInsurance
            .insuredForm(com.orbps.contractEntry.participateInInsurance.participateInfoForm);
 // 丢失焦点计算交接总天数
    $("#endPeriod").change(function() {
    	
    	var startPeriod = $("#startPeriod").val();
    	var endPeriod = $("#endPeriod").val();
    	if(startPeriod!=="" && endPeriod!==""){
    		var totalDates=DateDiff(startPeriod,endPeriod);
    		if (totalDates<0){
                lion.util.info("提示","交接期间开始日期不能大于结束日期，请重新选择");
                $("#endPeriod").val("");
                return false;
    		}
    	}
    });
 // 丢失焦点计算交接总天数
    $("#startPeriod").change(function() {
    	
    	var startPeriod = $("#startPeriod").val();
    	var endPeriod = $("#endPeriod").val();
    	if(startPeriod!=="" && endPeriod!==""){
    		var totalDates=DateDiff(startPeriod,endPeriod);
    		if (totalDates<0){
                lion.util.info("提示","交接期间开始日期不能大于结束日期，请重新选择");
                $("#startPeriod").val("");
                return false;
    		}
    	}
    });
    //计算天数差的函数 
    function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
        var Date1 = new Date(sDate1);
        var Date2 = new Date(sDate2);
        var a = (Date2.getTime() - Date1.getTime())/(24 * 60 * 60 * 1000);
        return a;
    } 
 // 丢失协议号查询协议名称
    $("#agreementNo").blur(function() {
    	//$("#agreementName").reset();
        var agreementNo = $("#agreementNo").val();
        if (agreementNo === null || "" === agreementNo) {
            lion.util.info("警告", "协议号不能为空");
            $(".fa").removeClass("fa-warning");
            $(".fa").removeClass("fa-check");
            $(".fa").removeClass("has-success");
            $(".fa").removeClass("has-error");
            return false;
        }
        else{
            var participateInInsuranceVo = com.orbps.contractEntry.participateInInsurance.participateInfoForm.serializeObject();
    		lion.util.postjson(
    				'/orbps/web/orbps/contractEntry/par/query',
    				participateInInsuranceVo,
    				com.orbps.contractEntry.participateInInsurance.successQueryAgreementName,
    				null,
    				null);
        }
    });

    $("#num").blur(function() {
        var num = $("#num").val();
        if (num == null || "" == num) {
            lion.util.info("警告", "交接批次总人数不能为空");
            return false;
        }
    });

    

    $("#transferAmnt").blur(function() {
        var transferAmnt = $("#transferAmnt").val();
        if (transferAmnt == null || "" == transferAmnt) {
            lion.util.info("警告", "交接金额不能为空");
            $(".fa").removeClass("fa-warning");
            $(".fa").removeClass("fa-check");
            $(".fa").removeClass("has-success");
            $(".fa").removeClass("has-error");
            return false;
        }
    });

    $("#insurNum").blur(function() {
        var insurNum = $("#insurNum").val();
        if (insurNum == null || "" == insurNum) {
            lion.util.info("警告", "交接件数不能为空");
            return false;
        }
    });

    $("#salesBranchNo").blur(function() {
        var salesBranchNo = $("#salesBranchNo").val();
        if (salesBranchNo == null || "" == salesBranchNo) {
            lion.util.info("警告", "销售机构不能为空");
            return false;
        }
    });

    $("#salesCode").blur(function() {
        var salesCode = $("#salesCode").val();
        if (salesCode == null || "" == salesCode) {
            lion.util.info("警告", "销售员代码不能为空")
            return false;
        }else{
            var participateInInsuranceVo = com.orbps.contractEntry.participateInInsurance.participateInfoForm.serializeObject();
            var grpApplInfoVo = {};
            grpApplInfoVo.salesBranchNo=$("#salesBranchNo").val();
            grpApplInfoVo.salesChannel=$("#salesChannel").val();
            grpApplInfoVo.saleCode=$("#salesCode").val();
            lion.util.postjson(
    				'/orbps/web/orbps/contractEntry/search/querySaleName',
    				grpApplInfoVo,
    				com.orbps.contractEntry.participateInInsurance.successQuerySaleName,
    				null,
    				null);
        }
    });
    
 // 丢失代理网点号查询网点名称
//    $("#worksiteNo,#salesBranchNo").blur(
//    		function() {
//    			$("#worksiteName").val("");
//    			com.orbps.contractEntry.participateInInsurance.participateInfoForm
//    					.qureyWorksite();
//    		});
    // 当销售渠道改变时也要查询网点名称
//    $("#salesListForm #salesChannel").change(
//    		function() {
//    			if ("OA" === $("#salesListForm #salesChannel").val()) {
//    				$("#worksiteName").val("");
//    				com.orbps.contractEntry.participateInInsurance.participateInfoForm
//    						.qureyWorksite();
//    			}
//    		});
    // 丢失焦点查询网点名称的公共方法
//    com.orbps.contractEntry.participateInInsurance.participateInfoForm.qureyWorksite = function() {
//    	var worksiteNo = $("#participateInfoForm #worksiteNo").val();
//    	var salesBranchNo = $("#participateInfoForm #salesBranchNo").val();
//    	var salesChannel = $("#participateInfoForm #salesChannel").val();
//    	if ("" === worksiteNo || "" === salesBranchNo || "" === salesChannel) {
//    		return false;
//    	} else {
//    		var regSalesListFormVo = {};
//    		regSalesListFormVo.worksiteNo = worksiteNo;
//    		regSalesListFormVo.salesBranchNo = salesBranchNo;
//    		regSalesListFormVo.salesChannel = salesChannel;
//    		lion.util
//    				.postjson(
//    						'/orbps/web/orbps/contractEntry/search/queryWorksite',
//    						regSalesListFormVo,
//    						com.orbps.contractEntry.participateInInsurance.participateInfoForm.successQueryWorksiteName,
//    						null, null);
//    	}
//    };
    
    
    });
//查询销售员姓名回调函数
com.orbps.contractEntry.participateInInsurance.successQuerySaleName = function (data, arg){
	if(data==="fail"){
	    lion.util.info("销售员不存在，请确认销售渠道，销售机构，销售员代码是否正确");
	}else{
	    $("#saleName").val(data);
	}
};
//查询协议号回调函数
com.orbps.contractEntry.participateInInsurance.successQueryAgreementName = function (data, arg){
	if(data==="没有查询到符合条件数据！"){
		lion.util.info("提示",data);
	    $("#agreementName").val("");
	}else{
	    $("#agreementName").val(data);
	}
};

//查询网点名称回调函数
//com.orbps.contractEntry.participateInInsurance.participateInfoForm.successQueryWorksiteName = function(
//		data, arg) {
//	if (data === "fail") {
//		lion.util.info("网点不存在，请确认销售渠道，销售机构，代理网点号是否正确");
//	} else {
//		$("#participateInfoForm  #worksiteName").val(data);
//		$("#participateInfoForm  #worksiteName").attr("readOnly", true);
//	}
//};

//导出批量核销模板
$('#btnDownload').click(function(){
	window.location.href="/orbps/web/orbps/sendreceipt/CvTask/downloadListImport";
});


// 批量导入
$("#btnBatchImport").click(function(){
	var batchNo="";
	//清空提示标记的flag
	var path = $('#fileupload').val(); 
	if("" === path){
		lion.util.info("请选择要上传的文件");
		return false;
	};
	var myDate = new Date();
	batchNo = myDate.getTime();
	var formData = new FormData($('#uploadListForms')[0]);
	lion.util.postfile("/orbps/web/orbps/contractEntry/par/upload",formData,function(data, args){
		com.orbps.contractEntry.participateInInsurance.insuredList = data;
		reloadInsuredModalTable();
	});
});

