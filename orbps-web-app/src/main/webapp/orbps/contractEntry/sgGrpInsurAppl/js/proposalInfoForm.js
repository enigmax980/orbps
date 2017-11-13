com.orbps.common = {};
com.orbps.common.list = new Array();
com.orbps.common.allList = new Array();
com.orbps.contractEntry.sgGrpInsurAppl.proposalInfoForm = $("#proposalInfoForm");
//新建contractEntry.sgGrpInsurAppl命名空间
com.orbps.contractEntry.sgGrpInsurAppl.polList = new Array();
//新建险种集合
com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode = [];
com.orbps.contractEntry.sgGrpInsurAppl.selectData;
com.orbps.common.busiPrdCode;
// 基本信息校验规则
com.orbps.contractEntry.sgGrpInsurAppl.proposalValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        messages : {
            insuredTotalNum : {
                required : "被保险人总数不能为空",
                isIntGteZero : "请输入>=0的整数"
            },
            sumPrem : {
                required : "保费合计不能为空",
                isFloatGteZero : "保费合计必须>=0"
            },
        },
        rules : {
            insuredTotalNum : {
                required : true,
                isIntGteZero : true
            },
            sumPrem : {
                required : true,
                isFloatGteZero : true
            },
        },
        invalidHandler : function(event, validator) {
            Metronic.scrollTo(error2, -200);
        },

        errorPlacement : function(error, element) {
            var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            if (icon.attr('title')
                    || typeof icon.attr('data-original-title') !== 'string') {
                icon.attr('data-original-title', icon.attr('title') || '')
                        .attr('title', error.text())
            }
        },

        highlight : function(element) {
            $(element).closest('.col-md-2').removeClass("has-success")
                    .addClass('has-error');
        },

        success : function(label, element) {
            var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass(
                    'has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
    });
}

