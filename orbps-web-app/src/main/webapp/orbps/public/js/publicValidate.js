$(function(){
	 //参数设置，若用默认值可以省略以下面代
	toastr.options = {
		  "closeButton": false,
		  "debug": false,
		  "positionClass": "toast-top-center",
		  "onclick": null,
		  "showDuration": "0",
		  "hideDuration": "0",
		  "timeOut": "0",
		  "extendedTimeOut": "0",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
	}
});
//非法字符校验
jQuery.validator.addMethod("zh_name", function(value, element) {
	var zh_name=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	return this.optional(element) || (zh_name.test(value));    
}, "请输入合法字符！");
//中文验证10位
jQuery.validator.addMethod("zh_verify", function(value, element) {
	var zh_verify = /^[\u4E00-\u9FA5\uf900-\ufa2d]{2,10}$/;
	return this.optional(element) || (zh_verify.test(value));    
}, "请输入2~10个字的中文！");
//中文验证30位
jQuery.validator.addMethod("zh_verify30", function(value, element) {
	var zh_verify30 = /^[\u4E00-\u9FA5\uf900-\ufa2d]{2,30}$/;
	return this.optional(element) || (zh_verify30.test(value));    
}, "请输入2~30个字的中文！");
//中文验证100位
jQuery.validator.addMethod("zh_verify100", function(value, element) {
	var zh_verify100 = /^[\u4E00-\u9FA5\uf900-\ufa2d]{2,100}$/;
	return this.optional(element) || (zh_verify100.test(value));    
}, "请输入2~100个字的中文！");

//中文和数字验证30位
jQuery.validator.addMethod("zhAndNum_verify30", function(value, element) {
	var zh_verify30 = /^[\u4E00-\u9FA5\uf900-\ufa2d\d\w]{2,30}$/;
	return this.optional(element) || (zh_verify30.test(value));    
}, "请输入2~30个字的中文或者数字！");
//中文和数字验证100位
jQuery.validator.addMethod("zhAndNum_verify100", function(value, element) {
	var zh_verify100 = /^[\u4E00-\u9FA5\uf900-\ufa2d\d\w]{2,100}$/;
	return this.optional(element) || (zh_verify100.test(value));    
}, "请输入2~100个字的中文或者数字！");

//投保单号验证(正整数16位)
jQuery.validator.addMethod("isApplNo", function(value, element) {
	var zh_verify = /^[0-9a-zA-Z]{16}$/g;
	return this.optional(element) || (zh_verify.test(value));    
}, "投保单号为16位字母和数字组合！");
//纳税人识别号验证(正整数15位)
jQuery.validator.addMethod("isTaxIdNo", function(value, element) {
	var zh_verify = /^[0-9]{15}$/;
	return this.optional(element) || (zh_verify.test(value));    
}, "投保单号为16位正整数！");
//机构号验证(正整数6位)
jQuery.validator.addMethod("isBranchNo", function(value, element) {
	var zh_verify = /^[0-9]{6}$/;
	return this.optional(element) || (zh_verify.test(value));    
}, "机构为6位正整数！");
//保单号验证（22位字母数字组合）
jQuery.validator.addMethod("isCntrNo", function(value, element) {
    var isCntrNo = /^[0-9a-zA-Z]{22}$/;
    return this.optional(element) || (isCntrNo.test(value));    
}, "请输入22位字母或数字！");

//医保代码验证（1位字母数字组合）
jQuery.validator.addMethod("isMedicalInsurCode", function(value, element) {
    var isMedicalInsurCode = /^[0-9a-zA-Z]{1}$/;
    return this.optional(element) || (isMedicalInsurCode.test(value));    
}, "请输入1位字母或数字！");

//18位中文和字母验证
jQuery.validator.addMethod("zh_verifyl", function(value, element) {
	var zh_verifyl = /^[\u4E00-\u9FA5\uf900-\ufa2d-\0-9a-zA-Z\()\（）]{2,100}$/;
	return this.optional(element) || (zh_verifyl.test(value));    
}, "请输入2~18个字的中文、字母或数字！");

//汉字、数字、字母、.、括号、·、-、字符间支持一个空格，1到200字符。
jQuery.validator.addMethod("zh_verifyl200", function(value, element) {
	var zh_verifyl = /^[\u4E00-\u9FA5\uf900-\ufa2d-\0-9a-zA-Z\()\（）]{2,200}$/;
	return this.optional(element) || (zh_verifyl.test(value));    
}, "请输入2~18个字的中文、字母或数字！");

