package com.newcore.orbps.dao.api;



import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.para.StructT;

public interface UntilPolicyDao {

	public  int pGetChkChr(char str[] );


	public  String getTime(int i);

	public  String cfsSetCntrNoBit(String strCntrNo,int inLen) ;


	public	 String cfcComputeDate(String lstrBeginDate,int llPassCount,String lstrFlag);

	public  String getRelativeDate(String lstrBeginDate,int count);

	public  String cfsGetRelativeDate(String lstrBeginDate,int count);

	public  String cfsGetCntrNoBit(String str , int leng);
	public  Object cfdSelSwiftNumber( String  tablename ,StructT structT);
	public  int cfdInsSwiftNumber( String  tablename ,StructT structT);

	public	 int cfdUpdSwiftNumber( String  tablename ,StructT structT);

	public  String  cfoGetCounter(StructT structT);
	public  String  cfcGetCounter(int num,String parLcpCounterNameChn,String strBranch,String strSysdate,String strApplType,String 
			strMrType,String strInsurDurUnit);


	public  String cfsGetCounterCntrNo(String strBranchNo,
			String strYear,
			String applType,
			String mrType,
			String insurDurType	
			);


	public  String cfsGetCounterCgNo(String strBranchNo,String strYear);

	public  int cfsGetCntrNoSubCounterCode(String parApplType, String parMrType, String parInsurDurType);

	public RetInfo creatAgreementNo(String mgrBranch);

	public  long cfsGetDateYear(String str);

	public  String cfsGetCounterApplNo(String strBranchNo,String strYear);
	public	 String cfsSetApplNo( String cntrType,String salesBranchNo);


	public	 String cfsSetMCntrNo(String applType,String polCode,String	inSignDate,String applNo);
	public String contractInforce(String applNo);

}