$(function() {
	//要约信息长度为2000个字符，暂时放开特别约定长度限制
//	$("#proposalInfoForm #convention").on(
//			'keyup',
//			function(event) {
//	var convention = $("#proposalInfoForm #convention").val();
//	var len = convention.length;
//	var reLen = 0;
//	for (var i = 0; i < len; i++) {        
//		if (convention.charCodeAt(i) < 27 || convention.charCodeAt(i) > 126) {
//			// 全角    
//			reLen += 2;
//		} else {
//			reLen++;
//		}
//	}
//	if(reLen>2000){
//		convention = convention.substring(0,convention.length-1);
//		$("#proposalInfoForm #convention").val(convention);
//	}
//	});
	
	
	if("" === $("#firstChargeDate").val()){
	//设置日期，当前日期的后15天
	var myDate = new Date(); //获取今天日期	
	myDate.setDate(myDate.getDate() + 15);
	$("#firstChargeDate").val(myDate.format("yyyy-MM-dd"));
	}
	//校验函数初始化
	com.orbps.contractEntry.sgGrpInsurAppl.proposalValidateForm(com.orbps.contractEntry.sgGrpInsurAppl.proposalInfoForm);
	// 增加表格
	$("#btnAdd").click(function() {
	    $("#bsInfoListTb").editDatagrids("addRows");
	});

	// 删除表格
	$("#btnDel").click(function() {
    	com.orbps.contractEntry.sgGrpInsurAppl.selectData = $("#bsInfoListTb").editDatagrids("getSelectRows");
        if(lion.util.isEmpty(com.orbps.contractEntry.sgGrpInsurAppl.selectData)){
        	lion.util.info("警告", "请选择要删除的主险!");
        	return false;
        }
        if(com.orbps.common.proposalGroupList.length > 0){
			if(confirm("请先删除被保人分组中删除该险种,再进行删除要约险种删除")){
				
			}else{
				return false;
			}
		}
        if(Object.prototype.toString.call(com.orbps.contractEntry.sgGrpInsurAppl.selectData) === '[object Array]'){
        	if(com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList.length>0){
        		for (var i = 0; i < com.orbps.contractEntry.sgGrpInsurAppl.selectData.length; i++) {
    				var busiPrdCode = com.orbps.contractEntry.sgGrpInsurAppl.selectData[i].busiPrdCode;
    				if(com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList[0].busiPrdCode===busiPrdCode){
    					lion.util.info("提示","【"+busiPrdCode+"】是契约受理的险种,不能进行删除,请重新选则!");
    					return false;
    				}
    				
    			}
        	}
        	for (var i = 0; i < com.orbps.contractEntry.sgGrpInsurAppl.selectData.length; i++) {
				var busiPrdCode = com.orbps.contractEntry.sgGrpInsurAppl.selectData[i].busiPrdCode;
				// 删除所有该险种的责任
				for (var x = 0; x < com.orbps.common.list.length; x++) {
					var array_element = com.orbps.common.list[x];
					var productCodes = array_element.productCode;
					var productCodesStart = productCodes.split("-")[0];
					if(busiPrdCode===productCodesStart){
						com.orbps.common.list.splice(x,1);
						for (var x = 0; x < com.orbps.common.list.length; x++) {
							var array_element = com.orbps.common.list[x];
							var productCodes = array_element.productCode;
							var productCodesStart = productCodes.split("-")[0];
							if(busiPrdCode===productCodesStart){
								com.orbps.common.list.splice(x,1);
								for (var x = 0; x < com.orbps.common.list.length; x++) {
									var array_element = com.orbps.common.list[x];
									var productCodes = array_element.productCode;
									var productCodesStart = productCodes.split("-")[0];
									if(busiPrdCode===productCodesStart){
										com.orbps.common.list.splice(x,1);
										for (var x = 0; x < com.orbps.common.list.length; x++) {
											var array_element = com.orbps.common.list[x];
											var productCodes = array_element.productCode;
											var productCodesStart = productCodes.split("-")[0];
											if(busiPrdCode===productCodesStart){
												com.orbps.common.list.splice(x,1);
											}
										}
									}
								}
							}
						}
					}
				}
			}
        }else{
        	if(com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList.length>0){
				var busiPrdCode = com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode;
				if(com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList[0].busiPrdCode===busiPrdCode){
					lion.util.info("提示","【"+busiPrdCode+"】是契约受理的险种,不能进行删除,请重新选则!");
					return false;
				}
        	}
        	// 删除所有该险种的责任
        	for (var x = 0; x < com.orbps.common.list.length; x++) {
        		var array_element = com.orbps.common.list[x];
        		var productCodes = array_element.productCode;
        		var productCodesStart = productCodes.split("-")[0];
        		if(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode===productCodesStart){
        			com.orbps.common.list.splice(x,1);
        			for (var x = 0; x < com.orbps.common.list.length; x++) {
        				var array_element = com.orbps.common.list[x];
        				var productCodes = array_element.productCode;
        				var productCodesStart = productCodes.split("-")[0];
        				if(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode===productCodesStart){
        					com.orbps.common.list.splice(x,1);
        					for (var x = 0; x < com.orbps.common.list.length; x++) {
        						var array_element = com.orbps.common.list[x];
        						var productCodes = array_element.productCode;
        						var productCodesStart = productCodes.split("-")[0];
        						if(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode===productCodesStart){
        							com.orbps.common.list.splice(x,1);
        							for (var x = 0; x < com.orbps.common.list.length; x++) {
        								var array_element = com.orbps.common.list[x];
        								var productCodes = array_element.productCode;
        								var productCodesStart = productCodes.split("-")[0];
        								if(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode===productCodesStart){
        									com.orbps.common.list.splice(x,1);
        								}
        							}
        						}
        					}
        				}
        			}
        		}
        	}
        }
        $("#bsInfoListTb").editDatagrids("delRows");
	});

	//责任信息
	$("#btnSelect")
	        .click(
	                function() {
	                	// 获取选择的险种信息
	                    com.orbps.contractEntry.sgGrpInsurAppl.selectData = $("#bsInfoListTb").editDatagrids(
	                            "getSelectRows");
	                    // 判断选择的是否是一个主险，判断是否添加主险信息
	                    if (null === com.orbps.contractEntry.sgGrpInsurAppl.selectData){
	                        lion.util.info("警告", "请选择主险信息");
	                        return false;
	                    }else{
	                    	if((com.orbps.contractEntry.sgGrpInsurAppl.selectData.length === 0) || (com.orbps.contractEntry.sgGrpInsurAppl.selectData.length > 1)){
	                    		 lion.util.info("警告", "请选择一个主险信息");
	                    		 return false;
	                    	}
	                    	if ((null === com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode)) {
	                            lion.util.info("警告", "请录入险种代码");
	                            return false;
	                        }
	                    }
	                    com.orbps.common.busiPrdCode = com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode;
	                    // 清空modal
	                    com.orbps.contractEntry.sgGrpInsurAppl.addDialog.empty();
	                    // 加载责任modal
	                    com.orbps.contractEntry.sgGrpInsurAppl.addDialog
	                            .load(
	                                    "/orbps/orbps/public/modal/html/insurRespModal.html",
	                                    function() {
	                                        // 初始化插件
	                                    	$(this).modal('toggle');
	                                        // 初始化editTable组件
	                                        $("#coverageInfo_list")
	                                                .editDatagridsLoadById();
	                                        //alert("长度是"+JSON.stringify(com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode));
	                                        if(com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode.length>0){
	                                        	for (var j = 0; j < com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode.length; j++) {
	                                        		var array_element = com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode[j];
	                                        		if(array_element===com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode){
	                                        			lion.util.postjson(
	                                                            '/orbps/web/orbps/contractEntry/grp/search',
	                                                            com.orbps.contractEntry.sgGrpInsurAppl.selectData,
	                                                            com.orbps.contractEntry.sgGrpInsurAppl.successbusiPrdCode,
	                                                            null,
	                                                            null);
	                                        			// 延时回显
	                                        			setTimeout(function() {
	                                        				//alert("眼屎回显");
	                                        				for (var k = 0; k < com.orbps.common.allList.length; k++) {
																var array_element = com.orbps.common.allList[k].productCode;
																for (var m = 0; m < com.orbps.common.list.length; m++) {
																	var productCode = com.orbps.common.list[m].productCode;
																	if(array_element === productCode){
																		com.orbps.common.allList.splice(k, 1,com.orbps.common.list[m]);//替换方法
																	}
																}
															}
	                                        				// 回显list
	                                        				$('#coverageInfo_list').editDatagrids("bodyInit", com.orbps.common.allList);
	                                        				// 循环已经保存的责任信息
	                                        				if ((com.orbps.common.list).length>0) {
	                                        					for (var i = 0; i < com.orbps.common.list.length; i++) {
	                                        						var array_element = com.orbps.common.list[i];
	                                        						// 判断选择的责任信息是一条还是多条
	                                        						if (array_element.length >= 0) {
	                                        							var productCode = array_element[0].productCode;
	                                        							if(productCode.indexOf("-") > 0 ){
	                                        								var result = productCode.substr(0, productCode.indexOf('-'));
	                                        								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === result) {
	                                        									// 回显责任信息
	                                        									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                        								}
	                                        							}else{
	                                        								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === productCode) {
	                                        									// 回显责任信息
	                                        									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                        								}
	                                        							}
	                                        						} else {
	                                        							var productCode = array_element.productCode;
	                                        							if(productCode.indexOf("-") > 0 ){
	                                        								var result = productCode.substr(0, productCode.indexOf('-'));
	                                        								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === result) {
	                                        									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                        								}
	                                        							}else{
	                                        								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === productCode) {
	                                        									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                        								}
	                                        							}
	                                        						}
	                                        					}
	                                        				}
	                                        			}, 2000);
	                                        			return false;
	                                        		} else {
	                                        			if ((j+1) === com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode.length) {
	                                        				// 将险种放到集合中
	                                        				com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode.push(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode);
	                                        				lion.util.postjson(
	                                        						'/orbps/web/orbps/contractEntry/grp/search',
	                                        						com.orbps.contractEntry.sgGrpInsurAppl.selectData,
	                                        						com.orbps.contractEntry.sgGrpInsurAppl.successbusiPrdCode,
	                                        						null,
	                                        						null);
	                                        			}
	                                        		}
	                                        	}
	                                        }else{
	                                        	// 将险种放到集合中
	                                			com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode.push(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode);
	                                			lion.util.postjson(
	                                                    '/orbps/web/orbps/contractEntry/grp/search',
	                                                    com.orbps.contractEntry.sgGrpInsurAppl.selectData,
	                                                    com.orbps.contractEntry.sgGrpInsurAppl.successbusiPrdCode,
	                                                    null,
	                                                    null);
	                                			// 延时回显
	                                			setTimeout(function() {
	                                				for (var k = 0; k < com.orbps.common.allList.length; k++) {
														var array_element = com.orbps.common.allList[k].productCode;
//														alert(array_element+" ====com.orbps.common.allList[k]"+JSON.stringify(com.orbps.common.allList[k]));
														//alert(array_element+" ====com.orbps.common.list"+JSON.stringify(com.orbps.common.list));
														for (var m = 0; m < com.orbps.common.list.length; m++) {
//															for(var j=0 ;j<com.orbps.common.list[m].length;j++){
																var productCode = com.orbps.common.list[m].productCode;
//																alert(array_element +" "+productCode);
																if(array_element === productCode){
																	//com.orbps.common.list[m].subStdPremium = com.orbps.common.allList[k].subStdPremium;
																	com.orbps.common.allList.splice(k, 1,com.orbps.common.list[m]);//替换方法
																}
//															}
															
														}
													}
	                                				// 回显list
	                                				$('#coverageInfo_list').editDatagrids("bodyInit", com.orbps.common.allList);
	                                				// 循环已经保存的责任信息
	                                				if ((com.orbps.common.list).length>0) {
	                                					for (var i = 0; i < com.orbps.common.list.length; i++) {
	                                						var array_element = com.orbps.common.list[i];
	                                						// 判断选择的责任信息是一条还是多条
	                                						if (array_element.length >= 0) {
	                                							var productCode = array_element[0].productCode;
	                                							if(productCode.indexOf("-") > 0 ){
	                                								var result = productCode.substr(0, productCode.indexOf('-'));
	                                								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === result) {
	                                									// 回显责任信息
	                                									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                								}
	                                							}else{
	                                								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === productCode) {
	                                									// 回显责任信息
	                                									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                								}
	                                							}
	                                						} else {
	                                							var productCode = array_element.productCode;
	                                							if(productCode.indexOf("-") > 0 ){
	                                								var result = productCode.substr(0, productCode.indexOf('-'));
	                                								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === result) {
	                                									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                								}
	                                							}else{
	                                								if (com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode === productCode) {
	                                									$("#coverageInfo_list").editDatagrids("selectRows",array_element);
	                                								}
	                                							}
	                                						}
	                                					}
	                                				}
	                                			}, 2000);
	                                			return false;
	                                        }
	                                    });
	                });

    // 初始化校验函数
    com.orbps.contractEntry.sgGrpInsurAppl
            .proposalValidateForm(com.orbps.contractEntry.sgGrpInsurAppl.proposalInfoForm);
    
