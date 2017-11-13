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
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.ErrFileCreatService;

/**
 * @author wangxiao 创建时间：2016年11月28日下午4:32:30
 */
@Service("errFileCreatService")
public class ErrFileCreatServiceImpl implements ErrFileCreatService {
	@Autowired
	private MongoBaseDao mongoBaseDao;
	
	private static Logger logger = LoggerFactory.getLogger(ErrFileCreatServiceImpl.class);

	@Override
	public void creatCsvFile(String applNo) throws IOException {
		int subscript = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		List<String> polCodes = new ArrayList<>();
		List<ErrorGrpInsured> errorGrpInsureds = mongoBaseDao.find(ErrorGrpInsured.class, map);
		for (ErrorGrpInsured errorGrpInsured : errorGrpInsureds) {
			List<SubState> subStates = errorGrpInsured.getSubStateList();
			if (subStates != null && !subStates.isEmpty()) {
				setPolCodes(polCodes, subStates);
			}
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFRow headrow = sheet.createRow(0);
		createcsv(headrow, subscript++, "处理标记", cellStyle);
		createcsv(headrow, subscript++, "被保险人编号", cellStyle);
		if (StringUtils.equals(grpInsurAppl.getCntrType(), "L")) {
			createcsv(headrow, subscript++, "投保人姓名", cellStyle);
			createcsv(headrow, subscript++, "投保人性别", cellStyle);
			createcsv(headrow, subscript++, "投保人出生日期", cellStyle);
			createcsv(headrow, subscript++, "投保人证件类别", cellStyle);
			createcsv(headrow, subscript++, "投保人证件号码", cellStyle);
			createcsv(headrow, subscript++, "投保人手机号", cellStyle);
			createcsv(headrow, subscript++, "与投保人关系", cellStyle);
		}
		createcsv(headrow, subscript++, "被保险人姓名", cellStyle);
		createcsv(headrow, subscript++, "主被保人编号", cellStyle);
		createcsv(headrow, subscript++, "与主被保险人关系", cellStyle);
		createcsv(headrow, subscript++, "被保险人类型", cellStyle);
		createcsv(headrow, subscript++, "性别", cellStyle);
		createcsv(headrow, subscript++, "出生日期", cellStyle);
		createcsv(headrow, subscript++, "证件类别", cellStyle);
		createcsv(headrow, subscript++, "证件号码", cellStyle);
		createcsv(headrow, subscript++, "被保人手机号", cellStyle);
		createcsv(headrow, subscript++, "被保人电子邮箱", cellStyle);
		createcsv(headrow, subscript++, "职业代码", cellStyle);
		createcsv(headrow, subscript++, "工作地点", cellStyle);
		createcsv(headrow, subscript++, "工号", cellStyle);
		createcsv(headrow, subscript++, "在职标志", cellStyle);
		createcsv(headrow, subscript++, "医保标识", cellStyle);
		createcsv(headrow, subscript++, "医保代码", cellStyle);
		createcsv(headrow, subscript++, "医保编号", cellStyle);
		createcsv(headrow, subscript++, "异常告知", cellStyle);
		createcsv(headrow, subscript++, "收费属组编号", cellStyle);
		createcsv(headrow, subscript++, "组织层次代码", cellStyle);
		createcsv(headrow, subscript++, "险种属组", cellStyle);
		for (int i = 0; i < polCodes.size(); i++) {
			createcsv(headrow, subscript++, "险种" + (i + 1) + "代码", cellStyle);
			createcsv(headrow, subscript++, "险种" + (i + 1) + "保额", cellStyle);
			createcsv(headrow, subscript++, "险种" + (i + 1) + "保费", cellStyle);
		}
		createcsv(headrow, subscript++, "受益人姓名", cellStyle);
		createcsv(headrow, subscript++, "受益人受益顺序", cellStyle);
		createcsv(headrow, subscript++, "受益人受益份额", cellStyle);
		createcsv(headrow, subscript++, "受益人性别", cellStyle);
		createcsv(headrow, subscript++, "受益人出生日期", cellStyle);
		createcsv(headrow, subscript++, "受益人证件类别", cellStyle);
		createcsv(headrow, subscript++, "受益人证件号码", cellStyle);
		createcsv(headrow, subscript++, "受益人手机号", cellStyle);
		createcsv(headrow, subscript++, "受益人是被保险人的", cellStyle);
		createcsv(headrow, subscript++, "同业公司人身保险保额合计", cellStyle);
		createcsv(headrow, subscript++, "个人交费金额", cellStyle);
		createcsv(headrow, subscript++, "单位交费金额", cellStyle);
		createcsv(headrow, subscript++, "账户顺序", cellStyle);
		createcsv(headrow, subscript++, "个人账户扣款金额", cellStyle);
		createcsv(headrow, subscript++, "个人账户交费比例", cellStyle);
		createcsv(headrow, subscript++, "开户名", cellStyle);
		createcsv(headrow, subscript++, "开户银行", cellStyle);
		createcsv(headrow, subscript++, "开户帐号", cellStyle);
		createcsv(headrow, subscript++, "账户顺序", cellStyle);
		createcsv(headrow, subscript++, "个人账户扣款金额", cellStyle);
		createcsv(headrow, subscript++, "个人账户交费比例", cellStyle);
		createcsv(headrow, subscript++, "开户名", cellStyle);
		createcsv(headrow, subscript++, "开户银行", cellStyle);
		createcsv(headrow, subscript++, "开户帐号", cellStyle);
		createcsv(headrow, subscript, "错误信息", cellStyle);
		for (int i = 0; i < errorGrpInsureds.size(); i++) {
			subscript = 0;
			ErrorGrpInsured errorGrpInsured = errorGrpInsureds.get(i);
			// 创建行
			HSSFRow row = sheet.createRow(i + 1);
			createcsv(row, subscript++, errorGrpInsured.getProcFlag(), cellStyle);
			createcsv(row, subscript++,
					errorGrpInsured.getIpsnNo() == null ? "" : errorGrpInsured.getIpsnNo().toString(), cellStyle);
			if (StringUtils.equals(grpInsurAppl.getCntrType(), "L") && errorGrpInsured.getHldrInfo() != null) {
				createcsv(row, subscript++, errorGrpInsured.getHldrInfo().getHldrName(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getHldrInfo().getHldrSex(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getHldrInfo().getHldrBirthDate() == null ? ""
						: DateFormatUtils.format(errorGrpInsured.getHldrInfo().getHldrBirthDate(), "yyyy/MM/dd"),
						cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getHldrInfo().getHldrIdType(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getHldrInfo().getHldrIdNo(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getHldrInfo().getHldrMobilePhone(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getRelToHldr(), cellStyle);
			} else if (StringUtils.equals(grpInsurAppl.getCntrType(), "L") && errorGrpInsured.getHldrInfo() == null) {
				subscript += 7;
			}
			createcsv(row, subscript++, errorGrpInsured.getIpsnName(), cellStyle);
			createcsv(row, subscript++,
					errorGrpInsured.getMasterIpsnNo() == null ? "" : errorGrpInsured.getMasterIpsnNo().toString(),
					cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnRtMstIpsn(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnType(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnSex(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnBirthDate() == null ? ""
					: DateFormatUtils.format(errorGrpInsured.getIpsnBirthDate(), "yyyy/MM/dd"), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnIdType(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnIdNo(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnMobilePhone(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnEmail(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnOccCode(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnCompanyLoc(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnRefNo(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getInServiceFlag(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnSss(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnSsc(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getIpsnSsn(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getNotificaStat(), cellStyle);
			createcsv(row, subscript++,
					errorGrpInsured.getFeeGrpNo() == null ? "" : errorGrpInsured.getFeeGrpNo().toString(), cellStyle);
			createcsv(row, subscript++, errorGrpInsured.getLevelCode(), cellStyle);
			if(errorGrpInsured.getSubStateList()!=null
				&& !errorGrpInsured.getSubStateList().isEmpty()){
				createcsv(row, subscript++,errorGrpInsured.getSubStateList().get(0).getClaimIpsnGrpNo() == null ? ""
							: errorGrpInsured.getSubStateList().get(0).getClaimIpsnGrpNo().toString(),
							cellStyle);
			}else{
					subscript++;
			}
			subscript = createSubStateLine(subscript,grpInsurAppl,polCodes, errorGrpInsured, row,cellStyle);
			if (errorGrpInsured.getBnfrInfoList() != null && !errorGrpInsured.getBnfrInfoList().isEmpty()) {
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrName(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrLevel() == null ? ""
						: errorGrpInsured.getBnfrInfoList().get(0).getBnfrLevel().toString(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrProfit() == null ? ""
						: errorGrpInsured.getBnfrInfoList().get(0).getBnfrProfit().toString(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrSex(), cellStyle);
				createcsv(row, subscript++,
						errorGrpInsured.getBnfrInfoList().get(0).getBnfrBirthDate() == null ? ""
								: DateFormatUtils.format(errorGrpInsured.getBnfrInfoList().get(0).getBnfrBirthDate(),
										"yyyy/MM/dd"),
						cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrIdType(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrIdNo(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrMobilePhone(), cellStyle);
				createcsv(row, subscript++, errorGrpInsured.getBnfrInfoList().get(0).getBnfrRtIpsn(), cellStyle);
			} else {
				subscript += 9;
			}
			createcsv(row, subscript++,
					errorGrpInsured.getTyNetPayAmnt() == null ? "" : errorGrpInsured.getTyNetPayAmnt().toString(),
					cellStyle);
			createcsv(row, subscript++,
					errorGrpInsured.getIpsnPayAmount() == null ? "" : errorGrpInsured.getIpsnPayAmount().toString(),
					cellStyle);
			createcsv(row, subscript++,
					errorGrpInsured.getGrpPayAmount() == null ? "" : errorGrpInsured.getGrpPayAmount().toString(),
					cellStyle);
			if (errorGrpInsured.getAccInfoList() != null && !errorGrpInsured.getAccInfoList().isEmpty()) {
				for (int j = 0; j < errorGrpInsured.getAccInfoList().size(); j++) {
					createcsv(row, subscript++, errorGrpInsured.getAccInfoList().get(j).getSeqNo() == null ? ""
							: errorGrpInsured.getAccInfoList().get(j).getSeqNo().toString(), cellStyle);
					createcsv(row, subscript++, errorGrpInsured.getAccInfoList().get(j).getIpsnPayAmnt() == null ? ""
							: errorGrpInsured.getAccInfoList().get(j).getIpsnPayAmnt().toString(), cellStyle);
					createcsv(row, subscript++, errorGrpInsured.getAccInfoList().get(j).getIpsnPayPct() == null ? ""
							: errorGrpInsured.getAccInfoList().get(j).getIpsnPayPct().toString(), cellStyle);
					createcsv(row, subscript++, errorGrpInsured.getAccInfoList().get(j).getBankAccName(), cellStyle);
					createcsv(row, subscript++, errorGrpInsured.getAccInfoList().get(j).getBankCode(), cellStyle);
					createcsv(row, subscript++, errorGrpInsured.getAccInfoList().get(j).getBankAccNo(), cellStyle);
				}
			}
			if (errorGrpInsured.getAccInfoList() != null && errorGrpInsured.getAccInfoList().size() == 1) {
				subscript += 6;
			} else if (errorGrpInsured.getAccInfoList() == null || errorGrpInsured.getAccInfoList().isEmpty()) {
				subscript += 12;
			}
			createcsv(row, subscript, errorGrpInsured.getRemark(), cellStyle);
		}
		// 创建文件保存路径
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
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
		
	}

	private void setPolCodes(List<String> polCodes, List<SubState> subStates) {
		for (SubState subState : subStates) {
			if (!polCodes.contains(subState.getPolCode())) {
				polCodes.add(subState.getPolCode());
			}
		}
	}
	private int createSubStateLine(int subscript,GrpInsurAppl grpInsurAppl,List<String> polCodes,ErrorGrpInsured errorGrpInsured,HSSFRow row,HSSFCellStyle cellStyle){
		for(int j=0;j<polCodes.size();j++){
			if(errorGrpInsured.getSubStateList()==null || errorGrpInsured.getSubStateList().isEmpty()){
				subscript+=3;
				continue;
			}
			int i = 0;
			for (;i < errorGrpInsured.getSubStateList().size();i++) {
				SubState subState = errorGrpInsured.getSubStateList().get(i);
				if (null != subState && StringUtils.equals(polCodes.get(j), subState.getPolCode())) {
					createcsv(row, subscript++, subState.getPolCode(), cellStyle);
					createcsv(row, subscript++, subState.getFaceAmnt() == null ? "" : subState.getFaceAmnt().toString(),
							cellStyle);
					createcsv(row, subscript++, subState.getPremium() == null ? "" : subState.getPremium().toString(),
							cellStyle);
					break;
				}
			}
			if(i == errorGrpInsured.getSubStateList().size()){
				subscript+=3;
			}
		}
		return subscript;
	}

	// 写一行csv文件
	private static void createcsv(HSSFRow row, int index, String value, HSSFCellStyle cellStyle) {
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

	/**
	 * @return the mongoBaseDao
	 */
	public MongoBaseDao getMongoBaseDao() {
		return mongoBaseDao;
	}

	/**
	 * @param mongoBaseDao the mongoBaseDao to set
	 */
	public void setMongoBaseDao(MongoBaseDao mongoBaseDao) {
		this.mongoBaseDao = mongoBaseDao;
	}
	
}
