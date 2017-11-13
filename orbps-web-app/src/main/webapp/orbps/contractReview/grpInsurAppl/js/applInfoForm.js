// 新建contractReview命名空间
com.orbps.contractReview = {};
// 新建contractReview.grpInsurAppl命名空间
com.orbps.contractReview.grpInsurAppl = {};
// treegrid
com.orbps.contractReview.grpInsurAppl.tableId = $('#treegridId');
// 编辑或添加对话框
com.orbps.contractReview.grpInsurAppl.addDialog = $('#btnModel');
com.orbps.contractReview.grpInsurAppl.benesList = [];
com.orbps.contractReview.grpInsurAppl.applInfoForm = $('#applInfoForm');
//com.orbps.contractReview.grpInsurAppl.busiPrdCode = null;
com.orbps.contractReview.grpInsurAppl.taskId;
// 险种信息全局list
com.orbps.contractReview.grpInsurAppl.queryBusiprodList = [];
//责任信息list
com.orbps.contractReview.grpInsurAppl.busiPrdVos = [];
com.orbps.contractReview.grpInsurAppl.agreementNoFlag;
com.orbps.contractReview.grpInsurAppl.dataList;
com.orbps.contractReview.grpInsurAppl.healthPublicAmountArray = [];
com.orbps.contractReview.grpInsurAppl.constructionInfoArray = [];
com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos = [];
//基金险标示
com.orbps.contractReview.grpInsurAppl.FundInsurFlag;
//查询成功回调函数
com.orbps.contractReview.grpInsurAppl.successQueryDetail = function(data,args) {
	com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos = data.applInfoVo.grpSalesListFormVos;
	com.orbps.contractReview.grpInsurAppl.dataList = data;
    com.orbps.contractReview.grpInsurAppl.taskId = data.taskInfo.taskId;
        var jsonStrs = JSON.stringify(data);
        var objs = eval("(" + jsonStrs + ")");
        var form;
        for (y in objs) {
            // form = y;
            // var keys = form;
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            var key, value, tagName, type, arr;
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                if("applDate"==key||"inForceDate"==key||"enstPremDeadline"==key||"constructionDur"==key||"until"==key || "preMioDate" ===key){
					value = new Date(value).format("yyyy-MM-dd");
				}
                $("[name='" + key + "'],[name='" + key + "[]']").each(
                        function() {
                            tagName = $(this)[0].tagName;
                            type = $(this).attr('type');
                            if (tagName == 'INPUT') {
                                if (type == 'radio') {
                                    $(this).attr('checked',
                                            $(this).val() == value);
                                } else if (type == 'checkbox') {
                                    arr = value.split(',');
                                    for (var i = 0; i < arr.length; i++) {
                                        if ($(this).val() == arr[i]) {
                                            $(this).attr('checked', true);
                                            break;
                                        }
                                    }
                                } else {
                                    $("#" + key).val(value);
                                }
                            } else if (tagName == 'SELECT'
                                    ) {
                            	$("#"+ key).comboInitLoadById(value);
                            }else if( tagName == 'TEXTAREA'){
                            	 //$("#" + key).combo("val", value);
                            	$("#" + key).val(value);
                            }
                        });
            }
        }
      //特别约定单独赋值
    	$("#specialPro").val(data.proposalInfoVo.specialPro);
        $("*").comboInitLoad();
    setTimeout( function(){
        // 将省市县置为可以回显的状态
    	$(".dist").attr("disabled",false);
        $(".city").attr("disabled",false);
        $(".prov").attr("disabled",false);
        $("#applBaseInfoForm #appAddrProv").combo("val",data.applBaseInfoVo.appAddrProv);
        $("#applBaseInfoForm #appAddrCity").combo("val",data.applBaseInfoVo.appAddrCity);
        $("#applBaseInfoForm #appAddrCountry").combo("val",data.applBaseInfoVo.appAddrCountry);
    },1000);

    //判断争议处理方式置灰仲裁机构名称
    var disputePorcWay = data.applBaseInfoVo.disputePorcWay;
    
    if($("#disputePorcWay").val()==="1"){
    	$("#parbOrgName").val("");
        $("#parbOrgName").attr("disabled",true);
    }else{
        $("#parbOrgName").attr("disabled",false);
    }
    //结算日期
    if(lion.util.isNotEmpty(data.payInfoVo.settlementDate)){
    	com.orbps.contractReview.grpInsurAppl.settleList = data.payInfoVo.settlementDate;
    }
    // 赋值被保人分组信息为全局变量
    com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
    //险种信息，由于现在复核不能对数据进行操作，所以现在直接付给一个全局变量，复核提交时直接用
    com.orbps.contractReview.grpInsurAppl.busiPrdVos = data.busiPrdVos;
    // 取到责任信息list，用于被保人分组中的险种下拉框的责任回显
	for (var i = 0; i < data.busiPrdVos.length; i++) {
		// 当责任不为空时放入全局变量
		if (lion.util.isNotEmpty(data.busiPrdVos[i].responseVos)) {
			for (var j = 0; j < data.busiPrdVos[i].responseVos.length; j++) {
				var array_element = data.busiPrdVos[i].responseVos[j];
				com.orbps.common.list.push(array_element);
			}
		}
	}
    //收付费分组信息
    com.common.chargePayList = data.chargePayGroupModalVos;
   
    // 险种表格回显
    $('#fbp-editDataGrid').editDatagrids("bodyInit", data.busiPrdVos);
    com.orbps.contractReview.grpInsurAppl.queryBusiprodList = data.busiPrdVos;

    //计算施工天数
    var constructionDur = $("#specialInsurAddInfoForm #constructionDur").val();
    var until = $("#specialInsurAddInfoForm #until").val();
    if(constructionDur!=="" && until!==""){
        com.orbps.contractReview.grpInsurAppl.DateDiff(constructionDur,until);
    }
    //根据结算方式更改页面样式
    if(data.payInfoVo.stlType==="N"){
        $("#payInfoForm #stlLimit").attr("disabled",true);
        $("#payInfoForm #settlementRatio").attr("disabled",true);
        $("#payInfoForm #btnStlDate").attr("disabled",true);
    }
    else if(data.payInfoVo.stlType==="X"){
        $("#stlLimit").attr("disabled",false);
        $("#settlementRatio").attr("disabled",true);
        $("#btnStlDate").attr("disabled",false);
    }else if(data.payInfoVo.stlType==="D"){
        $("#stlLimit").attr("disabled",true);
        $("#settlementRatio").attr("disabled",true);
        $("#btnStlDate").attr("disabled",false);
    }else if(data.payInfoVo.stlType==="L"){
        $("#stlLimit").attr("disabled",true);
        $("#settlementRatio").attr("disabled",false);
        $("#btnStlDate").attr("disabled",true);
    }
    
    // 取到组织树节点信息
    if(data.organizaHierarModalVos.length>0){
    	com.orbps.common.zTrees = com.orbps.common.OHMFtn(data.organizaHierarModalVos);
    }else{
    	com.orbps.common.zTrees = [ {
    		id : 1,
			pId : "#",
			name : "默认节点(请重命名)",
			open : false,
			nocheck : false,
			checked : true,
			noRemoveBtn : true,
			noRenameBtn : true,
			noEditBtn : true,
			chkDisabled : false,
			isEdit : false,
			children : []
    	}];
    }
    com.orbps.common.oranLevelList = data.organizaHierarModalVos;
    console.log(data);
    console.log(com.orbps.common.zNodes);
    //如果新单状态是提交，置灰跳转按钮。  团单基本信息复核
    if("04" !== data.approvalState){
		$("#btnlocation").attr("disabled",true);
    }
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
		for(var j = 0; j < com.orbps.contractReview.grpInsurAppl.healthPublicAmountArray.length; j++){
			var array_element = com.orbps.contractReview.grpInsurAppl.healthPublicAmountArray[j];
			//如果险种中有健康险的险种
			if(a === array_element){
				$("#specialInsurAddInfoForm").show();
				$("#tab1").show();
				hide2 = 1;
				break;
			}
			
		}
		//建筑险的对比
		for(var k = 0; k < com.orbps.contractReview.grpInsurAppl.constructionInfoArray.length; k++){
			var array_elements = com.orbps.contractReview.grpInsurAppl.constructionInfoArray[k];
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
				com.orbps.contractReview.grpInsurAppl.FundInsurFlag = "Y";
			}else{
				$("#tab2").hide();
				com.orbps.contractReview.grpInsurAppl.FundInsurFlag = "N";
			}
			com.orbps.contractReview.grpInsurAppl.checkTabshowOrHidden();
		})
    setTimeout(function(){
    	// 增加共保协议号是否必录的标识
    	if("agreementNo" in data.applInfoVo){
    		if ("" === data.applInfoVo.agreementNo) {
    			com.orbps.contractReview.grpInsurAppl.agreementNoFlag = "N";
    		} else {
    			com.orbps.contractReview.grpInsurAppl.agreementNoFlag = "Y";
    		}
    	}else{
    		com.orbps.contractReview.grpInsurAppl.agreementNoFlag = "N";
    	}
    	$("#printInfoForm #giftFlag").attr("disabled", true);
    },800)
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

