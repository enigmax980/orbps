com.orbps.common.saleChannelTableReload =  function (data){
	//重新加载表格
	$('#salesChannelTable').find("tbody").empty();
	if (data !== null && data.length > 0) {
		for (var i = 0; i < data.length; i++) {
			//隐藏table 
			//判断代理员工号，和姓名是否为空
			if(data[i].agencyNo !== "" 
				&& data[i].agencyName !== "" 
					&& data[i].agencyNo !== undefined 
						&& data[i].agencyName !== undefined ){
					//隐藏table 
					$("#salesChannelTable").hide();
					//展示网点和代理员信息。
					$("#salesListForm").show();
					$("#salesDevelopFlagForm").show();
					//赋值
						$("#jointFieldWorkFlag").val(data[0].jointFieldWorkFlag === "Y" ? "是" : "否");//是否有网点代理
						// 转换销售渠道	
						var salesChannel="";
						if(data[i].salesChannel!==""){
							salesChannel = com.orbps.publicSearch.salesChannelQuery(data[i].salesChannel);
						}
						$("#salesChannel").val(data[0].salesChannel);//销售渠道
						$("#salesBranchNo").val(data[0].salesBranchNo);//销售机构号
						$("#worksiteNo").val(data[0].worksiteNo);//代理网点号
						$("#worksiteName").val(data[0].worksiteName);//代理网点名称
						$("#salesBranchNos").val(data[0].salesBranchNo);// 销售人员机构号
						$("#saleCode").val(data[0].saleCode);//销售人员工号
						$("#saleName").val(data[0].saleName);//销售人员姓名
						$("#agencyNo").val(data[0].agencyNo);//代理员工号
						$("#agencyName").val(data[0].agencyName);//代理员姓名
			}else{//代理信息为空
				//销售员信息和网店信息都不为空时，展示以上信息，隐藏代理员姓名和代理员代码
				if(data[i].saleCode !== ""  
					&&  data[i].saleName !== "" 
						&&  data[i].worksiteNo !== "" 
							&& data[i].worksiteName !== ""
								&& data[i].saleCode !== undefined  
									&&  data[i].saleName !== undefined 
										&&  data[i].worksiteNo !== undefined 
											&& data[i].worksiteName !== undefined){
					//隐藏table 
					$("#salesChannelTable").hide();
					//展示网点和代理员信息。
					$("#salesListForm").show();
					$("#salesDevelopFlagForm").show();
					$("#beijing").hide();
					//赋值
					$("#jointFieldWorkFlag").val(data[0].jointFieldWorkFlag === "Y" ? "是" : "否");//是否有网点代理
					// 转换销售渠道	
					var salesChannel="";
					if(data[0].salesChannel!==""){
						salesChannel = com.orbps.publicSearch.salesChannelQuery(data[0].salesChannel);
					}
					$("#salesChannel").val(salesChannel);//销售渠道
					$("#salesBranchNo").val(data[0].salesBranchNo);//销售机构号
					$("#worksiteNo").val(data[0].worksiteNo);//代理网点号
					$("#worksiteName").val(data[0].worksiteName);//代理网点名称
					$("#salesBranchNos").val(data[0].salesBranchNo);// 销售人员机构号
					$("#saleCode").val(data[0].saleCode);//销售人员工号
					$("#saleName").val(data[0].saleName);//销售人员姓名
				}else{
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
						businessPct = data[i].businessPct*100;
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
				
			}
		}
	}else {
		$('#salesChannelTable').find("tbody").append("<tr><td colspan='8' align='center'>无记录</td></tr>");
	}
}
