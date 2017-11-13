package com.newcore.orbps.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.banktrans.BtBranchContrast;
import com.newcore.orbps.models.banktrans.BuildBanTransBean;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.finance.PlnmioRecGroupBean;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.BuildBankTransService;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.orbpsutils.dao.api.BtBranchContrastDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.PLNMIO_STATE;
import com.newcore.supports.dicts.TRANS_CLASS;
import com.newcore.supports.dicts.TRAN_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 银行转账功能实现
 * @author JCC
 * 2016年10月8日 10:46:55
 */
@Service("buildBankTransService")
public class BuildBankTransServiceImpl implements BuildBankTransService {
	/**
	 * 日志管理工具实例.
	 */
	private Logger logger = LoggerFactory.getLogger(BuildBankTransServiceImpl.class);
	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Autowired
	BankTransDao bankTransDao;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	BtBranchContrastDao btBranchContrastDao;

	private final String SUCCESS = "<RESULT>0</RESULT>";
	private final String FAILED = "<RESULT>-1</RESULT>";
	/**
	 * 生成银行转账数据方法
	 * @param message 请求报文
	 */
	@Override
	public String build(String message) {
		boolean flag = false;
		this.logger.info("开始进行银行转账操作，传入报文为：" + message);
		BuildBanTransBean bean= new BuildBanTransBean();
		try {
			//解析传入报文内容
			Document doucument = DocumentHelper.parseText(message);
			bean=readXml(doucument);
		} catch (Exception e) {
			this.logger.info("解析报文失败！", e);
			return addOutBody(this.FAILED);
		}
	
		// 1.开始抽取待生成转账的应收数据
		List<PlnmioRecGroupBean> prgbList = null;
		try {
			prgbList = getPlnmioRecList(bean);
			if(prgbList.isEmpty()){
				flag=false;
			}else{
				// 2.根据抽取出的应收数据，生成银行转账数据
				flag=buildBankTrans(prgbList, bean);
			}
		} catch (Exception e) {
			this.logger.error("生成转账数据失败！", e);
			try {
				// 3.银行转账失败处理
				buildBankTransError(prgbList);
				this.logger.info("解锁数据库成功！");
			} catch (Exception ex) {
				this.logger.error("解锁数据库失败！", ex);
			}
		}finally {
			try {
				// 4.删除当前机构号数据
			} catch (Exception e) {
				this.logger.error("删除当前机构号数据失败！", e);
			}
		}
		if(flag){
			this.logger.info("生成银行转账数据：操作成功！");
			return addOutBody(this.SUCCESS);
		}else{
			this.logger.info("生成银行转账数据：操作失败！");
			return addOutBody(this.FAILED);
		}
	}
	
	/**
	 * 银行转账失败处理
	 * @param prgbList
	 */
	private void buildBankTransError(List<PlnmioRecGroupBean> prgbList) {
		StringBuilder sbr = new StringBuilder();
		for (PlnmioRecGroupBean prgb : prgbList) {
			sbr.append(prgb.getPlnmioRecId()+",");
		}
		//回滚数据
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("lock_flag", "0");		//锁标识[未锁定0]，
		paramMap.put("trans_stat", TRAN_STATE.FAILURE.getKey()); //转账状态[失败 F]
		paramMap.put("proc_stat", PLNMIO_STATE.UNCOLLECTED.getKey()); //应收状态[未收N]
		plnmioRecDao.updatePlnmioRecByIds(sbr.toString().substring(0, sbr.length()-1),paramMap);
	}
	
	/**
	 * 抽取满足条件的数据
	 * @param bean 请求报文BO
	 * @return
	 */
	private List<PlnmioRecGroupBean> getPlnmioRecList(BuildBanTransBean bean) {	
		this.logger.info(">>>>>>>>>>>>>>>>开始抽取应收数据！");
		if(bean.getSbFlag() == 1){ 
			//FcFlag 是否包含下属机构 ： 1-包含，0-不包含
			String branchNos=getBranchNos(bean.getBranchNo());
			bean.setBranchNo(branchNos);
		}
		List<PlnmioRecGroupBean> prgbList = plnmioRecDao.getPlnmioRecList(bean);
		if(prgbList.isEmpty()){
			this.logger.info("开始抽取应收数据：未抽取到满足条件的！");
			return prgbList;
		}
		this.logger.info("本次共应生成" + prgbList.size() + "条转账数据");
		StringBuilder sbr = new StringBuilder();
		for (PlnmioRecGroupBean prgb : prgbList) {
			sbr.append(prgb.getPlnmioRecId()+",");
			List<PlnmioRec> plnmioRecList = plnmioRecDao.queryPlnmioRecList(prgb.getPlnmioRecId());
			prgb.setPlnmioRecList(plnmioRecList);
		}
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("lock_flag", "1");		//锁标识[未锁定0]，
		paramMap.put("trans_Stat", TRAN_STATE.NEW.getKey()); //转账状态[新增 N]
		boolean isLock = plnmioRecDao.updatePlnmioRecByIds(sbr.toString().substring(0, sbr.length()-1),paramMap);
		if(isLock){
			this.logger.info("开始抽取应收数据:上锁完成！");
			return prgbList;
		}else{
			this.logger.info("开始抽取应收数据:上锁失败，终止程序流转！");
			return null;
		}
	}

