// 新建contractEntry命名空间
com.orbps.contractReview = {};
// 新建contractEntry.offlineList命名空间
com.orbps.contractReview.listimport = {};
com.orbps.contractReview.listimport.coverageListTb = $("#coverageListTb");
com.orbps.contractReview.listimport.ipsnInfoForm = $("#ipsnInfoForm");
com.orbps.contractReview.listimport.hldrInfoForm = $("#hldrInfoForm");
com.orbps.contractReview.listimport.btnModel = $("#uploadListForm");
// 集合
com.orbps.contractReview.listimport.listImportBsInfoVo =[];
com.orbps.contractReview.listimport.listImportSubPolVos = [];
com.orbps.contractReview.listimport.listImportSubPolVo = {};
com.orbps.contractReview.listimport.queryBusiprodList = [];
// 新建businessKey全局变量
com.orbps.contractReview.listimport.businessKey;
// 新建taskId全局变量
com.orbps.contractReview.listimport.taskId;
// 新建要约信息list
com.orbps.contractReview.listimport.insuredList = [];
com.orbps.contractReview.listimport.BsInfoFormList = [];
// 新建insuredCount
com.orbps.contractReview.listimport.insuredCount = 0;
// 新建insuredType
com.orbps.contractReview.listimport.insuredType = -1;
// 新建保单类型全局变量
com.orbps.contractReview.listimport.cntrType ={};
com.orbps.contractReview.listimport.appConnIdTypeValue;
com.orbps.contractReview.listimport.appIdTypeValue;
//上一人 下一人标识
com.orbps.contractReview.listimport.flag;
//基本信息校验规则
com.orbps.contractReview.listimport.offlineValidateForm = function(vform){
	var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules :{
        	//医保编号
        	medicalInsurNo : {
        		isNum : true
        	},
        	//医保代码
        	medicalInsurCode : {
        		isMedicalInsurCode : true
        	},
        	//被保险人编号
        	ipsnNo : {
        		required : true,
        		isNum : true
        	},
        	//被保险人姓名
        	name :{
        		required : true,
        		zh_verifyl : true
        	},
        	//被保险人属组号
        	feeGrpNo : {
        		isNum : true
        	},
        	//邮箱
        	email : {
        		email : true
        	},
        	//工号
        	workNo : {
        		isNum : true
        	},
        	//个人交费金额
        	charge : {
        		isFloatGteZero : true
        	},
        	//单位交费金额
        	grpPayAmount : {
        		isFloatGteZero : true
        	},
        	//同业公司人身保险保额合计
        	tyNetPayAmnt : {
        		isFloatGteZero : true
        	},
        	//证件号码
        	idNo : {
        		required : true,
        		isIdCardNo : true
        	}
        },
        messages : {
        	//医保编号
        	medicalInsurNo : {
        		isNum : "请输入正确的医保编号"
        	},
        	//医保代码
        	medicalInsurCode : {
        		isMedicalInsurCode : "请输入1位医保代码"
        	},
        	//被保险人编号
        	ipsnNo : {
        		required : "被保险人编号不能为空",
        		isNum : "请输入正确的被保险人编号"
        	},
        	name  : {
        		required : "请输入被保险人姓名",
        		zh_verifyl : "请输入2~10位保险人姓名"
        	},
        	//被保险人属组号
        	feeGrpNo : {
        		isNum : "请输入正确的被保险人属组号"
        	},
        	//邮箱
        	email : {
        		email : "请输入正确格式的邮箱"
        	},
        	//工号
        	workNo : {
        		isNum : "请输入正确的工号"
        	},
        	//个人交费金额
        	charge : {
        		isFloatGteZero : "请输入>=0的交费金额"
        	},
        	//单位交费金额
        	grpPayAmount : {
        		isFloatGteZero : "请输入>=0的交费金额"
        	},
        	//同业公司人身保险保额合计
        	tyNetPayAmnt : {
        		isFloatGteZero : "同业公司人身保险保额合计"
        	},
        	//证件号码
        	idNo : {
        		required : "证件号码不能为空",
        		 isIdCardNo : "请您核对并输入汇交人正确的证件号码",
        	}
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

//被保险人编号失去焦点事件
$("#ipsnNo").blur(function(){
	var ipsnNo = $("#ipsnNo").val();
	if(ipsnNo === null || "" === ipsnNo){
		lion.util.info("警告","被保险人编号不能为空");
		return false;
	}
});

$('body').delegate('table#AccInfoTb #seqNo' , 'blur', function(){
	var seqNo = $("#seqNo").val();
	var zh_verify = /^[0-9]*$/;
	if(!zh_verify.test(seqNo))
		lion.util.info("警告", "请输入数字");
	return false;
});

//账户顺序抬起事件 缴费信息校验
$('body').delegate('table#AccInfoTb #seqNo' , 'keyup', function(){
	if(!isNaN(this.value)){
		this.value=/^[1-9]\d*$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-this.value.length)
	}else{
		this.value="";
		};
	$("#seqNo").val(this.value);
});

$('body').delegate('table#AccInfoTb #ipsnPayAmnt' , 'blur', function(){
	var ipsnPayAmnt = $("#ipsnPayAmnt").val();
	var zh_verify = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
	if(!zh_verify.test(ipsnPayAmnt))
		lion.util.info("警告", "请输入大于零的或者符合规范的个人扣款金额");
	$("#ipsnPayAmnt").val("");
	return false;
});
$('body').delegate('table#AccInfoTb #ipsnPayPct' , 'blur', function(){
	var ipsnPayPct = $("#ipsnPayPct").val();
	var zh_verify = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
	if(!zh_verify.test(ipsnPayPct))
		lion.util.info("警告", "请输入大于零的个人账户交费比例");
		$("#ipsnPayPct").val("");
	return false;
});
$('body').delegate('table#AccInfoTb #bankAccName' , 'keyup', function(){
	var $nameInput = $(this);
	// 响应鼠标事件，允许左右方向键移动
	event = window.event || event;
	if (event.keyCode === 37 | event.keyCode === 39) {
		return;
	}
	event = window.event || event;
	if (event.keyCode == 37 | event.keyCode == 39) {
		return;
	}
	// 先把非数字的都替换掉，除了数字和.
	$nameInput.val($nameInput.val().replace(
			/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\.\(\)\（\）\·\-\ ]/g,
			''));

	var name = this.value;
	var len = name.length;
	var reLen = 0;
	for (var i = 0; i < len; i++) {
		if (name.charCodeAt(i) < 27
				|| name.charCodeAt(i) > 126) {
			// 全角
			reLen += 2;
		} else {
			reLen++;
		}
	}
	if (reLen > 200) {
		name = name.substring(0, name.length - 1);
		$nameInput.val(name);
	}	
});
$('body').delegate('table#AccInfoTb #bankAccName' , 'blur', function(){
	  var name = $("#AccInfoTb #bankAccName").val();
	    if (name == null || "" == name) {
	        lion.util.info("警告", "交费账户不能为空");
	        return false;
	    }
	    //字符间只能有一个空格
	    name=name.replace(/^ +| +$/g,'').replace(/ +/g,' ');
		$("#AccInfoTb #bankAccName").val(name);
		var $nameInput = $(this);
		// 最后一位是小数点的话，移除
		$nameInput.val(($nameInput.val().replace(/\.$/g, "")));
});
$('body').delegate('table#AccInfoTb #bankAccNo' , 'blur', function(){
	var bankAccNo = $("#bankAccNo").val();
	 if (bankAccNo == null || "" == bankAccNo) {
			lion.util.info("警告", "银行账号不允许为空");
		return false;
		 }
});
//受益人
$('body').delegate('table#beneInfoTb #benefitOrder' , 'keyup', function(){
	if(!isNaN(this.value)){
		this.value=/^[1-9]\d*$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-this.value.length)
	}else{
		this.value="";
		};
	$("#benefitOrder").val(this.value);
});
//姓名   
$('body').delegate('table#beneInfoTb #name' , 'keyup', function(){
	var $nameInput = $(this);
	// 响应鼠标事件，允许左右方向键移动
	event = window.event || event;
	if (event.keyCode === 37 | event.keyCode === 39) {
		return;
	}
	event = window.event || event;
	if (event.keyCode == 37 | event.keyCode == 39) {
		return;
	}
	// 先把非数字的都替换掉，除了数字和.
	$nameInput.val($nameInput.val().replace(
			/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\.\(\)\（\）\·\-\ ]/g,
			''));

	var name = this.value;
	var len = name.length;
	var reLen = 0;
	for (var i = 0; i < len; i++) {
		if (name.charCodeAt(i) < 27
				|| name.charCodeAt(i) > 126) {
			// 全角
			reLen += 2;
		} else {
			reLen++;
		}
	}
	if (reLen > 200) {
		name = name.substring(0, name.length - 1);
		$nameInput.val(name);
	}	
});
$('body').delegate('table#beneInfoTb #name' , 'blur', function(){
	  var name = $("#beneInfoTb #name").val();
	    if (name == null || "" == name) {
	        lion.util.info("警告", "受益人姓名不能为空");
	        return false;
	    }
	    //字符间只能有一个空格
	    name=name.replace(/^ +| +$/g,'').replace(/ +/g,' ');
		$("#beneInfoTb #name").val(name);
		var $nameInput = $(this);
		// 最后一位是小数点的话，移除
		$nameInput.val(($nameInput.val().replace(/\.$/g, "")));
});

