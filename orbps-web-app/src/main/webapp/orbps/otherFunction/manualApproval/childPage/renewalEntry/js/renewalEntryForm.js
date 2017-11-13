// 新建contractEntry命名空间
com.orbps.contractEntry={};
// 新建contractEntry.renewalEntry命名空间
com.orbps.contractEntry.renewalEntry={};
//treegrid
com.orbps.contractEntry.renewalEntry.tableId = $('#treegridId');
//编辑或添加对话框
com.orbps.contractEntry.renewalEntry.addDialog = $('#btnModel');
com.orbps.contractEntry.renewalEntry.insuredList = new Array();
com.orbps.contractEntry.renewalEntry.oranLevelList = new Array();
com.orbps.contractEntry.renewalEntry.grpInsurApplList = new Array();
com.orbps.contractEntry.renewalEntry.renewalEntryForm = $('#renewalEntryForm');
com.orbps.contractEntry.renewalEntry.pollicitation=$("#pollicitation_one")
com.orbps.contractEntry.renewalEntry.busiPrqdCode = null;
//基本信息校验规则
com.orbps.contractEntry.renewalEntry.handleForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		messages : {
			applNo : {
				required : '请输入投保单号'
			},
			applDate : {
				required : '请选择投保日期'
			},
			salesChannel : {
				required : "请输入销售渠道"
			},
			salesBranchNo : {
				required : "请输入销售机构"
			},
			saleCodeNo : {
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
			birthDate: {
				required : "请输入出生日期"
			},
			old: {
				required : "请输入年龄"
			},
			idType: {
				required : "请输入证件类型"
			},
			idNo: {
				required : "请输入证件号码"
			},
			idTerm: {
				required : "请输入证件有效期"
			},
			companyBankCode: {
				required : "请输入职业代码"
			},
			companyBankClass: {
				required : "请输入职业类别"
			},
			nationality: {
				required : "请输入国籍"
			},
			zipCode: {
				required : "请输入邮编"
			},
			posAddress: {
				required : "请输入通讯地址"
			},
			mobPhone: {
				required : "请输入移动电话"
			},
			lanPhone: {
				required : "请输入固定电话"
			},
			mailBox: {
				required : "请输入电子邮箱"
			}
			
		},
		rules : {
			applNo : {
				required : true
			},
			applDate : {
				required : true
			},
			salesChannel : {
				required : true
			},
			salesBranchNo : {
				required : true
			},
			saleCodeNo : {
				required : true
			},
			customerNo: {
				required : true
			},
			Name: {
				required : true
			},
//			sex: {
//				required : true
//			},
			birthDate: {
				required : true
			},
			old: {
				required : true
			},
//			idType: {
//				required : true
//			},
			idNo: {
				required : true
			},
			idTerm: {
				required : true
			},
//			companyBankCode: {
//				required : true
//			},
			companyBankClass: {
				required : true
			},
//			nationality: {
//				required : true
//			},
			zipCode: {
				required : true
			},
			posAddress: {
				required : true
			},
			mobPhone: {
				required : true
			},
			lanPhone: {
				required : true
			},
			mailBox: {
				required : true
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
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// 上一步下一步控件初始化
	$("*").stepInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	// 文件上传下载插件初始化
	$("#fileTal").fileinput({
        'allowedFileExtensions' : ['jpg', 'png','gif','xlsx'],
        'showUpload':true,
        'showPreview':false,
        'showCaption':true,
        'browseClass':'btn btn-success',
    });
	$("#fileVal").fileinput({
        'allowedFileExtensions' : ['jpg', 'png','gif','xlsx'],
        'showUpload':true,
        'showPreview':false,
        'showCaption':true,
        'browseClass':'btn btn-success',
    });
	$("#fbp-editDataGrids").editDatagridsLoadById();
	// 初始化edittable组件
	// combo组件初始化
	$("*").comboInitLoad();
	// 调用校验方法
	com.orbps.contractEntry.renewalEntry.handleForm(com.orbps.contractEntry.renewalEntry.renewalEntryForm);
	
	setTimeout(function(){
		//是否覆盖以往信息下拉框默认显示否
		$("#renewalEntryForm #companyBankCode").combo("val","N");
	},1000);
	
	// 点击next
	$("#btnNext").click(function() {
		
		if(com.orbps.contractEntry.renewalEntry.renewalEntryForm.validate().form()){
			com.example.formwizard.submitForm();
			
		}else{
//			document.getElementById("btnPrev").click();
			alert(2222222);
			return false;
		}
	});
	
	//增加表格
	$("#renewalEntryForm #btnAdd").click(function() {
		$("#fbp-editDataGrids").editDatagrids("addRows");
	});
	
	//删除表格
	$("#renewalEntryForm #btnDel").click(function () {  
		$("#fbp-editDataGrids").editDatagrids("delRows");
    }); 
	
	//查询责任信息
	$("#renewalEntryForm #btnSelect").click(function() {
		var selectData = $("#renewalEntryForm #fbp-editDataGrids").editDatagrids("getSelectRows");
		// 判断选择的是否是一个主险，判断是否添加主险信息
		if((null == selectData)||(selectData.length==0)||(selectData.length>1)){
			lion.util.warning("警告","请选择一个主险信息");
			return false;
		}
		com.orbps.contractEntry.renewalEntry.addDialog.empty();
		com.orbps.contractEntry.renewalEntry.addDialog.load("/orbps/orbps/contractEntry/renewalEntry/html/renewalEntryModel.html",function(){
			alert(1);
			$("#coverageInfo_list").editDatagridsLoadById();
			alert(2);
			$(this).modal('toggle');
			// combo组件初始化
			$("#renewalEntryForm #coverageInfo_list").editDatagrids("queryparams",selectData);
			// 重新加载数据
			$("#renewalEntryForm #coverageInfo_list").editDatagrids("reload");
			setTimeout(function(){
    			$("#renewalEntryForm #coverageInfo_list").editDatagrids("selectRows",com.orbps.contractEntry.renewalEntry.grpInsurApplList);
			},400);		
		});
	});
	
	 //被保人分组
	 $("#btnGoup").click(function() {
		  com.orbps.contractEntry.renewalEntry.addDialog.empty();
		  com.orbps.contractEntry.renewalEntry.addDialog.load("/orbps/orbps/contractEntry/grpInsurAppl/html/insuredGroupModal.html",function(){
			  $(this).modal('toggle');
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
		com.orbps.contractEntry.renewalEntry.addDialog.empty();
		com.orbps.contractEntry.renewalEntry.addDialog.load("/orbps/orbps/contractEntry/grpInsurAppl/html/organizaHierarModal.html",function(){
			$(this).modal('toggle');
			// combo组件初始化
			$(this).comboInitLoad();
			$(this).combotreeInitLoad();	
		});
		setTimeout(function(){
			// 回显
			reloadOranLevelModalTable();
		},100);
	 });
	
});

