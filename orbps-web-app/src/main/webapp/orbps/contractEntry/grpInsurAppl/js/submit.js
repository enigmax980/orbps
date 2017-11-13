com.orbps.common.oranLevelList = new Array();
com.common.insuredList = new Array();
com.orbps.common.zTrees = [];
com.common.chargePayList = new Array();
com.orbps.common.getAddRowsData;
com.orbps.common.proposalGroupList = new Array();
com.orbps.common.insuranceList = new Array();
com.orbps.common.zNodes = [];
com.orbps.common.companyName;
/** 要约险种信息 */
com.orbps.contractEntry.grpInsurAppl.polRowsData
/** 节点名称存储集合 */
com.orbps.common.treeNodeNameList = [];
com.orbps.common.applNo;
com.orbps.common.quotaEaNo;
com.orbps.common.salesBranchNo;
com.orbps.common.salesChannel;
com.orbps.common.worksiteNo;
com.orbps.common.saleCode;
com.orbps.common.occDangerFactor;
com.orbps.common.idNo;
com.orbps.common.idType;
com.orbps.common.numOfEmp;
com.orbps.common.ojEmpNum;
com.orbps.common.appAddrProv;
com.orbps.common.appAddrCity;
com.orbps.common.appAddrTown;
com.orbps.common.appAddrCountry;
com.orbps.common.appAddrValige;
com.orbps.common.appAddrHome;
com.orbps.common.appPost;
com.orbps.common.registerArea;
com.orbps.common.connName;
com.orbps.common.connPhone;
com.orbps.common.connPostcode;
com.orbps.common.appHomeTel;

