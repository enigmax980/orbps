/** 新建otherFunction命名空间 */
com.orbps.otherFunction = {};
/** 新建otherFunction.contractCorrected命名空间 */
com.orbps.otherFunction.contractCorrected = {};
/** 新建otherFunction.contractCorrected.childPage命名空间 */
com.orbps.otherFunction.contractCorrected.childPage={};
/** 新建com.orbps.otherFunction.contractCorrected.parentPage命名空间 */
com.orbps.otherFunction.contractCorrected.parentPage = {};
/** 新建parentPage命名空间下的表单名reviseForm */
com.orbps.otherFunction.contractCorrected.parentPage.reviseForm = $("#reviseForm");
/** 新建parentPage命名空间下的modal名 */
com.orbps.otherFunction.contractCorrected.parentPage.addDialog = $('#btnModel1');
/** 新建parentPage命名空间下的表单名contractlist */
com.orbps.otherFunction.contractCorrected.parentPage.contractlist = $('#contractlist')
/** 新建parentPage命名空间下的变量applNo */
com.orbps.otherFunction.contractCorrected.parentPage.applNo="";
/** 新建childPage命名空间下的全局对象名 */
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl={};
/** 新建parentPage命名空间下的全局对象名 */
com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo = {};
/** 新建险种List */
com.orbps.otherFunction.contractCorrected.childPage.busiProd = [];
/** 新建险种Vo */
com.orbps.otherFunction.contractCorrected.childPage.busiProdVo;
/** 定义回退标识 */
com.orbps.otherFunction.contractCorrected.parentPage.backFlag = {};
/**共保协议号**/
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.agreementNoFlag;
com.orbps.common = {};
com.orbps.common.rootIsEdit = false;
com.orbps.common.proposalGroupList = new Array();
com.orbps.otherFunction.contractCorrected.parentPage.data;
com.orbps.common.zTrees = [];
com.orbps.common.oranLevelList=[];
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray = [];
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.healthPublicAmountArray = [];
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.grpSalesListFormVos = [];
/**基金险标示*/
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.FundInsurFlag;
//基本信息校验规则
com.orbps.otherFunction.contractCorrected.parentPage.contrantForm= function (vform) {
        var error2 = $('.alert-danger', vform);
        var success2 = $('.alert-success', vform);
        vform.validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error', 
            focusInvalid: false, 
            onkeyup:false,
            ignore: '', 
            rules : {
                correctApplNo : {
                    isApplNo : true
                }
            },
            messages : {
                correctApplNo : {
                    isApplNo : "投保单号为16位正整数！"
                }
             },
            invalidHandler: function (event, validator) {
                Metronic.scrollTo(error2, -200);
            },
            
            errorPlacement:function(error,element){
                var icon = $(element).parent('.input-icon').children('i');
                icon.removeClass('fa-check').addClass("fa-warning");
                if (icon.attr('title') || typeof icon.attr('data-original-title') != 'string') {
                    icon.attr('data-original-title', icon.attr('title') || '').attr('title', error.text())
                }
            },
            
            highlight: function (element) {
                $(element).closest('.col-md-2').removeClass("has-success").addClass('has-error');
            },
            
            success: function (label, element) {
                var icon = $(element).parent('.input-icon').children('i');
                $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
                icon.removeClass("fa-warning").addClass("fa-check");
            }
        });
    }