$('body').delegate('table#beneInfoTb #molecule' , 'blur', function(){
	var ipsnPayPct = $("#ipsnPayPct").val();
	//var zh_verify = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/
	if(0>ipsnPayPct || 100<ipsnPayPct)
		lion.util.info("警告", "请输入大于零并且不大于100的受益份额比例");
	return false;
});
$('body').delegate('table#beneInfoTb #molecule' , 'keyup', function(){
	if(!isNaN(this.value)){
		//小数点后两位的校验。
		numberfloatValidata(this);
	}
});

$('body').delegate('table#beneInfoTb #idNo' , 'keyup', function(){
	
	checklength(this,25);
	return false;
});



//证件号码验证
$('body').delegate('table#beneInfoTb #idNo' , 'blur', function(){
	var flag;
	var idNo = $("#beneInfoTb #idNo").val();
	var idTypeTb = $("#idTypeTb").val();
	 if(idNo === null || "" === idNo){
		 lion.util.info("警告","证件号不能为空");
		 return false;
	 }else{
		 
	 }
	
	if (idTypeTb === "I") {
		flag=idCardNoUtil.checkIdCardNo($("#beneInfoTb #idNo").val());
		if(!flag){
			lion.util.info("警告", "身份证号码不符合规则");
			$("#beneInfoTb #idNo").val("");
			return;
		}
    }
});
// 查询任务ID回调函数
com.orbps.contractReview.listimport.successQueryDetail = function (data,args){
	//alert(JSON.stringify(data));
	//alert("查询的保单类型"+data.cntrType);
	com.orbps.contractReview.listimport.cntrType = data.cntrType;
	if("G"===com.orbps.contractReview.listimport.cntrType){
		lion.util.postjson('/orbps/web/orbps/contractEntry/grp/query',data.taskInfo,com.orbps.contractReview.successQueryDetail,null,null);		
		$("#hldrInfoForm").hide();
		$("#divrelToHldr").hide();//隐藏select框的div
	}else{
		lion.util.postjson('/orbps/web/orbps/contractEntry/sg/query',data.taskInfo,com.orbps.contractReview.successQueryDetail,null,null);
	}
	if("1"===data.premSource){
		$("#AccInfo").hide();
	}
	com.orbps.contractReview.listimport.data = data;
	com.orbps.contractReview.listimport.businessKey = data.taskInfo.bizNo;
	com.orbps.contractReview.listimport.taskId = data.taskInfo.taskId;
	$('#ipsnInfoFormQuery #applNo').val(com.orbps.contractReview.listimport.businessKey);
};
$(function() {
	// 校验函数初始化
	com.orbps.contractReview.listimport.offlineValidateForm(com.orbps.contractReview.listimport.ipsnInfoForm);
	 // 身份证号验证
    jQuery.validator.addMethod("isIdCardNo", function(value, element) {
    	var appIdTypeValue = $("#ipsnInfoForm #idType").val();
        // 先判断证件类型是否为身份证再去校验,appIdTypeValue(证件类型的值)再各自需要的js里设置为全局变量,
        if (appIdTypeValue === "I") {
            return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
        } else {
            return true;
        }
    }, "请正确输入您的身份证号码！");
 // 如果联系人证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别、年龄等
    $("#ipsnInfoForm #idNo").blur(
         function() {
        	 var idNo = $("#idNo").val();
        	 if(idNo === null || "" === idNo){
        		 lion.util.info("警告","证件号不能为空");
        		 return false;
        	 }else{
        		 idcard($("#ipsnInfoForm #idType"),
    					$("#ipsnInfoForm #idNo"),
    					$("#ipsnInfoForm #birthDate"),
    					$("#ipsnInfoForm #ipsursex"));
        		 checkAge();
        	 }
         });
    // 当联系人证件类型改变的 时候，将五要素只读变成可写
    $("#ipsnInfoForm #idType")
         .change(
                 function() {
                	 var str = $("#ipsnInfoForm #idType").val();
                     if ("I" !== str) {
                         $("#ipsnInfoForm #birthDate").attr("readonly",
                                 false);
                         $("#ipsnInfoForm #ipsursex").attr("readonly",
                                 false);
                     }else{
                    	 $("#ipsnInfoForm #birthDate").attr("readonly",
                                 true);
                         $("#ipsnInfoForm #ipsursex").attr("readonly",
                                 true);
                     }
                 });
    //投保人信息联系人证件类型改变的 时候，将五要素只读变成可写
    //changFlag用于页面回显时，证件号码发生改变时，判断不进行操作
    var changFlag = 0;
    $("#hldrInfoForm #hldrIdType").change(
            function() {
            	 var str = $("#hldrInfoForm #hldrIdType").val();
            	 if(str === "I"){
            		 $("#hldrInfoForm #hldrBirth").attr("disabled",true);
                  	 $("#hldrInfoForm #hldrSex").attr("disabled",true);
            	 }
            	//当页面回显时等于 0 1 2 3 直接return false 不做操作。因为回显做改变时会把之前回显的数据给丢失
             	 if(changFlag === 0 ||changFlag === 1 || changFlag === 2 ||changFlag === 3 ){
             		 changFlag ++;
             		 return false;
             	 }
                if ("I" === str) {
                    $("#hldrInfoForm #hldrBirth").attr("disabled",true);
	                $("#hldrInfoForm #hldrSex").attr("disabled",true);
	               	$("#hldrInfoForm #hldrBirth").val("");
                	$("#hldrInfoForm #hldrSex").combo("clear");
                	 $("#hldrInfoForm #hldrIdNo").val("");
                }else{
                	$("#hldrInfoForm #hldrBirth").attr("disabled",false);
                	$("#hldrInfoForm #hldrSex").attr("disabled",false);
                	$("#hldrInfoForm #hldrBirth").val("");
                	$("#hldrInfoForm #hldrSex").combo("clear");
                	 $("#hldrInfoForm #hldrIdNo").val("");
	               	
                }
            });
    //如果证件类型是身份证的时候校验身份证信息，并回显生日和性别
    $("#hldrInfoForm #hldrIdNo").blur(function(){
    	 var hldrIdNo = $("#hldrIdNo").val();
    	 if(hldrIdNo === null || "" === hldrIdNo){
    		 lion.util.info("警告","证件号不能为空");
    		 return false;
    	 }else{
    		 if($("#hldrInfoForm #hldrIdType").val() === "I"){
    			 if(idCardNoUtil.checkIdCardNo(hldrIdNo)){
    				 idcard($("#hldrInfoForm #hldrIdType"),
    						 $("#hldrInfoForm #hldrIdNo"),
    						 $("#hldrInfoForm #hldrBirth"),
    						 $("#hldrInfoForm #hldrSex"));
    			 }else{
        			 lion.util.info("警告","证件号码输入不正确，请检查！");
            		 return false; 
        		 }
    		 }
    	 }
    });
	$("#applNo").attr("readOnly",true);
	$("#ipsnInfoForm #age").attr("disabled",true);
	$("#charge").attr("readOnly",true);
	$("#grpPayAmount").attr("readOnly",true);

    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
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
    
    // 文件上传参数设置
    $("#fileupload").fileinput({
        'allowedFileExtensions' : ['xlsm', 'xlt','xls','xlsx','csv'],
        'showUpload':false,
        'showPreview':false,
        'showCaption':true,
        'browseClass':'btn btn-success',
    });
    
    // combo组件初始化
    $("*").comboInitLoad();
    
    // 初始化edittable组件
    $("#beneInfoTb").editDatagridsLoadById();

//    var taskInfo = {};
//    taskInfo.bizNo="2017000000032203";
//    lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/comback",taskInfo,com.orbps.contractReview.listimport.successQueryDetail,null,null);
    //现在用的逻辑是页面跳转不是上面的服务调用。
    var taskInfo = {};
    var applTaskId = com.ipbps.data;
    taskInfo.bizNo = applTaskId.substr(0,16);//投保单号
    taskInfo.taskId= applTaskId.substr(17);//taskId
    lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/comback",taskInfo,com.orbps.contractReview.listimport.successQueryDetail,null,null);

    //根据职业代码，设置风险等级
    $("#ipsnInfoForm #occupationalCodes").change(function(){
    	setTimeout(function(){
    		//获取隐藏的风险等级
    		var level = $("#ipsnInfoForm #level").val();
    		$("#ipsnInfoForm #riskLevel").val(level);
    	}, 100);
    });
    
    // 被保险人信息查询
    $('#ipsnInfoFormQuery #btnQuery').click(function(){
    	//重置证件类型flag
    	changFlag = 0;
    	com.orbps.contractReview.listimport.BsInfoFormList=[];
        var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
        var applNo = $('#ipsnInfoFormQuery #applNo').val();
        if(applNo!==""){
            //var custNo = $('#ipsnInfoFormQuery #custNo').val();
            var ipsnNo = $('#ipsnInfoFormQuery #ipsnNo').val();
            if(ipsnNo!==""){
            	//清空被保人编号
            	$("#ipsnInfoFormQuery #ipsnNo").val("");
                lion.util.postjson(
                        '/orbps/web/orbps/contractEntry/offlineList/queryInsured',
                        listImportIpsnInfoVo,
                        com.orbps.contractReview.listimport.successQuery,
                        null,
                        null);
            }else{
                lion.util.info("提示","被保险人编号至少填写一个")
            }
        }
    });
 // 上一个被保人
    $('#ipsnInfoFormQuery #btnLast').click(function(){
    	//重置证件类型flag
    	changFlag = 0;
    	com.orbps.contractReview.listimport.BsInfoFormList = [];
    	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
    	var applNo = $('#ipsnInfoForm #applNo').val();
    	if(applNo!==""){
            var ipsnNo = $('#ipsnInfoForm #ipsnNo').val();
            if(ipsnNo!==""){
            	//上一人标识
            	com.orbps.contractReview.listimport.flag = "s";
            	listImportIpsnInfoVo.flag="pre";
            	listImportIpsnInfoVo.ipsnNo = ipsnNo;
                lion.util.postjson(
                        '/orbps/web/orbps/contractEntry/offlineList/queryNextInsured',
                        listImportIpsnInfoVo,
                        com.orbps.contractReview.listimport.successQuery,
                        null,
                        null);
            }else{
                lion.util.info("提示","客户号或者被保险人编号至少填写一个")
            }
    	}
    });
    // 下一个被保人
    $('#ipsnInfoFormQuery #btnNext').click(function(){
    	//重置证件类型flag
    	changFlag = 0;
    	com.orbps.contractReview.listimport.BsInfoFormList = [];
    	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
    	var applNo = $('#ipsnInfoFormQuery #applNo').val();
    	if(applNo!==""){
            var ipsnNo = $('#ipsnInfoForm #ipsnNo').val();
            if(ipsnNo!==""){
            	//下一人标识
            	com.orbps.contractReview.listimport.flag = "x";
            	listImportIpsnInfoVo.flag="next";
            	listImportIpsnInfoVo.ipsnNo = ipsnNo;
                lion.util.postjson(
                        '/orbps/web/orbps/contractEntry/offlineList/queryNextInsured',
                        listImportIpsnInfoVo,
                        com.orbps.contractReview.listimport.successQuery,
                        null,
                        null);
            }else{
                lion.util.info("提示","客户号或者被保险人编号至少填写一个")
            }
    	}
    });
    
    // 删除,fuhe 
//    $('#submit #btnDel').click(function(){
//        var listImportIpsnInfoVo = {};
//        listImportIpsnInfoVo = com.orbps.contractReview.listimport.ipsnInfoForm.serializeObject();
//        listImportIpsnInfoVo.applNo = com.orbps.contractReview.listimport.businessKey;
//        lion.util.postjson(
//                '/orbps/web/orbps/contractReview/offlineList/delete',
//                listImportIpsnInfoVo,
//                com.orbps.contractReview.listimport.successdelete,
//                null,
//                null);
//    });
   
    // 修改
    $('#submit #btnEdit').click(function(){
    	 $("#ipsnInfoForm #birthDate").attr("disabled",
                 false);
    	 $("#hldrInfoForm #hldrBirth").attr("disabled",
                 false);
    	if(com.orbps.contractReview.listimport.ipsnInfoForm.validate().form()){
        	//调用修改代码
        	var listImportPageVo = {};
         	if(typeof(experimental(listImportPageVo)) === "boolean") {
        		return false;
        	}else{
        		listImportPageVo=experimental(listImportPageVo);
        	}
        	lion.util.postjson(
                    '/orbps/web/orbps/contractReview/offlineList/update',
                    listImportPageVo,
                    com.orbps.contractReview.listimport.successUpdate,
                    null,
                    null);
        	}
    });
    
    //导出被保人清单
    $("#btnExport").click(function(){
    	window.location.href="/orbps/web/orbps/contractReview/offlineList/download/"+com.orbps.contractReview.listimport.businessKey;
    });
    
    // 全部复核完成
	$('#btnAllSubmit')
			.click(
					function() {
						applNo = com.orbps.contractReview.listimport.businessKey;
						applNo = applNo + "," + com.orbps.contractReview.listimport.taskId;
						var str;
						var key = com.orbps.contractReview.listimport.cntrType;
						// 如果是选择通过
						if ($("#reviewFlag").val() === "Y") {
							switch (key) {
							case "L":
								str = "27";// 清汇复核通过
								break;
							case "G":
								str = "21";// 团单复合
								break;
							}
						}else if($("#reviewFlag").val() === "N") {
							switch (key) {
							case "L":
								str = "28";// 清汇复核不通过
								break;
							case "G":
								str = "22";// 团单复合不通过
								break;
							}
						}else{
							lion.util.info("提示", "请选择复核是否通过！");
							return false;
						}
						applNo = applNo + "," + str;
						lion.util
								.postjson(
										'/orbps/web/orbps/contractReview/offlineList/submitAll',
										applNo,
										com.orbps.contractReview.listimport.successSubmitAll,
										null, null);
					});
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
	if(joinIpsnFlag==="N"){
		$("#relToIpsn").combo("val", "M");
		$("#relToIpsn").attr("readOnly",true);
		$("#mainIpsnNo").val("");
		$("#mainIpsnNo").attr("readOnly",true);
        $(".fa").removeClass("fa-warning");
        $(".fa").removeClass("fa-check");
        $(".fa").removeClass("has-success");
        $(".fa").removeClass("has-error");
	}else{
		$("#relToIpsn").attr("readOnly",false);
		//$("#ipsnInfoForm #relToIpsn").combo("refresh");
		$("#mainIpsnNo").attr("readOnly",false);
	}
});

