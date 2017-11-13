// 新建otherFunction命名空间
com.orbps.otherFunction = {};
// 新建otherFunction.contractRevocation命名空间
com.orbps.otherFunction.contractRevocation = {};
com.orbps.otherFunction.contractRevocation.reviseForm = $("#reviseForm");
com.orbps.otherFunction.contractRevocation.addDialog = $('#btnModel1');
com.orbps.otherFunction.contractRevocation.revokelist = $('#revokelist')

//基本信息校验规则
com.orbps.otherFunction.contractRevocation.contrantForm= function (vform) {
        var error2 = $('.alert-danger', vform);
        var success2 = $('.alert-success', vform);
        vform.validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error', 
            focusInvalid: false, 
            onkeyup:false,
            ignore: '', 
            rules : {
                correctApplNo : {
                    isApplNo : true
                }
            },
            messages : {
                correctApplNo : {
                    isApplNo : "投保单号为16位正整数！"
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
    //初始化
    com.orbps.otherFunction.contractRevocation.contrantForm(com.orbps.otherFunction.contractRevocation.reviseForm);
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    // combo组件初始化
    $("*").comboInitLoad();
    
    // 查询契约撤销信息
    $("#query").click(function() {
       var correctionVo =com.orbps.otherFunction.contractRevocation.reviseForm.serializeObject();
            // 添加查询参数
            $("#revokelist").datagrids({
                querydata : correctionVo            
            });
            // 重新加载数据   
            $("#revokelist").datagrids('reload');
      });
    
    //撤销提交
    $("#btnrevoke")
    .click(
            function() { 
                var queryinfVo = {};
                queryinfVo = com.orbps.otherFunction.contractRevocation.revokelist.datagrids('getSelected');
                var applNo = queryinfVo.applNo;
                //alert(applNo);
                if(undefined !== applNo && null !== applNo && "" !== applNo){
                	lion.util.postjson(
                            '/orbps/web/orbps/otherfunction/revoke/submit',
                            applNo,
                            com.orbps.otherFunction.contractRevocation.succesubmit,
                            null,
                            null);
                }else{
                	lion.util.info("提示", "请选择撤销的投保单！");
                    return false;
                }
            });
});

//撤销后回调函数
com.orbps.otherFunction.contractRevocation.succesubmit=function(arg,data,msg){
    var flag = arg.applNo;
    if(flag==1)
    	lion.util.info("提示",'撤销成功！');   
    else
    	lion.util.info("提示",'撤销失败！');
 // 成功后刷新页面
    setTimeout(function() {
    	var correctionVo =com.orbps.otherFunction.contractRevocation.reviseForm.serializeObject();
        // 添加查询参数
        $("#revokelist").datagrids({
            querydata : correctionVo            
        });
        // 重新加载数据   
        $("#revokelist").datagrids('reload');
    }, 500);
    
}



