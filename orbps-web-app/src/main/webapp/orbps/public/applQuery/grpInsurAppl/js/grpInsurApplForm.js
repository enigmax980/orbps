com.orbps.otherFunction.contractQuery.addDialog = $('#btnModel');
com.orbps.otherFunction.contractQuery.settlementDateList = [];
com.orbps.common.oranLevelList = new Array();
com.orbps.common.zTrees = [];
com.orbps.common.proposalGroupList = new Array();
com.orbps.common.treeNodeNameList=[];
com.orbps.common.rootIsEdit = false;
com.orbps.common.quotaEaNo;
com.orbps.common.applNo;
com.orbps.common.salesBranchNo;
com.orbps.common.salesChannel;
com.orbps.common.worksiteNo;
com.orbps.common.saleCode;
com.orbps.otherFunction.contractQuery.grpSalesListFormVos = [];
/*基金险标示**/
com.orbps.otherFunction.contractQuery.FundInsurFlag;

$(function(){
	// combo组件初始化
   // $("*").comboInitLoad();
	
	//省市县三级联动
	$("#citySelect").citySelect({
		url:"/resources/global/js/cityselect/js/city.min.json",
		required:false
	});
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
	// 设置总天数只读
    $("#totalDays").attr("readOnly",true);
    
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
    						pId : "#",
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
    var data = com.orbps.otherFunction.contractQuery.data;
    //特别约定
    $("#specialPro").val(data.proposalInfoVo.specialPro);
	var key,value,tagName,type,arr ,keys,form;
	 for (y in data) {
     	form = y;
     	var a = form.replace("Vos","Form");
     	var formId = a.replace("Vo","Form");
         var values = data[y];
         for (x in values) {
             key = x;
             value = values[key];
             if("applDate"===key||"inForceDate"===key||"enstPremDeadline"===key
            		 ||"constructionDur"===key||"until"===key ||"effectDate"===key || "preMioDate" === key){
            	
            	
 				value = new Date(value).format("yyyy-MM-dd");
 			}
             $("[name='" + key + "'],[name='" + key + "[]']").each(
                     function() {
                         tagName = $(this)[0].tagName;
                         type = $(this).attr('type');
                         if (tagName === 'INPUT') {
                             if (type === 'radio') {
                                 $(this).attr('checked',
                                         $(this).val() == value);
                             } else if (type === 'checkbox') {
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
         				}else if(tagName==='SELECT' ){
         					  $("#"+ key).comboInitLoadById(value);
                         }else if(tagName==='TEXTAREA'){
                        	 $("#"+formId +" #"+key).combo("val", value);
                         }

                     });
         }
     }

	setTimeout(function(){
		com.orbps.otherFunction.contractQuery.grpSalesListFormVos = data.applInfoVo.grpSalesListFormVos;
	    // 将省市县置为可以回显的状态
	    $(".dist").attr("disabled",false);
	    $(".city").attr("disabled",false);
	    $(".prov").attr("disabled",false);
	    $("#applBaseInfoForm #appAddrProv").combo("val",data.applBaseInfoVo.appAddrProv);
        $("#applBaseInfoForm #appAddrCity").combo("val",data.applBaseInfoVo.appAddrCity);
        $("#applBaseInfoForm #appAddrCountry").combo("val",data.applBaseInfoVo.appAddrCountry);
        //跟据险种信息设置页面样式，隐藏特殊险种模块
        if(lion.util.isNotEmpty(data.busiPrdVos[0].busiPrdCodeName)){
	        var flag = data.busiPrdVos[0].busiPrdCodeName.indexOf("建筑");
	        if(flag > 0){
	        	$("#specialInsurAddInfoForm").show();
	        }else{
	        	$("#specialInsurAddInfoForm").hide();
	        }
        }
        //计算施工总天数
        if((data.specialInsurAddInfoVo.constructionDur !== null && data.specialInsurAddInfoVo.constructionDur !=undefined)
        		&& (data.specialInsurAddInfoVo.until !== undefined && data.specialInsurAddInfoVo.until !==null)){
        	//alert(JSON.stringify(data));
        	var Date1 = new Date(data.specialInsurAddInfoVo.constructionDur);
            var Date2 = new Date(data.specialInsurAddInfoVo.until);
            var a = (Date2.getTime() - Date1.getTime())/(24 * 60 * 60 * 1000);
            if(a !== ""){
                $("#totalDays").val(a);
            }
        }
        
	}, 500);
	//结算日期
    if(lion.util.isNotEmpty(data.payInfoVo.settlementDate)){
    	com.orbps.otherFunction.contractQuery.settlementDateList = data.payInfoVo.settlementDate;
    }
	
    // 赋值被保人分组信息为全局变量
    com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
    // 赋值收付费分组信息为全局变量
    com.common.chargePayList = data.chargePayGroupModalVos;
	//加载要约信息表格
	$('#fbp-editDataGrid').find("tbody").empty();
	if (data.busiPrdVos !== null && data.busiPrdVos.length > 0) {
//		alert(JSON.stringify(data.busiPrdVos));
		for(var i = 0; i < data.busiPrdVos.length; i++){
			var amount="";
			if(lion.util.isNotEmpty(data.busiPrdVos[i].amount)){
				amount=data.busiPrdVos[i].amount;
			}
			var healthInsurFlag="";
			if(lion.util.isNotEmpty(data.busiPrdVos[i].healthInsurFlag)){
				speciBusinessLogo=com.orbps.publicSearch.speciBusinessLogo(data.busiPrdVos[i].healthInsurFlag);
			}
			var insurDurUnit="";
			if(lion.util.isNotEmpty(data.busiPrdVos[i].insurDurUnit)){
				insurDurUnit=com.orbps.publicSearch.insurDurUnit(data.busiPrdVos[i].insurDurUnit);
			}
			var premium="";
			if(lion.util.isNotEmpty(data.busiPrdVos[i].premium)){
				premium=data.busiPrdVos[i].premium;
			}
			var insuredNum="";
			if(lion.util.isNotEmpty(data.busiPrdVos[i].insuredNum)){
				insuredNum=data.busiPrdVos[i].insuredNum;
			}
			var insurDur="";
			if(lion.util.isNotEmpty(data.busiPrdVos[i].insurDur)){
				insurDur=data.busiPrdVos[i].insurDur;
			}
			$('#fbp-editDataGrid').find("tbody")
			.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.contractQuery.publicRadio();' id='insuredRaded"
					+ i
					+ "' name='insuredRaded' value='"
					+ i
					+ "'></td><td >"
					+ data.busiPrdVos[i].busiPrdCode
					+ "</td><td >"
					+ data.busiPrdVos[i].busiPrdCodeName
					+ "</td><td >"
					+ amount
					+ "</td><td >"
					+ premium
					+ "</td><td >"
					+ insuredNum
					+ "</td><td >"
					+ insurDur
					+ "</td><td >"
					+ insurDurUnit
					+ "</td><td >"
					+ healthInsurFlag
					+ "</td></tr>");
		}
	} else {
		$('#fbp-editDataGrid').find("tbody").append("<tr><td colspan='9' align='center'>无记录</td></tr>");
	}
	com.orbps.common.oranLevelList = com.orbps.otherFunction.contractQuery.data.organizaHierarModalVos;
		setTimeout(function(){
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
				for(var j = 0; j < com.orbps.otherFunction.contractQuery.healthPublicAmountArray.length; j++){
					var array_element = com.orbps.otherFunction.contractQuery.healthPublicAmountArray[j];
					//如果险种中有健康险的险种
					if(a === array_element){
						$("#specialInsurAddInfoForm").show();
						$("#tab1").show();
						hide2 = 1;
						break;
					}
					
				}
				//建筑险的对比
				for(var k = 0; k < com.orbps.otherFunction.contractQuery.constructionInfoArray.length; k++){
					var array_elements = com.orbps.otherFunction.contractQuery.constructionInfoArray[k];
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
						com.orbps.otherFunction.contractQuery.FundInsurFlag = "Y";
					}else{
						$("#tab2").hide();
						com.orbps.otherFunction.contractQuery.FundInsurFlag = "N";
					}
					com.orbps.otherFunction.contractQuery.checkTabshowOrHidden();
			})
		},1000);
});
//点击查看责任按钮弹出责任列表
com.orbps.otherFunction.contractQuery.publicRadio = function(vform) {
//	$("#proposalInfoForm input[type='text']").val("");
//	$("#proposalInfoForm select2").combo("refresh");
	var radioVal;
	//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
	var temp = document.getElementsByName("insuredRaded");
	for(var i=0;i<temp.length;i++){
	     if(temp[i].checked){
	    	 radioVal = temp[i].value;
	     }
	}
	com.orbps.otherFunction.contractQuery.insuredType = radioVal;
	insuredVo= com.orbps.otherFunction.contractQuery.grpBusiPrdList[radioVal];
	com.orbps.otherFunction.contractQuery.responseList = insuredVo;
	setTimeout(function(){
		// 回显
		com.orbps.otherFunction.contractQuery.showInsured(insuredVo);
	},100);
};

