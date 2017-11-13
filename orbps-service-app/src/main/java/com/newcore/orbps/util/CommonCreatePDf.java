package com.newcore.orbps.util;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.halo.core.common.util.PropertiesUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.supports.dicts.SALES_TYPE;


/** 
* @ClassName: CreatePDf 
* @Description:  生成pdf公共方法
* @author  jiangbomeng
* @date 2016年7月27日 下午9:22:30 
*  
*/
public class  CommonCreatePDf {
	/**
	 * @param f 汉字必传 解决汉字不显示 以及样式 字体大小等
	 * @param table 生成表格 用于添加单元格
	 * @param str 单元格中显示内容 必传 不可传空值  可传空格
	 * @param col 表示合并多少列 
	 * @param rows 表示合并多少行
	 * @param isAling 是否居中
	 * @param iscolor 设置背景颜色 目前颜色固定
	 * 
	 */
	static void createCell(Font f, PdfPTable table, String str, Integer col, Integer rows, boolean isAling,
			boolean iscolor) {
		PdfPCell cell = new PdfPCell(new Phrase(str, f));
		if (null != col) {
			cell.setColspan(col);
		}
		if (null != rows) {
			cell.setRowspan(rows);
		}
		if (isAling) {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
		}
		if (iscolor) {
			cell.setBackgroundColor(new BaseColor(0, 160, 229));
		}
		table.addCell(cell);
	}

	/**
	 * @param pdf    
	 * @param doc  页面内容添加
	 * @param font 汉字显示
	 * @param f   字体样式设置
	 * @param myString   条形码下面字体
	 * @param titleName  pdf
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws Exception
	 */
	static void CreateTitle(PdfWriter pdf, Document doc, BaseFont font, Font f,String myString,String titleName,GrpInsurAppl grpInsurAppl) throws DocumentException, MalformedURLException, IOException{
		Font titleFont = new Font(font, 15, Font.BOLD);// 设置title字体
		PdfContentByte cb = pdf.getDirectContent();
		// 条形码生成
			Barcode128 code128 = new Barcode128();
			code128.setAltText(myString);
			myString=myString.replace("*", "");
			code128.setCode(myString);
			//code128.setCodeType(Barcode128.CODE128);
			Image code128Image = code128.createImageWithBarcode(cb, null, null);
			code128Image.setAbsolutePosition(350, 790);
			
			code128Image.scalePercent(125);
			doc.add(code128Image);
			Image img = Image.getInstance(PropertiesUtils.getProperty("CreateGrpIsurApplService.img.url"));
			img.scaleAbsolute(300, 50);// 控制图片大小
			img.setAbsolutePosition(140, 740);// 控制图片位置
			img.setAlignment(Image.ALIGN_RIGHT);
			doc.add(img);
			/* 生成二维码  暂时不需要
			 * Image qrcodeImage; qrcodeImage = qrcode.getImage();
			 * qrcodeImage.setAbsolutePosition(10,600);
			 * qrcodeImage.scalePercent(200); doc.add(qrcodeImage);
			 * */
			Paragraph title = new Paragraph(titleName, titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(-40f);
			title.setSpacingBefore(40f);
			title.setLeading(40f);
			doc.add(title);
			String salesBranchNo= "销售机构号码:\n";
			String salesChannel = "销售渠道:\n";
			String deptNo = "代理机构名称:\n";
			String deptName = "代理机构号码:\n";
			String salesName = "销售人员姓名:\n";
			String salesNo = "销售人员代码:                                            投保人客户号:";
			if(null!=grpInsurAppl.getSalesInfoList()&&grpInsurAppl.getSalesInfoList().size()>0){
				if(grpInsurAppl.getSalesInfoList().size()==1){
					SalesInfo sa = grpInsurAppl.getSalesInfoList().get(0);
					salesBranchNo = "销售机构号码:"+isEmpty(sa.getSalesBranchNo())+"\n";
					salesChannel = "销售渠道:"+getSalesChannelName(sa.getSalesChannel())+"\n";
					deptNo = "代理机构名称:"+isEmpty(sa.getSalesDeptNo())+"\n";
					deptName = "代理机构号码:"+isEmpty(sa.getDeptName())+"\n";
					 salesName = "销售人员姓名:"+isEmpty(sa.getSalesName())+"\n";
					 if(StringUtils.equals("G",grpInsurAppl.getCntrType()) || StringUtils.equals("G",grpInsurAppl.getSgType())){
							if (grpInsurAppl.getGrpHolderInfo() != null) {
								salesNo = "销售人员代码:"+isEmpty(sa.getSalesNo())+"                                            投保人客户号:"+isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
								}
							}	else if(StringUtils.equals("P",grpInsurAppl.getSgType())){
								salesNo = "销售人员代码:"+isEmpty(sa.getSalesNo())+"                                            投保人客户号:"+isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
								}else{
									salesNo = "销售人员代码:"+isEmpty(sa.getSalesNo())+"                                            投保人客户号:";
								}
				}else{
					for (SalesInfo sa : grpInsurAppl.getSalesInfoList()) {
						if(StringUtils.equals(sa.getDevelMainFlag(), "1")){
							salesBranchNo = "销售机构号码:"+isEmpty(sa.getSalesBranchNo())+"\n";
							salesChannel = "销售渠道:"+getSalesChannelName(sa.getSalesChannel())+"\n";
							deptNo = "代理机构名称:"+isEmpty(sa.getSalesDeptNo())+"\n";
							deptName = "代理机构号码:"+isEmpty(sa.getDeptName())+"\n";
							 salesName = "销售人员姓名:"+isEmpty(sa.getSalesName())+"\n";
							 if(StringUtils.equals("G",grpInsurAppl.getCntrType())||StringUtils.equals("G",grpInsurAppl.getSgType())){
									if (grpInsurAppl.getGrpHolderInfo() != null) {
										salesNo = "销售人员代码:"+isEmpty(sa.getSalesNo())+"                                            投保人客户号:"+isEmpty(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
									}
								}else if(StringUtils.equals("P",grpInsurAppl.getSgType())){
										salesNo = "销售人员代码:"+isEmpty(sa.getSalesNo())+"                                            投保人客户号:"+isEmpty(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
										}else{
											salesNo = "销售人员代码:"+isEmpty(sa.getSalesNo())+"                                            投保人客户号:";
										}
						}
						
					}
				}
				
			}
			doc.add((new Phrase(salesBranchNo, f)));
			doc.add(new Phrase(salesChannel, f));
			doc.add(new Phrase(deptNo, f));
			doc.add(new Phrase(deptName, f));
			doc.add(new Phrase(salesName, f));
			doc.add(new Phrase(salesNo, f));
	}
	
	
	/**
	 *  判断 输入字符串是否为空并返回指定数据
	 * @param str
	 * @return
	 */
	public static String isEmpty(String str){
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
	public static String getSalesChannelName(String str){
		if(StringUtils.isBlank(str)){
			return " ";
		}
	   switch (str) {
       case "BS":
           return "业务员";
       case "EB":
           return "网上直销";
       case "OA":
           return "机构代理";
       case "PA":
           return "个人代理";
       case "PH":
           return "电话销售";
       case "RD":
           return "农村网点";
       case "SD":
           return "营业网点";
       case "SP":
           return "（银保通）代理点";
       default:
    	   return " ";
       }
	}

}
