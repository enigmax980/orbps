com.orbps.contractReview.grpInsurAppl.payInfoForm = $("#payInfoForm");
com.orbps.contractReview.grpInsurAppl.settleList = [];
var numfalg = 0;//用于记录change事件的次数
// 基本信息校验规则
com.orbps.contractReview.grpInsurAppl.payValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		rules : {
//			moneyinItrvl : {
//				required : true
//			},
//			premFrom : {
//				required : true
//			},
//			stlType : {
//				required : true
//			}
		},
		messages : {
//			moneyinItrvl : {
//				required : '请选择缴费方式'
//			},
//			premFrom : {
//				required : '请选择保费来源'
//			},
//			stlType : {
//				required : "请选择结算方式"
//			}
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

    // 初始化校验函数
    com.orbps.contractReview.grpInsurAppl
            .payValidateForm(com.orbps.contractReview.grpInsurAppl.payInfoForm);
  //根据结算方式更改页面样式
	$("#payInfoForm #stlType").change(function() {
		//用于记录change事件的次数。
		numfalg++;
		var stlType = $("#stlType").val();
		if (stlType === "N") {
			$("#payInfoForm #stlLimit").attr("disabled", true);
			$("#payInfoForm #settlementRatio").attr("disabled", true);
			$("#payInfoForm #btnStlDate").attr("disabled", true);
			//页面加载时也会有chang事件，然后值已经回显了，在清空就会有问题了。
			if(numfalg > 2){
				$("#payInfoForm #stlLimit").val("");
				$("#payInfoForm #settlementRatio").val("");
			}
		} else if (stlType === "X") {
			$("#stlLimit").attr("disabled", false);
			$("#settlementRatio").attr("disabled", true);
			$("#btnStlDate").attr("disabled", false);
			//页面加载时也会有chang事件，然后值已经回显了，在清空就会有问题了。
			if(numfalg > 2){
				$("#payInfoForm #settlementRatio").val("");
			}
		} else if (stlType === "D") {
			$("#stlLimit").attr("disabled", true);
			$("#settlementRatio").attr("disabled", true);
			$("#btnStlDate").attr("disabled", false);
			//页面加载时也会有chang事件，然后值已经回显了，在清空就会有问题了。
			if(numfalg > 2){
				$("#payInfoForm #stlLimit").val("");
				$("#payInfoForm #settlementRatio").val("");
			}
		} else if (stlType === "L") {
			$("#stlLimit").attr("disabled", true);
			$("#settlementRatio").attr("disabled", false);
			$("#btnStlDate").attr("disabled", true);
			//页面加载时也会有chang事件，然后值已经回显了，在清空就会有问题了。
			if(numfalg > 2){
				$("#payInfoForm #settlementRatio").val("");
			}
		}
	});
	//如果交费形式是现金或者银行交款单，置灰交费开户行，开户名称，银行账号输入框
	$("#moneyinType,#premFrom").change(
			function() {
				var moneyinType = $("#moneyinType").val();
				if (moneyinType === "R" || moneyinType === "C"
						|| $("#premFrom").val() === "2") {
					$("#bankCode").attr("disabled", true);
					$("#bankCode").combo("clear");
					$("#bankName").attr("disabled", true);
					$("#bankName").val("");
					$("#bankAccNo").attr("disabled", true);
					$("#bankAccNo").val("");
					$("#bankCodeIs").hide();
					$("#bankNameIs").hide();
					$("#bankAccNoIs").hide();
				} else {
					$("#moneyinType").attr("disabled", false);
					$("#bankCode").attr("disabled", false);
					$("#bankName").attr("disabled", false);
					$("#bankAccNo").attr("disabled", false);
					$("#bankCodeIs").show();
					$("#bankNameIs").show();
					$("#bankAccNoIs").show();
				}
			});
	$("#premFrom").change(function() {
		var premFrom = $("#premFrom").val();
		//如果保费来源是个人账户或团体账户时供费比例和供费金额置灰
		if (premFrom === "2") {
			$("#multiPartyScale").attr("disabled", true);
			$("#multiPartyScale").combo("clear");
			$("#multiPartyMoney").attr("disabled", true);
			$("#multiPartyMoney").combo("clear");
			$("#bankCode").attr("disabled", true);
			$("#bankCode").combo("clear");
			$("#bankName").attr("disabled", true);
			$("#bankName").val("");
			$("#bankAccNo").attr("disabled", true);
			$("#bankAccNo").val("");
			$("#moneyinType").combo("clear");
			$("#moneyinType").attr("disabled", true);
		} else if (premFrom === "1") {
			$("#multiPartyScale").attr("disabled", true);
			$("#multiPartyScale").combo("clear");
			$("#multiPartyMoney").attr("disabled", true);
			$("#multiPartyMoney").combo("clear");
		} else {
			$("#multiPartyScale").attr("disabled", false);
			$("#multiPartyMoney").attr("disabled", false);
		}
	});

	//如果交费形式不是现金和银行交款单，校验银行账号必须输入
	$("#payInfoForm #bankAccNo").blur(function() {
		var moneyinType = $("#moneyinType").val();
		if (moneyinType === "R" || moneyinType === "C") {
		} else {
			var bankaccNo = $("#payInfoForm #bankAccNo").val();
			if (bankaccNo === null || "" === bankaccNo) {
				lion.util.info("警告", "银行账号不能为空");
				return false;
			}
		}
	});

	//如果交费形式不是现金和银行交款单，校验开户行必须输入
	$("#payInfoForm #bankCode").blur(function() {
		var moneyinType = $("#moneyinType").val();
		if (moneyinType === "R" || moneyinType === "C") {
		} else {
			var bankCode = $("#payInfoForm #bankCode").val();
			if (bankCode === null || "" === bankCode) {
				lion.util.info("警告", "开户银行不能为空");
				return false;
			}
		}
	});
	
	//如果交费形式不是现金和银行交款单，校验开户名必须输入
	$("#payInfoForm #bankName").blur(function() {
		var moneyinType = $("#moneyinType").val();
		var bankName = $("#payInfoForm #bankName").val();
		if (moneyinType === "R" || moneyinType === "C") {
		} else {
			if (bankName === null || "" === bankName) {
				lion.util.info("警告", "开户行名不能为空");
				return false;
			}
		}
		var $bankNameInput = $(this);
		// 最后一位是小数点的话，移除
		$bankNameInput.val(($bankNameInput.val().replace(/\.$/g, "")));
		// 投银行名只能有一个空格
		bankName = bankName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
		$("#payInfoForm #bankName").val(bankName);
	});

	$("#payInfoForm #stlLimit").blur(
			function() {
				var stlLimit = $("#payInfoForm #stlLimit").val();
				if (stlLimit === null || "" === stlLimit) {
					lion.util.info("警告", "结算限额不能为空");
					return false;
				}
				var sumPrem = $("#proposalInfoForm #sumPrem").val();
				if (parseFloat(stlLimit) > parseFloat(sumPrem)
						|| parseFloat(stlLimit) <= 0) {
					lion.util.info("提示", "结算限额应<=总保费" + sumPrem + "并且>0");
					return false;
				}
			});
	
	$("#payInfoForm #settlementRatio").blur(function() {
		var stlLimit = $("#payInfoForm #settlementRatio").val();
		if (stlLimit === null || "" === stlLimit) {
			lion.util.info("警告", "结算比例不能为空");
			return false;
		}
		if (parseFloat(stlLimit) > 100 || parseFloat(stlLimit) <= 0) {
			lion.util.info("警告", "结算比例应<=100%,>0%");
			return false;
		}
	});

});