//回显方法
com.orbps.otherFunction.contractQuery.showInsured = function(msg){
	jsonStr = JSON.stringify(msg);
    var obj = eval("("+jsonStr+")");
    var key,value,tagName,type,arr;
    for(x in obj){
        key = x;
        value = obj[x];
        if("applDate"===key||"inForceDate"===key||"enstPremDeadline"===key||"constructionDur"===key||"until"==key){
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
                    $("#proposalInfoForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
                $("#proposalInfoForm #"+key).combo("val", value);
            }
        });
    }
}

//根据是否指定生效日修改页面样式
$("#forceType").change(function(){
    if($("#forceType").val()==="0"){
        $("#inForceDate").val("");
        $("#inForceDate").attr("disabled",true);
        $("#inForceDateBtn").attr("disabled",true);
    }else if($("#forceType").val()==="1"){
        $("#inForceDate").attr("disabled",false);
        $("#inForceDateBtn").attr("disabled",false);
    }
});

//根据保费来源更改页面样式
$("#premFrom").change(function() {
	//如果是现金都是灰的
	if($("#moneyinType").val()==="C"){
		//将账号信息置为不可用。
        $("#bankCode").attr("disabled",true);
        $("#bankCode").val("");
        $("#bankName").attr("disabled",true);
        $("#bankName").val("");
        $("#bankAccNo").attr("disabled",true);
        $("#bankAccNo").val("");
	}else{
		if($("#premFrom").val()==="2"){
            //如果选择个人账户，将账号信息置为不可用。
            $("#bankCode").attr("disabled",true);
            $("#bankCode").val("");
            $("#bankName").attr("disabled",true);
            $("#bankName").val("");
            $("#bankAccNo").attr("disabled",true);
            $("#bankAccNo").val("");
        }else{
            $("#bankCode").attr("disabled",false);
            $("#bankName").attr("disabled",false);
            $("#bankAccNo").attr("disabled",false);
        }
	}
    
});