//判断整数value是否大于0
jQuery.validator.addMethod("isIntNum", function(value, element) { 
	var isIntNum = /^\+?[0-9]\d*$/;
	return this.optional(element) || (isIntNum.test(value));      
}, "整数必须大于等于0");

//判断整数value是否大于0
jQuery.validator.addMethod("isIntGteZero", function(value, element) { 
	var isIntGteZero = /^\+?[1-9]\d*$/;
	return this.optional(element) || (isIntGteZero.test(value));      
}, "整数必须大于0");

//判断大于0的数字（包含小数）
jQuery.validator.addMethod("contractAmount", function(value, element) { 
	var contractAmount = /^[+]?\d+(\.\d+)?$/;
	return this.optional(element) || (contractAmount.test(value));      
}, "请输入大于0的数"); 

//判断浮点数value是否大于或等于0
jQuery.validator.addMethod("isFloatGteZero", function(value, element) { 
     value=parseFloat(value);      
     return this.optional(element) || value>=0;       
}, "浮点数必须大于或等于0"); 

//判断浮点数value是否大于0且小于100
jQuery.validator.addMethod("isBilityPct", function(value, element) { 
     value=parseFloat(value);      
     return this.optional(element) || (value>0 && value<100);       
}, "占比份额必须大于0且小于100"); 

//字母数字组合验证
jQuery.validator.addMethod("isNumLetter", function(value, element) {
	var isNumLetter = /^[0-9a-zA-Z]*$/g;
	return this.optional(element) || (isNumLetter.test(value));    
}, "请输入字母或数字！");

// 手机号码验证 
jQuery.validator.addMethod("isMobile", function(value, element) { 
	var length = value.length; 
	var isMobile = /^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\d{8}$/;
	return this.optional(element) || (length == 11 && isMobile.test(value)); 
}, "请正确填写您的手机号码");

// 电话号码验证 
jQuery.validator.addMethod("isTel", function(value, element) { 
	var isTel = /^\d{3,4}-?\d{7,9}(\*\d{1,4})?$/; //电话号码格式010-12345678*1234 
	return this.optional(element) || (isTel.test(value)); 
}, "请正确填写您的电话号码");

// 传真号码验证 
jQuery.validator.addMethod("isFax", function(value, element) { 
    var isFax = /^\d{3,4}-?\d{7,9}(\*\d{1,4})?$/;
	return this.optional(element) || (isFax.test(value)); 
}, "请正确填写您的传真号码");
// 邮政编码验证 
jQuery.validator.addMethod("isZipCode", function(value, element) { 
	var isZipCode = /^[0-9]{6}$/; 
	return this.optional(element) || (isZipCode.test(value)); 
}, "请正确填写您的邮政编码");
// 邮箱验证 
jQuery.validator.addMethod("email", function(value, element) { 
	var email = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|\-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	return this.optional(element) || (email.test(value)); 
}, "请正确填写您的邮箱");
// 要约属组编号验证 
jQuery.validator.addMethod("isipsnGrpNo", function(value, element) { 
	var isipsnGrpNo =  /^([1-9]\d{0,2}|999)$/; 
	return this.optional(element) || (isipsnGrpNo.test(value)); 
}, "要约属组编号非空非0,不能重复并且小于1000");
// 银行卡号验证 
jQuery.validator.addMethod("isluhmCheck", function(value, element) { 
	return this.optional(element) || luhmCheck(value); 
}, "请正确填写您的银行卡号");

//全数字验证 
jQuery.validator.addMethod("isNum", function(value, element) { 
	var isNum = /^\d*$/; 
	return this.optional(element) || (isNum.test(value)); 
}, "请您输入全数字，不能包含字符");