$(function() {
	
	//投保单号置灰
	$("#applInfoForm #applNo").attr("readonly",true);
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
    //页面加载时先隐藏网点信息
    $("#worksiteHideDiv").hide();
    
    // 文件上传下载插件初始化
    $("#file").fileinput({
        'allowedFileExtensions' : [ 'jpg', 'png', 'gif', 'xlsx' ],
        'showUpload' : false,
        'showPreview' : false,
        'showCaption' : true,
        'browseClass' : 'btn btn-success',
    });

    // 初始化edittable组件
    $("#fbp-editDataGrid").editDatagridsLoadById();

    // combo组件初始化
   // $("*").comboInitLoad();
    
    lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
			function (data, arg) {
			com.orbps.contractReview.grpInsurAppl.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
			com.orbps.contractReview.grpInsurAppl.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
			},null, null);
    //服务器测试
    var dataCipher=com.ipbps.getDataCipher();
    var url= '/orbps/web/authSupport/check?serviceName=cntrEntryGrpInsurApplService&dataCipher='+dataCipher;
    lion.util.postjson(url,null,com.orbps.contractReview.grpInsurAppl.successQueryDetail,null,null);
//    taskInfo = {};
//    taskInfo.bizNo = "2017031316390002"
//    lion.util.postjson('/orbps/web/orbps/contractReview/grp/query',taskInfo,com.orbps.contractReview.grpInsurAppl.successQueryDetail,null,null);

});

//按回车相当于tab功能(键盘按键触发事件)
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

//计算天数差的函数 
com.orbps.contractReview.grpInsurAppl.DateDiff = function(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
    var Date1 = new Date(sDate1);
    var Date2 = new Date(sDate2);
    var a = (Date2.getTime() - Date1.getTime())/(24 * 60 * 60 * 1000) + 1;
    $("#specialInsurAddInfoForm #totalDays").val(a);
} 

//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.contractReview.grpInsurAppl.addDialog.empty();
	com.orbps.contractReview.grpInsurAppl.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.contractReview.grpInsurAppl.grpSalesListFormVos);
	});
});