// 增加表格
$("#beneficiaryInfo #btnAddBusi").click(function() {
    $("#beneInfoTb").editDatagrids("addRows");
    return false;
});


// 删除表格
$("#beneficiaryInfo #btnDelBusi").click(function() {
    $("#beneInfoTb").editDatagrids("delRows");
    return false;
});

//增加表格
$("#AccInfo #addAccInfo").click(function() {
    $("#AccInfoTb").editDatagrids("addRows");
    return false;
});

// 删除表格
$("#AccInfo #delAccInfo").click(function() {
    $("#AccInfoTb").editDatagrids("delRows");
    return false;
});

//根据医保标识选择是否置灰
$("#ipsnInfoForm #medicalInsurFlag").change(function(){
	var medicalInsurFlag = $("#ipsnInfoForm #medicalInsurFlag").val();
	if(medicalInsurFlag==="1"){
		$("#ipsnInfoForm #medicalInsurCode").attr("readOnly",true);
		$("#ipsnInfoForm #medicalInsurNo").attr("readOnly",true);
	}else{
		$("#ipsnInfoForm #medicalInsurCode").attr("readOnly",false);
		$("#ipsnInfoForm #medicalInsurNo").attr("readOnly",false);
	}
});

//清汇被保人与投保人关系,增加如果是清汇保单校验投保信息必录项
$("#relToHldr").change(function(){
	var name = $("#name").val();
	var sex = $("#ipsursex").val();
	var idType = $("#idType").val();
	var idNo = $("#idNo").val();
	var birthDate = $("#birthDate").val();
	var phone = $("#phone").val();
	$("#hldrName").attr("readonly",false);
	$("#hldrSex").attr("readonly",false);
	$("#hldrIdType").attr("readonly",false);
	$("#hldrIdNo").attr("readonly",false);
	$("#hldrBirth").attr("disabled",false);
	$("#hldrBirths").attr("disabled",false);
	$("#hldrPhone").attr("readonly",false);
	
	$("#hldrName").val("");
	$("#hldrSex").combo("clear");
	$("#hldrIdType").combo("clear");
	$("#hldrIdNo").val("");
	$("#hldrBirth").val("");
	$("#hldrPhone").val("");
	setTimeout(function(){
		if(com.orbps.contractReview.listimport.cntrType ==="L" && $("#relToHldr").val()==="M"){
			$("#hldrName").val(name);
			$("#hldrIdType").combo("val",idType);
			$("#hldrSex").combo("val",sex);
			$("#hldrIdNo").val(idNo);
			$("#hldrBirth").val(birthDate);
			$("#hldrPhone").val(phone);
			$("#hldrName").attr("readonly",true);
			$("#hldrSex").attr("readonly",true);
			$("#hldrIdType").attr("readonly",true);
			$("#hldrIdNo").attr("readonly",true);
			$("#hldrBirth").attr("disabled",true);
			$("#hldrBirths").attr("disabled",true);
			$("#hldrPhone").attr("readonly",true);
		}else if(com.orbps.contractReview.listimport.cntrType ==="L"){
		    $("#hldrName").blur(function() {
		        var hldrName = $("#hldrName").val();
		        if (hldrName === null || "" === hldrName) {
		            lion.util.info("警告", "投保人姓名不能为空");
		            return false;
		        }
		    });
		    $("#hldrIdNo").blur(function() {
		        var hldrIdNo = $("#hldrIdNo").val();
		        if (hldrIdNo === null || "" === hldrIdNo) {
		            lion.util.info("警告", "证件号码不能为空");
		            return false;
		        }
		    });
		    $("#hldrPhone").blur(function() {
		        var hldrPhone = $("#hldrPhone").val();
		        if (hldrPhone === null || "" === hldrPhone) {
		            lion.util.info("警告", "投保人手机号不能为空");
		            return false;
		        }
		    });
		}else{
			$("#hldrName").val("");
			$("#hldrInfoForm select").combo("refresh");
			$("#hldrIdNo").val("");
			$("#hldrBirth").val("");
			$("#hldrPhone").val("");
		}	
	},500);

});