// 银行卡号校验
function luhmCheck(bankno){
	if (bankno.length < 16 || bankno.length > 19) {
		//$("#banknoInfo").html("银行卡号长度必须在16到19之间");
		return false;
	}
	var num = /^\d*$/;  //全数字
	if (!num.exec(bankno)) {
		//$("#banknoInfo").html("银行卡号必须全为数字");
		return false;
	}
	//开头6位
	var strBin="10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";    
	if (strBin.indexOf(bankno.substring(0, 2))== -1) {
		//$("#banknoInfo").html("银行卡号开头6位不符合规范");
		return false;
	}
    var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）

    var first15Num=bankno.substr(0,bankno.length-1);//前15或18位
    var newArr=new Array();
    for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组
        newArr.push(first15Num.substr(i,1));
    }
    var arrJiShu=new Array();  //奇数位*2的积 <9
    var arrJiShu2=new Array(); //奇数位*2的积 >9
    
    var arrOuShu=new Array();  //偶数位数组
    for(var j=0;j<newArr.length;j++){
        if((j+1)%2==1){//奇数位
            if(parseInt(newArr[j])*2<9)
            arrJiShu.push(parseInt(newArr[j])*2);
            else
            arrJiShu2.push(parseInt(newArr[j])*2);
        }
        else //偶数位
        arrOuShu.push(newArr[j]);
    }
    
    var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数
    var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数
    for(var h=0;h<arrJiShu2.length;h++){
        jishu_child1.push(parseInt(arrJiShu2[h])%10);
        jishu_child2.push(parseInt(arrJiShu2[h])/10);
    }        
    
    var sumJiShu=0; //奇数位*2 < 9 的数组之和
    var sumOuShu=0; //偶数位数组之和
    var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
    var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和
    var sumTotal=0;
    for(var m=0;m<arrJiShu.length;m++){
        sumJiShu=sumJiShu+parseInt(arrJiShu[m]);
    }
    
    for(var n=0;n<arrOuShu.length;n++){
        sumOuShu=sumOuShu+parseInt(arrOuShu[n]);
    }
    
    for(var p=0;p<jishu_child1.length;p++){
        sumJiShuChild1=sumJiShuChild1+parseInt(jishu_child1[p]);
        sumJiShuChild2=sumJiShuChild2+parseInt(jishu_child2[p]);
    }      
    //计算总和
    sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);
    
    //计算Luhm值
    var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;        
    var luhm= 10-k;
    
    if(lastNum==luhm){
    $("#banknoInfo").html("Luhm验证通过");
    return true;
    }
    else{
    $("#banknoInfo").html("银行卡号必须符合Luhm校验");
    return false;
    }        
}

var idCardNoUtil = {	
		provinceAndCitys : {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		},

		powers : [ "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9",
				"10", "5", "8", "4", "2" ],
		
		parityBit : [ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" ],
		
		genders : {
			male : "男",
			female : "女"
		},
		
		checkAddressCode : function(addressCode) {
			var check = /^[1-9]\d{5}$/.test(addressCode);
			if (!check)
				return false;
			if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
				return true;
			} else {
				return false;
			}
		},
		
		checkBirthDayCode : function(birDayCode) {
			var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/
					.test(birDayCode);
			if (!check)
				return false;
			var yyyy = parseInt(birDayCode.substring(0, 4), 10);
			var mm = parseInt(birDayCode.substring(4, 6), 10);
			var dd = parseInt(birDayCode.substring(6), 10);
			var xdata = new Date(yyyy, mm - 1, dd);
			if (xdata > new Date()) {
				return false;// 生日不能大于当前日期
			} else if ((xdata.getFullYear() == yyyy)
					&& (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
				return true;
			} else {
				return false;
			}
		},
		
		getParityBit : function(idCardNo) {
			var id17 = idCardNo.substring(0, 17);
			var power = 0;
			for (var i = 0; i < 17; i++) {
				power += parseInt(id17.charAt(i), 10)
						* parseInt(idCardNoUtil.powers[i]);
			}
			var mod = power % 11;
			return idCardNoUtil.parityBit[mod];
		},
		
		checkParityBit : function(idCardNo) {
			var parityBit = idCardNo.charAt(17).toUpperCase();
			if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
				return true;
			} else {
				return false;
			}
		},
		
		checkIdCardNo : function(idCardNo) {
			// 15位和18位身份证号码的基本校验
			var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
			if (!check)
				return false;
			// 判断长度为15位或18位
			if (idCardNo.length == 15) {
				return idCardNoUtil.check15IdCardNo(idCardNo);
			} else if (idCardNo.length == 18) {
				return idCardNoUtil.check18IdCardNo(idCardNo);
			} else {
				return false;
			}
		},
		// 校验15位的身份证号码
		check15IdCardNo : function(idCardNo) {
			// 15位身份证号码的基本校验
			var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/
					.test(idCardNo);
			if (!check)
				return false;
			// 校验地址码
			var addressCode = idCardNo.substring(0, 6);
			check = idCardNoUtil.checkAddressCode(addressCode);
			if (!check)
				return false;
			var birDayCode = '19' + idCardNo.substring(6, 12);
			// 校验日期码
			return idCardNoUtil.checkBirthDayCode(birDayCode);
		},
		// 校验18位的身份证号码
		check18IdCardNo : function(idCardNo) {
			// 18位身份证号码的基本格式校验
			var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/
					.test(idCardNo);
			if (!check)
				return false;
			// 校验地址码
			var addressCode = idCardNo.substring(0, 6);
			check = idCardNoUtil.checkAddressCode(addressCode);
			if (!check)
				return false;
			// 校验日期码
			var birDayCode = idCardNo.substring(6, 14);
			check = idCardNoUtil.checkBirthDayCode(birDayCode);
			if (!check)
				return false;
			// 验证校检码
			return idCardNoUtil.checkParityBit(idCardNo);
		},
		formateDateCN : function(day) {
			var yyyy = day.substring(0, 4);
			var mm = day.substring(4, 6);
			var dd = day.substring(6);
			return yyyy + '-' + mm + '-' + dd;
		}

};

