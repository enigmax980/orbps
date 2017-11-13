package com.newcore.orbps.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.util.IOUtils;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.InsuredFileCreatService;
import com.newcore.supports.dicts.CNTR_TYPE;

/**
 * 
 * @Description 
 * @author zhoushoubo
 * @date 2016年12月15日下午7:41:00
 */
@Service("insuredFileCreatService")
public class InsuredFileCreatServiceImpl implements InsuredFileCreatService {
	@Autowired
	private MongoBaseDao mongoBaseDao;
	
	private static Logger logger = LoggerFactory.getLogger(InsuredFileCreatServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public RetInfo creatExcelFile(String applNo) throws IOException {
		RetInfo retInfo = new RetInfo();
		int subscript = 0;
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isEmpty(applNo.trim())){
			throw new BusinessException("0001", "参数投保单号为空");
		}
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if (grpInsurAppl == null || StringUtils.isEmpty(grpInsurAppl.getApplNo())) {
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("0");
			retInfo.setErrMsg("保单基本信息不存在!");
			return retInfo;
		}
		List<String> polCodes = new ArrayList<>();
		List<GrpInsured> grpInsuredList = (List<GrpInsured>)mongoBaseDao.find(GrpInsured.class, map);
		if (grpInsuredList == null) {
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("0");
			retInfo.setErrMsg("被保险人信息不存在!");
			return retInfo;
		}
		for (GrpInsured grpInsured : grpInsuredList) {
			List<SubState> subStates = grpInsured.getSubStateList();
			if (subStates != null && !subStates.isEmpty()) {
				setPolCodes(polCodes, subStates);
			}
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFRow headrow = sheet.createRow(0);
		createcell(headrow, subscript++, "处理标记", cellStyle);
		createcell(headrow, subscript++, "处理状态", cellStyle);
		createcell(headrow, subscript++, "被保险人编号", cellStyle);
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			createcell(headrow, subscript++, "投保人姓名", cellStyle);
			createcell(headrow, subscript++, "投保人性别", cellStyle);
			createcell(headrow, subscript++, "投保人出生日期", cellStyle);
			createcell(headrow, subscript++, "投保人证件类别", cellStyle);
			createcell(headrow, subscript++, "投保人证件号码", cellStyle);
			createcell(headrow, subscript++, "投保人手机号", cellStyle);
			createcell(headrow, subscript++, "与投保人关系", cellStyle);
		}
		createcell(headrow, subscript++, "被保险人姓名", cellStyle);
		createcell(headrow, subscript++, "主被保人编号", cellStyle);
		createcell(headrow, subscript++, "与主被保险人关系", cellStyle);
		createcell(headrow, subscript++, "被保险人类型", cellStyle);
		createcell(headrow, subscript++, "性别", cellStyle);
		createcell(headrow, subscript++, "出生日期", cellStyle);
		createcell(headrow, subscript++, "证件类别", cellStyle);
		createcell(headrow, subscript++, "证件号码", cellStyle);
		createcell(headrow, subscript++, "被保人手机号", cellStyle);
		createcell(headrow, subscript++, "被保人电子邮箱", cellStyle);
		createcell(headrow, subscript++, "职业代码", cellStyle);
		createcell(headrow, subscript++, "工作地点", cellStyle);
		createcell(headrow, subscript++, "工号", cellStyle);
		createcell(headrow, subscript++, "在职标志", cellStyle);
		createcell(headrow, subscript++, "医保标识", cellStyle);
		createcell(headrow, subscript++, "医保代码", cellStyle);
		createcell(headrow, subscript++, "医保编号", cellStyle);
		createcell(headrow, subscript++, "异常告知", cellStyle);
		createcell(headrow, subscript++, "收费属组编号", cellStyle);
		createcell(headrow, subscript++, "组织层次代码", cellStyle);
		if (StringUtils.equals(grpInsurAppl.getAccessSource(), "GCSS")) {
			for (int i = 0; i < polCodes.size(); i++) {
				createcell(headrow, subscript++, "险种" + (i + 1) + "代码", cellStyle);
				createcell(headrow, subscript++, "险种" + (i + 1) + "保额", cellStyle);
				createcell(headrow, subscript++, "险种" + (i + 1) + "保费", cellStyle);
				createcell(headrow, subscript++, "险种" + (i + 1) + "属组", cellStyle);
			}
		} else if (grpInsurAppl.getIpsnStateGrpList() != null && !grpInsurAppl.getIpsnStateGrpList().isEmpty()) {
			createcell(headrow, subscript++, "险种属组", cellStyle);
		} else {
			for (int i = 0; i < polCodes.size(); i++) {
				createcell(headrow, subscript++, "险种" + (i + 1) + "代码", cellStyle);
				createcell(headrow, subscript++, "险种" + (i + 1) + "保额", cellStyle);
				createcell(headrow, subscript++, "险种" + (i + 1) + "保费", cellStyle);
			}
		}
		createcell(headrow, subscript++, "受益人姓名", cellStyle);
		createcell(headrow, subscript++, "受益人受益顺序", cellStyle);
		createcell(headrow, subscript++, "受益人受益份额", cellStyle);
		createcell(headrow, subscript++, "受益人性别", cellStyle);
		createcell(headrow, subscript++, "受益人出生日期", cellStyle);
		createcell(headrow, subscript++, "受益人证件类别", cellStyle);
		createcell(headrow, subscript++, "受益人证件号码", cellStyle);
		createcell(headrow, subscript++, "受益人手机号", cellStyle);
		createcell(headrow, subscript++, "受益人是被保险人的", cellStyle);
		createcell(headrow, subscript++, "同业公司人身保险保额合计", cellStyle);
		createcell(headrow, subscript++, "个人交费金额", cellStyle);
		createcell(headrow, subscript++, "单位交费金额", cellStyle);
		createcell(headrow, subscript++, "账户顺序", cellStyle);
		createcell(headrow, subscript++, "个人账户扣款金额", cellStyle);
		createcell(headrow, subscript++, "个人账户交费比例", cellStyle);
		createcell(headrow, subscript++, "开户名", cellStyle);
		createcell(headrow, subscript++, "开户银行", cellStyle);
		createcell(headrow, subscript++, "开户帐号", cellStyle);
		createcell(headrow, subscript++, "账户顺序", cellStyle);
		createcell(headrow, subscript++, "个人账户扣款金额", cellStyle);
		createcell(headrow, subscript++, "个人账户交费比例", cellStyle);
		createcell(headrow, subscript++, "开户名", cellStyle);
		createcell(headrow, subscript++, "开户银行", cellStyle);
		createcell(headrow, subscript++, "开户帐号", cellStyle);
		createcell(headrow, subscript, "被保人作废原因", cellStyle);
		for (int i = 0; i < grpInsuredList.size(); i++) {
			subscript = 0;
			GrpInsured grpInsured = grpInsuredList.get(i);
			// 创建行
			HSSFRow row = sheet.createRow(i + 1);
			createcell(row, subscript++, grpInsured.getProcFlag(), cellStyle);
			createcell(row, subscript++, grpInsured.getProcStat(), cellStyle);
			createcell(row, subscript++,
					grpInsured.getIpsnNo() == null ? "" : grpInsured.getIpsnNo().toString(), cellStyle);
			if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey()) && grpInsured.getHldrInfo() != null) {
				createcell(row, subscript++, grpInsured.getHldrInfo().getHldrName(), cellStyle);
				createcell(row, subscript++, grpInsured.getHldrInfo().getHldrSex(), cellStyle);
				createcell(row, subscript++, grpInsured.getHldrInfo().getHldrBirthDate() == null ? ""
						: DateFormatUtils.format(grpInsured.getHldrInfo().getHldrBirthDate(), "yyyy/MM/dd"),
						cellStyle);
				createcell(row, subscript++, grpInsured.getHldrInfo().getHldrIdType(), cellStyle);
				createcell(row, subscript++, grpInsured.getHldrInfo().getHldrIdNo(), cellStyle);
				createcell(row, subscript++, grpInsured.getHldrInfo().getHldrMobilePhone(), cellStyle);
				createcell(row, subscript++, grpInsured.getRelToHldr(), cellStyle);
			} else if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey()) && grpInsured.getHldrInfo() == null) {
				subscript += 7;
			}
			createcell(row, subscript++, grpInsured.getIpsnName(), cellStyle);
			createcell(row, subscript++,
					grpInsured.getMasterIpsnNo() == null ? "" : grpInsured.getMasterIpsnNo().toString(),
					cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnRtMstIpsn(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnType(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnSex(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnBirthDate() == null ? ""
					: DateFormatUtils.format(grpInsured.getIpsnBirthDate(), "yyyy/MM/dd"), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnIdType(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnIdNo(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnMobilePhone(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnEmail(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnOccCode(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnCompanyLoc(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnRefNo(), cellStyle);
			createcell(row, subscript++, grpInsured.getInServiceFlag(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnSss(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnSsc(), cellStyle);
			createcell(row, subscript++, grpInsured.getIpsnSsn(), cellStyle);
			createcell(row, subscript++, grpInsured.getNotificaStat(), cellStyle);
			createcell(row, subscript++,
					grpInsured.getFeeGrpNo() == null ? "" : grpInsured.getFeeGrpNo().toString(), cellStyle);
			createcell(row, subscript++, grpInsured.getLevelCode(), cellStyle);
			if (!StringUtils.equals(grpInsurAppl.getAccessSource(), "GCSS")
					&& grpInsurAppl.getIpsnStateGrpList() != null && !grpInsurAppl.getIpsnStateGrpList().isEmpty()) {
				if (grpInsured.getSubStateList() != null && !grpInsured.getSubStateList().isEmpty()) {
					createcell(row, subscript++,
							grpInsured.getSubStateList().get(0).getClaimIpsnGrpNo() == null ? ""
									: grpInsured.getSubStateList().get(0).getClaimIpsnGrpNo().toString(),
							cellStyle);
				} else {
					subscript++;
				}
			}else{
				subscript = createSubStateLine(subscript,grpInsurAppl,polCodes, grpInsured, row,cellStyle);
			}
			if (grpInsured.getBnfrInfoList() != null && !grpInsured.getBnfrInfoList().isEmpty()) {
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrName(), cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrLevel() == null ? ""
						: grpInsured.getBnfrInfoList().get(0).getBnfrLevel().toString(), cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrProfit() == null ? ""
						: grpInsured.getBnfrInfoList().get(0).getBnfrProfit().toString(), cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrSex(), cellStyle);
				createcell(row, subscript++,
						grpInsured.getBnfrInfoList().get(0).getBnfrBirthDate() == null ? ""
								: DateFormatUtils.format(grpInsured.getBnfrInfoList().get(0).getBnfrBirthDate(),
										"yyyy/MM/dd"),
						cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrIdType(), cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrIdNo(), cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrMobilePhone(), cellStyle);
				createcell(row, subscript++, grpInsured.getBnfrInfoList().get(0).getBnfrRtIpsn(), cellStyle);
			} else {
				subscript += 9;
			}
			createcell(row, subscript++,
					grpInsured.getTyNetPayAmnt() == null ? "" : grpInsured.getTyNetPayAmnt().toString(),
					cellStyle);
			createcell(row, subscript++,
					grpInsured.getIpsnPayAmount() == null ? "" : grpInsured.getIpsnPayAmount().toString(),
					cellStyle);
			createcell(row, subscript++,
					grpInsured.getGrpPayAmount() == null ? "" : grpInsured.getGrpPayAmount().toString(),
					cellStyle);
			if (grpInsured.getAccInfoList() != null && !grpInsured.getAccInfoList().isEmpty()) {
				for (int j = 0; j < grpInsured.getAccInfoList().size(); j++) {
					createcell(row, subscript++, grpInsured.getAccInfoList().get(j).getSeqNo() == null ? ""
							: grpInsured.getAccInfoList().get(j).getSeqNo().toString(), cellStyle);
					createcell(row, subscript++, grpInsured.getAccInfoList().get(j).getIpsnPayAmnt() == null ? ""
							: grpInsured.getAccInfoList().get(j).getIpsnPayAmnt().toString(), cellStyle);
					createcell(row, subscript++, grpInsured.getAccInfoList().get(j).getIpsnPayPct() == null ? ""
							: grpInsured.getAccInfoList().get(j).getIpsnPayPct().toString(), cellStyle);
					createcell(row, subscript++, grpInsured.getAccInfoList().get(j).getBankAccName(), cellStyle);
					createcell(row, subscript++, grpInsured.getAccInfoList().get(j).getBankCode(), cellStyle);
					createcell(row, subscript++, grpInsured.getAccInfoList().get(j).getBankAccNo(), cellStyle);
				}
			}
			if (grpInsured.getAccInfoList() != null && grpInsured.getAccInfoList().size() == 1) {
				subscript += 6;
			} else if (grpInsured.getAccInfoList() == null || grpInsured.getAccInfoList().isEmpty()) {
				subscript += 12;
			}
			createcell(row, subscript, grpInsured.getRemark(), cellStyle);
		}
		// 创建文件保存路径
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsnlst.ipsn.dir"));
		if (sb.lastIndexOf("/") != sb.length() - 1)
			sb.append("/");
		File repositoryBase = new File(sb.toString());
		if (!repositoryBase.exists())
			repositoryBase.mkdirs();
        
		File file = new File(sb.toString() + "/" + applNo + ".xls");
		FileOutputStream out = null;
		try {			
			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
			IOUtils.close(out);
			IOUtils.close(workbook);
			
		} finally {
			if (null != out) {
				safeClose(out);
			}
		}
		retInfo.setRetCode("1");
		retInfo.setErrMsg("清单文件导出成功");
		return retInfo;
	}

	private void setPolCodes(List<String> polCodes, List<SubState> subStates) {
		for (SubState subState : subStates) {
			if (!polCodes.contains(subState.getPolCode())) {
				polCodes.add(subState.getPolCode());
			}
		}
	}
	private int createSubStateLine(int subscript,GrpInsurAppl grpInsurAppl,List<String> polCodes,GrpInsured grpInsured,HSSFRow row,HSSFCellStyle cellStyle){
		for(int j=0;j<polCodes.size();j++){
			if(grpInsured.getSubStateList()==null || grpInsured.getSubStateList().isEmpty()){
				if(StringUtils.equals(grpInsurAppl.getAccessSource(), "GCSS")){
					subscript+=4;
				}else{
					subscript+=3;
				}
				continue;
			}
			for (SubState subState : grpInsured.getSubStateList()) {
				if (StringUtils.equals(polCodes.get(j), subState.getPolCode())
						&& StringUtils.equals(grpInsurAppl.getAccessSource(), "GCSS")) {
					createcell(row, subscript++, subState.getPolCode(), cellStyle);
					createcell(row, subscript++, subState.getFaceAmnt() == null ? "" : subState.getFaceAmnt().toString(),
							cellStyle);
					createcell(row, subscript++, subState.getPremium() == null ? "" : subState.getPremium().toString(),
							cellStyle);
					createcell(row, subscript++,
							subState.getClaimIpsnGrpNo() == null ? "" : subState.getClaimIpsnGrpNo().toString(),
							cellStyle);
				} else if (StringUtils.equals(polCodes.get(j), subState.getPolCode())) {
					createcell(row, subscript++, subState.getPolCode(), cellStyle);
					createcell(row, subscript++, subState.getFaceAmnt() == null ? "" : subState.getFaceAmnt().toString(),
							cellStyle);
					createcell(row, subscript++, subState.getPremium() == null ? "" : subState.getPremium().toString(),
							cellStyle);
				}
			}
		}
		return subscript;
	}

	// 写一行excel文件
	private static void createcell(HSSFRow row, int index, String value, HSSFCellStyle cellStyle) {
		// 创建单元格（左上端）
		HSSFCell cell = row.createCell(index);
		// 定义单元格为字符串类型
		cell.setCellType(CellType.STRING);
		// 格式
		cell.setCellStyle(cellStyle);
		// 在单元格中输入一些内容
		cell.setCellValue(value);
	}

	public static void safeClose(FileOutputStream fos) {
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}	
}
