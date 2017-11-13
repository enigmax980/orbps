
//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.beneficiaryInfoValidateForm= function (vform) {
	    var error2 = $('.alert-danger', vform);
	    var success2 = $('.alert-success', vform);
	    vform.validate({
			errorElement: 'span',
	        errorClass: 'help-block help-block-error', 
	        focusInvalid: false, 
	        onkeyup:false,
	        ignore: '', 
			rules : {  								
			},
			messages : {				
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



//增加表格
$("#beneficiaryInfo #btnAdd").click(function() {
	$("#fbp-editDataGrid-one").editDatagrids("addRows");
	return false;
});

//删除表格
$("#beneficiaryInfo #btnDel").click(function () {  
	$("#fbp-editDataGrid-one").editDatagrids("delRows");
	return false;
}); 