//验证护照是否正确
function checknumber(number) {
	var str = number;
	// 在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
	var Expression = /(P\d{7})|(G\d{8})/;
	var objExp = new RegExp(Expression);
	if (objExp.test(str) == true) {
		return true;
	} else {
		return false;
	}
}

//如果证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别
function idcard(applIdType,appIdNo,appBirthday,appGender){
	var appIdNoValue = appIdNo.val();
	if (applIdType.val() != "I") {
		return false;
	}
	//检查身份证校验如果不满足则日期，性别都置空
	var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(appIdNoValue);
	if (!check){
		appGender.combo("refresh");
		appBirthday.val("");
		return false;
	}
	// 判断长度为15位或18位
	if (appIdNoValue.length == 15) {
		if(!idCardNoUtil.check15IdCardNo(appIdNoValue)){
			appGender.combo("refresh");
			appBirthday.val("");
			return false;
		}
	} else if (appIdNoValue.length == 18) {
		if(!idCardNoUtil.check18IdCardNo(appIdNoValue)){
			appGender.combo("refresh");
			appBirthday.val("");
			return false;
		}
	}
	// 获取出生日期
	var birthday = appIdNo.val().substring(6, 10) + "-"
	+ appIdNo.val().substring(10, 12) + "-" + appIdNo.val().substring(12, 14);
	appBirthday.val(birthday);
	//appBirthday.attr("readonly", true);根据业管要求去除出生日期只读功能
	// 获取性别
	if (parseInt(appIdNo.val().substr(16, 1)) % 2 == 1) {
		// 是男则执行代码 ...
		appGender.combo("val", "M");
	} else {
		// 是女则执行代码 ...
		appGender.combo("val", "F");
	}
	//appGender.attr("readonly", true); 根据业管要求去除性别只读功能
	idCardFlag = -1
}
//检查身份证信息满足18周岁需求
function check18Age(applIdType,appIdNo,appBirthday,appGender){
	 var appIdTypeValue = applIdType.val();
     // 先判断证件类型是否为身份证再去校验
     if (appIdTypeValue === "I") {
		var idNo = appIdNo.val();
		var birthYear = parseInt(idNo.substring(6, 10));
		var date = new Date();
		var year = date.getFullYear();
		if((year - birthYear) < 18 ){
			setTimeout( function(){
				appGender.combo("refresh");
				appBirthday.val("");
			},500);
			return false ;
		}
		return true;
	}
}
//校验只能为数字
function numberValidate(form,items){
	if(/^[0-9]*$/.test(form)){                 
		 lion.util.info('提示', items+'只能是数字');
		 return false;
	}
}
//校验数字，并且数字只能保留到小数点后两位
function numberfloatValidata(obj){
	var $amountInput = $(obj);
	// 响应鼠标事件，允许左右方向键移动
	event = window.event || event;
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		return ;
	}
	//获取光标所在文本中的下标
	var pos = getTxt1CursorPosition(obj);
	// 先把非数字的都替换掉，除了数字和.
	$amountInput.val($amountInput.val().replace(/[^\d.]/g, "").
	// 只允许一个小数点
	replace(/^\./g, "").replace(/\.{2,}/g, ".").
	// 只能输入小数点后两位
	replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(
			/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'));
	//移动光标到所定位置
	setCaret(obj,pos);
}
//判断数字是否大于0.01
function checknum(obj){  
	if(parseFloat(obj.value)<0.01){
		obj.value = '';
	}
}
//判断正整数大于等于1的
function checkValueContentByYch(obj){	
	event = window.event || event;
	var pos = getTxt1CursorPosition(obj)
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		return;
	}
	var regExp = /^[1-9]\d*$/;
	if(!regExp.exec(obj.value)){
		obj.value=obj.value.substring(0,obj.value.length-1);
	}	
	setCaret(obj,pos);
}
//判断正整数可以等于0
function checkValueContent(obj){
	var regExp = /^[0-9]\d*$/;
	if(!regExp.exec(obj.value)){
		obj.value='';
	}	 
}
//判断字符长度
function checklength(obj,length){
	// 响应鼠标事件，允许左右方向键移动
	event = window.event || event;
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		return ;
	}
	// 先把非数字的都替换掉，除了数字和.
	var len = obj.value.length;
	var reLen = 0;
	for (var i = 0; i < len; i++) {        
		if (obj.value.charCodeAt(i) < 27 || obj.value.charCodeAt(i) > 126) {
			// 全角    
			reLen += 2;
		} else {
			reLen++;
		}
		if(reLen>length){
			obj.value = obj.value.substring(0,obj.value.length-1);
		}
	}
}