// 初始化函数
$(function() {
	
     var correctionVo =com.orbps.otherFunction.contractCorrected.parentPage.reviseForm.serializeObject();  
     // 添加查询参数
     $("#contractlist").datagrids({
         querydata : correctionVo            
     });
     // 重新加载数据   
     $("#contractlist").datagrids('reload');
	
    //初始化jQuery-validate
    com.orbps.otherFunction.contractCorrected.parentPage.contrantForm(com.orbps.otherFunction.contractCorrected.parentPage.reviseForm);
    
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
    // combo组件初始化
    $("*").comboInitLoad();
    
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
    						pId : organizaHierarModalVo.prioLevelCode,
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
    
    // 点击订正
    $("#reviseForm #revise").click(function() {
    	com.orbps.otherFunction.contractCorrected.parentPage.backFlag = "N";
        // 获取选中数据
        com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo = com.orbps.otherFunction.contractCorrected.parentPage.contractlist.datagrids('getSelected'); 
        var queryinfVo = {};
        queryinfVo = com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo;
        if(com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo===""){
        	lion.util.info("提示","请勾选要订正的数据");
        	return false;
        }
       	// 清空modal
        com.orbps.otherFunction.contractCorrected.parentPage.addDialog.empty();
        // 获取查询到的契约形式，根据契约形式调用对应的查询界面
        var cntrForm = com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo.cntrForm;
        switch (cntrForm) {
        case '团单':
            // 团体保单信息界面
            com.orbps.otherFunction.contractCorrected.parentPage.addDialog
                    .load(
                            "/orbps/orbps/otherFunction/contractCorrected/childPage/grpInsurAppl/html/grpInsurApplForm.html",
                            function() {
                                $(this).modal('toggle');                                               
                            });
            setTimeout(function(){
            	// 查询特殊险种
            	lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
            			function (data, arg) {
            		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
            		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
            	},null, null);
            	// 向后台发送请求
            	lion.util
            	.postjson(
            			'/orbps/web/orbps/otherfunction/Correction/query',
            			com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo,
            			com.orbps.otherFunction.contractCorrected.parentPage.successQueryDetail);
            },350);
            break;
        case '清汇':
            // 清汇信息界面
            com.orbps.otherFunction.contractCorrected.parentPage.addDialog
                    .load(
                            "/orbps/orbps/otherFunction/contractCorrected/childPage/sgGrpInsurAppl/html/sgGrpInsurAppl.html",
                            function() {
                                $(this).modal('toggle');
                                // 向后台发送请求
                                lion.util
                                        .postjson(
                                                '/orbps/web/orbps/otherfunction/Correction/sgquery',
                                                queryinfVo,
                                                com.orbps.otherFunction.contractCorrected.parentPage.successQueryDetail);
                            });            
           
           break;
        default:
            break;
        }
    });
    
    // 点击回退
    $("#reviseForm #backBtn").click(function() {
    	com.orbps.otherFunction.contractCorrected.parentPage.backFlag = "Y";
        // 获取选中数据
        com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo = com.orbps.otherFunction.contractCorrected.parentPage.contractlist.datagrids('getSelected'); 
        var queryinfVo = {};
        queryinfVo = com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo;
        if(com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo===""){
        	lion.util.info("提示","请勾选要回退的数据");
        	return false;
        }
        lion.util.postjson('/orbps/web/orbps/public/branchControl/beforeFallback',queryinfVo.applNo,function(data, arg){
        	//查询保单是否可进行回退操作
        	if(data.retCode === '0'){
        		lion.util.info("提示",data.errMsg);
        		return false;
        	}
        	// 清空modal
        	com.orbps.otherFunction.contractCorrected.parentPage.addDialog.empty();
        	// 获取查询到的契约形式，根据契约形式调用对应的查询界面
        	var cntrForm = com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo.cntrForm;
        	switch (cntrForm) {
        	case '团单':
        		// 团体保单信息界面
        		com.orbps.otherFunction.contractCorrected.parentPage.addDialog
        		.load(
        				"/orbps/orbps/otherFunction/contractCorrected/childPage/grpInsurAppl/html/grpInsurApplForm.html",
        				function() {
        					$(this).modal('toggle');                                               
        				});
        		setTimeout(function(){
        			// 查询特殊险种
        			lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
        					function (data, arg) {
        				com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
        				com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
        			},null, null);
        			// 向后台发送请求
        			lion.util
        			.postjson(
        					'/orbps/web/orbps/otherfunction/Correction/query',
        					com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo,
        					com.orbps.otherFunction.contractCorrected.parentPage.successQueryDetail,
        					null,
        					com.orbps.otherFunction.contractCorrected.parentPage.reviseForm
        			);
        		},350);
        		// 查询特殊险种
        		lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
        				function (data, arg) {
        			com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
        			com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
        		},null, null);
        		break;
        	case '清汇':
        		// 清汇信息界面
        		com.orbps.otherFunction.contractCorrected.parentPage.addDialog
        		.load(
        				"/orbps/orbps/otherFunction/contractCorrected/childPage/sgGrpInsurAppl/html/sgGrpInsurAppl.html",
        				function() {
        					$(this).modal('toggle');
        				});            
        		// 向后台发送请求
        		lion.util
        		.postjson(
        				'/orbps/web/orbps/otherfunction/Correction/sgquery',
        				queryinfVo,
        				com.orbps.otherFunction.contractCorrected.parentPage.successQueryDetail,
        				null,
        				null);
        		break;
        	default:
        		break;
        	}
        },null,null);
    });

    // 查询订正信息
    $("#query").click(function() {
        var correctionVo = com.orbps.otherFunction.contractCorrected.parentPage.reviseForm.serializeObject();
        // 添加查询参数
        $("#contractlist").datagrids({
            querydata : correctionVo            
        });
        // 重新加载数据   
        $("#contractlist").datagrids('reload');
        setTimeout( function(){
        	// 重新加载数据
        	var getdata = $("#contractlist").datagrids('getdata');
        	if(getdata.length<=0){
        		lion.util.info("提示","查询数据为空,请重新输入查询条件")
        	}
        },500);
    });
});

