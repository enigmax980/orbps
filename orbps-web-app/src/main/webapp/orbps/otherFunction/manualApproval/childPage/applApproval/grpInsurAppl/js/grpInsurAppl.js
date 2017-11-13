com.orbps.otherFunction = {};
com.orbps.common = {};
com.orbps.otherFunction.manualApproval = {};
com.orbps.otherFunction.manualApproval.childPage = {};
com.orbps.otherFunction.manualApproval.childPage.applApproval = {};
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl = {};
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.apTaskList = new Array();
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.apTaskCount = 0;
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.apTaskType = -1;
/** 新建险种List */
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd = [];
/** 新建全局命名变量TaskID */
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.taskId;
/** 新建险种Vo */
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProdVo;
/** 新建险种责任List */
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos = [];
/** 新建被保人分组 */
com.orbps.common.proposalGroupList = new Array();
/** 新建收付费分组 */
com.common.chargePayList = new Array();
/** 新建结算日期清单 */
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.settleList = new Array();
com.orbps.common.rootIsEdit = false;
com.orbps.common.treeNodeNameList = [];
com.orbps.common.zTrees = [];
com.orbps.common.zNodes = [];
com.orbps.common.oranLevelList = [];
// 编辑或添加对话框
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog = $('#btnModel');
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.bizNo;
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.grpSalesListFormVos = [];
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.healthPublicAmountArray;
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.constructionInfoArray;

