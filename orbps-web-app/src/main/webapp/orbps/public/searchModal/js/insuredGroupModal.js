com.orbps.common.proposalGroupCount = 0;
com.orbps.common.reloadProposalGroupType = -1;
com.orbps.common.insuranceCount = 0;
com.orbps.common.reloadInsuranceType = -1;
com.orbps.common.proposalGroupInfoForm = $("#proposalGroupInfoForm");
com.orbps.common.insuranceInfoForm = $("#insuranceInfoForm");
com.orbps.common.proposalGroupVo = {};

//初始化函数
$(function() {
	
	// 点击radio，将其数据回显到录入界面
	com.orbps.common.proposalGroupRadio = function() {
		var radioVal;
		var temp = document.getElementsByName("proposalGroupRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.common.reloadProposalGroupType = radioVal;
		com.orbps.common.proposalGroupVo = com.orbps.common.proposalGroupList[radioVal];
		//回显
		$("input[name='proposalGroupRad']").eq(radioVal).attr("checked","checked");
		// 展示险种信息
		$("#insuranceInfo").show();
		// 判断属性是否存在
		if('insuranceInfoVos' in com.orbps.common.proposalGroupVo){
			com.orbps.common.insuranceList = com.orbps.common.proposalGroupVo.insuranceInfoVos;
			// 重新加载险种表格
			com.orbps.common.reloadInsuranceModalTable();
		}else{
			com.orbps.common.proposalGroupVo.insuranceInfoVos = "";
			com.orbps.common.reloadInsuranceModalTable();
		}
	}
});


//重新加载表格
com.orbps.common.reloadProposalGroupModalTable = function () { 
	$('#ipsnStateGrpList').find("tbody").empty();
	//alert(JSON.stringify(com.orbps.common.proposalGroupList))
	if (com.orbps.common.proposalGroupList !== null && com.orbps.common.proposalGroupList.length > 0) {
		for (var i = 0; i < com.orbps.common.proposalGroupList.length; i++) {
			$('#ipsnStateGrpList').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.common.proposalGroupRadio();' id='proposalGroupRad"
							+ i
							+ "' name='proposalGroupRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.common.proposalGroupList[i].ipsnGrpNo
							+ "</td><td >"
							+ com.orbps.common.proposalGroupList[i].ipsnGrpName
							+ "</td><td >"
							+ com.orbps.common.proposalGroupList[i].ipsnGrpNum
							+ "</td></tr>");
		}
	} else {
		$('#ipsnStateGrpList').find("tbody").append("<tr><td colspan='4' align='center'>无记录</td></tr>");
	}
};


//重新加载表格
com.orbps.common.reloadInsuranceModalTable = function () {
	$('#grpPolicyList').find("tbody").empty();
	if (com.orbps.common.proposalGroupVo.insuranceInfoVos !== null && com.orbps.common.proposalGroupVo.insuranceInfoVos.length > 0) {
		for (var i = 0; i < com.orbps.common.proposalGroupVo.insuranceInfoVos.length; i++) {
			var mrCode = "";
			if("" !== com.orbps.common.proposalGroupVo.insuranceInfoVos[i].mrCode){
				mrCode = com.orbps.publicSearch.mrCodeQuery(com.orbps.common.proposalGroupVo.insuranceInfoVos[i].mrCode);
			}
			
			var  faceAmnt = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].faceAmnt;
			if(faceAmnt === undefined){
				faceAmnt = ""
			}
			var  premium = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].premium;
			if(premium === undefined){
				premium = ""
			}
			var  stdPremium = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].stdPremium;
			if(stdPremium === undefined){
				stdPremium = ""
			}
			
			$('#grpPolicyList').find("tbody")
					.append("<tr><td >"
							+ com.orbps.common.proposalGroupVo.insuranceInfoVos[i].polCode
							+ "</td><td >"
							+ faceAmnt
							+ "</td><td >"
							+ premium
							+ "</td><td >"
							+ stdPremium
							+ "</td></tr>");
		}
	} else {
		$('#grpPolicyList').find("tbody").append("<tr><td colspan='4' align='center'>无记录</td></tr>");
	}
};