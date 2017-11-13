package com.newcore.orbps.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.halo.core.common.util.PropertiesUtils;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.supports.dicts.DUR_UNIT;
import com.newcore.supports.dicts.GRP_ID_TYPE;
import com.newcore.supports.dicts.ID_TYPE;
import com.newcore.supports.dicts.OCC_CLASS_CODE;

/** 
* @ClassName: CreateGrpIsurAppl 
* @Description: 生成三个固定pdf模版  √ 选择字符
* @author  jiangbomeng  
* @date 2016年7月27日 下午9:23:52 
*  
*/
public class CreateGrpIsurAppl {
	
	/**
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws DocumentException 
	 * @throws Exception 
	 * @throws IOException 
	 * @throws DocumentException  
	* @Title: createGroup 
	* @Description:团单
	* @param @param out 
	* @param @param grpInsurAppl    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public static void createGroup(ByteArrayOutputStream out,GrpInsurAppl grpInsurAppl) throws DocumentException, MalformedURLException, IOException {
		//创建document
		
		 
		 
		 Document doc=new Document(PageSize.A4 ); 
			PdfWriter pdf = PdfWriter.getInstance(doc, out);
			BaseFont font = BaseFont.createFont(PropertiesUtils.getProperty("CreateGrpIsurApplService.font.url")+",1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);// 解决jar包问题 读取本地字体 
			Font f = new Font(font, 8);  //设置表格内容字体
			Font f1 = new Font(font, 8,Font.BOLD); 
			//打开document
			doc.open();  
			CommonCreatePDf.CreateTitle(pdf, doc, font, f, "* "+grpInsurAppl.getApplNo() +" *","团体保险投保单",grpInsurAppl);
	        PdfPTable table = new PdfPTable(6);   //添加一个六列的表格
 	        table.setWidthPercentage(100);  
	        PdfPCell cell;  
	       String tabletitle=  "投保提示：\n"+
	    		   "1. 请您在仔细阅读保险条款，充分理解保险责任、责任免除、解除合同等规定，权衡保险需求后作出投保决定，填写投保单。\n"+
	    		   "2. 投保资料（包括投保单、投保交费清单等相关资料）为保险合同的重要组成部分，填写内容必须真实、准确。若有不明事项"+
	    		   "请向销售人员或我公司咨询（客户服务热线：95519）。\n"+
	    		   "3. 根据《中华人民共和国保险法》规定，我公司有权就投保人、被保险人的有关情况进行询问，您应如实告知，如您未如实告"+
	    		   "知，我公司有权在法定期限内解除保险合同，并依法决定是否对合同解除前发生的保险事故承担保险责任。\n"+
	    		   "4. 一切与本投保单各项内容及保险条款相违背或增减的销售人员说明及解释均属无效，一切告知均以书面为准。\n"+
	    		   "5. 生效日期以保险单载明日期为准，此前我公司不承担保险责任。\n";
		     CommonCreatePDf.createCell(f, table,tabletitle,6,null,false,false); 

		     CommonCreatePDf.createCell(f, table,"一、投保人资料",6,null,false,true); 
	         
		     CommonCreatePDf.createCell(f, table,"单位/团体名称",null,null,true,false); 
	         CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpName()),3,null,false,false); 
	         CommonCreatePDf.createCell(f, table,"行业类别",null,null,true,false); 
	      // table.addCell(isEmpty(grpInsurAppl.getGrpHolderInfo().getOccClassCode())); 

	         CommonCreatePDf.createCell(f, table,isEmpty( OCC_CLASS_CODE.valueOfKey(grpInsurAppl.getGrpHolderInfo().getOccClassCode()).getDescription()),1,null,false,false); 
	         CommonCreatePDf.createCell(f, table,"证件类型",null,null,true,false); 
	         table.addCell(new Phrase(isEmpty(GRP_ID_TYPE.valueOfKey(grpInsurAppl.getGrpHolderInfo().getGrpIdType()).getDescription()),f));  
	         CommonCreatePDf.createCell(f, table,"证件号码",null,null,true,false); 
	         table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpIdNo()),f));  
	         CommonCreatePDf.createCell(f, table,"传    真",null,null,true,false); 
	         table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getFax()),f)); 
	         
	         CommonCreatePDf.createCell(f, table,"通讯地址",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,getAddress(grpInsurAppl.getGrpHolderInfo().getAddress()),3,null,false,false); 
	         CommonCreatePDf.createCell(f, table,"邮政编码",null,null,true,false); 
	         CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode()),1,null,false,false); 
	         CommonCreatePDf.createCell(f, table,"成员总数",null,null,true,false);
	         cell = new PdfPCell(new Phrase(grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise()+"      人",f));  
	         table.addCell(cell);   
	         CommonCreatePDf.createCell(f, table,"在职人数",null,null,true,false);
	         cell = new PdfPCell(new Phrase(grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum()+"      人",f));  
	         table.addCell(cell);   
	         CommonCreatePDf.createCell(f, table,"投保人数",null,null,true,false);
	         cell = new PdfPCell(new Phrase(grpInsurAppl.getGrpHolderInfo().getIpsnNum()+"        人",f));  
	         table.addCell(cell);  
	         
	         CommonCreatePDf.createCell(f, table,"职业类别",null,null,true,false);
	         table.addCell(new Phrase(" ",f));  
	         CommonCreatePDf.createCell(f, table,"联系人姓名",null,null,true,false);
	         table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getContactName()),f));  
	         CommonCreatePDf.createCell(f, table,"联系人手机",null,null,true,false);
	         table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getContactMobile()),f)); 
	         
	         CommonCreatePDf.createCell(f, table,"联系人电子邮件",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactEmail()),2,null,true,false);
	         CommonCreatePDf.createCell(f, table,"联系人固定电话",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactTelephone()),2,null,true,false);
//二、被保险人资料（详见所附投保交费清单。）        
	         CommonCreatePDf.createCell(f, table,"二、被保险人资料（详见所附投保交费清单。）",6,null,false,true);
	         CommonCreatePDf.createCell(f, table,"被保险人总数",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,grpInsurAppl.getApplState().getIpsnNum()+"               人",5,null,true,false);

//三、受益人资料
	         CommonCreatePDf.createCell(f, table,"三、受益人资料",6,null,false,true);
	         String  tablestr1="1．除本合同另有约定外，身故保险金以外的其他保险金受益人为被保险人本人。\n"
	         		+ "2．身故保险金的受益人由被保险人或投保人指定（详见所附投保交费清单)。\n"
	         		+ "3．投保人在指定或变更身故保险金受益人时需经被保险人书面同意。投保人为与其有劳动关系的劳动者投保人身保险，不得"
	         		+ "	指定被保险人及其近亲属以外的人为受益人。\n"
	         		+ "	 4．若投保人未填写身故保险金受益人信息的，我公司将依据《中华人民共和国保险法》第42条规定履行给付保险金的义务。\n   " ; 
	         CommonCreatePDf.createCell(f, table,tablestr1,6,null,false,false);
//四、要约内容
	         CommonCreatePDf.createCell(f, table,"四、要约内容",6,null,false,true);
	         
	         CommonCreatePDf.createCell(f, table,"险种名称",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"总保险金额\n(元)",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"总保险费\n(元)",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"被保险人数\n(人)",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"保险期间",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"缴费期间",null,null,true,false);
	         if(!isEmpty(grpInsurAppl.getApplState().getPolicyList())){
	        	int numrows=grpInsurAppl.getApplState().getPolicyList().size();
	        	CommonCreatePDf.createCell(f, table,grpInsurAppl.getApplState().getPolicyList().get(0).getPolNameChn(),null,null,true,false);
		         table.addCell(String.format("%.2f",grpInsurAppl.getApplState().getPolicyList().get(0).getFaceAmnt()));
		         table.addCell(String.format("%.2f",grpInsurAppl.getApplState().getPolicyList().get(0).getPremium()));
		         table.addCell(grpInsurAppl.getApplState().getPolicyList().get(0).getPolIpsnNum()+"");
		         String insurDurs="";
		         //保费期间等于保险期类型+保险期间 
		         	if(null!=grpInsurAppl.getApplState()){
		         		String insurDurUnit= grpInsurAppl.getApplState().getInsurDurUnit();   //保险期类型
		         		/*if(StringUtils.equals("Y",insurDurUnit)){
		        		 insurDurUnit="保险年数";
		         		}else if(StringUtils.equals("M",insurDurUnit)){
		        		 insurDurUnit="保险月数";
		         		}else if(StringUtils.equals("D",insurDurUnit)){
		        		 insurDurUnit="保险天数";
		         		}else{
		        		 insurDurUnit="其他";
		         	}*/
		         		
		         	 Integer insurDur = grpInsurAppl.getApplState().getInsurDur();  //保险期间
		        	 insurDurUnit= DUR_UNIT.valueOfKey(insurDurUnit).getDescription();
		        	 insurDurs =insurDur+"-"+insurDurUnit;
		        	 CommonCreatePDf.createCell(f, table,insurDurs,null,numrows,true,false);
		         }else{
		        	    CommonCreatePDf.createCell(f, table," ",null,numrows,true,false);
		         }
		         	 String moneyinDur =" ";
