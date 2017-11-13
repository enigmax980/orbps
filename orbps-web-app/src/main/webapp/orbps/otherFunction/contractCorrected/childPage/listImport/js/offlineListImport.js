com.orbps.otherFunction={};
com.orbps.otherFunction.contractCorrected={};
com.orbps.otherFunction.contractCorrected.childPage={};
// 新建otherFunction.offlineList命名空间
com.orbps.otherFunction.contractCorrected.childPage.listImport = {};
// 新建表单id
com.orbps.otherFunction.contractCorrected.childPage.listImport.offlineListImportForm = $("#offlineListImportForm");
// 新建表格id
com.orbps.otherFunction.contractCorrected.childPage.listImport.coverageListTb = $("#coverageListTb");
// 新建表单id
com.orbps.otherFunction.contractCorrected.childPage.listImport.ipsnInfoForm = $("#ipsnInfoForm");
//新建表单id
com.orbps.otherFunction.contractCorrected.childPage.listImport.hldrInfoForm = $("#hldrInfoForm");
// modal id
com.orbps.otherFunction.contractCorrected.childPage.listImport.btnModel = $("#btnModel");
// 新建businessKey全局变量
com.orbps.otherFunction.contractCorrected.childPage.listImport.businessKey;
// 新建taskId全局变量
com.orbps.otherFunction.contractCorrected.childPage.listImport.taskId;
// 新建要约信息list
com.orbps.otherFunction.contractCorrected.childPage.listImport.insuredList = [];
// 新建受益人信息list
com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList = [];
// 新建insuredCount
com.orbps.otherFunction.contractCorrected.childPage.listImport.insuredCount = 0;
// 新建insuredType
com.orbps.otherFunction.contractCorrected.childPage.listImport.insuredType = -1;
//上一人 下一任 标示
com.orbps.otherFunction.contractCorrected.childPage.listImport.flag;
//页面信息的全局变量
com.orbps.otherFunction.contractCorrected.childPage.listImport.data;
//查询任务ID回调函数
//com.orbps.otherFunction.contractCorrected.childPage.listImport.successQueryDetail = function (data,args){
//	com.orbps.otherFunction.contractCorrected.childPage.listImport.businessKey = data.bizNo;
//	com.orbps.otherFunction.contractCorrected.childPage.listImport.taskId = data.taskId;
//};
$(function() {
	//查询任务ID回调函数
	com.orbps.otherFunction.contractCorrected.childPage.listImport.successQueryDetail = function (data,args){
		com.orbps.otherFunction.contractCorrected.childPage.listImport.data = data;
		//alert("查询的保单类型"+data.cntrType);
		if("G"===data.cntrType){
			$("#hldrInfoForm").hide();
		}
		if("1"===data.premSource){
			$("#AccInfo").hide();
		}
		if(null !== data.insuredGroupModalVos && data.insuredGroupModalVos.length>0){
			var groupCode = document.getElementById("groupCode");
			for(var i = 0; i < data.insuredGroupModalVos.length; i++){
				var insuredGroupModalVos= data.insuredGroupModalVos[i];
				var groupCodeid = insuredGroupModalVos.ipsnGrpNo;
				var groupCodename=insuredGroupModalVos.ipsnGrpNo;
	        	// 添加属组option
	        	groupCode.options.add(new Option(groupCodeid,groupCodename));	
			}
		}
	};
	// 回显投保单号
	$("#ipsnInfoFormQuery #applNo").val($("#applInfoForm #applNo").val());
	// 设置投保单号只读
	$("#ipsnInfoFormQuery #applNo").attr("readOnly",true);
	//设置属组名称只读
	$("#groupName").attr("readOnly",true);
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    var taskInfo = {};
    taskInfo.bizNo=$("#applInfoForm #applNo").val();
    lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/comback",taskInfo,com.orbps.otherFunction.contractCorrected.childPage.listImport.successQueryDetail,null,null);
	
    // 对日期进行单独初始化，控制日期选择范围
    $("#taskEndTime").datepicker({
        autoclose : true,
        language : 'zh-CN',
        endDate : new Date()
    });
    
    // 对日期进行单独初始化，控制日期选择范围
    $("#taskStartTime").datepicker({
        autoclose : true,
        language : 'zh-CN',
        endDate : new Date()
    });
    
	// combo组件初始化
    $("*").comboInitLoad();
});
//根据属组回显相应的险种信息以及责任名称
$("#bsInfoForm #groupCode").change(function(){
	com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList = [];
	com.orbps.otherFunction.contractCorrected.childPage.listImport.listImportBsInfoVo=[];
	var insuredVos = {};
	var listImportBsInfoVo = {};
	var listImportSubPolVo = {};
	var name="";
	var groupCode = $("#bsInfoForm #groupCode").val();
	var data = com.orbps.otherFunction.contractCorrected.childPage.listImport.data;
	for(var i = 0; i < data.insuredGroupModalVos.length; i++){
		var insuredGroupModalVos= data.insuredGroupModalVos[i];
		if(parseInt(groupCode) === insuredGroupModalVos.ipsnGrpNo){
			name=insuredGroupModalVos.ipsnGrpName;
			for (var k = 0; k < insuredGroupModalVos.insuranceInfoVos.length; k++) {
				var insuredVos = {};
				var listImportBsInfoVo = {};
				var listImportSubPolVo = {};
				var polCode="";
				var polName="";
				var subPolName="";
				//添加到相应的vo里
				listImportSubPolVo.subPolCode = insuredGroupModalVos.insuranceInfoVos[k].polCode;
				listImportSubPolVo.groupCode =insuredGroupModalVos.ipsnGrpNo;
				listImportSubPolVo.groupName =insuredGroupModalVos.ipsnGrpName;
				listImportSubPolVo.amount = insuredGroupModalVos.insuranceInfoVos[k].faceAmnt;
				listImportSubPolVo.premium = insuredGroupModalVos.insuranceInfoVos[k].premium;
				//添加到列表中
				insuredVos.subPolCode = insuredGroupModalVos.insuranceInfoVos[k].polCode;
				insuredVos.groupCode =insuredGroupModalVos.ipsnGrpNo;
				insuredVos.groupName =insuredGroupModalVos.ipsnGrpName;
				insuredVos.amount = insuredGroupModalVos.insuranceInfoVos[k].faceAmnt;
				insuredVos.premium = insuredGroupModalVos.insuranceInfoVos[k].premium;
				for (var j = 0; j < data.bsInfoVo.length; j++) {
					if(insuredGroupModalVos.insuranceInfoVos[k].polCode.substring(0,3) === data.bsInfoVo[j].polCode){
						polName = data.bsInfoVo[j].polName;
						polCode = data.bsInfoVo[j].polCode;
						for (var s = 0; s < data.bsInfoVo[j].listImportSubPolVos.length; s++) {
							if(insuredGroupModalVos.insuranceInfoVos[k].polCode === data.bsInfoVo[j].listImportSubPolVos[s].subPolCode){
								subPolName=data.bsInfoVo[j].listImportSubPolVos[s].subPolName;
							}
						}
						
					}
				}
				listImportBsInfoVo.polCode = polCode;
				listImportBsInfoVo.polName =  polName;
				listImportSubPolVo.subPolName = subPolName;
				insuredVos.polCode = polCode;
				insuredVos.polName =  polName;
				insuredVos.subPolName = subPolName;
				listImportBsInfoVo.listImportSubPolVos = listImportSubPolVo;
				//后台的数据格式的全局变量
				com.orbps.otherFunction.contractCorrected.childPage.listImport.listImportBsInfoVo.push(listImportBsInfoVo);
				com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList.push(insuredVos);
				com.orbps.otherFunction.contractCorrected.childPage.listImport.insuredCount++;
			}
			
		}else{
			$("#bsInfoForm #groupName").val("");
		}
	}
	$("#bsInfoForm #groupName").val(name);
	com.orbps.otherFunction.contractCorrected.childPage.listImport.reloadPublicInsuredModalTable();
});