//查询成功回调函数
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.successQueryDetail = function(data,args) {
	console.log(data);
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.taskId = data.taskInfo.taskId;
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.grpSalesListFormVos = data.applInfoVo.grpSalesListFormVos;
	setTimeout(function() {
        var jsonStrs = JSON.stringify(data);
        var objs = eval("(" + jsonStrs + ")");
        var form;
        for (y in objs) {
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            var key, value, tagName, type, arr;
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                if("applDate"==key||"appBirthday"==key||"inForceDate"==key||"enstPremDeadline"==key||"constructionDur"==key||"until"==key || "preMioDate" === key){
    				value = new Date(value).format("yyyy-MM-dd");
    			}
                $("[name='" + key + "'],[name='" + key + "[]']").each(
                        function() {
                            tagName = $(this)[0].tagName;
                            type = $(this).attr('type');
                            if (tagName == 'INPUT') {
                                if (type == 'radio') {
                                    $(this).attr('checked',
                                            $(this).val() == value);
                                } else if (type == 'checkbox') {
                                    arr = value.split(',');
                                    for (var i = 0; i < arr.length; i++) {
                                        if ($(this).val() == arr[i]) {
                                            $(this).attr('checked', true);
                                            break;
                                        }
                                    }
                                } else {
                                    $("#" + key).val(value);
                                }
                            } else if (tagName === 'SELECT') {
                                $("#" + key).combo("val", value);
                            } else if (tagName === 'TEXTAREA') {
            					$("#" + key).val(value);
            				}

                        });
            }
        }
        $("#aprovalReason").val(data.aprovalReason);
    	setTimeout(function() {
    		// 将省市县置为可以回显的状态
//    		$(".city").attr("disabled", false);
//    		$(".prov").attr("disabled", false);
//    		$(".dist").attr("disabled", false);
    		$("#applBaseInfoForm #appAddrProv").combo("val",
    				data.applBaseInfoVo.appAddrProv);
    		$("#applBaseInfoForm #appAddrCity").combo("val",
    				data.applBaseInfoVo.appAddrCity);
    		$("#applBaseInfoForm #appAddrCountry").combo("val",
    				data.applBaseInfoVo.appAddrCountry);
    	}, 1000);
        //计算施工总天数
        if(data.specialInsurAddInfoVo.constructionDur !== "" && data.specialInsurAddInfoVo.until !== ""){
        	var Date1 = new Date(data.specialInsurAddInfoVo.constructionDur);
            var Date2 = new Date(data.specialInsurAddInfoVo.until);
            var a = (Date2.getTime() - Date1.getTime())/(24 * 60 * 60 * 1000);
            if(a !== ""){
                $("#totalDays").val(a);
            }
        }
    }, 1000);
  
  
    // 赋值结算日期的的全局变量
    com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.settleList = data.payInfoVo.settlementDate;
    //赋值收付费分组信息为全局变量
    com.common.chargePayList = data.chargePayGroupModalVos;
    // 赋值被保人分组信息为全局变量
	com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
	// 赋值险种信息为全局变量
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd = data.busiPrdVos;
	// 加载险种表格
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.reloadbusiProdTable();
	//跟据险种信息设置页面样式，隐藏特殊险种模块
	if(lion.util.isNotEmpty(data.busiPrdVos[0].busiPrdCodeName)){
	    var flag = data.busiPrdVos[0].busiPrdCodeName.indexOf("建筑");
	    if(flag > 0){
	    	$("#specialInsurAddInfoForm").show();
	    }else{
	    	$("#specialInsurAddInfoForm").hide();
	    }
	}
	// 取到组织树节点信息
	if (data.organizaHierarModalVos.length > 0) {
		com.orbps.common.zTrees = com.orbps.common
				.OHMFtn(data.organizaHierarModalVos);
	} else {
		com.orbps.common.zTrees = [ {
			id : 1,
			pId : "#",
			name : "默认节点(请重命名)",
			open : false,
			nocheck : false,
			checked : true,
			noRemoveBtn : true,
			noRenameBtn : true,
			noEditBtn : true,
			chkDisabled : false,
			isEdit : false,
			children : []
		} ];
	}
	com.orbps.common.oranLevelList = data.organizaHierarModalVos;
	console.log(data);
	setTimeout(function(){
    	// 跟据险种信息设置页面样式，隐藏特殊险种模块
		$("#specialInsurAddInfoForm").hide();
		var hide1 = 0;
		var hide2 = 0;
		$("#tab1").hide();//表头
		$("#tab2").hide();
		$("#tab3").hide();
		//循环要约险种的险种。
		for(var i = 0; i < data.busiPrdVos.length; i++){
			var a = data.busiPrdVos[i].busiPrdCode;
			//健康险的比对
			for(var j = 0; j < com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.healthPublicAmountArray.length; j++){
				var array_element = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.healthPublicAmountArray[j];
				//如果险种中有健康险的险种
				if(a === array_element){
					$("#specialInsurAddInfoForm").show();
					$("#tab1").show();
					hide2 = 1;
					break;
				}
				
			}
			//建筑险的对比
			for(var k = 0; k < com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.constructionInfoArray.length; k++){
				var array_elements = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.constructionInfoArray[k];
				if(a === array_elements){
					$("#specialInsurAddInfoForm").show();
					$("#tab3").show();
					hide1 = 1;
					break;
				}
			}
		}
		if(hide2===0&&hide1===0){
			$("#specialInsurAddInfoForm").hide();
		}
		/***
		 * 查询险种中是否有基金险
		 */
		var busiPrdCode = "";
		for (var i = 0; i < data.busiPrdVos.length; i++) {
			if(busiPrdCode === ""){
				busiPrdCode = data.busiPrdVos[i].busiPrdCode;
			}else{
				busiPrdCode = busiPrdCode + "," + data.busiPrdVos[i].busiPrdCode;
			}
		}
		lion.util.postjson("/orbps/web/orbps/contractEntry/read/queryFundInsurInfo",busiPrdCode,function(data){
				if(data === "1"){
					//有基金险
					$("#specialInsurAddInfoForm").show();
					$("#tab2").show();
				}else{
					$("#tab2").hide();
				}
				com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.checkTabshowOrHidden();
			})
	},1000);
};

//重新加载险种表格
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.reloadbusiProdTable = function () {
	$('#fbp-editDataGrid').find("tbody").empty();
	if (com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd !== null && com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd.length; i++) {
			var healthInsurFlag = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].healthInsurFlag;
			if(healthInsurFlag!==undefined && healthInsurFlag !== null){
				healthInsurFlag =com.orbps.publicSearch.speciBusinessLogo(healthInsurFlag);
			}else{
				healthInsurFlag = '';
			}
			var insurDurUnit = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].insurDurUnit;
			if(insurDurUnit !== undefined && insurDurUnit !== null){
				insurDurUnit = com.orbps.publicSearch.insurDurUnit(insurDurUnit);
			}else{
				insurDurUnit="";
			}
			$('#fbp-editDataGrid').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.queryProductRdio();' id='insuredRad"
					        + i
                            + "'name='insuredRad' value='"
                            + i
                            + "'></td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].busiPrdCode
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].busiPrdCodeName
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].amount
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].premium
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].insuredNum
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].insurDur
							+ "</td><td>"
							+ insurDurUnit
							+ "</td><td>"
							+ healthInsurFlag
							+ "</td></tr>");
		}
	} else {
		$('#fbp-editDataGrid').find("tbody").append("<tr><td colspan='9' align='center'>无记录</td></tr>");
	}
};



