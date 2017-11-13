package com.newcore.orbps.dao.impl;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.UntilPolicyDao;
import com.newcore.orbps.models.banktrans.MioLog;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.para.StructT;
import com.newcore.orbps.models.service.bo.ApplState;
import com.newcore.orbps.models.service.bo.GcLppPremRateBo;
import com.newcore.orbps.models.service.bo.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.ipms.api.IforceRuleService;
import com.newcore.orbps.service.ipms.api.PolicyDurService;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;



@Repository("untilPolicy")
public class UntilPolicyDaoImpl implements UntilPolicyDao {



	/**
	 * ; JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	PolicyQueryService resulpolicyQueryService;

	@Autowired
	PolicyDurService resulpolicyDurService;

	@Autowired
	IforceRuleService resuliforceRuleService;

	private static Logger logger = LoggerFactory.getLogger(UntilPolicyDaoImpl.class);

	/**
	 *  常量
	 */
	public static final int  FALSE            =0 ;                /*用于判断*/
	public static final int  SUCCESS          =1 ;                /*用于判断*/

	/*计数器*/
	public static final int  COUNTER_CNTR_NO          =1 ;                /*保单号的流水号*/
	public static final int  COUNTER_CG_NO            =2 ;              /*合同组号的流水号*/
	public static final int  COUNTER_SG_NO            =3 ;              /*汇缴事件号的流水号*/
	public static final int  COUNTER_MIO_TX_NO        =4 ;             /*收付费交易号*/
	public static final int  COUNTER_CUST_NO          =7 ;              /*客户号的流水号*/
	public static final int  COUNTER_CASERPT_NO       =8 ;             /*报案流水号*/
	public static final int  COUNTER_CPNST_ARC_NO     =9 ;             /*理赔归档作业号的流水号*/
	public static final int  COUNTER_MTN_ARC_NO       =5 ;              /*保全归档作业号的流水号*/
	public static final int  COUNTER_MTN_NO           =6 ;              /*批单号的流水号(不用)*/
	public static final int  COUNTER_BT_BATCH_NO      =10;            /*转帐批次号*/
	public static final int  COUNTER_ARC_NO           =11;             /*业务档案归档批次号*/
	public static final int  COUNTER_VOU_AGT_NO       =12;           /*台帐协议号*/
	public static final int  COUNTER_VOU_ASS_NO       =13;             /*台帐交接单号*/
	public static final int  COUNTER_APPL_NO          =15;             /*投保单号的流水号*/

	/*保单号子计数器，下列宏定义，只是在计数器函数内部使用*/
	public static final int  COUNTER_PL_CNTR_NO       = 1 ;             /*保单号(个人长期主险)的流水号*/
	public static final int COUNTER_PS_CNTR_NO        = 2 ;            /*保单号(个人短期主险)的流水号*/
	public static final int  COUNTER_CC_CNTR_NO       = 3 ;              /*保单号(个人卡折式主险)的流水号*/
	public static final int  COUNTER_EC_CNTR_NO       = 4 ;            /*保单号(个人短险简易保单)的流水号*/
	public static final int  COUNTER_GL_CNTR_NO       = 5 ;            /*保单号(团体长期主险)的流水号*/
	public static final int  COUNTER_GS_CNTR_NO       = 6 ;             /*保单号(团体短期主险)的流水号*/
	public static final int  COUNTER_OC_CNTR_NO       = 7 ;             /*保单号(老保单主险)的流水号*/
	public static final int  COUNTER_DY_CNTR_NO       = 8 ;             /*保单号(机动)的流水号*/
	public static final int  COUNTER_RC_CNTR_NO       = 9 ;             /*保单号(附险)的流水号*/
	public static final int  COUNTER_TS_CNTR_NO       =16 ;              /*保单号(台帐)的流水号*/


	public static final int SIZE_OF_COUNTER_PARAM_NUM = 10 ;  /*计数器产生规则数*/
	public static final int SIZE_OF_COUNTER_PARAM_LEN = 20 ;  /*计数器规则的长度*/
	public static final int SIZE_OF_COUNTER_NUM       =200 ; /*最多允许请求的计数器数目*/






	/***************************************************
				方法名称:pGetChkChr
				功能描述:生成保单号校验位
				参    数:
					str[]:输入参数
					subChkStr:用于记录 输入参数 char数组的每一个值
					ascStr:赋值后的char数组 的具体值
					cnt:数据转换的中间值。
				返 回 值:校验位 mod_value
				约    束:
				算法描述:
	 ****************************************************/
	@Override
	@SuppressWarnings("null")
	public  int pGetChkChr(char str[] ){
		char subChkStr;
		short ascStr;
		short modValue;
		short cnt = 0;
		//判断输入参数是否为空
		if(str.length<=0){
			return (Short) null;
		}else{
			for (int i = 1; i <= str.length; i++) {
				subChkStr=str[i-1];
				if(     subChkStr=='0' ||subChkStr=='1' ||subChkStr=='2'){ascStr=(short) subChkStr ;}
				else if(subChkStr=='3' ||subChkStr=='4' ||subChkStr=='5'){ascStr=(short) subChkStr ;}
				else if(subChkStr=='6' ||subChkStr=='7' ||subChkStr=='8'){ascStr=(short) subChkStr ;}
				else if(subChkStr=='9'){ascStr=(short) subChkStr ;}
				else if(Character.toUpperCase(subChkStr)=='A'){ascStr=65;}
				else if(Character.toUpperCase(subChkStr)=='B'){ascStr=66;}
				else if(Character.toUpperCase(subChkStr)=='C'){ascStr=67;}
				else if(Character.toUpperCase(subChkStr)=='D'){ascStr=68;}
				else if(Character.toUpperCase(subChkStr)=='E'){ascStr=69;}
				else if(Character.toUpperCase(subChkStr)=='F'){ascStr=70;}
				else if(Character.toUpperCase(subChkStr)=='G'){ascStr=71;}
				else if(Character.toUpperCase(subChkStr)=='H'){ascStr=72;}
				else if(Character.toUpperCase(subChkStr)=='I'){ascStr=73;}
				else if(Character.toUpperCase(subChkStr)=='J'){ascStr=74;}
				else if(Character.toUpperCase(subChkStr)=='K'){ascStr=75;}
				else if(Character.toUpperCase(subChkStr)=='L'){ascStr=76;}
				else if(Character.toUpperCase(subChkStr)=='M'){ascStr=77;}
				else if(Character.toUpperCase(subChkStr)=='N'){ascStr=78;}
				else if(Character.toUpperCase(subChkStr)=='O'){ascStr=79;}
				else if(Character.toUpperCase(subChkStr)=='P'){ascStr=80;}
				else if(Character.toUpperCase(subChkStr)=='Q'){ascStr=81;}
				else if(Character.toUpperCase(subChkStr)=='R'){ascStr=82;}
				else if(Character.toUpperCase(subChkStr)=='S'){ascStr=83;}
				else if(Character.toUpperCase(subChkStr)=='T'){ascStr=84;}
				else if(Character.toUpperCase(subChkStr)=='U'){ascStr=85;}
				else if(Character.toUpperCase(subChkStr)=='V'){ascStr=86;}
				else if(Character.toUpperCase(subChkStr)=='W'){ascStr=87;}
				else if(Character.toUpperCase(subChkStr)=='X'){ascStr=88;}
				else if(Character.toUpperCase(subChkStr)=='Y'){ascStr=89;}
				else if(Character.toUpperCase(subChkStr)=='Z'){ascStr=90;}
				else if(subChkStr=='!'){         			   ascStr=33;}
				else{
					return -1;
				}
				//对4取余，然后按照对应的方法。
				modValue = (short) (i%4);
				if ( modValue==1 ){     cnt = (short) (cnt + ascStr*3);}
				else if (modValue==2 ){ cnt = (short) (cnt + ascStr*1);}
				else if (modValue==3 ){ cnt = (short) (cnt + ascStr*9);}
				else if (modValue==0 ){ cnt = (short) (cnt + ascStr*7);}
			}//对10取余，返回结果
			modValue = (short) (cnt%10);
			return modValue;
		}
	}