// 按回车相当于tab功能(键盘按键触发事件)
$("input:text").keypress(function (e) {
    if (e.which == 13) {// 判断所按是否回车键  
        var inputs = $("input:text "); // 获取表单中的所有输入框  
        var selects = $("select"); // 获取表单中的所有输入框  
        var idx = inputs.index(this); // 获取当前焦点输入框所处的位置  
        inputs[idx + 1].focus(); // 设置焦点  
        inputs[idx + 1].select(); // 选中文字  
        return false; // 取消默认的提交行为  
    }
});

// 是否连带被保险人
$("#joinIpsnFlag").change(function(){
	var joinIpsnFlag = $("#joinIpsnFlag").val();
	if(joinIpsnFlag=="N"){
		$("#relToIpsn").combo("val", "M");
		$("#relToIpsn").attr("readOnly",true);
	}else{
		$("#relToIpsn").attr("readOnly",false);
	}
});

	setTimeout(function(){
		//连带保险人下拉框默认显示否
		$("#joinIpsnFlag").combo("val","N");
	},1000);
	setTimeout(function(){
		//是否在职下拉框默认显示否
		$("#onJobFlag").combo("val","N");
	},1000);
	
	setTimeout(function(){
		//是否在职下拉框默认显示否
		$("#healthFlag").combo("val","N");
	},1000);

