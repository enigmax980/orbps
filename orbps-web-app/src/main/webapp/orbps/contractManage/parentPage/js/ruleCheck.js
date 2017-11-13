com.orbps.contractManage = {};
com.orbps.contractManage.parentPage = {};
com.orbps.contractManage.parentPage.ruleCheckForm = $("#approvalQueryForm")
com.orbps.contractManage.parentPage.apTaskListTab = $("#apTaskListTab")

$(function() {
	// combo组件初始化
	$("*").comboInitLoad();
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// 清空表单录入
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
	// 清空表单录入
	$("#approvalQueryForm #btnClear").click(function() {
		$("#approvalQueryForm").reset();
	});
	// 点击返回
	$("#ruleSubmitForm #btnReturn").click(function() {
		// 隐藏相应规则页面
		$("#ruleCheckInfo").hide();
		$("#ruleSubmitForm").hide();
		// 显示清空与下一步按钮
		$("#approvalQueryForm #option").show();
		$("#approvalQueryForm").reset();
	});
	
	// 查询详细信息
	$("#approvalQueryForm #btnCheck").click(function() {
		var ruleType=0;
		// 获取选择的table数据
		var ruleCheckVo = com.orbps.contractManage.parentPage.apTaskListTab.datagrids('getSelected');
		// 隐藏清空和下一步
		$("#approvalQueryForm #option").hide();
		// 显示相应规则页面
		$("#ruleCheckInfo").show();
		switch (ruleType){
		case 0:
			lion.util.load($("#accOwnAddForm"),"orbps/contractManage/childPage/accountConfig/html/accountRulesCheckView.html");
			break;
		case 1:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/contractConfig/html/contractRulesCheckView.html");
			break;
		case 2:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/examineConfig/html/examineRulesCheckView.html");
			break;
		case 3:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/grpEntryConfig/html/grpEntryRulesCheckView.html");
			break;
		case 4:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/identifyConfig/html/identifyRulesCheckView.html");
			break;
		case 5:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/noticeConfig/html/noticeRulesCheckView.html");
			break;
		case 6:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/prePrintConfig/html/prePrintRulesCheckView.html");
			break;
		case 7:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/revokeConfig/html/revokeRulesCheckView.html");
			break;
		case 8:
			lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/sendCheckConfig/html/sendCheckRulesCheckView.html");
			break;
		default:
			break;
		}
		$("#ruleSubmitForm").show();
	});
	// 向后台发送请求
	lion.util.postjson('/orbps/web/orbps/contractManage/rule/submitCheck',ruleCheckVo,successQuery,null,com.orbps.contractManage.parentPage.ruleQueryForm);
});

//$("#approvalQueryForm #btnQuery").click(function() {
//	var ruleCheckVo=com.orbps.contractManage.parentPage.ruleCheckForm.serializeObject();
//	if(ruleCheckVo!=null){
//		alert(JSON.stringify(ruleCheckVo));	
//		lion.util.postjson('/orbps/web/orbps/contractManage/rule/searchCheck',ruleCheckVo,successQuery,null,null);
//	}else{
//		lion.util.info("提示","请选择规则类型");
//		return false;
//	}
//});		

//查询规则信息
$("#btnQuery").click(function() {	
	var ruleCheckVo=com.orbps.contractManage.parentPage.ruleCheckForm.serializeObject();
	if(ruleCheckVo!=null){			
		//alert(JSON.stringify(ruleCheckVo)+"");			
		// 添加查询参数
		$("#apTaskListTab").datagrids({
			querydata : ruleCheckVo		    
		});
        // 重新加载数据
		$("#apTaskListTab").datagrids('reload');
	}else{
		lion.util.info("提示","请选择规则类型");
		return false;
	}
});		


//查询成功回调函数
function successQuery(data,arg){
	//alert("success");
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
            if(tagName=='INPUT'){
                if(type=='radio'){
                    $(this).attr('checked',$(this).val()==value);
                }else if(type=='checkbox'){
                    arr = value.split(',');
                    for(var i =0;i<arr.length;i++){
                        if($(this).val()==arr[i]){
                            $(this).attr('checked',true);
                            break;
                        }
                    }
                }else{
                    $("#accOwnAddForm #"+key).val(value);
                }
            }else if(tagName=='SELECT' || tagName=='TEXTAREA'){
                $("#accOwnAddForm #"+key).combo("val", value);
            }
        });
    }
    
    var ruleType = $("#ruleQueryForm #ruleType").val();
    //根据规则类型显示相应的表格
    if(ruleType==0){
		// 添加查询参数
		$("#accountOperaListTab").datagrids({
			querydata : com.orbps.contractManage.parentPage.ruleQueryForm.serializeObject()
		});
		// 重新加载数据
		$("#accountOperaListTab").datagrids('reload');
	}else if(ruleType==1){
		// 添加查询参数
		$("#contractOperaListTab").datagrids({
			querydata : com.orbps.contractManage.parentPage.ruleQueryForm.serializeObject()
		});
		// 重新加载数据
		$("#contractOperaListTab").datagrids('reload');
    }
}


