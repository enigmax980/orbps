package com.newcore.orbps.web.sendreceipt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.exception.BusinessException;
import com.halo.core.filestore.api.FileStoreService;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.InsurApplCvTask;
import com.newcore.orbps.models.service.bo.QueryReceipt;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.web.vo.sendreceipt.ReceiptCvTaskRuleVo;
import com.newcore.orbps.models.web.vo.sendreceipt.ReceiptCvTaskVo;
import com.newcore.orbps.models.web.vo.sendreceipt.SendReceiptVo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.OrbpsInsurApplCvTaskServer;
import com.newcore.orbps.service.api.QueryInsurApplCvTaskServer;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * @author huangLong
 * @date 2016年8月30日 内容:回执核销控制
 */

@Controller
@RequestMapping("/orbps/sendreceipt/CvTask")
public class InsurApplCvTaskController {
    /**
     * 日志对象.
     */
    private static Logger logger = LoggerFactory.getLogger(InsurApplCvTaskController.class);

    RetInfo retInfo = new RetInfo();
    LinkedList<SendReceiptVo> files = null;
    SendReceiptVo sendReceiptVo = null;
    @Autowired
    OrbpsInsurApplCvTaskServer orbpsInsurApplCvTaskServer;
    @Autowired
    QueryInsurApplCvTaskServer queryInsurApplCvTaskServer;
    @Autowired
    PageQueryService pageQueryService;
    @Autowired
    InsurApplServer insurApplServer;
    @Resource
    FileStoreService fileStoreService;
    @Autowired
    BranchService branchService;
	/**
	 * 批量核销
	 * 
	 * @param request
	 * @author xiaoYe
	 * @return files
	 * @date 2016-9-22 19 :17:33
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody LinkedList<SendReceiptVo> upload(
			MultipartHttpServletRequest request, HttpServletResponse response) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		files = new LinkedList<SendReceiptVo>();
		MultipartFile multipartFile = request.getFile("multipartFile");
		InputStream in = null;
		try {
			in = multipartFile.getInputStream();
			// 上传成功后读取Excel表格里面的数据
			files = loadGrayListInfo(in);
		} catch (Exception e) {
			logger.error("异常", e);
			try {
				response.getWriter().print("{success:false}");
			} catch (IOException e1) {
				logger.error("IO异常", e1);
			}
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
    protected LinkedList<SendReceiptVo> loadGrayListInfo(InputStream in) throws IOException {
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
                sendReceiptVo = new SendReceiptVo();
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
                            String applNo = value;
                            sendReceiptVo.setApplNo(applNo);
                        } else {
                            String signDate = value;
                            sendReceiptVo.setSignDate(signDate);
                        }
                    }
                }
                files.add(sendReceiptVo);
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

    /**
     * 核销处理
     * 
     * @author wangyanjie
     * @param receiptCvTaskVo
     * @return
     */
    @RequestMapping(value = "/implement")
    public @ResponseMessage RetInfo imple(@CurrentSession Session session,@RequestBody ReceiptCvTaskVo receiptCvTaskVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (receiptCvTaskVo.getApplNo() != null) {
            insurApplCvTask.setApplNo(receiptCvTaskVo.getApplNo());
        }
        if (receiptCvTaskVo.getCgNo() != null) {
            insurApplCvTask.setCgNo(receiptCvTaskVo.getCgNo());
        }
        try {
            insurApplCvTask.setSignDate(sdf.parse(receiptCvTaskVo.getSignDate()));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        // 操作员机构操作员代码 从session中获取
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        insurApplCvTask.setOclkBranchNo(sessionModel.getBranchNo());
        insurApplCvTask.setOclkClerkNo(sessionModel.getClerkNo());
        insurApplCvTask.setOclkClerkName(sessionModel.getName());
        return orbpsInsurApplCvTaskServer.insurApplCvTask(insurApplCvTask);
    }


	
	
	 /**
     * 文件上传批量核销
     * 
     * @param request
     * @author xiaoYe
     * @return files
     * @date 2016-9-22 19 :17:33
     */
    @RequestMapping(value = "/uploads/{batchNo}", method = RequestMethod.POST)
    @ResponseMessage
    public RetInfoObject<RetInfo> uploads(@CurrentSession Session session, MultipartFile multipartFile, @PathVariable("batchNo") String batchNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        String pclkBranchNo = sessionModel.getBranchNo();//机构代码
        String pclkName = sessionModel.getName();//操作员姓名
        String pclkNo = sessionModel.getClerkNo();//操作员工号
        InputStream in = null;
        OutputStream out = null;
        RetInfoObject<RetInfo> retInfo=null;
        String sourceDirectory = PropertiesUtils.getProperty("fs.ipsnlst.ipsn.dir");
        try {
            in = multipartFile.getInputStream();
            File parentfile = new File(sourceDirectory);
            if (!parentfile.exists()) {
                parentfile.mkdirs();
            }
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = format.format(date);
            String fileResource = null;
            
            //获取上传的文件名
            String fileName = multipartFile.getOriginalFilename();
            String endwith = fileName.substring(fileName.indexOf("."));
            if (fileName.endsWith("xlsx")){
                //2007
                fileResource = parentfile + File.separator + time + ".xlsx";
            	
            } else if (fileName.endsWith("xls")){
                //2003
                fileResource = parentfile + File.separator + time + ".xls";	
            } else {
            	fileResource = parentfile + File.separator + time + ".csv";
            }
            out  = new FileOutputStream(new File(fileResource));
            byte[] b = new byte[1024];
            int i;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, i);
            }
            out.flush();
//            String resourceId = fileStoreService.addResource(new File(fileResource));
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("fileName", time+endwith);
            fileMap.put("batNo", batchNo);
            fileMap.put("oclkBranchNo", pclkBranchNo);
            fileMap.put("oclkClerkName", pclkName);
            fileMap.put("oclkClerkNo", pclkNo);
            retInfo = orbpsInsurApplCvTaskServer.insurApplCvTasks(fileMap);
        } catch (Exception e) {
            logger.error("异常", e);
        } finally {
        	if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("IO异常", e);
				}
			}
        	if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("IO异常", e);
				}
			}
        }
		return retInfo;
    }
	
	
	
	
	/**
	 * 描述：查询批量核销处理进度
	 * @author wangyanjie
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value = "/queryProg")
	public @ResponseMessage JSONObject queryProg(@RequestBody String batchNo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Map<String, String> map = new HashMap<>();
		map.put("batNo",batchNo);
		Map<String, Object> map1 = queryInsurApplCvTaskServer.queryExecuteState(map);
		JSONObject jsobject = new JSONObject();
		//成功条数是否为空
		if (map1.get("succNum")!=null ){
			jsobject.put("succNum", map1.get("succNum"));
		}else{
			jsobject.put("succNum", 0);
		}
		//失败条数是否为空
		if (map1.get("failNum")!=null){
			jsobject.put("failNum", map1.get("failNum"));
		}else{
			jsobject.put("failNum", 0);
		}
		//总条数是否为空
		if (map1.get("sum")!=null){
			jsobject.put("sum", map1.get("sum"));
		}else{
			jsobject.put("sum", 0);
		}
		//异常
		if (map1.get("fail")!=null){
			jsobject.put("fail", map1.get("fail"));
		}
		return jsobject;
	}
	


    /**
     * 描述：回执查询
     * 
     * @param cell
     * @return
     */
    @RequestMapping(value = "/Cvquery")
    public @ResponseMessage DataTable<ReceiptCvTaskVo> getCvQuerySummaryList(QueryDt query,
            ReceiptCvTaskVo receiptCvTaskVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        return pageQueryService.tranToDataTable(query.getRequestId(), null);
    }

    /**
     * 查询投保人姓名
     * 
     * @param applNo
     * @return applName
     */
    @RequestMapping(value = "/queryApplName")
    public @ResponseMessage SendReceiptVo queryName(@RequestBody String applNo) {

        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurApplPara grpInsurApplPara = null;
        String[] args = null;
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isBlank(applNo)) {
            args = new String[2];
            args = applNo.split(",");
            if (null != args) {
                map.put(args[0], args[1]);
                grpInsurApplPara = insurApplServer.searchGrpInsurAppl(map);
            }

        }
        String contactName = "";
        SendReceiptVo sendReceiptVo = new SendReceiptVo();
        if (null != grpInsurApplPara && null != grpInsurApplPara.getGrpInsurAppl()) {
            if (StringUtils.equals("G", grpInsurApplPara.getGrpInsurAppl().getCntrType())
                    || StringUtils.equals("G", grpInsurApplPara.getGrpInsurAppl().getSgType())) {
                if (grpInsurApplPara.getGrpInsurAppl().getGrpHolderInfo() != null) {
                    contactName = grpInsurApplPara.getGrpInsurAppl().getGrpHolderInfo().getGrpName();
                    sendReceiptVo.setApplName(contactName);
                }
            } else {
                if (null != grpInsurApplPara.getGrpInsurAppl().getPsnListHolderInfo()) {
                    contactName = grpInsurApplPara.getGrpInsurAppl().getPsnListHolderInfo().getSgName();
                    sendReceiptVo.setApplName(contactName);
                }
            }

        }
        return sendReceiptVo;
    }

    /**
     * 契约查询 chang
     * 
     * @throws Exception
     */
    // @chang
    @RequestMapping(value = "/search")
    public @ResponseMessage DataTable<ReceiptCvTaskVo> getReceiptCvTaskVoResult(@CurrentSession Session session,
            QueryDt query, ReceiptCvTaskRuleVo receiptCvTaskRuleVo) throws Exception {
        //获取session的省级机构号
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        String sessionPrBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        
        Map<String, Object> map = new HashMap<String, Object>();
        // 是否包含下级机构
        if(!StringUtils.isBlank(receiptCvTaskRuleVo.getFindSubBranchNoFlag())){
            map.put("levelFlag", receiptCvTaskRuleVo.getFindSubBranchNoFlag());
        }else{
        	map.put("levelFlag", YES_NO_FLAG.YES.getKey());
        }
        // 投保单号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getApplNo())) {
            map.put("applNo", receiptCvTaskRuleVo.getApplNo());
        }
        // 回执核销机构号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getOclkBranchNo())) {
            map.put("oclkBranchNo", receiptCvTaskRuleVo.getOclkBranchNo());
        }
        // 销售机构号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getSalesBranchNo())) {
        	//获取销售机构的省级机构号
        	CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo2);
            String salesPrBranchNo = branchService.findProvinceBranch(receiptCvTaskRuleVo.getSalesBranchNo()); 
            if(!StringUtils.equals(sessionPrBranchNo, salesPrBranchNo)){
            	throw new BusinessException("0004","操作员只能查询本省机构下信息");
            }
            map.put("salesBranchNo", receiptCvTaskRuleVo.getSalesBranchNo());
        }else{
        	map.put("salesBranchNo", sessionPrBranchNo);
        }
        // 回执核销员工号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getOclkClerkNo())) {
            map.put("oclkClerkNo", receiptCvTaskRuleVo.getOclkClerkNo());
        }
        // 回执核销起期
        // 将后台的日期字符串转成日期送给后台。
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getStartDate())) {
            map.put("startDate", sdf.parse(receiptCvTaskRuleVo.getStartDate()));
        }
        // 回执核销止期
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getEndDate())) {
            map.put("endDate", sdf.parse(receiptCvTaskRuleVo.getEndDate()));
        }

        int page = Integer.valueOf(String.valueOf(query.getPage()));
        int size = query.getRows();
        page = (page / size) + 1;
        map.put("page", page);
        map.put("size", query.getRows());
        // 调用后台queryInsurApplCvTask方法。
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<QueryReceipt> retInfoObject = queryInsurApplCvTaskServer.queryInsurApplCvTask(map);
        // 创建PageData。
        PageData<ReceiptCvTaskVo> pageData = new PageData<ReceiptCvTaskVo>();
        if (null != retInfoObject && retInfoObject.getTotalCount() != 0) {
            // 调用queryInsurApplCvTask的getListObject()方法。进行类型转换，转换成InsurApplCvTask类型。
            // 创建arrayList，接收数据。
            List<ReceiptCvTaskVo> arrayList = new ArrayList<>();
            // yyyy-MM-dd HH:mm
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // yyyy-MM-dd
            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
            for (QueryReceipt insurApplCvTask : retInfoObject.getData()) {
                ReceiptCvTaskVo receiptCvTaskVo1 = new ReceiptCvTaskVo();
                // 销售机构号
                receiptCvTaskVo1.setSalesBranchNo(insurApplCvTask.getSalesBranchNo());
                // 投保单号
                receiptCvTaskVo1.setApplNo(insurApplCvTask.getApplNo());
                // 投保人（汇交人）姓名
                receiptCvTaskVo1.setSgName(insurApplCvTask.getSgName());
                // 投保申请日期
                if (null != insurApplCvTask.getApplDate()) {
                    receiptCvTaskVo1.setApplDate(simpleDateFormats.format(insurApplCvTask.getApplDate()));
                }
                // 打印时间
                if (null != insurApplCvTask.getPrintDate()) {
                    receiptCvTaskVo1.setPrintDate(simpleDateFormat.format(insurApplCvTask.getPrintDate()));
                }
                // 合同送达日期
                if (null != insurApplCvTask.getSignDate()) {
                    receiptCvTaskVo1.setSignDate(simpleDateFormats.format(insurApplCvTask.getSignDate()));
                }
                // 合同送达方式
                receiptCvTaskVo1.setCntrSendType(insurApplCvTask.getCntrSendType());
                // 回执核销操作时间
                if (null != insurApplCvTask.getRespDate()) {
                    receiptCvTaskVo1.setRespDate(simpleDateFormat.format(insurApplCvTask.getRespDate()));
                }
                // 回执操作人员机构号
                receiptCvTaskVo1.setOclkBranchNo(insurApplCvTask.getOclkBranchNo());
                // 回执核销操作人员工号
                receiptCvTaskVo1.setOclkClerkNo(insurApplCvTask.getOclkClerkNo());
                // 回执核销操作人员姓名
                receiptCvTaskVo1.setOclkClerkName(insurApplCvTask.getOclkClerkName());
                arrayList.add(receiptCvTaskVo1);

            }
            // 当前页号的列表数据
            pageData.setData(arrayList);
            // 总记录数
            pageData.setTotalCount(retInfoObject.getTotalCount());
        }

        return pageQueryService.tranToDataTable(query.getRequestId(), pageData);
    }

    @RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download() throws IOException {
        StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsnlst.template.dir"));

        File file = new File(sb + File.separator + "批量核销导入模版.xlsx");
        // 处理显示中文文件名的问题
        String fileName = new String(file.getName().getBytes("utf-8"), "ISO-8859-1");

        // 设置请求头内容,告诉浏览器代开下载窗口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    
    /**
     * 批量导入模板下载
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/downloadListImport", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadListImport() throws IOException {
        StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsnlst.template.dir"));

        File file = new File(sb + File.separator + "批量导入模版.xlsx");
        // 处理显示中文文件名的问题
        String fileName = new String(file.getName().getBytes("utf-8"), "ISO-8859-1");

        // 设置请求头内容,告诉浏览器代开下载窗口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
    
    /***
     * 导出excle
     * 
     * @param request
     * @param response
     * @throws ParseException
     */
    @RequestMapping(value = "/outExcel")
    public @ResponseMessage List<ReceiptCvTaskVo> outExcle(@CurrentSession Session session,@RequestBody ReceiptCvTaskRuleVo receiptCvTaskRuleVo)
            throws ParseException {
    	 //获取session的省级机构号
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        String sessionPrBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());

        Map<String, Object> map = new HashMap<String, Object>();
        // 是否包含下级机构
        if(!StringUtils.isBlank(receiptCvTaskRuleVo.getFindSubBranchNoFlag())){
            map.put("levelFlag", receiptCvTaskRuleVo.getFindSubBranchNoFlag());
        }else{
        	map.put("levelFlag", YES_NO_FLAG.YES.getKey());
        }
        // 销售机构号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getSalesBranchNo())) {
        	//获取销售机构的省级机构号
        	CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo2);
            String salesPrBranchNo = branchService.findProvinceBranch(receiptCvTaskRuleVo.getSalesBranchNo()); 
            if(!StringUtils.equals(sessionPrBranchNo, salesPrBranchNo)){
            	throw new BusinessException("0004","操作员只能查询本省机构下信息");
            }
            map.put("salesBranchNo", receiptCvTaskRuleVo.getSalesBranchNo());
        }else{
        	map.put("salesBranchNo", sessionPrBranchNo);
        }
        // 投保单号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getApplNo())) {
            map.put("applNo", receiptCvTaskRuleVo.getApplNo());
        }
        // 回执核销机构号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getOclkBranchNo())) {
            map.put("oclkBranchNo", receiptCvTaskRuleVo.getOclkBranchNo());
        }
        // 回执核销员工号
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getOclkClerkNo())) {
            map.put("oclkClerkNo", receiptCvTaskRuleVo.getOclkClerkNo());
        }
        // 回执核销起期
        // 将后台的日期字符串转成日期送给后台。
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getStartDate())) {
            map.put("startDate", sdf.parse(receiptCvTaskRuleVo.getStartDate()));
        }
        // 回执核销止期
        if (!StringUtils.isBlank(receiptCvTaskRuleVo.getEndDate())) {
            map.put("endDate", sdf.parse(receiptCvTaskRuleVo.getEndDate()));
        }
