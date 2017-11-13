$(function(){
	var grpApplInfoVo = {};
	grpApplInfoVo.salesBranchNo = com.orbps.common.salesBranchNo;
	grpApplInfoVo.salesChannel = com.orbps.common.salesChannel;
	grpApplInfoVo.saleCode = com.orbps.common.saleCode;
	grpApplInfoVo.worksiteNo = com.orbps.common.worksiteNo;
	$("#imageCollectionQueryInfoForm #tbdh").val(com.orbps.common.applNo);
	$("#imageCollectionQueryInfoForm #batchid").val(com.orbps.common.salesBranchNo);
	// 查询销售员信息
	lion.util
	.postjson(
			'/orbps/web/orbps/contractEntry/search/querySalesInfo',
			grpApplInfoVo,
			function(data, arg) {
				$("#imageCollectionQueryInfoForm #mngdeptid").val(data.mgrBranchNo);
			},
			null, null);
	// 影像采集方法
	com.orbps.common.acquisition = function(mode) {
		var btnCollection = $("#imageCollectionQueryInfoForm").serializeObject();
		var inputparams = {};
		inputparams.mode = mode;
		var indexinfos = new Array();
		var indexinfo = {};
		var field = new Array();

		if(lion.util.isNotEmpty(btnCollection)){
			if(btnCollection.appid!==""){
				var fieldVo1 = {};
				fieldVo1.name = "appid";
				fieldVo1.value = btnCollection.appid;
				field.push(fieldVo1);
			}else{
				lion.util.info("提示","请选择类型")
				return false;
			}
			
			if(btnCollection.mngdeptid!==""){
				var fieldVo5 = {};
				fieldVo5.name = "mngdeptid";
				fieldVo5.value = btnCollection.mngdeptid;
				field.push(fieldVo5);
			}else{
				lion.util.info("提示","请填写管理机构代码")
				return false;
			}

			var queryForm = $("#queryForm").val();
			if(queryForm==="applNo"){
				if(btnCollection.tbdh!==""){
					var fieldVo2 = {};
					fieldVo2.name = "TBDH";
//					fieldVo2.value = btnCollection.applNo;
					//临时修改影像扫描，索引号为空的问题
					fieldVo2.value = com.orbps.common.applNo;
				}else{
					lion.util.info("提示","请填写投保单号")
					return false;
				}
				field.push(fieldVo2);
			}else if (queryForm==="quotaEaNo"){
				if(btnCollection.tbdh!==""){
					var fieldVo2 = {};
					fieldVo2.name = "TBDH";
					fieldVo2.value = com.orbps.common.quotaEaNo;
				}else{
					lion.util.info("提示","请填写报价审批号")
					return false;
				}
				field.push(fieldVo2);
			}
			
			if(btnCollection.batchid!==""){
				var fieldVo3 = {};
				fieldVo3.name = "batchid";
				fieldVo3.value = btnCollection.batchid;
				field.push(fieldVo3);
			}else{
				lion.util.info("提示","请填写机构代码")
				return false;
			}
			
				var fieldVo4 = {};
				fieldVo4.name = "sysid";
				fieldVo4.value = "ORBPS";
				field.push(fieldVo4);
		}

		indexinfo.field = field;
		indexinfos.push(indexinfo);
		inputparams.indexinfos = indexinfos;
		console.log(JSON.stringify(inputparams));
		lion.util.postjson("/orbps/web/cms/appurlrs/getUrl",inputparams,function(
				url, args) {
			// 跳转页面
			if (lion.util.isEmpty(url)) {
				lion.util.info("调用影像采集服务错误！");
				return;
			}
			window.open(url);
		});
	}
});

//点击采集
$("#btnCollection").click(function(){
	com.orbps.common.acquisition(10);
});

// 点击本次影像展现
$("#btnImageShow").click(function(){
	com.orbps.common.acquisition(20);
});

// 点击影像批次流水号调阅
$("#btnImageSerialNoSum").click(function(){
	com.orbps.common.acquisition(21);
});

// 点击查询方式
$("#queryForm").change(function(){
	var queryForm = $("#queryForm").val();
	if("applNo"===queryForm){
		$("#quotaEaNoType").hide();
		$("#applNoType").show();
		$("#imageCollectionQueryInfoForm #tbdh").val(com.orbps.common.applNo);
	}else if("quotaEaNo"===queryForm){
		$("#applNoType").hide();
		$("#quotaEaNoType").show();
		$("#imageCollectionQueryInfoForm #tbdh").val(com.orbps.common.quotaEaNo);
	}
});