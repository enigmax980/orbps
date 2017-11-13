com.orbps.contractManage = {};
com.orbps.contractManage.parentPage = {};
com.orbps.contractManage.childPage = {};
com.orbps.contractManage.parentPage.apTaskListTab = $("#apTaskListTab");
com.orbps.contractManage.parentPage.ruleQueryForm = $("#ruleQueryForm");
com.orbps.contractManage.parentPage.accOwnAddForm = $("#accOwnAddForm");
//com.orbps.contractManage.parentPage.id;
$(function() {
	// combo组件初始化
	$("*").comboInitLoad();
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// 清空表单录入
	$("#ruleQueryForm #ruleType").combo("val",1);
	$("#ruleQueryForm #btnClear").click(function() {
		$("#ruleQueryForm").reset();
	});
	
	// 点击返回
	$("#ruleSubmitForm #btnReturn").click(function() {
		$("#ruleQueryForm").reset();
		$("#ruleSubmitForm").hide();
		// 隐藏相应规则页面
		$("#ruleQueryInfo").hide();
		// 显示清空与下一步按钮
		$("#ruleQueryForm #option").show();
	});
	
	// 查询详细信息
	$("#btnQueryDetail").click(function() {
		// 获取选择的table数据
		var ruleQueryVo = com.orbps.contractManage.parentPage.apTaskListTab.datagrids('getSelected');
		 //判断这个值是否为空
		if (!ruleQueryVo) {
			lion.util.info('提示', '请选择要详细查询的规则');
			return false;
		}
		var ruleType=$("#ruleType").val();
		// 隐藏按钮
		$("#option").hide();
		// combo组件初始化
		$("*").comboInitLoad();
		// 显示相应规则页面
		$("#ruleQueryInfo").show();
		// 显示相应按钮
		$("#ruleSubmitForm").show();
		if(ruleType===0){
			lion.util.load($("#accOwnAddForm"),"/orbps/orbps/contractManage/childPage/accountConfig/html/accountRulesDetailView.html");
		}else if(ruleType==="1"){
			lion.util.load($("#accOwnAddForm"),"/orbps/orbps/contractManage/childPage/examineConfig/html/examineRulesDetailView.html");
		}else if(ruleType==="2"){
		}else if(ruleType==="3"){
		}else if(ruleType==="4"){
		}else if(ruleType==="5"){
		}else if(ruleType==="6"){
		}else if(ruleType==="7"){
		}else if(ruleType==="8"){
		}else{
			lion.util.info("提示","请选择规则类型");
			return false;
		}
		setTimeout( function(){
			// 向后台发送请求
			lion.util.postjson('/orbps/web/orbps/contractManage/rule/search',ruleQueryVo,com.orbps.contractManage.parentPage.successQuery,null,com.orbps.contractManage.parentPage.ruleQueryForm);
		},500);
	});
	
	// 查询规则信息
	$("#btnQuery").click(function() {
		var mgrBranchNo = $("#ruleQueryForm #mgrBranchNo").val();
		if(mgrBranchNo!=null){			
			var ruleQueryVo=com.orbps.contractManage.parentPage.ruleQueryForm.serializeObject();		
			// 添加查询参数
			$("#apTaskListTab").datagrids({
				querydata : ruleQueryVo			    
			});
	        // 重新加载数据
			$("#apTaskListTab").datagrids('reload');
		}else{
			lion.util.info("提示","请选择规则类型");
			return false;
		}
	});	
	
	
});

