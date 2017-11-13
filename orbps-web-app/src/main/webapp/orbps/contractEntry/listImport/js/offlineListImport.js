// 新建contractEntry命名空间com.orbps.contractEntry.listimport.BsInfoFormList
com.orbps.contractEntry = {};
// 新建contractEntry.offlineList命名空间
com.orbps.contractEntry.listimport = {};
// 新建表单id
com.orbps.contractEntry.listimport.offlineListImportForm = $("#offlineListImportForm");
// 新建表格id
com.orbps.contractEntry.listimport.coverageListTb = $("#coverageListTb");
// 新建表单id
com.orbps.contractEntry.listimport.ipsnInfoForm = $("#ipsnInfoForm");
com.orbps.contractEntry.listimport.hldrInfoForm = $("#hldrInfoForm");
com.orbps.contractEntry.listimport.bsInfoForm =$("#bsInfoForm");
// uploadListForm id
com.orbps.contractEntry.listimport.btnModel = $("#uploadListForm");
com.orbps.contractEntry.listimport.listImportBsInfoVo =[];
com.orbps.contractEntry.listimport.listImportSubPolVos = [];
com.orbps.contractEntry.listimport.listImportSubPolVo = {};
// 新建businessKey全局变量
com.orbps.contractEntry.listimport.businessKey;
// 新建taskId全局变量
com.orbps.contractEntry.listimport.taskId;
// 新建要约信息list
com.orbps.contractEntry.listimport.insuredList = [];
com.orbps.contractEntry.listimport.BsInfoFormList = [];
// 新建insuredCount
com.orbps.contractEntry.listimport.insuredCount = 0;
// 新建insuredType
com.orbps.contractEntry.listimport.insuredType = -1;
//新建cntrType全局变量
com.orbps.contractEntry.listimport.cntrType = {};
com.orbps.contractEntry.listimport.data = {};
com.orbps.contractEntry.listimport.appConnIdTypeValue;
com.orbps.contractEntry.listimport.appIdTypeValue;
com.orbps.contractEntry.listimport.contactIdTypeValue={};
com.orbps.contractEntry.listimport.isIdNo;
//上一人，下一人标识
com.orbps.contractEntry.listimport.flag;
//导入进度条展示，全局变量
com.orbps.contractEntry.listimport.pct = 0;
com.orbps.contractEntry.listimport.clearFlag = 0;
com.orbps.contractEntry.listimport.errExportFlag;
//分组类型全局变量，如果是按赔付比例分组，要约中的保额保费允许修改
com.orbps.contractEntry.listimport.noGroupTypeFlag = 0;//无分组
com.orbps.contractEntry.listimport.amountGroupTypeFlag = 0;//保额保费一致分组
com.orbps.contractEntry.listimport.repGroupTypeFlag = 0;//赔付比例一致分组
//基本信息校验规则
com.orbps.contractEntry.listimport.offlineValidateForm = function(vform){
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
//        	//被保险人姓名
//        	name :{
//        		required : true,
//        		zh_verifyl : true
//        	},
        	//被保险人属组号
        	feeGrpNo : {
        		isNum : true
        	},
        	//邮箱
        	email : {
        		email : true
        	},
        	//邮箱
        	phone : {
        		isMobile : true
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
        	    isIdCardNo : "请核对并输入正确的证件号码",
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

//账户顺序抬起事件
$('body').delegate('table#AccInfoTb #seqNo' , 'keyup', function(){
	if(commevent(this)){
		return;
	}
	if(!isNaN(this.value)){
		this.value=/^[1-9]\d*$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-this.value.length)
	}else{
		this.value="";
		};
	$("#seqNo").val(this.value);
});

$('body').delegate('table#AccInfoTb #ipsnPayAmnt' , 'blur', function(){
	var ipsnPayAmnt = $("#ipsnPayAmnt").val();
	//var zh_verify = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
	if(!isNaN(this.value)){
		if(ipsnPayAmnt<0){
			lion.util.info("请输入>=0的交费金额");
			$("#ipsnPayAmnt").val("");
			return;
		}
	}else{
		lion.util.info("警告", "请输入正确的格式");
		$("#ipsnPayAmnt").val("");
		return;
	}
	
	if(!zh_verify.test(ipsnPayAmnt))
		
	return false;
});
$('body').delegate('table#AccInfoTb #ipsnPayPct' , 'blur', function(){
	var ipsnPayPct = $("#ipsnPayPct").val();
	//var zh_verify = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
	if(0>ipsnPayPct || 100<ipsnPayPct){
		lion.util.info("警告", "请输入大于零的个人账户交费比例");
		$("#ipsnPayPct").val("");
		return false;
	}
	
});
$('body').delegate('table#AccInfoTb #ipsnPayPct' , 'keyup', function(){
	if(commevent(this)){
		return;
	}
	if(!isNaN(this.value)){
		this.value=/^[1-9]\d*$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-this.value.length)
	}else{
		this.value="";
		};
		$("#molecule").val(this.value);
});
//$('body').delegate('table#AccInfoTb #bankAccName' , 'blur', function(){
//	var bankAccName = $("#bankAccName").val();
//	var zh_verify = /^[\u4E00-\u9FA5]+$/;
//	if(!zh_verify.test(bankAccName))
//		lion.util.info("警告", "请输入正确的交费账户");
//	$("#bankAccName").val("");
//	return false;
//});
$('body').delegate('table#AccInfoTb #bankAccName' , 'keyup', function(){
	var $nameInput = $(this);
	// 响应鼠标事件，允许左右方向键移动
	//获取光标所在文本中的下标
	var pos = getTxt1CursorPosition(this);
	event = window.event || event;
	if (event.keyCode === 37 | event.keyCode === 39) {
		return;
	}
	// 先把非数字的都替换掉，除了数字和.
	$nameInput.val($nameInput.val().replace(
			/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\.\(\)\（\）\·\-\ ]/g,
			''));
	//移动光标到所定位置
	setCaret(this,pos);
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
	if(commevent(this)){
		return;
	}
	if(!isNaN(this.value)){
		this.value=/^[1-9]\d*$/.test(this.value) ? this.value : this.value.substring(0,this.value.length-this.value.length)
	}else{
		this.value="";
		};
	$("#benefitOrder").val(this.value);
});
//姓名   
$('body').delegate('table#beneInfoTb #name' , 'keyup', function(){
	if(commevent(this)){
		return;
	}
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
			/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\@\.\(\)\（\）\·\-\ ]/g,
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
	if(commevent(this)){
		return;
	}
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
			var s= $("#sex");
			return;
		}else{
			 idcard( $("#beneInfoTb #idTypeTb"),
					 $("#beneInfoTb #idNo"),
					 $("#beneInfoTb #birthDate"),
					 $("#sex"));
		}
	}
});