//审批提交
$("#btnApproval").click(function(){
    var grpInsurApplVo = {};
    grpInsurApplVo.applInfoVo = {};
    grpInsurApplVo.taskInfo = {};
    grpInsurApplVo.taskInfo.taskId = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.taskId;
    grpInsurApplVo.applInfoVo.applNo = $("#applNo").val();
    grpInsurApplVo.approvalState = $("#approvalFlag").val();
    grpInsurApplVo.note = $("#note").val();
    lion.util.postjson("/orbps/web/orbps/otherfunction/manualapproval/grpapplapproval",grpInsurApplVo,com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.successSubmitDetail,null,null);
});


//审批结果提交成功回调函数
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.successSubmitDetail = function(data,args){
	if (data.retCode==="1"){
        lion.util.info("提示", "提交成功");
    }else{
        lion.util.info("提示", "提交失败，失败原因："+data.errMsg);
    }
};




//日期回显long转String
Date.prototype.format = function(fmt)   { 
	//author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
};  


com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.queryProductRdio = function () {
		var radioVal;
		var temp = document.getElementsByName("insuredRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProdVo = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[radioVal];
	};
	
// 责任信息
	$("#btnSelect").click(function() {
		for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd.length; i++) {
			var busiPrdCode = com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].busiPrdCode;
			if(busiPrdCode===com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProdVo.busiPrdCode){
				com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos=com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.busiProd[i].responseVos;
				break;
			}
		}
        // 清空modal
		com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.empty();
        // 加载责任modal
		com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.load(
            "/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
                // 初始化插件
            	$(this).modal('toggle');
            	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.reloadProductTable();
            });
    });	
	

//责任回显
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.reloadProductTable = function () {
	$('#coverageInfo_list').find("tbody").empty();
	if (com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos !== null && com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos.length; i++) {
			$('#coverageInfo_list').find("tbody")
					.append("<tr><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos[i].productCode
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos[i].productName
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos[i].productNum
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos[i].productPremium
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.responseVos[i].subStdPremium
							+ "</td></tr>");
		}
	} else {
		$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
	}
}

//被保人分组
$("#btnGoup").click(function() {
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.empty();
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.load("/orbps/orbps/public/searchModal/html/insuredGroupModal.html",function(){
		  $(this).modal('toggle');
		  $(this).comboInitLoad();
		  $(this).combotreeInitLoad();
		// 刷新table
	  });
	  setTimeout(function(){
		  // 回显
		  com.orbps.common.reloadProposalGroupModalTable();
	  },100);
});