//    setTimeout(function(){
//    	//是否频次生效下拉框默认显示否
//    	$("#frequencyEffectFlag").combo("val","N");
//    },1000)
    
});


//成功查询险种名称回调函数
com.orbps.contractEntry.sgGrpInsurAppl.successSearchBusiprodName = function(data,arg){
	if("fail"===data){
		lion.util.info("","险种不存在，请重新输入险种代码");
		$("#busiPrdName").val("");
		return false;
	}else{
		$("#busiPrdName").val(data);
	}
	$("#busiPrdName").attr("readOnly",true);
}

//查询责任成功回调函数
com.orbps.contractEntry.sgGrpInsurAppl.successbusiPrdCode = function (data,arg){
	if(data.length===1){
		lion.util.info("提示","该险种无责任!");
		com.orbps.common.allList='';
		$('#coverageInfo_list').editDatagrids("bodyInit", com.orbps.common.allList);
		return false;
	}else{
		if(data[0].productCode===com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode){
			lion.util.info("提示","该险种无责任!");
			com.orbps.common.allList='';
			$('#coverageInfo_list').editDatagrids("bodyInit", com.orbps.common.allList);
			return false;
		}
		$('#coverageInfo_list').editDatagrids("bodyInit", data);
		com.orbps.common.allList = data;
	}
}


