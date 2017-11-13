com.common.chargePayCount = 0;
com.common.chargePayType = -1;

// 初始化函数
$(function() {
	//重新加载表格
	com.common.reloadPublicChargePayModalTable = function () { 
		$('#chargePayGroupInfoTable').find("tbody").empty();
		if (com.common.chargePayList !== null && com.common.chargePayList.length > 0) {
			for (var i = 0; i < com.common.chargePayList.length; i++) {
				var bankCode = "";
				if(com.common.chargePayList[i].bankCode!==""){
					bankCode = com.orbps.publicSearch.bankCodeQuery(com.common.chargePayList[i].bankCode);
				}
				var moneyinType = "";
				if(com.common.chargePayList[i].moneyinType!==""){
					moneyinType = com.orbps.publicSearch.moneyinTypeQuery(com.common.chargePayList[i].moneyinType);
				}
				$('#chargePayGroupInfoTable').find("tbody")
						.append("<tr><td >"
								+ com.common.chargePayList[i].groupNo
								+ "</td><td >"
								+ com.common.chargePayList[i].groupName
								+ "</td><td >"
								+ com.common.chargePayList[i].num
								+ "</td><td >"
								+ moneyinType
								+ "</td><td >"
								+ bankCode
								+ "</td><td >"
								+ com.common.chargePayList[i].bankName
								+ "</td><td >"
								+ com.common.chargePayList[i].bankAccNo
								+ "</td></tr>");
			}
		} else {
			$('#chargePayGroupInfoTable').find("tbody").append("<tr><td colspan='7' align='center'>无记录</td></tr>");
		}
	};
});