//根据结算方式更改页面样式
$("#payInfoForm #stlType").change(function() {
    if($("#stlType").val()==="N"){
        $("#payInfoForm #stlLimit").attr("disabled",true);
        $("#payInfoForm #settlementRatio").attr("disabled",true);
        $("#payInfoForm #btnStlDate").attr("disabled",true);
    }
    else if($("#stlType").val()==="X"){
        $("#stlLimit").attr("disabled",false);
        $("#settlementRatio").attr("disabled",true);
        $("#btnStlDate").attr("disabled",false);
    }else if($("#stlType").val()==="D"){
        $("#stlLimit").attr("disabled",true);
        $("#settlementRatio").attr("disabled",true);
        $("#btnStlDate").attr("disabled",false);
    }else if($("#stlType").val()==="L"){
        $("#stlLimit").attr("disabled",true);
        $("#settlementRatio").attr("disabled",false);
        $("#btnStlDate").attr("disabled",true);
    }
});

//如果交费形式是现金，置灰交费开户行，开户名称，银行账号输入框
$("#moneyinType").change(function(){
	if($("#premFrom").val()==="2"){
		$("#bankCode").attr("disabled",true);
        $("#bankCode").val("");
        $("#bankName").attr("disabled",true);
        $("#bankName").val("");
        $("#bankAccNo").attr("disabled",true);
        $("#bankAccNo").val("");
    }else {
    	if($("#moneyinType").val()==="C"){
            $("#bankCode").attr("disabled",true);
            $("#bankCode").val("");
            $("#bankName").attr("disabled",true);
            $("#bankName").val("");
            $("#bankAccNo").attr("disabled",true);
            $("#bankAccNo").val("");
        }
        else{
        	 $("#bankCode").attr("disabled",false);
             $("#bankName").attr("disabled",false);
             $("#bankAccNo").attr("disabled",false);
        }
    }
	
});