//查询任务ID回调函数
com.orbps.contractEntry.listimport.successQueryDetail = function (data,args){
	//alert("查询的保单类型"+data.cntrType);
	com.orbps.contractEntry.listimport.cntrType = data.cntrType;
	if("G"===com.orbps.contractEntry.listimport.cntrType){
		lion.util.postjson('/orbps/web/orbps/contractEntry/grp/query',data.taskInfo,com.orbps.contractEntry.successQueryDetail,null,null);		
		$("#hldrInfoForm").hide();//隐藏投保人姓名
		$("#divrelToHldr").hide();//隐藏select框的div
	}else{
		lion.util.postjson('/orbps/web/orbps/contractEntry/sg/query',data.taskInfo,com.orbps.contractEntry.successQueryDetail,null,null);
	}
	if("1"===data.premSource){
		$("#AccInfo").hide();
	}
	com.orbps.contractEntry.listimport.data = data;
	com.orbps.contractEntry.listimport.businessKey = data.taskInfo.bizNo;
	com.orbps.contractEntry.listimport.taskId = data.taskInfo.taskId;
	$('#ipsnInfoFormQuery #applNo').val(com.orbps.contractEntry.listimport.businessKey);
	
	if(null !== data.insuredGroupModalVos && data.insuredGroupModalVos.length>0){
		//有被保人分组，获取分组类型，给全局变量赋值
		if("2" === data.insuredGroupModalVos[0].ipsnGrpType){
			com.orbps.contractEntry.listimport.repGroupTypeFlag = 1;//赔付比例一致分组
			document.getElementById('btnAdd').style.display='none';
			var groupCode = document.getElementById("groupCode");
			for(var i = 0; i < data.insuredGroupModalVos.length; i++){
				var insuredGroupModalVos= data.insuredGroupModalVos[i];
				var groupCodeid = insuredGroupModalVos.ipsnGrpNo;
				var groupCodename=insuredGroupModalVos.ipsnGrpNo;
	        	// 添加属组option
	        	groupCode.options.add(new Option(groupCodeid,groupCodename));	
			}
			$("#bsInfoForm #polCode").attr("disabled",true);
			$("#bsInfoForm #polName").attr("readOnly",true);
			$("#bsInfoForm #subPolCode").attr("disabled",true);
			$("#bsInfoForm #subPolName").attr("readOnly",true);
		}else{
			com.orbps.contractEntry.listimport.amountGroupTypeFlag = 1;//保额保费一致分组
			var groupCode = document.getElementById("groupCode");
			document.getElementById('polDiv').style.display='none';
			document.getElementById('premiumDiv').style.display='none';
			document.getElementById('btnDiv').style.display='none';
			for(var i = 0; i < data.insuredGroupModalVos.length; i++){
				var insuredGroupModalVos= data.insuredGroupModalVos[i];
				var groupCodeid = insuredGroupModalVos.ipsnGrpNo;
				var groupCodename=insuredGroupModalVos.ipsnGrpNo;
	        	// 添加属组option
	        	groupCode.options.add(new Option(groupCodeid,groupCodename));	
			}
			$("#bsInfoForm #subPolCode").attr("disabled",true);
			$("#bsInfoForm #subPolName").attr("readOnly",true);
			$("#bsInfoForm #polCode").attr("disabled",true);
			$("#bsInfoForm #polName").attr("readOnly",true);
			$("#bsInfoForm #groupName").attr("readOnly",true);
			$("#bsInfoForm #amount").attr("readOnly",true);
			$("#bsInfoForm #premium").attr("readOnly",true);
		}
	}else{
		  com.orbps.contractEntry.listimport.noGroupTypeFlag = 1;//无分组
		  var selectpolCode = document.getElementById("polCode");
		    for(var i = 0; i < data.bsInfoVo.length; i++){
		    	//把险种代码绑定到select的id上
		        var id = data.bsInfoVo[i].polCode;
		        //把险种代码绑定到select的value上
		        var name = data.bsInfoVo[i].polCode;
		        //添加option
		        selectpolCode.options.add(new Option(name, id));
		    }
		    document.getElementById('groupDiv').style.display='none';
		  	$("#bsInfoForm #subPolCode").attr("readOnly",true);
			$("#bsInfoForm #polName").attr("readOnly",true);
			$("#bsInfoForm #subPolName").attr("readOnly",true);
	}
};

//清单导入进度查询功能
com.orbps.contractEntry.listimport.showImp = function(){
	var applNo = $("#applNo").val();
	lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/progressQuery",applNo,com.orbps.contractEntry.listimport.progressQuery,null,null);
}

com.orbps.contractEntry.listimport.progressQuery = function(data,args){
	if(null === data){
		return false;
	}else{
		//进度展示
		com.orbps.contractEntry.listimport.errExportFlag = data.taskState;
		var sum = data.thisIpsnNum;
		var imp = data.thisImportNum;
		var err = data.thisErrorNum;
		$("#spanPct").remove();
		com.orbps.contractEntry.listimport.pct=parseInt(((imp+err)/sum)*100);
		$("#progressBar").attr("style","width: "+com.orbps.contractEntry.listimport.pct+"%;");
		$("#progressBar").append("<span id='spanPct'>"+com.orbps.contractEntry.listimport.pct+"%</span>");
		//页面赋值
		$("#listSumNum").val(data.insuredNum);
		$("#listImpNum").val(data.importNum);
		$("#thisListSumNum").val(sum);
		$("#thisListImpNum").val(imp);
		$("#thisListErrNum").val(err);
		if("C" === com.orbps.contractEntry.listimport.errExportFlag){
			lion.util.info("清单导入完成");
			clearInterval(com.orbps.contractEntry.listimport.clearFlag);
		}else if("E" === com.orbps.contractEntry.listimport.errExportFlag){
			lion.util.info("清单导入错误，请导出并修复错误文件");
			clearInterval(com.orbps.contractEntry.listimport.clearFlag)
		}
	}
}