	/****************************************************
	 * 功能描述:根据输入不同参数 返回不同的日期格式。
	 * *************************************************/
	@Override
	public  String getTime(int i){
		//输入为1时，显示当前时间 年月日；输入为2时，显示当前时间  时分秒；输入为3时，显示当前时间  月日时分秒；输入为其他参数时，返回完整日期格式。
		if(i==1){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			return df.format(new Date());
		}else if(i==2){
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
			return df.format(new Date());
		}else if(i==3){
			SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");//设置日期格式
			return df.format(new Date());
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			return df.format(new Date());
		}
	}
	/****************************************************
	 * 功能描述:根据输入不同参数 得到不同的校验码，并将得到的校验码放到输入字符串参数的末尾，返回一个新的字符串数据。
	 * 参    数:
	 *	strCntrNo 字符串数据
	 *	inLen	      字符串长度
	 *返 回 值:String
	 * 
	 * *************************************************/
	@Override
	public  String cfsSetCntrNoBit(String strCntrNo,int inLen) {
		char linChkChr =(char) pGetChkChr(strCntrNo.toCharArray());
		linChkChr=(char) (linChkChr+'0');
		String str=strCntrNo.substring(0,strCntrNo.length())+linChkChr;
		return str;
	}

	/***************************************************************************************
	 * 功能描述:计算任意一天,经过指定的月/日/年数后的日期。
	 * 	参数：
	 *  		lstrBeginDate    :起始日期，格式为"YYYY-MM-DD"
	 *	   		llPassCount      :经过的年月日数
	 *	   		lstrFlag         :标志 年-"Y"、月-"M"、日-"D"
	 * 返 回 值: 
	 *	   	String：返回的日期
	 * 约     束:
	 ****************************************************************************************/
	@Override
	public	 String cfcComputeDate(String lstrBeginDate,int llPassCount,String lstrFlag){

		//判断日期合法性，默认合法。如果输入的日期之间间隔符不同 或者 月份日期不符合形式 会自动加减，若输入不是日期 字符，程序默认当前日期。
		//格式化日期
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		//日历
		Calendar c = Calendar.getInstance();
		try {//设置时间为 输入的时间
			c.setTime(df.parse(lstrBeginDate));
		} catch (ParseException e) {
			//若输入不是日期 字符，程序默认当前日期。
			Date date = new Date();
			c.setTime(date);
		} 
		//判断输入数字的长度
		Integer numLen=llPassCount;
		if(numLen.toString().length()<=9){	
			//判断标志，判断输入数据是 年或是 月 或是日。年-"Y"、月-"M"、日-"D"。
			if("Y".equals(lstrFlag)){
				c.add(Calendar.YEAR, llPassCount); // 目前時間加
				return cfcComputeDate(df.format(c.getTime()), 0, "D");    	
			}else if("M".equals(lstrFlag)){
				c.add(Calendar.MONTH, llPassCount); // 目前時間加
				return df.format(c.getTime());  
			}else if("D".equals(lstrFlag)){
				c.add(Calendar.DAY_OF_WEEK, llPassCount); // 目前時間加
				return df.format(c.getTime());  
			}else{
				return "标志输入错误，请重新输入！";
			}
		}else{
			return "输入数字长度不能超过10位，请重新输入！";
		}
	}
	/*********************************************************************
	 * 方法名称:getRelativeDate
	 * 功能描述:计算相对日期
	 * 参    数:
	 *      输入日期:lstrBeginDate
	 *	            相对天数:count
	 * 返 回 值:
	 *		String：返回的日期
	 * 约    束:
	 **********************************************************************/
	@Override
	public  String getRelativeDate(String lstrBeginDate,int count){
		//判断日期合法性，默认合法。如果输入的日期之间间隔符不同 或者 月份日期不符合形式 会自动加减，若输入不是日期 字符，程序默认当前日期。
		//格式化日期
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {//设置时间为 输入的时间
			c.setTime(df.parse(lstrBeginDate));
		} catch (ParseException e) {
			//若输入不是日期 字符，程序默认当前日期。
			Date date = new Date();
			c.setTime(date);
		} 
		//判断输入数字的长度
		Integer a1=count;
		if(a1.toString().length()<=9){	
			c.add(Calendar.DAY_OF_WEEK, count); // 目前時間加
			return df.format(c.getTime());  
		}else{
			return "输入数字长度不能超过10位，请重新输入！";
		}
	}
	/*********************************************************************
				方法名称:cfsGetRelativeDate
				功能描述:计算相对日期	(有日期限制：1899-12-31<==>4712-12-31)
				参    数:
					输入日期:lstrBeginDate
					相对天数:count
				返 回 值:
					String：返回的日期
				约    束:
	 *********************************************************************/
	@Override
	public  String cfsGetRelativeDate(String lstrBeginDate,int count){
		//判断日期合法性，默认合法。如果输入的日期之间间隔符不同 或者 月份日期不符合形式 会自动加减，若输入不是日期 字符，程序默认当前日期。
		//格式化日期
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date d1=new Date();
		Date d2=new Date();
		Date c;
		try {//设置时间为 输入的时间
			String befo="1899-12-31";
			String afte="4712-12-31";
			d1 = df.parse(befo);
			d2 = df.parse(afte);
			c  = df.parse(lstrBeginDate);
		} catch (ParseException e) {
			//若输入不是日期 字符，程序默认当前日期。
			c=new Date();
		} 

		//判断日期是否在合法范围内(有日期限制：1899-12-31<==>4712-12-31
		if(c.before(d1)||c.after(d2)){
			return "输入时间不合法，请重新输入！";
		}else{
			//判断输入数字的长度。因为参数为int类型 故有长度限制
			Integer a1=count;
			if(a1.toString().length()<=9){	
				return df.format(c.getTime()+ count * 24 * 60 * 60 * 1000);  
			}else{
				return "输入数字长度不能超过10位，请重新输入！";
			}
		}
	}
	/************************************************************************
			方法名称:cfsGetCntrNoBit
			功能描述:根据字符串str的第1~in_len位生成第in_len位校验位
			参    数:
					 Str:前17位身份证号码
					 leng:该字符串的长度
			返 回 值:char 第18位身份证号码的校验位
			约    束:
	 **************************************************************************/
	@Override
	public  String cfsGetCntrNoBit(String str , int leng){
		//获得p_get_chk_chr(ch)函数  返回的结果
		int sh=pGetChkChr(str.toCharArray());
		//在结果后 拼接字符‘0’
		String lchChkChr=sh+"0";
		//把获得的新值 赋到str最后，替 换str原来结尾的值
		String strNew=str.substring(0,str.length())+lchChkChr;
		return strNew;

	}
	/***************************************************************************
				函数名称:cfdUpdSwiftNumber
				功能描述:更新流水号
				参    数:
				   tablename：    表名      
				   StructT ：实体类          
				返 回 值:int
	 **************************************************************************/
	@Override
	public	 int cfdUpdSwiftNumber( String  tablename ,StructT structT){
		int counter =0;
		int updateNum;
		//编写查询语句-根据不同表名查询对应的counter值
		String selectcounter = "select counter from "+tablename+"  WHERE gen_branch_no = ? and gen_year= ?";
		if(null==structT||StringUtils.isBlank(tablename)){//判断参数是否为空
			return counter;
		}else{
			try {
				//执行查询语句，获得对应counter值
				updateNum =	  jdbcTemplate.queryForObject(selectcounter, Integer.class, structT.getStrBranch(),structT.getStrSysdate().substring(0, 4));
			} catch (EmptyResultDataAccessException e) {
				logger.info(e.getMessage(),e);
				return 0;
			}
		}
		structT.setiCount(updateNum+1);
		String updatesql;
		//编写修改语句-根据不同表名修改对应的counter值
		updatesql="update "+tablename+" SET counter = ? WHERE gen_branch_no = ? and gen_year= ?";
		//执行修改语句，修改对应值（在原来基础上加一）
		updateNum =	jdbcTemplate.update(updatesql, structT.getiCount(),structT.getStrBranch(),structT.getStrSysdate().substring(0, 4));
		return updateNum;
	}
	/**************************************************************************
		函数名称:cfdInsSwiftNumber
		功能描述:插入流水号
		参    数:
			 tablename：    表名      
			 StructT ：实体类          
		返 回 值:int
	 **************************************************************************/
	@Override
	public  int cfdInsSwiftNumber( String  tablename ,StructT structT){	
		int counter =0;
		int updateNum;
		if(null==structT||StringUtils.isBlank(tablename)){//判断参数是否为空
			return counter;
		}else{
			int counterNum;
			//根据不同表名 给表中count值赋初始值
			if("APPL_NO".equals(tablename)){
				counterNum = 50500001;
			}else if("EC_CNTR_NO".equals(tablename)){
				counterNum = 20000002;
			}else if("GL_CNTR_NO".equals(tablename)){
				counterNum = 2;
			}else if("GS_CNTR_NO".equals(tablename)){
				counterNum = 10500001;
			}else if("RC_CNTR_NO".equals(tablename)){
				counterNum = 30500001;
			}else if("CG_NO".equals(tablename)){
				counterNum = 40500001;
			}else if("COMMON_NO".equals(tablename)){
				/*防止共保协议号与老短险系统重复 序列号从 1000001*/
				counterNum =1000001;
			}else{
				counterNum = 100;
			}
			//编写插入语句：根据不同表，设置不同的缺省值-
			String insertsql = "INSERT INTO "+ tablename +"	(gen_branch_no,gen_year,counter)  VALUES (?,?,?)";
			//执行插入语句，得到影响的行数
			updateNum =	 jdbcTemplate.update(insertsql, structT.getStrBranch(),structT.getStrSysdate().substring(0, 4),counterNum);
			return updateNum;
		}
	}
	/**************************************************************************
			函数名称:cfdSelSwiftNumber
			功能描述:查询流水号
					参    数:
				   tablename：    表名      
				   StructT ：实体类          
			返 回 值:long
	 **************************************************************************/
	@Override
	public  Object cfdSelSwiftNumber( String  tablename ,StructT structT){
		int counter =0;
		Object updateNum =null;
		if(null==structT||StringUtils.isBlank(tablename)){//判断参数是否为空
			return counter;
		}else{//编写查询语句-根据不同表名查询对应的counter值
			String selectsql="select counter from "+ tablename +" WHERE gen_branch_no =? and gen_year=?";
			try {
				//执行查询语句，获得对应counter值
				updateNum =	  jdbcTemplate.queryForObject(selectsql, Object.class, structT.getStrBranch(),structT.getStrSysdate().substring(0, 4));
			} catch (EmptyResultDataAccessException e) {
				logger.info(e.getMessage(),e);
				return null;
			}
			return updateNum;
		}
	}