// 点击radio，将其数据回显到录入界面
com.orbps.contractReview.listimport.publicRadio = function(vform) {
    $("#bsInfoForm input[type='text']").val("");
    $("#bsInfoForm #polCode").find("option[value='']").attr("selected", true); 
	$("#bsInfoForm #subPolCode").find("option[value='']").attr("selected", true); 
	$("#bsInfoForm #groupCode").find("option[value='']").attr("selected", true); 
    var radioVal;
    //回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
    var temp = document.getElementsByName("insuredRad");
    for(var i=0;i<temp.length;i++){
         if(temp[i].checked){
             radioVal = temp[i].value;
         }
    }
    com.orbps.contractReview.listimport.insuredType = radioVal;
    insuredVo= com.orbps.contractReview.listimport.BsInfoFormList[radioVal];
    setTimeout(function(){
        // 回显
        com.orbps.contractReview.listimport.showInsured(insuredVo);
    },100);
};
//
com.orbps.contractReview.listimport.reloadPublicInsuredModalTable = function() {
	$('#coverageListTb').find("tbody").empty();
	if (com.orbps.contractReview.listimport.BsInfoFormList !== null || com.orbps.contractReview.listimport.BsInfoFormList > 0) {
		for (var i = 0; i < com.orbps.contractReview.listimport.BsInfoFormList.length; i++) {
			var polCode=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].polCode);
			var polName=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].polName);
			var subPolCode=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].subPolCode);
			var subPolName=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].subPolName);
			var groupCode=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].groupCode);
			var groupName=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].groupName);
			var amount=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].amount);
			var premium=com.orbps.contractReview.listimport.isEmpty(com.orbps.contractReview.listimport.BsInfoFormList[i].premium);
			$('#coverageListTb').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.contractReview.listimport.publicRadio();' id='insuredRad"
							+ i
							+ "' name='insuredRad' value='"
							+ i
							+ "'></td><td >"
							+ polCode
							+ "</td><td >"
							+ polName
							+ "</td><td >"
							+ subPolCode
							+ "</td><td >"
							+ subPolName
							+ "</td><td >"
							+ groupCode
							+ "</td><td >"
							+ groupName
							+ "</td><td >"
							+ amount
							+ "</td><td >"
							+ premium
							+ "</td></tr>");
		}
	} else {
		$('#coverageListTb').find("tbody").append("<tr><td colspan='9' align='center'>无记录</td></tr>");
	}
}

