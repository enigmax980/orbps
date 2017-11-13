package com.newcore.orbps.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.halo.core.common.util.PropertiesUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.CustNoSearchSrevice;
import com.newcore.orbps.service.api.IpsnImportResultService;
import com.newcore.orbps.util.FileZip;

/**
 * 清单导入结果查询
 * 
 * @author liushuaifeng
 *
 *         创建时间：2016年7月28日下午5:04:57
 */
@Service("ipsnImportResultService")
public class IpsnImportResultServiceImpl implements IpsnImportResultService {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory
			.getLogger(IpsnImportResultServiceImpl.class);

	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	CustNoSearchSrevice custNoSearchSrevice;
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	// 根据投保单号获取清单导入结果
	@Override
	public RetInfo GetIpsnImportResult(Map<String, String> applMap)
			throws IOException {
		String applNo = applMap.get("applNo");
		RetInfo retInfo = new RetInfo();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		List<ErrorGrpInsured> errorGrpInsureds = (List<ErrorGrpInsured>) mongoBaseDao
				.find(ErrorGrpInsured.class, map);
		List<GrpInsured> grpInsureds = (List<GrpInsured>) mongoBaseDao.find(
				GrpInsured.class, map);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(
				GrpInsurAppl.class, map);
		if (null == grpInsurAppl) {
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("0");
			retInfo.setErrMsg("投保单号对应的基本信息不存在");
			return retInfo;
		}
		if (!errorGrpInsureds.isEmpty()) {
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("5");
			retInfo.setErrMsg("清单信息有误，请导出错误信息");
			return retInfo;
		} else if (null == grpInsureds
				|| grpInsureds.size() != grpInsurAppl.getApplState()
						.getIpsnNum()) {
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("4");
			retInfo.setErrMsg("清单导入未完成");
			return retInfo;
		} else {
			for (GrpInsured grpInsured : grpInsureds) {
				if (StringUtils.equals(grpInsured.getProcFlag(), "F")) {
					retInfo.setApplNo(applNo);
					retInfo.setRetCode("7");
					retInfo.setErrMsg("清单导入完成，存在开户失败数据");
					return retInfo;
				}
				if (StringUtils.equals(grpInsured.getProcFlag(), "N")) {
					retInfo.setApplNo(applNo);
					retInfo.setRetCode("6");
					retInfo.setErrMsg("清单导入完成，存在未开户数据");
					return retInfo;
				}
			}
			RetInfoObject retInfoObject = custNoSearchSrevice.search(applMap);
			if (("1").equals(retInfoObject.getRetCode())) {
				String finalPath = pathFile(applMap);
				retInfo.setApplNo(applNo);
				retInfo.setRetCode("1");
				retInfo.setErrMsg(finalPath);
				return retInfo;
			} else {
				retInfo.setApplNo(applNo);
				retInfo.setRetCode("0");
				retInfo.setErrMsg("开户成功,但查询客户号失败");
				return retInfo;
			}
		}
	}

	/**
	 * 开户信息存txt文件并压缩，返回文件存放路径
	 * 
	 * @return
	 * @throws IOException
	 */
	public String pathFile(Map<String, String> applMap) throws IOException {
		RetInfoObject retInfoObject = custNoSearchSrevice.search(applMap);
		if (null == retInfoObject || ("").equals(retInfoObject)) {
			return null;
		}
		String applNo = applMap.get("applNo");
		String retInfoObjectJson = JSON.toJSONString(retInfoObject);
		StringBuffer pathfile = new StringBuffer();
		pathfile.append(PropertiesUtils.getProperty("fs.ftp.filedir.custnodir"));
		pathfile.append(applNo);
//		pathfile.append("_");
//		pathfile.append(df.format(new Date()));
		// 写入Txt文件
		File writename = new File(pathfile.toString() + ".txt");
		// 创建新文件
		writename.createNewFile();
		writename.setReadable(true, false);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(writename));
			out.write(retInfoObjectJson);
			// 把缓存区内容压入文件
			out.flush();
			out.close();
			FileZip fileZip = new FileZip(pathfile.toString() + ".zip");
			fileZip.compress(pathfile.toString() + ".txt");
			File zipFileName = new File(pathfile.toString() + ".zip");
			zipFileName.setReadable(true, false);
			return pathfile.toString() + ".zip";
			
		} finally {
			if (null != out) {
				out.close();
			}
		}
		
		
	}
}