	@Override
	public RetInfo creatAgreementNo(String mgrBranch){
		RetInfo retInfo = new RetInfo();
		if(Pattern.matches("\\d+",mgrBranch)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String year = sdf.format(new Date()).substring(0, 4);
			StructT structT =new StructT();
			structT.setStrBranch(mgrBranch);
			structT.setStrSysdate(year);
			Object obj=cfdSelSwiftNumber("COMMON_NO", structT);
			if(null==obj){
				//如果没有该条数据就插入一条新的
				cfdInsSwiftNumber("COMMON_NO", structT);
				obj=cfdSelSwiftNumber("COMMON_NO", structT);
			}
			//修改表count字段值（加一）
			cfdUpdSwiftNumber("COMMON_NO", structT);
			//获取计数器值 的长度
			String str=String.valueOf(obj);
			int	lengh=str.length();
			//根据计数器值 的长度，在前面拼接对应长度的数字
			switch (lengh) {
			case 1:
				str="000000"+str;
				break;
			case 2:
				str="00000"+str;
				break;
			case 3:
				str="0000"+str;
				break;
			case 4:
				str="000"+str;
				break;
			case 5:
				str="00"+str;
				break;
			case 6:
				str="0"+str;
				break;
			case 7:
				break;
			default:
				break;
			}
			String agreementNo = mgrBranch + year + str;

			/*获取校验位*/
			String checkCode = getCheckCode(agreementNo);
			if(null == checkCode){
				retInfo.setApplNo(mgrBranch);
				retInfo.setErrMsg("检验位为空!");
				retInfo.setRetCode("0");
				return retInfo;
			}else{	
				String result=agreementNo + checkCode;
				retInfo.setApplNo(result);
				retInfo.setErrMsg("成功！");
				retInfo.setRetCode("1");
				return retInfo;
			}	
		}else{
			retInfo.setApplNo(mgrBranch);
			retInfo.setErrMsg("管理机构号中有非法字符！");
			retInfo.setRetCode("0");
			return retInfo;
		}

	}

	/*获取校验位函数*/
	private String getCheckCode(String agreementNo){
		if(StringUtils.isEmpty(agreementNo)){
			return null;
		}
		int sum = 0;
		int asc_value = 0;
		int mod_value = 0;
		for (int i = 0; i < agreementNo.length(); i++) {
			if(Character.isDigit(agreementNo.charAt(i))){
				asc_value = agreementNo.charAt(i);
			}else if(Character.toUpperCase(agreementNo.charAt(i)) >= 'A' && Character.toUpperCase(agreementNo.charAt(i)) <= 'Z' ){
				asc_value = Character.toUpperCase(agreementNo.charAt(i));
			}else if (agreementNo.charAt(i) == '!') {
				asc_value = agreementNo.charAt(i);
			}else {
				return null;
			}
			mod_value = i%4;
			if(mod_value == 1){
				sum += asc_value * 3;
			}else if (mod_value == 2) {
				sum += asc_value * 1;
			}else if (mod_value == 3) {
				sum += asc_value * 9;
			}else if (mod_value == 0) {
				sum += asc_value * 7;
			}
		}		
		return String.valueOf(sum%10); 
	}