// 回显表单方法
com.orbps.contractReview.listimport.showInsured = function(msg){
    jsonStr = JSON.stringify(msg);
    var obj = eval("("+jsonStr+")");
    var key,value,tagName,type,arr;
    for(x in obj){
        key = x;
        value = obj[x];
        if("birthDate"==key||"birthDate"==key){
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
                    $("#bsInfoForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
            	 $("#bsInfoForm #"+key).find("option[value='"+value+"']").attr("selected", true); 
            }
        });
    }
}

//日期回显long转String
Date.prototype.format = function(fmt)   { 
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

// 添加成功回调函数，复核不需要添加被保人
//com.orbps.contractReview.listimport.successSubmit = function (data,arg){
//	if (data.retCode==="1"){
//        lion.util.info("提示", "添加成功");
//    }else{
//        lion.util.info("提示", "添加失败，失败原因："+data.errMsg);
//    }
//}

// 全部录入完成回调函数
com.orbps.contractReview.listimport.successSubmitAll = function (data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示", "全部复核完成");
    }else{
        lion.util.info("提示", "全部复核失败，失败原因："+data.errMsg);
    }
}

//修改成功回调函数
com.orbps.contractReview.listimport.successUpdate = function (data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示", "修改成功");
    }else{
        lion.util.info("提示", "修改失败，失败原因："+data.errMsg);
    }
}

// 删除成功回调函数，复核不需要删除被保人功能
//com.orbps.contractReview.listimport.successdelete = function (data,arg){
//	if (data.retCode==="1"){
//        lion.util.info("提示", "删除成功");
//    }else{
//        lion.util.info("提示", "删除失败，失败原因："+data.errMsg);
//    }
//}

//公共代码 回显值
com.orbps.contractReview.listimport.isEmpty =function(str){
	if(undefined!==str&& null!=str && ''!==str){
		return str;
	}else{
		return '';
	}
}
//公共代码 回显bool值
com.orbps.contractReview.listimport.isEmptyBool=function(str){
	if(undefined!==str&& null!==str && ''!==str){
		return true;
	}else{
		return false;
	}
}

// 查询成功回调函数
com.orbps.contractReview.listimport.successQuery = function (data,arg){
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
	    com.orbps.contractReview.listimport.listImportBsInfoVo = data.bsInfoVo;
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
	    		com.orbps.contractReview.listimport.BsInfoFormList.push(BsInfoFormList);
	    	}
	    }
	    // 重新加载险种表格
	    com.orbps.contractReview.listimport.reloadPublicInsuredModalTable();
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
		if(com.orbps.contractReview.listimport.flag === "s"){
			lion.util.info("提示","当前被保险人已经是第一个人了！");
		}else if(com.orbps.contractReview.listimport.flag === "x"){
			lion.util.info("提示","当前被保险人已经是最后一人了！");
		}
	}
}