//      map.put("status", "C");
        // 调用后台queryInsurApplCvTask方法。
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<QueryReceipt> retInfoObject = queryInsurApplCvTaskServer.queryInsurApplCvTask(map);
        // 创建arrayList，接收数据。
        List<ReceiptCvTaskVo> arrayList = new ArrayList<>();
        if (null != retInfoObject && retInfoObject.getTotalCount() != 0) {
            // 调用queryInsurApplCvTask的getListObject()方法。进行类型转换，转换成InsurApplCvTask类型。
            // yyyy-MM-dd HH:mm
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // yyyy-MM-dd
            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
            for (QueryReceipt insurApplCvTask : retInfoObject.getData()) {
                ReceiptCvTaskVo receiptCvTaskVo1 = new ReceiptCvTaskVo();
                // 销售机构号
                receiptCvTaskVo1.setSalesBranchNo(insurApplCvTask.getSalesBranchNo());
                // 投保单号
                receiptCvTaskVo1.setApplNo(insurApplCvTask.getApplNo());
                // 投保人（汇交人）姓名
                receiptCvTaskVo1.setSgName(insurApplCvTask.getSgName());
                // 投保申请日期
                if (null != insurApplCvTask.getApplDate()) {
                    receiptCvTaskVo1.setApplDate(simpleDateFormats.format(insurApplCvTask.getApplDate()));
                }
                // 打印时间
                if (null != insurApplCvTask.getPrintDate()) {
                    receiptCvTaskVo1.setPrintDate(simpleDateFormat.format(insurApplCvTask.getPrintDate()));
                }
                // 合同送达日期
                if (null != insurApplCvTask.getSignDate()) {
                    receiptCvTaskVo1.setSignDate(simpleDateFormats.format(insurApplCvTask.getSignDate()));
                }
                // 合同送达方式
                receiptCvTaskVo1.setCntrSendType(insurApplCvTask.getCntrSendType());
                // 回执核销操作时间
                if (null != insurApplCvTask.getRespDate()) {
                    receiptCvTaskVo1.setRespDate(simpleDateFormat.format(insurApplCvTask.getRespDate()));
                }
                // 回执操作人员机构号
                receiptCvTaskVo1.setOclkBranchNo(insurApplCvTask.getOclkBranchNo());
                // 回执核销操作人员工号
                receiptCvTaskVo1.setOclkClerkNo(insurApplCvTask.getOclkClerkNo());
                // 回执核销操作人员姓名
                receiptCvTaskVo1.setOclkClerkName(insurApplCvTask.getOclkClerkName());
                arrayList.add(receiptCvTaskVo1);

            }
        }
        return arrayList;

    }
    
	/***
	 * 核销错误文件导出
	 * @param batchNo
	 * @return
	 */
    @RequestMapping(value = "/download/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte []> download(@PathVariable("id") String batchNo) throws IOException {    
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
        HttpHeaders headers = new HttpHeaders();
        byte[] body = null;
        HttpStatus httpState = HttpStatus.NOT_FOUND;
		File file = new File(sb+File.separator+batchNo+"_Fail"+".xlsx");
		if(file.exists()){
	        //处理显示中文文件名的问题
	        String fileName=new String(file.getName().getBytes("utf-8"),"ISO-8859-1");
	        //设置请求头内容,告诉浏览器代开下载窗口
	        body=FileUtils.readFileToByteArray(file);
	        httpState=HttpStatus.OK;
	        headers.setContentDispositionFormData("attachment",fileName );
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		}
        return new ResponseEntity<>(body,headers,httpState);    
    }
	//判断文件是否存在
	@RequestMapping(value = "/exists")
	public @ResponseMessage String fileExistsOrNo(@RequestBody String batchNo) {
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
		File file = new File(sb+File.separator+batchNo+"_Fail"+".xlsx");
		String rtn;
		if(file.exists()){
			rtn="1";
		}else{
			rtn="0";
		}
		return rtn;
	}
}