	/***********************************************************************************************
				函数名称:cfoGetCounter
				功能描述:取计数器值
				参    数: 
					StructT
				返 回 值:String
	 **********************************************************************************************/
	@Override
	public  String  cfoGetCounter(StructT structT){
		int  iRet;
		String tableName = null;
		int liCntrCounterCode;
		switch(Integer.valueOf(structT.getiCount())){
		case COUNTER_CNTR_NO:/*保单号的流水号*/ 
			/*取合同子计数器代码*/
			//	liCntrCounterCode=cfs_GetCntrNoSubCounterCode(new_param[2][0], new_param[3][0], new_param[4][0]);//本参数为临时参数 
			liCntrCounterCode=	cfsGetCntrNoSubCounterCode(structT.getStrAppType(), structT.getStrMrType(), structT.getStrInsurDurUnit());
			if (liCntrCounterCode!=FALSE &&  liCntrCounterCode==COUNTER_EC_CNTR_NO) 						{
				//修改表count字段值（加一）
				iRet=cfdUpdSwiftNumber("EC_CNTR_NO", structT);
				//若修改失败，即不存在该条数据
				if (iRet == 0){
					//插入一条新数据
					cfdInsSwiftNumber("EC_CNTR_NO", structT);
				}
				tableName="EC_CNTR_NO";
			}
			else if (liCntrCounterCode!=FALSE &&  liCntrCounterCode==COUNTER_GL_CNTR_NO)/*保单号(团体长期主险)的流水号*/   {
				//修改表count字段值（加一）
				iRet=cfdUpdSwiftNumber("GL_CNTR_NO", structT);
				//若修改失败，即不存在该条数据
				if (iRet == 0){
					//插入一条新数据
					cfdInsSwiftNumber("GL_CNTR_NO", structT);
				}
				tableName="GL_CNTR_NO";
			}
			else if (liCntrCounterCode!=FALSE &&  liCntrCounterCode==COUNTER_GS_CNTR_NO) /*保单号(团体短期主险)的流水号*/   {
				//修改表count字段值（加一）
				iRet=cfdUpdSwiftNumber("GS_CNTR_NO", structT);
				//若修改失败，即不存在该条数据
				if (iRet == 0){
					//插入一条新数据
					cfdInsSwiftNumber("GS_CNTR_NO", structT);
				}
				tableName="GS_CNTR_NO";
			}
			else if (liCntrCounterCode!=FALSE &&  liCntrCounterCode==COUNTER_RC_CNTR_NO)	/*保单号(附险)的流水号*/   {
				//修改表count字段值（加一）
				iRet=cfdUpdSwiftNumber("RC_CNTR_NO", structT);
				//若修改失败，即不存在该条数据
				if (iRet == 0){
					//插入一条新数据
					cfdInsSwiftNumber("RC_CNTR_NO", structT);
				}
				tableName="RC_CNTR_NO";
			}else{//抛异常
				throw new BusinessException("保单号子计数器代码错误！");
			}

			break;
		case COUNTER_CG_NO:/*合同组号的流水号*/ 
			//修改表count字段值（加一）
			iRet=cfdUpdSwiftNumber("CG_NO", structT);
			//若修改失败，即不存在该条数据
			if (iRet == 0){
				//插入一条新数据
				cfdInsSwiftNumber("CG_NO", structT);
			}
			tableName="CG_NO";
			break;	
		case COUNTER_APPL_NO:/*投保单号的流水号*/
			//修改表count字段值（加一）
			iRet=cfdUpdSwiftNumber("APPL_NO", structT);
			//若修改失败，即不存在该条数据
			if (iRet == 0){
				//插入一条新数据
				cfdInsSwiftNumber("APPL_NO", structT);
			}
			tableName="APPL_NO";
			break;
		default:
			break;
		}//end switch
		return String.valueOf(cfdSelSwiftNumber(tableName, structT));
	}





	/*******************************************************************************************************************************
		方法名称:cfcGetCounter
		功能描述:获得计数器值
		参    数:
		    num：                                                     	计数器数量-对应名称
		    parLcpCounterNameChn  	计数器中文名称
		    strBranch  			            机构号
		    strSysdate				日期
		    strApplType				 投保类型
		    strMrType				主附险性质
		    strInsurDurUnit			保险期单位
		    查询计数器参数：    

		                计数器码            计数器中文名称                               宏名                                                                        参数说明
		--------------------------------------------------------------------------------------------------------------------------------   
		      1               保单号的流水号                     COUNTER_CNTR_NO             0-归档机构;1-签单年度;2-投保类型;3-主附险性质;4保险期类型
		      2               合同组号的流水号                 COUNTER_CG_NO			      0-归档机构;1-签单年度
		      3               汇缴事件号的流水号             COUNTER_SG_NO			      0-管理机构;1-审核通过年度
		      15             投保单号的流水号                COUNTER_APPL_NO            0-管理机关;1-系统年度;     ---added by hanwenbing 20090911

		   注：若码与中文名称不匹配，将会返回FAIL。
		       投保类型：
		      	P	标准个人保单
				D	标准联生保单
				F	家庭联合投保单
				C	卡折式合同补录
				T	撕票式合同补录
				G	标准团体投保单
				H	简易团体保单(无清单)
				L	个单集体投保简易A(按类标准团单处理：个人投保有清单，出简易保单A(有清单)
				N	个单集体投保B(按类无清单团单处理：个人投保无清单，出简易保单A(无清单))
				O	老保单个单
				Q	老保单团单
		       主附险性质:
		      M	主险
				R	附险(被保人)
				E	附险(投保人)	
		       保险期类型:
		      	Y	定期(年)
				A	定期(岁)
				W	终身
				S	短期
				O	一次性		
		返 回 值:计数器值
	 * @throws Exception 
	 *************************************************************************************************************************/
	@Override			
	public  String  cfcGetCounter(int num,String parLcpCounterNameChn,String strBranch,String strSysdate,String strApplType,String 
			strMrType,String strInsurDurUnit){
		StructT structT =new StructT();
		/*根据计数器名称，分别调用不同的逻辑*/
		if(COUNTER_CNTR_NO==num){//保单号的流水号
			structT.setStrBranch(strBranch);
			structT.setStrSysdate(strSysdate);
			structT.setStrAppType(strApplType);
			structT.setStrMrType(strMrType);
			structT.setStrInsurDurUnit(strInsurDurUnit);
		}else{//2合同组号的流水号 、汇缴事件号的流水号 、投保单号的流水号
			structT.setStrBranch(strBranch);
			structT.setStrSysdate(strSysdate);
		}
		structT.setiCount(num);
		//获取计数器值
		String	strCounter=cfoGetCounter(structT);
		//非空判断
		if(StringUtils.isBlank(strCounter)){
			return null;
		}else{
			return strCounter;
		}
	}

	/*******************************************************************
		 方法名称:cfsGetCounterCntrNo
		功能描述:保单号的流水号
		参    数:机构号,年
		返 回 值:是否成功标志
		约    束:
	 * @throws Exception 
	 ********************************************************************/
	@Override
	public  String cfsGetCounterCntrNo(String strBranchNo,
			String strYear,
			String applType,
			String mrType,
			String insurDurType	
			){ 
		String str = "";
		//非空判断
		if(StringUtils.isBlank(strBranchNo)&&StringUtils.isBlank(strYear)){
			return null;
		}else{//获得计数器值
			String counter= cfcGetCounter(1,"保单号的流水号",strBranchNo,strYear,applType,mrType,insurDurType);
			//非空判断
			if(StringUtils.isBlank(counter)){
				return null;
			}else{
				//获取计数器值 的长度
				int	lengh=counter.length();
				str=counter;
				//根据计数器值 的长度，在前面拼接对应长度的数字
				switch (lengh) {
				case 1:
					str="9000000"+str;
					break;
				case 2:
					str="900000"+str;
					break;
				case 3:
					str="90000"+str;
					break;
				case 4:
					str="9000"+str;
					break;
				case 5:
					str="900"+str;
					break;
				case 6:
					str="90"+str;
					break;
				case 7:
					str="9"+str;
					break;
				case 8:
					break;
				case 9:
					str=str.substring(1);
					break;
				default:
					break;
				}
			}
			//非空判断
			if(StringUtils.isBlank(str)){
				return null ;
			}else{
				return str;
			}
		}
	}

