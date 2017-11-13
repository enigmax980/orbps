// 新建contractEntry命名空间
com.orbps.contractEntry = {};
com.orbps.contractEntry.registrationAcceptance = {};
//回调函数标识，页面下拉框的change事件根据此标识判断是否要清空信息
com.orbps.contractEntry.registrationAcceptance.queryFlag = 1;
com.orbps.contractEntry.registrationAcceptance.salesDevelopChangeFlag = 1;

// com.orbps.contractEntry.registrationAcceptance.handleForm= function (vform) {
// var error2 = $('.alert-danger', vform);
// var success2 = $('.alert-success', vform);
// vform.validate({
// errorElement: 'span',
// errorClass: 'help-block help-block-error',
// focusInvalid: false,
// onkeyup:false,
// ignore: '',
// messages : {
// applNo : {
// required : '请输入投保单号'
// },
// applDate : {
// required : '请选择申请日期'
// },
// groupPriceNo : {
// required : '请输入团险报价审批号'
// },
// entChannelFlag : {
// required : '请选择外包录入标记'
// },
// insuredNum : {
// required : '请输入被保险人数'
// },
// cntrType : {
// required : "请选择契约形式"
// },
// polCode : {
// required : "请选择险种"
// },
// sumPremium : {
// required : "请输入保费合计"
// },
// hldrName : {
// required : "请输入投保人/汇交人"
// },
// applNum : {
// required : "请输入投保单数量"
// }
// },
// rules : {
// applNo : {
// required : true
// },
// applDate : {
// required : true
// },
// groupPriceNo : {
// required : true
// },
// entChannelFlag : {
// required : true
// },
// insuredNum : {
// required : true
// },
// cntrType : {
// required : true
// },
// polCode : {
// required : true
// },
// sumPremium : {
// required : true
// },
// hldrName : {
// required : true
// },
// applNum : {
// required : true
// }
// },
//
// invalidHandler: function (event, validator) {
// Metronic.scrollTo(error2, -200);
// },
//        
// errorPlacement:function(error,element){
// var icon = $(element).parent('.input-icon').children('i');
// icon.removeClass('fa-check').addClass("fa-warning");
// icon.attr("data-original-title", error.text()).tooltip({'container':
// 'body'});
// },
//    
// highlight: function (element) {
// $(element).closest('.col-md-2').removeClass("has-success").addClass('has-error');
// },
//        
// unhighlight: function (element) {
//
// },
//        
// submitHandler: function (form) {
// success2.show();
// error2.hide();
// form[0].submit();
// },
//        
// success: function (label, element) {
// var icon = $(element).parent('.input-icon').children('i');
// $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
// icon.removeClass("fa-warning").addClass("fa-check");
// }
// });
// }

$(function() {
    // datagrid控件初始化表格 与bootstrap的表格样式不兼容
    // $('#applListTab').datagrid();
    // $("#treegridId").datagrid();
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // combo组件初始化
    $("*").comboInitLoad();
    
    //隐藏北分代理员信息
    $("#salesListForm #beijing").hide();
    // 调用校验逻辑
    // $("#btnCommit1").click(function(){
    // com.orbps.contractEntry.grpInsurAppl.handleForm(com.orbps.contractEntry.registrationAcceptance.uniersalAcceptForm);
    // });
    // 清空按钮
    
    //判断是否为北分人员
	lion.util.postjson(
			'/orbps/web/orbps/contractEntry/search/searchClerk',
			"",
			function(data,args){
				if("1" === data){
					$("#salesListForm #beijing").show();
				}
			});
	
    $("#btnClear1").click(function() {
        $("#insurRegistAccept").reset();
    });
});