// 组织层次分组
$("#btnOranLevel")
        .click(
                function() {
                	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.empty();
                	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog
                            .load(
                                    "/orbps/orbps/public/modal/html/organizaHierarModal.html",
                                    function() {
                                        $(this).modal('toggle');
                                        // combo组件初始化
                                        $(this).comboInitLoad();
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
                });

//收付费分组
$("#btnChargePay")
        .click(
                function() {
                	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.empty();
                	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog
                            .load(
                                    "/orbps/orbps/public/searchModal/html/chargePayGroupModal.html",
                                    function() {
                                        $(this).modal('toggle');
                                        // combo组件初始化
                                        $(this).comboInitLoad();
                                    });
                    setTimeout(function() {
                    	com.common.reloadPublicChargePayModalTable();
                    },600);
                });

//定期结算日期
$("#btnStlDate")
		.click(
				function() {
					com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.empty();
					com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog
							.load(
									"/orbps/orbps/public/searchModal/html/settlementDateModal.html",
									function() {
										$(this).modal('toggle');
									});
					setTimeout(function() {
						// 回显
						com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.reloadsettlementInsuredModalTable();
					}, 100);
				});
// 重新加载表格
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.reloadsettlementInsuredModalTable = function () {
    $('#settlementDataGrid').find("tbody").empty();
    if (com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.settleList != null && com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.settleList.length > 0) {
        for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.settleList.length; i++) {
        	var settleList = new Date(com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.settleList[i]).format("yyyy-MM-dd");
        	$('#settlementDataGrid').find("tbody")
                    .append("<tr><td >"
                            + settleList
                            + "</td></tr>"
                            );
        }
    } else {
        $('#settlementDataGrid').find("tbody").append("<tr><td colspan='1' align='center'>无记录</td></tr>");
    }
}

$(function() {
    // 时间控件初始化
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    //页面加载时先隐藏网点信息
    $("#worksiteHideDiv").hide();
    
    // datagrid组件初始化
    $("*").datagridsInitLoad();

    // combo组件初始化
    $("*").comboInitLoad();
    
    //省市县三级联动
    $("#citySelect").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        required:false
    });
    
    /* 根节点模型 */
    com.orbps.common.OHMFootTemp = {
    	id : null,
    	pId : null,
    	name : null,
    	open : false,
    	nocheck : false,
    	checked : false,
    	noRemoveBtn : true,
    	noRenameBtn : true,
    	noEditBtn : true,
    	chkDisabled : false,
    	isEdit : true,
    	children:[]
    }

    /* 子节点模型 */
    com.orbps.common.OHMTemp = {
    	id : null,
    	pId : null,
    	name : null,
    	checked : false,
    	noEditBtn : false,
    	noRemoveBtn : false,
    	isEdit : true,
    	children:[]
    }

    /* 组织层次信息递归遍历树   */
    com.orbps.common.OHMFtn = function(organizaHierarModalVos){
    	var ztreeVos = [];
    	if(lion.util.isNotEmpty(organizaHierarModalVos)){
    		com.orbps.common.rootIsEdit = true;
    		for (var i = 0; i < organizaHierarModalVos.length; i++) {
    			var organizaHierarModalVo = organizaHierarModalVos[i];
    			if(organizaHierarModalVo.isRoot==="Y"){
    				var ztreeVo = {
    						id : organizaHierarModalVo.levelCode,
    						pId : "#",
    						name : organizaHierarModalVo.companyName,
    						open : false,
    						nocheck : false,
    						checked : false,
    						noRemoveBtn : true,
    						noRenameBtn : true,
    						noEditBtn : true,
    						chkDisabled : false,
    						isEdit : true,
    						children:[]
    					};
    				com.orbps.common.OHMChildFtn(organizaHierarModalVos, ztreeVo);
    				ztreeVos.push(ztreeVo);
    			}
    		}
    	}
    	return ztreeVos;
    }

    com.orbps.common.OHMChildFtn = function(organizaHierarModalVos, ztreeVo){
    	for (var i = 0; i < organizaHierarModalVos.length; i++) {
    		var organizaHierarModalVo = organizaHierarModalVos[i];
    		if(organizaHierarModalVo.prioLevelCode === ztreeVo.id){
    			var ztreeVoChild = {
    					id : organizaHierarModalVo.levelCode,
    					pId : organizaHierarModalVo.prioLevelCode,
    					name : organizaHierarModalVo.companyName,
    					checked : false,
    					noEditBtn : false,
    					noRemoveBtn : false,
    					isEdit : true,
    					children:[]
    				};
    			com.orbps.common.OHMChildFtn(organizaHierarModalVos, ztreeVoChild);
    			ztreeVo.children.push(ztreeVoChild);
    		}
    	}
    }
	 // 设置页面元素只读
	$("#applInfoForm input[type='text']").attr("readOnly",true);
	$("#applInfoForm select").attr("readOnly",true);

	$("#vatInfoForm input[type='text']").attr("readOnly",true);
	$("#vatInfoForm select").attr("readOnly",true);
	$("#applBaseInfoForm input[type='text']").attr("readOnly",true);
	$("#applBaseInfoForm select").attr("readOnly",true);
	$("#proposalInfoForm input[type='text']").attr("readOnly",true);
	$("#proposalInfoForm select").attr("readOnly",true);
	$("#proposalInfoForm #specialPro").attr("readOnly",true);
	$("#payInfoForm input[type='text']").attr("readOnly",true);
	$("#payInfoForm select").attr("readOnly",true);
	$("#specialInsurAddInfoForm input[type='text']").attr("readOnly",true);
	$("#specialInsurAddInfoForm select").attr("readOnly",true);
	$("#printInfoForm input[type='text']").attr("readOnly",true);
	$("#printInfoForm select").attr("readOnly",true);
	$("#applDate").attr("disabled",true);
	$("#applDateBtn").attr("disabled",true);
	$("#inForceDate").attr("disabled",true);
	$("#inForceDateBtn").attr("disabled",true);
	$("#enstPremDeadline").attr("disabled",true);
	$("#enstPremDeadlineBtn").attr("disabled",true);
	$("#constructionDur").attr("disabled",true);
	$("#constructionDurBtn").attr("disabled",true);
	$("#until").attr("disabled",true);	
	$("#untilBtn").attr("disabled",true);
	$(".city").attr("disabled", true);
	$(".prov").attr("disabled", true);
	$(".dist").attr("disabled", true);
	//$(".btn default").attr("disabled",true);
	lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
			function (data, arg) {
		com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.healthPublicAmountArray = [];
		com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
		com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
			},null, null);
    //服务器测试
    var dataCipher=com.ipbps.getDataCipher();
    var url= '/orbps/web/authSupport/check?serviceName=cntrEntryGrpInsurApplService&dataCipher='+dataCipher;
    lion.util.postjson(url,null,com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.successQueryDetail,null,null);