$(function() {

	// 表单提交
	$("#btnSubmit").click(function() {
		//判断邮编和地区是否对应
		var appPost = $("#appPost").val();
		var city = com.orbps.publicSearch.checkPostCode(appPost);
		var appAddrProv = $("#appAddrProv").val();
		appAddrProv = appAddrProv.substr(0,2);
		if(city !== appAddrProv){
			lion.util.info("提示","邮编输入不正确，请选择[ "+appAddrProv+" ]的对应正确邮编！");
			return false;
		}
//		// 合同打印方式
//		var cntrType = $("#printInfoForm #cntrType").val();
//		if (cntrType === "0") {
//			var connPostcode = $("#applBaseInfoForm #connPostcode").val();
//			if (connPostcode === "") {
//				return false;
//			}
//		}
		// 仲裁机构名称
		var disputePorcWay = $("#applBaseInfoForm #disputePorcWay").val();
		if (disputePorcWay === "2") {
			var arbOrgName = $("#applBaseInfoForm #arbOrgName").val();
			if (arbOrgName === "") {
				lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
				return false;
			}
		}
		// 指定生效时必填
		var forceType = $("#forceType").val();
		if (forceType === "0") {
			$("#inForceDateIs").hide();
			$("#inForceDate").val("");
			$("#inForceDate").attr("disabled", true);
			$("#inForceDateBtn").attr("disabled", true);
		} else if (forceType === "1") {
			$("#inForceDateIs").show();
			$("#inForceDate").attr("disabled", false);
			$("#inForceDateBtn").attr("disabled", false);
		}
		var moneyinTypeFlag = 0;
		// 交费形式
		var moneyinType = $("#payInfoForm #moneyinType").val();
		var stlType = $("#payInfoForm #stlType").val();
		var premFrom = $("#payInfoForm #premFrom").val();
		// 判断组织树是否交费
		var payLevelCount = 0;
		for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
			var pay = com.orbps.common.oranLevelList[i].pay;
			if (pay === "Y") {
				moneyinTypeFlag = 1;
				if (moneyinType !== "") {
					// 如果组织树交费，则交费信息里的交费形式置空
					lion.util.info("提示","组织结构树存在交费节点,交费形式必须为空！")
					return false;
				}
			}else{
				payLevelCount++;
			}
		}
		if (premFrom === "2") {
			lion.util.info("提示","团单保费来源不能是个人账户付款!");
			return false;
		}
		if(payLevelCount===com.orbps.common.oranLevelList.length){
			if(moneyinType===""){
				lion.util.info("组织架构树中没有交费节点,交费形式必录!");
				return false;
			}
		}
		// 交费形式不为现金和银行交款单时，银行相关信息必填
		if ("" !== moneyinType) {
			if (moneyinType === "R" || moneyinType === "C") {

			} else {
				var bankCode = $("#payInfoForm #bankCode").val();
				var bankName = $("#payInfoForm #bankName").val();
				var bankAccNo = $("#payInfoForm #bankAccNo").val();
				if (bankCode === "" || bankName === "" || bankAccNo === "") {
					lion.util.info("提示", "交费形式是【" + moneyinType + "】银行相关信息必填！");
					return false;
				}
			}
		}
		// 判断共保协议号是否必录
		if ("Y" === com.orbps.contractEntry.grpInsurAppl.agreementNoFlag) {
			var agreementNo = $("#applInfoForm #agreementNo").val();
			if ("" === agreementNo) {
				lion.util.info("提示", "共保协议号不能为空!");
				return false;
			}
		}
		// 保费合计
		var sumPrem = $("#proposalInfoForm #sumPrem").val();
		// 校验结算限额
		var stlLimit = $("#payInfoForm #stlLimit").val();
		// 组合结算
		if ("X" === stlType) {
			if ((stlLimit === null || "" === stlLimit)&&(com.orbps.contractEntry.grpInsurAppl.settleList.length <= 0)) {
				lion.util.info("警告", "结算方式为组合结算时，结算限额、结算日期清单至少要填写一项");
				return false;
			}
			if (parseFloat(stlLimit) > parseFloat(sumPrem) || parseFloat(stlLimit) <= 0) {
				lion.util.info("提示", "结算限额应<=总保费" + sumPrem + "并且>0");
				return false;
			}
		}
		// 比例结算
		if ("L" === stlType) {
			// 校验结算比例
			var settlementRatio = $("#payInfoForm #settlementRatio").val();
			if (settlementRatio === null || "" === settlementRatio) {
				lion.util.info("警告", "结算比例不能为空");
				return false;
			}
			if (parseFloat(settlementRatio) > 100 || parseFloat(settlementRatio) <= 0) {
				lion.util.info("警告", "结算比例应<=100%,>0%");
				return false;
			}
		}
		// 指定日期结算
		if ("D" === stlType) {
			if (com.orbps.contractEntry.grpInsurAppl.settleList.length <= 0) {
				lion.util.info("提示", "交费信息的结算方式是指定日期或组合结算,结算日期清单必录");
				return false;
			}
		}else{
			com.orbps.contractEntry.grpInsurAppl.settleList = [];
		}
		//调用校验方法，判断手机号与电话是否都为空
		if (!com.orbps.contractEntry.grpInsurAppl.isPhoneOrTel()) {
			return false;
		}
		// 险种列表所有数据
		com.orbps.contractEntry.grpInsurAppl.polRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		//判断主险种承保人数与被保险人总数是否一致
		var ipsnNum = $("#proposalInfoForm #ipsnNum").val();
		
		if (Object.prototype.toString
				.call(com.orbps.contractEntry.grpInsurAppl.polRowsData) === '[object Array]') {
			if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList.length > 0) {
				for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.polRowsData.length; i++) {
					var busiPrdCode = com.orbps.contractEntry.grpInsurAppl.polRowsData[i].busiPrdCode;
					var insuredNum = com.orbps.contractEntry.grpInsurAppl.polRowsData[i].insuredNum;
					if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList[0].busiPrdCode === busiPrdCode) {
						ipsnNum = parseInt(ipsnNum);
						insuredNum = parseInt(insuredNum);
						if(insuredNum !== ipsnNum){
							lion.util.info("提示","第一主险的承保人数与被保险人总数不一致");
							return false;
						}
					}
				}
			}
		}
		// 所有险种和责任集合
		var polCodeResponseAll = [];
		for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.polRowsData.length; i++) {
			var array_element = com.orbps.contractEntry.grpInsurAppl.polRowsData[i].busiPrdCode;
			polCodeResponseAll.push(array_element);
		}
		for (var j = 0; j < com.orbps.common.list.length; j++) {
			var array_element = com.orbps.common.list[j].productCode;
			polCodeResponseAll.push(array_element);
		}			
		for (var j = 0; j < com.orbps.common.proposalGroupList.length; j++) {
			var insuranceInfoVos = com.orbps.common.proposalGroupList[j].insuranceInfoVos;
			if(insuranceInfoVos === undefined || insuranceInfoVos.length === 0){
				lion.util.info("提示","被保人分组中的要约信息必录。请检查。");
				return false;
			}
			for (var k = 0; k < insuranceInfoVos.length; k++) {
				var polCode = insuranceInfoVos[k].polCode;
				if(polCodeResponseAll.indexOf(polCode)==-1){
					lion.util.info("提示:【"+polCode+"】险种已经被删除，请去被保人分组中删除含有该险种的分组！");
					return false;
				}
			}
		}
		
		// jquery validate 校验
		if (com.orbps.contractEntry.grpInsurAppl.vatInfoForm.validate().form()
				&& com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm.validate().form()
				&& com.orbps.contractEntry.grpInsurAppl.proposalInfoForm.validate().form()
				&& com.orbps.contractEntry.grpInsurAppl.payInfoForm.validate().form()
				&& com.orbps.contractEntry.grpInsurAppl.printInfoForm.validate().form()) {
			//检查险种种险种是否需要录入公共保额字段。
		  if(com.orbps.contractEntry.grpInsurAppl.checkResponse()){
			  // 供款比例和供款金额不能为同时为空------------暂时取消多方供款验证功能
			if (premFrom === "3") {
				var multiPartyScale = $("#multiPartyScale").val();
				var multiPartyMoney = $("#multiPartyMoney").val();
				if (("" === multiPartyScale || null === multiPartyScale)
						&& ("" === multiPartyMoney || null === multiPartyMoney)) {
					lion.util.info("警告", "供款比例和供款金额不能为同时为空");
					return false;
				}
			}
			// 判断保费来源是否是团体交费，是团体交费则去判断所有节点缴费和总保费是否相等
			if (premFrom === "1") {
				if (com.orbps.common.oranLevelList.length > 0) {
					var nodePayAmnt = 0;
					// 校验组织架构树
					for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
						if ("nodePayAmnt" in com.orbps.common.oranLevelList[i]) {
							nodePayAmnt = parseFloat(nodePayAmnt
									+ parseFloat(com.orbps.common.oranLevelList[i].nodePayAmnt));
						}
					}
					sumPrem = parseFloat(sumPrem);
					if (nodePayAmnt !== sumPrem) {
						if (nodePayAmnt !== 0) {
							lion.util.info("警告","保费合计与节点所有缴费节点的总保费不相等");
							return false;
						}
					}
				}
			}
			// 联系人移动电话不能为空
			var connPhone = $("#connPhone").val();
//			var appHomeTel = $("#appHomeTel").val();
			if (connPhone === "" ) {
				lion.util.info("警告", "联系人移动电话不能为空");
				return false;
			}
			if ("M" === $("#ipsnlstId").val()) {
				lion.util.info("警告", "该保单不支持事后补录清单标记，请检查！");
				return false;
			}
			// 根据保费收取方式校验总造价和总面积
			var cpnstMioType = $("#specialInsurAddInfoForm #cpnstMioType").val();
			if ("S" === cpnstMioType) {
				var totalArea = $("#specialInsurAddInfoForm #totalArea").val();
				if (totalArea === "") {
					lion.util.info("提示", "总面积必录");
					return false;
				}
			}
			if ("B" === cpnstMioType) {
				var totalCost = $("#specialInsurAddInfoForm #totalCost").val();
				if (totalCost === "") {
					lion.util.info("提示", "总造价必录");
					return false;
				}
			}
			// 判断投保人数和要约分组中的人数是否一致
			if (com.orbps.common.proposalGroupList.length > 0) {
				var applNum = 0;
				for (var i = 0; i < com.orbps.common.proposalGroupList.length; i++) {
					var array_element = com.orbps.common.proposalGroupList[i].ipsnGrpNum;
					applNum = parseInt(array_element) + applNum;
				}

				if (applNum !== parseInt($("#applNum").val())) {
					lion.util.info("警告","被保人分组中的要约数组人数和投保人数不一致，请检查！");
					return false;
				}
			}
			// select校验
			if (com.orbps.contractEntry.grpInsurAppl.validateSelectVal()) {
				// 获取要约信息下面的所有险种信息
				var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
				var busiPrdCodes = [];
				//取到当前的第一个险种的保险期间类型
				var insurDurUnitOne = getAddRowsData[0].insurDurUnit;
				if (Object.prototype.toString.call(getAddRowsData) === '[object Array]') {
					for (var i = 0; i < getAddRowsData.length; i++) {
						var insurDur = getAddRowsData[i].insurDur;
						var busiPrdCode = getAddRowsData[i].busiPrdCode;
						var insuredNum = getAddRowsData[i].insuredNum;
						var insurDurUnit = getAddRowsData[i].insurDurUnit;
						if(lion.util.isEmpty(insurDur)){
							lion.util.info("提示:【"+busiPrdCode+"】险种的保险期间不能为空,请补填");
							return false;
						}
						if(lion.util.isEmpty(insuredNum)){
							lion.util.info("提示:【"+busiPrdCode+"】险种的被保险人数不能为空,请补填");
							return false;
						}
						if(lion.util.isEmpty(insurDurUnit)){
							lion.util.info("提示:【"+busiPrdCode+"】险种的保险期间类型不能为空,请补填");
							return false;
						}
					}
					for (var i = 0; i < getAddRowsData.length; i++) {
						var busiPrdCode = getAddRowsData[i].busiPrdCode;
						busiPrdCodes.push(busiPrdCode);
						if(getAddRowsData[i].insurDurUnit !== insurDurUnitOne){
							lion.util.info("警告","险种信息的保险期间类型必须要和主险的保险期间类型保持一致，请检查！");
							return false;
						}
					}
					var s = busiPrdCodes.join(",") + ",";
					for (var i = 0; i < busiPrdCodes.length; i++) {
						if (s.replace(busiPrdCodes[i] + ",","").indexOf(busiPrdCodes[i] + ",") > -1) {
							lion.util.info("提示", "【" + busiPrdCodes[i] + "】险种重复,请修改一个重复的险种代码");
							return false;
						}
					}
				}
				// 定义团单大对象
				var grpInsurApplVo = {};
				// 获取所有团体提交的投保人数
				var applNum = parseInt($("#applNum").val());
				for (var j = 0; j < getAddRowsData.length; j++) {
					if (lion.util.isNotEmpty(getAddRowsData[j].busiPrdCodeName)) {
						// 承保人数
						var sum = 0;
						// 获取险种的承保人数
						sum = parseInt(getAddRowsData[j].insuredNum);
						// 判断承保人数和被保人总数是否相等。
						if (sum > applNum) {
							lion.util.info("警告","险种的承保人数和投保人数不一致，请检查！");
							return false;
						}
					}
				}
				// 判断要约险种中是否有建工险，如果有，需要校验建工险信息。
				if (Object.prototype.toString.call(getAddRowsData) === '[object Array]') {
					for (var i = 0; i < getAddRowsData.length; i++) {
						var busiprdCodeTable = getAddRowsData[i].busiPrdCode;
						for (var j = 0; j < com.orbps.contractEntry.grpInsurAppl.constructionInfoArray.length; j++) {
							var array_element = com.orbps.contractEntry.grpInsurAppl.constructionInfoArray[j];
							// 判断主险是否是建筑工程险种
							if (busiprdCodeTable === array_element) {
								// 判断建工信息施工天数为负
								if ($("#totalDays").val() < 0) {
									lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
									return false;
								}
								i = getAddRowsData.length;
								// 进行建工险校验
								if (com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.validate().form()
										&& com.orbps.contractEntry.grpInsurAppl.specialValidateSelectVal()) {
									grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
								} else {
									lion.util.info("提示","建工险信息输入不正确，请检查");
									return false;
								}
								break;
							}
						}
					}
				} else {
					if (lion.util.isNotEmpty(getAddRowsData.busiPrdCode)) {
						var busiprdCodeTable = getAddRowsData.busiPrdCode;
						for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.constructionInfoArray.length; i++) {
							var array_element = com.orbps.contractEntry.grpInsurAppl.constructionInfoArray[i];
							if (busiprdCodeTable === array_element) {
								// 判断建工信息施工天数为负
								if ($("#totalDays").val() < 0) {
									lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
									return false;
								}
								// 进行建工险校验
								if (com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.validate().form()
										&& com.orbps.contractEntry.grpInsurAppl.specialValidateSelectVal()) {
									grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
								} else {
									lion.util.info("提示","建工险信息输入不正确，请检查");
									return false;
								}
							}
						}
					}
				}
				
				// 判断要约险种中是否有健康险公共保额信息，如果有，需要校验健康险公共保额信息。
				if (Object.prototype.toString.call(getAddRowsData) === '[object Array]') {
					for (var i = 0; i < getAddRowsData.length; i++) {
						var busiprdCodeTable = getAddRowsData[i].busiPrdCode;
						for (var j = 0; j < com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray.length; j++) {
							var array_element = com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray[j];
							// 判断主险是否是健康险公共保额
							if (busiprdCodeTable === array_element) {
								// 进行健康险公共保额校验
								if (com.orbps.contractEntry.grpInsurAppl.healthPublicAmountValidate()) {

								} else {
									lion.util.info("提示","健康险公共保额信息输入不正确，请检查");
									return false;
								}
								break;
							}
						}
						for (var k = 0; k < com.orbps.contractEntry.grpInsurAppl.constructionInfoArray.length; k++) {
							var array_element = com.orbps.contractEntry.grpInsurAppl.constructionInfoArray[k];
							// 判断主险是否是建筑工程险种
							if (busiprdCodeTable === array_element) {
								// 判断建工信息施工天数为负
								if ($("#totalDays").val() < 0) {
									lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
									return false;
								}
								// 进行建工险校验
								if (com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.validate().form()
										&& com.orbps.contractEntry.grpInsurAppl.specialValidateSelectVal()) {
								
								} else {
									lion.util.info("提示","建工险信息输入不正确，请检查");
									return false;
								}
								break;
							}
						}
					}
				} else {
					var busiprdCodeTable = getAddRowsData.busiPrdCode;
					if (lion.util.isNotEmpty(getAddRowsData.busiPrdCode)) {
						for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray.length; i++) {
							var array_element = com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray[i];
							if (busiprdCodeTable === array_element) {
								// 进行健康险信息校验
								if (com.orbps.contractEntry.grpInsurAppl.healthPublicAmountValidate()) {

								} else {
									lion.util.info("提示","健康险信息输入不正确，请检查");
									return false;
								}
							}
						}
					}
					for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.constructionInfoArray.length; i++) {
						var array_element = com.orbps.contractEntry.grpInsurAppl.constructionInfoArray[i];
						if (busiprdCodeTable === array_element) {
							// 判断建工信息施工天数为负
							if ($("#totalDays").val() < 0) {
								lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
								return false;
							}
							// 进行建工险校验
							if (com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.validate().form()
									&& com.orbps.contractEntry.grpInsurAppl.specialValidateSelectVal()) {

							} else {
								lion.util.info("提示","建工险信息输入不正确，请检查");
								return false;
							}
						}
					}
				}
				
				/**
				*判断险种中的是否有基金险，有基金险要校验一些逻辑
				*/
				if(com.orbps.contractEntry.grpInsurAppl.FundInsurFlag === "Y"){
					//管理费提取方式、基金险管理费比例、个人与公共账户总保费与总保额的非空判断
					var adminCalType = $("#adminCalType").val();//管理费提取方式
					var ipsnAccAmnt = $("#ipsnAccAmnt").val();//个人账户金额 
					var sumPubAccAmnt = $("#sumPubAccAmnt").val();//公共账户交费金额
					var adminPcent = $("#adminPcent").val();//管理费比例
					var adminCalType = $("#adminCalType").val();//管理费提取方式
					if(adminCalType === "" || ipsnAccAmnt === "" || sumPubAccAmnt === "" ||  adminPcent === "" || adminCalType === ""){
						lion.util.info("险种中有基金险，请检查基金险录入信息是否录入完整！"); 
						return false;
					}
					//被保人是否分组的判断，基金险必须做被保人分组。
					if(com.orbps.common.proposalGroupList.length < 1 ){
						lion.util.info("险种中有基金险，被保人分组必录！"); 
						return false;
					}
					//基金险不能是档案清单
					var ipsnlstId = $("#ipsnlstId").val();
					if(ipsnlstId === "A"){
						lion.util.info("提示","基金险暂不支持档案清单！");
						return false;
					}
					// 获取要约信息下面的所有险种信息
					var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
					if(getAddRowsData.length > 1){
						lion.util.info("提示","基金险不能录入多险种,请检查！");
						return false;
					}
					//对总保费、总保额与个人、公共账户保额、保费累计之和进行校验，也就是总保费=个人账户总保费+公共账户总保费，总保额=个人账户总保额+公共账户总保额
					var amount = 0;
					var premium = 0;
					for (var i = 0; i < getAddRowsData.length; i++) {
						 amount = parseInt(getAddRowsData[i].amount)+amount;//总保额
						 premium = parseInt(getAddRowsData[i].premium)+premium;//总保费
					}
					//总保额校验
					var inclIpsnAccAmnt = parseInt($("#inclIpsnAccAmnt").val());//计入个人账户金额 
					var inclSumPubAccAmnt = parseInt($("#inclSumPubAccAmnt").val());//计入公共账户金额；
					if(amount !== inclIpsnAccAmnt + inclSumPubAccAmnt){
						lion.util.info("基金险信息个人帐户总保额+基金险公共帐户总保额不等于险种总保额");
						return false;
					}
					//总保费校验
					if(premium !== parseInt(ipsnAccAmnt)+parseInt(sumPubAccAmnt)){
						lion.util.info("基金险信息个人帐户总保费+基金险公共帐户总保费不等于险种总保费");
						return false;
					}
					//判断保费来源如果不是1，提示错误。
					if($("#premFrom").val() !== "1"){
						lion.util.info("基金险种的保费来源只能是团体付款....");
						return false;
					}
				}
				
				//判断被保人分组信息是否正确
				if(com.orbps.common.proposalGroupList.length>0){
					if("" === $("#printInfoForm #groupType").val()){
						lion.util.info("有被保人分组时，分组类型不能为空");
						return false;
					}
				}else{
					if("" !== $("#printInfoForm #groupType").val()){
						lion.util.info("没有被保人分组时，分组类型必须为空");
						return false;
					}
				}
				var amount = getAddRowsData[0].amount;//保额
				var premium =  getAddRowsData[0].premium;//保费
				if(amount === undefined || premium === undefined || amount === "" || premium === ""){
					lion.util.info("提示","请填写 "+getAddRowsData[0].busiPrdCode+" 险种中的保额或者保费。");
					return false;
				}
				var commPremium = $("#specialInsurAddInfoForm #commPremium").val();//固定保费
				var fixedComAmnt = $("#specialInsurAddInfoForm #fixedComAmnt").val();//固定保额
				var comInsurAmntUse = $("#specialInsurAddInfoForm #comInsurAmntUse").val();//公共保额使用范围
				var comInsurAmntType = $("#specialInsurAddInfoForm #comInsurAmntType").val();//公共保额类型
				//公共保额使用范围 当公共保额选择了就说明这个录入了公共保额信息。
				if(comInsurAmntUse !== "0" && comInsurAmntUse !== undefined && "" !== comInsurAmntUse){
						//如果是档案清单和无清单不允许录入共保信息。
						if($("#ipsnlstId").val() === "A" || $("#ipsnlstId").val() === "N"){
							lion.util.info("提示","团单清单标记是档案清单或者是无清单时，不能录入公共保额信息。");
							return false;
						}
						//判断公共保额和保费是否是空，是空的话就说明公共保额和保费是比第一主险中的小。
						if(commPremium !== "" || fixedComAmnt !== ""){
							//浮动的保费比险种的保费小 或者固定的保费和保额比主险中要小。
							if(parseFloat(commPremium) >= parseFloat(premium) || parseFloat(fixedComAmnt) >= parseFloat(amount)){
								lion.util.info("提示","公共保额，保费必须要累加到第一主险的保额，保费上。");
								return false;
							}
						}
				}
				grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
				// 提交方法
				grpInsurApplVo.applInfoVo = com.orbps.contractEntry.grpInsurAppl.applInfoForm.serializeObject();
				grpInsurApplVo.vatInfoVo = com.orbps.contractEntry.grpInsurAppl.vatInfoForm.serializeObject();
				grpInsurApplVo.applBaseInfoVo = com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm.serializeObject();
				grpInsurApplVo.proposalInfoVo = com.orbps.contractEntry.grpInsurAppl.proposalInfoForm.serializeObject();
				grpInsurApplVo.payInfoVo = com.orbps.contractEntry.grpInsurAppl.payInfoForm.serializeObject();
				grpInsurApplVo.payInfoVo.settlementDate = com.orbps.contractEntry.grpInsurAppl.settleList;
				grpInsurApplVo.printInfoVo = com.orbps.contractEntry.grpInsurAppl.printInfoForm.serializeObject();
				grpInsurApplVo.insuredGroupModalVos = com.orbps.common.proposalGroupList;
				if(($("#comInsurAmntUse").val() === "1" || $("#comInsurAmntUse").val() === "2") && com.orbps.common.oranLevelList.length !== 0){
					if(confirm("健康险公共保额与组织架构树不能同时存在，此时提交会删除组织架构树信息，是否继续？")){
						com.orbps.common.oranLevelList = new Array();
					}else{
						return false;
					}
				}
				grpInsurApplVo.organizaHierarModalVos = com.orbps.common.oranLevelList;
				// 循环获取的全部主险
				for (var i = 0; i < getAddRowsData.length; i++) {
					var busiPrdVos = getAddRowsData[i];
					// 清空当前险种下的责任信息
					busiPrdVos.responseVos = [];
					// 循环责任的list，如果责任里的busiPrdCode和主险下的一样，把责任信息放到险种下的responseVos里
					for (var j = 0; j < com.orbps.common.list.length; j++) {
						// 取到的责任信息
						var reponseVos = com.orbps.common.list[j];
						// 当险种的busiPrdCode等于责任的时候，往险种下的责任list中放值
						if (reponseVos.busiPrdCode === busiPrdVos.busiPrdCode) {
							busiPrdVos.responseVos.push(reponseVos);
						}
					}
				}
				grpInsurApplVo.busiPrdVos = getAddRowsData;
				grpInsurApplVo.chargePayGroupModalVos = com.common.chargePayList;
				// 声明taskInfo对象
				grpInsurApplVo.taskInfo = {};
				grpInsurApplVo.taskInfo.taskId = com.orbps.contractEntry.grpInsurAppl.taskId;
				lion.util.postjson(
								'/orbps/web/orbps/contractEntry/grp/submit',
								grpInsurApplVo,
								com.orbps.contractEntry.grpInsurAppl.successSubmit,
								null, null);
			}
		 }
		} else {
			if (!com.orbps.contractEntry.grpInsurAppl.applInfoForm.validate().form()) {
				lion.util.info("提示", "投保单信息输入不正确，请检查");
			}
			if (!com.orbps.contractEntry.grpInsurAppl.vatInfoForm.validate().form()) {
				lion.util.info("提示", "增值税信息输入不正确，请检查");
			}
			if (!com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm.validate().form()) {
				lion.util.info("提示", "保单基本信息输入不正确，请检查");
			}
			if (!com.orbps.contractEntry.grpInsurAppl.proposalInfoForm.validate().form()) {
				lion.util.info("提示", "要约信息输入不正确，请检查");
			}
			if (!com.orbps.contractEntry.grpInsurAppl.payInfoForm.validate().form()) {
				lion.util.info("提示", "交费信息输入不正确，请检查");
			}
			if (!com.orbps.contractEntry.grpInsurAppl.printInfoForm.validate().form()) {
				lion.util.info("提示", "其他信息输入不正确，请检查");
			}
		}
	});
	// 被保人分组
	$("#btnGoup").click(function() {
		if("" === $("#groupType").val()){
			lion.util.info("请先选择分组类型");
			return false;
		}
		com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
		com.orbps.contractEntry.grpInsurAppl.addDialog.load(
			"/orbps/orbps/public/modal/html/insuredGroupModal.html",
			function() {
				$(this).modal('toggle');
				$(this).comboInitLoad();
		});
		com.orbps.common.getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		setTimeout(function() {
			// 回显table
			com.orbps.common.reloadProposalGroupModalTable();
		}, 100);
	});

	// 组织层次架构
	$("#btnOranLevel").click(function() {
		//如果公共保额使用范围为包括连带被保险人或不包括连带被保险人，不支持录入组织架构书
		if($("#comInsurAmntUse").val() === "1" || $("#comInsurAmntUse").val() === "2"){
			lion.util.info("提示","健康险公共保额暂不支持组织架构树");
			return false;
		}
		
		//如果清单标记是档案清单，不支持录入组织架构树信息。
		if($("#ipsnlstId").val() === "A"){
			lion.util.info("提示","团单清单标记是档案清单，暂不支持录入组织架构树信息");
			return false;
		}
		//如果是基金险账户，组织架构树不能录入。
		if(com.orbps.contractEntry.grpInsurAppl.FundInsurFlag === "Y"){
			lion.util.info("提示","基金险账户不能录入组织架构。");
			return false;
		}
		// 单位/团体名称
		var companyName = $("#applBaseInfoForm #companyName").val();
		com.orbps.common.companyName = companyName;
		// 投保日期
		var applDate = $("#applInfoForm #applDate").val();
		// 行业类别
		var occDangerFactor = $("#applBaseInfoForm #occDangerFactor").val();
		com.orbps.common.occDangerFactor = occDangerFactor;
		// 证件类型
		var idType = $("#applBaseInfoForm #idType").val();
		com.orbps.common.idType = idType;
		// 证件号码
		var idNo = $("#applBaseInfoForm #idNo").val();
		com.orbps.common.idNo = idNo;
		// 企业员工总数
		var numOfEmp = $("#applBaseInfoForm #numOfEmp").val();
		com.orbps.common.numOfEmp = numOfEmp;
		// 在职人数
		var ojEmpNum = $("#applBaseInfoForm #ojEmpNum").val();
		com.orbps.common.ojEmpNum = ojEmpNum;
		// 省
		var appAddrProv = $("#applBaseInfoForm #appAddrProv").val();
		com.orbps.common.appAddrProv = appAddrProv;
		// 市
		var appAddrCity = $("#applBaseInfoForm #appAddrCity").val();
		com.orbps.common.appAddrCity = appAddrCity;
		// 县
		var appAddrCountry = $("#applBaseInfoForm #appAddrCountry").val();
		com.orbps.common.appAddrCountry = appAddrCountry;
		// 乡
		var appAddrTown = $("#applBaseInfoForm #appAddrTown").val();
		com.orbps.common.appAddrTown = appAddrTown;
		// 村
		var appAddrValige = $("#applBaseInfoForm #appAddrValige").val();
		com.orbps.common.appAddrValige = appAddrValige;
		// 详细地址
		var appAddrHome = $("#applBaseInfoForm #appAddrHome").val();
		com.orbps.common.appAddrHome = appAddrHome;
		// 邮编
		var appPost = $("#applBaseInfoForm #appPost").val();
		com.orbps.common.appPost = appPost;
		// 企业注册地
		var registerArea = $("#applBaseInfoForm #registerArea").val();
		com.orbps.common.registerArea = registerArea;
		// 联系人姓名
		var connName = $("#applBaseInfoForm #connName").val();
		com.orbps.common.connName = connName;
		// 联系人手机
		var connPhone = $("#applBaseInfoForm #connPhone").val();
		com.orbps.common.connPhone = connPhone;
		// 联系人邮箱
		var connPostcode = $("#applBaseInfoForm #connPostcode").val();
		com.orbps.common.connPostcode = connPostcode;
		// 联系人固定电话
		var appHomeTel = $("#applBaseInfoForm #appHomeTel").val();
		com.orbps.common.appHomeTel = appHomeTel;
		if (numOfEmp !== "" && occDangerFactor !== ""
				&& idNo !== "" && idType !== ""
				&& companyName !== "" && applDate !== "") {
			com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
			com.orbps.contractEntry.grpInsurAppl.addDialog.load(
				"/orbps/orbps/public/modal/html/organizaHierarModal.html",
				function() {
					$(this).modal('toggle');
					// combo组件初始化
					$(this).comboInitLoad();
					com.orbps.common.grpInsurApplVo.applInfoVo = com.orbps.contractEntry.grpInsurAppl.applInfoForm
							.serializeObject();
					com.orbps.common.grpInsurApplVo.applInfoVo.grpSalesListFormVos = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos;
					com.orbps.common.grpInsurApplVo.applBaseInfoVo = com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm
							.serializeObject();
					console.log("点击modal的noes"+ JSON.stringify(com.orbps.common.zTrees));
					if (com.orbps.common.zTrees.length === 1) {
						com.orbps.common.zTrees[0].name = companyName;
					}
					// zTree初始化
					var zTree = $.fn.zTree.init($("#treeDemo"),setting,com.orbps.common.zTrees);
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var nodeSel = treeObj.getNodeByParam("id", 1);
					treeObj.selectNode(nodeSel);
					var node = treeObj.getNodes();
					var nodes = treeObj.transformToArray(node);
					for (var i = 0; i < nodes.length; i++) {
						var array_element = nodes[i].name;
						com.orbps.common.treeNodeNameList.push(array_element);
					}
				});
		} else {
			lion.util.info("提示", "请先录入投保单信息和保单基本信息再添加组织架构树！");
			return false;
		}
	});
	// 收付费分组
	$("#btnChargePay").click(function() {
		if($("#premFrom").val() === ""){
			lion.util.info("提示","请先选择保费来源，在录入收付费信息。");
			return false;
		}
		if($("#premFrom").val() === "1" ){
			lion.util.info("提示","当保费来源是团体付款时，不能录入收付费信息。");
			return false;
		}
		com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
		com.orbps.contractEntry.grpInsurAppl.addDialog.load(
			"/orbps/orbps/public/modal/html/chargePayGroupModal.html",function() {
			$(this).modal('toggle');
			// combo组件初始化
			$(this).comboInitLoad();
		});
		setTimeout(function() {
			com.common.reloadPublicChargePayModalTable();
		}, 600);
	});

	// 影像信息查询
	$("#imageQuery").click(function() {
		var salesBranchNo ="";
		if(com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos.length===1){
			salesBranchNo = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[0].salesBranchNo;
			salesChannel = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[0].salesChannel;
			worksiteNo = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[0].worksiteNo;
			saleCode = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[0].saleCode;
		}else{
			for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos.length; i++) {
				var jointFieldWorkFlag = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[i].jointFieldWorkFlag;
				if(jointFieldWorkFlag==="Y"){
					salesBranchNo = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[i].salesBranchNo;
					salesChannel = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[i].salesChannel;
					worksiteNo = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[i].worksiteNo;
					saleCode = com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos[i].saleCode;
					break;
				}
			}
		}
		com.orbps.common.applNo = $("#applInfoForm #applNo").val();
		com.orbps.common.quotaEaNo = $("#applInfoForm #quotaEaNo").val();
		if (salesBranchNo !== "") {
			com.orbps.common.salesBranchNo = salesBranchNo;
		}
		com.orbps.common.salesChannel = salesChannel;
		com.orbps.common.worksiteNo = worksiteNo;
		com.orbps.common.saleCode = saleCode;
		com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
		com.orbps.contractEntry.grpInsurAppl.addDialog.load(
			"/orbps/orbps/public/modal/html/imageCollection.html",
			function() {
				$(this).modal('toggle');
				$(this).comboInitLoad();
		});
	});

	// 建工险选择信息的校验
	com.orbps.contractEntry.grpInsurAppl.specialValidateSelectVal = function() {
		var projLocType = $("#specialInsurAddInfoForm #projLocType").val();
		if (projLocType === null || "" === projLocType) {
			lion.util.info("警告", "请选择工程位置类别");
			return false;
		}
		var projectType = $("#specialInsurAddInfoForm #projectType").val();
		if (projectType === null || "" === projectType) {
			lion.util.info("警告", "请选择工程类型");
			return false;
		}
		var cpnstMioType = $("#specialInsurAddInfoForm #cpnstMioType").val();
		if (cpnstMioType === null || "" === cpnstMioType) {
			lion.util.info("警告", "请选择保费收取方式");
			return false;
		}
		var constructionDur = $("#specialInsurAddInfoForm #constructionDur").val();
		if (constructionDur === null || "" === constructionDur) {
			lion.util.info("警告", "请选择施工期间 ");
			return false;
		}
		var until = $("#specialInsurAddInfoForm #until").val();
		if (until === null || "" === until) {
			lion.util.info("警告", "请选择施工期间 ");
			return false;
		}
		var enterpriseLicence = $("#specialInsurAddInfoForm #enterpriseLicence")
				.val();
		if (enterpriseLicence === null || "" === enterpriseLicence) {
			lion.util.info("警告", "请选择企业资质");
			return false;
		}
		var awardGrade = $("#specialInsurAddInfoForm #awardGrade").val();
		if (awardGrade === null || "" === awardGrade) {
			lion.util.info("警告", "请选择获奖情况");
			return false;
		}
		var safetyFlag = $("#specialInsurAddInfoForm #safetyFlag").val();
		if (safetyFlag === null || "" === safetyFlag) {
			lion.util.info("警告", "请选择是否有安防措施");
			return false;
		}
		var saftyAcdntFlag = $("#specialInsurAddInfoForm #saftyAcdntFlag")
				.val();
		if (saftyAcdntFlag === null || "" === saftyAcdntFlag) {
			lion.util.info("警告", "请选择过去二年内是否有四级以上安全事故");
			return false;
		}
		return true;
	}

	// 健康险公共保额信息的校验
	com.orbps.contractEntry.grpInsurAppl.healthPublicAmountValidate = function() {
		//健康险公共保额相关规则未梳理，暂时不做校验
//		var comInsurAmntType = $("#specialInsurAddInfoForm #comInsurAmntType").val();
//		var commPremium = $("#specialInsurAddInfoForm #commPremium").val();
//		var fixedComAmnt = $("#specialInsurAddInfoForm #fixedComAmnt").val();
//		var ipsnFloatAmnt = $("#specialInsurAddInfoForm #ipsnFloatAmnt").val();
//		var ipsnFloatPct = $("#specialInsurAddInfoForm #ipsnFloatPct").val();
//		if(comInsurAmntType==="0"){
//			if(commPremium===""||fixedComAmnt===""){
//				lion.util.info("提示","健康险公共保额类型选择固定公共保额时,公共保费、固定公共保额必填");
//				return false;
//			}
//		}else if(comInsurAmntType==="1"){
//			if(commPremium===""){
//				lion.util.info("提示","健康险公共保额类型选择浮动公共保额时,公共保费必填");
//				return false;
//			}
//			if(ipsnFloatAmnt===""&&ipsnFloatPct===""){
//				lion.util.info("提示","健康险公共保额类型选择浮动公共保额时,人均浮动公共保额、人均浮动比例至少要填一项");
//				return false;
//			}
//		}
//		if (parseFloat(ipsnFloatPct) > 100 || parseFloat(ipsnFloatPct) <= 0) {
//			lion.util.info("警告", "健康险人均浮动比例应<=100%,>0%");
//			return false;
//		}
		return true;
	}

	// 校验选择信息
	com.orbps.contractEntry.grpInsurAppl.validateSelectVal = function() {
		var applDate = $("#applInfoForm #applDate").val();
		if (applDate === null || "" === applDate) {
			lion.util.info("警告", "请选择投保申请日期");
			return false;
		}
		var occDangerFactor = $("#applBaseInfoForm #occDangerFactor").val();
		if (occDangerFactor === null || "" === occDangerFactor) {
			lion.util.info("警告", "请选择职业类别");
			return false;
		}
		var registerArea = $("#applBaseInfoForm #registerArea").val();
		if (registerArea === null || "" === registerArea) {
			lion.util.info("警告", "请选择企业注册地");
			return false;
		}
		var deptType = $("#applBaseInfoForm #deptType").val();
		if (deptType === null || "" === deptType) {
			lion.util.info("警告", "请选择部门类型");
			return false;
		}

		var idType = $("#applBaseInfoForm #idType").val();
		if (idType === null || "" === idType) {
			lion.util.info("警告", "请选择证件类型");
			return false;
		}
		var appAddrProv = $("#applBaseInfoForm #appAddrProv").val();
		if (appAddrProv === null || "" === appAddrProv) {
			lion.util.info("警告", "请选择省/直辖市 ");
			return false;
		}
		var appAddrCity = $("#applBaseInfoForm #appAddrCity").val();
		if (appAddrCity === null || "" === appAddrCity) {
			lion.util.info("警告", "请选择市/城区");
			return false;
		}
		var appAddrCountry = $("#applBaseInfoForm #appAddrCountry").val();
		if (appAddrCountry === null || "" === appAddrCountry) {
			lion.util.info("警告", "请选择县/地级市");
			return false;
		}
		var connIdType = $("#applBaseInfoForm #connIdType").val();
		if (connIdType === null || "" === connIdType) {
			lion.util.info("警告", "请选择联系人证件类型");
			return false;
		}
		var disputePorcWay = $("#applBaseInfoForm #disputePorcWay").val();
		if (disputePorcWay === null || "" === disputePorcWay) {
			lion.util.info("警告", "请选择争议处理方式");
			return false;
		}
		
		var forceType = $("#proposalInfoForm #forceType").val();
		if (forceType === null || "" === forceType) {
			lion.util.info("警告", "请选择生效方式");
			return false;
		}
		
		if("1"===forceType){
			var inForceDate = $("#proposalInfoForm #inForceDate").val();
			if (inForceDate === null || "" === inForceDate) {
				lion.util.info("警告", "请录入指定生效日");
				return false;
			}
		}
		
		var moneyinItrvl = $("#payInfoForm #moneyinItrvl").val();
		if (moneyinItrvl === null || "" === moneyinItrvl) {
			lion.util.info("警告", "请选择交费方式");
			return false;
		}

		var premFrom = $("#payInfoForm #premFrom").val();
		if (premFrom === null || "" === premFrom) {
			lion.util.info("警告", "请选择保费来源");
			return false;
		}

		var stlType = $("#payInfoForm #stlType").val();
		if (stlType === null || "" === stlType) {
			lion.util.info("警告", "请选择结算方式");
			return false;
		}

		var cntrType = $("#printInfoForm #cntrType").val();
		if (cntrType === null || "" === cntrType) {
			lion.util.info("警告", "请选择合同打印方式");
			return false;
		}

		var ipsnlstId = $("#printInfoForm #ipsnlstId").val();
		if (ipsnlstId === null || "" === ipsnlstId) {
			lion.util.info("警告", "请选择团单清单标记");
			return false;
		} else if (ipsnlstId === "N"
				&& $("#payInfoForm #premFrom").val() !== "1") {
			lion.util.info("警告", "团体清单标记选择“无清单”时保费来源为“团体账户付款”！");
			return false;
		}

		// 团单清单标记不为无清单时候 清单打印和赠送险标记必录
		if ("N" === ipsnlstId) {
		} else {
			var prtIpsnLstType = $("#printInfoForm #prtIpsnLstType").val();
			if (prtIpsnLstType === null || "" === prtIpsnLstType) {
				lion.util.info("警告", "请选择清单打印");
				return false;
			}

			var ipsnVoucherPrt = $("#printInfoForm #ipsnVoucherPrt").val();
			if (ipsnVoucherPrt === null || "" === ipsnVoucherPrt) {
				lion.util.info("警告", "请选择个人凭证打印");
				return false;
			}
		}

		var giftFlag = $("#printInfoForm #giftFlag").val();
		if (giftFlag === null || "" === giftFlag) {
			lion.util.info("警告", "请选择赠送险标记");
			return false;
		}

		var applProperty = $("#printInfoForm #applProperty").val();
		if (applProperty === null || "" === applProperty) {
			lion.util.info("警告", "请选择保单性质");
			return false;
		}

		var exceptionInform = $("#printInfoForm #exceptionInform").val();
		if (exceptionInform === null || "" === exceptionInform) {
			lion.util.info("警告", "请选择是否异常告知");
			return false;
		}
		var moneyinType = $("#moneyinType").val();
		var premFrom = $("#premFrom").val();
		if (moneyinType === "T" || moneyinType === "W" || moneyinType === "Q"
			 || moneyinType === "D" || moneyinType === "M") {
			var bankCode = $("#bankCode").val();
			if (bankCode === null || "" === bankCode) {
				lion.util.info("警告", "缴费开户行不能为空");
				return false;
			}
			var bankaccNo = $("#bankAccNo").val();
			if (bankaccNo === null || "" === bankaccNo) {
				lion.util.info("警告", "银行账号不能为空");
				return false;
			}
			var bankBranchName = $("#bankName").val();
			if (bankBranchName === null || "" === bankBranchName) {
				lion.util.info("警告", "开户行名不能为空");
				return false;
			}
		}
		return true;
	}

	// 暂存功能
	$("#btnSave").click(function() {
		// 暂存投保单号不能为空
		if ($("#applNo").val() === "") {
			lion.util.info("请输入投保单号");
			return false;
		}
		// 正则表达式校验投保单号
		if (!/^[0-9]{16}$/.test($("#applNo").val())) {
			lion.util.info("投保单号输入不正确");
			return false;
		}
		// 获取要约信息下面的所有险种信息
		var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		// 定义团单大对象
		var grpInsurApplVo = {};
		// 判断要约险种中是否有建工险，如果有，需要校验建工险信息。
		grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractEntry.grpInsurAppl.specialInsurAddInfoForm
				.serializeObject();
		grpInsurApplVo.applInfoVo = com.orbps.contractEntry.grpInsurAppl.applInfoForm
				.serializeObject();
		grpInsurApplVo.vatInfoVo = com.orbps.contractEntry.grpInsurAppl.vatInfoForm
				.serializeObject();
		grpInsurApplVo.applBaseInfoVo = com.orbps.contractEntry.grpInsurAppl.applBaseInfoForm
				.serializeObject();
		grpInsurApplVo.proposalInfoVo = com.orbps.contractEntry.grpInsurAppl.proposalInfoForm
				.serializeObject();
		grpInsurApplVo.payInfoVo = com.orbps.contractEntry.grpInsurAppl.payInfoForm
				.serializeObject();
		grpInsurApplVo.payInfoVo.settlementDate = com.orbps.contractEntry.grpInsurAppl.settleList;
		grpInsurApplVo.printInfoVo = com.orbps.contractEntry.grpInsurAppl.printInfoForm
				.serializeObject();
		grpInsurApplVo.insuredGroupModalVos = com.orbps.common.proposalGroupList;
		grpInsurApplVo.organizaHierarModalVos = com.orbps.common.oranLevelList;
		// 循环获取的全部主险
		for (var i = 0; i < getAddRowsData.length; i++) {
			var busiPrdVos = getAddRowsData[i];
			// 清空当前险种下的责任信息
			busiPrdVos.responseVos = [];
			// 循环责任的list，如果责任里的busiPrdCode和主险下的一样，把责任信息放到险种下的responseVos里
			for (var j = 0; j < com.orbps.common.list.length; j++) {
				// 取到的责任信息
				var reponseVos = com.orbps.common.list[j];
				// 当险种的busiPrdCode等于责任的时候，往险种下的责任list中放值
				if (reponseVos.busiPrdCode === busiPrdVos.busiPrdCode) {
					busiPrdVos.responseVos.push(reponseVos);
				}
			}
		}
		grpInsurApplVo.busiPrdVos = getAddRowsData;
		grpInsurApplVo.chargePayGroupModalVos = com.common.chargePayList;
		var responseVos = new Array();
		// 声明taskInfo对象
		grpInsurApplVo.taskInfo = {};
		grpInsurApplVo.taskInfo.taskId = com.orbps.contractEntry.grpInsurAppl.taskId;
		lion.util.postjson(
			'/orbps/web/orbps/contractEntry/grp/tempSave',
			grpInsurApplVo,
			com.orbps.contractEntry.grpInsurAppl.successbtnsave,
			null, null);
	});

	// 清空按钮功能
	$("#btnClear").click(function() {
		var applNo = $("#applNo").val();
		var giftFlag = $("#giftFlag").val();
		$("#applBaseInfoForm").reset();
		$("#applInfoForm").reset();
		$("#payInfoForm").reset();
		$("#printInfoForm").reset();
		$("#proposalInfoForm").reset();
		$("#specialInsurAddInfoForm").reset();
		$("#vatInfoForm").reset();
		$(".fa").removeClass("fa-warning");
		$(".fa").removeClass("fa-check");
		$(".fa").removeClass("has-success");
		$(".fa").removeClass("has-error");
		//$('#fbp-editDataGrid').find("tbody").empty();
		$("#applNo").val(applNo);
		$("#giftFlag").combo("val",giftFlag);
	});
});
// 添加成功回调函数
com.orbps.contractEntry.grpInsurAppl.successbtnsave = function(data, arg) {
	if (data.retCode === "1") {
		lion.util.info("提示", "暂存成功");
	} else {
		lion.util.info("提示", "暂存失败，失败原因：" + data.errMsg);
	}
}
// 添加成功回调函数
com.orbps.contractEntry.grpInsurAppl.successSubmit = function(data, arg) {
	if (data.retCode === "1") {
		lion.util.info("提示", "提交成功");
		// 提交成功，判断有无清单有清单，跳转按钮置成可点操作
		// 如果是团单清单标记 是无清单 置灰跳转按钮
		if ("L" === $("#ipsnlstId").val()) {
			$("#btnlocation").attr("disabled", false);// 可点
		} else {
			$("#btnlocation").attr("disabled", true);// 不可点
		}

	} else {
		lion.util.info("提示", "提交失败，失败原因：" + data.errMsg);
	}

}
// 清单录入
$("#btnlocation").click(function() {
	var applNo = $("#applNo").val();
	var taskId = com.orbps.contractEntry.grpInsurAppl.taskId;
	var applNoTaskId = applNo + "," + taskId;
	com.ipbps.menuFtn("orbps/orbps/contractEntry/listImport/html/offlineListImport.html",applNoTaskId);
});
/**
 * 查询险种中是否含有健康险专项标示，在检查非空。
 */
com.orbps.contractEntry.grpInsurAppl.checkResponse = function(){
	var getAll = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		var string = com.orbps.contractEntry.grpInsurAppl.allHealthInsur.split(",")
		var busiPrdCode = "";
		for (var i = 0; i < getAll.length; i++) {
			for (var j = 0; j < string.length; j++) {
				if(getAll[i].busiPrdCode === string[j]){
					if(getAll[i].healthInsurFlag ===null || getAll[i].healthInsurFlag === undefined){
						busiPrdCode = busiPrdCode + getAll[i].busiPrdCode + " ";
					}
				}
			}
		}
		if(busiPrdCode !== ""){
			lion.util.info("警告","险种信息中 [ "+busiPrdCode+" ]险种的健康险专项标识不能为空！");
			return false;
		}
		return true;
}