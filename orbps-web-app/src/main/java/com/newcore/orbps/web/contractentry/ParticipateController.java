package com.newcore.orbps.web.contractentry;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.para.RetInfo;

import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.PeriodComInsur;
import com.newcore.orbps.models.service.bo.PeriodComRec;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.web.vo.contractentry.participateininsurance.ParticipateInInsuranceVo;
import com.newcore.orbps.models.web.vo.contractentry.participateininsurance.ParticipateInfoVo;
import com.newcore.orbps.service.api.CommonAgreementService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.web.sendreceipt.InsurApplCvTaskController;
import com.newcore.orbps.web.util.BranchNoUtils;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;


/**
 * 参与共保
 * 
 * @author chang
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/par")
public class ParticipateController {

	 /**
     * 日志对象.
     */
    private static Logger logger = LoggerFactory.getLogger(InsurApplCvTaskController.class);

    /**
     * 保单辅助销售人员查询服务
     */
    @Autowired
    SalesmanInfoService resulsalesmanInfoService;
    /**
     * 参与共保提交信息
     */
    @Autowired
    CommonAgreementService comAgrService;
    
    @Autowired
    BranchService branchService;

    /**
     * 查询共保协议名称
     * 
     * @author wujiahui
     * @param query
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/query")
    public @ResponseMessage String search(@RequestBody ParticipateInInsuranceVo participateInInsuranceVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        if (null == participateInInsuranceVo || StringUtils.isEmpty(participateInInsuranceVo.getAgreementNo())) {
            return "入参不符要求";
        }
        CommonAgreement commonAgreement = new CommonAgreement();
        commonAgreement.setAgreementNo(participateInInsuranceVo.getAgreementNo());
        RetInfoObject<CommonAgreement> queryReturnBo = comAgrService.comAgrSerQuery(commonAgreement);
        if ("1".equals(queryReturnBo.getRetCode()) && null != queryReturnBo.getListObject()
                && !queryReturnBo.getListObject().isEmpty()) {
            return queryReturnBo.getListObject().get(0).getAgreementName();
        }
        return queryReturnBo.getErrMsg();
    }

    /**
     * 参与共保提交信息
     * 
     * @author wujiahui
     * @param submit
     * @param ParticipateInInsuranceVo
     * @return
     */
    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo grpInsurAppl(@CurrentSession Session session,
            @RequestBody ParticipateInInsuranceVo participateInInsuranceVo) {

        RetInfo retInfo = new RetInfo();
        String branchStr1 = "";
        String branchStr2 = "";

        // 创建BO
        CommonAgreement commonAgreement = new CommonAgreement();
        PeriodComRec periodComRec = new PeriodComRec();
        List<PeriodComInsur> periodComInsurlist = new ArrayList<>();

        /** 协议号 */
        commonAgreement.setAgreementNo(participateInInsuranceVo.getAgreementNo());
        /** 协议名称 */
        commonAgreement.setAgreementName(participateInInsuranceVo.getAgreementName());
        /** 交易标志I--参与共保 */
        commonAgreement.setTransFlag("I");
        /** 交接批次总人数 */
        periodComRec.setHandoverNum(participateInInsuranceVo.getNum());
        /** 交接件数 */
        periodComRec.setCntrCount(participateInInsuranceVo.getInsurNum());
        /** 交接起始日期 */
        periodComRec.setHandoverStartDate(participateInInsuranceVo.getStartPeriod());
        /** 交接终止日期 */
        periodComRec.setHandoverEndDate(participateInInsuranceVo.getEndPeriod());
        if (participateInInsuranceVo.getSalesBranchNo() != null) {
            /** 管理机构号 */
            periodComRec.setSalesBranchNo(participateInInsuranceVo.getSalesBranchNo());
            branchStr1 = participateInInsuranceVo.getSalesBranchNo();
        }
        /** 销售渠道 */
        periodComRec.setSalesChannel(participateInInsuranceVo.getSalesChannel());
        /** 销售员代码 */
        periodComRec.setSalesNo(participateInInsuranceVo.getSalesCode());
        /** 交接金额 */
        periodComRec.setAmnt(participateInInsuranceVo.getTransferAmnt());
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        /** 操作员机构号 */
        periodComRec.setPclkBranchNo(sessionModel.getBranchNo());
        /** 操作员工号 */
        periodComRec.setClerkNo(sessionModel.getClerkNo());

        /** 参与 承保单位信息 */
        if (participateInInsuranceVo.getParticipateInfoVos() != null) {
            for (int i = 0; i < participateInInsuranceVo.getParticipateInfoVos().size(); i++) {
                PeriodComInsur periodComInsur = new PeriodComInsur();
                periodComInsur.setCntrNo(participateInInsuranceVo.getParticipateInfoVos().get(i).getCntrNo());
                periodComInsur.setCustName(participateInInsuranceVo.getParticipateInfoVos().get(i).getCompanyName());
                periodComInsur
                        .setHandoverNum(participateInInsuranceVo.getParticipateInfoVos().get(i).getPerPolicyNum());
                periodComInsur.setRemark(participateInInsuranceVo.getParticipateInfoVos().get(i).getRemark());
                periodComInsurlist.add(periodComInsur);
            }
            periodComRec.setPeriodComInsurs(periodComInsurlist);
        }
        List<PeriodComRec> periodComRecs = new ArrayList<>();
        periodComRecs.add(periodComRec);
        commonAgreement.setPeriodComRecs(periodComRecs);

        branchStr2 = sessionModel.getBranchNo();
        boolean b = false;
        if (branchStr1.equals(branchStr2)) {
        	b = true;
        }else{
	        //查询session机构的下级机构号
	        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
	    	HeaderInfoHolder.setOutboundHeader(headerInfo);
	    	Branch branch = branchService.findSubBranchAll(branchStr2);
	    	List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
	    	for(String branchNo:branchNos){
	    		if(StringUtils.equals(branchStr1, branchNo)){
	    			b = true;
	    			break;
	    		}
	    	}
        }
        if(b){
	        // 调用服务
	        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
	        HeaderInfoHolder.setOutboundHeader(headerInfo);
	        retInfo = comAgrService.comAgrSerSubmit(commonAgreement);
	        return retInfo;
        }
        retInfo.setRetCode("0");
        retInfo.setErrMsg("提交失败，该操作员只能录入本级及下级机构的共保协议");
        return retInfo;
    }

    /**
     * 批量导入
     * 
     * @param request
     * @author xiaoYe
     * @return files
     * @date 2016-9-22 19 :17:33
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseMessage
    public LinkedList<ParticipateInfoVo> upload(@CurrentSession Session session, MultipartFile multipartFile) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        LinkedList<ParticipateInfoVo> files = new LinkedList<ParticipateInfoVo>();
        InputStream in = null;
        try {
            in = multipartFile.getInputStream();
            // 上传成功后读取Excel表格里面的数据
            files = loadGrayListInfo(in);
        } catch (Exception e) {
            logger.error("异常", e);
//            try {
////                response.getWriter().print("{success:false}");
//            } catch (IOException e1) {
//                logger.error("IO异常", e1);
//            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
        return files;
    }
    /**
     * 
     * 此方法描述的是： 读取excel中的domain信息 excel模板放在项目目录的conf/template文件夹下
     * 
     * @author: xiaoYe
     * @param in
     * @version: 2016年7月5日 下午2:56:47
     */
    protected LinkedList<ParticipateInfoVo> loadGrayListInfo(InputStream in) throws IOException {
        LinkedList<ParticipateInfoVo> files = new LinkedList<ParticipateInfoVo>();
        try {
            // 根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new XSSFWorkbook(in);
            // 获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            // 取得最后一行的行号
            int rowNum = sht0.getLastRowNum() + 1;
            for (int i = 1; i < rowNum; i++) {
                XSSFRow row = (XSSFRow) sht0.getRow(i);
                int cellNum = row.getLastCellNum();
                ParticipateInfoVo participateInfoVo = new ParticipateInfoVo();
                for (int j = 0; j < cellNum; j++) {
                    XSSFCell cell = row.getCell(Integer.parseInt(j + ""));
                    String value = null;
                    if (null != cell) {
                        switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            value = cell.getRichStringCellValue().getString();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            value = Double.toString(cell.getNumericCellValue());
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                                SimpleDateFormat sdf = null;
                                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else {
                                    // 日期
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                }
                                Date date = cell.getDateCellValue();
                                value = sdf.format(date);
                            } else if (cell.getCellStyle().getDataFormat() == 58) {
                                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                double values = cell.getNumericCellValue();
                                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(values);
                                value = sdf.format(date);
                            } else {
                                double values = cell.getNumericCellValue();
                                CellStyle style = cell.getCellStyle();
                                DecimalFormat format = new DecimalFormat();
                                String temps = style.getDataFormatString();
                                // 单元格设置成常规
                                if (temps.equals("General")) {
                                    format.applyPattern("#");
                                }
                                value = format.format(values);
                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            value = Boolean.toString(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            break;
                        }
                        if (j == 0) {
                            String cntrNo = value;
                            participateInfoVo.setCntrNo(cntrNo);
                        } else if (j == 1) {
                            String companyName = value;
                            participateInfoVo.setCompanyName(companyName);
                        } else if (j == 2) {
                            Integer perPolicyNum = Integer.parseInt(value);
                            participateInfoVo.setPerPolicyNum(perPolicyNum);
                        } else if (j == 3) {
                            String remark = value;
                            participateInfoVo.setRemark(remark);
                        }
                    }
                }
                files.add(participateInfoVo);
            }
        } catch (Exception e) {
            logger.error("IO异常", e);
        }

        return files;
    }

    /**
     * 描述：对表格中数值进行格式化
     * 
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell) {
        String value = null;

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            value = cell.getRichStringCellValue().getString();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            value = Double.toString(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            value = Boolean.toString(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_BLANK:
            value = "";
            break;
        default:
            break;
        }
        return value;
    }

}