//回显保额保费
com.orbps.common.reloadPre = function(productNum,productPremium){
	var getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
	for (var i = 0; i < getAddRowsData.length; i++) {
		var busiPrdCode = getAddRowsData[i].busiPrdCode;
		if(com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode===busiPrdCode){
			com.orbps.contractEntry.sgGrpInsurAppl.selectData.amount = productNum;
			com.orbps.contractEntry.sgGrpInsurAppl.selectData.premium = productPremium;
			getAddRowsData.splice(i,1,com.orbps.contractEntry.sgGrpInsurAppl.selectData);
			break;
		}
	}
	$('#bsInfoListTb').editDatagrids("bodyInit", getAddRowsData);
}
//指定生效日改变页面样式
$("#effectType").change(function(){
	if($("#effectType").val()==="0"){
		$("#speEffectDate").attr("disabled",true);
		$("#speEffectDatebtn").attr("disabled",true);
		$("#speEffectDate").val("");
	}else if($("#effectType").val()==="1"){
		$("#speEffectDate").attr("disabled",false);
		$("#speEffectDatebtn").attr("disabled",false);
	}
});
//当指定生效日期的时候提示用户选择生效日
$("#speEffectDate").blur(function(){
	if($("#effectType").val()==="1"){
		lion.util.info("警告", "请选择生效日");
	}
});		

