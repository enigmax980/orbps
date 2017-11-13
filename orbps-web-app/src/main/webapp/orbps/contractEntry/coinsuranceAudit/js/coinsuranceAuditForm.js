// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.coinsuranceAudit命名空间
com.orbps.contractEntry.coinsuranceAudit = {};
// 编辑或添加对话框
com.orbps.contractEntry.coinsuranceAudit.insurancecompanylist = $('#insurancecompanylist');
com.orbps.contractEntry.coinsuranceAudit.busiprdlist = $('#busiprdlist');

$(function() { 
    // datagrid组件初始化
    // $("*").datagridsInitLoad();

    // combo组件初始化
    $("*").comboInitLoad();

    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
    //清空按钮功能
    $("#btnClear").click(function(){
    $("#agreementInf").reset();
    $("#insuranceInf").reset();
    });
  
    //查询按钮功能
    $("#btnQuery").click(function(){
        var agreementNo =  $("#agreementNo").val();
        lion.util
        .postjson(
                '/orbps/web/orbps/contractEntry/coinAudit/query',
                agreementNo,
                com.orbps.contractEntry.coinsuranceAudit.successQuery,
                null,
                null);
        var commonAgreement = {};
        commonAgreement.agreementNo = agreementNo;
     // 添加查询参数
        $("#insurancecompanylist").datagrids({
            querydata : commonAgreement
        });
        // 重新加载数据
        $("#insurancecompanylist").datagrids('reload');
        // 添加查询参数
        $("#busiprdlist").datagrids({
            querydata : commonAgreement
        });
        // 重新加载数据
        $("#busiprdlist").datagrids('reload');
    });
    
    //提交按钮功能
    
    $("#btnSubmit").click(function(){
        var coinsuranceAuditZVo = {};
        var agreementNo =  $("#agreementNo").val();
        var auditFindings =  $("#auditFindings").val();
        var mgrBranchNo = $("#mgrBranchNo").val();
        var remark = $("#remark").val();
        coinsuranceAuditZVo.coinsuranceInfoVo={};
        coinsuranceAuditZVo.coinsuranceInfoVo.agreementNo = agreementNo;
        coinsuranceAuditZVo.coinsuranceInfoVo.mgrBranchNo = mgrBranchNo;
        coinsuranceAuditZVo.auditFindings = auditFindings;
        coinsuranceAuditZVo.remark = remark;
        if (agreementNo === null || "" === agreementNo) {
            lion.util.info("警告", "请输入共保协议号并查询");
            return false;}
        if (mgrBranchNo === null || "" === agreementNo) {
            lion.util.info("警告", "审批的共保协议号不能为空");
            return false;}
        if (auditFindings === null || "" === auditFindings) {
            lion.util.info("警告", "请选择是否审批通过");
            return false;
        }else{
        	$("#btnSubmit").attr("disabled",true);
            lion.util.postjson('/orbps/web/orbps/contractEntry/coinAudit/submit',
            		coinsuranceAuditZVo,
            		com.orbps.contractEntry.coinsuranceAudit.successSubmit,
                    null,
                    null);
        }
    });
});

//查询成功回调函数
com.orbps.contractEntry.coinsuranceAudit.successQuery = function(data,args) {
//	console.log(data);
//	debugger;
    setTimeout(function() {
        var jsonStrs = JSON.stringify(data);
        var objs = eval("(" + jsonStrs + ")");
        var form;
        for (y in objs) {
            // form = y;
            // var keys = form;
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            if("appAddrCountry"===y){
            	$("#appAddrCountry").val(values);
            }
            var key, value, tagName, type, arr;
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                if("applDate"==key||"inforceDate"==key||"termDate"==key){
					value = new Date(value).format("yyyy-MM-dd");
				}
                $("[name='" + key + "'],[name='" + key + "[]']").each(
                        function() {
                            tagName = $(this)[0].tagName;
                            type = $(this).attr('type');
                            if (tagName == 'INPUT') {
                                if (type == 'radio') {
                                    $(this).attr('checked',
                                            $(this).val() == value);
                                } else if (type == 'checkbox') {
                                    arr = value.split(',');
                                    for (var i = 0; i < arr.length; i++) {
                                        if ($(this).val() == arr[i]) {
                                            $(this).attr('checked', true);
                                            break;
                                        }
                                    }
                                } else {
                                    $("#" + key).val(value);
                                }
                            } else if (tagName == 'SELECT'
                                    || tagName == 'TEXTAREA') {
                                $("#" + key).combo("val", value);
                            }
                        });
            }
        }
        $("#agreementInf input[type='text']").attr("readOnly", true);
        $("#agreementInf select").attr("readOnly", true);
        $("#agreementInf #agreementNo").attr("readOnly",false);
        $("#insuranceInf #auditFindings").attr("readOnly",false);
        $("#insuranceInf #remark").attr("readOnly",false);
    }, 100);
}

//日期回显long转String
Date.prototype.format = function(fmt)   { 
	//author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  

//审批提交后回调函数
com.orbps.contractEntry.coinsuranceAudit.successSubmit = function (data,args){
    if (data.retCode==="1"){
        lion.util.info("提示","提交成功");
    }else if (data.retCode==="0"){
        lion.util.info("提示", "提交失败，失败原因："+data.errMsg);
    }
    if("" !== data.retCode){
    	$("#btnSubmit").attr("disabled",false);
    }
}

