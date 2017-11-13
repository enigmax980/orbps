com.orbps.common.proposalGroupCount = 0;
com.orbps.common.reloadProposalGroupType = -1;
com.orbps.common.insuranceCount = 0;
com.orbps.common.reloadInsuranceType = -1;
com.orbps.common.proposalGroupInfoForm = $("#proposalGroupInfoForm");
com.orbps.common.insuranceInfoForm = $("#insuranceInfoForm");
com.orbps.common.proposalGroupVo = {};
//基本信息校验规则
com.orbps.common.proposalGroupModalValidateForm = function (vform) {
        var error2 = $('.alert-danger', vform);
        var success2 = $('.alert-success', vform);
        vform.validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error', 
            focusInvalid: false, 
            onkeyup:false,
            ignore: '', 
            rules : {
            	ipsnGrpNo : {
            		required : true,
            		isipsnGrpNo : true
                },
                ipsnGrpNum : {
            		required : true,
            		isIntGteZero : true
                },
                genderRadio : {
                	contractAmount : true
                },
                ipsnGrpName : {
                	required : true
                },
            },
            messages : {
            	ipsnGrpNo : {
                	required : "请填写要约属组编号",
                	isipsnGrpNo : "要约属组编号非空非0,不能重复并且小于1000"
                },
                ipsnGrpNum : {
                	required : "请填写要约属组人数",
                	isIntGteZero : "请输入大于0的整数"
                },
                ipsnGrpName : {
                	required : "请填写要约属组名称"
                },
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
//	alert($("#groupType").val());
	//如果分组类型是赔付比例一致。及2 隐藏保额保费输入框
	if($("#groupType").val() === "2"){
		$("#faceAmnt").attr("readonly",true);
		$("#stdPremium").attr("readonly",true);
		$("#premium").attr("readonly",true);
	}
	
	//初始化校验信息
	com.orbps.common.proposalGroupModalValidateForm(com.orbps.common.proposalGroupInfoForm);
	
	//设置承保费率只读
	$("#insuranceInfoForm #premRate").attr("readOnly",true);
	$("#insuranceInfoForm #mrCode").comboInitLoadById("");
	$("#insuranceInfoForm #ipsnOccSubclsCode").comboInitLoadById("");
	$("#insuranceInfoForm #mrCode").attr("readOnly",true);
	//新增
	$("#proposalGroupInfoForm #btnAdd").click(function() {
		//查询数组号是否重复
		if(com.orbps.common.queryIpsnGrpNo()){
			if(com.orbps.common.proposalGroupInfoForm.validate().form()){
				var proposalGroup =$("#proposalGroupInfoForm").serializeObject();
				// 表单验证成功后执行
				var ipsnGrpNo=$("#proposalGroupInfoForm #ipsnGrpNo").val();
				var ipsnGrpName=$("#proposalGroupInfoForm #ipsnGrpName").val();
				var ipsnGrpNum=$("#proposalGroupInfoForm #ipsnGrpNum").val();
				
				if (ipsnGrpNo === null || "" === ipsnGrpNo || "undefined" === ipsnGrpNo) {
					lion.util.info("警告", "要约属组编号1.非空非零2.不能重复 <1000");
					return false;
				}else if (ipsnGrpName === null || "" === ipsnGrpName || "undefined" === ipsnGrpName) {
					lion.util.info("警告", "要约属组名称不能为空");
					return false;
				}else if (ipsnGrpNum === null || "" === ipsnGrpNum || "undefined" === ipsnGrpNum) {
					lion.util.info("警告", "要约属组人数不能为空");
					return false;
				}
				com.orbps.common.proposalGroupList.push(proposalGroup);
				com.orbps.common.proposalGroupCount++;
				// 刷新table
				com.orbps.common.reloadProposalGroupModalTable();
				com.orbps.common.clearProposalGroupInfoForm();
//		        // 隐藏险种信息
				$("#insuranceInfo").hide();
			}
		}
	});

	// 清空属组表单
	com.orbps.common.clearProposalGroupInfoForm = function (){
		// form清空
		$("#proposalGroupInfoForm input[type='text']").val("");
		$("#proposalGroupInfoForm select").combo("clear");
		$(".fa").removeClass("fa-warning");
		$(".fa").removeClass("fa-check");
		$(".fa").removeClass("has-success");
		$(".fa").removeClass("has-error");
	}

	$("#proposalGroupInfoForm #ipsnGrpNo").blur(
		function(){
			var ipsnGrpNo=$("#proposalGroupInfoForm #ipsnGrpNo").val();
            if (ipsnGrpNo === null || "" === ipsnGrpNo
                    || "undefined" === ipsnGrpNo) {
                lion.util.info("警告", "要约属组编号不能为空");
                return false;
            }
		});	
	$("#proposalGroupInfoForm #ipsnGrpName").blur(
		function(){
			var ipsnGrpName=$("#proposalGroupInfoForm #ipsnGrpName").val();
            if (ipsnGrpName === null || "" === ipsnGrpName
                    || "undefined" === ipsnGrpName) {
                lion.util.info("警告", "要约属组名称不能为空");
                return false;
            }
		});	
	$("#proposalGroupInfoForm #ipsnGrpNum").blur(
		function(){
			var ipsnGrpNum=$("#proposalGroupInfoForm #ipsnGrpNum").val();
            if (ipsnGrpNum === null || "" === ipsnGrpNum
                    || "undefined" === ipsnGrpNum) {
                lion.util.info("警告", "要约属组人数不能为空");
                return false;
            }
		});

	//修改
	$("#proposalGroupInfoForm #btnEdit").click(function() {
		if(com.orbps.common.proposalGroupInfoForm.validate().form()){
			//判断修改记录是否重复，需要判断是否修改了当前记录
			var insuredBtuVo =$("#proposalGroupInfoForm").serializeObject();
			for(var i=0; i<com.orbps.common.proposalGroupList.length; i++){
				if(com.orbps.common.proposalGroupList[i].ipsnGrpNo === insuredBtuVo.ipsnGrpNo && i.toString() !== com.orbps.common.reloadProposalGroupType){
					lion.util.info("修改失败，属组号重复");
					return false;
				}
			}
			// 表单验证成功后执行
			if (com.orbps.common.reloadProposalGroupType > -1) {
				var insuranceInfoVos = com.orbps.common.proposalGroupList[com.orbps.common.reloadProposalGroupType].insuranceInfoVos;
				com.orbps.common.proposalGroupList[com.orbps.common.reloadProposalGroupType] = insuredBtuVo;
				com.orbps.common.proposalGroupList[com.orbps.common.reloadProposalGroupType].insuranceInfoVos = insuranceInfoVos;
				com.orbps.common.reloadProposalGroupType = -1;
			}
			// 刷新table
			com.orbps.common.reloadProposalGroupModalTable();
			// form清空
			com.orbps.common.clearProposalGroupInfoForm();
//	        // 隐藏险种信息
			$("#insuranceInfo").hide();
		}else{
			return;
		}
	});
	
	
	// 删除功能
	com.orbps.common.proposalGroupDelete = function(vform){
		if(confirm("您确认要删除此条被保人信息？")){
			
			if (com.orbps.common.proposalGroupList !== null && com.orbps.common.proposalGroupList.length > 0) {
				com.orbps.common.proposalGroupList.splice(vform, 1);
				com.orbps.common.proposalGroupCount--;
			}
			// 刷新属组table
			com.orbps.common.reloadProposalGroupModalTable();
			// form清空
			com.orbps.common.clearProposalGroupInfoForm();
			com.orbps.common.clearInsuranceInfoForm();
			// 刷新险种table
			com.orbps.common.reloadInsuranceModalTable();
		}
	};
	
	// 清空功能
	$("#proposalGroupInfoForm #btnClear").click(function() {
		com.orbps.common.clearProposalGroupInfoForm();
	});
	
	// 查询属组号是否重复
	$("#proposalGroupInfoForm #ipsnGrpNo").blur(function() {
		com.orbps.common.queryIpsnGrpNo();
	});
	
	// 属组号是否重复的函数
	com.orbps.common.queryIpsnGrpNo = function (){
		var ipsnGrpNo = $("#proposalGroupInfoForm #ipsnGrpNo").val();
		if(ipsnGrpNo!==""){
			for (var i = 0; i < com.orbps.common.proposalGroupList.length; i++) {
				var array_element = com.orbps.common.proposalGroupList[i].ipsnGrpNo;
				if(ipsnGrpNo===array_element){
					lion.util.info("提示","属组号已存在,请重新输入");
					return false;
				}
			}
		}
		return true;
	}
	// 点击radio，将其数据回显到录入界面
	com.orbps.common.proposalGroupRadio = function() {
		com.orbps.common.clearProposalGroupInfoForm();
		var radioVal;
		var temp = document.getElementsByName("proposalGroupRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.common.reloadProposalGroupType = radioVal;
		com.orbps.common.proposalGroupVo = com.orbps.common.proposalGroupList[radioVal];
		//回显数据
		com.orbps.common.showProposalGroup(com.orbps.common.proposalGroupVo);
		$("input[name='proposalGroupRad']").eq(radioVal).attr("checked","checked");
		// 展示险种信息
		$("#insuranceInfo").show();
		// 清空险种表单
		com.orbps.common.clearInsuranceInfoForm();
		// 判断属性是否存在
		if('insuranceInfoVos' in com.orbps.common.proposalGroupVo){
			com.orbps.common.insuranceList = com.orbps.common.proposalGroupVo.insuranceInfoVos;
			// 重新加载险种表格
			com.orbps.common.reloadInsuranceModalTable();
		}else{
			com.orbps.common.proposalGroupVo.insuranceInfoVos = [];
			com.orbps.common.reloadInsuranceModalTable();
		}
		com.orbps.common.insuranceList=[];
		var reloadBusiPrdCodeSelect = [];
		// 判断险种是否是list
		if(Object.prototype.toString.call(com.orbps.common.getAddRowsData) === '[object Array]'){
			for (var i = 0; i < com.orbps.common.getAddRowsData.length; i++) {
				var busiPrdCode = com.orbps.common.getAddRowsData[i].busiPrdCode;
				// 获取所有主险集合
				reloadBusiPrdCodeSelect.push(busiPrdCode);
			}
		}else{
			// 获取所有主险集合
			reloadBusiPrdCodeSelect.push(com.orbps.common.getAddRowsData.busiPrdCode);
		}
		// 获取险种id
		var polCode = document.getElementById("polCode");
		// 清空option
		polCode.options.length=1;
		if(com.orbps.common.list.length>0){
			var productCodes = [];
			// 循环所以责任，取出删除-的责任代码
			for (var i = 0; i < com.orbps.common.list.length; i++) {
				var productCode = com.orbps.common.list[i].productCode;
				productCode = productCode.split("-")[0];
				productCodes.push(productCode);
			}
			// 去重(责任里取出来的险种名)
			var rets = [],
			ends;
			productCodes.sort();
			ends = productCodes[0];
			rets.push(productCodes[0]);
			for (var i = 1; i < productCodes.length; i++) {
				if (productCodes[i] != ends) {
					rets.push(productCodes[i]);
					ends = productCodes[i];
				}
			}
			// 删除主险列表中含有责任的险种代码，避免与责任重复
			for (var i = 0; i < rets.length; i++) {
				var retsCode = rets[i];
				for (var j = 0; j < reloadBusiPrdCodeSelect.length; j++) {
					var array_element = reloadBusiPrdCodeSelect[j];
					if(retsCode===array_element){
						// 删除与责任相同的主险代码
						reloadBusiPrdCodeSelect.splice(j,1);
					}
				}
			}
			
			// 循环不重复的险种代码
			for (var i = 0; i < reloadBusiPrdCodeSelect.length; i++) {
				//把险种代码绑定到select的value上
				var name = reloadBusiPrdCodeSelect[i];
				//添加option
				polCode.options.add(new Option(name, name));
			}
			// 循环责任代码
			for (var i = 0; i < com.orbps.common.list.length; i++) {
				//把子险种代码绑定到select的value上
				var name =  com.orbps.common.list[i].productCode;
				//添加option
				polCode.options.add(new Option(name, name));
			}
		}else{
			// 没有责任的情况下,直接增加主险option
			for (var i = 0; i < reloadBusiPrdCodeSelect.length; i++) {
				var array_element = reloadBusiPrdCodeSelect[i];
				polCode.options.add(new Option(array_element, array_element));
			}
		}
	}
	
	//承保费率计算
	$("#insuranceInfoForm #faceAmnt").blur(function() {
		var faceAmnt = $("#insuranceInfoForm #faceAmnt").val();
		var premium = $("#insuranceInfoForm #premium").val();
		var premRate;
		if(faceAmnt!==""&&premium!==""){
			if(premium===0){
				lion.util.info("提示","实际保费不能为0!");
				return false;
			}else{
				premRate = (parseFloat(premium)/parseFloat(faceAmnt)).toFixed(2);
			}
			$("#insuranceInfoForm #premRate").val(premRate);
		}else{
			$("#insuranceInfoForm #premRate").val("");
		}
	});
	
	//承保费率计算 && 费率浮动计算
	$("#insuranceInfoForm #premium").blur(function() {
		var faceAmnt = $("#insuranceInfoForm #faceAmnt").val();
		var premium = $("#insuranceInfoForm #premium").val();
		var premRate="";
		if(faceAmnt!==""&&premium!==""){
			if(premium===0){
				lion.util.info("提示","实际保费不能为0!");
			}else{
				premRate = (parseFloat(premium)/parseFloat(faceAmnt)).toFixed(2);
			}
			$("#insuranceInfoForm #premRate").val(premRate);
		}else{
			$("#insuranceInfoForm #premRate").val("");
		}
		
		var stdPremium = $("#insuranceInfoForm #stdPremium").val();
		if(stdPremium===""){
			$("#insuranceInfoForm #stdPremium").val(premium);
		}
		stdPremium = $("#insuranceInfoForm #stdPremium").val();
		var recDisount="";
		if(stdPremium!==""&&premium!==""){
			if(stdPremium===0){
				lion.util.info("提示","标准保费不能为0!");
			}else{
				recDisount = (parseFloat(premium)/parseFloat(stdPremium)).toFixed(2);
			}
			$("#insuranceInfoForm #recDisount").val(recDisount);
		}else{
			$("#insuranceInfoForm #recDisount").val("");
		}
		
	});
	
	// 费率浮动幅度计算
	$("#insuranceInfoForm #stdPremium").blur(function() {
		var stdPremium = $("#insuranceInfoForm #stdPremium").val();
		var premium = $("#insuranceInfoForm #premium").val();
		var recDisount;
		if(stdPremium!==""&&premium!==""){
			if(stdPremium===0){
				lion.util.info("提示","标准保费不能为0!");
				return false;
			}else{
				recDisount = (parseFloat(premium)/parseFloat(stdPremium)).toFixed(2);
			}
			$("#insuranceInfoForm #recDisount").val(recDisount);
		}else{
			$("#insuranceInfoForm #recDisount").val("");
		}
	});
});
//回显方法
com.orbps.common.showProposalGroup = function(msg){
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
com.orbps.common.reloadProposalGroupModalTable = function () { 
	$('#ipsnStateGrpList').find("tbody").empty();
	if (com.orbps.common.proposalGroupList !== null && com.orbps.common.proposalGroupList.length > 0) {
		for (var i = 0; i < com.orbps.common.proposalGroupList.length; i++) {
			/*var occClassCode = "";
			if(com.orbps.common.proposalGroupList[i].occClassCode!==""){
				occClassCode = com.orbps.publicSearch.occClassCodeQuery(com.orbps.common.proposalGroupList[i].occClassCode);
			}
			var ipsnOccSubclsCode = "";
			if(com.orbps.common.proposalGroupList[i].ipsnOccSubclsCode!==""){
				ipsnOccSubclsCode = com.orbps.publicSearch.ipsnOccSubclsCodeQuery(com.orbps.common.proposalGroupList[i].ipsnOccSubclsCode);
			}
			var gsRate = "";
			if(com.orbps.common.proposalGroupList[i].gsRate !=="" &&
					com.orbps.common.proposalGroupList[i].gsRate !== undefined){
				gsRate = com.orbps.common.proposalGroupList[i].gsRate+"%";
			}
			var ssRate = "";
			if(com.orbps.common.proposalGroupList[i].ssRate !=="" &&
					com.orbps.common.proposalGroupList[i].ssRate != undefined){
				ssRate = com.orbps.common.proposalGroupList[i].ssRate+"%";
			}
			var genderRadio = "";
			if(com.orbps.common.proposalGroupList[i].genderRadio !=="" && 
					com.orbps.common.proposalGroupList[i].genderRadio !== undefined){
				genderRadio = com.orbps.common.proposalGroupList[i].genderRadio +"%";
			}*/
			$('#ipsnStateGrpList').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.common.proposalGroupRadio();' id='proposalGroupRad"
							+ i
							+ "' name='proposalGroupRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.common.proposalGroupList[i].ipsnGrpNo
							+ "</td><td >"
							+ com.orbps.common.proposalGroupList[i].ipsnGrpName
							+ "</td><td >"
							+ com.orbps.common.proposalGroupList[i].ipsnGrpNum
							+ "</td><td ><a href='javascript:void(0);' onclick='com.orbps.common.proposalGroupDelete("+i+");' for='proposalGroupRad' id='insuredDel" 
							+ i + "'>删除</a></td></tr>");
		}
	} else {
		$('#ipsnStateGrpList').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
	}
		
	
};


//基本信息校验规则
com.orbps.common.insuranceModalValidateForm = function (vform) {
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
            submitHandler: function (form) {
                success2.show();
                error2.hide();
                form[0].submit();
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
	com.orbps.common.insuranceModalValidateForm(com.orbps.common.insuranceInfoForm);
	//新增
	$("#insuranceInfoForm #btnAdd").click(function() {
		if(com.orbps.common.querypolCode()){
			if(com.orbps.common.insuranceInfoForm.validate().form()){
				var insurance =$("#insuranceInfoForm").serializeObject();
				// 表单验证成功后执行
				var polCode=$("#insuranceInfoForm #polCode").val();
				if (polCode === null || "" === polCode || "undefined" === polCode) {
					lion.util.info("警告", "险种代码不能为空");
					return false;
				}
				//按照保额、保费分组时，保额、保费不能为空
				if("1" === $("#printInfoForm #groupType").val()){
					var faceAmnt=$("#insuranceInfoForm #faceAmnt").val();
					if (faceAmnt === null || "" === faceAmnt || "undefined" === faceAmnt) {
						lion.util.info("警告", "险种保额不能为空");
						return false;
					}
					var premium=$("#insuranceInfoForm #premium").val();
					if (premium === null || "" === premium || "undefined" === premium) {
						lion.util.info("警告", "实际保费不能为空");
						return false;
					}
					var stdPremium=$("#insuranceInfoForm #stdPremium").val();
					if (stdPremium === null || "" === stdPremium || "undefined" === stdPremium) {
						lion.util.info("警告", "标准保费不能为空");
						return false;
					}	
				}
				com.orbps.common.insuranceList.push(insurance);
				com.orbps.common.insuranceCount++;
				com.orbps.common.proposalGroupVo.insuranceInfoVos.push(insurance);
				// 刷新table
				com.orbps.common.reloadInsuranceModalTable();
				// form清空
				com.orbps.common.clearInsuranceInfoForm();
			}
		}
	});

	// 清空保险表单
	com.orbps.common.clearInsuranceInfoForm = function(){
		$("#insuranceInfoForm input[type='text']").val("");
		$("#insuranceInfoForm select").combo("clear");
		$(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
	}

	$("#insuranceInfoForm #polCode").blur(
		function(){
			var polCode=$("#insuranceInfoForm #polCode").val();
            if (polCode === null || "" === polCode
                    || "undefined" === polCode) {
                lion.util.info("警告", "险种代码不能为空");
                return false;
            }
		});

	//修改
	$("#insuranceInfoForm #btnEdit").click(function() {
		if(com.orbps.common.insuranceInfoForm.validate().form()){
			var insuredBtuVo =$("#insuranceInfoForm").serializeObject();
			// 表单验证成功后执行
			if (com.orbps.common.reloadInsuranceType > -1) {
				com.orbps.common.proposalGroupVo.insuranceInfoVos[com.orbps.common.reloadInsuranceType] = insuredBtuVo;
				com.orbps.common.reloadInsuranceType = -1;
			} 
			// 刷新table
			com.orbps.common.reloadInsuranceModalTable();
			
			// form清空
			com.orbps.common.clearInsuranceInfoForm();
		}else{
			return;
		}
	});
	
	
	// 删除功能
	com.orbps.common.insuranceDelete = function(vform){
		if(confirm("您确认要删除此条险种信息？")){
			if (com.orbps.common.proposalGroupVo.insuranceInfoVos !== null && com.orbps.common.proposalGroupVo.insuranceInfoVos.length > 0) {
				com.orbps.common.proposalGroupVo.insuranceInfoVos.splice(vform, 1);
				com.orbps.common.insuranceCount--;
			}
			// 刷新table
			com.orbps.common.reloadInsuranceModalTable();
			// form清空
			com.orbps.common.clearInsuranceInfoForm();
		}
	};
	
	// 清空功能
	$("#insuranceInfoForm #btnClear").click(function() {
		com.orbps.common.clearInsuranceInfoForm();
	});
	
	// 查询险种代码是否重复
	$("#insuranceInfoForm #polCode").blur(function() {
		com.orbps.common.querypolCode();
	});
	
	// 险种代码是否重复的函数
	com.orbps.common.querypolCode = function (){
		var polCode = $("#insuranceInfoForm #polCode").val();
		if(polCode!==""){
			for (var i = 0; i < com.orbps.common.proposalGroupVo.insuranceInfoVos.length; i++) {
				var array_element = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].polCode;
				if(polCode===array_element){
					lion.util.info("提示","险种代码已存在,请重新输入");
					return false;
				}
			}
		}
		return true;
	}
	
	// 点击radio，将其数据回显到录入界面
	com.orbps.common.insuranceRadio = function(vform) {
		// 清空页面
		com.orbps.common.clearInsuranceInfoForm();
		var radioVal;
		var temp = document.getElementsByName("insuredRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.common.reloadInsuranceType = radioVal;
		var insuredVo = com.orbps.common.proposalGroupVo.insuranceInfoVos[radioVal];
		//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
		setTimeout(function(){
			// 回显
			com.orbps.common.showInsurance(insuredVo);
		},400);
	};
});


//回显方法
com.orbps.common.showInsurance = function(msg){
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
com.orbps.common.reloadInsuranceModalTable = function () {
	$('#grpPolicyList').find("tbody").empty();
	if (com.orbps.common.proposalGroupVo.insuranceInfoVos !== null && com.orbps.common.proposalGroupVo.insuranceInfoVos.length > 0) {
		for (var i = 0; i < com.orbps.common.proposalGroupVo.insuranceInfoVos.length; i++) {
			var faceAmnt = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].faceAmnt;
			if (faceAmnt == undefined){
				faceAmnt = "";
			}
		    var premium = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].premium;
		    if (premium == undefined){
		    	premium = "";
		    }
		    var stdPremium = com.orbps.common.proposalGroupVo.insuranceInfoVos[i].stdPremium;
		    if(stdPremium == undefined){
		    	stdPremium = "";
		    }
			$('#grpPolicyList').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.common.insuranceRadio();' id='insuredRad"
							+ i
							+ "' name='insuredRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.common.proposalGroupVo.insuranceInfoVos[i].polCode
							+ "</td><td >"
							+ faceAmnt
							+ "</td><td >"
							+ premium
							+ "</td><td >"
							+ stdPremium
							+ "</td><td ><a href='javascript:void(0);' onclick='com.orbps.common.insuranceDelete("+i+");' for='insuredRad' id='insuredDel" 
							+ i + "'>删除</a></td></tr>");
		}
	} else {
		$('#grpPolicyList').find("tbody").append("<tr><td colspan='6' align='center'>无记录</td></tr>");
	}
};
