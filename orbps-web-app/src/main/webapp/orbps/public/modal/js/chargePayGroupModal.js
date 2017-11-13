com.common.chargePayCount = 0;
com.common.chargePayType = -1;

// 基本信息校验规则
com.common.chargePayModalValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		rules : {
			groupNo : {
				required : true
			},
			groupName : {
				required : true
			},
			num : {
				required : true
			},
			moneyinType : {
				required : true
			}
		},
		messages : {
			groupNo : {
				required : "请填写组号"
			},
			groupName : {
				required : "请填写组名"
			},
			num : {
				required : "请填写人数"
			},
			moneyinType : {
				required : "请选择交费形式"
			}
		},
		invalidHandler : function(event, validator) {
			Metronic.scrollTo(error2, -200);
		},

		errorPlacement : function(error, element) {
			var icon = $(element).parent('.input-icon').children('i');
			icon.removeClass('fa-check').addClass("fa-warning");
			if (icon.attr('title')
					|| typeof icon.attr('data-original-title') != 'string') {
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

// 初始化函数
$(function() {
	setTimeout(function(){
		// 交费形势设置为只读
		$("#chargePayGroupInfoForm #moneyinType").attr("readOnly", true);
		if($("#chargePayGroupInfoForm #moneyinType").val() === ""){
			$("#chargePayGroupInfoForm #moneyinType").combo("val", "T");
		}
		},2000)
	// 初始化校验信息
	com.common.chargePayModalValidateForm($("#chargePayGroupInfoForm"));
	// 新增
	$("#chargePayGroupInfoForm #btnAdd").click(function() {
		if($("#chargePayGroupInfoForm").validate().form()){
			if($("#chargePayGroupInfoForm #moneyinType").val()==="T"){
				if($("#chargePayGroupInfoForm #bankCode").val()===""){
					lion.util.info("提示","请选择开户银行");
					return false;
				}
				if($("#chargePayGroupInfoForm #bankName").val()===""){
					lion.util.info("提示","请填写开户名称");
					return false;
				}
			}
			var insuredVos =$("#chargePayGroupInfoForm").serializeObject();
			for(var i=0; i<com.common.chargePayList.length; i++){
				if(com.common.chargePayList[i].groupNo === insuredVos.groupNo){
					lion.util.info("增加失败，收费组号重复");
					return false;
				}
			}
			com.common.chargePayList.push(insuredVos);
			com.common.chargePayCount++;
			// 刷新table
			com.common.reloadPublicChargePayModalTable();
			// form清空
			$("#chargePayGroupInfoForm input[type='text']").val("");
			$("#chargePayGroupInfoForm #bankCode").combo("clear");
			$("#chargePayGroupInfoForm #moneyinType").combo("val", "T");
			$(".fa").removeClass("fa-warning");
			$(".fa").removeClass("fa-check");
			$(".fa").removeClass("has-success");
			$(".fa").removeClass("has-error");
		}
	});

	//修改
	$("#chargePayGroupInfoForm #btnEdit").click(function() {
		if($("#chargePayGroupInfoForm").validate().form()){
			var insuredBtuVo =$("#chargePayGroupInfoForm").serializeObject();
			for(var i=0; i<com.common.chargePayList.length; i++){
				if(com.common.chargePayList[i].groupNo === insuredBtuVo.groupNo && i.toString() !== com.common.chargePayType){
					lion.util.info("修改失败，收费组号重复");
					return false;
				}
			}
			// 表单验证成功后执行
			if (com.common.chargePayType > -1) {
				com.common.chargePayList[com.common.chargePayType] = insuredBtuVo;
				com.common.chargePayType = -1;
			} 
			// 刷新table
			com.common.reloadPublicChargePayModalTable();
			
			// form清空
			$("#chargePayGroupInfoForm input[type='text']").val("");
			$("#chargePayGroupInfoForm #bankCode").combo("clear");
			$("#chargePayGroupInfoForm #moneyinType").combo("val", "T");
	        $(".fa").removeClass("fa-warning");
	        $(".fa").removeClass("fa-check");
	        $(".fa").removeClass("has-success");
	        $(".fa").removeClass("has-error");
		}
	});
	
	
	// 删除功能
	com.common.publicDeletes = function(vform){
		if(confirm("您确认要删除此条被保人信息？")){
			if (com.common.chargePayList !== null && com.common.chargePayList.length > 0) {
				com.common.chargePayList.splice(vform, 1);
				com.common.chargePayCount--;
			}
			// 刷新table
			com.common.reloadPublicChargePayModalTable();
			// form清空
			$("#chargePayGroupInfoForm input[type='text']").val("");
			$("#chargePayGroupInfoForm #bankCode").combo("clear");
			$("#chargePayGroupInfoForm #moneyinType").combo("val", "T");
	        $(".fa").removeClass("fa-warning");
	        $(".fa").removeClass("fa-check");
	        $(".fa").removeClass("has-success");
	        $(".fa").removeClass("has-error");
		}
	};
	
	// 清空功能
	$("#chargePayGroupInfoForm #btnClear").click(function() {
		$("#chargePayGroupInfoForm input[type='text']").val("");
		$("#chargePayGroupInfoForm #bankCode").combo("clear");
		$("#chargePayGroupInfoForm #moneyinType").combo("val", "T");
        $(".fa").removeClass("fa-warning");
        $(".fa").removeClass("fa-check");
        $(".fa").removeClass("has-success");
        $(".fa").removeClass("has-error");
	});
	
	// 点击radio，将其数据回显到录入界面
	com.common.chargePayRadio = function(vform) {
		$("#chargePayGroupInfoForm input[type='text']").val("");
		$("#chargePayGroupInfoForm select2").combo("refresh");
		var radioVal;
		//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
		var temp = document.getElementsByName("insuredRad");
		for(var i=0;i<temp.length;i++){
			if(temp[i].checked){
				 radioVal = temp[i].value;
			}
		}
		com.common.chargePayType = radioVal;
		insuredVo= com.common.chargePayList[radioVal];
		setTimeout(function(){
			// 回显
			com.common.showChargePay(insuredVo);
			$("input[name='insuredRad']").eq(radioVal).attr("checked","checked");
		},100);
	}
	
	// 交费形式
	$("#chargePayGroupInfoForm #moneyinType").change(function() {
		if($("#chargePayGroupInfoForm #moneyinType").val()==="T"){
			$("#chargePayGroupInfoForm #bankCode").attr("readOnly",false);
			$("#chargePayGroupInfoForm #bankName").attr("readOnly",false);
			$("#chargePayGroupInfoForm #bankAccNo").attr("readOnly",false);
		}else{
			$("#chargePayGroupInfoForm #bankCode").attr("readOnly",true);
			$("#chargePayGroupInfoForm #bankName").attr("readOnly",true);
			$("#chargePayGroupInfoForm #bankAccNo").attr("readOnly",true);
		}
	});
	
});


//回显方法
com.common.showChargePay = function(msg){
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
                    $("#chargePayGroupInfoForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
                $("#chargePayGroupInfoForm #"+key).combo("val", value);
            }
        });
    }
}

