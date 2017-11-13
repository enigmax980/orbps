com.orbps.common.oranLevelList = new Array();
com.common.insuredList = new Array();
com.orbps.common.zTrees = [];
com.common.chargePayList = new Array();
com.orbps.common.getAddRowsData;
com.orbps.common.proposalGroupList = new Array();
com.orbps.common.insuranceList = new Array();
com.orbps.common.zNodes = [];
com.orbps.common.companyName;
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
    $("#btnSubmit")
            .click(
                    function() {
                    	console.log(1);
                    	//判断邮编和地区是否对应
                		var appPost = $("#appPost").val();
                		var city = com.orbps.publicSearch.checkPostCode(appPost);
                		var appAddrProv = $("#appAddrProv").val();
                		appAddrProv = appAddrProv.substr(0,2);
                		if(city !== appAddrProv){
                			lion.util.info("提示","邮编输入不正确，请选择[ "+appAddrProv+" ]的对应正确邮编！");
                			return false;
                		}
                		//当页面选择无清单时清单打印置灰
                		var ipsnlstId = $("#printInfoForm #ipsnlstId").val();
            			if (ipsnlstId === "N") {
            				$("#printInfoForm #ipsnVoucherPrtIs").hide();
            				$("#printInfoForm #prtIpsnLstTypeIs").hide();
            				$("#printInfoForm #prtIpsnLstType").attr("disabled", false);
            				$("#printInfoForm #ipsnVoucherPrt").attr("disabled", false);
        					$("#printInfoForm #prtIpsnLstType").combo("clear");
        					$("#printInfoForm #ipsnVoucherPrt").combo("clear");
            			}
                		// 合同打印方式
//                		var cntrType = $("#printInfoForm #cntrType").val();
//                		if (cntrType === "0") {
//                			var connPostcode = $("#applBaseInfoForm #connPostcode").val();
//                			if (connPostcode === "") {
//                				lion.util.info("提示", "合同打印方式为电子保单,联系人邮箱必录");
//                				return false;
//                			}
//                		}
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
                			if (moneyinType === "R" || moneyinType === "C" || moneyinType === "P") {

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
                		if ("Y" === com.orbps.contractReview.grpInsurAppl.agreementNoFlag) {
                			var agreementNo = $("#applInfoForm #agreementNo").val();
                			if ("" === agreementNo) {
                				lion.util.info("提示", "共保协议号不能为空!");
                				return false;
                			}
                		}
                		var sumPrem = $("#proposalInfoForm #sumPrem").val();
                		var stlLimit = $("#payInfoForm #stlLimit").val();
                		// 组合结算
                		if ("X" === stlType) {
                			// 校验结算限额
                			if ((stlLimit === null || "" === stlLimit)&&(com.orbps.contractReview.grpInsurAppl.settleList.length <= 0)) {
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
                			if (com.orbps.contractReview.grpInsurAppl.settleList.length <= 0) {
                				lion.util.info("提示", "交费信息的结算方式是指定日期或组合结算,结算日期清单必录");
                				return false;
                			}
                		} else {
                			com.orbps.contractReview.grpInsurAppl.settleList = [];
                		}
                		//调用校验方法，判断手机号与电话是否都为空
                		if (!com.orbps.contractReview.grpInsurAppl.isPhoneOrTel()) {
                			return false;
                		}
                         //jquery validate 校验
                        if (com.orbps.contractReview.grpInsurAppl.applInfoForm
                                .validate().form()
                                && com.orbps.contractReview.grpInsurAppl.vatInfoForm
                                        .validate().form()
                                && com.orbps.contractReview.grpInsurAppl.applBaseInfoForm
                                        .validate().form()
                                && com.orbps.contractReview.grpInsurAppl.proposalInfoForm
                                        .validate().form()
                                && com.orbps.contractReview.grpInsurAppl.payInfoForm
                                        .validate().form()
                                && com.orbps.contractReview.grpInsurAppl.printInfoForm
                                        .validate().form()) {
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
                			// 联系人移动电话和固定电话不能为同时为空
                			var connPhone = $("#connPhone").val();
                			//var appHomeTel = $("#appHomeTel").val();
                			if (connPhone === "") {
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
                            if (com.orbps.contractReview.grpInsurAppl.validateSelectVal()) {
                            	//获取要约信息下面的所有险种信息
                                var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
                                //定义团单大对象
                                var grpInsurApplVo = {};
                             // 判断要约险种中是否有建工险，如果有，需要校验建工险信息。
                				var flag = 0;
                				if (Object.prototype.toString.call(getAddRowsData) === '[object Array]') {
                					for (var i = 0; i < getAddRowsData.length; i++) {
                						if (lion.util.isNotEmpty(getAddRowsData[i].busiPrdCodeName)) {
                							flag = getAddRowsData[i].busiPrdCodeName.indexOf("建筑");
                							if (flag > 0) {
                								// 判断建工信息施工天数为负
                								if ($("#totalDays").val() < 0) {
                									lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
                									return false;
                								}
                								i = getAddRowsData.length;
                								// 进行建工险校验
                								if (com.orbps.contractReview.grpInsurAppl.specialInsurAddInfoForm.validate().form()
                										&& com.orbps.contractReview.grpInsurAppl.specialValidateSelectVal()) {
                									grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractReview.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
                								} else {
                									lion.util.info("提示","建工险信息输入不正确，请检查");
                									return false;
                								}
                								break;
                							}
                						}
                					}
                				} else {
                					if (lion.util.isNotEmpty(getAddRowsData.busiPrdCodeName)) {
                						flag = getAddRowsData.busiPrdCodeName.indexOf("建筑");
                						if (flag > 0) {
                							// 判断建工信息施工天数为负
                							if ($("#totalDays").val() < 0) {
                								lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
                								return false;
                							}
                							// 进行建工险校验
                							if (com.orbps.contractReview.grpInsurAppl.specialInsurAddInfoForm.validate().form()
                									&& com.orbps.contractReview.grpInsurAppl.specialValidateSelectVal()) {
                								grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractReview.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
                							} else {
                								lion.util.info("提示","建工险信息输入不正确，请检查");
                								return false;
                							}
                						}
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
                				var commPremium = $("#specialInsurAddInfoForm #commPremium").val();//固定保费
                				var fixedComAmnt = $("#specialInsurAddInfoForm #fixedComAmnt").val();//固定保额
                				var comInsurAmntUse = $("#specialInsurAddInfoForm #comInsurAmntUse").val();//公共保额使用范围
                				var comInsurAmntType = $("#specialInsurAddInfoForm #comInsurAmntType").val();//公共保额类型
                				//公共保额使用范围 当公共保额选择了就说明这个录入了公共保额信息。
                				if(comInsurAmntUse !== "0" && comInsurAmntUse !== undefined && comInsurAmntUse !== ""){
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
                				/**
                				*判断险种中的是否有基金险，有基金险要校验一些逻辑
                				*/
                				if(com.orbps.contractReview.grpInsurAppl.FundInsurFlag === "Y"){
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
                					if(com.orbps.common.proposalGroupList.length < 0){
                						lion.util.info("险种中有基金险，被保人分组必录！"); 
                						return false;
                					}
                					//基金险不能是档案清单
                					var ipsnlstId = $("#ipsnlstId").val();
                					if(ipsnlstId === "A"){
                						lion.util.info("提示","基金险暂不支持档案清单！");
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
                				//特殊险种表单做对象序列化
                				grpInsurApplVo.specialInsurAddInfoVo = com.orbps.contractReview.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
                                // 提交方法
                                grpInsurApplVo.applInfoVo = com.orbps.contractReview.grpInsurAppl.applInfoForm
                                        .serializeObject();
                                grpInsurApplVo.vatInfoVo = com.orbps.contractReview.grpInsurAppl.vatInfoForm
                                        .serializeObject();
                                grpInsurApplVo.applBaseInfoVo = com.orbps.contractReview.grpInsurAppl.applBaseInfoForm
                                        .serializeObject();
                                grpInsurApplVo.proposalInfoVo = com.orbps.contractReview.grpInsurAppl.proposalInfoForm
                                        .serializeObject();
                                grpInsurApplVo.payInfoVo = com.orbps.contractReview.grpInsurAppl.payInfoForm
                                        .serializeObject();
                                grpInsurApplVo.printInfoVo = com.orbps.contractReview.grpInsurAppl.printInfoForm
                                        .serializeObject();
                                grpInsurApplVo.insuredGroupModalVos = com.orbps.common.proposalGroupList;
                                grpInsurApplVo.organizaHierarModalVos = com.orbps.common.oranLevelList;
                                grpInsurApplVo.busiPrdVos = com.orbps.contractReview.grpInsurAppl.busiPrdVos;
                                grpInsurApplVo.chargePayGroupModalVos = com.common.chargePayList;
                                grpInsurApplVo.payInfoVo.settlementDate = com.orbps.contractReview.grpInsurAppl.settleList;
                                //判断复合结果
                                if("Y"===$("#reviewFlag").val()){
                                	
                                    grpInsurApplVo.approvalState = "19";
                                    
                                }else if("N"===$("#reviewFlag").val()){
                                	
                                    grpInsurApplVo.approvalState = "20";
                                    
                                }else{
                                	lion.util.info("警告", "请选择复核是否通过！");
                                	return false;
                                }
                                grpInsurApplVo.taskInfo ={};
                                grpInsurApplVo.taskInfo.taskId = com.orbps.contractReview.grpInsurAppl.taskId;
                                lion.util
                                        .postjson(
                                                '/orbps/web/orbps/contractReview/grp/submit',
                                                grpInsurApplVo,
                                                com.orbps.contractReview.grpInsurAppl.successSubmit,
                                                null,
                                                null);
                            }
                        }
                    });

    // 被保人分组
    $("#btnGoup")
            .click(
                    function() {
                        com.orbps.contractReview.grpInsurAppl.addDialog.empty();
                        com.orbps.contractReview.grpInsurAppl.addDialog
                                .load(
                                        "/orbps/orbps/public/modal/html/insuredGroupModal.html",
                                        function() {
                                            $(this).modal('toggle');
                                            $(this).comboInitLoad();
                                        });
                        com.orbps.common.getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
                        setTimeout(function() {
                        	// 刷新table
                        	com.orbps.common.reloadProposalGroupModalTable();
                        }, 100);
                    });

    // 组织层次架构
    $("#btnOranLevel")
            .click(
                    function() {
                		//如果清单标记是档案清单，不支持录入组织架构树信息。
                		if($("#ipsnlstId").val() === "A"){
                			lion.util.info("提示","团单清单标记是档案清单，暂不支持录入组织架构树信息");
                			return false;
                		}
                		//如果是基金险账户，组织架构树不能录入。
                		if(com.orbps.contractReview.grpInsurAppl.FundInsurFlag === "Y"){
                			lion.util.info("提示","基金险账户不能录入组织架构。");
                			return false;
                		}
                    	// 单位/团体名称
                		var companyName = $("#applBaseInfoForm #companyName").val();
                		com.orbps.common.companyName = companyName;
                		// 投保日期
                		var applDate = $("#applInfoForm #applDate").val();
                		com.orbps.common.companyName = applDate;
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
                			com.orbps.contractReview.grpInsurAppl.addDialog.empty();
                			com.orbps.contractReview.grpInsurAppl.addDialog.load(
                				"/orbps/orbps/public/modal/html/organizaHierarModal.html",
                				function() {
                					$(this).modal('toggle');
                					// combo组件初始化
                					$(this).comboInitLoad();
                					com.orbps.common.grpInsurApplVo.applInfoVo = com.orbps.contractReview.grpInsurAppl.applInfoForm
                							.serializeObject();
                					com.orbps.common.grpInsurApplVo.applBaseInfoVo = com.orbps.contractReview.grpInsurAppl.applBaseInfoForm
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
    
}); 


    //建工险的选择信息校验
	com.orbps.contractReview.grpInsurAppl.specialValidateSelectVal = function () {
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
    // 校验选择信息
    com.orbps.contractReview.grpInsurAppl.validateSelectVal = function () {
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
		if ($("#moneyinType").val() === "T" && $("#premFrom").val() !== "2") {
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
    
// 添加成功回调函数
com.orbps.contractReview.grpInsurAppl.successSubmit = function (data, arg) {
    if (data.retCode==="1"){
        lion.util.info("提示", "提交成功");
      //提交成功，判断有无清单有清单，跳转按钮置成可点操作
        //如果是团单清单标记 是无清单 置灰跳转按钮
        if("L" !== $("#ipsnlstId").val() || "N"===$("#reviewFlag").val()){
        	$("#btnlocation").attr("disabled",true);
        	//复合不通过，按钮置成不可点
        }else{
        	$("#btnlocation").attr("disabled",false);
        }
    }else{
        lion.util.info("提示", "提交失败，失败原因："+data.errMsg);
    }
}

//收付费分组
$("#btnChargePay")
        .click(
                function() {
                	if($("#premFrom").val() === ""){
            			lion.util.info("提示","请先选择保费来源，在录入收付费信息。");
            			return false;
            		}
            		if($("#premFrom").val() === "1" ){
            			lion.util.info("提示","当保费来源是团体付款时，不能录入收付费信息。");
            			return false;
            		}
                    com.orbps.contractReview.grpInsurAppl.addDialog.empty();
                    com.orbps.contractReview.grpInsurAppl.addDialog
                            .load(
                                    "/orbps/orbps/public/modal/html/chargePayGroupModal.html",
                                    function() {
                                        $(this).modal('toggle');
                                        // combo组件初始化
                                        $(this).comboInitLoad();
                                    });
                    setTimeout(function() {
                    	com.common.reloadPublicChargePayModalTable();
                    },100);
                });

//影像信息查询
$("#imageQuery")
        .click(
                function() {
                	var salesBranchNo ="";
                	if(com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos.length===1){
            			salesBranchNo = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[0].salesBranchNo;
            			salesChannel = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[0].salesChannel;
                    	worksiteNo = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[0].worksiteNo;
                    	saleCode = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[0].saleCode;
            		}else{
	            		for (var i = 0; i < com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos.length; i++) {
	            			var jointFieldWorkFlag = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[i].jointFieldWorkFlag;
	            			if(jointFieldWorkFlag==="Y"){
	            				salesBranchNo = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[i].salesBranchNo;
	            				salesChannel = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[i].salesChannel;
	                        	worksiteNo = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[i].worksiteNo;
	                        	saleCode = com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos[i].saleCode;
	            				break;
	            			}
	            		}
            		}
                	com.orbps.common.applNo = $("#applInfoForm #applNo").val();
                	com.orbps.common.quotaEaNo = $("#applInfoForm #quotaEaNo").val();
                	if(salesBranchNo !== ""){
                		com.orbps.common.salesBranchNo = salesBranchNo;
                	}
                	com.orbps.common.salesChannel = salesChannel;
                	com.orbps.common.worksiteNo = worksiteNo;
                	com.orbps.common.saleCode = saleCode;
                	com.orbps.contractReview.grpInsurAppl.addDialog.empty();
                	com.orbps.contractReview.grpInsurAppl.addDialog
                          .load(
                                  "/orbps/orbps/public/modal/html/imageCollection.html",
                                  function() {
                                      $(this).modal('toggle');
                                      $(this).comboInitLoad();
                                  });
                });

$("#btnlocation").click(function(){
	var applNo = $("#applNo").val();
	var taskId = com.orbps.contractReview.grpInsurAppl.taskId;
	var applNoTaskId = applNo + "," + taskId;
	//跳转清单复核页面。
	com.ipbps.menuFtn("orbps/orbps/contractReview/listImport/html/offlineListImport.html",applNoTaskId); 
});