//重新加载险种表格
com.orbps.otherFunction.contractCorrected.childPage.listImport.reloadPublicInsuredModalTable = function () {
	var str="</td><td ></td></tr>";
	$('#coverageListTb').find("tbody").empty();
	var c=com.orbps.otherFunction.contractCorrected.childPage.listImport.data; 
	if (com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList !== null && com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList.length; i++) {
			if("subPolCode" in com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i]){
			}else{
				com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].subPolCode = "";
			}
			if("subPolName" in com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i]){
			}else{
				com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].subPolName = "";
			}
			if("groupCode" in com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i]){
			}else{
				com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].groupCode = "";
			}
			if("groupName" in com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i]){
			}else{
				com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].groupName = "";
			}
			if(isEmpty(com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].groupCode, "")){
				str="</td><td ><a href='javascript:void(0);' onclick='com.orbps.otherFunction.contractCorrected.childPage.listImport.publicDeletes("+i+");' for='insuredRad' id='insuredDel" 
				+ i + "'>删除</a></td></tr>";
			}
			
			$('#coverageListTb').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.contractCorrected.childPage.listImport.publicRadio();' id='insuredRad"
							+ i
							+ "' name='insuredRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].polCode
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].polName
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].subPolCode
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].subPolName
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].groupCode
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].groupName
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].amount
							+ "</td><td >"
							+ com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList[i].premium
							+ str);
		}
	} else {
		$('#coverageListTb').find("tbody").append("<tr><td colspan='10' align='center'>无记录</td></tr>");
	}
}