//争议处理方式
$("#applBaseInfoForm #disputePorcWay").change(function() {
	var disputePorcWay = $("#disputePorcWay").val();
	if(disputePorcWay==="1"){
		$("#applBaseInfoForm #arbOrgName").attr("disabled", true);
	}else{
		$("#applBaseInfoForm #arbOrgName").attr("disabled", false);
	}
});


//定期结算日期
$("#btnStlDate").click(function() {
	$("#btnModel").empty();
	$("#btnModel").load(
		"/orbps/orbps/public/applQuery/grpInsurAppl/html/settlementDateModal.html",function() {
			$(this).modal('toggle');
        });
	setTimeout(function() {
		// 回显
		com.orbps.otherFunction.contractQuery.reloadsettlementInsuredModalTable();
	}, 100);
});
    
//点击查看责任按钮弹出责任列表
$("#btnSelect").click(function() {
	var radioVal;
	//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
	var temp = document.getElementsByName("insuredRaded");
	for(var i=0;i<temp.length;i++){
	     if(temp[i].checked){
	    	 radioVal = temp[i].value;
	     }
	}
	com.orbps.otherFunction.contractQuery.insuredType = radioVal;
	var selectData = com.orbps.otherFunction.contractQuery.grpBusiPrdList[radioVal];
	// 判断选择的是否是一个主险
	if ((null == selectData) || (selectData.length == 0) || (selectData.length > 1)) {
		lion.util.info("警告", "请选择一个主险信息");
		return false;
	}
	$("#btnModel").empty();
	$("#btnModel").load(
			"/orbps/orbps/public/applQuery/grpInsurAppl/html/responseForm.html",function() {
		$(this).modal('toggle');
	});
	setTimeout(function() {
		// 回显
		com.orbps.otherFunction.contractQuery.reloadResponseModalTable();
	}, 100);
});
 // 重新加载表格