//生效频率改变页面样式
//$("#frequencyEffectFlag").change(function(){
//	if($("#frequencyEffectFlag").val()==="N"){
//		$("#effectFreq").attr("disabled",true)
//	}else if($("#frequencyEffectFlag").val()==="Y"){
//		$("#effectFreq").attr("disabled",false)
//		var effectFreq = $("#effectFreq").val();
//		if (effectFreq === null || "" === effectFreq) {
//        lion.util.info("警告", "请输入生效频率");
//        return false;
//		}    
//	}else{		
//	}
//});


//$("#proposalInfoForm #effectFreq").blur(
//		function(){
//			var effectFreq = $("#effectFreq").val();
//			var zh_verify = /^[0-9]*[1-9][0-9]*$/;
//			if(!zh_verify.test(effectFreq))
//				lion.util.info("警告", "请输入大于零的整数类型生效频率");
//			return false;
//		});
$('body').delegate('table#bsInfoListTb #amount' , 'blur', function(){
			var amount = $("#amount").val();
			var zh_verify = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
			if(!zh_verify.test(amount))
				lion.util.info("警告", "请输入大于零的保额");
			return false;
});
$('body').delegate('table#bsInfoListTb #premium' , 'blur', function(){
	var premium = $("#premium").val();
	var zh_verify =  /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
	if(!zh_verify.test(premium))
		lion.util.info("警告", "请输入大于零的保费");
	return false;
});

//聚焦险种名称代码事件
$('body').delegate('table#bsInfoListTb #busiPrdName' , 'focus', function(){
	$("#busiPrdName").attr("readOnly",true);
});