// 查询成功回调函数
com.orbps.otherFunction.contractCorrected.parentPage.successQueryDetail = function (data,args) {
	com.orbps.otherFunction.contractCorrected.parentPage.data = data;
	$("#applNo").attr("readOnly",true);
	$("#polProperty").attr("readonly",true);//保单性质
	$("#giftInsFlag").attr("readonly",true);//赠送险标记
	$("#giftFlag").attr("readonly",true);//团单的赠送险标记
	$("#applProperty").attr("readonly",true);//保单性质
	
	// 加延时，避免页面未初始化完造成数据回显不成功
    setTimeout(function() {
        var jsonStrs = JSON.stringify(data);
        com.orbps.otherFunction.contractCorrected.parentPage.applNo=jsonStrs.applNo;
        var objs = eval("(" + jsonStrs + ")");
        var form;
        for (y in objs) {
        	form = y;
        	var a = form.replace("Vos","Form");
        	var formId = a.replace("Vo","Form");
        	if(formId==="sgGrpVatInfoForm"){
        		formId="vAddTaxInfoForm";
        	}else if(formId==="sgGrpApplInfoForm"){
        		formId="applInfoForm";
        	}else if(formId==="sgGrpInsurInfoForm"){
        		formId="grpInsurInfoForm";
        	}else if(formId==="sgGrpPersonalInsurInfoForm"){
        		formId="personalInsurInfoForm";
        	}else if(formId==="sgGrpProposalInfoForm"){
        		formId="proposalInfoForm";
        	}else if(formId==="sgGrpPayInfoForm"){
        		formId="payInfoForm";
        	}else if(formId==="sgGrpPrintInfoForm"){
        		formId="printInfoForm";
        	}else{
        		
        	}
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            var key, value, tagName, type, arr;
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                if("insureStartDate"==key||"insureEndtDate"==key||"applDate"==key
                		||"applDate"==key||"inForceDate"==key||"inForceDate"==key
                		||"enstPremDeadline"==key||"enstPremDeadline"==key||"constructionDur"==key
                		||"constructionDur"==key||"until"==key||"preMioDate"==key){
    				value = new Date(value).format("yyyy-MM-dd");
    			}
                if("firstChargeDate"==key||"applDate"==key||"chargeDeadline"==key
                		||"speEffectDate"==key||"joinBirthDate"==key ){
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
                                	$("#"+formId +" #"+key).val(value);
            					}
            				}else if(tagName=='SELECT' || tagName=='TEXTAREA'){
            					$("#"+formId +" #"+key).combo("val", value);
                            }

                        });
            }
        }
    }, 1000);
 // 增加共保协议号是否必录的标识
    var applInfoVo = {};
    //清汇和团单里的共保协议号不一样，所以做取值判断
    var insurForm = com.orbps.otherFunction.contractCorrected.parentPage.queryinfVo.cntrForm;
    if(insurForm == "团单"){
    	applInfoVo = data.applInfoVo;
    	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.grpSalesListFormVos = data.applInfoVo.grpSalesListFormVos;
    }else if(insurForm === "清汇"){
    	applInfoVo = data.sgGrpApplInfoVo;
    	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.grpSalesListFormVos = data.sgGrpApplInfoVo.grpSalesListFormVos;
    }
	if("agreementNo" in applInfoVo){
		if ("" === applInfoVo.agreementNo) {
			com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.agreementNoFlag = "N";
		} else {
			com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.agreementNoFlag = "Y";
		}
	}else{
		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.agreementNoFlag = "N";
	}
    if (insurForm === "团单") {
    	setTimeout( function(){
    				// 将省市县置为可以回显的状态
    		$(".dist").attr("disabled",false);
    		$(".city").attr("disabled",false);
    		$(".prov").attr("disabled",false);
    		$("#applBaseInfoForm #appAddrCountry").combo("val",data.applBaseInfoVo.appAddrCountry);
    		$("#applBaseInfoForm #appAddrCity").combo("val",data.applBaseInfoVo.appAddrCity);
    		$("#applBaseInfoForm #appAddrProv").combo("val",data.applBaseInfoVo.appAddrProv);
    		$("#payInfoForm #moneyinItrvl").attr("readOnly",true);
    		$("#payInfoForm #moneyinType").attr("readOnly",true);
    		$("#payInfoForm #premFrom").attr("readOnly",true);
    		$("#payInfoForm #bankCode").attr("readOnly",true);
    		$("#payInfoForm #bankName").attr("readOnly",true);
    		$("#payInfoForm #bankAccNo").attr("readOnly",true);
    		$("#payInfoForm #stlType").attr("readOnly",true);
    		$("#payInfoForm #stlLimit").attr("readOnly",true);
    		$("#payInfoForm #settlementRatio").attr("readOnly",true);
    		$("#printInfoForm #groupType").attr("readOnly",true);
    	},1000);
    	//特别约定赋值
    	$("#specialPro").val(data.proposalInfoVo.specialPro);
        
        // 丢失焦点计算施工总天数
        $("#totalDays").val("");
        var constructionDur = data.specialInsurAddInfoVo.constructionDur;
        var until = data.specialInsurAddInfoVo.until;
        if(constructionDur!=="" && until!=="" && constructionDur!==undefined && until !== undefined){
        	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.DateDiff(constructionDur,until);
        }
        // 计算天数差的函数
        com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.DateDiff = function(sDate1, sDate2) { // sDate1和sDate2是2006-12-18格式
        	var Date1 = new Date(sDate1);
        	var Date2 = new Date(sDate2);
        	var a = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000) + 1;
        	$("#totalDays").val(a);
        }
    	// 赋值被保人分组信息为全局变量
    	com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
    	// 赋值收付费分组
    	com.common.chargePayList = data.chargePayGroupModalVos;
    	// 赋值险种信息为全局变量
    	com.orbps.otherFunction.contractCorrected.childPage.busiProd = data.busiPrdVos;
    	// 赋值结算日期的的全局变量
    	if (lion.util.isNotEmpty(data.payInfoVo.settlementDate)) {
    		com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList = data.payInfoVo.settlementDate;
    	}
    	// 加载险种表格
    	com.orbps.otherFunction.contractCorrected.childPage.reloadbusiProdTable();
    	var constructionFlag = 0;
		var healthFlag = 0;
    	
        // 取到组织树节点信息
        if(data.organizaHierarModalVos.length>0){
        	com.orbps.common.zTrees = com.orbps.common.OHMFtn(data.organizaHierarModalVos);
        }else{
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
    			children : []}];
        }
        com.orbps.common.oranLevelList = data.organizaHierarModalVos;
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
    			for(var j = 0; j < com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.healthPublicAmountArray.length; j++){
    				var array_element = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.healthPublicAmountArray[j];
    				//如果险种中有健康险的险种
    				if(a === array_element){
    					$("#specialInsurAddInfoForm").show();
    					$("#tab1").show();
    					hide2 = 1;
    					break;
    				}
    				
    			}
    			//建筑险的对比
    			for(var k = 0; k < com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray.length; k++){
    				var array_elements = com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.constructionInfoArray[k];
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
    					com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.FundInsurFlag = "Y";
    				}else{
    					$("#tab2").hide();
    					com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.FundInsurFlag = "N";
    				}
    				com.orbps.otherFunction.contractCorrected.childPage.checkTabshowOrHidden();
    			})
    		$("#specialInsurAddInfoForm #comInsurAmntUse").attr("readOnly",true);
    		$("#specialInsurAddInfoForm #comInsurAmntType").attr("readOnly",true);
        },1000);
        console.log(data);
        console.log(com.orbps.common.zNodes);
    } else if (insurForm == "清汇") {
    	if("G"===data.listType){
	  	  $("#listType").combo("val", "G")
	  	  document.getElementById('personalInsurInfoForm').style.display='none';
	  	  document.getElementById('grpInsurInfoForm').style.display='';
	  	  setTimeout( function(){
	            // 将省市县置为可以回显的状态
	      	  	$(".dist").attr("disabled",false);
	            $(".city").attr("disabled",false);
	            $(".prov").attr("disabled",false);
	            $("#grpInsurInfoForm #provinceCode").combo("val",data.sgGrpInsurInfoVo.provinceCode);
	            $("#grpInsurInfoForm #cityCode").combo("val",data.sgGrpInsurInfoVo.cityCode);
	            $("#grpInsurInfoForm #countyCode").combo("val",data.sgGrpInsurInfoVo.countyCode);
	        },1000);
	    }else{
	  	  $("#listType").combo("val", "P")
	  	  document.getElementById('grpInsurInfoForm').style.display='none';
	  	  document.getElementById('personalInsurInfoForm').style.display='';
	  	  setTimeout( function(){
	            // 将省市县置为可以回显的状态
	      	  	$(".dist").attr("disabled",false);
	            $(".city").attr("disabled",false);
	            $(".prov").attr("disabled",false);
	            $("#personalInsurInfoForm #province").combo("val",data.sgGrpPersonalInsurInfoVo.province);
	            $("#personalInsurInfoForm #city").combo("val",data.sgGrpPersonalInsurInfoVo.city);
	            $("#personalInsurInfoForm #county").combo("val",data.sgGrpPersonalInsurInfoVo.county);
	        },1000);
	    }
    	setTimeout(function(){
    		$("#payInfoForm #moneyItrvl").attr("readOnly",true);
    		$("#payInfoForm #moneyinType").attr("readOnly",true);
    		$("#payInfoForm #premFrom").attr("readOnly",true);
    		$("#payInfoForm #bankCode").attr("readOnly",true);
    		$("#payInfoForm #bankBranchName").attr("readOnly",true);
    		$("#payInfoForm #bankaccNo").attr("readOnly",true);
    		$("#payInfoForm #multiRevFlag").attr("readOnly",true);
    		$("#payInfoForm #multiTempYear").attr("readOnly",true);
    		$("#printInfoForm #groupType").attr("readOnly",true);
    	},500);
        //特别约定赋值
        $("#proposalInfoForm #convention").val(data.sgGrpProposalInfoVo.convention);
    	// 赋值被保人分组信息为全局变量
        com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
        // 赋值收付费分组
    	com.common.chargePayList = data.chargePayGroupModalVos;
    	// 赋值险种信息为全局变量
    	com.orbps.otherFunction.contractCorrected.childPage.busiProd = data.addinsuranceVos;
    	// 加载险种表格
    	com.orbps.otherFunction.contractCorrected.childPage.sgreloadbusiProdTable();
    }
    
}

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

