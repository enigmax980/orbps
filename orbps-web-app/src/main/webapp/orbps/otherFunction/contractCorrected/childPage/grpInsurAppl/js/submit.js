com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog= $('#btnModel');
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applInfoForm=$("#applInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.vatInfoForm=$("#vatInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseInfoForm=$("#applBaseInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.proposalInfoForm=$("#proposalInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payInfoForm=$("#payInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm=$("#specialInsurAddInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printInfoForm=$("#submit");
com.common.oranLevelList = new Array();
com.common.insuredList = new Array();
com.common.zTrees = [];
com.common.chargePayList = new Array();
com.orbps.common.zNodes = [];
com.orbps.common.insuranceList = new Array();
com.orbps.common.companyName;
/** 节点名称存储集合 */
com.orbps.common.treeNodeNameList=[];
//基本信息校验规则
$(function(){
// 初始化校验函数
	// 表单提交
	$("#btnSubmit").click(function () {
			// jquery validate 校验
		if(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseInfoForm.validate().form()
					&& com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.proposalInfoForm.validate().form()
						&& com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payInfoForm.validate().form()
								&& com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printInfoForm.validate().form()){
									// select校验
									if(validateSelectVal()){
										//判断邮编和地区是否对应
										var appPost = $("#appPost").val();
										var city = com.orbps.publicSearch.checkPostCode(appPost);
										var appAddrProv = $("#appAddrProv").val();
										appAddrProv = appAddrProv.substr(0,2);
										if(city !== appAddrProv){
											lion.util.info("提示","邮编输入不正确，请选择[ "+appAddrProv+" ]的对应正确邮编！");
											return false;
										}
									// 获取要约信息下面的所有险种信息
										// 提交方法
										var grpInsurApplVo = {};
										var getAddRowsData = com.orbps.otherFunction.contractCorrected.childPage.busiProd;
										// 判断要约险种中是否有建工险，如果有，需要校验建工险信息。
										if (Object.prototype.toString.call(getAddRowsData) === '[object Array]') {
											for (var i = 0; i < getAddRowsData.length; i++) {
												var busiprdCodeTable = getAddRowsData[i].busiPrdCode;
												for (var j = 0; j < com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray.length; j++) {
													var array_element = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray[j];
													// 判断主险是否是建筑工程险种
													if (busiprdCodeTable === array_element) {
														// 判断建工信息施工天数为负
														if ($("#totalDays").val() < 0) {
															lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
															return false;
														}
														i = getAddRowsData.length;
														// 进行建工险校验
														// 进行建工险校验
														if (com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm.validate().form()
																&& com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialValidateSelectVal()) {
															grpInsurApplVo.specialInsurAddInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
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
												for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray.length; i++) {
													var array_element = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray[i];
													if (busiprdCodeTable === array_element) {
														// 判断建工信息施工天数为负
														if ($("#totalDays").val() < 0) {
															lion.util.info("提示","施工期间开始日期不能大于结束日期，请重新选择");
															return false;
														}
														// 进行建工险校验
														if (com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm.validate().form()
																&& com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialValidateSelectVal()) {
															grpInsurApplVo.specialInsurAddInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
														} else {
															lion.util.info("提示","建工险信息输入不正确，请检查");
															return false;
														}
													}
												}
											}
										}										
										
										/**
										*判断险种中的是否有基金险，有基金险要校验一些逻辑
										*/
										if(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.FundInsurFlag === "Y"){
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
											var getAddRowsData = com.orbps.otherFunction.contractCorrected.childPage.busiProd;
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
										grpInsurApplVo.applInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applInfoForm.serializeObject();
										grpInsurApplVo.vatInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.vatInfoForm.serializeObject();
										grpInsurApplVo.applBaseInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseInfoForm.serializeObject();
										grpInsurApplVo.proposalInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.proposalInfoForm.serializeObject();
										grpInsurApplVo.payInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payInfoForm.serializeObject();
										//如果结算方式为指定日期结算或是组合结算时，则送指点结算日期清单
										grpInsurApplVo.payInfoVo.settlementDate = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList;
										grpInsurApplVo.specialInsurAddInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialInsurAddInfoForm.serializeObject();
										grpInsurApplVo.printInfoVo = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printInfoForm.serializeObject();
										grpInsurApplVo.insuredGroupModalVos = com.common.insuredList;
										grpInsurApplVo.organizaHierarModalVos = com.common.oranLevelList;
										grpInsurApplVo.busiPrdVos = com.orbps.otherFunction.contractCorrected.childPage.busiProd;
										grpInsurApplVo.responseVos = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos;
										lion.util.postjson('/orbps/web/orbps/otherfunction/grpInsurAppl/submit',grpInsurApplVo,com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.successSubmit,null,null);
									}
								}
	}); 
	
	//回退信息
	$("#btnBack").click(function() {
		// 提交方法
		$("#btnBack").attr("disabled",true);
		var backInfo="";
		var backStat = $("#backStat").val()
		if( backStat=== "" || backStat === undefined){
			lion.util.info("提示", "请选择回退状态！");
			return false;
		}
		backInfo = backInfo + $("#applInfoForm #applNo").val() + ",";//投保单号
		backInfo = backInfo + backStat + ",";//回退状态
		backInfo = backInfo + $("#backReason").val();//回退原因
		lion.util.postjson('/orbps/web/orbps/otherfunction/grpInsurAppl/back',backInfo,com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.successBackSubmit,null,null);
	});
	
	//被保人分组
	 $("#btnGoup").click(function() {
		  com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.empty();
		  com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.load("/orbps/orbps/public/searchModal/html/insuredGroupModal.html",function(){
			  $(this).modal('toggle');
			  $(this).comboInitLoad();
			  $(".fa").removeClass("fa-warning");
			  $(".fa").removeClass("fa-check");
			  $(".fa").removeClass("has-success");
			  $(".fa").removeClass("has-error");
		  });
		  setTimeout(function(){
			  // 刷新table
	          	com.orbps.common.reloadProposalGroupModalTable();
		  },100);
	 });
	 
	// 组织层次架构
	    $("#btnOranLevel")
	            .click(
	                    function() {
	                		//如果是基金险账户，组织架构树不能录入。
	                		if(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.FundInsurFlag === "Y"){
	                			lion.util.info("提示","基金险账户不能录入组织架构。");
	                			return false;
	                		}
	                    	var companyName = $("#applBaseInfoForm #companyName").val();
	                    	com.orbps.common.companyName = companyName;
	                    	if(companyName!==""){
	                    		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.empty();
	                    		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog
	                    		.load(
	                    				"/orbps/orbps/public/modal/html/organizaHierarModal.html",
	                    				function() {
	                    					$(this).modal('toggle');
	                    					// combo组件初始化
	                    					$(this).comboInitLoad();
	                    					com.orbps.common.grpInsurApplVo.applInfoForm = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applInfoForm.serializeObject();
	                    					com.orbps.common.grpInsurApplVo.applBaseInfoForm = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.applBaseInfoForm.serializeObject();
	                    					console.log("点击modal的noes"+JSON.stringify(com.orbps.common.zTrees));
	                    					if(com.orbps.common.zTrees.length===1){
	                    						com.orbps.common.zTrees[0].name = companyName;
	                    					}
	                    					// zTree初始化
	                    					var zTree = $.fn.zTree.init($("#treeDemo"), setting, com.orbps.common.zTrees);
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
	                    	}else{
	                    		lion.util.info("提示","请先录入单位/团体名称再添加组织架构树！");
	                    		return false;
	                    	}
	                    });
	    
	 
	// 收付费分组
	    $("#btnChargePay")
	            .click(
	                    function() {
	                    	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.empty();
	                    	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog
	                                .load(
	                                        "/orbps/orbps/public/searchModal/html/chargePayGroupModal.html",
	                                        function() {
	                                            $(this).modal('toggle');
	                                            // combo组件初始化
	                                            $(this).comboInitLoad();
	                                            $(".fa").removeClass("fa-warning");
	                              			  $(".fa").removeClass("fa-check");
	                              			  $(".fa").removeClass("has-success");
	                              			  $(".fa").removeClass("has-error");
	                                        });
	                        setTimeout(function() {
	                        	com.common.reloadPublicChargePayModalTable();
	                        },600);
	                    });
	 
	 //修改被保人信息
	 $("#btnUpdate").click(function(){
//		 var listImportIpsnInfoVo ={};
//		 listImportIpsnInfoVo.applNo = $("#applInfoForm #applNo").val(); 
		 $('#btnModel').empty();  		
		 $('#btnModel').load("/orbps/orbps/otherFunction/contractCorrected/childPage/listImport/html/offlineListImport.html",function(){
			 $(this).modal('toggle');
			 $(".fa").removeClass("fa-warning");
			  $(".fa").removeClass("fa-check");
			  $(".fa").removeClass("has-success");
			  $(".fa").removeClass("has-error");
		 });
	 });
	 
	// 校验选择信息
	function validateSelectVal(){
			var applDate = $("#applInfoForm #applDate").val();
		    if (applDate === null || "" === applDate) {
		        lion.util.info("警告", "请选择投保日期");
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
	        var appAddrCity = $("#applBaseInfoForm #appAddrCity").val();
	        if (appAddrCity === null || "" === appAddrCity) {
	            lion.util.info("警告", "请选择市/城区");
	            return false;
	        }
	        
	        var appAddrProv = $("#applBaseInfoForm #appAddrProv").val();
	        if (appAddrProv === null || "" === appAddrProv) {
	            lion.util.info("警告", "请选择省/直辖市");
	            return false;
	        }
	        var appAddrCountry = $("#applBaseInfoForm #appAddrCountry").val();
	        if (registerArea === null || "" === registerArea) {
	            lion.util.info("警告", "请选择县/地级市");
	            return false;
	        }
	        
	        var idType = $("#applBaseInfoForm #idType").val();
	        if (idType === null || "" === idType) {
	            lion.util.info("警告", "请选择证件类型");
	            return false;
	        }
	        
	        var disputePorcWay = $("#applBaseInfoForm #disputePorcWay").val();
	        if (disputePorcWay === null || "" === disputePorcWay) {
	            lion.util.info("警告", "请选择争议处理方式");
	            return false;
	        }
	        
	        // 仲裁机构名称
			if (disputePorcWay === "2") {
				var arbOrgName = $("#applBaseInfoForm #arbOrgName").val();
				if (arbOrgName === "") {
					lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
					return false;
				}
			}
	        
	        var forceType = $("#proposalInfoForm #forceType").val();
	        if (forceType === null || "" === forceType) {
	            lion.util.info("警告", "请选择生效方式");
	            return false;
	        }
	        if("1" === forceType){
	        	var inForceDate = $("#proposalInfoForm #inForceDate").val();
	        	if(inForceDate === null || "" === inForceDate){
	        		 lion.util.info("警告", "请选择生效方式日期");
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
	        //清单标记为“无清单”时，清单打印 &个人凭证打印不可用 ，不等于的时候再去校验必录校验
	        if("N" !== ipsnlstId){
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
	        if (ipsnlstId === null || "" === ipsnlstId) {
	            lion.util.info("警告", "请选择团单清单标记");
	            return false;
	        }else if(ipsnlstId==="N" &&  $("#payInfoForm #premFrom").val()!=="1"){
	        	lion.util.info("警告", "团体清单标记选择“无清单”时保费来源为“团体账户付款”！");
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
	        var moneyinTypeFlag = 0;
	        // 交费形式
	        var moneyinType = $("#payInfoForm #moneyinType").val();
	        var stlType = $("#payInfoForm #stlType").val();
	        var premFrom = $("#payInfoForm #premFrom").val();
	        if (moneyinType === "T" || moneyinType === "W" || moneyinType === "Q"
				 || moneyinType === "D" || moneyinType === "M") {
				var bankCode = $("#bankCode").val();
				if (bankCode === null || "" === bankCode) {
					lion.util.info("警告", "交费开户行不能为空");
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
			// 判断组织树是否交费
			for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
				var pay = com.orbps.common.oranLevelList[i].pay;
				if (pay === "Y") {
					moneyinTypeFlag = 1;
					if (moneyinType !== "") {
						// 如果组织树交费，则交费信息里的交费形式置空
						lion.util.info("提示","组织结构树存在交费节点,交费形式必须为空！")
						return false;
					}
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
			if ("Y" === com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.agreementNoFlag) {
				var agreementNo = $("#applInfoForm #agreementNo").val();
				if ("" === agreementNo) {
					lion.util.info("提示", "共保协议号不能为空!");
					return false;
				}
			}
			// 组合结算
			if ("X" === stlType) {
				// 校验结算限额
				var stlLimit = $("#payInfoForm #stlLimit").val();
				if (stlLimit === null || "" === stlLimit) {
					lion.util.info("警告", "结算限额不能为空");
					return false;
				}
				var sumPrem = $("#proposalInfoForm #sumPrem").val();
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
			if ("D" === stlType || "X" === stlType) {
				if (com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList.length <= 0) {
					lion.util.info("提示", "交费信息的结算方式是指定日期或是组合结算时,结算日期清单必录");
					return false;
				}
			}else{
				com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList = [];
			}
			// 联系人移动电话和固定电话不能为同时为空
			var connPhone = $("#connPhone").val();
			//var appHomeTel = $("#appHomeTel").val();
			if (connPhone === "" ) {
				lion.util.info("警告", "联系人移动电话不能为空");
				return false;
			}
	        return true;
	}
	// 建工险选择信息的校验
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.specialValidateSelectVal = function() {
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
		return true;
	}
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.successSubmit = function (data,arg){
		if (data.retCode==="1"){
	        lion.util.info("提示", "订正成功");
	    }else{
	        lion.util.info("提示", "订正失败，失败原因："+data.errMsg);
	    }
	}
	
	//回退成功回调函数
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.successBackSubmit = function(data,arg){
		if (data.retCode==="1"){
	        lion.util.info("提示", "回退成功");
	    }else{
	        lion.util.info("提示", "回退失败，失败原因："+data.errMsg);
	    }
		//将提交按钮置成可点的
		$("#btnBack").attr("disabled",false);
	}
	
	
});