//重新加载受益人表格
com.orbps.otherFunction.contractCorrected.childPage.listImport.reloadPublicBeneModalTable = function () { 
	$('#beneInfoTb').find("tbody").empty();
	if (com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList !== null && com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList.length; i++) {
			var benefitOrder = '';
			if("benefitOrder" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				benefitOrder = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].benefitOrder;
			}
			var name = '';
			if("name" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				name = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].name;
			}
			var sex = '';
			if("sex" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				sex = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].sex;
				sex = com.orbps.publicSearch.sex(sex);
			}
			var birthDate = '';
			if("birthDate" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				birthDate = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].birthDate;
				birthDate = new Date(birthDate).format("yyyy-MM-dd");
			}
			var relToIpsn = '';
			if("relToIpsn" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				relToIpsn = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].relToIpsn;
			}
			var beneAmount = '';
			if("beneAmount" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				beneAmount = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].beneAmount;
			}
			var idType = '';
			if("idType" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				idType = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].idType;
			}
			var idNo = '';
			if("idNo" in com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i]){
				idNo = com.orbps.otherFunction.contractCorrected.childPage.listImport.beneList[i].idNo;
			}
			$('#beneInfoTb').find("tbody")
					.append("<tr><td >"
							+ benefitOrder
							+ "</td><td >"
							+ name
							+ "</td><td >"
							+ sex
							+ "</td><td >"
							+ birthDate
							+ "</td><td >"
							+ relToIpsn
							+ "</td><td >"
							+ beneAmount
							+ "</td><td >"
							+ idType
							+ "</td><td >"
							+ idNo
							+ "</td></tr>");
		}
	} else {
		$('#beneInfoTb').find("tbody").append("<tr><td colspan='8' align='center'>无记录</td></tr>");
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
}  
//被保险人查询
$('#ipsnInfoFormQuery #btnQuery').click(function(){
	com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList = [];
	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
	var applNo = $('#ipsnInfoFormQuery #applNo').val();
	if(applNo!==""){
		//var custNo = $('#ipsnInfoFormQuery #custNo').val();
        var ipsnNo = $('#ipsnInfoFormQuery #ipsnNo').val();
        if(ipsnNo!==""){
        	$("#ipsnInfoFormQuery #ipsnNo").val("");
            lion.util.postjson(
                    '/orbps/web/orbps/contractEntry/offlineList/queryInsured',
                    listImportIpsnInfoVo,
                    com.orbps.otherFunction.contractCorrected.childPage.listImport.successQuery,
                    null,
                    null);
        }else{
            lion.util.info("提示","客户号或者被保险人编号至少填写一个")
        }
	}
});
// 上一个被保人
$('#ipsnInfoFormQuery #btnLast').click(function(){
	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
	var applNo = $('#ipsnInfoForm #applNo').val();
	if(applNo!==""){
        var ipsnNo = $('#ipsnInfoForm #ipsnNo').val();
        if(ipsnNo!==""){
        	//上一人标识
        	com.orbps.otherFunction.contractCorrected.childPage.listImport.flag = "s";
        	//清空被保人编号
        	listImportIpsnInfoVo.flag="pre";
        	listImportIpsnInfoVo.ipsnNo = ipsnNo;
            lion.util.postjson(
                    '/orbps/web/orbps/contractEntry/offlineList/queryNextInsured',
                    listImportIpsnInfoVo,
                    com.orbps.otherFunction.contractCorrected.childPage.listImport.successQuery,
                    null,
                    null);
        }else{
            lion.util.info("提示","客户号或者被保险人编号至少填写一个")
        }
	}
});
// 下一个被保人
$('#ipsnInfoFormQuery #btnNext').click(function(){
	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
	var applNo = $('#ipsnInfoFormQuery #applNo').val();
	if(applNo!==""){
		//var custNo = $('#ipsnInfoFormQuery #custNo').val();
        var ipsnNo = $('#ipsnInfoForm #ipsnNo').val();
        if(ipsnNo!==""){
        	//下一人标识
        	com.orbps.otherFunction.contractCorrected.childPage.listImport.flag = "x";
        	//清空被保人编号
        	listImportIpsnInfoVo.flag="next";
        	listImportIpsnInfoVo.ipsnNo = ipsnNo;
            lion.util.postjson(
                    '/orbps/web/orbps/contractEntry/offlineList/queryNextInsured',
                    listImportIpsnInfoVo,
                    com.orbps.otherFunction.contractCorrected.childPage.listImport.successQuery,
                    null,
                    null);
        }else{
            lion.util.info("提示","客户号或者被保险人编号至少填写一个")
        }
	}
});
// 查询成功回调函数
com.orbps.otherFunction.contractCorrected.childPage.listImport.successQuery = function (data,arg){
	//如果有被保险人信息
	if("ipsnInfoVo" in data){
		//清空被保人信息
    	clearbtn();
		$("#btnLast").attr("disabled",false);
    	$("#btnNext").attr("disabled",false);
		var msg=data.ipsnInfoVo;
		$("#ipsnInfoForm #idType").combo("val", msg.idType);
		$("#ipsnInfoForm #joinIpsnFlag").combo("val", msg.joinIpsnFlag);
		//回显方法
		jsonStr = JSON.stringify(msg);
	    var obj = eval("("+jsonStr+")");
	    var key,value,tagName,type,arr;
	    for(x in obj){
	        key = x;
	        value = obj[x];
	        if("birthDate"==key||"birthDate"==key){
				value = new Date(value).format("yyyy-MM-dd");
			}
	        if("idType"===key || key=="idType"){
	        	continue;
	        }
	        if("joinIpsnFlag"===key || key=="joinIpsnFlag"){
	        	continue;
	        	}
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
	                    $("#ipsnInfoForm #"+key).val(value);
	                }
	            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
	                $("#ipsnInfoForm #"+key).combo("val", value);
	            }
	        });
	    }
	   //回显投保人信息
	    var msg=data.hldrInfoVo;
		//回显方法
		jsonStr = JSON.stringify(msg);
	    var obj = eval("("+jsonStr+")");
	    var key,value,tagName,type,arr;
	    for(x in obj){
	        key = x;
	        value = obj[x];
	        if("hldrBirth"==key||"hldrBirth"==key){
				value = new Date(value).format("yyyy-MM-dd");
			}
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
	                    $("#hldrInfoForm #"+key).val(value);
	                }
	            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
	                $("#hldrInfoForm #"+key).combo("val", value);
	            }
	        });
	    }
	    
	    
	    //将数据放到后台专用全局变量中
	    com.orbps.otherFunction.contractCorrected.childPage.listImport.listImportBsInfoVo = data.bsInfoVo;
	    // 将后台过来的数据转换成页面用的全局变量
	 
	    for(var i = 0; i < data.bsInfoVo.length;i++){
	    	for(var j = 0; j < data.bsInfoVo[i].listImportSubPolVos.length;j++){
	    		var BsInfoFormList = {};
	        	BsInfoFormList.polCode = data.bsInfoVo[i].polCode;
	        	BsInfoFormList.polName = data.bsInfoVo[i].polName;
	    		BsInfoFormList.subPolCode = data.bsInfoVo[i].listImportSubPolVos[j].subPolCode;
	    		BsInfoFormList.subPolName = data.bsInfoVo[i].listImportSubPolVos[j].subPolName;
	    		if("groupCode" in data.bsInfoVo[i].listImportSubPolVos[j]){
	    			BsInfoFormList.groupCode = data.bsInfoVo[i].listImportSubPolVos[j].groupCode;
	    		}
	    		if("groupName" in data.bsInfoVo[i].listImportSubPolVos[j]){
	    			BsInfoFormList.groupName = data.bsInfoVo[i].listImportSubPolVos[j].groupName;
	    		}
	    		BsInfoFormList.amount = data.bsInfoVo[i].listImportSubPolVos[j].amount;
	    		BsInfoFormList.premium = data.bsInfoVo[i].listImportSubPolVos[j].premium;
	    		com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList.push(BsInfoFormList);
	    	}
	    }
	    // 重新加载险种表格
	    com.orbps.otherFunction.contractCorrected.childPage.listImport.reloadPublicInsuredModalTable();
	    // 刷新交费账户信息
	    var f=Object.prototype.toString.call(data.accInfoList) === '[object Array]';
	    if(f){
	    	 for (var i = 0; i < data.accInfoList.length; i++) {
	    	    	data.accInfoList[i].ipsnPayPct=data.accInfoList[i].ipsnPayPct;
	    		}
	    }else{
	    	if(null!==data.accInfoList && undefined !== data.accInfoList){
	    		data.accInfoList.ipsnPayPct=data.accInfoList.ipsnPayPct;
	    	}
	    }
	    $('#AccInfoTb').editDatagrids("bodyInit", data.accInfoList);
	    if(data.beneficiaryInfo !== undefined && data.beneficiaryInfo !== null){
	    	 for (var i = 0; i < data.beneficiaryInfo.length; i++) {
	 	    	var birthDate = data.beneficiaryInfo[i].birthDate;
	 	    	if(birthDate!==""){
	 	    		var value = new Date(birthDate).format("yyyy-MM-dd");
	 	    		data.beneficiaryInfo[i].birthDate = value;
	 	    	}else{
	 	    		data.beneficiaryInfo[i].birthDate = "";
	 	    	}
	 		}
	 	    // 重新加载受益人表格
	 	    var f=Object.prototype.toString.call(data.beneficiaryInfo) === '[object Array]';
	 	    if(f){
	 	    	  for (var i = 0; i < data.beneficiaryInfo.length; i++) {
	 	        	  if(null!==data.beneficiaryInfo[i].birthDate && undefined !==data.beneficiaryInfo[i].birthDate && ""!==data.beneficiaryInfo[i].birthDate){
	 	        		  data.beneficiaryInfo[i].birthDate=new Date(data.beneficiaryInfo[i].birthDate).format("yyyy-MM-dd");
	 	        	 	}	
	 	        	  if(null!==data.beneficiaryInfo[i].beneAmount && undefined !==data.beneficiaryInfo[i].beneAmount && ""!==data.beneficiaryInfo[i].beneAmount){
	 	        		  data.beneficiaryInfo[i].beneAmount=data.beneficiaryInfo[i].beneAmount;
	 	        	  	}
	 	        }
	 	    }else{
	 	    	  if(null!==data.beneficiaryInfo.birthDate && undefined !==data.beneficiaryInfo.birthDate && ""!==data.beneficiaryInfo.birthDate){
	 	    		  data.beneficiaryInfo.birthDate=new Date(data.beneficiaryInfo.birthDate).format("yyyy-MM-dd");
	 	    	 	}	
	 	    	  if(null!==data.beneficiaryInfo.beneAmount && undefined !==data.beneficiaryInfo.beneAmount && ""!==data.beneficiaryInfo.beneAmount){
	 	    		  data.beneficiaryInfo.beneAmount=data.beneficiaryInfo.beneAmount;
	 	    	  	}
	 	    }
	 	  
	 	    $("#beneInfoTb").editDatagrids("bodyInit",data.beneficiaryInfo);
	    }
	}else{
		if(com.orbps.otherFunction.contractCorrected.childPage.listImport.flag === "s"){
			lion.util.info("提示","当前被保险人已经是第一个人了！");
		}else if(com.orbps.otherFunction.contractCorrected.childPage.listImport.flag === "x"){
			lion.util.info("提示","当前被保险人已经是最后一人了！");
		}
	}
}
function clearbtn(){
	//基本信息
	$("#ipsnInfoForm input[type='text']").val("");
	$("#ipsnInfoForm #joinIpsnFlag").combo("val","N");
	//是否继续导入
	//$("#continueImport").combo("val","N");
	//是否在职下拉框默认显示否
	$("#ipsnInfoForm #onJobFlag").combo("val","Y");
	$("#relToIpsn").combo("val", "M");
	$("#relToIpsn").attr("readOnly",true);
	$("#mainIpsnNo").val("");
	$("#mainIpsnNo").attr("readOnly",true);
    $(".fa").removeClass("fa-warning");
    $(".fa").removeClass("fa-check");
    $(".fa").removeClass("has-success");
    $(".fa").removeClass("has-error");
	
	//是否在职下拉框默认显示否
	$("#ipsnInfoForm #healthFlag").combo("val","N");
	
	 $("#bankCode").combo("clear");
	//身份证类型
	$("#ipsnInfoForm #idType").combo("clear");
	//性别
	$("#ipsnInfoForm #ipsursex").combo("clear");
	//职业代码.
	$("#ipsnInfoForm #occupationalCodes").combo("clear");
	//与投保人关系
	$("#ipsnInfoForm #relToHldr").combo("clear");
	//医保标识
	$("#ipsnInfoForm #medicalInsurFlag").combo("clear");
	//投保人信息
	hldrInfoForm
	$("#hldrInfoForm input[type='text']").val("");
	//身份证类型
	$("#hldrInfoForm #hldrIdType").combo("clear");
	//性别
	$("#hldrInfoForm #hldrSex").combo("clear");
	//要约信息 
	com.orbps.otherFunction.contractCorrected.childPage.listImport.BsInfoFormList = [];
	com.orbps.otherFunction.contractCorrected.childPage.listImport.insuredCount=0;
	com.orbps.otherFunction.contractCorrected.childPage.listImport.reloadPublicInsuredModalTable();
	$("#bsInfoForm input[type='text']").val("");
	$("#bsInfoForm #polCode").find("option[value='']").attr("selected", true); 
	$("#bsInfoForm #subPolCode").find("option[value='']").attr("selected", true); 
	$("#bsInfoForm #groupCode").find("option[value='']").attr("selected", true); 
	//删除已录入受益人信息
	 var code_Values = document.getElementsByName("checkboxs");  
	  for (i = 0; i < code_Values.length; i++) {  
	    if (code_Values[i].type == "checkbox") {  
	    		code_Values[i].checked = true;  
	    }  
	  }  
	  $("#beneInfoTb").editDatagridsLoadById();
	  $("#AccInfoTb").editDatagridsLoadById();
}
//判断所有为空项公共代码
function isEmpty(id,name){ 
	if(null ===id || undefined === id || "" === id){
		if("" !== name){
			lion.util.info("警告",name+"不能为空");
		}
		return true;
	}else{
		 false;
	}
}