//团单的重新加载险种表格
com.orbps.otherFunction.contractCorrected.childPage.reloadbusiProdTable = function () {
	$('#fbp-editDataGrid').find("tbody").empty();
	if (com.orbps.otherFunction.contractCorrected.childPage.busiProd !== null && com.orbps.otherFunction.contractCorrected.childPage.busiProd.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.busiProd.length; i++) {
			var healthInsurFlag = '';
			if("healthInsurFlag" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				healthInsurFlag = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].healthInsurFlag;
				healthInsurFlag = com.orbps.publicSearch.speciBusinessLogo(healthInsurFlag);
			}
			var insurDurUnit = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insurDurUnit;
			if(insurDurUnit!==""){
				insurDurUnit = com.orbps.publicSearch.insurDurUnit(com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insurDurUnit);
			}
			var amount = "";
			if("amount" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				amount = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].amount;
			}
			var premium = "";
			if("premium" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				premium = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].premium;
			}
			var insuredNum = "";
			if("insuredNum" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				insuredNum = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insuredNum;
			}
			var insurDur = "";
			if("insurDur" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				insurDur = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insurDur;
			}
			var busiPrdCodeName = "";
			if("busiPrdCodeName" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				busiPrdCodeName = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].busiPrdCodeName;
			}
			
			$('#fbp-editDataGrid').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.contractCorrected.childPage.queryProductRdio();' id='insuredRad"
					        + i
                            + "'name='insuredRad' value='"
                            + i
                            + "'></td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].busiPrdCode
							+ "</td><td>"
							+ busiPrdCodeName
							+ "</td><td>"
							+ amount
							+ "</td><td>"
							+ premium
							+ "</td><td>"
							+ insuredNum
							+ "</td><td>"
							+ insurDur
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
//清汇的重新加载险种表格
com.orbps.otherFunction.contractCorrected.childPage.sgreloadbusiProdTable = function () {
	$('#fbp-editDataGrid').find("tbody").empty();
	if (com.orbps.otherFunction.contractCorrected.childPage.busiProd !== null && com.orbps.otherFunction.contractCorrected.childPage.busiProd.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.busiProd.length; i++) {
		
			
			var healthInsurFlag = '';
			if("healthInsurFlag" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				healthInsurFlag = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].healthInsurFlag;
				healthInsurFlag = com.orbps.publicSearch.speciBusinessLogo(healthInsurFlag);
			}
			var insurDurUnit = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insurDurUnit;
			if(insurDurUnit!==""){
				insurDurUnit = com.orbps.publicSearch.insurDurUnit(com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insurDurUnit);
			}
			var busiPrdName = "";
			if("busiPrdName" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				busiPrdName = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].busiPrdName;
			}
			var insurDur = "";
			if("validateDate" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				insurDur = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].validateDate;
			}
			var amount = "";
			if("amount" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				amount = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].amount;
			}
			var premium = "";
			if("premium" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				premium = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].premium;
			}
			var insuredNum = "";
			if("insuredNum" in com.orbps.otherFunction.contractCorrected.childPage.busiProd[i]){
				insuredNum = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insuredNum;
			}
			$('#fbp-editDataGrid').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.contractCorrected.childPage.queryProductRdio();' id='insuredRad"
					        + i
                            + "'name='insuredRad' value='"
                            + i
                            + "'></td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].busiPrdCode
							+ "</td><td>"
							+ busiPrdName
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].amount
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].premium
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].insuredNum
							+ "</td><td>"
							+ insurDur
							+ "</td><td>"
							+ insurDurUnit
							+ "</td></tr>");
		}
	} else {
		$('#fbp-editDataGrid').find("tbody").append("<tr><td colspan='8' align='center'>无记录</td></tr>");
	}
};

//清空按钮功能
$("#clear").click(function(){
	$("#reviseForm").reset();
	$(".fa").removeClass("fa-warning");
	$(".fa").removeClass("fa-check");
	$(".fa").removeClass("has-success");
	$(".fa").removeClass("has-error");
});
/***
 * 查询tab中的基金险，健康险，建工险的隐藏或者展示
 */
com.orbps.otherFunction.contractCorrected.childPage.checkTabshowOrHidden = function (){
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