$("#proposalInfoForm #insuredTotalNum").blur(
        function() {
            var insuredTotalNum = $("#proposalInfoForm #insuredTotalNum").val();
            if (insuredTotalNum === null || "" === insuredTotalNum
                    || "undefined" === insuredTotalNum) {
                lion.util.info("警告", "被保险人总数不能为空");
                return false;
            }
        });

$("#proposalInfoForm #dateType").blur(function() {
    var dateType = $("#dateType").val();
    if (dateType === null || "" === dateType || "undefined" === dateType) {
        lion.util.info("警告", "保险期间不能为空");
        return false;
    }
});




$("#sumPrem").focus(function(){
	// 获取要约信息下面的所有险种信息
	var getRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
	var sum = 0;
	for(var index in getRowsData){
		var rowData = getRowsData[index];
		if("premium" in rowData){
			if("" !== rowData["premium"]){
				sum += parseFloat(rowData["premium"]);
			}
		}
	}
	if(0 === sum){
		lion.util.info("请添加险种保费");
	}
	$("#sumPrem").val(sum.toFixed(2));
});

//丢失焦点事件(根据险种代码查询险种名称)
$('body').delegate('table#bsInfoListTb #busiPrdCode' , 'blur', function(){
	var getAddrowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
	var busiPrdName = $("#busiPrdName").val();
	var a = $("#busiPrdCode").val();
	if(busiPrdName===""){
		for (var i = 0; i < getAddrowsData.length; i++) {
			var busiPrdCode = getAddrowsData[i].busiPrdCode;
			if(a===busiPrdCode&&a!==""){
				lion.util.info("提示","【"+a+"】险种已经存在，请录入其他险种代码");
				$("#busiPrdCode").val("");
				return false;
			}
		}
	}
	
	if(a!==null){
		var responseVo = {};
		responseVo.busiPrdCode = a;
		// 向后台发送请求
		lion.util.postjson(
				'/orbps/web/orbps/contractEntry/search/searchBusiName',
				responseVo,
				com.orbps.contractEntry.sgGrpInsurAppl.successSearchBusiprodName,
				null,
				null);	
	}else{
		$("#busiPrdName").val("");
		return false;
	}
});

//聚焦险种代码事件
$('body').delegate('table#bsInfoListTb #busiPrdCode' , 'focus', function(){
	var a = $("#busiPrdCode").val();
	if(com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList.length>0){
		if(com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList[0].busiPrdCode===a){
			lion.util.info("提示","【"+a+"】是契约受理的险种,不能修改险种代码和名称!");
			$("#busiPrdCode").val(a);
			$("#busiPrdCode").attr("readOnly",true);
			$("#busiPrdCodeName").attr("readOnly",true);
			return false;
		}
	}
	// 获取要约信息下面的所有险种信息
    var getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
    if(getAddRowsData.length === 0){
    	return false;
    }else{
    	//保险期间类型要跟第一条险种中的保持一致
    	var array_element = getAddRowsData[0].insurDurUnit;
    	$("#insurDurUnit").combo("val",array_element);
    	$("#insurDurUnit").attr("readOnly",true);
    }
});
// 回显保额保费
com.common.reloadPre = function(){
	var amounts = 0.00;
	var premiums = 0.00;
	for (var i = 0; i < com.orbps.common.list.length; i++) {
		var productNum = com.orbps.common.list[i].productNum;
		amounts+=parseFloat(productNum);
		var productPremium = com.orbps.common.list[i].productPremium;
		premiums+=parseFloat(productPremium);
	}
	var insuredGroupModalListVo = [];
	var object = new Object();
//	premiums.replace("0","");
//	amounts.replace("0","");
	object.premium = premiums;
	object.amount = amounts;
	object.busiPrdCode = com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdCode;
	object.busiPrdName = com.orbps.contractEntry.sgGrpInsurAppl.selectData.busiPrdName;
	insuredGroupModalListVo.push(object);
	$('#bsInfoListTb').editDatagrids("bodyInit", insuredGroupModalListVo);
}