//		        	 缴费期单位加缴费期
//		        	 缴费期	moneyinDur
//		        	 缴费期单位	moneyinDurUnit
		         if(null!=grpInsurAppl.getPaymentInfo()){
		        	moneyinDur=grpInsurAppl.getPaymentInfo().getMoneyinDur()+"-"+DUR_UNIT.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit()).getDescription();
		        	CommonCreatePDf.createCell(f, table,moneyinDur ,null,numrows,true,false);
		         }else{
		        	 CommonCreatePDf.createCell(f, table," " ,null,numrows,true,false);
		        	 	}
		         for (int i = 1; i < numrows; i++) {
			         CommonCreatePDf.createCell(f, table,grpInsurAppl.getApplState().getPolicyList().get(i).getPolNameChn(),null,null,true,false);
			         table.addCell(String.format("%.2f", grpInsurAppl.getApplState().getPolicyList().get(i).getFaceAmnt()));
			         table.addCell(String.format("%.2f", grpInsurAppl.getApplState().getPolicyList().get(i).getPremium()));
			         table.addCell( grpInsurAppl.getApplState().getPolicyList().get(i).getPolIpsnNum()+"");
				}
	         }else{
	        	 table.addCell(" ");
		         table.addCell(" ");
		         table.addCell(" ");
		         table.addCell(" ");
		         CommonCreatePDf.createCell(f, table," ",null,5,true,false);
		         CommonCreatePDf.createCell(f, table," ",null,5,true,false);
		         for (int i = 0; i < 16; i++) {
		        	 table.addCell(" ");
				}
	         }
	        
	       
	         CommonCreatePDf.createCell(f, table,"保险费合计",null,null,true,false);
	         Paragraph p = new Paragraph("(大写)",f1);  
	         p.add(NumberToCN.number2CNMontrayUnit(grpInsurAppl.getApplState().getSumPremium())+" ");
	         p.add("(小写)"+String.format("%.2f",grpInsurAppl.getApplState().getSumPremium()));  
	         cell = new PdfPCell(p); 
	         cell.setColspan(3); 
	         table.addCell(cell);
	         CommonCreatePDf.createCell(f, table,"币种",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"CNY".equals((grpInsurAppl.getPaymentInfo().getCurrencyCode()))?"√ 人民币  □其他":"□ 人民币  √ 其他"+grpInsurAppl.getPaymentInfo().getCurrencyCode(),null,null,false,false);
	         CommonCreatePDf.createCell(f, table,"保单性质",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"0".equals(grpInsurAppl.getInsurProperty()) ?"√新单投保  □续保保单":"□新单投保  √续保保单",2,null,false,false);
	         CommonCreatePDf.createCell(f, table,"指定生效日",null,null,true,false);
	         CommonCreatePDf.createCell(f, table,"1".equals(grpInsurAppl.getApplState().getInforceDateType())?"√指定为 "+dateTOstring(grpInsurAppl.getApplState().getDesignForceDate())+" □不指定":"□指定为    年  月  日 √不指定",2,null,false,false);
	       
	         CommonCreatePDf.createCell(f, table,"交费方式",null,null,true,false); 
	        
	         CommonCreatePDf.createCell(f, table,getMoneyItrvl(isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())),5,null,false,false); 
	         
	        
		     CommonCreatePDf.createCell(f, table,"交费形式",null,null,true,false); 
		    
	       
		     CommonCreatePDf.createCell(f, table,getMonetInType(isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())),5,null,false,false); 
		        
		     CommonCreatePDf.createCell(f, table,"交费开户银行",null,null,true,false); 
		     CommonCreatePDf.createCell(f, table,getBankName(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankCode())?" ":grpInsurAppl.getPaymentInfo().getBankCode()),null,null,true,false); 
		     CommonCreatePDf.createCell(f, table,"交费账户开户名称",null,null,true,false); 
		     CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankAccName())?" ":grpInsurAppl.getPaymentInfo().getBankAccName(),null,null,true,false); 
		     CommonCreatePDf.createCell(f, table,"账号",null,null,true,false); 
		     CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankAccNo())?" ":grpInsurAppl.getPaymentInfo().getBankAccNo(),null,null,true,false); 
		     
		     CommonCreatePDf.createCell(f, table,"短险结算方式",null,null,true,false); 
		     CommonCreatePDf.createCell(f, table,getpayment(grpInsurAppl.getPaymentInfo()),5,null,false,false); 
		        
		     CommonCreatePDf.createCell(f, table,"争议处理方式",null,null,true,false); 
		     
		     CommonCreatePDf.createCell(f, table,"1".equals(grpInsurAppl.getArgueType())?"√诉讼  □仲裁（若选择仲裁,请在次数明确填写全称:          仲裁委员会）\n(若选择仲裁选项但未明确写仲裁委员会的名称，或填写了不存在的仲裁委员会，则仲裁约定无效)":"□诉讼  √仲裁（若选择仲裁,请在次数明确填写全称:"+ grpInsurAppl.getArbitration()+"）\n(若选择仲裁选项但未明确写仲裁委员会的名称，或填写了不存在的仲裁委员会，则仲裁约定无效)",5,null,false,false); 
		     
		     if(null!=grpInsurAppl.getHealthInsurInfo()){
		    	 CommonCreatePDf.createCell(f, table,"五,公共限额保险资料          险种名称：健康险",6,null,false,true); 
			     CommonCreatePDf.createCell(f, table,getHealthInsur(grpInsurAppl.getHealthInsurInfo().getComInsurAmntUse()),6,null,false,false); 
			     CommonCreatePDf.createCell(f, table,"公共保额 ",null,null,true,false); 
			     String healthInsur="";
			     if("0".equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType())){   
						healthInsur="√固定公共保额 固定公共保额合计"+grpInsurAppl.getHealthInsurInfo().getSumFixedAmnt()+"元；\n□浮动公共保额 人均浮动公共保额        元；人均浮动比例 :  % ；合计保额:";
					 }
					 else if("1".equals(grpInsurAppl.getHealthInsurInfo().getComInsurAmntType())){
						 healthInsur="□固定公共保额 固定公共保额合计        元；\n√浮动公共保额 人均浮动公共保额"+grpInsurAppl.getHealthInsurInfo().getIpsnFloatAmnt()+"元；人均浮动比例 :"+grpInsurAppl.getHealthInsurInfo().getFloatInverse()+" % ；合计保额:"+grpInsurAppl.getHealthInsurInfo().getComInsrPrem();
					 }
					else{
						 healthInsur="□固定公共保额 固定公共保额合计        元；\n□浮动公共保额 人均浮动公共保额        元；人均浮动比例 :  % ；合计保额:";
					 }
			     CommonCreatePDf.createCell(f, table,healthInsur,5,null,false,false); 
		     }
		     //基金险
		     else if(null!=grpInsurAppl.getFundInsurInfo()){
		    	 CommonCreatePDf.createCell(f, table,"五,公共限额保险资料          险种名称：基金险",6,null,false,true); 
			     CommonCreatePDf.createCell(f, table,"公共保额使用范围： □不选择公共保额(不必填写公共保额内容。) □不包括连带被保险人  □包括连带被保险人 ",6,null,false,false); 
			     CommonCreatePDf.createCell(f, table,"公共保额 ",null,null,true,false); 
			     CommonCreatePDf.createCell(f, table,"□固定公共保额 固定公共保额合计        元；\n□浮动公共保额 人均浮动公共保额        元；人均浮动比例 :  % ；合计保额:        元",5,null,false,false); 
			     
		     
		     }
		     else{
		    	 CommonCreatePDf.createCell(f, table,"五,公共限额保险资料          险种名称：",6,null,false,true); 
			     CommonCreatePDf.createCell(f, table,"公共保额使用范围： □不选择公共保额(不必填写公共保额内容。) □不包括连带被保险人  □包括连带被保险人 ",6,null,false,false); 
			     CommonCreatePDf.createCell(f, table,"公共保额 ",null,null,true,false); 
			     CommonCreatePDf.createCell(f, table,"□固定公共保额 固定公共保额合计        元；\n□浮动公共保额 人均浮动公共保额        元；人均浮动比例 :  % ；合计保额:        元",5,null,false,false); 
			     
			     CommonCreatePDf.createCell(f, table,"公共保险费",null,null,true,false); 
			     CommonCreatePDf.createCell(f, table,"          元",null,null,true,false);
			     CommonCreatePDf.createCell(f, table,"公共保额使用许可",null,null,true,false); 
			     CommonCreatePDf.createCell(f, table,"□经投保人确认后使用    □无需投保人确认，直接使用",3,null,true,false);
			     
			     CommonCreatePDf.createCell(f, table," 每一被保险人可使用额度",null,null,true,false); 
			     CommonCreatePDf.createCell(f, table,"  □相同额度        元  □同被保险人个人保额  □无限额  □详见附件",5,null,true,false); 
			     
			     
			     
		     }
		     //健康险给付约定  因数据问题暂时不生成
		     
		     //基金险账户资料
		     CommonCreatePDf.createCell(f, table,"六,基金险账户资料",6,null,false,true);
		     CommonCreatePDf.createCell(f, table,"首期缴费金额",2,2,true,false);
		     CommonCreatePDf.createCell(f, table,"个人账户交费金额(元)",null,null,true,false);
		     if(null!=grpInsurAppl.getFundInsurInfo()){
		    	 CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getFundInsurInfo().getIpsnFundPremium()?grpInsurAppl.getFundInsurInfo().getIpsnFundPremium()+"":" ",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,"计入个人账户金额(元)",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt()?grpInsurAppl.getFundInsurInfo().getIpsnFundAmnt()+"":" ",null,null,true,false);
			    
		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getFundInsurInfo().getSumFundPremium()?grpInsurAppl.getFundInsurInfo().getSumFundPremium()+"":" ",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,"计入公共账户金额(元)",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getFundInsurInfo().getSumFundAmnt()?grpInsurAppl.getFundInsurInfo().getSumFundAmnt()+"":" ",null,null,true,false);
		     