//重新加载表格
com.common.reloadPublicChargePayModalTable = function () { 
	$('#chargePayGroupInfoTable').find("tbody").empty();
	if (com.common.chargePayList !== null && com.common.chargePayList.length > 0) {
		for (var i = 0; i < com.common.chargePayList.length; i++) {
			var bankCode = "";
			if(com.common.chargePayList[i].bankCode!==""){
				bankCode = com.orbps.publicSearch.publicQuerySelectText("#bankCode",com.common.chargePayList[i].bankCode);
			}
			var moneyinType = "";
			if(com.common.chargePayList[i].moneyinType!==""){
				moneyinType = com.orbps.publicSearch.moneyinTypeQuery(com.common.chargePayList[i].moneyinType);
			}
			$('#chargePayGroupInfoTable').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.common.chargePayRadio();' id='insuredRad"
							+ i
							+ "' name='insuredRad' value='"
							+ i
							+ "'></td><td >"
							+ com.common.chargePayList[i].groupNo
							+ "</td><td >"
							+ com.common.chargePayList[i].groupName
							+ "</td><td >"
							+ com.common.chargePayList[i].num
							+ "</td><td >"
							+ moneyinType
							+ "</td><td >"
							+ bankCode
							+ "</td><td >"
							+ com.common.chargePayList[i].bankName
							+ "</td><td >"
							+ com.common.chargePayList[i].bankAccNo
							+ "</td><td ><a href='javascript:void(0);' onclick='com.common.publicDeletes("+i+");' for='insuredRad' id='insuredDel" 
							+ i + "'>删除</a></td></tr>");
		}
	} else {
		$('#chargePayGroupInfoTable').find("tbody").append("<tr><td colspan='9' align='center'>无记录</td></tr>");
	}
};