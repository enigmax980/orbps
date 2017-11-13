
//初始化
com.orbps.otherFunction.contractQuery.reloadSgResponseModalTable = function() {
					  //alert(JSON.stringify(com.orbps.otherFunction.contractQuery.responseList.responseVos))
	var responseVos = com.orbps.otherFunction.contractQuery.responseList.responseVos;
	$("#responseTable").find("tbody").empty();
    if (responseVos != null && responseVos.length > 0) {
    	for (var i = 0; i < responseVos.length; i++) {
    		
			var speciBusinessLogo="";
			if(null !== responseVos[i].speciBusinessLogo && undefined !== responseVos[i].speciBusinessLogo){
				speciBusinessLogo=com.orbps.publicSearch.speciBusinessLogo(responseVos[i].speciBusinessLogo);
			}

    		$('#responseTable').find("tbody")
                    .append("<tr role='row' class='odd' id='responseTabletr0' data-type='add' data-updating='false'><td>" +
                            + responseVos[i].applId
                            + "</td><td >"
                            + responseVos[i].busiPrdCode   
                            + "</td><td >"
                            + responseVos[i].productCode
                            + "</td><td >"
                            + responseVos[i].productName
                            + "</td><td >"
                            + responseVos[i].productNum
                            + "</td><td >"
                            + responseVos[i].productPremium
                            + "</td><td >"
                            + speciBusinessLogo
                            + "</td></tr>"
                            );
        }
    } else {
    	$('#responseTable').find("tbody").append("<tr><td colspan='8' align='center'>无记录</td></tr>");
    }
};