	/**
	 * 生成银行转账数据
	 * @param prgbList 抽取出的应收数据集合
	 * @param bean 封装请求报文实体
	 */
	private boolean buildBankTrans(List<PlnmioRecGroupBean> prgbList, BuildBanTransBean bean) {
		// 银行转账数据集合
		List<BankTrans> btList = new ArrayList<>();
		StringBuilder sbr = new StringBuilder();
		//isContains 保存已经查询过bt_branch_contrast库的条件
		Map<String,String> isContains = new HashMap<>();
		for (PlnmioRecGroupBean plnmioRecGroupBean : prgbList) {
			sbr.append(plnmioRecGroupBean.getPlnmioRecId()+",");
			String applNo=plnmioRecGroupBean.getCntrNo();
			//应收时才判断首期扣款截至日期是否超期   收付类型 [1收] [-1付]
			GrpInsurAppl grp =procMioInfoDao.getGrpInsurAppl(applNo);
			if(bean.getRpFlag() == 1 && grp.getPaymentInfo().getForeExpDate() != null){
				Date foreExpDate = grp.getPaymentInfo().getForeExpDate(); //首期扣款截至日期
				Date nowTime = new Date(); 		//当前系统时间
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String data1 = dateFormat.format(nowTime); 
				String data2 = dateFormat.format(foreExpDate);
				//大于返回1,等于返回0，小于返回-1
				int isOverTime = data1.compareTo(data2); 
				//则判断当前日期是否大于首期扣款截至日期【foreExpDate】：小于才能进行生成银行转账数据
				if(isOverTime==1){
					this.logger.info("当前日期大于首期扣款截至日期【foreExpDate】,不能做银行转账操作["+applNo+"]！");
					continue;
				}
			}
			
			PlnmioRec plnmio= plnmioRecGroupBean.getPlnmioRecList().get(0);
			BankTrans bankTrans = new BankTrans();
			bankTrans.setTransStat(TRAN_STATE.NEW.getKey()); //转账状态[待转帐:N]
			// 得到当前银行转账信息是由几条应收数据分组统计后得出的
			int count = plnmioRecGroupBean.getCount();
			if (count == 1) {		
				this.logger.info(">>>>>>>>>>>>>>>>>单条应收：生成银行转账数据["+applNo+"]！");
				bankTrans.setPlnmioRecId(plnmio.getPlnmioRecId());	//应收付记录标识
				bankTrans.setPlnmioRecIdList("");					//只有一条数据时，为空
				bankTrans.setMgrBranchNo(plnmio.getMgrBranchNo());	//管理机构
				bankTrans.setPlnmioDate(plnmio.getPlnmioDate());	//应收付日期
				bankTrans.setTransAmnt(plnmio.getAmnt());			//金额
				bankTrans.setIpsnNo(plnmio.getIpsnNo()); 			//被保人序号
			}else if(count > 1){
				this.logger.info(">>>>>>>>>>>>>>>>>多条应收：生成银行转账数据["+applNo+"]！");
				bankTrans.setPlnmioRecId(0L);		//超过1条数据时，应收付记录标识为0
				bankTrans.setPlnmioRecIdList(plnmioRecGroupBean.getPlnmioRecId());	//应收付记录标识：id1,id2,id3....
//				String branchNo = BranchUtil.getBranch(plnmio.getMgrBranchNo()).getProvBranch();
				bankTrans.setMgrBranchNo(plnmio.getMgrBranchNo()); //管理机构
				if(plnmio.getPlnmioDate() == null){
					this.logger.info(">>>>>>>>>>>>>>>>>多条应收：生成银行转账数据出现错误，应收日期不能为空["+plnmio.getPlnmioRecId()+"]！");
				}
				Date date = plnmio.getPlnmioDate();				
				for (PlnmioRec plnmioRec : plnmioRecGroupBean.getPlnmioRecList()) {
					if (plnmioRec.getPlnmioDate().before(date)) {
						date = plnmioRec.getPlnmioDate();
					}
				}
				bankTrans.setPlnmioDate(date);	//得到最早的应收付日期
				bankTrans.setTransAmnt(plnmioRecGroupBean.getAmnts());//金额
				bankTrans.setIpsnNo(0L); 		//被保人序号
			}
			bankTrans.setTransBatSeq(-1);					//转账批号
			bankTrans.setBankCode(plnmio.getBankCode());  	//银行代码
			//判断当前查询条件是否已经查过库了
			String isKey=plnmio.getBankCode()+plnmio.getMgrBranchNo()+plnmio.getMioClass()+plnmio.getMioType();
			if(isContains.containsKey(isKey)){
				//若isKey在isContains中，则直接取value值
				bankTrans.setBranchBankAccNo(isContains.get(isKey)); 
			}else{
				//若isKey不在isContains中，则查库获取
				BtBranchContrast bt = new BtBranchContrast();
				bt.setBankCode(plnmio.getBankCode());
				bt.setPclkBranchNo(plnmio.getMgrBranchNo());
				bt.setMioClass(plnmio.getMioClass());
				bt.setMioType(plnmio.getMioType());
				bt.setServiceDeptNo(0);
				bt = btBranchContrastDao.queryBtBranchContrastInfo(bt);
				if(bt.getBranchBankAccNo().isEmpty() || "".equals(bt.getBranchBankAccNo())){
					throw new RuntimeException("该机构不存在对应的保险公司银行账号！"+applNo);
				}else{
					bankTrans.setBranchBankAccNo(bt.getBranchBankAccNo()); 
					isContains.put(isKey, bt.getBranchBankAccNo());
				}
			}
			bankTrans.setBankAccNo(plnmio.getBankAccNo());		//银行帐号
			bankTrans.setAccCustName(plnmio.getBankAccName());	//账户所有人名称
			bankTrans.setCntrNo(plnmio.getCgNo());				//合同号[投保单号]
			bankTrans.setTransClass(TRANS_CLASS.FIRST_PHASE.getKey());	//转账类型:F
			bankTrans.setMioClass(plnmio.getMioClass());		//收付类型
			bankTrans.setMioItemCode(plnmio.getMioItemCode());	//收付项目代码
			bankTrans.setMioDate(new Date());					//实收付日期
			bankTrans.setCustNo(plnmio.getMioCustNo());			//客户号
			bankTrans.setCreateDate(new Date());				//生成日期
			bankTrans.setBtMioTxNo(0L); 						//转账交易号
			btList.add(bankTrans);
		}
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("proc_stat", PLNMIO_STATE.ON_THE_WAY.getKey()); //在途
		plnmioRecDao.updatePlnmioRecByIds(sbr.toString().substring(0, sbr.length()-1),paramMap);
		return bankTransDao.insertBankTrans(btList);
	}
	