com.orbps.otherFunction.contractQuery.reloadsettlementInsuredModalTable = function () {
	$('#settlementDataGrid').find("tbody").empty();
    if (com.orbps.otherFunction.contractQuery.settlementDateList != null && com.orbps.otherFunction.contractQuery.settlementDateList.length > 0) {
        for (var i = 0; i < com.orbps.otherFunction.contractQuery.settlementDateList.length; i++) {
        	var value = new Date(com.orbps.otherFunction.contractQuery.settlementDateList[i]).format("yyyy-MM-dd");
            $('#settlementDataGrid').find("tbody")
                    .append("<tr role='row' class='odd' id='settlementDataGridtr0' data-type='add' data-updating='false'><td ><input type='radio' onclick='com.orbps.otherFunction.contractQuery.dateRadio();' id='insuredRad"
                            + i
                            + "' name='insuredRad' value='"
                            + i
                            + "'></td><td >"
                            + value
                            + "</td><td ><a href='javascript:void(0);' onclick='com.orbps.otherFunction.contractQuery.deleteSettleDate("+i+");' for='insuredDel' id='insuredDel" 
							+ i + "'>删除</a></td></tr>"
                            );
        }
    } else {
    	$('#settlementDataGrid').find("tbody").append("<tr><td colspan='3' align='center'>无记录</td></tr>");
    }
}
//日期回显long转String
Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
//被保人分组
$("#btnGoup").click(
        function() {
        	com.orbps.otherFunction.contractQuery.addDialog.empty();
        	com.orbps.otherFunction.contractQuery.addDialog.load(
                    "/orbps/orbps/public/searchModal/html/insuredGroupModal.html",
                    function() {
                        $(this).modal('toggle');
                        $(this).comboInitLoad();
                        // 刷新table
                    });
            setTimeout(function() {
                // 回显
            	com.orbps.common.reloadProposalGroupModalTable();
            }, 400);
        });
//组织层次架构
$("#submit #btnOranLevel")
      .click(
              function() {
            	  	com.orbps.common.zTrees =  com.orbps.common.OHMFtn(com.orbps.otherFunction.contractQuery.data.organizaHierarModalVos)
              		com.orbps.otherFunction.contractQuery.addDialog.empty();
              		com.orbps.otherFunction.contractQuery.addDialog
                          .load(
                                  "/orbps/orbps/public/modal/html/organizaHierarModal.html",
                                  function() {
                            	    var companyName = $("#applBaseInfoForm #companyName").val();
                            	    $(this).modal('toggle');
                  					// combo组件初始化
                  					$(this).comboInitLoad();
                  					com.orbps.common.grpInsurApplVo.applInfoVo = $("#applInfoForm").serializeObject();
                  					com.orbps.common.grpInsurApplVo.applBaseInfoVo = $("#applBaseInfoForm").serializeObject();
                  					console.log("点击modal的noes"+JSON.stringify(com.orbps.common.zTrees));
                  					if(com.orbps.common.zTrees.length === 1){
                  						com.orbps.common.zTrees[0].name = companyName;
                  					}
                  					// zTree初始化
                  					var zTree = $.fn.zTree.init($("#treeDemo"), setting, com.orbps.common.zTrees);
                  					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
                  					var nodeSel = treeObj.getNodeByParam("id", 1);
                  					treeObj.selectNode(nodeSel);
                  					var node = treeObj.getNodes();
                  					var nodes = treeObj.transformToArray(node);
                  					for (var i = 0; i < nodes.length; i++) {
                  						var array_element = nodes[i].name;
              							com.orbps.common.treeNodeNameList.push(array_element);
                  					}
                                  });
});

//导出被保人清单
$("#btnExport").click(function(){
	window.location.href="/orbps/web/orbps/contractReview/offlineList/download/"+com.orbps.otherFunction.contractQuery.data.applInfoVo.applNo;
});

//影像信息查询
$("#imageQuery").click(function() {
	var salesBranchNo ="";
	if(com.orbps.otherFunction.contractQuery.grpSalesListFormVos.length===1){
		salesBranchNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].salesBranchNo;
		salesChannel = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].salesChannel;
    	worksiteNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].worksiteNo;
    	saleCode = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].saleCode;
	}else{
		for (var i = 0; i < com.orbps.otherFunction.contractQuery.grpSalesListFormVos.length; i++) {
			var jointFieldWorkFlag = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].jointFieldWorkFlag;
			if(jointFieldWorkFlag==="Y"){
				salesBranchNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].salesBranchNo;
				salesChannel = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].salesChannel;
		    	worksiteNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].worksiteNo;
		    	saleCode = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].saleCode;
				break;
			}
		}
	}
	com.orbps.common.applNo = $("#applInfoForm #applNo").val();
	com.orbps.common.quotaEaNo = $("#applInfoForm #quotaEaNo").val();
	if (salesBranchNo !== "") {
		com.orbps.common.salesBranchNo = salesBranchNo;
	}
	com.orbps.common.salesChannel = salesChannel;
	com.orbps.common.worksiteNo = worksiteNo;
	com.orbps.common.saleCode = saleCode;
	com.orbps.otherFunction.contractQuery.addDialog.empty();
	com.orbps.otherFunction.contractQuery.addDialog.load(
		"/orbps/orbps/public/modal/html/imageCollection.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
	});
});