//根据险种代码选择相应的责任名称
$("#bsInfoForm #polCode").change(function(){
	var polCode = $("#bsInfoForm #polCode").val();
	var data = com.orbps.contractReview.listimport.data;
	for(var i = 0; i < data.bsInfoVo.length; i++){
		//如果页面取到的险种代码和全局变量相等时，取对应下标的name值
		if(polCode === data.bsInfoVo[i].polCode){
			var polName = data.bsInfoVo[i].polName;
			//绑定到险种名称上
			$("#bsInfoForm #polName").val(polName);
			break;
		}else{
			//绑定到险种名称上
			$("#bsInfoForm #polName").val("");
		}
	}
	
	var subPolCode = document.getElementById("subPolCode");
	var groupCode = document.getElementById("groupCode");
	$("#bsInfoForm #subPolCode").find("option").remove();
	$("#bsInfoForm #groupCode").find("option").remove();
	// 添加责任option
	subPolCode.options.add(new Option("请先选择险种代码...","0"));
	// 添加属组option
    groupCode.options.add(new Option("请先选择险种代码...","0"));
	if(polCode!==""){
		var data = com.orbps.contractReview.listimport.data;
		for(var i = 0; i < data.bsInfoVo.length; i++){
			if(polCode===data.bsInfoVo[i].polCode){
				$("#bsInfoForm #subPolCode").attr("readOnly",false);
				$("#bsInfoForm #groupCode").attr("readOnly",false);
		        for(var j = 0; j < data.bsInfoVo[i].listImportSubPolVos.length ; j++ ){
		        	// 把责任代码绑定到select的id上
		        	var subPolCodeid = data.bsInfoVo[i].listImportSubPolVos[j].subPolCode;
	        		// 添加责任option
		        	subPolCode.options.add(new Option(subPolCodeid,subPolCodeid));
		        	// 把属组代码绑定到select的value上
		        	var groupCodeid = data.bsInfoVo[i].listImportSubPolVos[j].groupCode;
		        	// 添加属组option
		        	groupCode.options.add(new Option(groupCodeid,groupCodeid));	
		        }
		        $("#bsInfoForm #subPolCode").combo("val","请先选择险种代码...");
		        $("#bsInfoForm #subPolName").val("");
		    	$("#bsInfoForm #groupCode").combo("val","请先选择险种代码...");
		    	$("#bsInfoForm #groupName").val("");
		        return false;
			}
	    }
	}
});
//逻辑同上
$("#bsInfoForm #subPolCode").change(function(){
	 var subPolCode = $("#subPolCode").val();
	 data = com.orbps.contractReview.listimport.data;
	 for(var i = 0; i < data.bsInfoVo.length; i++){
		 for(var j = 0; j < data.bsInfoVo[i].listImportSubPolVos.length ; j++ ){
			 if(subPolCode === data.bsInfoVo[i].listImportSubPolVos[j].subPolCode){
				 var subPolName = data.bsInfoVo[i].listImportSubPolVos[j].subPolName;
				 $("#subPolName").val(subPolName);
				 $("#subPolName").attr("readOnly",true);
				 return false;
			 }else{
				 $("#bsInfoForm #subPolName").val("");
			 }
		 }
	 }
});
//逻辑同上
//根据属组回显相应的险种信息以及责任名称
$("#bsInfoForm #groupCode").change(function(){
	com.orbps.contractReview.listimport.BsInfoFormList = [];
	com.orbps.contractReview.listimport.listImportBsInfoVo=[];
	var insuredVos = {};
	var listImportBsInfoVo = {};
	var listImportSubPolVo = {};
	var name="";
	var groupCode = $("#bsInfoForm #groupCode").val();
	var data = com.orbps.contractReview.listimport.data;
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
				com.orbps.contractReview.listimport.listImportBsInfoVo.push(listImportBsInfoVo);
				com.orbps.contractReview.listimport.BsInfoFormList.push(insuredVos);
				com.orbps.contractReview.listimport.insuredCount++;
			}
			
		}else{
			$("#bsInfoForm #groupName").val("");
		}
	}
	$("#bsInfoForm #groupName").val(name);
	com.orbps.contractReview.listimport.reloadPublicInsuredModalTable();
});

//要约险种信息校验
com.orbps.contractReview.listimport.proposalInsuredGroupValidate = function (){
	var polCode = $("#bsInfoForm #polCode").val();
	if(polCode === null || "" === polCode){
		lion.util.info("警告","请选择险种代码");
		return false;
	}
	var subPol=0;
	for(var i = 0; i < data.bsInfoVo.length; i++){
		if(polCode === data.bsInfoVo[i].polCode){  //比较险种是否相同 相同则获取 name 和责任
			subPol=data.bsInfoVo[i].listImportSubPolVos.length;
		}
	}
	if(subPol>0){
		var subPolCode = $("#bsInfoForm #subPolCode").val();
		if(subPolCode === null || "" === subPolCode){
			lion.util.info("警告","险种有责任时，责任必录");
			return false;
		}
	}
	var amount = $("#bsInfoForm #amount").val();
	if(amount === null || "" === amount){
		lion.util.info("警告","保额不能为空");
		return false;
	}
	
	var premium = $("#bsInfoForm #premium").val();
	if(premium === null || "" === premium){
		lion.util.info("警告","保费不能为空");
		return false;
	}
	return true;
}
$("#btnlocation").click(function(){
	var listType = com.orbps.contractReview.listimport.cntrType;
	if("G" === listType){
		com.ipbps.menuFtn("orbps/orbps/contractReview/grpInsurAppl/html/grpInsurApplForm.html",""); 
	}else if("L" === listType){
		com.ipbps.menuFtn("orbps/orbps/contractReview/sgGrpInsurAppl/html/sgGrpInsurAppl.html",""); 
	}
	
});

