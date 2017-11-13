com.orbps.otherFunction.contractQuery.dateType = -1;
com.orbps.otherFunction.contractQuery.dateCount = 0;
//初始化
$(function() {

	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});

	//新增
	$("#btnDateAdd").click(
			function() {
				var settlementDate = $("#settlementfoForm #settlementDate").val();
				if (settlementDate !== "") {
					com.orbps.otherFunction.contractQuery.settlementDateList
							.push(settlementDate);
					com.orbps.otherFunction.contractQuery
							.reloadsettlementInsuredModalTable();
					lion.util.info("提示", "保存成功");
					// form清空
					$("#settlementfoForm input[type='text']").val("");
				} else {
					lion.util.info("提示", "请输入日期");
					return false;
				}
			});
	
	//修改
	$("#btnEdit").click(function() {
		var settlementDate = $("#settlementfoForm #settlementDate").val();
		if(settlementDate !== ""){
			if (com.orbps.otherFunction.contractQuery.dateType > -1) {
				com.orbps.otherFunction.contractQuery.settlementDateList[com.orbps.otherFunction.contractQuery.dateType] = settlementDate;
				com.orbps.otherFunction.contractQuery.dateType = -1;
			}
			// 刷新table
			com.orbps.otherFunction.contractQuery.reloadsettlementInsuredModalTable();
			lion.util.info("提示", "修改日期成功");
			// form清空
			$("#settlementfoForm input[type='text']").val("");
		} else {
			lion.util.info("提示", "请输入日期");
			return false;
		}
	});
	
	
	// 删除功能
	com.orbps.otherFunction.contractQuery.deleteSettleDate = function(vform){
		if(confirm("您确认要删除此条信息？")){
			if (com.orbps.otherFunction.contractQuery.settlementDateList !== null && com.orbps.otherFunction.contractQuery.settlementDateList.length > 0) {
				com.orbps.otherFunction.contractQuery.settlementDateList.splice(vform, 1);
			}
			// 刷新table
			com.orbps.otherFunction.contractQuery.reloadsettlementInsuredModalTable();
		}
	};
	
	// 点击radio，将其数据回显到录入界面
	com.orbps.otherFunction.contractQuery.dateRadio = function(vform) {
		$("#settlementfoForm input[type='text']").val("");
		var radioVal;
		//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
		var temp = document.getElementsByName("insuredRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.otherFunction.contractQuery.dateType = radioVal;
		insuredVo= com.orbps.otherFunction.contractQuery.settlementDateList[radioVal];
		setTimeout(function(){
			// 回显
			 $("#settlementfoForm #settlementDate").val(insuredVo);
		},100);
	};
     
 });

