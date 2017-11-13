com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery ={};
com.orbps.otherFunction.contractQuery.mgrBranchList = new Array();
//险种
com.orbps.otherFunction.contractQuery.grpBusiPrdList = new Array();
//责任
com.orbps.otherFunction.contractQuery.responseList = new Array();
com.orbps.otherFunction.contractQuery.query = $("#query");
com.orbps.otherFunction.contractQuery.cntrQueryForm= $("#cntrQueryForm");
com.orbps.otherFunction.contractQuery.data;
com.orbps.otherFunction.contractQuery.proposalGroupList;
com.orbps.otherFunction.contractQuery.addDialog = $("#btnModel");
com.orbps.common = {};
com.orbps.otherFunction.contractQuery.healthPublicAmountArray;
com.orbps.otherFunction.contractQuery.constructionInfoArray;
//初始化函数
$(function() {
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
});

//查询点击按钮
$("#cntrQueryForm #query").click(function () {
	var applNo = $("#applNo").val();
	if(applNo===""){
		lion.util.info("提示","请输入投保单号");
	}else if(!/^[0-9]{16}$/.test($("#applNo").val())){
		lion.util.info("提示","投保单号输入不正确");
	}else{
		lion.util.postjson('/orbps/web/orbps/otherfunction/contractQuery/check',applNo,com.orbps.otherFunction.contractQuery.successQuerysss,null,"");
	}
});


//查询成功回调函数
com.orbps.otherFunction.contractQuery.successQuerysss = function(data,arg){
	if(data.cntrType =="G"){
		com.orbps.otherFunction.contractQuery.data=data;
		com.orbps.otherFunction.contractQuery.grpBusiPrdList = data.busiPrdVos;
		//查询公共保额的险种，健康险集合，
		lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
				function (data, arg) {
			com.orbps.otherFunction.contractQuery.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
			com.orbps.otherFunction.contractQuery.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
				});
		lion.util.load($("#ruleAddInfo"),"/orbps/orbps/public/applQuery/grpInsurAppl/html/grpInsurApplForm.html");
	}else if(data.cntrType =="L"){
		var applNo = $("#applNo").val();
		lion.util.postjson('/orbps/web/orbps/contractEntry/sg/querySgGrpInser',applNo,com.orbps.otherFunction.contractQuery.successQuerysg,null,"");
	}else {
    	lion.util.info("提示","没有符合条件的记录");
	}
} 

com.orbps.otherFunction.contractQuery.successQuerysg = function(data,arg){
	com.orbps.otherFunction.contractQuery.data=data;
	com.orbps.otherFunction.contractQuery.grpBusiPrdList = data.addinsuranceVos;
	lion.util.load($("#ruleAddInfo"),"/orbps/orbps/public/applQuery/sgGrpInsurAppl/html/sgGrpInsurAppl.html");
}