//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.otherFunction.contractQuery.addDialog.empty();
	com.orbps.otherFunction.contractQuery.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.otherFunction.contractQuery.grpSalesListFormVos);
	});
});

//被保人分组
$("#btnChargePay").click(
        function() {
        	com.orbps.otherFunction.contractQuery.addDialog.empty();
        	com.orbps.otherFunction.contractQuery.addDialog.load(
                    "/orbps/orbps/public/searchModal/html/chargePayGroupModal.html",
                    function() {
                        $(this).modal('toggle');
                        $(this).comboInitLoad();
                        // 刷新table
                    });
            setTimeout(function() {
                // 回显
            	com.common.reloadPublicChargePayModalTable();
            }, 400);
        });
//公共保额使用范围change事件
$("#specialInsurAddInfoForm #comInsurAmntUse").change(function(){
	var comInsurAmntUse = $("#specialInsurAddInfoForm #comInsurAmntUse").val();
	if(comInsurAmntUse==="0"){
		$("#specialInsurAddInfoForm #comInsurAmntType").combo("clear");
		$("#specialInsurAddInfoForm #comInsurAmntType").attr("disabled", true);
		$("#specialInsurAddInfoForm #commPremium").val("");
		$("#specialInsurAddInfoForm #fixedComAmnt").val("");
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").val("");
		$("#specialInsurAddInfoForm #ipsnFloatPct").val("");
		$("#specialInsurAddInfoForm #commPremium").attr("disabled",true);
		$("#specialInsurAddInfoForm #fixedComAmnt").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatPct").attr("disabled",true);
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").hide();
		$("#specialInsurAddInfoForm #commPremiumIs").hide();
		$("#specialInsurAddInfoForm #fixedComAmntIs").hide();
		//显示所有隐藏的div块。
		$("#specialInsurAddInfoForm #commPremiumtype").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").show();
	}else{
		$("#specialInsurAddInfoForm #comInsurAmntType").attr("disabled", false);
		$("#specialInsurAddInfoForm #commPremium").attr("disabled",false);
		$("#specialInsurAddInfoForm #fixedComAmnt").attr("disabled",false);
		$("#specialInsurAddInfoForm #ipsnFloatAmnt").attr("disabled",false);
		$("#specialInsurAddInfoForm #ipsnFloatPct").attr("disabled",false);
	}
});

// 公共保额类型change事件
$("#specialInsurAddInfoForm #comInsurAmntType").change(function(){
	var comInsurAmntType = $("#specialInsurAddInfoForm #comInsurAmntType").val();
	if(comInsurAmntType==="0"){
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").show();
		$("#specialInsurAddInfoForm #commPremiumIs").hide();
		$("#specialInsurAddInfoForm #fixedComAmntIs").hide();
		//隐藏显示div
		$("#specialInsurAddInfoForm #commPremiumtype").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").show();
	}else if(comInsurAmntType==="1"){
		$("#specialInsurAddInfoForm #commPremiumIs").show();
		$("#specialInsurAddInfoForm #fixedComAmntIs").show();
		$("#specialInsurAddInfoForm #ipsnFloatPctIs").hide();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIs").hide();
		//隐藏显示div
		$("#specialInsurAddInfoForm #commPremiumtype").show();
		$("#specialInsurAddInfoForm #ipsnFloatAmntIsdiv").hide();
	}
});

/***
 * 查询tab中的基金险，健康险，建工险的隐藏或者展示
 */
com.orbps.otherFunction.contractQuery.checkTabshowOrHidden = function (){
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