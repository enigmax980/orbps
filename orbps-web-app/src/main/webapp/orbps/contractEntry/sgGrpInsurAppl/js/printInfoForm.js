com.orbps.contractEntry.sgGrpInsurAppl.printInfoForm = $("#printInfoForm");

com.orbps.contractEntry.sgGrpInsurAppl.printValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
//            underNoticeType : {
//                required : true
//            },
//            listPrint : {
//                required :  true
//            },
//            personalIdPrint : {
//                required :  true
//            },
//            listFlag : {
//                required :  true
//            },
//            unusualFlag : {
//                required :  true
//            } 
        },
        messages : {
//            underNoticeType : {
//                required : "请选择承保通知书类型"
//            },
//            listPrint : {
//                required : "请选择清单打印"
//            },
//            personalIdPrint : {
//                required : "请选择个人凭证打印"
//            },
//            listFlag : {
//                required : "请选择清单标记"
//            },
//            unusualFlag : {
//                required : "请选择是否异常告知"
//            }         
        },
        invalidHandler : function(event, validator) {
            Metronic.scrollTo(error2, -200);
        },

        errorPlacement : function(error, element) {
            var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            if (icon.attr('title')
                    || typeof icon.attr('data-original-title') !== 'string') {
                icon.attr('data-original-title', icon.attr('title') || '')
                        .attr('title', error.text())
            }
        },

        highlight : function(element) {
            $(element).closest('.col-md-2').removeClass("has-success")
                    .addClass('has-error');
        },

        success : function(label, element) {
            var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass(
                    'has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
    });
}
$(function() {
 // 初始化校验函数
    com.orbps.contractEntry.sgGrpInsurAppl
            .printValidateForm(com.orbps.contractEntry.sgGrpInsurAppl.printInfoForm);
    
//    setTimeout(function(){
//    	//是否人工核保下拉框默认显示否
//    	$("#printInfoForm #manualUwFlag").combo("val","N");
//    },1000);
    
});

$("#printInfoForm #listFlag").change(function() { 
	$("#personalIdPrint").attr("disabled",false);
	$("#listPrint").attr("disabled",false);
	if($("#printInfoForm #listFlag").val()==="N"){
		$("#personalIdPrint").attr("disabled",true);
		$("#personalIdPrint").combo("clear");
		$("#listPrint").attr("disabled",true);
		$("#listPrint").combo("clear");
	}
	if($("#printInfoForm #listFlag").val()==="N" || $("#printInfoForm #listFlag").val() === "M"){
		lion.util.info("警告","该保单不支持无清单和事后补录清单，请检查！");
	}
});




    