//		    	 CommonCreatePDf.createCell(f, table,"管理费记提方式",null,null,true,false);
//		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
//		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
//		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
//		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
//		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
		     
		     }else{
		    	 
		    	 CommonCreatePDf.createCell(f, table," ",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,"计入个人账户金额",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table," ",null,null,true,false);
		    	 
		    	 CommonCreatePDf.createCell(f, table,"公共账户交费金额(元)",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table," ",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table,"计入公共账户金额(元)",null,null,true,false);
		    	 CommonCreatePDf.createCell(f, table," ",null,null,true,false);
		     
		     }
		     //七 养老金保险资料
		     CommonCreatePDf.createCell(f, table,"七,养老金保险资料",6,null,false,true);
		     CommonCreatePDf.createCell(f, table,"领取方式",null,null,true,false);
		     CommonCreatePDf.createCell(f, table," □延期领取   □即期领取  □清单指定",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"□一次性领取 □年领  □月领 □其它",3,null,true,false);
		     CommonCreatePDf.createCell(f, table,"领取年龄",null,null,true,false);
		     CommonCreatePDf.createCell(f, table,"男:     周岁  女：      周岁",5,null,false,false);
		     //告知事项
		     CommonCreatePDf.createCell(f, table,"八,告知事项",6,null,false,true);
		     CommonCreatePDf.createCell(f, table,"1.投保单位是否已在我公司投保其它人身保险？（若“是”，请在下表中，详细说明。) □是  □否：",6,null,true,false);
		     CommonCreatePDf.createCell(f, table,"险种名称",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"保险单号码",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"保险单生效日期",2,null,true,false);
		     for (int i = 0; i < 3; i++) {
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"    年     月   日",2,null,true,false);
			}
		     CommonCreatePDf.createCell(f, table," 2．过去三年是否向保险公司索赔过？\n（若“是”一请在备注栏列明索赔险种，索赔时间索赔原因及索赔人数。)□是□否",6,null,false,false);
		     CommonCreatePDf.createCell(f, table," 3．过去三年是否划过死亡或伤残情况？（若“是”，请告知人数。）□是  □否：\n 疾病死亡    人，疾病伤残    人，意外死亡     人，意外伤残     人",6,null,false,false);
		     CommonCreatePDf.createCell(f, table," 4．参加投保的被保险人是否患有以下疾病？□是  □否：\n"
		     		+ "a，恶性肿瘤                   b，心脏病（心功能不全Ⅱ级以上）         c,心肌模塞;\n"
		     		+ "d．高血压（1级以上）          e．白血病                               f．肝硬化;\n"
		     		+ "g，慢性梗塞性支气管疾病       h，脑血管疾病                           L，慢性肾脏疾病;\n"
		     		+ "j．糖尿病，                   k，再生障碍性贫血                       i，先天性疾病（见条款中释义部分）;\n"
		     		+ "m．精神病或精神分裂           n,街角病                                o,身体残障;\n"
		     		+ "p,妇科疾病                    q．其它疾病                             r,是否曾因病全休或半休;",6,null,false,false);
		     CommonCreatePDf.createCell(f, table," 5. 是否有长期病假，长期接受治疗或住院治疗人员参加本次投保？（若“是”，有      人    □是  □否：",6,null,false,false);
		     CommonCreatePDf.createCell(f, table," 6. 是否有残疾人员参加本次投保？（若“是”，有        人 )   □是  □否:",6,null,false,false);
		     CommonCreatePDf.createCell(f, table," 7. 保险金额分配规则：□均一保额  □按年收入  □按职位  □综合了多种因素(请在备注栏列明)",6,null,false,false);
		     CommonCreatePDf.createCell(f, table,"  （说明：4,5,6项目若为“是”：请在被保险人清单的具体被保险人“备注”栏写明",6,null,false,false);
		     
		    
		     CommonCreatePDf.createCell(f, table,"九,其他约定（备注栏）",6,null,false,true);
		     if(null!=grpInsurAppl.getConventions()){
		    	 if(!StringUtils.isBlank(grpInsurAppl.getConventions().getPolConv())){
				     CommonCreatePDf.createCell(f, table,grpInsurAppl.getConventions().getPolConv()+"\n" ,6,null,false,false);
		    	 }else{
				     CommonCreatePDf.createCell(f, table,"“无” \n\n\n\n" ,6,null,false,false);
		    	 }
		     }else{
			     CommonCreatePDf.createCell(f, table,"“无” \n\n\n\n" ,6,null,false,false);
		     }
		     
		     CommonCreatePDf.createCell(f, table,"十，投保人及被保险人声明",6,null,false,true);
		     
		     CommonCreatePDf.createCell(f, table,"  贵公司已对保险合同的条款内容履行了说明义务，并对责任免除条款履行了明确说明义务，投保单位已仔细阅知，理解投保提示\n"
+"及保险条款尤其是责任免除解除合同等规定，并同意遵守，所填投保单各项及告知事项均属事实并确无欺瞒，上述一切陈述及本声\n"
+"明将成为贵公司承保的依据，并作为保险合同的一部分，如有不实告知，贵公司有权在法定期限内解除合同，并依法决定是否对合同\n"
  +"解除前发生的保险事故承担保险责任，\n"
+"  特授权本投保单所填写的联系人(身份证号；"
 +"                           )为我公司日常业务的办理人，联系人可持\n"
 +"我公司相关资料，办理我公司保险日常业务，本授权委托自签发之日起生效．\n\n"
+"  投保人或授权人签字                                                      "
+"投保人盖章\n\n"
+ "                                                                        投保申请日期         年     月      日",6,null,false,false);
		     doc.add(table);     
		     Phrase ps = new Phrase("健康险专项:                    受理机构:                    经办:                            受理日期:", f);     
		     doc.add(ps);
			doc.close();
	}

	
	
	/**
	 * @throws Exception 
	 * @throws IOException 
	 * @throws DocumentException  
	* @Title: createAccident 
	* @Description: TODO建工险
	* @param @param out
	* @param @param grpInsurAppl    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public static void createAccident(ByteArrayOutputStream out,GrpInsurAppl grpInsurAppl) throws Exception{
		 Document doc=new Document(PageSize.A4 ); 
				PdfWriter pdf = PdfWriter.getInstance(doc, out);
				BaseFont font = BaseFont.createFont(PropertiesUtils.getProperty("CreateGrpIsurApplService.font.url")+",1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);// 解决jar包问题 读取本地字体 
				Font f = new Font(font, 8);  //设置表格内容字体
				doc.open();  
				CommonCreatePDf.CreateTitle(pdf, doc, font, f, "* "+grpInsurAppl.getApplNo() +" *","建筑工程团体人身意外伤害保险投保单",grpInsurAppl );
				PdfPTable table = new PdfPTable(6);
				table.setWidthPercentage(100);  
				String str="投保提示："
	 +"1.请您在仔细阅读保险条款，充分理解保险责任责任免除、解除合同等规定，权衡保险需求后作出投保决定，填"
	 +"写投保单\n"
	 +"2.投保资料包括投保单、被保险人清单等相关资料）为保险合同的重要组成部分，填写内容必须真实、准确。"
	 +"有不明事项请向销售人员或我公司咨询（客户服务热线：95519）\n"
	 +"3.根据《中华人民共和国保险法》规定，我公司 有权就投保人被保险人的有关情况进行询问，您应如实告知，如"
	 +"您未如实告知，我公司有权在法定期限内解除保险合同，并依法决定是否对合同解除前发生的保险事故承担保险"
	 +"责任\n"
	 +"4.一切与本投保单各项内容及保险条款相违背或增减的销售人员说明及解释均属无效，一切告知均以书面为准。\n"
	 +"5.生效日期以保险单载明日期为准，此前我公司不承担保险责任。\n";
				
				CommonCreatePDf.createCell(f, table,str,6,null,false,false);
				CommonCreatePDf.createCell(f, table,"一、投保人资料",6,null,false,true);
				
				CommonCreatePDf.createCell(f, table,"单位/团体名称",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpName()),3,null,false,false);
				CommonCreatePDf.createCell(f, table,"行业类别",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(OCC_CLASS_CODE.valueOfKey(grpInsurAppl.getGrpHolderInfo().getOccClassCode()).getDescription()),null,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"证件类别",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(GRP_ID_TYPE.valueOfKey(grpInsurAppl.getGrpHolderInfo().getGrpIdType()).getDescription()),null,null,false,false);
				CommonCreatePDf.createCell(f, table,"证件号码",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactIdNo()),null,null,false,false);
				CommonCreatePDf.createCell(f, table,"传    真",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getFax()),null,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"通讯地址",null,null,true,false);
				CommonCreatePDf.createCell(f, table,getAddress(grpInsurAppl.getGrpHolderInfo().getAddress()),3,null,false,false);
				CommonCreatePDf.createCell(f, table,"邮政编码",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode()),null,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"员工总数",null,null,true,false);
				CommonCreatePDf.createCell(f, table,grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise()+"            人",null,null,false,false);
				CommonCreatePDf.createCell(f, table,"在职人数",null,null,true,false);
				CommonCreatePDf.createCell(f, table,grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum()+"            人",null,null,false,false);
				CommonCreatePDf.createCell(f, table,"投保人数",null,null,true,false);
				CommonCreatePDf.createCell(f, table,grpInsurAppl.getGrpHolderInfo().getIpsnNum()+"            人",null,null,false,false);

				CommonCreatePDf.createCell(f, table,"联系人姓名",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactName()),null,null,false,false);
				CommonCreatePDf.createCell(f, table,"联系人手机",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactMobile()),null,null,false,false);
				CommonCreatePDf.createCell(f, table,"联系人固定电话",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactTelephone()),null,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"联系人电子邮件",null,null,true,false);
				if(!StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getContactEmail())){
					CommonCreatePDf.createCell(f, table,grpInsurAppl.getGrpHolderInfo().getContactEmail(),5,null,true,false);
				}else{
					CommonCreatePDf.createCell(f, table," @",5,null,true,false);
				}
				
				
				CommonCreatePDf.createCell(f, table,"二、被保险人资料",6,null,false,true);
				
				CommonCreatePDf.createCell(f, table,"被保险人清单",null,null,true,false);
				if(StringUtils.isBlank(grpInsurAppl.getLstProcType())){
					CommonCreatePDf.createCell(f, table,"  □有清单    口无清单",5,null,false,false);
				}else if(StringUtils.equals("L", (grpInsurAppl.getLstProcType()))){
					CommonCreatePDf.createCell(f, table,"  √有清单    口无清单",5,null,false,false);
				}else{
					CommonCreatePDf.createCell(f, table,"  □有清单    √无清单",5,null,false,false);
				}
				CommonCreatePDf.createCell(f, table,"三、建筑工程资料",6,null,false,true);
				
				CommonCreatePDf.createCell(f, table,"建筑项目名称",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getConstructInsurInfo().getIobjName()),5,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"工程地址",null,null,true,false);
				CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getConstructInsurInfo().getProjLoc()),5,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"工程类型",null,null,true,false);
				if(StringUtils.isBlank(grpInsurAppl.getConstructInsurInfo().getProjType())){
					CommonCreatePDf.createCell(f, table,"  □一般工程  口桥梁隧道类工程",2,null,false,false);
				}else if(StringUtils.equals("3",grpInsurAppl.getConstructInsurInfo().getProjType())){
					CommonCreatePDf.createCell(f, table,"  □一般工程  √桥梁隧道类工程",2,null,false,false);
				}else{
					CommonCreatePDf.createCell(f, table,"  √一般工程  口桥梁隧道类工程",2,null,false,false);
				}
				CommonCreatePDf.createCell(f, table,"工程位置类别",null,null,true,false);
				if(StringUtils.isBlank(grpInsurAppl.getConstructInsurInfo().getProjLocType())){
					CommonCreatePDf.createCell(f, table,"  □平原、低洼地厂  □其它",2,null,false,false);
				}else if(StringUtils.equals("1", grpInsurAppl.getConstructInsurInfo().getProjLocType())
						||StringUtils.equals("2", grpInsurAppl.getConstructInsurInfo().getProjLocType())
						){
					CommonCreatePDf.createCell(f, table,"  √平原、低洼地厂  □其它",2,null,false,false);
					}else{
					CommonCreatePDf.createCell(f, table,"  □平原、低洼地厂  √其它",2,null,false,false);
				}
				CommonCreatePDf.createCell(f, table,"工程总面积\n（平方米）",null,null,true,false);
				CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getConstructInsurInfo().getIobjSize()?" ":String.format("%.2f",grpInsurAppl.getConstructInsurInfo().getIobjSize()),2,null,false,false);
				CommonCreatePDf.createCell(f, table,"工程总造价\n（万元）",null,null,true,false);
				CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getConstructInsurInfo().getIobjCost()?" ":String.format("%.2f",grpInsurAppl.getConstructInsurInfo().getIobjCost()),2,null,false,false);
				
				CommonCreatePDf.createCell(f, table,"企业资质",null,null,true,false);
				if(StringUtils.isBlank(grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
					CommonCreatePDf.createCell(f, table,"  □特级  □一级  □二级  □三级  口不分级 ",5,null,false,false);
				}else if(StringUtils.equals("0", grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
					CommonCreatePDf.createCell(f, table,"  √特级  □一级  □二级  □三级  口不分级 ",5,null,false,false);
				}else if(StringUtils.equals("1", grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
					CommonCreatePDf.createCell(f, table,"  □特级  √一级  □二级  □三级  口不分级 ",5,null,false,false);
				}else if(StringUtils.equals("2", grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
					CommonCreatePDf.createCell(f, table,"  □特级  □一级  √二级  □三级  口不分级 ",5,null,false,false);
				}else if(StringUtils.equals("3", grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
					CommonCreatePDf.createCell(f, table,"  □特级  □一级  □二级  √三级  口不分级 ",5,null,false,false);
				}else if(StringUtils.equals("9", grpInsurAppl.getConstructInsurInfo().getEnterpriseLicence())){
					CommonCreatePDf.createCell(f, table,"  □特级  □一级  □二级  □三级  √不分级 ",5,null,false,false);
				}
				
				
				CommonCreatePDf.createCell(f, table,"施工期间",null,null,true,false);
				if(null!=grpInsurAppl.getConstructInsurInfo().getConstructFrom()&&null!=grpInsurAppl.getConstructInsurInfo().getConstructTo()){
					CommonCreatePDf.createCell(f, table,"  自 " +dateTOstring(grpInsurAppl.getConstructInsurInfo().getConstructFrom())  +" 截至   " +dateTOstring(grpInsurAppl.getConstructInsurInfo().getConstructTo())
							+ "止",5,null,false,false);
				}else{
					CommonCreatePDf.createCell(f, table,"  自    年  月  日截至    年  月  日止",5,null,false,false);

				}
				CommonCreatePDf.createCell(f, table,"四、受益人资料",6,null,false,true);
				String str1="1.除本合同另有约定外，身故保险金以外的其他保险金受益人为被保险人本人。\n"
							+" 2，身故保险金的受益人由被保险人或投保人指定（详见所附被保险人清单。\n"
							+"3投保人在指定或变更身故保险金受益人时需经被保险人书面同意。投保人为与其有劳动关系的劳动者投保人身"
							+"保险，不得指定被保险人及其近亲属以外的人为受益人。\n"
							+"4.若投保人未填写身故保险金受益人信息的，我公司将依据《中华人民共和国保险法》第42条规定履行给付保险"
							+"金的义务.";
				CommonCreatePDf.createCell(f, table,str1,6,null,false,false);
				//新增一页 五要约内容
				CommonCreatePDf.createCell(f, table,"五、要约内容",6,null,false,true);
				
				CommonCreatePDf.createCell(f, table,"主险",null,5,true,false);
				CommonCreatePDf.createCell(f, table,"险种名称",null,null,true,false);
				Double premiumcount=null;
				Double StdPremiumCount=null;
				if(isEmpty(grpInsurAppl.getApplState().getPolicyList())){
					CommonCreatePDf.createCell(f, table," ",4,null,true,false);
					CommonCreatePDf.createCell(f, table,"保险费计收方式",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"每人保险金额(元)",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"总保险费(元)",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"保 险 期 间",2,null,true,false);
					
					CommonCreatePDf.createCell(f, table,"□按被保险人人数计收保费\n 被保险人总数      人",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"□ 1年\n□ 自    年  月  日起\n 至    年  月  日止",2,3,true,false);
					
					CommonCreatePDf.createCell(f, table,"□按建筑工程合同总造价计收保费",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					
					CommonCreatePDf.createCell(f, table,"□按建筑施工总面积计收保费",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					
					CommonCreatePDf.createCell(f, table,"附加险",null,3,true,false);
					CommonCreatePDf.createCell(f, table,"险 种 名 称",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"每人保险金额(元)",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"总保险费(元)",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"保 险 期 间",2,null,true,false);
					
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table,"□ 1年\n□ 同主险保险期间",2,2,true,false);
					
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					
				}else{
					int polSize=grpInsurAppl.getApplState().getPolicyList().size();
					for(Policy pol:grpInsurAppl.getApplState().getPolicyList()){
						if(StringUtils.equals("M", pol.getMrCode())){
							CommonCreatePDf.createCell(f, table,pol.getPolNameChn(),4,null,true,false);
							CommonCreatePDf.createCell(f, table,"保险费计收方式",null,null,true,false);
							CommonCreatePDf.createCell(f, table,"每人保险金额(元)",null,null,true,false);
							CommonCreatePDf.createCell(f, table,"总保险费(元)",null,null,true,false);
							CommonCreatePDf.createCell(f, table,"保 险 期 间",2,null,true,false);
							
							if(StringUtils.equals("N", grpInsurAppl.getConstructInsurInfo().getPremCalType())){
								CommonCreatePDf.createCell(f, table,"√按被保险人人数计收保费\n 被保险人总数      人",null,null,true,false);
								CommonCreatePDf.createCell(f, table,null==pol.getPremium()?" ":String.format("%.2f",pol.getFaceAmnt()/grpInsurAppl.getApplState().getIpsnNum()),null,null,true,false);
								CommonCreatePDf.createCell(f, table,null==pol.getPremium()?" ":String.format("%.2f",pol.getPremium()),null,null,true,false);
								 if(null !=grpInsurAppl.getInForceDate()  && null != grpInsurAppl.getCntrExpiryDate()){
										CommonCreatePDf.createCell(f, table," □1年\n √ 自  "+ dateTOstring((Date)grpInsurAppl.getInForceDate()) +"起\n 至  "+dateTOstring((Date)grpInsurAppl.getCntrExpiryDate())+"止",2,3,true,false);
								 }else{
								 CommonCreatePDf.createCell(f, table,"√ 1年\n□ 自    年  月  日起\n 至    年  月  日止",2,3,true,false);
								 } 
							 }else{
									CommonCreatePDf.createCell(f, table,"□按被保险人人数计收保费\n 被保险人总数      人",null,null,true,false);
									CommonCreatePDf.createCell(f, table," ",null,null,true,false);
									CommonCreatePDf.createCell(f, table," ",null,null,true,false);
									 if(null != grpInsurAppl.getInForceDate() && null != grpInsurAppl.getCntrExpiryDate()){
											CommonCreatePDf.createCell(f, table," □1年\n √ 自  "+ dateTOstring((Date)grpInsurAppl.getInForceDate()) +"起\n 至  "+ dateTOstring((Date)grpInsurAppl.getCntrExpiryDate()) +"止",2,3,true,false);
									 }else{
									 CommonCreatePDf.createCell(f, table,"√ 1年\n□ 自    年  月  日起\n 至    年  月  日止",2,3,true,false);
									 } 
							 }
							 
							if(StringUtils.equals("B", grpInsurAppl.getConstructInsurInfo().getPremCalType())){
								CommonCreatePDf.createCell(f, table,"√按建筑工程合同总造价计收保费",null,null,true,false);
								CommonCreatePDf.createCell(f, table,null==pol.getPremium()?" ":String.format("%.2f",pol.getFaceAmnt()/grpInsurAppl.getApplState().getIpsnNum()),null,null,true,false);
								CommonCreatePDf.createCell(f, table,null==pol.getPremium()?" ":String.format("%.2f",pol.getPremium()),null,null,true,false);
							}else{
								CommonCreatePDf.createCell(f, table,"□按建筑工程合同总造价计收保费",null,null,true,false);
								CommonCreatePDf.createCell(f, table," ",null,null,true,false);
								CommonCreatePDf.createCell(f, table," ",null,null,true,false);
							}
							
							if(StringUtils.equals("S", grpInsurAppl.getConstructInsurInfo().getPremCalType())){
								CommonCreatePDf.createCell(f, table,"√按建筑施工总面积计收保费",null,null,true,false);
								CommonCreatePDf.createCell(f, table,null==pol.getPremium()?" ":String.format("%.2f",pol.getFaceAmnt()/grpInsurAppl.getApplState().getIpsnNum()),null,null,true,false);
								CommonCreatePDf.createCell(f, table,null==pol.getPremium()?" ":String.format("%.2f",pol.getPremium()),null,null,true,false);
							}else{
								CommonCreatePDf.createCell(f, table,"□按建筑施工总面积计收保费",null,null,true,false);
								CommonCreatePDf.createCell(f, table," ",null,null,true,false);
								CommonCreatePDf.createCell(f, table," ",null,null,true,false);
							}
							premiumcount= null==premiumcount?pol.getPremium():premiumcount+pol.getPremium();
							StdPremiumCount=null==StdPremiumCount?pol.getFaceAmnt():StdPremiumCount+pol.getFaceAmnt();
							polSize--;
					}	
				}
					if(polSize<=0){
						CommonCreatePDf.createCell(f, table,"附加险",null,3,true,false);
						CommonCreatePDf.createCell(f, table,"险 种 名 称",null,null,true,false);
						CommonCreatePDf.createCell(f, table,"每人保险金额(元)",null,null,true,false);
						CommonCreatePDf.createCell(f, table,"总保险费(元)",null,null,true,false);
						CommonCreatePDf.createCell(f, table,"保 险 期 间",2,null,true,false);
						
						CommonCreatePDf.createCell(f, table," ",null,null,true,false);
						CommonCreatePDf.createCell(f, table," ",null,null,true,false);
						CommonCreatePDf.createCell(f, table," ",null,null,true,false);
						CommonCreatePDf.createCell(f, table,"□ 1年\n□ 同主险保险期间",2,2,true,false);
						
						CommonCreatePDf.createCell(f, table," ",null,null,true,false);
						CommonCreatePDf.createCell(f, table," ",null,null,true,false);
						CommonCreatePDf.createCell(f, table," ",null,null,true,false);
					}else{
						for (int i = 0; i < grpInsurAppl.getApplState().getPolicyList().size(); i++) {
							int j=0;
							if(!StringUtils.equals("M",grpInsurAppl.getApplState().getPolicyList().get(i).getMrCode())){
								j++;
							if(j==1){
								CommonCreatePDf.createCell(f, table,grpInsurAppl.getApplState().getPolicyList().get(0).getPolNameChn(),null,null,true,false);
							    CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getApplState().getPolicyList().get(0).getPremium()?" ":String.format("%.2f",grpInsurAppl.getApplState().getPolicyList().get(0).getPremium()),null,null,true,false);  
			     		        CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getApplState().getPolicyList().get(0).getStdPremium()?" ":String.format("%.2f",grpInsurAppl.getApplState().getPolicyList().get(0).getStdPremium()),null,null,true,false);  
								CommonCreatePDf.createCell(f, table,"□ 1年\n□ 同主险保险期间",2,polSize,true,false);
								premiumcount=null==premiumcount?grpInsurAppl.getApplState().getPolicyList().get(0).getPremium():premiumcount+grpInsurAppl.getApplState().getPolicyList().get(0).getPremium();
								StdPremiumCount=null==StdPremiumCount?grpInsurAppl.getApplState().getPolicyList().get(0).getFaceAmnt():StdPremiumCount+grpInsurAppl.getApplState().getPolicyList().get(0).getFaceAmnt();
								}else{
									CommonCreatePDf.createCell(f, table,grpInsurAppl.getApplState().getPolicyList().get(0).getPolNameChn(),null,null,true,false);
								    CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getApplState().getPolicyList().get(i).getPremium()?" ":String.format("%.2f",grpInsurAppl.getApplState().getPolicyList().get(i).getPremium()),null,null,true,false);  
				     		        CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getApplState().getPolicyList().get(i).getStdPremium()?" ":String.format("%.2f",grpInsurAppl.getApplState().getPolicyList().get(i).getStdPremium()),null,null,true,false);  
				     		       premiumcount=null==premiumcount?grpInsurAppl.getApplState().getPolicyList().get(i).getPremium():premiumcount+grpInsurAppl.getApplState().getPolicyList().get(i).getPremium();
				     		      StdPremiumCount=null==StdPremiumCount?grpInsurAppl.getApplState().getPolicyList().get(i).getFaceAmnt():StdPremiumCount+grpInsurAppl.getApplState().getPolicyList().get(i).getFaceAmnt();
								}
							}
						}
					}
				}			
				CommonCreatePDf.createCell(f, table,"给付约定：险种                       ，免赔额                            元，赔付比例                %\n     险种                        ，日津贴                             元。 ",6,null,true,false);
				CommonCreatePDf.createCell(f, table,"保险金额合计(元)",null,null,true,false);
				CommonCreatePDf.createCell(f, table,null==StdPremiumCount?" ":String.format("%.2f",StdPremiumCount),null,null,true,false);
				CommonCreatePDf.createCell(f, table,"指定生效日",null,null,true,false);
				CommonCreatePDf.createCell(f, table,"1".equals(grpInsurAppl.getApplState().getInforceDateType())?"√指定为 "+dateTOstring(grpInsurAppl.getApplState().getDesignForceDate())+" □不指定":"□指定为    年  月  日 √不指定",3,null,true,false);
				
				CommonCreatePDf.createCell(f, table,"保险费合计",1,null,true,false);
			    CommonCreatePDf.createCell(f, table,null==premiumcount?"（大写）                                  （小写）":"(大写)"+NumberToCN.number2CNMontrayUnit(premiumcount)+" (小写)"+String.format("%.2f",premiumcount),5,null,true,false); 
				CommonCreatePDf.createCell(f, table,"交费方式",1,null,true,false);
				CommonCreatePDf.createCell(f, table,"一次性交清/趸交",5,null,true,false);
				
				CommonCreatePDf.createCell(f, table,"交费形式",null,null,true,false); 
				CommonCreatePDf.createCell(f, table,getMonetInType(isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())),5,null,false,false); 
				
				CommonCreatePDf.createCell(f, table,"交费账户银行",null,null,true,false); 
			    CommonCreatePDf.createCell(f, table,getBankName(grpInsurAppl.getPaymentInfo().getBankCode()),null,null,true,false); 
				CommonCreatePDf.createCell(f, table,"交费账户开户名称",null,null,true,false); 
				CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankAccName())?" ":grpInsurAppl.getPaymentInfo().getBankAccName(),null,null,true,false); 
				CommonCreatePDf.createCell(f, table,"账号",null,null,true,false);
				CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankAccNo())?" ":grpInsurAppl.getPaymentInfo().getBankAccNo(),null,null,true,false); 
//				
//				CommonCreatePDf.createCell(f, table,"交费开户银行",null,null,true,false); 
//				CommonCreatePDf.createCell(f, table," ",null,null,true,false); 
//				CommonCreatePDf.createCell(f, table,"交费账户\n开户名称",null,null,true,false); 
//				CommonCreatePDf.createCell(f, table," ",null,null,true,false); 
//				CommonCreatePDf.createCell(f, table,"账号",null,null,true,false); 
//				CommonCreatePDf.createCell(f, table," ",null,null,true,false); 
				
				CommonCreatePDf.createCell(f, table,"争议处理方式",null,null,true,false); 
			    CommonCreatePDf.createCell(f, table,"1".equals(grpInsurAppl.getArgueType())?"√诉讼  □仲裁（若选择仲裁,请在次数明确填写全称:          仲裁委员会）\n(若选择仲裁选项但未明确写仲裁委员会的名称，或填写了不存在的仲裁委员会，则仲裁约定无效)":"□诉讼  √仲裁（若选择仲裁,请在次数明确填写全称:"+ grpInsurAppl.getArbitration()+")\n(若选择仲裁选项但未明确写仲裁委员会的名称，或填写了不存在的仲裁委员会，则仲裁约定无效)",5,null,false,false); 
				
			    CommonCreatePDf.createCell(f, table,"六、告知事项（说明：对投保单及告知内容，我公司承担保密责任。）",6,null,false,true);
			    CommonCreatePDf.createCell(f, table,"1．是否在施工现场为施工人员提供了《中华人民共和国劳动法》、《中华人民共和国安全生产法》、《中华人民共和国\n"
			    		+ "  建筑法》等法律法规要求的安全技术、安全防护措施和其它维护安全、防范危险、预防火灾等措施？\n"
			    		+ "  □是 □否\n"
			    		+ "2．施工单位或者所承办的工程过去三年是否发生过死亡或伤残情况？（若“是”，请告知人数。）\n"
			    		+ "  □是 □否\n"
			    		+ "  疾病死亡           人； 疾病伤残            人； 意外死亡            人； 意外伤残            人\n"
			    		+ "3．施工单位或所承办工程过去二年内是否发生过安全管理体系四级以上（含四级）重大安全生产事故？\n"
			    		+ "  □是 □否",6,null,false,false);
			    CommonCreatePDf.createCell(f, table,"七、其他约定（备注栏）：",6,null,false,true);
			    if(null!=grpInsurAppl.getConventions()){
			    	 if(!StringUtils.isBlank(grpInsurAppl.getConventions().getPolConv())){
					     CommonCreatePDf.createCell(f, table,grpInsurAppl.getConventions().getPolConv()+"\n" ,6,null,false,false);
			    	 }else{
			    		 CommonCreatePDf.createCell(f, table,"“无”：\n\n\n\n",6,null,false,false);			    	 }
			     }else{
			    	 CommonCreatePDf.createCell(f, table,"“无”：\n\n\n\n",6,null,false,false);
			     }
			    
			    CommonCreatePDf.createCell(f, table,"八、投保人及被保险人声明",6,null,false,true);
			    CommonCreatePDf.createCell(f, table,"  贵公司已对保险合同的条款内容履行了说明义务，并对责任免除条款履行了明确说明义务，投保单位已仔细阅知，理解投保提示\n"
			    		+"及保险条款尤其是责任免除解除合同等规定，并同意遵守，所填投保单各项及告知事项均属事实并确无欺瞒，上述一切陈述及本声\n"
			    		+"明将成为贵公司承保的依据，并作为保险合同的一部分，如有不实告知，贵公司有权在法定期限内解除合同，并依法决定是否对合同\n"
			    		  +"解除前发生的保险事故承担保险责任，\n"
			    		+"  特授权本投保单所填写的联系人(身份证号；"
			    		 +"                           )为我公司日常业务的办理人，联系人可持\n"
			    		 +"我公司相关资料，办理我公司保险日常业务，本授权委托自签发之日起生效．\n\n"
			    		+"  投保人或授权人签字                                                      "
			    		+"投保人盖章\n\n"
			    		+ "                                                                        投保申请日期         年     月      日",6,null,false,false);

			    doc.add(table);
			    Phrase ps = new Phrase("受理机构:                                           经办:                                           受理日期:", f);     
			    doc.add(ps);
			    doc.close();
		
			
	}
	/**
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws Exception 
	 * @throws IOException 
	 * @throws DocumentException  
	* @Title: createConcurrent 
	* @Description: 清汇
	* @param @param out
	* @param @param grpInsurAppl    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public static void createConcurrent(ByteArrayOutputStream out,GrpInsurAppl grpInsurAppl,Double moneyinCount) throws DocumentException, MalformedURLException, IOException{
		 Document doc=new Document(PageSize.A4 ); 
				PdfWriter pdf = PdfWriter.getInstance(doc, out);
				BaseFont font = BaseFont.createFont(PropertiesUtils.getProperty("CreateGrpIsurApplService.font.url")+",1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);// 解决jar包问题 读取本地字体 
				Font f = new Font(font, 8);  //设置表格内容字体
				Font f1 = new Font(font, 10,Font.BOLD); 
				//打开documen
				doc.open();  
				CommonCreatePDf.CreateTitle(pdf, doc, font, f, "* "+grpInsurAppl.getApplNo() +" *","汇交申请书(乙)" ,grpInsurAppl);

				// 人(详见被保险人清单：□ 按汇交人打印     □ 按被保险人打印)
				//按照一定比例生成表格
				float[] widths = {0.075f, 0.075f, 0.15f, 0.075f,0.075f,0.075f,0.15f,0.1f,0.125f,0.1f};  
				PdfPTable table = new PdfPTable(widths);
				  //100%  
		        System.out.println(new Chunk(new LineSeparator()));
		         table.setWidthPercentage(100);  
				String str="公司提示:\n"
						+ "1. 本申请书由3汇交人填写，有关被保险人详细信息请随附《被保险人清单》。\n"
						+ "2. 汇交件里所有被保险人的投保险种,交费方式,保险费交付日期必须相同。\n"
						+ "3. 客户号是我公司为客户设定的识别码，如汇交人此前未在我们公司投保，此栏无需填写。\n"
						+ "4. 单位汇交人证件类别及行代码是指汇交单位组织机构号,税务登记号或营业执照号。]\n"
						+ "5. 填写所有内容必须真实，准确。若有不明事项请向销售人员或我公司咨询。";
				
//		        CommonCreatePDf.createCell(f, table,str,10,null,false,false);
//		        CommonCreatePDf.createCell(f1, table,"汇交人资料",10,null,true,false);
//		         
//		        CommonCreatePDf.createCell(f, table,"汇交人",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table," ",8,null,true,false);
//		     
//		        CommonCreatePDf.createCell(f, table,"汇交类别",2,4,true,false);
//		        CommonCreatePDf.createCell(f, table,"G".equals(grpInsurAppl.getListType())?"√单位汇交":"□单位汇交",null,2,true,false);
//		        CommonCreatePDf.createCell(f, table,"行业类别",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getOccClassCode())?" ":grpInsurAppl.getGrpHolderInfo().getOccClassCode(),2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"证件类型",null,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"",5,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"G".equals(grpInsurAppl.getListType())?"□个人汇交":"√个人汇交",null,2,true,false);
//		        CommonCreatePDf.createCell(f, table,"性    别",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getPsnSex())?"□ 男     □ 女":"男".equals(grpInsurAppl.getPsnListHolderInfo().getPsnSex())?"√ 男     □ 女":"□ 男     √ 女",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"出生日期",null,null,true,false);
//		        CommonCreatePDf.createCell(f, table," ",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"证件类别",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table," ",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,  " ",5,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"通讯地址",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,getAddress(grpInsurAppl.getPsnListHolderInfo().getAddress()),3,null,true,false);
//		         table.addCell(new Phrase("邮编",f));
//		         table.addCell(new Phrase(" ",f));
//		        CommonCreatePDf.createCell(f, table,"联系人",2,null,true,false);
//		         table.addCell(new Phrase(" ",f));
//		         
//		        CommonCreatePDf.createCell(f, table,"联系人手机",2,null,true,false);
//		         table.addCell(new Phrase(" ",f));
//		        CommonCreatePDf.createCell(f, table,"联系人电子邮件",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getPsnEmail())?"@":grpInsurAppl.getPsnListHolderInfo().getPsnEmail(),2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"联系人固定电话",2,null,true,false);
//		         table.addCell(new Phrase(" ",f));
//		        CommonCreatePDf.createCell(f, table,"汇交人数",2,null,true,false);
//		        CommonCreatePDf.createCell(f, table,"人(详见被保险人清单：□ 电子文件     □ 书面文件)",8,null,true,false);
				holderInfo(f1, f, table, str,  grpInsurAppl);
//汇交件	     
			
		        CommonCreatePDf.createCell(f1, table,"汇交件信息",10,null,true,false);
		        CommonCreatePDf.createCell(f, table,"险种名称",4,null,true,false);  
		        CommonCreatePDf.createCell(f, table,"保险期间",2,null,true,false);  
		        CommonCreatePDf.createCell(f, table,"总保险金额(元)",2,null,true,false);  
		        CommonCreatePDf.createCell(f, table,"总保险费(元)",2,null,true,false);
		        Double premiumcount=null;
		        if(null!=grpInsurAppl.getApplState()){
		        	//要约不等于 分为俩种情况  第一种险种为空
		        		if(isEmpty(grpInsurAppl.getApplState().getPolicyList())){
		        			CommonCreatePDf.createCell(f, table," ",4,null,true,false);  
		      		        CommonCreatePDf.createCell(f, table," ",2,5,true,false);  
		      		        CommonCreatePDf.createCell(f, table," ",2,null,true,false);  
		      		        CommonCreatePDf.createCell(f, table," ",2,null,true,false);  
		      		         for (int i = 0; i < 4; i++) {
		      		        	CommonCreatePDf. createCell(f, table," ",4,null,true,false); 
		      		        	CommonCreatePDf. createCell(f, table," ",2,null,true,false);
		      		        	CommonCreatePDf. createCell(f, table," ",2,null,true,false); 
		      				}
		      		         //险种不为空
		        		}else{
		        			int i=grpInsurAppl.getApplState().getPolicyList().size();
		        			 List<Policy> list = grpInsurAppl.getApplState().getPolicyList();
		        			CommonCreatePDf.createCell(f, table,isEmpty(list.get(0).getPolNameChn()),4,null,true,false);  
		     		      // CommonCreatePDf.createCell(f, table,null!=list.get(0).getInsurDur()?"":"`"+String.valueOf(list.get(0).getInsurDur()),2,i,true,false);  
		     		     String insurDurs=" ";
				         //保费期间等于保险期类型+保险期间 
				         	if(null!=grpInsurAppl.getApplState()){
				         		String insurDurUnit= grpInsurAppl.getApplState().getInsurDurUnit();   //保险期类型
				         		/*if(StringUtils.equals("Y",insurDurUnit)){
				        		 insurDurUnit="保险年数";
				         		}else if(StringUtils.equals("M",insurDurUnit)){
				        		 insurDurUnit="保险月数";
				         		}else if(StringUtils.equals("D",insurDurUnit)){
				        		 insurDurUnit="保险天数";
				         		}else{
				        		 insurDurUnit="其他";
				         	}*/
				         		
				         	 Integer insurDur = grpInsurAppl.getApplState().getInsurDur();  //保险期间
				        	 insurDurUnit= DUR_UNIT.valueOfKey(list.get(0).getInsurDurUnit()).getDescription();
				        	 insurDurs =list.get(0).getInsurDur()+"-"+insurDurUnit;
				        	 CommonCreatePDf.createCell(f, table,insurDurs,2,i,true,false);
				         }else{
				        	    CommonCreatePDf.createCell(f, table," ",2,i,true,false);
				         }
		     		      CommonCreatePDf.createCell(f, table,null==list.get(0).getFaceAmnt()?" ":String.format("%.2f",list.get(0).getFaceAmnt()),2,null,true,false);  
		     		        CommonCreatePDf.createCell(f, table,null==list.get(0).getPremium()?" ":String.format("%.2f",list.get(0).getPremium()),2,null,true,false);  
		     		        if(null!=list.get(0).getFaceAmnt()){
		     		        	premiumcount=list.get(0).getPremium();
		     		        }
		     		       
		     		        for (int j = 1; j < i; j++) {
		     		    	 	CommonCreatePDf. createCell(f, table,isEmpty(list.get(j).getPolNameChn()),4,null,true,false); 
		    		        	CommonCreatePDf. createCell(f, table,null==list.get(j).getFaceAmnt()?" ":String.format("%.2f",list.get(j).getFaceAmnt()),2,null,true,false);
		    		        	CommonCreatePDf. createCell(f, table,null==list.get(j).getPremium()?" ":String.format("%.2f",list.get(j).getPremium()),2,null,true,false); 
		    		        if(null!=list.get(j).getFaceAmnt()){
		    		        	if(null==premiumcount){
		    		        		premiumcount=list.get(j).getPremium();
		    		        	}else{
			    		        	premiumcount=premiumcount+list.get(j).getPremium();
		    		        		}
		    		        	}
		     		        }
		        		}
		        }
		       
		       