//姓名检验规则
function keyupName(id,name){
	var $nameInput = $(name);
	// 响应鼠标事件，允许左右方向键移动
	//获取光标所在文本中的下标
	var pos = getTxt1CursorPosition(name);
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		return;
	}
	// 先把非数字的都替换掉，除了数字和.
	$nameInput.val($nameInput.val().replace(
			/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\.\(\)\（\）\·\-\ ]/g,
			''));
	//移动光标到所定位置
	setCaret(name,pos);
	var name = $("#"+id).val();
	var len = name.length;
	var reLen = 0;
	for (var i = 0; i < len; i++) {
		if (name.charCodeAt(i) < 27
				|| name.charCodeAt(i) > 126) {
			// 全角
			reLen += 2;
		} else {
			reLen++;
		}
	}
	if (reLen > 200) {
		name = name.substring(0, name.length - 1);
		$("#"+id).val(name);
	}
}
function blurName(id,name){
	  var name = $("#"+id).val();
	    if (name == null || "" == name) {
	        lion.util.info("警告", name+"不能为空");
	        return false;
	    }
	    //字符间只能有一个空格
	    name=name.replace(/^ +| +$/g,'').replace(/ +/g,' ');
		$("#"+id).val(name);
		var $nameInput = $(this);
		// 最后一位是小数点的话，移除
		$nameInput.val(($nameInput.val().replace(/\.$/g, "")));
}
//回显保存录入信息
com.orbps.contractReview.successQueryDetail =function(data,arg){
	if("G" === com.orbps.contractReview.listimport.cntrType){
		com.orbps.contractReview.grpInsurAppl = data;
	}else{
		com.orbps.contractReview.sgGrpInsurAppl = data;
	}
    var premFrom="G" === com.orbps.contractReview.listimport.cntrType?com.orbps.contractReview.grpInsurAppl.payInfoVo.premFrom:com.orbps.contractReview.sgGrpInsurAppl.sgGrpPayInfoVo.premFrom;
    
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
function checkAge(){
	if(null!==$("#birthDate").val() && ""!==$("#birthDate").val()){
		 var d1 = new Date($("#birthDate").val().replace(/\-/g, "\/"));  
		 var d2 = new Date();  
		 var yearNow = d2.getFullYear();
		 var monthNow = d2.getMonth();
		 var dayOfMonthNow = d2.getDate();
		 var yearBirth = d1.getFullYear();
		 var monthBirth = d1.getMonth();
		 var dayOfMonthBirth = d1.getDate();
		 var age = yearNow - yearBirth;
		 if (monthNow <= monthBirth) {
			 if (monthNow == monthBirth) {
				 if (dayOfMonthNow < dayOfMonthBirth) {
					 age--;
				 }
			 } else {
				 age--;
			 }
		 }
		 $("#age").val(age);
	}else{
		 $("#age").val("");
	}
}
function experimental(listImportPageVo){
	var getAddRowsData = $("#beneInfoTb").editDatagrids("getRowsData");  //受益人信息
	var accInfoList = $("#AccInfoTb").editDatagrids("getRowsData");//缴费账户信息
	listImportPageVo.ipsnInfoVo = com.orbps.contractReview.listimport.ipsnInfoForm.serializeObject(); //被保险人信息主体
	//如果为团单，则不对投保人信息做序列化
	if("G"!==com.orbps.contractReview.listimport.cntrType){
		listImportPageVo.hldrInfoVo = com.orbps.contractReview.listimport.hldrInfoForm.serializeObject(); 
		if(isEmpty(listImportPageVo.ipsnInfoVo.relToHldr,"清单汇交件时，与投保人关系"))return false;
	}
	listImportPageVo.ipsnInfoVo.applNo = com.orbps.contractReview.listimport.businessKey; 
	listImportPageVo.bsInfoVo = com.orbps.contractReview.listimport.listImportBsInfoVo; 
	listImportPageVo.beneficiaryInfo = getAddRowsData;     //受益人信息
	listImportPageVo.accInfoList = accInfoList;//缴费账户信息
	//判断被保险人信息必录项以及满足条件的一些必录项
	if(isEmpty(listImportPageVo.ipsnInfoVo.joinIpsnFlag,"是否连带被保险人")) return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.idType,"证件类型")) return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.ipsursex,"被保险人性别"))return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.birthDate,"被保险人出生日期"))return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.occupationalCodes,"职业代码"))return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.riskLevel,"职业风险等级"))return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.onJobFlag,"是否在职"))return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.healthFlag,"是否有异常告知"))return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.healthFlag,"")){
		if(isEmpty(listImportPageVo.ipsnInfoVo.relToIpsn,"有当主被保人编号不为空时，与主被保险人关系"))return false;
	} 
	//如果为团单 并且组织架构树不为空 则层次代码必填
	if("G" === com.orbps.contractReview.listimport.cntrType
			&&null !== com.orbps.contractReview.grpInsurAppl.organizaHierarModalVos
			&& com.orbps.contractReview.grpInsurAppl.organizaHierarModalVos.length > 0){
		var groupLevelCode=$("#groupLevelCode").val();
		if(null === groupLevelCode ||  groupLevelCode === ""){
			lion.util.info("提示", "有组织架构树时，组织层次代码为必填项");
			return false;
		}
		var count=0;
		for (var i = 0; i < com.orbps.contractReview.grpInsurAppl.organizaHierarModalVos.length; i++) {
			if(groupLevelCode === com.orbps.contractReview.grpInsurAppl.organizaHierarModalVos[i].levelCode){
				count++;
			}
		}
		if(count === 0){
			lion.util.info("提示", "组织层次代码必须属于已录入的组织架构树");
			return false;
		}
	}
	//如果为团单 “收费属组号” 有收付费组时必填，该号码必须属于已录入的收付费组 
	if("G" === com.orbps.contractReview.listimport.cntrType
			&& null !== com.orbps.contractReview.grpInsurAppl.chargePayGroupModalVos
			&& com.orbps.contractReview.grpInsurAppl.chargePayGroupModalVos.length > 0){
		var feeGrpNo=$("#feeGrpNo").val();
		if(null === feeGrpNo || feeGrpNo === ""){
			lion.util.info("提示", "有收付费组时，收费属组号为必填项");
			return false;
		}
		var count=0;
		for (var i = 0; i < com.orbps.contractReview.grpInsurAppl.chargePayGroupModalVos.length; i++) {
			if(parseInt(feeGrpNo) === com.orbps.contractReview.grpInsurAppl.chargePayGroupModalVos[i].groupNo){
				count++;
			}
		}
		if(count === 0){
			lion.util.info("提示", "收费属组号必须属于已录入的收付费组号");
			return false;
		}
	}
	var premFrom=com.orbps.contractReview.listimport.data.premSource;
	   if("2" === premFrom || "3"=== premFrom){
		   //当收付费属组号是空的时候去校验缴费账户信息。
		   if($("#feeGrpNo").val() === ""){
			   if("" === $("#charge").val() || null ===$("#charge").val()){
		    		lion.util.info("提示", "当录入界面保费来源选择团体个人共同付款、个人付款时,个人交费金额必录");
					return false;
		    	}
		    	if(null !==accInfoList && !accInfoList.length>0 ){
		    		lion.util.info("提示", "当录入界面保费来源选择团体个人共同付款、个人付款时,交费账户信息必录");
					return false;
		    	}
		   }
		   if("3"=== premFrom){
	    		if("" === $("#grpPayAmount").val() || null ===$("#grpPayAmount").val()){
	        		lion.util.info("提示", "当录入界面保费来源选择团体个人共同付款,单位交费金额必录");
	    			return false;
	        	}
	    	}
	    }
	   //缴费信息校验
		var seqNoInfo=[];
		var s=0;
	if(null !==accInfoList && accInfoList.length>0){
		var paypct=0;
		for (var i = 0; i < accInfoList.length; i++) {
			if(null!== accInfoList[i].ipsnPayPct && ""!== accInfoList[i].ipsnPayPct){
				if(0>accInfoList[i].ipsnPayPct || 100<accInfoList[i].ipsnPayPct){
					lion.util.info("提示", "个人账户交费比例必须大于0 小于100");
	    			return false;
				}else{
					paypct= Number((paypct+Number(accInfoList[i].ipsnPayPct)).toFixed(2));
				}
			}
			if(isEmpty(accInfoList[i].seqNo,"交费账户信息,账户顺序"))return false;
			
			if(null !== seqNoInfo || seqNoInfo.length>0){
				for (var j = 0; j < seqNoInfo.length; j++) {
					if(accInfoList[i].seqNo===seqNoInfo[j]){
						s++;
					}
				}
			}
	    		seqNoInfo.push(accInfoList[i].seqNo);
			if(isEmpty(accInfoList[i].ipsnPayAmnt,"交费账户信息,个人扣款金额"))return false;
			if(isEmpty(accInfoList[i].bankAccName,"交费账户信息,交费账户"))return false;
			if(isEmpty(accInfoList[i].bankCode,"交费账户信息,交费银行"))return false;
			if(isEmpty(accInfoList[i].bankAccNo,"交费账户信息,交费账号"))return false;     		
		}
		if(paypct!==100 && paypct!==0){
			lion.util.info("提示", "个人账户交费比例如果输入合计必须等于100");
			return false;
		}
		if(s>0){
			lion.util.info("提示", "交费账户信息,账户顺序不允许重复");
			return false;
		}
	}
	//投保人信息校验
	if("L" === com.orbps.contractReview.listimport.cntrType){
		if(isEmpty(listImportPageVo.hldrInfoVo.hldrName,"当前保单为清汇汇交件,投保人姓名"))return false;
		if(isEmpty(listImportPageVo.hldrInfoVo.hldrSex,"当前保单为清汇汇交件,投保人性别"))return false;
		if(isEmpty(listImportPageVo.hldrInfoVo.hldrIdType,"当前保单为清汇汇交件,投保人证件类型"))return false;
		if(isEmpty(listImportPageVo.hldrInfoVo.hldrIdNo,"当前保单为清汇汇交件,投保人证件号码"))return false;
		if(isEmpty(listImportPageVo.hldrInfoVo.hldrBirth,"当前保单为清汇汇交件,投保人出生日期."))return false;
	}
	//要约信息
	if(com.orbps.contractReview.listimport.BsInfoFormList === null || !com.orbps.contractReview.listimport.BsInfoFormList.length > 0){
		lion.util.info("警告","要约信息必填");
	    return false;
	}
	//如果添加了受益人信息，去校验受益人信息的受益份额之和必须要等于100
	if(getAddRowsData.length>0){
        
        for (var i = 0; i < getAddRowsData.length; i++) {
            var array_element = getAddRowsData[i];
            //sumPct = Number((sumPct+Number(array_element.beneAmount)).toFixed(2));
            if(null === array_element.benefitOrder || undefined === array_element.benefitOrder || "" === array_element.benefitOrder){
            	lion.util.info("警告","受益顺序不允许为空");
                return false;
            }
            if(null === array_element.name || undefined === array_element.name || "" === array_element.name){
            	lion.util.info("警告","受益人姓名不允许为空");
                return false;
            }
            if(null === array_element.sex || undefined === array_element.sex || "" === array_element.sex){
            	lion.util.info("警告","受益人性别不允许为空");
                return false;
            }
            if(null === array_element.birthDate || undefined === array_element.birthDate || "" === array_element.birthDate){
            	lion.util.info("警告","受益人出生日期不允许为空");
                return false;
            }else{
            	 var d1 = new Date(array_element.birthDate.replace(/\-/g, "\/"));  
            	 var d2 = new Date();  
            	 if(d1 >=d2)  
            	 {  
            		  lion.util.info("受益人出生日期不能大于当前时间！");  
            	  return false;  
            	 }
            	
            }
            if(null === array_element.relToIpsnTb || undefined === array_element.relToIpsnTb || "" === array_element.relToIpsnTb){
            	lion.util.info("警告","受益人与被保人关系不允许为空");
                return false;
            }
            if(null === array_element.beneAmount || undefined === array_element.beneAmount || "" === array_element.beneAmount){
            	lion.util.info("警告","受益份额不允许为空");
                return false;
            }
            if(null === array_element.idTypeTb || undefined === array_element.idTypeTb || "" === array_element.idTypeTb){
            	lion.util.info("警告","受益人证件类别不允许为空");
                return false;
            }
            if(null === array_element.idNo || undefined === array_element.idNo || "" === array_element.idNo ){
            	lion.util.info("警告","受益人证件号码不允许为空");
                return false;
            }
        }
        var bsInfoVo =[];
        var benefit={};
        for (var i = 0; i < getAddRowsData.length; i++) {
     	   benefit={};
     	   getAddRowsData[i].beneAmount= getAddRowsData[i].beneAmount;
     		if(null!==bsInfoVo && bsInfoVo.length>0){
     			var count=0;
     			for (var j = 0; j < bsInfoVo.length; j++) {
						if(bsInfoVo[j].benefitOrder===getAddRowsData[i].benefitOrder){
							count++;
							bsInfoVo[j].beneAmount=Number((Number(bsInfoVo[j].beneAmount)+Number(getAddRowsData[i].beneAmount)).toFixed(2));
						}
					}
     			if(count===0){
     				benefit.benefitOrder=getAddRowsData[i].benefitOrder;
	            		benefit.beneAmount=Number(getAddRowsData[i].beneAmount);
	            		bsInfoVo.push(benefit);
     			}
     		}else{
     			benefit.benefitOrder=getAddRowsData[i].benefitOrder;
         		benefit.beneAmount=Number(getAddRowsData[i].beneAmount);
         		bsInfoVo.push(benefit);
     		}
     }
        for (var j = 0; j < bsInfoVo.length; j++) {
        	if(100!==bsInfoVo[j].beneAmount){
        		lion.util.info("警告","受益顺序为"+bsInfoVo[j].benefitOrder+"受益份额不足100%");
                return false;
        	}
        }
	}
	return listImportPageVo;
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
	
	 $("#bankCode").combo("refresh");
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
	com.orbps.contractReview.listimport.BsInfoFormList = [];
	com.orbps.contractReview.listimport.insuredCount=0;
	com.orbps.contractReview.listimport.reloadPublicInsuredModalTable();
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
	  $("#beneInfoTb").find("tbody").empty();
	  $("#beneInfoTb").find("tbody").append("<tr><td>无记录</td></tr>");
	  $("#AccInfoTb").find("tbody").empty();
	  $("#AccInfoTb").find("tbody").append("<tr><td>无记录</td></tr>");
}