// 查询成功回调函数
com.orbps.contractManage.parentPage.successQuery = function(data,arg){
	var msg=data;
	//回显方法
	jsonStr = JSON.stringify(msg);
    var obj = eval("("+jsonStr+")");
    var key,value,tagName,type,arr;
    for(x in obj){
        key = x;
        value = obj[x];
        $("[name='"+key+"'],[name='"+key+"[]']").each(function(){
            tagName = $(this)[0].tagName;
            type = $(this).attr('type');
            if(tagName==='INPUT'){
                if(type==='radio'){
                    $(this).attr('checked',$(this).val()===value);
                }else if(type==='checkbox'){
                    arr = value.split(',');
                    for(var i =0;i<arr.length;i++){
                        if($(this).val()===arr[i]){
                            $(this).attr('checked',true);
                            break;
                        }
                    }
                }else{
                    $("#accOwnAddForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
                $("#accOwnAddForm #"+key).combo("val", value);
            }
        });
    }
    setTimeout(function(){
		$("#accOwnAddForm #salesChannel").attr("readOnly",true);
		$("#accOwnAddForm #cntrForm").attr("readOnly",true);
		$("#accOwnAddForm #validDate").attr("disabled",true);
		$("#accOwnAddForm #validDateBtn").attr("disabled",true);
		$("#accOwnAddForm #effectiveDateBackAcross").attr("readOnly",true);
		$("#accOwnAddForm #ruleType").attr("readOnly",true);
	},100);
//    var ruleType = $("#ruleQueryForm #ruleType").val();
//    //根据规则类型显示相应的表格
//    if(ruleType===0){
//		// 添加查询参数
//		$("#accountOperaListTab").datagrids({
//			querydata : com.orbps.contractManage.parentPage.ruleQueryForm.serializeObject()
//		});
//		// 重新加载数据
//		$("#accountOperaListTab").datagrids('reload');
//	}else if(ruleType===1){
//		// 添加查询参数
//		$("#contractOperaListTab").datagrids({
//			querydata : com.orbps.contractManage.parentPage.ruleQueryForm.serializeObject()
//		});
//		// 重新加载数据
//		$("#contractOperaListTab").datagrids('reload');
//    }
}

//修改
$("#ruleSubmitForm #btnUpdate").click(function() {
	if(com.orbps.contractManage.parentPage.accOwnAddForm
                    .validate().form()
            && com.orbps.contractManage.parentPage.validateSelectsVal()){
		var ruleName = $("#ruleName").val();
		if(ruleName.length > 30){
	    	 lion.util.info("警告", "规则名称长度不能大于30个字符");
	         return false;
	    }
		var ruleChangeReason = $("#ruleChangeReason").val();
		if(ruleChangeReason.length > 100){
		    	lion.util.info("警告", "规则变化原因长度小于100个汉字。");
		        return false;
		 }
		$("#accOwnAddForm #validDate").attr("disabled",false);
		var contractManageVo = {};
	    contractManageVo.examineRulesDetailViewVo = com.orbps.contractManage.parentPage.accOwnAddForm.serializeObject();
	    var validDate = contractManageVo.examineRulesDetailViewVo.validDate;
	    var invalidDate = contractManageVo.examineRulesDetailViewVo.invalidDate;
	    var validDate = new Date(validDate);
		var invalidDate = new Date(invalidDate);
	    if(validDate.getTime() > invalidDate.getTime()){
	    	lion.util.info("警告","失效日期大于生效日期，请选择！");
	    	return false;
	    }
		// 向后台发送请求
		lion.util.postjson('/orbps/web/orbps/contractManage/rule/update',contractManageVo,com.orbps.contractManage.parentPage.successUpdate,null,null);
	}
});

com.orbps.contractManage.parentPage.validateSelectsVal=function () {
  
	var ruleType = $("#accOwnAddForm #ruleType").val();
    if (ruleType === null || "" === ruleType) {
        lion.util.info("警告", "请选择规则类型");
        return false;
    };
    var salesChannel = $("#accOwnAddForm #salesChannel").val();
    if (salesChannel === null || "" === salesChannel) {
        lion.util.info("警告", "请选择销售渠道");
        return false;
    };
    var cntrForm = $("#accOwnAddForm #cntrForm").val();
    if (cntrForm === null || "" === cntrForm) {
        lion.util.info("警告", "请选择契约形式");
        return false;
    };
//页面暂时不展示产品类型
//    var productType = $("#accOwnAddForm #productType").val();
//    if (productType === null || "" === productType) {
//        lion.util.info("警告", "请选择产品类型");
//        return false;
//    };
    var artificialApproveFlag = $("#accOwnAddForm #artificialApproveFlag").val();
    if (artificialApproveFlag === null || "" === artificialApproveFlag) {
        lion.util.info("警告", "请选择是否人工审批");
        return false;
    };
    var effectiveDateBackAcross = $("#accOwnAddForm #effectiveDateBackAcross").val();
    if (effectiveDateBackAcross === null || "" === effectiveDateBackAcross) {
        lion.util.info("警告", "请选择生效日往前追溯跨越日期");
        return false;
    };
    return true;
}

//添加成功回调函数
com.orbps.contractManage.parentPage.successUpdate=function (data,arg){
	 if (data.retCode==="1"){
	        lion.util.info("提示", "修改成功");
	    }else{
	        lion.util.info("提示", "修改失败，失败原因："+data.errMsg);
	    }
//	// 成功后刷新页面
//	setTimeout(function(){
//		window.location.reload();
//	},500);
}