	/*******************************************************************
		方法名称:cfsGetCounterCgNo
		功能描述:
		参    数:机构号,年
		返 回 值:是否成功标志
		约    束:
	 * @throws Exception 8100  27000  5340 
	 ********************************************************************/
	@Override
	public  String cfsGetCounterCgNo(String strBranchNo,String strYear){
		//非空判断
		if(StringUtils.isBlank(strBranchNo)&&StringUtils.isBlank(strYear)){
			return null;
		}else{//获得计数器值
			String lin_successflag= cfcGetCounter(2,"合同组号的流水号",strBranchNo,strYear.substring(0,4),null,null,null);
			//非空判断
			if(!StringUtils.isBlank(lin_successflag)){
				return lin_successflag ;
			}else{
				return null;
			}
		}
	}

	/**************************************************************************
		方法名称:cfsGetCntrNoSubCounterCode
		功能描述:获得保单号子计数器代码
		参    数:
		      String  parApplType;       投保类型
		      String  parMrType ;        主附险性质
		      String  parInsurDurType;   保险期类型
		返 回 值:保单号子计数器代码
	 **************************************************************************/
	@Override
	public  int cfsGetCntrNoSubCounterCode(String parApplType, String parMrType, String parInsurDurType){
		if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"Y".equals(parInsurDurType)){
			return COUNTER_PL_CNTR_NO;
		}else if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"A".equals(parInsurDurType)){
			return COUNTER_PL_CNTR_NO;	
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"Y".equals(parInsurDurType)){
			return COUNTER_PL_CNTR_NO;
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"A".equals(parInsurDurType)){
			return COUNTER_PL_CNTR_NO;	
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"Y".equals(parInsurDurType)){
			return COUNTER_PL_CNTR_NO;
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"A".equals(parInsurDurType)){
			return COUNTER_PL_CNTR_NO;		
			/*如果投保类型 IN (G,H) & 主附险性质=M & 保险期类型 IN (Y,A,W)，则计数器类型为GL*/   
		}else if(("G".equals(parApplType))
				&&"M".equals(parMrType)
				&&"Y".equals(parInsurDurType)){
			return COUNTER_GL_CNTR_NO;		

		}else if(("G".equals(parApplType))
				&&"M".equals(parMrType)
				&&"A".equals(parInsurDurType)){
			return COUNTER_GL_CNTR_NO;		
		}else if(("H".equals(parApplType))
				&&"M".equals(parMrType)
				&&"Y".equals(parInsurDurType)){
			return COUNTER_GL_CNTR_NO;		

		}else if(("H".equals(parApplType))
				&&"M".equals(parMrType)
				&&"A".equals(parInsurDurType)){
			return COUNTER_GL_CNTR_NO;		
			/*如果投保类型 IN (G,H) & 主附险性质=M & 保险期类型 IN (S,O)，则计数器类型为GS   */
		}else if(("G".equals(parApplType))
				&&"M".equals(parMrType)
				&&"S".equals(parInsurDurType)){
			return COUNTER_GS_CNTR_NO;
		}else if(("G".equals(parApplType))
				&&"M".equals(parMrType)
				&&"O".equals(parInsurDurType)){
			return COUNTER_GS_CNTR_NO;	
		}else if(("G".equals(parApplType))
				&&"M".equals(parMrType)
				&&"C".equals(parInsurDurType)){
			return COUNTER_GS_CNTR_NO;

		}else if(("H".equals(parApplType))
				&&"M".equals(parMrType)
				&&"S".equals(parInsurDurType)){
			return COUNTER_GS_CNTR_NO;
		}else if(("H".equals(parApplType))
				&&"M".equals(parMrType)
				&&"O".equals(parInsurDurType)){
			return COUNTER_GS_CNTR_NO;	
		}else if(("H".equals(parApplType))
				&&"M".equals(parMrType)
				&&"C".equals(parInsurDurType)){
			return COUNTER_GS_CNTR_NO;
			/*如果投保类型 IN (P,D,F) & 主附险性质=M & 保险期类型 IN (S,O)， 则计数器类型为PS */
		}else if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"S".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"O".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"C".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"M".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("P".equals(parApplType))
				&&"M".equals(parMrType)
				&&"D".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"S".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"O".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"C".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"M".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("D".equals(parApplType))
				&&"M".equals(parMrType)
				&&"D".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;	
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"S".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"O".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"C".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"M".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;
		}else if(("F".equals(parApplType))
				&&"M".equals(parMrType)
				&&"D".equals(parInsurDurType)){
			return COUNTER_PS_CNTR_NO;	
			/*如果投保类型=C & 主附险性质=M，则计数器类型为CC*/	
		}else if ("C".equals(parApplType)&&"M".equals(parMrType)){
			return COUNTER_CC_CNTR_NO;
			/*如果投保类型 IN (L, N) & 主附险性质=M，则计数器类型为EC*/    
		}else if (("L".equals(parApplType)||"N".equals(parApplType))&&"M".equals(parMrType)) {
			return COUNTER_EC_CNTR_NO;
			/*如果主附险性质 IN (R, E)，则计数器类型为RC*/
		}else if ("R".equals(parMrType)||"E".equals(parMrType)){
			return COUNTER_RC_CNTR_NO;
		}else {
			return FALSE;
		}
	}



	/************************************************************************
		方法名称:cfsGetDateYear
		功能描述:根据日期字符串str,获取年限前4位数
		参    数:
		 Str:日期字符串
		返 回 值:long 整数类型的日期
		约    束:字符串长度大于等于4
	 **************************************************************************/
	@Override
	public  long cfsGetDateYear(String str){
		//判断字符串长度是否大于等于4
		if(str.length()>=4){
			//获取年限前4位数
			return Long.parseLong(str.substring(0,4));
		}else{
			return 0;
		}
	}



	/************************************************************************
		方法名称:cfsGetCounterApplNo
		功能描述:产生投保单号的流水号（处理逻辑借用合同组号流水号产生规则）
		参    数:机构号,年
		返 回 值:是否成功标志
		约    束:
	 * @throws Exception 
	 ************************************************************************/
	@Override
	public  String cfsGetCounterApplNo(String strBranchNo,String strYear) {
		//非空判断
		if(StringUtils.isBlank(strBranchNo)&&StringUtils.isBlank(strYear)){
			return null;
		}else{
			//获取计数器值
			String	lin_successflag=cfcGetCounter(15,"投保单号的流水号",strBranchNo.substring(0,2),strYear,null,null ,null);
			//非空判断
			if(StringUtils.isBlank(lin_successflag)){
				return null;
			}else{
				//获取计数器值 的长度
				int	lengh=lin_successflag.length();
				String str=lin_successflag;
				//根据计数器值 的长度，在前面拼接对应长度的数字
				switch (lengh) {
				case 1:
					str="9000000"+str;
					break;
				case 2:
					str="900000"+str;
					break;
				case 3:
					str="90000"+str;
					break;
				case 4:
					str="9000"+str;
					break;
				case 5:
					str="900"+str;
					break;
				case 6:
					str="90"+str;
					break;
				case 7:
					str="9"+str;
					break;
				case 8:
					break;
				case 9:
					str=str.substring(1);
					break;
				default:
					break;
				}
				return str;
			}
		}
	}

	/***************************************************************
	 * 方法名称:cfsSetApplNo
	 * 功能说明：生成投保单号流水号
	 * 
	 * @param cntrType
	 * @return 
	 * @throws Exception 
	 ***************************************************************/
	@Override
	public	 String cfsSetApplNo( String cntrType,String salesBranchNo){
		//签单年 
		long strSignYear;	
		//单证号码
		String productCode;
		String   lin_ApplNo_counter;
		//系统当前日期
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy");
		String lstrSysdate=format.format(date); 
		/*签单年*/
		strSignYear=cfsGetDateYear(lstrSysdate);
		/*获取投保单流水号*/				
		lin_ApplNo_counter=cfsGetCounterApplNo(salesBranchNo,lstrSysdate);
		if(StringUtils.isBlank(lin_ApplNo_counter)){
			return null;
		}else{
			//G：团单；L：清汇；
			if("P".equals(cntrType)){
				productCode = "1085";
			}
			else if("G".equals(cntrType)) {
				productCode = "1086";
			}
			else if("L".equals(cntrType)){ 
				productCode = "1087";
			}
			else if("C".equals(cntrType)){
				productCode = "1088";
			}
			else if("1209".equals(cntrType)) {
				//团体保险电子投保单
				productCode = "1209";
			}
			else if("1210".equals(cntrType)){ 
				//建筑工程团体人身意外伤害保险电子投保单
				productCode = "1210";
			}
			else if("1211".equals(cntrType)){
				//汇交申请书（乙）电子投保单
				productCode = "1211";
			}
			else{
				productCode = "1089";
			}	
			/*投保单号 = 拼字符串（四位单证号码 + 2位单证省级代码 + 2位年份 + 8位流水号）*/
			String newa =productCode+String.valueOf(strSignYear).substring(2)+salesBranchNo.substring(0, 2)+lin_ApplNo_counter;		     
			return newa;
		}
	}


	/************************************************************************
		方法名称:cfsSetMCntrNo
		功能描述:生成保单号
		参    数:
		 		String	 applType		保单类型 
				String	 polCode		险种代码
				String	 inSignDate     签单日期
				String 	 mrType		           主附险类型
				String   insurDurType   保险期间类型
				String   mgrBranchNo    管理机构号

		返 回 值:	int		lstrCntrNo 		保单号
		约    束:
	 * @throws Exception 
	 **************************************************************************/
	@Override
	public	 String cfsSetMCntrNo(String applType,String polCode,String	inSignDate,String mgrBranchNo){
		//入参 -签单日期-取前四位
		long ll_signyear=cfsGetDateYear(inSignDate);

		//通过险种去产品组查询对应接口，获得主附险类型/保险期间类型
		String	mrTypeStr =null;
		String	insurDurTypeStr;//http://10.31.58.110:16604/ESB/IPMS/PolicyDurService
		Map<String, Object> polMap=new HashMap<>();
		polMap.put("polCode",polCode);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		//		headerInfo.setOrginSystem("ORBPS");					
		//		headerInfo.getRouteInfo().setBranchNo("120000");
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Map<String, Object> polDurMap = resulpolicyDurService.excute(polMap);
		JSON json = (JSON) JSON.toJSON(polDurMap);
		JSONObject  obj = JSONObject.parseObject(json.toJSONString());	
		String	policyDurBos = obj.getString("policyDurBos");


		JSONArray arr = JSONArray.parseArray(policyDurBos);
		JSONObject ob  = (JSONObject) arr.get(0);//得到json对象
		insurDurTypeStr=ob.getString("durUnit");//A

		CxfHeader polheaderInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		//		polheaderInfo.setOrginSystem("ORBPS");					//<ORISYS>SLBPS</ORISYS>
		//		polheaderInfo.getRouteInfo().setBranchNo("120000");
		HeaderInfoHolder.setOutboundHeader(polheaderInfo);
		Map<String, Object> polBoMap = resulpolicyQueryService.excute(polMap);
		//非空校验
		if(polBoMap == null){
			logger.error("查询险种概要, 没有引用的对象或集合为空.");
		}else{
			//获取主附险类型/保险期间类型
			//JSONObject obj = JSONObject.parseObject(JSON.toJSONString(polBoMap));
			mrTypeStr = JSONObject.parseObject(JSON.toJSONString(polBoMap)).getJSONObject("policyBo").getString("mrCode");
		}
		//生成流水号			  
		String counter=cfsGetCounterCntrNo(mgrBranchNo,inSignDate,applType,mrTypeStr,insurDurTypeStr);
		if(null==counter){
			return null;
		}else{
			//22位保单号 = 拼字符串（签单年，归档分支机构，险种代码，保单流水号，校验位--4位日期+6位机构号+3险种代码+8位的流水号
			String   lstrCntrNo=String.valueOf(ll_signyear)+mgrBranchNo+polCode+counter;
			String  strCntrNo= cfsSetCntrNoBit(lstrCntrNo,lstrCntrNo.length()); 
			return strCntrNo;
		}

	}

	//1--团单 inForceDate获得
	/*********************************************************
	 *  方法名称:getInForceDate
	 *	功能描述:获得生效日期：
	 *					如果生效日类型为“1”，且指定生效日期存在，则生效日期=指定生效日期。
	 *					如果生效日类型不为“1”，且到账日存在，则生效日期=到账日+1天；若到账日不存在，取当前时间。
	 *	参数：
	 *		inforceDateType 生效日类型
	 * 		designForceDate 指定生效日期
	 * 		fundsAcctDate   到账日
	 * 
	 *******************************************************/
	public Date getInForceDate(String inforceDateType,Date designForceDate,String fundsAcctDate){
		Date date=new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		//生效日期 类型 判断：指定生效日 -取要约中生效日类型 1/0  判断：若为1且  指定生效日不为空
		if("1".equals(inforceDateType)&&designForceDate!=null){ //生效日期类型为指定生效日
			return designForceDate;
		}else{
			//取要约中生效日类型 1/0  判断：若为0且  指定生效日不为空 直接返回当前时间
			if(fundsAcctDate==null){
				return date;
			}else{
				try {//取要约中生效日类型 1/0  判断：若为0且  指定生效日 为空 --获取到账日-到账日加一天返回
					return sdf.parse(cfcComputeDate(fundsAcctDate,1,"D"));
				} catch (ParseException e) {
					throw new BusinessException("解析錯誤");
				}
			}
		}
	}





	/*********************************************************
	 *  方法名称:cfcGetAmorFlag
	 *	功能描述:产生分期交费的多期应收
	 *	参数：
	 *		polCode       险种代码
	 * 		insurDurDnit  保险期类型(调产品组获得---Y-年 M-月 D-天 A-岁 C-约定  )
	 * 		insurDur 	      保险期(调产品组获得 )
	 * 		moneyinItrvl  缴费方式(M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期)
	 * 
	 *******************************************************/
	public int cfcGetAmorFlag(String insurDurDnit,  double insurDur ,String moneyinItrvl){
		double moneyinDur =0.0;
		BigDecimal b1 = new BigDecimal(Double.toString(insurDur));
		BigDecimal b2 = new BigDecimal(Double.toString(1));
		BigDecimal b3 = new BigDecimal(Double.toString(4));
		BigDecimal b4 = new BigDecimal(Double.toString(6));
		BigDecimal b5 = new BigDecimal(Double.toString(12));
		double subtr=    b1.multiply(b5).doubleValue();
		BigDecimal b6 = new BigDecimal(Double.toString(subtr));
		//判断缴费方式
		if ("O".equals(moneyinItrvl) || "W".equals(moneyinItrvl) ||"Y".equals(moneyinItrvl) || "X".equals(moneyinItrvl)){
			moneyinDur=0.0;
		}else{
			if ("M".equals(insurDurDnit)){
				switch(moneyinItrvl)
				{
				case "M":
					moneyinDur=b1.divide(b2).doubleValue();
					break;
				case "Q":
					moneyinDur=b1.divide(b3).doubleValue();
					break;
				case "H":
					moneyinDur=b1.divide(b4).doubleValue();
					break;
				default:		      
					moneyinDur=0.0;
				}       
			} 
			if ("Y".equals(insurDurDnit)){
				switch(moneyinItrvl)
				{
				case "M":
					moneyinDur=b6.divide(b2).doubleValue();
					break;
				case "Q":
					moneyinDur=b6.divide(b3).doubleValue();
					break;
				case "H":
					moneyinDur=b6.divide(b4).doubleValue();
					break;
				default:
					moneyinDur=0.0;
					break;
				}
			} //end if
		}//end if
		return (int) moneyinDur;
	}
	/*******************************************************
	 * 
				方法名称:cfsSetSignDate
				功能描述:计算签单日期
				参    数:
		   			生效日期  inForceDate
		   			生效时点  enforcePoint (根据险种去产品组查)
				返 回 值:处理标记 -签单日期
				约    束:
	 *******************************************************/
	public	String cfsSetSignDate(Date inForceDate,int enforcePoint)
	{
		//根据业务要求修改为签单日为生效日的前n(0、1)天
		//去产品组查生效时点（该生效时间点，需从所有险种中取最大的值）
		// 计算相对日期 输入日期，相对天数，返回日期
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		String date=getRelativeDate(sdf.format(inForceDate), -enforcePoint);
		return date;
	}

	public	List<GcLppPremRateBo> getGcLppPremRateBo(int mutipayTime,Integer insurDurD,String insurDurUnit,
			String firPolCode,double sumPremium,Date inForceDate2){
		List<GcLppPremRateBo> gcLppPremRateBoList=new ArrayList<>();
		//根据多期暂缴年数 赋值
		for (int i = 0; i <mutipayTime; i++) {
			//团体保单一次性缴费多期缴费标准
			GcLppPremRateBo gcLppPremRateBo = new GcLppPremRateBo();
			try {
				SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
				Date inForcedate = sdf.parse(cfcComputeDate(sdf.format(inForceDate2), i, "Y"));
				//获得指定保单生效日期
				gcLppPremRateBo.setInForceDate(inForcedate);
			} catch (ParseException e) {
				throw new BusinessException("解析错误！");
			}
			//取保险期间
			gcLppPremRateBo.setInsurDur(insurDurD);
			//取保险期间单位
			gcLppPremRateBo.setInsurDurUnit(insurDurUnit);
			//取第一险种或主险
			gcLppPremRateBo.setPolCode(firPolCode);
			//取总保费
			gcLppPremRateBo.setPremium(sumPremium);
			//放入list集合
			gcLppPremRateBoList.add(gcLppPremRateBo);
		}
		return gcLppPremRateBoList;
	}


	/**retInfo
	 * 功能说明：校验生效前数据校验
	 * 
	 */
	private String checkGrpInsurAppl(GrpInsurAppl grpInsurAppl){
		//非空校验
		StringBuilder sb = new StringBuilder();

		if(StringUtils.isBlank(grpInsurAppl.getFirPolCode())){
			sb.append("[第一主险为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getCntrType())){
			sb.append("[契约形式为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getLstProcType())){
			sb.append("[团体清单标志为空]");
		}
		if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getSignDate()))){
			sb.append("[到账日为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())){
			sb.append("[缴费方式为空]");
		}
		if("L".equals(grpInsurAppl.getCntrType())&&StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getIsMultiPay())){
			sb.append("[是否多期暂缴为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit())){
			sb.append("[缴费期单位为空]");
		}
		//投保人数，时间，保额，保费是基本数据类型，有默认初始值。故没有非空校验。
		if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getInsurDur()))){
			sb.append("[保险期为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getApplState().getInforceDateType())){
			sb.append("[生效日类型为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getApplState().getInsurDurUnit())){
			sb.append("[保险期类型为空]");
		}
		if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getSumPremium()))){
			sb.append("[总保费为空]");
		}
		int lenPolicy=grpInsurAppl.getApplState().getPolicyList().size();
		for (int i = 0; i < lenPolicy; i++) {
			Policy policy=grpInsurAppl.getApplState().getPolicyList().get(i);
			if(StringUtils.isBlank(policy.getPolCode())){
				sb.append("第"+i+"列"+"[险种代码为空]");
			}
		}
		if(StringUtils.isBlank(grpInsurAppl.getMgrBranchNo())){
			sb.append("[管理机构号为空]");
		}
		return sb.toString();
	}

	@Override
	public String contractInforce(String applNo){

		//通过投保单号applNo查询mongodb获取管理机构号 
		Map<String, Object> map=new HashMap<>();
		map.put("applNo", applNo);
		Update update=new Update();
		GrpInsurAppl   grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		MioPlnmioInfo mioPlnmioInfo = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class, map);
		//非空校验
		String checkResult = checkGrpInsurAppl(grpInsurAppl);
		if(mioPlnmioInfo!=null && StringUtils.isBlank(checkResult)){
			//管理机构号
			String mgrBranchNo=grpInsurAppl.getMgrBranchNo();
			ApplState applState=grpInsurAppl.getApplState();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
			//取要约中生效日类型 1/0  判断：若为1且  指定生效日不为空
			//到账日在生效前被临时存放在mongodb中的SignDate字段中
			Date dd=grpInsurAppl.getSignDate();
			String firPolCode=grpInsurAppl.getFirPolCode();
			//获得生效日期
			Date inForceDate=getInForceDate(applState.getInforceDateType(), applState.getDesignForceDate(), String.valueOf(dd));
			grpInsurAppl.setInForceDate(inForceDate);
			Update updateinForceDate=new Update();
			updateinForceDate.set("inForceDate", inForceDate);
			mongoBaseDao.update(GrpInsurAppl.class, map, updateinForceDate);

			//获得签单日期       去产品组查生效时点（该生效时间点，需从所有险种中取最大的值）
			String enforcePoint="0";//
			Map<String, Object> firPolMap=new HashMap<>();
			firPolMap.put("polCode",firPolCode);
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			//			headerInfo.setOrginSystem("ORBPS");					//<ORISYS>SLBPS</ORISYS>
			//			headerInfo.getRouteInfo().setBranchNo("120000");
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			Map<String, Object> firPolResultMap = resuliforceRuleService.excute(firPolMap);
			//非空校验
			if(firPolResultMap == null){
				logger.error("查询生效时点, 生效时点对象或集合为空.");	
			}else{
				JSONObject obj = JSONObject.parseObject(JSON.toJSONString(firPolResultMap));
				//获取生效时点
				enforcePoint = obj.getJSONObject("iforceRuleBo").getString("inforceCode");
			}
			String  signDate =	cfsSetSignDate(inForceDate, Integer.valueOf(enforcePoint));
			//业务规定取流水号为用签单日，strYear为签单日前4位，strBranchNo管理机构号
			String strYear=signDate;
			//8位的流水号
			String counterCgNo  = cfsGetCounterCgNo(mgrBranchNo,strYear);
			//拼组合保单合同号时用签单日cgNo：4位签单年+6位的管理机构号+3位的险种+8位的流水号
			String cgNo=strYear.substring(0, 4)+mgrBranchNo+firPolCode+counterCgNo;
			cgNo= cfsSetCntrNoBit(cgNo,cgNo.length()); //需存入mongdb--cgNo
			grpInsurAppl.setCgNo(cgNo);
			update.set("cgNo", cgNo);
			mongoBaseDao.update(GrpInsurAppl.class, map, update);
			double  insurDurD= grpInsurAppl.getApplState().getInsurDur();
			String insurDurUnit=grpInsurAppl.getApplState().getInsurDurUnit();

			double sumPremium = 0;
			for (MioLog mioLog : mioPlnmioInfo.getMioLogList()) {
				sumPremium +=mioLog.getAmnt();
			}

			//缴费方式
			String	moneyinItrvlStr =grpInsurAppl.getPaymentInfo().getMoneyinItrvl();
			try {
				Update update1=new Update();
				grpInsurAppl.setSignDate(sdf.parse(signDate));
				update1.set("signDate", sdf.parse(signDate));
				mongoBaseDao.update(GrpInsurAppl.class, map, update1);
			} catch (ParseException e1) {
				throw new BusinessException("解析錯誤");
			}
			//险种policy
			int lenPolicy=grpInsurAppl.getApplState().getPolicyList().size();
			//缴费方式：M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期
			int moneyinItrvlMonInt;
			if("M".equals(moneyinItrvlStr)){
				moneyinItrvlMonInt=1;
			}else if("Q".equals(moneyinItrvlStr)){
				moneyinItrvlMonInt=3;
			}else if("H".equals(moneyinItrvlStr)){
				moneyinItrvlMonInt=6;
			}else if("Y".equals(moneyinItrvlStr)){
				moneyinItrvlMonInt=12;
			}else if("W".equals(moneyinItrvlStr)){
				moneyinItrvlMonInt=12;
			}else{
				moneyinItrvlMonInt=1;
			}

			List<Policy> policyList	= new ArrayList<>();
			//遍历险种集合
			for (int i = 0; i < lenPolicy; i++) {
				//险种集合第二个到第n个值
				Policy policyStr=grpInsurAppl.getApplState().getPolicyList().get(i);
				policyStr.setMoneyinItrvlMon(moneyinItrvlMonInt);
				if("M".equals(policyStr.getMrCode())&&!StringUtils.isBlank(cgNo)){
					policyStr.setCntrNo(cgNo);
				}else{
					String cntrNo=cfsSetMCntrNo(grpInsurAppl.getCntrType(), policyStr.getPolCode(), strYear,mgrBranchNo);
					//保单号
					policyStr.setCntrNo(cntrNo);
				}
				policyList.add(policyStr);
			}//end for
			Update updatepolicyList=new Update();
			updatepolicyList.set("applState.policyList", policyList);//
			mongoBaseDao.update(GrpInsurAppl.class, map, updatepolicyList);

			Update update3=new Update();
			//汇缴件号	-根据CntrType契约形式
			if("G".equals(grpInsurAppl.getCntrType())){
				grpInsurAppl.setSgNo(null);
				update3.set("sgNo", null);
			}else if("L".equals(grpInsurAppl.getCntrType())){
				grpInsurAppl.setSgNo(grpInsurAppl.getApplNo());
				update3.set("sgNo", grpInsurAppl.getApplNo());
			}else if("P".equals(grpInsurAppl.getCntrType())){
				//暂不处理
			}else if("C".equals(grpInsurAppl.getCntrType())){
				//暂不处理
			}else{
				logger.error("契约形式参数有误！");
			}
			mongoBaseDao.update(GrpInsurAppl.class, map, update3);
			Update update4=new Update();
			//若保险期类型 为"O"(一次性保险) 合同预计满期日期=生效日期
			if ("O".equals(insurDurUnit)){	
				grpInsurAppl.setCntrExpiryDate(inForceDate);
				update4.set("cntrExpiryDate", inForceDate);
			}else{
				//根据生效日期、投保要约applState下的-保险期间与保险期类型 得出合同预计满期日期
				String aa=    cfcComputeDate(sdf.format(inForceDate), (int)insurDurD, insurDurUnit);
				try {
					grpInsurAppl.setCntrExpiryDate(sdf.parse(aa));
					update4.set("cntrExpiryDate", sdf.parse(aa));
				} catch (ParseException e) {
					throw new BusinessException("解析錯誤");
				}
			}
			mongoBaseDao.update(GrpInsurAppl.class, map, update4);

			Update update5=new Update();
			//将生效日期和组合保单号，插入mongodb库的清单集合中
			Map<String, Object> mapGrpInsured=new HashMap<>();
			mapGrpInsured.put("applNo", grpInsurAppl.getApplNo());
			update5.set("ipsnInForceDate", inForceDate);
			update5.set("cgNo", cgNo);
			mongoBaseDao.updateAll(GrpInsured.class, mapGrpInsured, update5);
			//（保单生效时）当无清单多期时，在帽子中将多期数据发送过去.契约形式/团体清单标志/是否多期暂缴	
			int mutipayTime	= grpInsurAppl.getPaymentInfo().getMutipayTimes();
			if(("Y").equals(grpInsurAppl.getPaymentInfo().getIsMultiPay()) && "L".equals(grpInsurAppl.getCntrType())
					&&0!=mutipayTime&&null!=inForceDate){
				//获取多期暂缴数据
				List<GcLppPremRateBo> gcLppPremRateBoList=getGcLppPremRateBo
						(mutipayTime, (int)insurDurD, insurDurUnit, firPolCode, sumPremium, inForceDate);
				//存入mongodb
				Update updatenewApplState=new Update();
				//新单状态
				updatenewApplState.set("operTrace.newApplState", "14");
				mongoBaseDao.update(GrpInsurAppl.class, map, updatenewApplState);
				//理赔录入类型-写死
				Update update7=new Update();
				update7.set("applState.clDcType","M");
				mongoBaseDao.update(GrpInsurAppl.class, map, update7);
				Update update8=new Update();
				update8.set("gcLppPremRateBoList", gcLppPremRateBoList);
				mongoBaseDao.update(GrpInsurAppl.class, map, update8);//
			}
			//获得分期交费的多期应收--团单+无清单
			//			if("N".equals(grpInsurAppl.getLstProcType()) && "G".equals(grpInsurAppl.getCntrType())){
			//				int amorFlag =cfcGetAmorFlag(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit(), grpInsurAppl.getPaymentInfo().getMoneyinDur(), moneyinItrvlStr);
			//				if(amorFlag>0){
			//					//调李四魁接口
			//					grpInsurAppl.setInForceDate(inForceDate);
			//					//首期数据\
			//					procPlnmioRecRecordService.procPlnmioRecRecord(grpInsurAppl);//---------------------------------------临时放置
			//					//
			//					proPlnmioRecStepService.proPlnmioRecStep(grpInsurAppl); 
			//					//调贾陈--产生实收  方法
			//					
			//					
			//				}
			//			}
			//修改控制表中数据
			//			String	insertsql="insert into CNTR_EFFECT_CTRL "
			//					+ "(CG_NO,BATCH_NO,CNTR_TYPE,IPSN_NUM,SUM_FACE_AMNT,SUM_PREMIUM,"
			//					+ "PROC_STAT,LST_PROC_TYPE,PROC_CAUSE_DESC,APPL_NO,MG_BRANCH,POL_CODE,MiOLOG_DATE,ID) "
			//					+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";
			//			jdbcTemplate.update(insertsql, 
			//					" ", 
			//					"0",
			//					" ",
			//					1,
			//					1.0,
			//					1.0,
			//					"0",
			//					" ",
			//					"产生实收数据",
			//					applNo,
			//					" ",
			//					" ",
			//					MiOLOG_DATE);
			//			"");
		}else{//非空校验
			logger.error(checkResult);
		}
		return applNo;
	}


}