//		         for (int i = 0; i < 4; i++) {
//		        	CommonCreatePDf. createCell(f, table," ",4,null,true,false); 
//		        	CommonCreatePDf. createCell(f, table," ",2,null,true,false);
//		        	CommonCreatePDf. createCell(f, table," ",2,null,true,false); 
//				}
		        CommonCreatePDf.createCell(f, table,"注:请在所附被保险人清单里填写每人保险金额和每人保险费.",10,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"保险费合计",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"(大写)"+NumberToCN.number2CNMontrayUnit(premiumcount)+" (小写)"+String.format("%.2f",premiumcount),4,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"发票打印方式:□ 按汇交人打印     \n□ 按被保险人打印",4,null,true,false); 
		        
		        CommonCreatePDf.createCell(f, table,"币种",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,("CNY".equals(grpInsurAppl.getPaymentInfo().getCurrencyCode()))?"√ 人民币  □其他":"□ 人民币  √ 其他"+grpInsurAppl.getPaymentInfo().getCurrencyCode(),2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"指定生效日",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"1".equals(grpInsurAppl.getApplState().getInforceDateType())?"√指定为 "+dateTOstring(grpInsurAppl.getApplState().getDesignForceDate())+" □不指定":"□指定为    年  月  日 √不指定",4,null,true,false); 
		         
		        CommonCreatePDf.createCell(f, table,"缴费方式",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,/*"□一次性交情/ 交  □年交   □半年交  □月交  □其他"*/getMoneyItrvl(isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())),8,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"缴费形式",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,/*"□银行转账  □支(汇)票  □银行代收  □POS机  □现金  □其他"*/getMonetInType(isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinType())),8,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"交费账户银行",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,getBankName(grpInsurAppl.getPaymentInfo().getBankCode()),null,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"交费账户开户名称",2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankAccName())?" ":grpInsurAppl.getPaymentInfo().getBankAccName(),2,null,true,false); 
		        CommonCreatePDf.createCell(f, table,"账号",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getBankAccNo())?" ":grpInsurAppl.getPaymentInfo().getBankAccNo(),2,null,true,false); 

		         
		        if(StringUtils.equals("Y",grpInsurAppl.getPaymentInfo().getIsMultiPay()) && null != moneyinCount){
		        	 	CommonCreatePDf.createCell(f, table,"多期暂交项目",2,2,true,false); 
				        CommonCreatePDf.createCell(f, table,"暂交费合计 ",null,null,true,false);
				        CommonCreatePDf.createCell(f, table,"（大写）"+ NumberToCN.number2CNMontrayUnit(moneyinCount) +"（小写） "+String.format("%.2f",moneyinCount),7,null,true,false);
				        CommonCreatePDf.createCell
				        (f, table,"暂交期间 ",null,null,true,false);
				        CommonCreatePDf.createCell(f, table,"□  年        □其他         ",3,null,true,false);
				        CommonCreatePDf.createCell(f, table,"首期缴费日",2,null,true,false);
				        CommonCreatePDf.createCell(f, table,"    年    月    日 ",2,null,true,false);
		        }else{
		        		CommonCreatePDf.createCell(f, table,"多期暂交项目",2,2,true,false); 
				        CommonCreatePDf.createCell(f, table,"暂交费合计 ",null,null,true,false);
				        CommonCreatePDf.createCell(f, table,"（大写）                            （小写）                                           ",7,null,true,false);
				        CommonCreatePDf.createCell
				        (f, table,"暂交期间 ",null,null,true,false);
				        CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getPaymentInfo().getMutipayTimes()?"□"+grpInsurAppl.getPaymentInfo().getMutipayTimes()+ "年        □其他         ":"□  年        □其他         ",3,null,true,false);
				        CommonCreatePDf.createCell(f, table,"首期缴费日",2,null,true,false);
				        CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getPaymentInfo().getForeExpDate()?dateTOstring(grpInsurAppl.getPaymentInfo().getForeExpDate()):"    年    月    日 ",2,null,true,false);
		        }
		        CommonCreatePDf.createCell(f, table,"健康险给付约定项目",null,5,true,false);
		        CommonCreatePDf.createCell(f, table,"属组",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"险种名称 ",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"人数\n(人) ",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"医保情况 ",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"等待期\n(天) ",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"门诊免赔额\n(元/天或年)",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"门诊\n给付比例",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"住院免赔额\n(元/天或年)",null,null,true,false);
		        CommonCreatePDf.createCell(f, table,"住院\n给付比例 ",null,null,true,false);
		         for (int i = 0; i < 4; i++) {
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"       %",null,null,true,false); 
		        	CommonCreatePDf. createCell(f, table,"  ",null,null,true,false);
		        	CommonCreatePDf. createCell(f, table,"       %",null,null,true,false); 
				}
		        CommonCreatePDf.createCell(f, table,"其他约定(备注栏)",10,null,false,false);
		        
		        if(null!=grpInsurAppl.getConventions()){
			    	 if(!StringUtils.isBlank(grpInsurAppl.getConventions().getPolConv())){
					     CommonCreatePDf.createCell(f, table,grpInsurAppl.getConventions().getPolConv()+"\n" ,10,null,false,false);
			    	 }else{
					        CommonCreatePDf.createCell(f, table," “无” \n\n",10,null,false,false);
			    	 }
			     }else{
				        CommonCreatePDf.createCell(f, table,"“无” \n\n",10,null,false,false);
			     }
			     
		        CommonCreatePDf.createCell(f, table,"汇交人声明:\n    本汇交人自愿为上述保险合同按约定的交费标准和保险费用交付日期，向贵公司履行"
		         		+ "保险费汇交义务。同时，郑重声明凡因本汇交人原因所致保险合同效力中止责任，均有本汇交人承担。\n"
		         		+ "    \n    汇交人签字/盖章:\n"
		         		+ "                                                    申请日期:    年  月  日",10,null,false,false);
		         doc.add(table);
		         Phrase ps = new Phrase("受理机构:                                           经办:                                           受理日期:", f);     
			     doc.add(ps);
		         doc.close();
		
	}
	



	/** 
	* @Title: getMonetInType 
	* @Description: TODO生成交费类型
	* @param @param str
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	private static String getMonetInType(String str) {
		String monetInType="";
		 if("T".equals(str)){
			 monetInType="√银行转账  □支(汇)票  □银行代收  □POS机  □现金  □其他";
		 }
		 else if("Q".equals(str)){
			 monetInType="□银行转账  √支(汇)票  □银行代收  □POS机  □现金  □其他";
		 }
		 else if("R".equals(str)){
			 monetInType="□银行转账  □支(汇)票  √银行代收  □POS机  □现金  □其他";
		 }
		 else if("P".equals(str)){
			 monetInType="□银行转账  □支(汇)票  □银行代收  √POS机  □现金  □其他";
		 }
		 else if("C".equals(str)){
			 monetInType="□银行转账  □支(汇)票  □银行代收  □POS机  √现金  □其他";
		 }
		 else{
			 if("D".equals(str)){
				 monetInType="□银行转账  □支(汇)票  □银行代收  □POS机  □现金  √其他 社保结算";
			 }
			 else if("W".equals(str)){
				 monetInType="□银行转账  □支(汇)票  □银行代收  □POS机  □现金  √其他 贷款凭证";
			 }else{
				 monetInType="□银行转账  □支(汇)票  □银行代收  □POS机  □现金  □其他 ";
			 }
			 
		 }
		return monetInType;
	}

	/** 
	* @Title: getMoneyItrvl 
	* @Description: 生成交费形式
	* @param @param moneyItrvl
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	private static String getMoneyItrvl(String moneyItrvl) {
	String moneyItrvlTo="";
		if("M".equals( moneyItrvl)){    
			moneyItrvlTo="□一次性交情/趸交 □年交  □半年交  □季交 √月交 □不定期 □其他";
		 }
		 else if("Q".equals(moneyItrvl)){
			 moneyItrvlTo="□一次性交情/趸交 □年交  □半年交  √季交 □月交 □不定期 □其他";
		 }
		 else if("H".equals(moneyItrvl)){
			 moneyItrvlTo="□一次性交情/趸交 □年交   √半年交  □季交 □月交 □不定期 □其他";
		 }
		 else if("Y".equals( moneyItrvl)){
			 moneyItrvlTo="□一次性交情/趸交 √年交  □半年交  □季交 □月交 □不定期 □其他";
		 }
		 else if("W".equals( moneyItrvl)){
			 moneyItrvlTo="√一次性交情/趸交 □年交  □半年交  □季交 □月交 □不定期 □其他";
		 }
		 else if("X".equals( moneyItrvl)){
			 moneyItrvlTo="□一次性交情/趸交 □年交  □半年交  □季交 □月交 √不定期 □其他";
		 }else{
			 moneyItrvlTo="□一次性交情/趸交 □年交  □半年交  □季交 □月交 □不定期 □其他";
		 }
		return moneyItrvlTo;
	}
	/** 
	* @Title: getHealthInsur 
	* @Description: 公共保额使用范围
	* @param @param str
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	private static String getHealthInsur(String str) {
		String healthInsur="";
			if("0".equals(str)){    
				healthInsur="公共保额使用范围：√不选择公共保额(不必填写公共保额内容。) □不包括连带被保险人  □包括连带被保险人";
			 }
			 else if("1".equals(str)){
				 healthInsur="公共保额使用范围： □不选择公共保额(不必填写公共保额内容。) √不包括连带被保险人  □包括连带被保险人";
			 }
			 else if("2".equals(str)){
				 healthInsur="公共保额使用范围： □不选择公共保额(不必填写公共保额内容。) □不包括连带被保险人  √包括连带被保险人";
			 }else{
				 healthInsur="公共保额使用范围： □不选择公共保额(不必填写公共保额内容。) □不包括连带被保险人  □包括连带被保险人";
			 }
			return healthInsur;
		}
	
	
	
	/**
	 *  判断 输入字符串是否为空并返回指定数据
	 * @param str
	 * @return
	 */
	private static String isEmpty(String str){
		return StringUtils.isBlank(str)?" ":str;
	}
	
	/** 
	* @Title: isEmpty 
	* @Description: 验证集合是否为空
	* @param @param coll
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public static boolean isEmpty(Collection<?> coll){
		if(null==coll || coll.size()<=0 || coll.isEmpty()){
			return true;	
		}else{
			return false;
		}
	
	}
	/** 
	* @Title: getAddress 
	* @Description: 通讯地址
	* @param @param address
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	private static String getAddress(Address address){
		String addressString=" ";
		if(null!=address){
			//解决直辖市问题
			if(!StringUtils.equals(address.getProvince(),address.getCity())){
				 addressString+=isEmpty(address.getProvince())
						+isEmpty(address.getCity());
			}else{
				 addressString+=isEmpty(address.getProvince());
			}		
			 addressString+=isEmpty(address.getCounty())
						+isEmpty(address.getTown())
						+isEmpty(address.getVillage())
						+isEmpty(address.getHomeAddress());
		}
		return  addressString;
	}
	/** 
	* @Title: getpayment 
	* @Description: 返回结算方式
	* @param @param payment
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	private static String getpayment(PaymentInfo payment){
		String str="";
		if("N".equals(payment.getStlType())){
			str="√即时结算 □组合结算:结算限额        结算日期       □指定日期结算        □其他\n(若选择非即时结算，则每年6月30日，12月31日及合同期满日未固定结算日，此处不必填写)";
		}else if("X".equals(payment.getStlType())){
			str="□即时结算 √组合结算:结算限额 "+payment.getStlType()+" 结算日期 "+dateTOstring(payment.getStlDate().get(0))+" □指定日期结算        □其他\n(若选择非即时结算，则每年6月30日，12月31日及合同期满日未固定结算日，此处不必填写)";

		}else if("D".equals(payment.getStlType())){
			str="□即时结算 □组合结算:结算限额        结算日期       √指定日期结算"+dateTOstring(payment.getStlDate().get(0))+" □其他\n(若选择非即时结算，则每年6月30日，12月31日及合同期满日未固定结算日，此处不必填写)";
		}else if("".equals(payment.getStlType())){
			str="□即时结算 □组合结算:结算限额        结算日期       □指定日期结算        √其他"+payment.getStlRate()+"\n(若选择非即时结算，则每年6月30日，12月31日及合同期满日未固定结算日，此处不必填写)";
		}
		else{
			str="□即时结算 □组合结算:结算限额        结算日期       □指定日期结算        □其他\n(若选择非即时结算，则每年6月30日，12月31日及合同期满日未固定结算日，此处不必填写)";
		}
		return str;
	}
	/**
	 * 时间格式化成字符串
	 * 
	 * @param date
	 * @return　
	 */
	public static String dateTOstring(Date date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String str = sdf.format(date);
			return str;
	}
	//银行回显
	
	public static String getBankName(String bankCode){
		if(StringUtils.isBlank(bankCode)){
			return " ";
		}
		  switch(bankCode){
	      case "1101":
	          return "中国工商银行";
	      case "1102":
	          return "中国农业银行";
	      case "1103":
	          return "中国银行";
	      case "1104":
	          return "中国建设银行";
	      case "1105":
	          return "交通银行";
	      case "1106":
	          return "中国光大银行";
	      case "1107":
	          return "城市商业银行";
	      case "1108":
	          return "华夏银行";
	      case "1109":
	          return "民生银行";
	      case "1110":
	          return "深圳发展银行";
	      case "1111":
	          return "招商银行";
	      case "1112":
	          return "农村信用社";
	      case "1113":
	          return "上海浦东发展银行";
	      case "1114":
	          return "兴业银行";
	      case "1115":
	          return "广东发展银行";
	      case "1116":
	          return "中信实业银行";
	      case "1133":
	          return "中国人民银行";
	      case "1200":
	          return "中国邮政";
	      case "1602":
	          return "上海银行";
	      case "1603":
	          return "江苏银行";
	      case "1604":
	          return "南京市商业银行";
	      case "1615":
	          return "北京银行";
	      case "1621":
	          return "上海农村商业银行";
	      case "1628":
	          return "顺德农村商业银行";
	      case "1812":
	          return "无锡农村商业银行";
	      case "1817":
	          return "山西省晋商银行";
	      case "1818":
	          return "浙江民泰商业银行";
	      case "1819":
	          return "华融湘江银行";
	      case "1820":
	          return "浙江泰隆商业银行";
	      case "1821":
	          return "台州商业银行";
	      case "1822":
	          return "江苏江阴农村商业银行";
	      case "1824":
	          return "江南银行";
	      case "1825":
	          return "长江银行";
	      case "1826":
	          return "江苏常熟农村商业银行";
	      case "1827":
	          return "太仓农村商业银行";
	      case "1828":
	          return "江苏吴江农村商业银行";
	      case "1829":
	          return "安徽农信社";
	      case "1832":
	          return "武汉农村商业银行";
	      case "1834":
	          return "重庆银行";
	      case "1836":
	          return "苏州银行";
	      case "1840":
	          return "长沙银行";
	      case "1845":
	          return "南昌银行";
	      case "9907":
	          return "翼支付";
	      case "9998":
	          return "银联";
	      case "1685":
	          return "东莞农村商业银行";
	      default :
	    	  return " ";
		  }
	}
	public static void holderInfo(Font f1, Font f, PdfPTable table, String str,GrpInsurAppl grpInsurAppl){
		if(StringUtils.equals("G",grpInsurAppl.getSgType())){
			//□单位汇交"√个人汇交√ 男     □ 女
		     CommonCreatePDf.createCell(f, table,str,10,null,false,false);
		     CommonCreatePDf.createCell(f1, table,"汇交人资料",10,null,true,false);
		     CommonCreatePDf.createCell(f, table,"汇交人",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpName()),8,null,true,false);
		     CommonCreatePDf.createCell(f, table,"汇交类别",2,4,true,false);
		     CommonCreatePDf.createCell(f, table,"√单位汇交",null,2,true,false);
		     CommonCreatePDf.createCell(f, table,"行业类别",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,isEmpty( OCC_CLASS_CODE.valueOfKey(grpInsurAppl.getGrpHolderInfo().getOccClassCode()).getDescription()),2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"证件类型",null,null,true,false);
		     CommonCreatePDf.createCell(f, table,isEmpty(GRP_ID_TYPE.valueOfKey(grpInsurAppl.getGrpHolderInfo().getGrpIdType()).getDescription()),2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpIdNo()),5,null,true,false);
		     CommonCreatePDf.createCell(f, table,"□个人汇交",null,2,true,false);
		     CommonCreatePDf.createCell(f, table,"性    别",2,null,true,false);
		     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"出生日期",null,null,true,false);
		     CommonCreatePDf.createCell(f, table,"    年  月  日",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"证件类别",2,null,true,false);
		     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,  " ",5,null,true,false);
		     CommonCreatePDf.createCell(f, table,"通讯地址",2,null,true,false);
		     CommonCreatePDf.createCell(f, table, getAddress(grpInsurAppl.getGrpHolderInfo().getAddress()),3,null,true,false);
		      table.addCell(new Phrase("邮编",f));
		      table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode()),f));
		     CommonCreatePDf.createCell(f, table,"联系人",2,null,true,false);
		      table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getContactName()),f));
		      
		     CommonCreatePDf.createCell(f, table,"联系人手机",2,null,true,false);
		      table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getContactMobile()),f));
		     CommonCreatePDf.createCell(f, table,"联系人电子邮件",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getGrpHolderInfo().getContactEmail()),2,null,true,false);
		     CommonCreatePDf.createCell(f, table,"联系人固定电话",2,null,true,false);
		      table.addCell(new Phrase(isEmpty(grpInsurAppl.getGrpHolderInfo().getContactTelephone()),f));
		     CommonCreatePDf.createCell(f, table,"汇交人数",2,null,true,false);
		     CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getApplState().getIpsnNum()?"人(详见被保险人清单：□ 电子文件     □ 书面文件)":grpInsurAppl.getApplState().getIpsnNum()+"人(详见被保险人清单：□ 电子文件     □ 书面文件)",8,null,true,false);
		}  //个人汇交
		else if(StringUtils.equals("P",grpInsurAppl.getSgType())){
			//□单位汇交"√个人汇交√ 男     □ 女
			  CommonCreatePDf.createCell(f, table,str,10,null,false,false);
			     CommonCreatePDf.createCell(f1, table,"汇交人资料",10,null,true,false);
			     CommonCreatePDf.createCell(f, table,"汇交人",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgName()),8,null,true,false);
			     CommonCreatePDf.createCell(f, table,"汇交类别",2,4,true,false);
			     CommonCreatePDf.createCell(f, table," □ 单位汇交",null,2,true,false);
			     CommonCreatePDf.createCell(f, table,"行业类别",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件类型",null,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",5,null,true,false);
			     CommonCreatePDf.createCell(f, table,"√个人汇交",null,2,true,false);
			     CommonCreatePDf.createCell(f, table,"性    别",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"M".equals(grpInsurAppl.getPsnListHolderInfo().getSgSex())?"√ 男     □ 女":"□ 男     √ 女",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"出生日期",null,null,true,false);
			     CommonCreatePDf.createCell(f, table,null!=grpInsurAppl.getPsnListHolderInfo().getSgBirthDate()?dateTOstring(grpInsurAppl.getPsnListHolderInfo().getSgBirthDate()):"    年  月  日",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件类别",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,isEmpty(ID_TYPE.valueOfKey(grpInsurAppl.getPsnListHolderInfo().getSgIdType()).getDescription()),2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
			     CommonCreatePDf.createCell(f, table, isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgIdNo()),5,null,true,false);
			     CommonCreatePDf.createCell(f, table,"通讯地址",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,getAddress(grpInsurAppl.getPsnListHolderInfo().getAddress()),3,null,true,false);
			      table.addCell(new Phrase("邮编",f));
			      table.addCell(new Phrase(isEmpty(grpInsurAppl.getPsnListHolderInfo().getAddress().getPostCode()),f));
			     CommonCreatePDf.createCell(f, table,"联系人",2,null,true,false);
			      table.addCell(new Phrase(isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgName()),f));
			      
			     CommonCreatePDf.createCell(f, table,"联系人手机",2,null,true,false);
			      table.addCell(new Phrase(isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgMobile()),f));
			     CommonCreatePDf.createCell(f, table,"联系人电子邮件",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgEmail())?"@":grpInsurAppl.getPsnListHolderInfo().getSgEmail(),2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"联系人固定电话",2,null,true,false);
			      table.addCell(new Phrase(isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgTelephone()),f));
			     CommonCreatePDf.createCell(f, table,"汇交人数",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,null==grpInsurAppl.getApplState().getIpsnNum()?"人(详见被保险人清单：□ 电子文件     □ 书面文件)":grpInsurAppl.getApplState().getIpsnNum()+"人(详见被保险人清单：□ 电子文件     □ 书面文件)",8,null,true,false);
		}else{
			  CommonCreatePDf.createCell(f, table,str,10,null,false,false);
			     CommonCreatePDf.createCell(f1, table,"汇交人资料",10,null,true,false);
			     CommonCreatePDf.createCell(f, table,"汇交人",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",8,null,true,false);
			     CommonCreatePDf.createCell(f, table,"汇交类别",2,4,true,false);
			     CommonCreatePDf.createCell(f, table,"□单位汇交  ",null,2,true,false);
			     CommonCreatePDf.createCell(f, table,"行业类别",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件类型",null,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",5,null,true,false);
			     CommonCreatePDf.createCell(f, table,"□个人汇交",null,2,true,false);
			     CommonCreatePDf.createCell(f, table,"性    别",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"□ 男     □ 女 ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"出生日期",null,null,true,false);
			     CommonCreatePDf.createCell(f, table,"    年  月  日",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件类别",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"证件号码",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,  " ",5,null,true,false);
			     CommonCreatePDf.createCell(f, table,"通讯地址",2,null,true,false);
			     CommonCreatePDf.createCell(f, table," ",3,null,true,false);
			      table.addCell(new Phrase("邮编",f));
			      table.addCell(new Phrase(" ",f));
			     CommonCreatePDf.createCell(f, table,"联系人",2,null,true,false);
			      table.addCell(new Phrase(" ",f));
			      
			     CommonCreatePDf.createCell(f, table,"联系人手机",2,null,true,false);
			      table.addCell(new Phrase(" ",f));
			     CommonCreatePDf.createCell(f, table,"联系人电子邮件",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgEmail())?"@":grpInsurAppl.getPsnListHolderInfo().getSgEmail(),2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"联系人固定电话",2,null,true,false);
			      table.addCell(new Phrase(" ",f));
			     CommonCreatePDf.createCell(f, table,"汇交人数",2,null,true,false);
			     CommonCreatePDf.createCell(f, table,"人(详见被保险人清单：□ 电子文件     □ 书面文件)",8,null,true,false);
		}
	}
}