// 定期结算日期
$("#btnStlDate")
        .click(
                function() {
                    com.orbps.contractReview.grpInsurAppl.addDialog.empty();
                    com.orbps.contractReview.grpInsurAppl.addDialog
                            .load(
                                    "/orbps/orbps/contractReview/grpInsurAppl/html/settlementDateModal.html",
                                    function() {
                                        $(this).modal('toggle');
                         });
                setTimeout(function() {
                	com.orbps.contractReview.grpInsurAppl.reloadsettlementInsuredModalTable();
                 }, 100);
          });


//重新加载表格
com.orbps.contractReview.grpInsurAppl.reloadsettlementInsuredModalTable = function () {
    $('#settlementDataGrid').find("tbody").empty();
    if (com.orbps.contractReview.grpInsurAppl.settleList != null && com.orbps.contractReview.grpInsurAppl.settleList.length > 0) {
        for (var i = 0; i < com.orbps.contractReview.grpInsurAppl.settleList.length; i++) {
        	settlementDatas = new Date(com.orbps.contractReview.grpInsurAppl.settleList[i]).format("yyyy-MM-dd");
        	$('#settlementDataGrid').find("tbody")
                    .append("<tr role='row' class='odd' id='settlementDataGridtr0' data-type='add' data-updating='false'><td ><input type='radio' onclick='com.orbps.contractReview.grpInsurAppl.dateRadio();' id='insuredRad"
                            + i
                            + "' name='insuredRad' value='"
                            + i
                            + "'></td><td >"
                            + settlementDatas
                            + "</td><td ><a href='javascript:void(0);' onclick='com.orbps.contractReview.grpInsurAppl.deleteSettleDate("+i+");' for='insuredDel' id='insuredDel" 
							+ i + "'>删除</a></td></tr>"
                            );
        }
    } else {
        $('#settlementDataGrid').find("tbody").append("<tr><td colspan='3' align='center'>无记录</td></tr>");
    }
} 

//开户名校验规则
$("#payInfoForm #bankName")
		.on(
				'keyup',
				function(event) {
					var $bankNameInput = $(this);
					// 响应鼠标事件，允许左右方向键移动
					event = window.event || event;
					//获取光标所在文本中的下标
					var pos = getTxt1CursorPosition(this);
					if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
						return ;
					}
					// 先把非数字的都替换掉，除了数字和.
					$bankNameInput.val($bankNameInput.val().replace(
							/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\.\(\)\（\）\·\-\ ]/g,
							''));
					//移动光标到所定位置
					setCaret(this,pos);
					var hldrName = $("#payInfoForm #bankName").val();
					var len = hldrName.length;
					var reLen = 0;
					for (var i = 0; i < len; i++) {
						if (hldrName.charCodeAt(i) < 27
								|| hldrName.charCodeAt(i) > 126) {
							// 全角
							reLen += 2;
						} else {
							reLen++;
						}
					}
					if (reLen > 200) {
						hldrName = hldrName.substring(0, hldrName.length - 1);
						$("#payInfoForm #bankName").val(hldrName);
					}
				});