	/**
	 * 解析xml文件
	 * @param document
	 * @return
	 * @throws Exception
	 */
	private BuildBanTransBean readXml(Document document){
		//获取指定节点下的元素
		Element root = document.getRootElement().element("REQUEST").element("IO_MIOS_GEN_BANK_TRANS_SCXE");
		String sysNo = root.element("SYS_NO").getTextTrim();		//系统号
		String branchNo = root.element("BRANCH_NO").getTextTrim();	//机构号
		String bankCode = root.element("BANK_CODE").getTextTrim();	//银行代码
		Integer rpFlag = Integer.valueOf(root.element("RP_FLAG").getTextTrim());	//收付类型 [1收] [-1付]
		Integer fcFlag = Integer.valueOf(root.element("FC_FLAG").getTextTrim());	//首期续期表记 [0首期] [1续期]
		Integer sbFlag = Integer.valueOf(root.element("SB_FLAG").getTextTrim());	//是否包含下级机构 [0不包含] [1包含]
		String procDate = root.element("PROC_DATE").getTextTrim();	//操作日期 [对应应收付日期]
		String transSource = root.element("TRANS_SOURCE").getTextTrim();
		String taskId = root.element("TASK_ID").getTextTrim();
		String startExecTime = root.element("START_EXEC_TIME").getTextTrim();
		return new BuildBanTransBean(sysNo, branchNo, bankCode, rpFlag, fcFlag,
				sbFlag, procDate, transSource, taskId, startExecTime);
	}
	
	/**
	 * 返回报文
	 * @param content
	 * @return
	 */
	private String addOutBody(String content) {
		StringBuilder body = new StringBuilder();
		body.append("<BODY>");
		body.append("<RESPONSE>");
		body.append("<SERIALNUM>1</SERIALNUM>");
		body.append(content);
		body.append("</RESPONSE>");
		body.append("</BODY>");
		return body.toString();
	}
	
	@Autowired
	BranchService branchService;
	/**
	 * 获取下属机构号
	 * @param branchNo 父级机构号
	 * @return
	 */
	private String getBranchNos(String branchNo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Branch branch = branchService.findSubBranchAll(branchNo);
		List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
		StringBuilder sdr = new StringBuilder();
		sdr.append("'");
		sdr.append(branchNo);
		sdr.append("'");
		for(String branchNo1:branchNos){
			sdr.append(",");
			sdr.append("'");
			sdr.append(branchNo1);
			sdr.append("'");
		}
		return sdr.toString();
	}
}