//	var taskInfo = {};
//	taskInfo.bizNo = "2017032400020110"
//	lion.util.postjson('/orbps/web/orbps/contractEntry/grp/query',taskInfo,com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.successQueryDetail,null,null);
});

//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.empty();
	com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.grpSalesListFormVos);
	});
});
//公共保额使用范围change事件
$("#specialInsurAddInfoForm #comInsurAmntUse").change(function(){
	var comInsurAmntUse = $("#specialInsurAddInfoForm #comInsurAmntUse").val();
	if(comInsurAmntUse==="0"){
		$("#specialInsurAddInfoForm #comInsurAmntType").combo("clear");
		$("#specialInsurAddInfoForm #comInsurAmntType").attr("disabled", true);
		$("#specialInsurAddInfoForm #commPremium").val("");
		$("#specialInsurAddInfoForm #fixedComAmnt").val("");
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").val("");
		$("#specialInsurAddInfoForm #ipsnFloatPct").val("");
		$("#specialInsurAddInfoForm #commPremium").attr("disabled",true);
		$("#specialInsurAddInfoForm #fixedComAmnt").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatPct").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").hide();
		$("#specialInsurAddInfoForm #commPremiumIs").hide();
		$("#specialInsurAddInfoForm #fixedComAmntIs").hide();
		//显示所有隐藏的div块。
		$("#specialInsurAddInfoForm #commPremiumtype").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").show();
	}else{
		$("#specialInsurAddInfoForm #comInsurAmntType").attr("disabled", false);
		$("#specialInsurAddInfoForm #commPremium").attr("disabled",false);
		$("#specialInsurAddInfoForm #fixedComAmnt").attr("disabled",false);
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").attr("disabled",false);
		$("#specialInsurAddInfoForm #ipsnFloatPct").attr("disabled",false);
	}
});

// 公共保额类型change事件
$("#specialInsurAddInfoForm #comInsurAmntType").change(function(){
	var comInsurAmntType = $("#specialInsurAddInfoForm #comInsurAmntType").val();
	if(comInsurAmntType==="0"){
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").show();
		$("#specialInsurAddInfoForm #commPremiumIs").hide();
		$("#specialInsurAddInfoForm #fixedComAmntIs").hide();
		//隐藏显示div
		$("#specialInsurAddInfoForm #commPremiumtype").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").show();
	}else if(comInsurAmntType==="1"){
		$("#specialInsurAddInfoForm #commPremiumIs").show();
		$("#specialInsurAddInfoForm #fixedComAmntIs").show();
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").hide();
		//隐藏显示div
		$("#specialInsurAddInfoForm #commPremiumtype").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").hide();
	}
});

/***
 * 查询tab中的基金险，健康险，建工险的隐藏或者展示
 */
com.orbps.otherFunction.manualApproval.childPage.applApproval.grpInsurAppl.checkTabshowOrHidden = function (){
	//各个tab的是否隐藏，是隐藏 true 不是隐藏 false
	var tab1 = $('#myTab p[href="#tab_15_1"]').is(':hidden');
	var tab2 = $('#myTab p[href="#tab_15_2"]').is(':hidden');
	var tab3 = $('#myTab p[href="#tab_15_3"]').is(':hidden');
	//alert(tab1+"   " +tab2 +"    " +tab3);
	//如果三个都是隐藏的，隐藏div
	if(tab1 && tab2 && tab3){
		$("#specialInsurAddInfoForm").hide();
	}
	//如果tab3不是隐藏的，各个情况都展示tab3
	if((tab1 && tab2 && !tab3) || (tab1 && !tab2 && !tab3) || (!tab1 && !tab2 && !tab3) || (!tab1 && tab2 && !tab3)){
		$('#myTab p[href="#tab_15_3"]').tab('show');
	}
	//如果tab2不是隐藏的，各种情况都展示tab2
	if((tab1 && !tab2 && tab3) || (!tab1 && !tab2 && tab3)){
		$('#myTab p[href="#tab_15_2"]').tab('show');
	}
	//如果tab2 tab3 都是隐藏的，tabl不是隐藏的展示tab1
	if(!tab1 && tab2 && tab3){
		$('#myTab p[href="#tab_15_1"]').tab('show');
	}
}