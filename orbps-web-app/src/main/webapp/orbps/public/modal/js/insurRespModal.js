com.orbps.common.insurRespProductNum = [];
com.orbps.common.insurRespProductPremium = [];
//初始化函数
$(function() {
	
	// 迭代循环责任中选择的该险种责任
	com.orbps.common.reloadDatas = function(arrs){
		if(lion.util.isNotEmpty(com.orbps.common.list)){
			for (var i = 0; i < com.orbps.common.list.length; i++) {
				var array_element = com.orbps.common.list[i];
				var productCodes = array_element.productCode;
				var productCodesStart = productCodes.split("-")[0];
				if(arrs===productCodesStart){
					com.orbps.common.list.splice(i,1);
					com.orbps.common.reloadDatas(arrs);
				}
			}
		}
	}
	
	//添加责任
	$("#btnInsure").click(function() {
		var grpInsurApplList = $("#coverageInfo_list").editDatagrids("getSelectRows");
		if(grpInsurApplList!==null){
			if(Object.prototype.toString.call(grpInsurApplList) === '[object Array]'){
				for(var i = 0;i<grpInsurApplList.length;i++){
					if("productNum" in grpInsurApplList[i] || "productPremium" in grpInsurApplList[i]||"subStdPremium" in grpInsurApplList[i] ){					
						if(grpInsurApplList[i].productNum ==="" || grpInsurApplList[i].productPremium==="" ||grpInsurApplList[i].subStdPremium===""){
							lion.util.info("提示","保额，保费，标准保费不能为空！");
							return false;
						}
						grpInsurApplList[i].busiPrdCode = com.orbps.common.busiPrdCode;
					}else{
						lion.util.info("提示","保额，保费，标准保费不能为空！");
						return false;
					}
				}	
			}else{
				if("productNum" in grpInsurApplList || "productPremium" in grpInsurApplList||"subStdPremium" in grpInsurApplList){
					if(grpInsurApplList.productNum ==="" || grpInsurApplList.productPremium===""||grpInsurApplList.subStdPremium===""){
						lion.util.info("提示","保额，保费，标准保费不能为空！");
						return false;
					}
					grpInsurApplList.busiPrdCode = com.orbps.common.busiPrdCode;
				}else{
					lion.util.info("提示","保额，保费，标准保费不能为空！");
					return false;
				}
			}
		}
		var productCodeIndex = [];
		var arr = com.orbps.common.list;
		com.orbps.common.insurRespProductPremium=[];
		if(lion.util.isNotEmpty(grpInsurApplList)){
			// 判断选择的是否是多条
			if(Object.prototype.toString.call(grpInsurApplList) === '[object Array]'){
				var productCodeStart = grpInsurApplList[0].productCode;
			}else{
				var productCodeStart = grpInsurApplList.productCode;
			}
			var arrs = productCodeStart.split("-")[0];
			// 删除选中险种下的所有责任
			com.orbps.common.reloadDatas(arrs);
			// 判断选择的是否是多条
			if(Object.prototype.toString.call(grpInsurApplList) === '[object Array]'){
				for (var y = 0; y < grpInsurApplList.length; y++) {
					var array_element = grpInsurApplList[y];
					com.orbps.common.list.push(array_element);
					var productNum = grpInsurApplList[y].productNum;
					var productPremium = grpInsurApplList[y].productPremium;
					com.orbps.common.insurRespProductNum.push(productNum);
					com.orbps.common.insurRespProductPremium.push(productPremium);
				}
			}else{
				com.orbps.common.list.push(grpInsurApplList);
				com.orbps.common.insurRespProductNum.push(grpInsurApplList.productNum);
				com.orbps.common.insurRespProductPremium.push(grpInsurApplList.productPremium);
			}
			var div = document.getElementById('productModal'); 
			// 关闭modal
			div.setAttribute("data-dismiss", "modal"); 
			setTimeout(function(){
				lion.util.info("提示","添加责任信息成功！");
			},300);
		}else{
			//清空选择该险种的所有责任
			com.orbps.common.reloadDatas(com.orbps.common.busiPrdCode);
			var div = document.getElementById('productModal'); 
			// 关闭modal	
			div.setAttribute("data-dismiss", "modal"); 
			setTimeout(function(){
				lion.util.info("提示","清空"+com.orbps.common.busiPrdCode+"险种的责任！");
			},300);
		}
		setTimeout(function(){
			var premium = 0.00;
			for (var i = 0; i < com.orbps.common.insurRespProductPremium.length; i++) {
				var productPremium =parseFloat(com.orbps.common.insurRespProductPremium[i]);
				premium += productPremium;
			}
			var amount = 0.00;
			for (var j = 0; j < com.orbps.common.insurRespProductNum.length; j++) {
				var productNum = parseFloat(com.orbps.common.insurRespProductNum[j]);
				amount += productNum;
			}
			// 回显险种信息
			com.orbps.common.reloadPre(amount,premium);
		},500);
	});
	//当保费离开焦点时把保费的值回显到标准保费里
	$('body').delegate('table#coverageInfo_list #productPremium' , 'blur', function(){
		var subStdPremium =  $("#coverageInfo_list #productPremium").val();
		$("#subStdPremium").val(subStdPremium);
	});
});

//聚焦责任名称事件
$('body').delegate('table#coverageInfo_list #productName' , 'focus', function(){
	$("#productName").attr("readOnly",true);
});

//聚焦责任代码事件
$('body').delegate('table#coverageInfo_list #productCode' , 'focus', function(){
	$("#productCode").attr("readOnly",true);
});

//保额键盘抬起事件
$('body').delegate('table#coverageInfo_list #productNum' , 'keyup', function(){
	if(!isNaN(this.value)){this.value=/^\d+\.?\d{0,2}$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-1)}else{this.value = ''};
	$("#productNum").val(this.value);
});

// 保额丢失焦点事件
$('body').delegate('table#coverageInfo_list #productNum' , 'blur', function(){
	if(!isNaN(this.value)){if(this.value<=0){this.value = ''}this.value=/^\d+\.?\d{0,2}$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-1)}else{this.value = ''};
	$("#productNum").val(this.value);
});

// 保费键盘抬起事件
$('body').delegate('table#coverageInfo_list #productPremium' , 'keyup', function(){
	if(!isNaN(this.value)){this.value=/^\d+\.?\d{0,2}$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-1)}else{this.value = ''};
	$("#productPremium").val(this.value);
});

// 保费丢失焦点事件
$('body').delegate('table#coverageInfo_list #productPremium' , 'blur', function(){
	if(!isNaN(this.value)){if(this.value<=0){this.value = ''}this.value=/^\d+\.?\d{0,2}$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-1)}else{this.value = ''};
	$("#productPremium").val(this.value);
});

// 标准保费键盘抬起事件
$('body').delegate('table#coverageInfo_list #subStdPremium' , 'keyup', function(){
	if(!isNaN(this.value)){this.value=/^\d+\.?\d{0,2}$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-1)}else{this.value = ''};
	$("#subStdPremium").val(this.value);
});

// 标准保费丢失焦点事件
$('body').delegate('table#coverageInfo_list #subStdPremium' , 'blur', function(){
	if(!isNaN(this.value)){if(this.value<=0){this.value = ''}this.value=/^\d+\.?\d{0,2}$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-1)}else{this.value = ''};
	$("#subStdPremium").val(this.value);
});