//obj当前对象 ；regular 正则表达式 ；length 允许最大长度如果为null则不调用长度代码 其他则调用 
function commevent(obj,regular,length){
	setCaret(obj,getTxt1CursorPosition(obj));
	event = window.event || event;
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		return true;
	}
	if(undefined !==regular && null !==regular&& "" !== regular){
		obj.value=obj.value.replace(regular,'');
	}
	if(undefined !==length && null !== length && "" !== length){
		checklength(obj,length);
	}
}
//判断字符串中出现的空格，最多只能有一个空格
function onlyOneblank(obj){
	obj.value=obj.value.replace(/^ +| +$/g,'').replace(/ +/g,' ');
}
//不能出现空格
function noBlank(obj){
	event = window.event || event;
	var pos = getTxt1CursorPosition(obj)
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		return true;
	}
	obj.value=obj.value.replace(/^ +| +$/g,'');
	setCaret(obj,pos);
}
//光标移到text 中指定位置    
function setCaret(obj,pos){ 
	//设个超时，因为文本经过正则的时候可能去掉了文本的长度，这个时候再去移动光标的位置就会比预期的要多一位，
	setTimeout(function(){
		//获取到取到对应的from
		var form = obj.form.id;
		//应该将jquery对象转成dom对象。
		var tobj = $('#'+form +" #"+obj.id)[0];
		//因为当输入中文时，此时文本中的value值会多空格，所以，去掉文本中的空格。
		obj.value=obj.value.replace(/\s/gi,'');
		//objTextLength 如果输入文本的长度，和现在文本的长度对比小，那光标的位置就减1
		if(objTextLength > obj.value.length){
			 pos = parseInt(pos)-1
		}
		if(pos<0)
			pos = tobj.value.length;
		if(tobj.setSelectionRange){ //兼容火狐,谷歌
			setTimeout(function(){
				tobj.setSelectionRange(pos, pos);
				tobj.focus();
			}
				,0);
		}else if(tobj.createTextRange){ //兼容IE
			var rng = tobj.createTextRange();
			rng.move('character', pos);
			rng.select();
		}
	},0);
	return true;
	
}
//输入之前的文本长度。
var objTextLength = 0;
//获取input或者文本中的光标的下标
function getTxt1CursorPosition(obj){
	console.log("获取文本长度"+obj.value.length);
	// 响应鼠标事件，允许左右方向键移动
	var event = window.event || obj;
	//当键盘上的左右键 删除键和delete键执行的时候，不做操作。直接停止
	if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
		this.stop();
	}
	//alert(obj.value.length);
	var el = $(obj).get(0);
    var pos = 0;
    if ('selectionStart' in el) {
        pos = el.selectionStart;
    } else if ('selection' in document) {
        el.focus();
        var Sel = document.selection.createRange();
        var SelLength = document.selection.createRange().text.length;
        Sel.moveStart('character', -el.value.length);
        pos = Sel.text.length - SelLength;
    }
    //获取当前文本的长度。
    objTextLength = obj.value.length;
    //如果当前的文本长度等于当前光标的位置，就不去做操作。
    if(pos === objTextLength){
    	this.stop();
    }
    return pos;
}