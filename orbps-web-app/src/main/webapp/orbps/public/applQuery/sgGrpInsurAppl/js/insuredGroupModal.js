com.orbps.otherFunction.contractQuery.proposalGroupCount = 0;
com.orbps.otherFunction.contractQuery.reloadProposalGroupType = -1;
com.orbps.otherFunction.contractQuery.insuranceCount = 0;
com.orbps.otherFunction.contractQuery.reloadInsuranceType = -1;
com.orbps.otherFunction.contractQuery.proposalGroupInfoForm = $("#proposalGroupInfoForm");
com.orbps.otherFunction.contractQuery.insuranceInfoForm = $("#insuranceInfoForm");
com.orbps.otherFunction.contractQuery.proposalGroupVo = {};

//回显方法
com.orbps.otherFunction.contractQuery.showProposalGroup = function(msg){
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
                    $("#proposalGroupInfoForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
                $("#proposalGroupInfoForm #"+key).combo("val", value);
            }
        });
    }
}

//重新加载表格
com.orbps.otherFunction.contractQuery.reloadProposalGroupModalTable = function () { 
	$('#ipsnStateGrpList').find("tbody").empty();
	if (com.orbps.otherFunction.contractQuery.proposalGroupList !== null && com.orbps.otherFunction.contractQuery.proposalGroupList.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractQuery.proposalGroupList.length; i++) {
			var occClassCode = "";
			if(com.orbps.otherFunction.contractQuery.proposalGroupList[i].occClassCode!==""){
				occClassCode = com.orbps.publicSearch.ipsnOccSubclsCodeQuery(com.orbps.otherFunction.contractQuery.proposalGroupList[i].occClassCode);
			}
			var ipsnOccSubclsCode = "";
			if(com.orbps.otherFunction.contractQuery.proposalGroupList[i].ipsnOccSubclsCode!==""){
				ipsnOccSubclsCode = com.orbps.publicSearch.ipsnOccSubclsCodeQuery(com.orbps.otherFunction.contractQuery.proposalGroupList[i].ipsnOccSubclsCode);
			}
			$('#ipsnStateGrpList').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.contractQuery.proposalGroupRadio();' id='proposalGroupRad"
							+ i
							+ "' name='proposalGroupRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupList[i].ipsnGrpNo
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupList[i].ipsnGrpName
							+ "</td><td >"
							+ occClassCode
							+ "</td><td >"
							+ ipsnOccSubclsCode
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupList[i].ipsnGrpNum
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupList[i].gsRate
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupList[i].ssRate
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupList[i].genderRadio
							+ "</td></tr>");
		}
	} else {
		$('#ipsnStateGrpList').find("tbody").append("<tr><td colspan='10' align='center'>无记录</td></tr>");
	}
};


//基本信息校验规则
com.orbps.otherFunction.contractQuery.insuranceModalValidateForm = function (vform) {
        var error2 = $('.alert-danger', vform);
        var success2 = $('.alert-success', vform);
        vform.validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error', 
            focusInvalid: false, 
            onkeyup:false,
            ignore: '', 
            rules : {
            	polCode : {
            		required : true
                }
            },
            messages : {
            	polCode : {
                	required : "险种代码非空"
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



//初始化函数
$(function() {
	
	//初始化校验信息
	com.orbps.otherFunction.contractQuery.insuranceModalValidateForm(com.orbps.otherFunction.contractQuery.insuranceInfoForm);

	// 清空保险表单
	com.orbps.otherFunction.contractQuery.clearProposalGroupInfoForm = function(){
		$("#insuranceInfoForm input[type='text']").val("");
		$("#insuranceInfoForm select").combo("refresh");
		$(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
	}
	
	// 点击radio，将其数据回显到录入界面
	com.orbps.otherFunction.contractQuery.proposalGroupRadio = function() {
		com.orbps.otherFunction.contractQuery.clearProposalGroupInfoForm();
		var radioVal;
		var temp = document.getElementsByName("proposalGroupRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.otherFunction.contractQuery.reloadProposalGroupType = radioVal;
		com.orbps.otherFunction.contractQuery.proposalGroupVo = com.orbps.otherFunction.contractQuery.proposalGroupList[radioVal];
		//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
		setTimeout(function(){
			// 回显
			com.orbps.otherFunction.contractQuery.showProposalGroup(com.orbps.otherFunction.contractQuery.proposalGroupVo);
			$("input[name='proposalGroupRad']").eq(radioVal).attr("checked","checked");
		},400);
		// 展示险种信息
		$("#insuranceInfo").show();
		// 清空险种表单
		com.orbps.otherFunction.contractQuery.clearProposalGroupInfoForm();
		// 判断属性是否存在
		if('insuranceInfoVos' in com.orbps.otherFunction.contractQuery.proposalGroupVo){
			// 重新加载险种表格
			com.orbps.otherFunction.contractQuery.reloadInsuranceModalTable();
		}else{
			com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos = "";
			com.orbps.otherFunction.contractQuery.reloadInsuranceModalTable();
		}
	};
});


//回显方法
com.orbps.otherFunction.contractQuery.showInsurance = function(msg){
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
                    $("#insuranceInfoForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
                $("#insuranceInfoForm #"+key).combo("val", value);
            }
        });
    }
}

//重新加载表格
com.orbps.otherFunction.contractQuery.reloadInsuranceModalTable = function () {
	$('#grpPolicyList').find("tbody").empty();
	if (com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos !== null && com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos.length; i++) {
			var mrCode = "";
			if(com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].mrCode!==""){
				mrCode = com.orbps.publicSearch.mrCodeQuery(com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].mrCode);
			}
			$('#grpPolicyList').find("tbody")
					.append("<tr><td ><input type='radio' id='insuredRad"
							+ i
							+ "' name='insuredRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].polCode
							+ "</td><td >"
							+ mrCode
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].faceAmnt
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].premium
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].stdPremium
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].premRate
							+ "</td><td >"
							+ com.orbps.otherFunction.contractQuery.proposalGroupVo.insuranceInfoVos[i].recDiscount
							+ "</td></tr>");
		}
	} else {
		$('#grpPolicyList').find("tbody").append("<tr><td colspan='10' align='center'>无记录</td></tr>");
	}
};