$(function() {
	// 校验函数初始化
	com.orbps.contractEntry.listimport.offlineValidateForm(com.orbps.contractEntry.listimport.ipsnInfoForm);
	// 身份证号验证
    jQuery.validator.addMethod("isIdCardNo", function(value, element) {
    	var appIdTypeValue = $("#ipsnInfoForm #idType").val();
        // 先判断证件类型是否为身份证再去校验,appIdTypeValue(证件类型的值)再各自需要的js里设置为全局变量,
        if (appIdTypeValue === "I") {
        	com.orbps.contractEntry.listimport.isIdNo = this.optional(element) || idCardNoUtil.checkIdCardNo(value);
            return  this.optional(element) || idCardNoUtil.checkIdCardNo(value);
        } else {
            return true;
        }
    }, "请正确输入您的身份证号码！");
    // 如果联系人证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别、年龄等
    $("#ipsnInfoForm #idNo").blur(function() {
    	 var idNo = $("#idNo").val();
    	 if(idNo === null || "" === idNo){
    		 lion.util.info("警告","证件号不能为空");
    		 return false;
    	 }else{
    		 if(com.orbps.contractEntry.listimport.isIdNo){
    			 idcard($("#ipsnInfoForm #idType"),
    					 $("#ipsnInfoForm #idNo"),
    					 $("#ipsnInfoForm #birthDate"),
    					 $("#ipsnInfoForm #ipsursex"));
    	            	checkAge();
    		 }
    	 }
     });
    // 当联系人证件类型改变的 时候，将五要素只读变成可写
    //changFlag用于页面回显时，证件号码发生改变时，判断不进行操作
    var changFlag = 0;
    $("#hldrIdType")
         .change(
                 function() {
                	 var str = $("#hldrIdType").val();
                	 if(str === "I"){
                		 $("#hldrBirth").attr("disabled", true);
                		 $("#hldrSex").attr("readonly", true);
                	 }
                	 //当页面回显时等于 0 1 2 3 直接return false 不做操作。因为回显做改变时会把之前回显的数据给丢失
                	 if(changFlag === 0 ||changFlag === 1 || changFlag === 2 ||changFlag === 3 ){
                		 changFlag ++;
                		 return false;
                	 }
                     if ("I" !== str) {
                         $("#hldrBirth").attr("disabled",false);
                         $("#hldrBirths").attr("disabled", false);
                         $("#hldrSex").attr("readonly",false);
                         $("#hldrBirth").val("");
                         $("#hldrSex").combo("clear");
                         $("#hldrIdNo").val("");
                     }else{
                    	 $("#hldrBirth").attr("disabled", true);
                    	 $("#hldrBirths").attr("disabled", true);
                    	 $("#hldrSex").attr("readonly", true);
                    	 $("#hldrBirth").val("");
                         $("#hldrSex").combo("clear");
                         $("#hldrIdNo").val("");
                     }
                 });
    
    
 // 当联系人证件类型改变的 时候，将五要素只读变成可写
    $("#ipsnInfoForm #idType")
         .change(
                 function() {
                	 var str = $("#ipsnInfoForm #idType").val();
                     if ("I" !== str) {
                         $("#ipsnInfoForm #birthDate").attr("disabled", false);
                         $("#ipsnInfoForm #birthDates").attr("disabled", false);
                         $("#ipsnInfoForm #ipsursex").attr("readonly", false);
                         $("#ipsnInfoForm #birthDate").val("");
//                         $("#ipsnInfoForm #ipsursex").combo("refresh");
                         $("#ipsnInfoForm #idNo").val("");
                     }else{
                    	 $("#ipsnInfoForm #birthDate").attr("disabled",
                                 true);
                    	 $("#ipsnInfoForm #birthDates").attr("disabled",
                                 true);
                         $("#ipsnInfoForm #ipsursex").attr("readonly",
                                 true);
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
    
	//投保单号置灰
	$("#ipsnInfoFormQuery #applNo").attr("readonly",true);
	 $("#ipsnInfoForm #age").attr("disabled",true);
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
	setTimeout(function(){
		//连带保险人下拉框默认显示否
		$("#ipsnInfoForm #joinIpsnFlag").combo("val","N");
		//是否继续导入
		//$("#continueImport").combo("val","N");
		//是否在职下拉框默认显示否
		$("#ipsnInfoForm #onJobFlag").combo("val","Y");
		//是否在职下拉框默认显示否
		$("#ipsnInfoForm #healthFlag").combo("val","N");
	},1000);
//	    var taskInfo = {};
//	    taskInfo.bizNo="2017032855550001";
//	    lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/comback",taskInfo,com.orbps.contractEntry.listimport.successQueryDetail,null,null);
	    var taskInfo = {};
	    var applTaskId = com.ipbps.data;
	    taskInfo.bizNo = applTaskId.substr(0,16);//投保单号
	    taskInfo.taskId= applTaskId.substr(17);//taskId
	    lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/comback",taskInfo,com.orbps.contractEntry.listimport.successQueryDetail,null,null);

    //根据职业代码，设置风险等级
    $("#ipsnInfoForm #occupationalCodes").change(function(){
    	setTimeout(function(){
    		//获取隐藏的风险等级
    		var level = $("#ipsnInfoForm #level").val();
    		$("#ipsnInfoForm #riskLevel").val(level);
    	}, 100);
    });
    
    // 文件上传点击事件
    $('#btnImport').click(function(){
    	if(!confirm("导入前请先确认错误清单已经导出，避免错误文件被覆盖。\n是否要继续导入？")){
    		return false;
    	}
    	com.orbps.contractEntry.listimport.pct = 0;
    	var path = $('#fileupload').val(); 
    	var formData = new FormData($('#uploadListForm')[0]);
    	if("" === path){
    		lion.util.info("请选择要上传的文件");
    		return false;
    	}
    	var status = "";
    	if(document.getElementById("continueImport").checked){
    		status = "2";
    	}else{
    		status = "1";
    	}
    	var applNo = status;
    	applNo = applNo + "," +com.orbps.contractEntry.listimport.businessKey;
    	applNo = applNo + "," +com.orbps.contractEntry.listimport.taskId;
//    	applNo = applNo + ",14234324324,1234";
    	lion.util.info("提示","清单开始导入.........");
    	//显示清单导入进度
    	clearInterval(com.orbps.contractEntry.listimport.clearFlag);
    	com.orbps.contractEntry.listimport.clearFlag = setInterval("com.orbps.contractEntry.listimport.showImp()",10000);
		lion.util.postfile("/orbps/web/orbps/contractEntry/offlineList/upload/"+applNo,formData,function(data, args){
			if("0"==data.retCode){
				lion.util.info("提示",data.errMsg);
				clearInterval(com.orbps.contractEntry.listimport.clearFlag);
			}
		});
    });
//    $("#btnLast").attr("disabled",true);
//	$("#btnNext").attr("disabled",true);
    // 被保险人信息查询
    $('#ipsnInfoFormQuery #btnQuery').click(function(){
    	//重置证件类型flag
    	changFlag = 0;
    	com.orbps.contractEntry.listimport.BsInfoFormList = [];
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
                        com.orbps.contractEntry.listimport.successQuery,
                        null,
                        null);
            }else{
                lion.util.info("提示","客户号或者被保险人编号至少填写一个")
            }
    	}
    });
    // 上一个被保人
    $('#ipsnInfoFormQuery #btnLast').click(function(){
    	//重置证件类型flag
    	changFlag = 0;
    	com.orbps.contractEntry.listimport.BsInfoFormList = [];
    	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
    	var applNo = $('#ipsnInfoForm #applNo').val();
    	if(applNo!==""){
            var ipsnNo = $('#ipsnInfoForm #ipsnNo').val();
            if(ipsnNo!==""){
            	//上一人标识
            	com.orbps.contractEntry.listimport.flag = "s";
            	//清空被保人编号
            	listImportIpsnInfoVo.flag="pre";
            	listImportIpsnInfoVo.ipsnNo = ipsnNo;
                lion.util.postjson(
                        '/orbps/web/orbps/contractEntry/offlineList/queryNextInsured',
                        listImportIpsnInfoVo,
                        com.orbps.contractEntry.listimport.successQuery,
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
    	com.orbps.contractEntry.listimport.BsInfoFormList = [];
    	var listImportIpsnInfoVo = $('#ipsnInfoFormQuery').serializeObject();
    	var applNo = $('#ipsnInfoFormQuery #applNo').val();
    	if(applNo!==""){
    		//var custNo = $('#ipsnInfoFormQuery #custNo').val();
            var ipsnNo = $('#ipsnInfoForm #ipsnNo').val();
            if(ipsnNo!==""){
            	//下一人标识
            	com.orbps.contractEntry.listimport.flag = "x";
            	//清空被保人编号
            	listImportIpsnInfoVo.flag="next";
            	listImportIpsnInfoVo.ipsnNo = ipsnNo;
                lion.util.postjson(
                        '/orbps/web/orbps/contractEntry/offlineList/queryNextInsured',
                        listImportIpsnInfoVo,
                        com.orbps.contractEntry.listimport.successQuery,
                        null,
                        null);
            }else{
                lion.util.info("提示","客户号或者被保险人编号至少填写一个")
            }
    	}
    });
    

    
    // 添加
    $('#submit #btnSubmit').click(function(){
    	 $("#ipsnInfoForm #birthDate").attr("disabled",
                 false);
    	 $("#hldrInfoForm #hldrBirth").attr("disabled",
                 false);
    	if(com.orbps.contractEntry.listimport.ipsnInfoForm.validate().form()){
    		var listImportPageVo = {};
    	if(typeof(experimental(listImportPageVo)) === "boolean") {
    		return false;
    	}else{
    		listImportPageVo=experimental(listImportPageVo);
    	}
        	lion.util.postjson(
                    '/orbps/web/orbps/contractEntry/offlineList/submit',
                    listImportPageVo,
                    com.orbps.contractEntry.listimport.successSubmit,
                    null,
                    null);
     	}
    	
    });
    
    // 删除
    $('#submit #btnDel').click(function(){
    	var listImportIpsnInfoVo = {};
    	listImportIpsnInfoVo = com.orbps.contractEntry.listimport.ipsnInfoForm.serializeObject();
    	listImportIpsnInfoVo.applNo = com.orbps.contractEntry.listimport.businessKey;
    	lion.util.postjson(
                '/orbps/web/orbps/contractEntry/offlineList/delete',
                listImportIpsnInfoVo,
                com.orbps.contractEntry.listimport.successdelete,
                null,
                null);
    });

    // 

    $('#submit #btnEdit').click(function(){
    	 $("#ipsnInfoForm #birthDate").attr("disabled",
                 false);
    	 $("#hldrInfoForm #hldrBirth").attr("disabled",
                 false);
    	if(com.orbps.contractEntry.listimport.ipsnInfoForm.validate().form()){
    		var listImportPageVo = {};
        	if(typeof(experimental(listImportPageVo)) === "boolean") {
        		return false;
        	}else{
        		listImportPageVo=experimental(listImportPageVo);
        	}
    	//alert(JSON.stringify(listImportPageVo));
    	lion.util.postjson(
                '/orbps/web/orbps/contractEntry/offlineList/update',
                listImportPageVo,
                com.orbps.contractEntry.listimport.successUpdate,
                null,
                null);
    	}
    });
    //清空所有表单
    $('#submit #btnClear').click(function(){
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
		com.orbps.contractEntry.listimport.BsInfoFormList = [];
		com.orbps.contractEntry.listimport.insuredCount=0;
		com.orbps.contractEntry.listimport.listImportBsInfoVo =[];
		com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
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
		  $("#AccInfoTb").find("tbody").empty();
    });
		
    //停止导清单任务
    $("#btnStopImport").click(function(){
    	var applNo = $("#applNo").val();
    	lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/stopImport",applNo,com.orbps.contractEntry.listimport.stopImport);
    });
    
    // 导出差错
    $('#btnExportError').click(function(){
    	if("C" !== com.orbps.contractEntry.listimport.errExportFlag && "E" !== com.orbps.contractEntry.listimport.errExportFlag){
    		lion.util.info("清单正在导入，请等待清单导入完成后，再导出错误文件");
    		return false;
    	}
	    applNo = com.orbps.contractEntry.listimport.businessKey;
    	lion.util.postjson(
                '/orbps/web/exp/file/exists',
                applNo,
                com.orbps.contractEntry.listimport.successExists,
                null,
                null);
    });
    // 导出团单模板
    $('#btnGrpDownload').click(function(){
    	window.location.href="/orbps/web/orbps/contractEntry/offlineList/downloadTemplate/g";
    });
    
    // 导出清汇模板
    $('#btnSgDownload').click(function(){
    	window.location.href="/orbps/web/orbps/contractEntry/offlineList/downloadTemplate/s";
    });
    
    // 全部录入完成		
    $('#btnAllCommit').click(function(){
    	applNo = com.orbps.contractEntry.listimport.businessKey;
    	applNo = applNo + "," +com.orbps.contractEntry.listimport.taskId;
    	//alert("投保单号"+applNo);
    	var cntrType = com.orbps.contractEntry.listimport.cntrType;
    	var str;
    	if("G"===cntrType){
    		str = "03";//团单的清单导入
    	}else{
    		str = "08";//清汇的清单导入
    	}
    	applNo = applNo +","+str;
    	//alert("拼接的字符串，投保单号+taskId+保单类型"+applNo);
    	lion.util.postjson(
                '/orbps/web/orbps/contractEntry/offlineList/submitAll',
                applNo,
                com.orbps.contractEntry.listimport.successSubmitAll,
                null,
                null);
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

// 根据医保标识选择是否置灰
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

//根据属组回显相应的险种信息以及责任名称
$("#bsInfoForm #groupCode").change(function(){
	//清空险种代码的选项
	var polCodes = document.getElementById("polCode");
	$("#bsInfoForm #polCode").find("option").remove();
	polCodes.options.add(new Option("请选择险种代码",""));
	var polCodeList = [];//用于比较险种代码是否重复
	//清空子责任代码的选项
	var subPolCode = document.getElementById("subPolCode");
	$("#bsInfoForm #subPolCode").find("option").remove();
	subPolCode.options.add(new Option("请先选择险种代码...",""));
	//赋值属组信息
	com.orbps.contractEntry.listimport.BsInfoFormList = [];
	com.orbps.contractEntry.listimport.listImportBsInfoVo=[];
	var insuredVos = {};
	var listImportBsInfoVo = {};
	var listImportSubPolVo = {};
	var name="";
	var groupCode = $("#bsInfoForm #groupCode").val();
	var data = com.orbps.contractEntry.listimport.data;
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
				//添加到列表中
				insuredVos.subPolCode = insuredGroupModalVos.insuranceInfoVos[k].polCode;
				insuredVos.groupCode =insuredGroupModalVos.ipsnGrpNo;
				insuredVos.groupName =insuredGroupModalVos.ipsnGrpName;
				//判断保额保费是否为空，如果为空，则赋值为0
				if("faceAmnt" in insuredGroupModalVos.insuranceInfoVos[k]){
					listImportSubPolVo.amount = insuredGroupModalVos.insuranceInfoVos[k].faceAmnt;
					insuredVos.amount = insuredGroupModalVos.insuranceInfoVos[k].faceAmnt;
				}else{
					listImportSubPolVo.amount = "";
					insuredVos.amount = "";
				}
				if("premium" in insuredGroupModalVos.insuranceInfoVos[k]){
					listImportSubPolVo.premium = insuredGroupModalVos.insuranceInfoVos[k].premium;
					insuredVos.premium = insuredGroupModalVos.insuranceInfoVos[k].premium;
				}else{
					listImportSubPolVo.premium = "";
					insuredVos.premium = "";
				}
				
				for (var j = 0; j < data.bsInfoVo.length; j++) {
					if(insuredGroupModalVos.insuranceInfoVos[k].polCode.substring(0,3) === data.bsInfoVo[j].polCode){
						polName = data.bsInfoVo[j].polName;
						polCode = data.bsInfoVo[j].polCode;
						for (var s = 0; s < data.bsInfoVo[j].listImportSubPolVos.length; s++) {
							if(insuredGroupModalVos.insuranceInfoVos[k].polCode === data.bsInfoVo[j].listImportSubPolVos[s].subPolCode){
								subPolName=data.bsInfoVo[j].listImportSubPolVos[s].subPolName;
							}
						}
						if(""===subPolName){
							subPolName=polName;
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
				//向比对险种的list中赋值险种代码
				polCodeList.push(polCode);
				
				//后台的数据格式的全局变量
				com.orbps.contractEntry.listimport.listImportBsInfoVo.push(listImportBsInfoVo);
				com.orbps.contractEntry.listimport.BsInfoFormList.push(insuredVos);
				com.orbps.contractEntry.listimport.insuredCount++;
			}
			
		}else{
			$("#bsInfoForm #groupName").val("");
		}
	}
	//定义polCodeListNew，用于存放下拉框选择的险种代码
	var polCodeListNew = [];
	//调用数组去重方法，将polCodeList去重，将去重后的list赋值给polCodeListNew
	polCodeListNew = com.orbps.contractEntry.listimport.removeDuplicatedItem(polCodeList);
	//循环polCodeListNew，遍历属组，给险种下拉框添加option
	for(var n=0;n<polCodeListNew.length;n++){
		polCodes.options.add(new Option(polCodeListNew[n],polCodeListNew[n]));
	}
	//险种名称赋值
	$("#bsInfoForm #groupName").val(name);
	com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
});



// 根据险种代码选择相应的责任名称
$("#bsInfoForm #polCode").change(function(){
	var subPolCode = document.getElementById("subPolCode");
	$("#bsInfoForm #subPolCode").find("option").remove();
	// 添加责任option
	subPolCode.options.add(new Option("请先选择险种代码...",""));
	var polCode = $("#bsInfoForm #polCode").val();   //获取险种名称
	var polName = "";                               //定义险种名称
	var data = com.orbps.contractEntry.listimport.data;  //分组 险种 责任信息
	for(var i = 0; i < data.bsInfoVo.length; i++){
		if(polCode === data.bsInfoVo[i].polCode){  //比较险种是否相同 相同则获取 name 和责任
			$("#bsInfoForm #subPolCode").attr("readOnly",false);   //责任可选择
			polName=data.bsInfoVo[i].polName;
			//判断是否有子责任，如果没有子责任，将险种代码赋值为子责任代码    
			if(data.bsInfoVo[i].listImportSubPolVos.length>0){
				//绑定责任信息
				 for(var j = 0; j < data.bsInfoVo[i].listImportSubPolVos.length ; j++ ){
					// 把责任代码绑定到select的id上
			        	var subPolCodeid = data.bsInfoVo[i].listImportSubPolVos[j].subPolCode;
			        	// 添加责任option
			        	subPolCode.options.add(new Option(subPolCodeid,subPolCodeid));
				 }
			}else{
				subPolCode.options.add(new Option(polCode,polCode));
			}
			$("#bsInfoForm #subPolName").val("");
		}
	}
	$("#bsInfoForm #polName").val(polName);
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
		if(com.orbps.contractEntry.listimport.cntrType ==="L" && $("#relToHldr").val()==="M"){
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
		}else if(com.orbps.contractEntry.listimport.cntrType ==="L"){
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

// 点击上传清单
$("#btnUploadList").click(function() {
	com.orbps.contractEntry.listimport.btnModel.serializeObject();
});

//导出被保人清单
$("#btnExport").click(function(){
	window.location.href="/orbps/web/orbps/contractReview/offlineList/download/"+com.orbps.contractEntry.listimport.businessKey;
});

// 新增
$("#bsInfoForm #btnAdd").click(function() {
	//下拉框的只读设置为false，否则无法提取信息
	$("#bsInfoForm #polCode").attr("disabled",false);
	$("#bsInfoForm #subPolCode").attr("disabled",false);
	if(com.orbps.contractEntry.listimport.proposalInsuredGroupValidate()){
		var insuredVos =$("#bsInfoForm").serializeObject();
		var listImportBsInfoVo = {};
		var listImportSubPolVo = {};
		listImportSubPolVo.subPolCode = $("#subPolCode").val();
		listImportSubPolVo.subPolName = $("#subPolName").val();
		listImportSubPolVo.groupCode = $("#groupCode").val();
		listImportSubPolVo.groupName = $("#groupName").val();
		listImportSubPolVo.amount = $("#amount").val();
		listImportSubPolVo.premium = $("#premium").val();
		listImportBsInfoVo.polCode = $("#polCode").val();
		listImportBsInfoVo.polName = $("#polName").val();
		listImportBsInfoVo.listImportSubPolVos = listImportSubPolVo;
		//后台的数据格式的全局变量
		com.orbps.contractEntry.listimport.listImportBsInfoVo.push(listImportBsInfoVo);
		//将form中取到的数据放到全局变量中重新加载表格
		com.orbps.contractEntry.listimport.BsInfoFormList.push(insuredVos);
		com.orbps.contractEntry.listimport.insuredCount++;
		// 刷新table
		com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
		// form清空
		$("#bsInfoForm input[type='text']").val("");
		$("#bsInfoForm #polCode").find("option[value='']").attr("selected", true); 
		$("#bsInfoForm #subPolCode").find("option[value='']").attr("selected", true); 
		$("#bsInfoForm #groupCode").find("option[value='']").attr("selected", true); 
	}
	//根据分组类型判断是否要将险种下拉框设为只读
	if(1 === com.orbps.contractEntry.listimport.amountGroupTypeFlag || 1 === com.orbps.contractEntry.listimport.repGroupTypeFlag){
		$("#bsInfoForm #polCode").attr("disabled",true);
		$("#bsInfoForm #subPolCode").attr("disabled",true);
	}
});

// 修改
$("#bsInfoForm #btnEdit").click(function() {
	//下拉框的只读设置为false，否则无法提取信息
	$("#bsInfoForm #polCode").attr("disabled",false);
	$("#bsInfoForm #subPolCode").attr("disabled",false);
	if(com.orbps.contractEntry.listimport.proposalInsuredGroupValidate()){
		var insuredBtuVo =$("#bsInfoForm").serializeObject();
		// 表单验证成功后执行
		if (com.orbps.contractEntry.listimport.insuredType > -1) {
			//页面全局变量
			com.orbps.contractEntry.listimport.BsInfoFormList[com.orbps.contractEntry.listimport.insuredType] = insuredBtuVo;
			var listImportBsInfoVo = {};
			var listImportSubPolVo = {};
			listImportSubPolVo.subPolCode = $("#subPolCode").val();
			listImportSubPolVo.subPolName = $("#subPolName").val();
			listImportSubPolVo.groupCode = $("#groupCode").val();
			listImportSubPolVo.groupName = $("#groupName").val();
			listImportSubPolVo.amount = $("#amount").val();
			listImportSubPolVo.premium = $("#premium").val();
			listImportBsInfoVo.polCode = $("#polCode").val();
			listImportBsInfoVo.polName = $("#polName").val();
			listImportBsInfoVo.listImportSubPolVos = listImportSubPolVo;
			com.orbps.contractEntry.listimport.listImportBsInfoVo[com.orbps.contractEntry.listimport.insuredType]=listImportBsInfoVo;
			com.orbps.contractEntry.listimport.insuredType = -1;
		} 
		// 刷新table
		com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
		
		// form清空
		$("#bsInfoForm input[type='text']").val("");
		$("#bsInfoForm #polCode").find("option[value='']").attr("selected", true); 
		$("#bsInfoForm #subPolCode").find("option[value='']").attr("selected", true); 
		$("#bsInfoForm #groupCode").find("option[value='']").attr("selected", true); 
	}
	//根据分组类型判断是否要将险种下拉框设为只读
	if(1 === com.orbps.contractEntry.listimport.amountGroupTypeFlag || 1 === com.orbps.contractEntry.listimport.repGroupTypeFlag){
		$("#bsInfoForm #polCode").attr("disabled",true);
		$("#bsInfoForm #subPolCode").attr("disabled",true);
	}
});


// 删除功能
com.orbps.contractEntry.listimport.publicDeletes = function(vform){
	if(confirm("您确认要删除此条要约信息？")){
		if (com.orbps.contractEntry.listimport.BsInfoFormList !== null && com.orbps.contractEntry.listimport.BsInfoFormList.length > 0) {
			com.orbps.contractEntry.listimport.BsInfoFormList.splice(vform, 1);
			com.orbps.contractEntry.listimport.insuredCount--;
		}
		//删除
		if(com.orbps.contractEntry.listimport.listImportBsInfoVo !== null && com.orbps.contractEntry.listimport.listImportBsInfoVo.length > 0){
			com.orbps.contractEntry.listimport.listImportBsInfoVo.splice(vform, 1);
		}
		// 刷新table
		com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
		$("#bsInfoForm input[type='text']").val("");
		$("#bsInfoForm #polCode").find("option[value='']").attr("selected", true); 
		$("#bsInfoForm #subPolCode").find("option[value='']").attr("selected", true); 
		$("#bsInfoForm #groupCode").find("option[value='']").attr("selected", true); 
	}
};

// 清空功能
$("#bsInfoForm #btnClear").click(function() {
	$("#bsInfoForm").reset();
	//清空险种列表
	com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
});

//根据职业代码，设置风险等级
$("#ipsnInfoForm #occupationalCodes").change(function(){
	setTimeout(function(){
		//获取隐藏的风险等级
		var level = $("#ipsnInfoForm #level").val();
		$("#ipsnInfoForm #riskLevel").val(level);
	}, 100);
});

// 点击radio，将其数据回显到录入界面
com.orbps.contractEntry.listimport.publicRadio = function(vform) {
	// form清空
	$("#bsInfoForm input[type='text']").val("");
	$("#bsInfoForm #polCode").find("option[value='']").attr("selected", true); 
	$("bsInfoForm #subPolCode").find("option[value='']").attr("selected", true); 
	$("#bsInfoForm #groupCode").find("option[value='']").attr("selected", true); 
	var radioVal;
	//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
	var temp = document.getElementsByName("insuredRad");
	for(var i=0;i<temp.length;i++){
	     if(temp[i].checked){
	    	 radioVal = temp[i].value;
	     }
	}
	com.orbps.contractEntry.listimport.insuredType = radioVal;
	insuredVo= com.orbps.contractEntry.listimport.BsInfoFormList[radioVal];
	setTimeout(function(){
		// 回显
		com.orbps.contractEntry.listimport.showInsured(insuredVo);
	},400);
};

//重新加载险种表格
com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable = function () {
	var str="</td><td ></td></tr>";
	$('#coverageListTb').find("tbody").empty();
	var c=com.orbps.contractEntry.listimport.data; 
	if (com.orbps.contractEntry.listimport.BsInfoFormList !== null && com.orbps.contractEntry.listimport.BsInfoFormList.length > 0) {
		for (var i = 0; i < com.orbps.contractEntry.listimport.BsInfoFormList.length; i++) {
			//表格赋值
			var amount = "";
			var premium = "";
			if("subPolCode" in com.orbps.contractEntry.listimport.BsInfoFormList[i]){
			}else{
				com.orbps.contractEntry.listimport.BsInfoFormList[i].subPolCode = "";
			}
			if("subPolName" in com.orbps.contractEntry.listimport.BsInfoFormList[i]){
			}else{
				com.orbps.contractEntry.listimport.BsInfoFormList[i].subPolName = "";
			}
			if("groupCode" in com.orbps.contractEntry.listimport.BsInfoFormList[i]){
			}else{
				com.orbps.contractEntry.listimport.BsInfoFormList[i].groupCode = "";
			}
			if("groupName" in com.orbps.contractEntry.listimport.BsInfoFormList[i]){
			}else{
				com.orbps.contractEntry.listimport.BsInfoFormList[i].groupName = "";
			}
			if("amount" in com.orbps.contractEntry.listimport.BsInfoFormList[i]){
				amount = com.orbps.contractEntry.listimport.BsInfoFormList[i].amount;
			}
			if("premium" in com.orbps.contractEntry.listimport.BsInfoFormList[i]){
				premium = com.orbps.contractEntry.listimport.BsInfoFormList[i].premium;
			}
			if(isEmpty(com.orbps.contractEntry.listimport.BsInfoFormList[i].groupCode, "")){
				str="</td><td ><a href='javascript:void(0);' onclick='com.orbps.contractEntry.listimport.publicDeletes("+i+");' for='insuredRad' id='insuredDel" 
				+ i + "'>删除</a></td></tr>";
			}
			$('#coverageListTb').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.contractEntry.listimport.publicRadio();' id='insuredRad"
							+ i
							+ "' name='insuredRad' value='"
							+ i
							+ "'></td><td >"
							+ com.orbps.contractEntry.listimport.BsInfoFormList[i].polCode
							+ "</td><td >"
							+ com.orbps.contractEntry.listimport.BsInfoFormList[i].polName
							+ "</td><td >"
							+ com.orbps.contractEntry.listimport.BsInfoFormList[i].subPolCode
							+ "</td><td >"
							+ com.orbps.contractEntry.listimport.BsInfoFormList[i].subPolName
							+ "</td><td >"
							+ com.orbps.contractEntry.listimport.BsInfoFormList[i].groupCode
							+ "</td><td >"
							+ com.orbps.contractEntry.listimport.BsInfoFormList[i].groupName
							+ "</td><td >"
							+ amount
							+ "</td><td >"
							+ premium
							+ str);
		}
	} else {
		$('#coverageListTb').find("tbody").append("<tr><td colspan='10' align='center'>无记录</td></tr>");
	}
}


// 回显表单方法
com.orbps.contractEntry.listimport.showInsured = function(msg){
	//清空option
	$("#subPolCode").empty();
		var data = com.orbps.contractEntry.listimport.data;  //分组 险种 责任信息
		for(var i = 0; i < data.bsInfoVo.length; i++){
			if(msg.polCode === data.bsInfoVo[i].polCode){  //比较险种是否相同 相同则获取 name 和责任
				//判断险种是否有子责任，如果没有子责任，将险种代码赋值为子责任代码
				if(data.bsInfoVo[i].listImportSubPolVos.length>0){
					//绑定责任信息
					 for(var j = 0; j < data.bsInfoVo[i].listImportSubPolVos.length ; j++ ){
						// 把责任代码绑定到select的id上
				        	var subPolCodeid = data.bsInfoVo[i].listImportSubPolVos[j].subPolCode;
				        	// 添加责任option
				        	subPolCode.options.add(new Option(subPolCodeid,subPolCodeid));
					 }
				}else{
					subPolCode.options.add(new Option(msg.polCode,msg.polCode));
				}
			}
		}
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
	            if(type==='checkbox'){
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

// 添加成功回调函数
com.orbps.contractEntry.listimport.successSubmit = function (data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示", "添加成功");
    }else{
        lion.util.info("提示", "添加失败，失败原因："+data.errMsg);
    }
}

//修改成功回调函数
com.orbps.contractEntry.listimport.successUpdate = function (data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示", "修改成功");
    }else{
        lion.util.info("提示", "修改失败，失败原因："+data.errMsg);
    }
}


//错误清单导出回调函数
com.orbps.contractEntry.listimport.successExists = function (data,arg){
	if (data === "1"){
		window.location.href="/orbps/web/exp/file/download/"+applNo;
	}else{
		lion.util.info("提示", "导出失败，失败原因：错误清单文件不存在");
	}
}

//停止导清单任务回调函数
com.orbps.contractEntry.listimport.stopImport = function(data,arg){
	if("1" === data.retCode){
		lion.util.info("正在停止导清单批作业，请稍等。。。");
	}else{
		lion.util.info(data.errMsg);
	}
}

// 全部录入完成回调函数
com.orbps.contractEntry.listimport.successSubmitAll = function (data,arg){
	 if (data.retCode==="1"){
	        lion.util.info("提示", "全部录入完成操作成功");
	    }else{
	        lion.util.info("提示", "全部录入失败，失败原因："+data.errMsg);
	    }
}

// 删除成功回调函数
com.orbps.contractEntry.listimport.successdelete = function (data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示", "删除成功");
    }else{
        lion.util.info("提示", "删除失败，失败原因："+data.errMsg);
    }
}

//查询成功回调函数
com.orbps.contractEntry.listimport.successQuery = function (data,arg){
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
	    com.orbps.contractEntry.listimport.listImportBsInfoVo = data.bsInfoVo;
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
	    		com.orbps.contractEntry.listimport.BsInfoFormList.push(BsInfoFormList);
	    	}
	    }
	    // 重新加载险种表格
	    com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
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
		if(com.orbps.contractEntry.listimport.flag === "s"){
			lion.util.info("提示","当前被保险人已经是第一个人了！");
		}else if(com.orbps.contractEntry.listimport.flag === "x"){
			lion.util.info("提示","当前被保险人已经是最后一人了！");
		}
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

//逻辑同上
$("#bsInfoForm #subPolCode").change(function(){
	 var subPolCode = $("#bsInfoForm #subPolCode").val();
	 var data = com.orbps.contractEntry.listimport.data;
	 for(var i = 0; i < data.bsInfoVo.length; i++){
	 	//判断险种是否有子责任，如果没有子责任，将险种名称赋值为子责任的名称
		 if(subPolCode === data.bsInfoVo[i].polCode){
			 $("#bsInfoForm #subPolName").val(data.bsInfoVo[i].polName);
			 return false;
		 }
		 for(var j = 0; j < data.bsInfoVo[i].listImportSubPolVos.length ; j++ ){
			 if(subPolCode === data.bsInfoVo[i].listImportSubPolVos[j].subPolCode){
				 var subPolName = data.bsInfoVo[i].listImportSubPolVos[j].subPolName;
				 $("#bsInfoForm #subPolName").val(subPolName);
				 return false;
			 }else{
				 $("#bsInfoForm #subPolName").val("");
			 }
		 } 
	 }
});

// 要约险种信息校验
com.orbps.contractEntry.listimport.proposalInsuredGroupValidate = function (){
	var polCode = $("#bsInfoForm #polCode").val();
	if(polCode === null || "" === polCode){
		lion.util.info("警告","请选择险种代码");
		return false;
	}
	var subPol=0;
	var data = com.orbps.contractEntry.listimport.data;
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
	var listType = com.orbps.contractEntry.listimport.cntrType;
	if("G" === listType){
		com.ipbps.menuFtn("orbps/orbps/contractEntry/grpInsurAppl/html/grpInsurApplForm.html",""); 
	}else if("L" === listType){
		com.ipbps.menuFtn("orbps/orbps/contractEntry/sgGrpInsurAppl/html/sgGrpInsurAppl.html",""); 
	}
		
});


//回显保存录入信息
com.orbps.contractEntry.successQueryDetail =function(data,arg){
	if("G" === com.orbps.contractEntry.listimport.cntrType){
		com.orbps.contractEntry.grpInsurAppl = data;
	}else{
		com.orbps.contractEntry.sgGrpInsurAppl = data;
	}
    var premFrom="G" === com.orbps.contractEntry.listimport.cntrType?com.orbps.contractEntry.grpInsurAppl.payInfoVo.premFrom:com.orbps.contractEntry.sgGrpInsurAppl.sgGrpPayInfoVo.premFrom;
    
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

function keyupName(id,name){
		var $nameInput = $(name);
		// 响应鼠标事件，允许左右方向键移动
		event = window.event || event;
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


//判断数字是否大于0.01  当被保险人为连带被保人，金额可以等于0，其余情况，金额必须大于0
function checknumFlag(obj){ 
	var joinIpsnFlag=$("#joinIpsnFlag").val();
	if("N" === joinIpsnFlag){
		if(parseFloat(obj.value)<0.01){
			obj.value = '';
		}
	}else{
		if(parseFloat(obj.value)===0.00 || parseFloat(obj.value)===0.0){
			obj.value = 0;
		}
	}
	
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
	var listImportPageVo = {};
	var getAddRowsData = $("#beneInfoTb").editDatagrids("getRowsData");
	var accInfoList = $("#AccInfoTb").editDatagrids("getRowsData");//缴费账户信息
	listImportPageVo.ipsnInfoVo = com.orbps.contractEntry.listimport.ipsnInfoForm.serializeObject();
	//如果为团单，则不对投 保人信息做序列化
	if("G"!==com.orbps.contractEntry.listimport.cntrType){
		listImportPageVo.hldrInfoVo = com.orbps.contractEntry.listimport.hldrInfoForm.serializeObject();
	}
	listImportPageVo.ipsnInfoVo.applNo = com.orbps.contractEntry.listimport.businessKey;
	listImportPageVo.bsInfoVo = com.orbps.contractEntry.listimport.listImportBsInfoVo;
	listImportPageVo.beneficiaryInfo = getAddRowsData;
	listImportPageVo.accInfoList = accInfoList;//缴费账户信息
	
	
	
	var getAddRowsData = $("#beneInfoTb").editDatagrids("getRowsData");
	var accInfoList = $("#AccInfoTb").editDatagrids("getRowsData");
	listImportPageVo.ipsnInfoVo = com.orbps.contractEntry.listimport.ipsnInfoForm.serializeObject();  
	//如果为团单，则不对投保人信息做序列化
	if("G"!==com.orbps.contractEntry.listimport.cntrType){
		listImportPageVo.hldrInfoVo = com.orbps.contractEntry.listimport.hldrInfoForm.serializeObject();
		//清汇 与投保人关系  清单汇交件必填，团单可以不填
		if(null === listImportPageVo.ipsnInfoVo.relToHldr || listImportPageVo.ipsnInfoVo.relToHldr === undefined ){
			lion.util.info("提示", "清单汇交件时，与投保人关系为必填项");
			return false;
		}
	}
	if(isEmpty(listImportPageVo.ipsnInfoVo.name,"被保险人姓名")) return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.idType,"证件类型")) return false;
	if(isEmpty(listImportPageVo.ipsnInfoVo.joinIpsnFlag,"是否连带被保险人")) return false;
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
	if("G" === com.orbps.contractEntry.listimport.cntrType
			&&null !== com.orbps.contractEntry.grpInsurAppl.organizaHierarModalVos
			&& com.orbps.contractEntry.grpInsurAppl.organizaHierarModalVos.length > 0){
		var groupLevelCode=$("#groupLevelCode").val();
		if(null === groupLevelCode ||  groupLevelCode === ""){
			lion.util.info("提示", "有组织架构树时，组织层次代码为必填项");
			return false;
		}
		var count=0;
		for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.organizaHierarModalVos.length; i++) {
			if(groupLevelCode === com.orbps.contractEntry.grpInsurAppl.organizaHierarModalVos[i].levelCode){
				count++;
			}
		}
		if(count === 0){
			lion.util.info("提示", "组织层次代码必须属于已录入的组织架构树");
			return false;
		}
		
	}
	//如果为团单 “收费属组号” 有收付费组时必填，该号码必须属于已录入的收付费组 
	if("G" === com.orbps.contractEntry.listimport.cntrType
			&& null !== com.orbps.contractEntry.grpInsurAppl.chargePayGroupModalVos
			&& com.orbps.contractEntry.grpInsurAppl.chargePayGroupModalVos.length > 0){
		var feeGrpNo=$("#feeGrpNo").val();
		
		if(null === feeGrpNo || feeGrpNo === ""){
			lion.util.info("提示", "有收付费组时，收费属组号为必填项");
			return false;
		}
		var count=0;
	
		for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.chargePayGroupModalVos.length; i++) {
			if(parseInt(feeGrpNo) === com.orbps.contractEntry.grpInsurAppl.chargePayGroupModalVos[i].groupNo){
				count++;
			}
		}
		if(count === 0){
			lion.util.info("提示", "收费属组号必须属于已录入的收付费组号");
			return false;
		}
		
	}
	//获取保费来源1.团体账户付款；2.个人账户付款 3.团体个人共同付款。
	
    var premFrom=com.orbps.contractEntry.listimport.data.premSource;
    //当录入的类型是收付费分组时，校验就会有问题。
    if("2" === premFrom || "3"=== premFrom){
    	  //当收付费属组号是空的时候去校验缴费账户信息。
    	 if($("#feeGrpNo").val() === ""){
    		 if("" === $("#charge").val() || null ===$("#charge").val()){
    	    		lion.util.info("提示", "当录入界面保费来源选择团体个人共同付款、个人付款时,个人交费金额必录");
    				return false;
    	    	}
    	    	if(null !==accInfoList && !accInfoList.length>0 ){
    	    		lion.util.info("提示", "当录入界面保费来源选择团体个人共同付款、个人付款时,缴费账户信息必录");
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

	
	
//判断缴费账户信息相关
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
		if(isEmpty(accInfoList[i].seqNo,"缴费账户信息,账户顺序"))return false;
		if(null !== seqNoInfo && seqNoInfo.length>0){
			for (var j = 0; j < seqNoInfo.length; j++) {
					if(accInfoList[i].seqNo===seqNoInfo[j]){
						s++;
					}
			}
		}
    	seqNoInfo.push(accInfoList[i].seqNo);
		if(isEmpty(accInfoList[i].ipsnPayAmnt,"缴费账户信息,个人扣款金额"))return false;
		if(isEmpty(accInfoList[i].bankAccName,"缴费账户信息,缴费账户"))return false;
		if(isEmpty(accInfoList[i].bankCode,"缴费账户信息,缴费银行"))return false;
		if(isEmpty(accInfoList[i].bankAccNo,"缴费账户信息,缴费账号"))return false;     		
	}
	if(paypct!==100 && paypct!==0){
		lion.util.info("提示", "个人账户交费比例如果输入合计必须等于100");
		return false;
	}
	if(s>0){
		lion.util.info("提示", "缴费账户信息,账户顺序不允许重复");
		return false;
	}
}

//投保人信息验证
if("L" === com.orbps.contractEntry.listimport.cntrType){
	if(isEmpty(listImportPageVo.hldrInfoVo.hldrName,"当前保单为清汇汇交件,投保人姓名"))return false;
	if(isEmpty(listImportPageVo.hldrInfoVo.hldrSex,"当前保单为清汇汇交件,投保人性别"))return false;
	if(isEmpty(listImportPageVo.hldrInfoVo.hldrIdType,"当前保单为清汇汇交件,投保人证件类型"))return false;
	if(isEmpty(listImportPageVo.hldrInfoVo.hldrIdNo,"当前保单为清汇汇交件,投保人证件号码"))return false;
	if(isEmpty(listImportPageVo.hldrInfoVo.hldrBirth,"当前保单为清汇汇交件,投保人出生日期."))return false;
}	
if(com.orbps.contractEntry.listimport.BsInfoFormList !== null && com.orbps.contractEntry.listimport.BsInfoFormList.length > 0){
	if( Object.prototype.toString.call(com.orbps.contractEntry.listimport.BsInfoFormList) === '[object Array]'){
	}
}else{
	lion.util.info("警告","要约信息必填");
    return false;
}

	listImportPageVo.ipsnInfoVo.applNo = com.orbps.contractEntry.listimport.businessKey;
	listImportPageVo.bsInfoVo = com.orbps.contractEntry.listimport.listImportBsInfoVo;
	listImportPageVo.beneficiaryInfo = getAddRowsData;//受益人信息
	listImportPageVo.accInfoList = accInfoList;//缴费账户信息
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
        		lion.util.info("警告","受益顺序为"+bsInfoVo[j].benefitOrder+"受益份额不足100%或者超出100%请修改");
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
	com.orbps.contractEntry.listimport.BsInfoFormList = [];
	com.orbps.contractEntry.listimport.insuredCount=0;
	com.orbps.contractEntry.listimport.reloadPublicInsuredModalTable();
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

//数组去重功能
com.orbps.contractEntry.listimport.removeDuplicatedItem = function(ar) {
    var ret = [],
        end;
    ar.sort();
    end = ar[0];
    ret.push(ar[0]);

    for (var i = 1; i < ar.length; i++) {
        if (ar[i] != end) {
            ret.push(ar[i]);
            end = ar[i];
        }
    }
    return ret;
}
