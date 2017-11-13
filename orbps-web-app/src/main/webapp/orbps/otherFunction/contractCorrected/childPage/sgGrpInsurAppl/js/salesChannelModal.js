com.orbps.common.saleChannelTableReload =  function (data){
	//重新加载表格
	$('#salesChannelTable').find("tbody").empty();
	if (data !== null && data.length > 0) {
		for (var i = 0; i < data.length; i++) {
			// 转换主副标记
			var jointFieldWorkFlag = "";
			if(data[i].jointFieldWorkFlag!==""){
				jointFieldWorkFlag = com.orbps.publicSearch.jointFieldWorkFlag(data[i].jointFieldWorkFlag);
			}
			// 判断是销售人员还是代理网点
			var saleNameOrWorksiteName = "";
			var saleCodeOrWorksiteNo = "";
			if(data[i].salesChannel==="OA"){
				saleCodeOrWorksiteNo = data[i].worksiteNo;
				saleNameOrWorksiteName = data[i].worksiteName;
			}else{
				saleCodeOrWorksiteNo = data[i].saleCode;
				saleNameOrWorksiteName = data[i].saleName;
			}
			// 转换销售渠道	
			var salesChannel="";
			if(data[i].salesChannel!==""){
				salesChannel = com.orbps.publicSearch.salesChannelQuery(data[i].salesChannel);
			}
			// 转换展业比例
			var businessPct="";
			if("businessPct" in data[i]){
				businessPct = data[i].businessPct;
			}
			$('#salesChannelTable').find("tbody")	
			.append("<tr><td >"+(i+1)+"</td><td >"
					+ jointFieldWorkFlag
					+ "</td><td >"
					+ salesChannel
					+ "</td><td >"
					+ data[i].salesBranchNo
					+ "</td><td >"
					+ saleCodeOrWorksiteNo
					+ "</td><td >"
					+ saleNameOrWorksiteName
					+ "</td><td >"
					+ businessPct
					+ "</td></tr>");
		}
	}else {
		$('#salesChannelTable').find("tbody").append("<tr><td colspan='8' align='center'>无记录</td></tr>");
	}
}