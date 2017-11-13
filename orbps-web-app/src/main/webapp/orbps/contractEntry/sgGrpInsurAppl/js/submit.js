com.orbps.contractEntry.sgGrpInsurAppl.addDialog = $('#btnModel');
com.orbps.contractEntry.sgGrpInsurAppl.benesList = [];
com.orbps.contractEntry.sgGrpInsurAppl.polRowsData = [];
com.common.insuredList = new Array();
com.common.oranLevelList = new Array();
com.common.chargePayList = new Array();
com.orbps.common.proposalGroupList = new Array();
com.orbps.common.insuranceList = new Array();
com.orbps.common.getAddRowsData;
com.orbps.common.applNo;
com.orbps.common.quotaEaNo;
com.orbps.common.salesBranchNo;
com.orbps.common.salesChannel;
com.orbps.common.worksiteNo;
com.orbps.common.saleCode;
$(function() {
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
    // 表单提交
    $("#btnSubmit").click(
    		
            function() {
                // jquery validate 校验
                if (com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm
                        .validate().form()
                        && com.orbps.contractEntry.sgGrpInsurAppl.payInfoForm
                                .validate().form()
                        && com.orbps.contractEntry.sgGrpInsurAppl.printInfoForm
                                .validate().form()
                        && com.orbps.contractEntry.sgGrpInsurAppl.proposalInfoForm
                                .validate().form()
                        && com.orbps.contractEntry.sgGrpInsurAppl.vAddTaxInfoForm
                                .validate().form()) {
                    // select校验
                    if (com.orbps.contractEntry.sgGrpInsurAppl.validateSelectVal()) {
                    	if(com.orbps.contractEntry.sgGrpInsurAppl.validateSubmitbegin()){
		                	// 获取要约信息下面的所有险种信息
		                    var getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
		                	   // 提交方法
		                    var sgGrpInsurApplVo = {};
		                   
		                    sgGrpInsurApplVo.sgGrpApplInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm
		                            .serializeObject();
		                    var listType = $("#listType").val();
		                    //根据汇交人类型序列化vo对象
		                    if("G" === listType){
		                    	if(com.orbps.contractEntry.sgGrpInsurAppl.grpInsurInfoForm.validate().form()){
		                    		//团单
			                        sgGrpInsurApplVo.sgGrpInsurInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.grpInsurInfoForm
			                                .serializeObject();
		                    	}else{
		                    		lion.util.info("汇交人信息输入不正确");
		                    		return false;
		                    	}
		                    }else if("P" === listType){
		                    	$("#personalInsurInfoForm #joinBirthDate").attr("disabled",false);			                    
		                    	//个人
				               	if(com.orbps.contractEntry.sgGrpInsurAppl.personalInsurInfoForm.validate().form()){
				               		sgGrpInsurApplVo.sgGrpPersonalInsurInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.personalInsurInfoForm
	                                .serializeObject();
				               	}else{
		                    		lion.util.info("汇交人信息输入不正确");
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
		                    sgGrpInsurApplVo.sgGrpPayInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.payInfoForm
		                            .serializeObject();
		                    sgGrpInsurApplVo.sgGrpPrintInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.printInfoForm
		                            .serializeObject();
		                    sgGrpInsurApplVo.sgGrpProposalInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.proposalInfoForm 
		                    		.serializeObject();
		                    sgGrpInsurApplVo.sgGrpVatInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.vAddTaxInfoForm
		                            .serializeObject();
		                    sgGrpInsurApplVo.insuredGroupModalVos = com.orbps.common.proposalGroupList;
		                    //sgGrpInsurApplVo.organizaHierarModalVos = com.orbps.contractEntry.sgGrpInsurAppl.oranLevelList;
		                    //循环获取的全部主险
		                    for (var i = 0; i < getAddRowsData.length; i++) {
		                    	var addinsuranceVos = getAddRowsData[i];
		                    	//清空当前险种下的责任信息
		                    	addinsuranceVos.responseVos = [];
		                    	//循环责任的list，如果责任里的busiPrdCode和主险下的一样，把责任信息放到险种下的responseVos里
		                    	for (var j = 0; j < com.orbps.common.list.length; j++) {
		                    		//取到的责任信息
									var reponseVos = com.orbps.common.list[j];
									//当险种的busiPrdCode等于责任的时候，往险种下的责任list中放值	
									if(reponseVos.busiPrdCode === addinsuranceVos.busiPrdCode){
										addinsuranceVos.responseVos.push(reponseVos);
									}
								}
							}
		                    //alert(JSON.stringify(getAddRowsData));
		                    sgGrpInsurApplVo.addinsuranceVos = getAddRowsData;
		                    sgGrpInsurApplVo.chargePayGroupModalVos = com.common.chargePayList;
		                    sgGrpInsurApplVo.listType = $("#listType").val();
		                    sgGrpInsurApplVo.taskInfo={};
		                    sgGrpInsurApplVo.taskInfo.taskId = com.orbps.contractEntry.sgGrpInsurAppl.taskId;
		                    lion.util
		                            .postjson(
		                                    '/orbps/web/orbps/contractEntry/sg/submit',
		                                    sgGrpInsurApplVo,
		                                    com.orbps.contractEntry.sgGrpInsurAppl.successSubmit,
		                                    null,
		                                    null);
                    }
                  }
                }
            });
 // 通过投保单号查询险种信息
    $("#btnQuery")
            .click(
                    function() {
                        // 取出投保单号 
                        var applNo = $("#applInfoForm #applNo").val();
                        // 校验投保单号
                        if (applNo !== null || "".equals(applNo)
                                || applNo === "undefined") {
                            var insurApplVo = {};
                            insurApplVo.applInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm
                                    .serializeObject();
                            lion.util
                                    .postjson(
                                            '/orbps/web/orbps/contractEntry/grp/query',
                                            insurApplVo,
                                            successQuery,
                                            null,
                                            com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm);
                        } else {
                            lion.util.info('提示', '请输入正确投保单号');
                        }
                    });

    // 被保人分组
    $("#btnGoup").click(
            function() {
            	if("" === $("#groupType").val()){
        			lion.util.info("请先选择分组类型");
        			return false;
        		}
                com.orbps.contractEntry.sgGrpInsurAppl.addDialog.empty();
                com.orbps.contractEntry.sgGrpInsurAppl.addDialog.load(
                        "/orbps/orbps/public/modal/html/insuredGroupModal.html",
                        function() {
                            $(this).modal('toggle');
                            $(this).comboInitLoad();
                            // 刷新table
                        });
                com.orbps.common.getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
                setTimeout(function() {
                    // 回显
                	com.orbps.common.reloadProposalGroupModalTable();
                }, 400);
            });
        });

	//影像信息查询
	$("#imageQuery").click(
                function() {
                	var salesBranchNo ="";
                	if(com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos.length===1){
            			salesBranchNo = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[0].salesBranchNo;
            			salesChannel = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[0].salesChannel;
                    	worksiteNo = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[0].worksiteNo;
                    	saleCode = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[0].saleCode;
                	}else{
	            		for (var i = 0; i < com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos.length; i++) {
	            			var jointFieldWorkFlag = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[i].jointFieldWorkFlag;
	            			if(jointFieldWorkFlag==="Y"){
	            				salesBranchNo = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[i].salesBranchNo;
	            				salesChannel = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[i].salesChannel;
	                        	worksiteNo = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[i].worksiteNo;
	                        	saleCode = com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos[i].saleCode;
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
                	com.orbps.contractEntry.sgGrpInsurAppl.addDialog.empty();
                	com.orbps.contractEntry.sgGrpInsurAppl.addDialog
                          .load(
                                  "/orbps/orbps/public/modal/html/imageCollection.html",
                                  function() {
                                      $(this).modal('toggle');
                                      $(this).comboInitLoad();
                                  });
                });
	//提交校验
	com.orbps.contractEntry.sgGrpInsurAppl.validateSubmitbegin=function(){
		//校验邮政编码和输入的是否一致
		var listType = $("#listType").val();
		if("G" === listType){
			var zipCode = $("#zipCode").val();
			var city = com.orbps.publicSearch.checkPostCode(zipCode);
			var province = $("#provinceCode").val();
			province = province.substr(0,2);
			if(city !== province){
				lion.util.info("提示","邮编输入不正确，请选择[ "+province+" ]的对应正确邮编！");
				return false;
			}
			
		}else if("P" === listType){
			var postCode = $("#postCode").val();
			var city = com.orbps.publicSearch.checkPostCode(postCode);
			var province = $("#province").val();
			province = province.substr(0,2);
			if(city !== province){
				lion.util.info("提示","邮编输入不正确，请选择[ "+province+" ]的对应正确邮编！");
				return false;
			}
		}
		//判断主险种承保人数与被保险人总数是否一致
		var ipsnNum = $("#proposalInfoForm #insuredTotalNum").val();
		com.orbps.contractEntry.sgGrpInsurAppl.polRowsData = $(
		"#bsInfoListTb").editDatagrids(
		"getRowsData");
		if (Object.prototype.toString
				.call(com.orbps.contractEntry.sgGrpInsurAppl.polRowsData) === '[object Array]') {
			if (com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList.length > 0) {
				for (var i = 0; i < com.orbps.contractEntry.sgGrpInsurAppl.polRowsData.length; i++) {
					var busiPrdCode = com.orbps.contractEntry.sgGrpInsurAppl.polRowsData[i].busiPrdCode;
					var insuredNum = com.orbps.contractEntry.sgGrpInsurAppl.polRowsData[i].insuredNum;
					if (com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList[0].busiPrdCode === busiPrdCode) {
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
    	//如果证件号码不是身份证时，校验出生日期是否大于18岁
    	var appIdTypeValue = $("#joinIdType").val()
		var joinBirthDate = $("#personalInsurInfoForm #joinBirthDate").val();
		joinBirthDate = joinBirthDate.substring(0,4)
		var date = new Date();
		var year = date.getFullYear();
		if((year - joinBirthDate) < 18 ){
			lion.util.info("提示","请输入大于18岁的出生日期");
			return false ;
		}

    	// 判断共保协议号是否必录
		if ("Y" === com.orbps.contractEntry.sgGrpInsurAppl.agreementNoFlag) {
			var agreementNo = $("#applInfoForm #agreementNo")
					.val();
			if ("" === agreementNo) {
				lion.util.info("提示", "共保协议号不能为空!");
				return false;
			}
		}
		//如果选择多期暂缴保费来源必须是团体  交费方式必须是年 和 趸。 Y  W
    	if("Y" === $("#multiRevFlag").val()){
    		var premFrom = $("#premFrom").val();
    		var moneyItrvl = $("#moneyItrvl").val()
    		if("W" !== moneyItrvl && "Y" !== moneyItrvl){
    			lion.util.info("提示","当选择多期暂交时，交费方式必须是年或者趸！");
    			return false;
    		}
    		if("1" !== premFrom){
    			lion.util.info("提示","当选择多期暂交时，保费来源必须是团体缴费");
    			return false;
    		}
    	}
    	//供款比例和供款金额不能为同时为空
    	if($("#premFrom").val()==="3"){
    		var multiPartyScale = $("#multiPartyScale").val();
			var multiPartyMoney = $("#multiPartyMoney").val();
			if(( "" === multiPartyScale || null === multiPartyScale) &&
					( "" === multiPartyMoney || null === multiPartyMoney)){
				 lion.util.info("警告", "供款比例和供款金额不能为同时为空");
		         return false;
			}
    	}
    	//如果选择多期暂缴保费来源必须是团体  交费方式必须是年 和 趸。 Y  W
    	if("Y" === $("#multiRevFlag").val()){
    		var premFrom = $("#premFrom").val();
    		var moneyItrvl = $("#moneyItrvl").val()
    		if("W" !== moneyItrvl && "Y" !== moneyItrvl){
    			lion.util.info("提示","当选择多期暂缴时，交费方式必须是年或者趸！");
    			return false;
    		}
    	}
    	var listType = $("#listType").val();
		if("G" === listType){
//		if("0"===$("#underNoticeType").val() || "0" === $("#listPrint").val()){
//				if("" === $("#contactEmail").val()){
//					lion.util.info("警告","合同打印方式或清单打印为电子方式，请输入联系人邮箱!");
//					return false;
//				}
//				
//			}
			//增加团体汇交联系人移动电话和固定电话同时为空的判断
			var contactMobile = $("#contactMobile").val();
			//var contactTel = $("#contactTel").val();
			if("" === contactMobile || null === contactMobile  ){
				 lion.util.info("警告", "联系人移动电话不能为空");
		         return false;
			}
			com.orbps.contractEntry.sgGrpInsurAppl.grpInsurInfoForm
            .validate().form();
		}else if("P" === listType){
			var contactMobile = $("#joinMobile").val();
			//var contactTel = $("#contactTel").val();
			if("" === contactMobile || null === contactMobile  ){
				 lion.util.info("警告", "汇交人移动电话不能为空");
		         return false;
			}
			com.orbps.contractEntry.sgGrpInsurAppl.personalInsurInfoForm
            .validate().form();
		}
		//判断清汇页面的清单标记 只能选择 普通清单和档案清单。
		if($("#listFlag").val()==="N" || $("#listFlag").val() === "M"){
			lion.util.info("警告","该保单不支持无清单和事后补录清单，请检查！");
			return false;
		}
		
		 var getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
	     var busiPrdCodes = [];
	     //取到当前的第一个险种的保险期间类型
	     var insurDurUnitOne = getAddRowsData[0].insurDurUnit;
	 	 if(Object.prototype.toString.call(getAddRowsData) === '[object Array]'){
	 		for (var i = 0; i < getAddRowsData.length; i++) {
				var validateDate = getAddRowsData[i].validateDate;
				var insuredNum = getAddRowsData[i].insuredNum;
				var insurDurUnit = getAddRowsData[i].insurDurUnit;
				var busiPrdCode = getAddRowsData[i].busiPrdCode;
				if(lion.util.isEmpty(validateDate)){
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
	 		var s = busiPrdCodes.join(",")+",";
	 		for (var i = 0; i < busiPrdCodes.length; i++) {
	 			if(s.replace(busiPrdCodes[i]+",","").indexOf(busiPrdCodes[i]+",")>-1){
	 				lion.util.info("提示","【"+busiPrdCodes[i]+"】险种重复,请修改一个重复的险种代码");
	 				return false;
	 			}
			}
	 	}
        //判断险种是否符合要求
        for(var i=0;i<getAddRowsData.length;i++){
        	if (lion.util.isNotEmpty(getAddRowsData[i].busiPrdName)){
            	var sum = 0;//承保人数
            	//获取险种的承保人数
            	sum=parseInt(getAddRowsData[i].insuredNum);
            	if("G" === listType){
            		//获取所有团体提交的投保人数
                	var gInsuredTotalNum = parseInt($("#gInsuredTotalNum").val());
                	//判断承保人数和被保人总数是否相等。
                	if(sum > gInsuredTotalNum){
                		lion.util.info("警告","承保人数和投保人数不一致，请检查！");
                		return false;
                	}
            	}
       	    }
        }
        // 所有险种和责任集合
		var polCodeResponseAll = [];
		for (var i = 0; i < getAddRowsData.length; i++) {
			var array_element = getAddRowsData[i].busiPrdCode;
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
        //判断被保人分组中的要约数组人数是否和投保人数是否一致
        if("G" === listType){
        	if(com.orbps.common.proposalGroupList.length > 0){
        		//获取所有团体提交的投保人数
            	var gInsuredTotalNum = parseInt($("#gInsuredTotalNum").val());
            	var applNum = 0;
    			for (var i = 0; i < com.orbps.common.proposalGroupList.length; i++) {
					var array_element = com.orbps.common.proposalGroupList[i].ipsnGrpNum;
					applNum = parseInt(array_element) + applNum;
				}
    			
    			if(applNum !== gInsuredTotalNum){
    				 lion.util.info("警告", "被保人分组中的要约数组人数和投保人数不一致，请检查！");
    		         return false;
    			}
        	}
        }
		
		return true;
		
	}
// 校验选择信息
com.orbps.contractEntry.sgGrpInsurAppl.validateSelectVal=function () {
	 var applDate = $("#applInfoForm #applDate").val();
	    if (applDate === null || "" === applDate) {
	        lion.util.info("警告", "请选择投保申请日期");
	        return false;
	    }
	
    var listType = $("#listType").val();
	if(listType=="G"){
		
		 var idType = $("#grpInsurInfoForm #idType").val();
		    if (idType === null || "" === idType) {
		        lion.util.info("警告", "请选择证件类型");
		        return false;
		    }

		    var companyRegist = $("#grpInsurInfoForm #companyRegist").val();
		    if (companyRegist === null || "" === companyRegist) {
		        lion.util.info("警告", "请选择企业注册地");
		        return false;
		    }

		    var departmentType = $("#grpInsurInfoForm #departmentType").val();
		    if (departmentType === null || "" === departmentType) {
		        lion.util.info("警告", "请选择部门类型");
		        return false;
		    }
		    
		    var occClassCode = $("#grpInsurInfoForm #occClassCode").val();
		    if (occClassCode === null || "" === occClassCode) {
		        lion.util.info("警告", "请选择职业类别");
		        return false;
		    }

		    var provinceCode = $("#grpInsurInfoForm #provinceCode").val();
		    if (provinceCode === null || "" === provinceCode) {
		        lion.util.info("警告", "请选择省/直辖市");
		        return false;
		    }
		    
		    var cityCode = $("#grpInsurInfoForm #cityCode").val();
		    if (cityCode === null || "" === cityCode) {
		        lion.util.info("警告", "请选择市/城区 ");
		        return false;
		    }
		    
		    var countyCode = $("#grpInsurInfoForm #countyCode").val();
		    if (countyCode === null || "" === countyCode) {
		        lion.util.info("警告", "请选择县/地级市");
		        return false;
		    }

		    var contactIdTypede = $("#grpInsurInfoForm #contactIdType").val();
		    if (contactIdType === null || "" === contactIdType) {
		        lion.util.info("警告", "请选择联系人证件类型");
		        return false;
		    }
		    
		    var gSettleDispute = $("#grpInsurInfoForm #gSettleDispute").val();
		    if (gSettleDispute === null || "" === gSettleDispute) {
		        lion.util.info("警告", "请选择争议处理方式");
		        return false;
		    }
		    
		    // 仲裁机构名称
			if (gSettleDispute === "2") {
				var parbOrgName = $("#grpInsurInfoForm #parbOrgName").val();
				if (parbOrgName === "") {
					lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
					return false;
				}
			}
	}else{	
		
		var joinIdType = $("#personalInsurInfoForm #joinIdType").val();
	    if (joinIdType === null || "" === joinIdType) {
	        lion.util.info("警告", "请选择汇交人证件类型");
	        return false;
	    }
	    var joinSex = $("#personalInsurInfoForm #joinSex").val();
	    if (joinSex === null || "" === joinSex) {
	        lion.util.info("警告", "请选择汇交人性别");
	        return false;
	    }
	    var joinBirthDate = $("#personalInsurInfoForm #joinBirthDate").val();
	    if (joinBirthDate === null || "" === joinBirthDate) {
	        lion.util.info("警告", "请选择汇交人出生日期");
	        return false;
	    }
	    var provinceCode = $("#personalInsurInfoForm #province").val();
	    if (provinceCode === null || "" === provinceCode) {
	        lion.util.info("警告", "请选择省/直辖市");
	        return false;
	    }
	    
	    var city = $("#personalInsurInfoForm #city").val();
	    if (city === null || "" === city) {
	        lion.util.info("警告", "请选择市/城区 ");
	        return false;
	    }
	    
	    var county = $("#personalInsurInfoForm #county").val();
	    if (county === null || "" === county) {
	        lion.util.info("警告", "请选择县/地级市");
	        return false;
	    }
	    var pSettleDispute = $("#personalInsurInfoForm #pSettleDispute").val();
	    if (pSettleDispute === null || "" === pSettleDispute) {
	        lion.util.info("警告", "请选择争议处理方式");
	        return false;
	    }
	    // 仲裁机构名称
		if (pSettleDispute === "2") {
			var joinParbOrgName = $("#personalInsurInfoForm #joinParbOrgName").val();
			if (joinParbOrgName === "") {
				lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
				return false;
			}
		}
	}
	//如果不是现金提示以下信息
	if($("#moneyinType").val() !== "C" && $("#moneyinType").val() !== "R" && $("#moneyinType").val() !== "P"){
		//不用校验银行账号信息
		if ($("#premFrom").val()==="1" || $("#premFrom").val()==="3") {
			var bankCode = $("#bankCode").val();
			if (bankCode === null || "" === bankCode) {
				lion.util.info("警告", "交费开户行不能为空");
				return false;
			}
			var bankaccNo = $("#bankaccNo").val();
			if (bankaccNo === null || "" === bankaccNo) {
				lion.util.info("警告", "银行账号不能为空");
				return false;
			}
			var bankBranchName = $("#bankBranchName").val();
			if (bankBranchName === null || "" === bankBranchName) {
				lion.util.info("警告", "开户行名不能为空");
				return false;
			}
		}
	}
	
	
   
    var insurDurUnit = $("#proposalInfoForm #insurDurUnit").val();
    if (insurDurUnit === null || "" === insurDurUnit) {
        lion.util.info("警告", "请选择保险期间单位");
        return false;
    }
     
    var effectType = $("#proposalInfoForm #effectType").val();
    if (effectType === null || "" === effectType) {
        lion.util.info("警告", "请选择生效方式");
        return false;
    }else{
    	if (effectType === 1){
    	   	var speEffectDate = $("#proposalInfoForm #speEffectDate").val();
            if (speEffectDate === null || "" === speEffectDate) {
                lion.util.info("警告", "请选择指定生效日");
                return false;
            }
        }   	
    }
    
    var firstChargeDate = $("#proposalInfoForm #firstChargeDate").val();
    if (firstChargeDate === null || "" === firstChargeDate) {
        lion.util.info("警告", "请选择首期扣款截止日期");
        return false;
    }
    
    var moneyItrvl = $("#payInfoForm #moneyItrvl").val();
    if (moneyItrvl === null || "" === moneyItrvl) {
        lion.util.info("警告", "交费方式不能为空");
        return false;
    }

//    var moneyinType = $("#payInfoForm #moneyinType").val();
//    if (moneyinType === null || "" === moneyinType) {
//        lion.util.info("警告", "请选择交费形式");
//        return false;
//    }

    var premFrom = $("#payInfoForm #premFrom").val();
    if (premFrom === null || "" === premFrom) {
        lion.util.info("警告", "请选择保费来源");
        return false;
    }

//    var renewalChargeFlag = $("#payInfoForm #renewalChargeFlag").val();
//    if (renewalChargeFlag === null || "" === renewalChargeFlag) {
//        lion.util.info("警告", "请选择是否续期扣款");
//        return false;
//    }else{
//    	if(renewalChargeFlag==='Y'){
//		 var chargeDeadline = $("#payInfoForm #chargeDeadline").val();
//		    if (chargeDeadline === null || "" === chargeDeadline) {
//		        lion.util.info("警告", "请选择扣款截止日期");
//		        return false;
//		    }
//   	}
//   }
 
   

    var multiRevFlag = $("#payInfoForm #multiRevFlag").val();
    if (multiRevFlag === null || "" === multiRevFlag) {
        lion.util.info("警告", "请选择是否多期暂交");
        return false;
    }else{
    	if(multiRevFlag==='Y'){
      		 var multiTempYear = $("#payInfoForm #multiTempYear").val();
      		    if (multiTempYear === null || "" === multiTempYear) {
      		        lion.util.info("警告", "请输入多期暂交年数");
      		        return false;
      		    }	
      	}
      }   
    var underNoticeType = $("#printInfoForm #underNoticeType").val();
    if (underNoticeType === null || "" === underNoticeType) {
        lion.util.info("警告", "请选择合同打印方式");
        return false;
    }
    var listFlag = $("#printInfoForm #listFlag").val();
    if (listFlag === null || "" === listFlag) {
		lion.util.info("警告", "请选择清单标记");
		return false;
	}
    //清单标记为“无清单”时，清单打印 &个人凭证打印不可用 ，不等于的时候再去校验必录校验
    if(listFlag !== "N"){
    	var applProperty = $("#printInfoForm #listPrint").val();
        if (applProperty === null || "" === applProperty) {
            lion.util.info("警告", "请选择清单打印");
            return false;
        }
    	var personalIdPrint = $("#printInfoForm #personalIdPrint").val();
    	if (personalIdPrint === null || "" === personalIdPrint) {
    	    lion.util.info("警告", "请选择个人凭证打印");
    	    return false;
    	  }
    }
    
    var giftInsFlag = $("#printInfoForm #giftInsFlag").val();
    if (giftInsFlag === null || "" === giftInsFlag) {
        lion.util.info("警告", "请选择赠送险标记");
        return false;
    }

    var polProperty = $("#printInfoForm #polProperty").val();
    if (polProperty === null || "" === polProperty) {
        lion.util.info("警告", "请选择保单性质");
        return false;
    }


    var unusualFlag = $("#printInfoForm #unusualFlag").val();
    if (unusualFlag === null || "" === unusualFlag) {
        lion.util.info("警告", "请选择异常告知");
        return false;
    }
    return true;
}

// });
// 暂存功能
$("#btnSave").click(function(){
        //暂存投保单号不能为空
        if($("#applNo").val()===""){
            lion.util.info("请输入投保单号");
            return false;
        }
        //正则表达式校验投保单号
        if(!/^[0-9]{16}$/.test($("#applNo").val())){
            lion.util.info("投保单号输入不正确");
            return false;
        }
        // 获取要约信息下面的所有险种信息
        var getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
        var sgGrpInsurApplVo = {};
        sgGrpInsurApplVo.sgGrpApplInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm
                .serializeObject();
        sgGrpInsurApplVo.sgGrpPayInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.payInfoForm
                .serializeObject();
        var listType = $("#listType").val();
        //根据汇交人类型序列化vo对象
        if("G" === listType){
        	//团单
            sgGrpInsurApplVo.sgGrpInsurInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.grpInsurInfoForm
                    .serializeObject();
        }else if("P" === listType){
           $("#personalInsurInfoForm #joinBirthDate").attr("disabled",false);
        	//个人
            sgGrpInsurApplVo.sgGrpPersonalInsurInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.personalInsurInfoForm
                    .serializeObject();
        }
        sgGrpInsurApplVo.sgGrpPrintInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.printInfoForm
                .serializeObject();
        sgGrpInsurApplVo.sgGrpProposalInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.proposalInfoForm 
        		.serializeObject();
        sgGrpInsurApplVo.sgGrpVatInfoVo = com.orbps.contractEntry.sgGrpInsurAppl.vAddTaxInfoForm
                .serializeObject();
        sgGrpInsurApplVo.insuredGroupModalVos = com.orbps.common.proposalGroupList;
        //alert("责任信息"+JSON.stringify(com.orbps.common.proposalGroupList));
        //sgGrpInsurApplVo.organizaHierarModalVos = com.orbps.contractEntry.sgGrpInsurAppl.oranLevelList;
        //循环获取的全部主险
        for (var i = 0; i < getAddRowsData.length; i++) {
        	var addinsuranceVos = getAddRowsData[i];
        	//清空当前险种下的责任信息
        	addinsuranceVos.responseVos = [];
        	//循环责任的list，如果责任里的busiPrdCode和主险下的一样，把责任信息放到险种下的responseVos里
        	for (var j = 0; j < com.orbps.common.list.length; j++) {
        		//取到的责任信息
				var reponseVos = com.orbps.common.list[j];
				//当险种的busiPrdCode等于责任的时候，往险种下的责任list中放值	
				if(reponseVos.busiPrdCode === addinsuranceVos.busiPrdCode){
					addinsuranceVos.responseVos.push(reponseVos);
				}
			}
		}
       // alert(JSON.stringify(getAddRowsData));
        sgGrpInsurApplVo.addinsuranceVos = getAddRowsData;
        sgGrpInsurApplVo.chargePayGroupModalVos = com.common.chargePayList;
         sgGrpInsurApplVo.listType = $("#listType").val();
        sgGrpInsurApplVo.taskInfo={};
        sgGrpInsurApplVo.taskInfo.taskId = com.orbps.contractEntry.sgGrpInsurAppl.taskId;
        //alert("清汇暂存放的信息"+JSON.stringify(sgGrpInsurApplVo));
        lion.util
                .postjson(
                        '/orbps/web/orbps/contractEntry/sg/tempSave',
                        sgGrpInsurApplVo,
                        com.orbps.contractEntry.sgGrpInsurAppl.successbtnSave,
                        null,
                        null);
});
//添加暂存成功回调函数
com.orbps.contractEntry.sgGrpInsurAppl.successbtnSave=function (data, arg) {
	  if (data.retCode==="1"){
	        lion.util.info("提示", "暂存清汇录入信息成功");
	    }else{
	        lion.util.info("提示", "暂存失败，失败原因："+data.errMsg);
	    }
}

//清空按钮功能
$("#btnClear").click(function(){
	var applNo = $("#applNo").val();
	var giftInsFlag = $("#giftInsFlag").val();
    $("#applInfoForm").reset();
    $("#grpInsurInfoForm").reset();
    $("#payInfoForm").reset();
    $("#personalInsurInfoForm").reset();
    $("#printInfoForm").reset();
    $("#proposalInfoForm").reset();
    $("#vAddTaxInfoForm").reset();
    $(".fa").removeClass("fa-warning");
    $(".fa").removeClass("fa-check");
    $(".fa").removeClass("has-success");
    $(".fa").removeClass("has-error");
    $("#applNo").val(applNo);
	$("#giftInsFlag").combo("val",giftInsFlag);
});


//收付费分组
$("#btnChargePay")
        .click(
                function() {
                    com.orbps.contractEntry.sgGrpInsurAppl.addDialog.empty();
                    com.orbps.contractEntry.sgGrpInsurAppl.addDialog
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
//添加提交成功回调函数
com.orbps.contractEntry.sgGrpInsurAppl.successSubmit=function (data, arg) {
	  if (data.retCode==="1"){
	        lion.util.info("提示", "提交清汇录入信息成功");
	      //提交成功，判断有无清单有清单，跳转按钮置成可点操作
	      //如果是团单清单标记 是无清单 置灰跳转按钮
	        if("L" === $("#listFlag").val()){
	        	$("#btnlocation").attr("disabled",false);//可点
	        }else{
	        	$("#btnlocation").attr("disabled",true);//不可点
	        }	        
	    }else{
	        lion.util.info("提示", "提交失败，失败原因："+data.errMsg);
	    }
}
$("#btnlocation").click(function(){
	var applNo = $("#applNo").val();
	var taskId = com.orbps.contractEntry.sgGrpInsurAppl.taskId;
	var applNoTaskId = applNo + "," + taskId;
	com.ipbps.menuFtn("orbps/orbps/contractEntry/listImport/html/offlineListImport.html",applNoTaskId); 
});