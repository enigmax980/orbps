/** 新建otherFunction命名空间 */
com.orbps.otherFunction = {};
/** 新建otherFunction.earnestPay命名空间 */
com.orbps.otherFunction.earnestPay = {};
/** 声明表单form */
com.orbps.otherFunction.earnestPay.applInfoForm = $("#applInfoForm");
com.orbps.otherFunction.earnestPay.accInfoForm = $("#accInfoForm");
/** 声明table表格 */
com.orbps.otherFunction.earnestPay.chargePayGroupInfoTable = $("chargePayGroupInfoTable");
/** 收费组表格，选中的list */
com.orbps.otherFunction.earnestPay.chargePayGroupInfoList = new Array();
com.orbps.otherFunction.earnestPay.tempList = [];
/** 账户信息表格，选中的list */
com.orbps.otherFunction.earnestPay.accInfoList = [];

com.orbps.otherFunction.earnestPay.submitFun = function(data,args){
	if("1" === data.retCode){
		lion.util.info("提交成功");
	}else if("0" === data.retCode){
		lion.util.info("提交失败","失败原因："+data.errMsg);
	}else{
		lion.util.info("提交失败");
	}
}

// 初始化函数
$(function() {
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    // datagrid组件初始化
	$("*").datagridsInitLoad();
	// 初始化edittable组件
	$("#accInfoTable").editDatagridsLoadById();
    // combo组件初始化
    $("*").comboInitLoad();
    
    //初始化界面，隐藏信息
    $("#grpInfo").show();
    $("#payInfo").hide();
    $("#payInfoBtn").hide();
    
    //设置表格checkbox属性
    $("#chargePayGroupInfoTable").find("thead th input[type=checkbox]").each(function(){
        var box=$(this);
        box.attr('disabled',true);
    });
    $("#accInfoTable").find("thead th input[type=checkbox]").each(function(){
        var box=$(this);
        box.attr("disabled",true);
    });
    
    //初始化下拉框
    setTimeout(function(){
    	$("#payAllFlag").combo("val","N");
    },100);
    
    //显示团体查询信息
    $("#grpRad").click(function(){
    	$("#grpName").val("");
    	$("#grpInfo").show();
        $("#payInfo").hide();
        $("#payInfoBtn").hide();
        com.orbps.otherFunction.earnestPay.chargePayGroupInfoList = [];
        com.orbps.otherFunction.earnestPay.tempList = [];
    });
    
    //显示个人查询信息
    $("#perRad").click(function(){
    	$("#payGrpNo").val("");
    	$("#grpInfo").hide();
        $("#payInfo").show();
        $("#payInfoBtn").show();
        queryInfoVo = {};
    	// 添加查询参数
        $("#chargePayGroupInfoTable").datagrids({
            querydata : queryInfoVo            
        });
        // 重新加载数据   
        $("#chargePayGroupInfoTable").datagrids("reload");
    });
    
    //获取收费组的list
    $("table#chargePayGroupInfoTable")
	.delegate(
			'span',
			'click',
			function() {
				com.orbps.otherFunction.earnestPay.getSelectRows();
			});
    com.orbps.otherFunction.earnestPay.getSelectRows = function() {
		var flag = 0;
		var k = 0;
		com.orbps.otherFunction.earnestPay.tempList = $("#chargePayGroupInfoTable").datagrids("getSelected");
		if(com.orbps.otherFunction.earnestPay.chargePayGroupInfoList.length>0){
			for(i = 0;i<com.orbps.otherFunction.earnestPay.chargePayGroupInfoList.length;i++){
				if(com.orbps.otherFunction.earnestPay.tempList.groupNo===com.orbps.otherFunction.earnestPay.chargePayGroupInfoList[i].groupNo){
					flag = 1;
					k = i;
				}
			}
			if(flag === 0){
				com.orbps.otherFunction.earnestPay.chargePayGroupInfoList.push(com.orbps.otherFunction.earnestPay.tempList)
			}else if(flag === 1){
				com.orbps.otherFunction.earnestPay.chargePayGroupInfoList.splice(k,1);
			}
		}else{
			com.orbps.otherFunction.earnestPay.chargePayGroupInfoList.push($("#chargePayGroupInfoTable").datagrids('getSelected'));
		}
	}
    
    //查询账户信息
    $("#btnAccInfoQuery").click(function(){
    	queryInfoVo = {};
    	queryInfoVo = com.orbps.otherFunction.earnestPay.applInfoForm.serializeObject();
    	queryInfoVo.payGrpNos = "";
    	for(var i = 0;i<com.orbps.otherFunction.earnestPay.chargePayGroupInfoList.length;i++){
    		queryInfoVo.payGrpNos = com.orbps.otherFunction.earnestPay.chargePayGroupInfoList[i].groupNo+","+queryInfoVo.payGrpNos;
    	}
    	// 添加查询参数
    	$("#accInfoTable").editDatagrids("queryparams",queryInfoVo);
    	// 重新加载数据
        $("#accInfoTable").editDatagrids("reload", queryInfoVo);
        setTimeout( function(){
        	// 重新加载数据
        	var getdata = $("#accInfoTable").editDatagrids("getRowsData");
        	if(getdata.length>0){
        		$("#sumAmount").val(getdata[0].sumBalance);
        	}else{
        		lion.util.info("提示","查询数据为空,请重新输入查询条件");
        	}
        },5000);
    });
    
    //设置账户表格只读属性
	$("#accInfoTable").find("tbody").dblclick(function() {
		$("#accInfoTable #applNo").attr("readOnly", true);
		$("#accNo").attr("readOnly", true);
		$("#accType").attr("readOnly", true);
		$("#accPersonNo").attr("readOnly", true);
		$("#balance").attr("readOnly", true);
	});
    
    //获取被选中的暂交费账户
    com.orbps.otherFunction.earnestPay.getAccInfoRows = function(){
    	com.orbps.otherFunction.earnestPay.accInfoList = [];
    	var rows = $("#accInfoTable").find("tbody tr");
    	for (var i = 0; i < rows.length; i++) {
    		var trData = $(rows[i]).data();
    		if($("#" + rows[i].id).find("input[name='checkboxs']").is(":checked")){
    			com.orbps.otherFunction.earnestPay.accInfoList.push(trData.data);
    		}
    	}
    }
    
    //提交账户信息
    $("#btnSubmit").click(function(){
    	com.orbps.otherFunction.earnestPay.getAccInfoRows();
    	var payAllFlag = $("#payAllFlag").val();
    	if('N'===payAllFlag){
    		lion.util.info("提示","如果不是全部提交,则只能提交本页所选择的账户");
    	}
    	if('Y'!==payAllFlag && com.orbps.otherFunction.earnestPay.accInfoList.length===0){
    		lion.util.info("提交失败，请选择提交的账户");
			return false;
    	}
    	if(com.orbps.otherFunction.earnestPay.accInfoList.length>0){
    		for(i=0; i<com.orbps.otherFunction.earnestPay.accInfoList.length; i++){
    			if(0 >= parseInt(com.orbps.otherFunction.earnestPay.accInfoList[i].balance)){
    				lion.util.info("提交失败，提交的账户余额不足，请检查账户信息");
    				return false;
    			}
    			if(0 > parseInt(com.orbps.otherFunction.earnestPay.accInfoList[i].payAmount)){
    				lion.util.info("提交失败，提交的支取金额小于0，请检查账户信息");
    				return false;
    			}
    			if(parseInt(com.orbps.otherFunction.earnestPay.accInfoList[i].balance) < parseInt(com.orbps.otherFunction.earnestPay.accInfoList[i].payAmount)){
    				lion.util.info("提交失败，提交的支取金额大于账户余额，请检查账户信息");
    				return false;
    			}
    		}
    	}
    	earnestPay = {};
    	earnestPay.queryInfoVo = com.orbps.otherFunction.earnestPay.applInfoForm.serializeObject();
    	earnestPay.accInfoVos = com.orbps.otherFunction.earnestPay.accInfoList;
    	earnestPay.sumAmount = $("#sumAmount").val();
    	earnestPay.payAllFlag = $("#payAllFlag").val();
    	lion.util
		.postjson("/orbps/web/orbps/otherfunction/earnestPay/submit", earnestPay,
				com.orbps.otherFunction.earnestPay.submitFun);
    });
});


//查询收费组信息
$("#btnPayGrpQuery").click(function(){
	
	if("" === $("#applInfoForm #applNo").val()){
		lion.util.info("请输入投保单号");
		return false;
	}
	queryInfoVo = {};
	queryInfoVo = com.orbps.otherFunction.earnestPay.applInfoForm.serializeObject();
	// 添加查询参数
    $("#chargePayGroupInfoTable").datagrids({
        querydata : queryInfoVo            
    });
    // 重新加载数据   
    $("#chargePayGroupInfoTable").datagrids("reload");
    setTimeout( function(){
    	// 重新加载数据
    	var getdata = $("#chargePayGroupInfoTable").datagrids("getdata");
    	if(getdata.length<=0){
    		lion.util.info("提示","查询数据为空,请重新输入查询条件");
    	}
    },3000);
});
