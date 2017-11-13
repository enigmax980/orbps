com.orbps.contractEntry.participateInInsurance.subForm = $("#submit");
com.orbps.contractEntry.participateInInsurance.successQuery;
//基本信息校验规则

$(function() {

	// 表单提交
	$("#btnSubmit")
	.click(
			function() {
				
				// jquery validate 校验
				if (com.orbps.contractEntry.participateInInsurance.participateInfoForm
						.validate().form())
//						||com.orbps.contractEntry.participateInInsurance.policyInfoForm
//						.validate().form()) 
				{     
					if (validateSelectVal()) {
						// 提交方法
						var participateInInsuranceVo = {};
						participateInInsuranceVo = com.orbps.contractEntry.participateInInsurance.participateInfoForm
						.serializeObject();       
						participateInInsuranceVo.participateInfoVos = com.orbps.contractEntry.participateInInsurance.insuredList;                                                          
//						alert(JSON
//								.stringify(participateInInsuranceVo));
						lion.util
						.postjson(
								'/orbps/web/orbps/contractEntry/par/submit',
								participateInInsuranceVo,
								successSubmit, null, null);
					}
				}
			});

});

//添加成功回调函数
function successSubmit(obj, data, arg) {
	if (obj.retCode==="1"){
		lion.util.info("提示","提交成功");
	}else if (obj.retCode==="0"){
		lion.util.info("提示",obj.errMsg);
	}
	// 成功后刷新页面
	setTimeOut(function() {
		window.location.reload();
	}, 500);
}
//校验选择信息
function validateSelectVal() {
	var salesChannel = $("#participateInfoForm #salesChannel").val();
	if (salesChannel == null || "" == salesChannel) {
		lion.util.info("警告", "请选择销售渠道");
		return false;
	}
	var startPeriod = $("#participateInfoForm #startPeriod").val();
	if (startPeriod == null || "" == startPeriod) {
		lion.util.info("警告", "有效起始时间不能为空");
		return false;
	}
	var endPeriod = $("#participateInfoForm #endPeriod").val();
	if (endPeriod == null || "" == endPeriod) {
		lion.util.info("警告", "有效起始时间不能为空");
		return false;
	}
	return true;
}


com.orbps.contractEntry.participateInInsurance.successQuery = function(data,arg){
	var msg=data.agreementInfVo;
	//回显方法
	jsonStr = JSON.stringify(data);
	var obj = eval("("+jsonStr+")");
	var key,value,tagName,type,arr;
	for(x in obj){
		key = x;
		value = obj[x];
		$("[name='"+key+"'],[name='"+key+"[]']").each(function(){
			tagName = $(this)[0].tagName;
			type = $(this).attr('type');
			if(tagName==='INPUT'){
				if(type==='radio'){
					$(this).attr('checked',$(this).val()===value);
				}else if(type==='checkbox'){
					arr = value.split(',');
					for(var i =0;i<arr.length;i++){
						if($(this).val()===arr[i]){
							$(this).attr('checked',true);
							break;
						}
					}
				}else{
					$("#participateInfoForm #"+key).val(value);
				}
			}else if(tagName==='SELECT' || tagName==='TEXTAREA'){
				$("#participateInfoForm #"+key).combo("val", value);
			}
		});
	}  

	// 重新加载表格

	//alert(JSON.stringify(data.participateInfoVo));
	com.orbps.contractEntry.participateInInsurance.insuredList = data.participateInfoVo;

	$('#applListForm').find("tbody").empty();
	if (com.orbps.contractEntry.participateInInsurance.insuredList != null
			&& com.orbps.contractEntry.participateInInsurance.insuredList.length > 0) {
		for (var i = 0; i < com.orbps.contractEntry.participateInInsurance.insuredList.length; i++) {
			$('#applListForm')
			.find("tbody")
			.append(
					"<tr><td ><input type='radio' onclick='com.orbps.contractEntry.participateInInsurance.radio();' id='insuredRad"
					+ i
					+ "' name='insuredRad' value='"
					+ i
					+ "'></td><td >"
					+ com.orbps.contractEntry.participateInInsurance.insuredList[i].cntrNo
					+ "</td><td >"
					+ com.orbps.contractEntry.participateInInsurance.insuredList[i].companyName
					+ "</td><td >"
					+ com.orbps.contractEntry.participateInInsurance.insuredList[i].perPolicyNum
					+ "</td><td >"
					+ com.orbps.contractEntry.participateInInsurance.insuredList[i].remark

					+ "</td><td ><a href='javascript:void(0);' onclick='com.orbps.contractEntry.participateInInsurance.del("
					+ i
					+ ");' for='insuredRad' id='insuredDel"
					+ i + "'>删除</a></td></tr>");
		}
	} else {
		$('#applListForm').find("tbody").append(
		"<tr><td colspan='5' align='center'>无记录</td></tr>");
	}


}