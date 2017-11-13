package com.newcore.orbps.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.MioLog;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.pcms.bo.FinQueueData;
import com.newcore.orbps.models.pcms.bo.FinTaskData;
import com.newcore.orbps.models.pcms.bo.ImBizSwds;
import com.newcore.orbps.models.pcms.bo.ImBizTask;
import com.newcore.orbps.models.pcms.bo.ImBizTlst;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.ProcCommonFinQueueRecordService;
import com.newcore.orbps.service.api.SetComMioRecService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/** 
 * 首席共保产生财务队列数据功能实现类
 * @author JCC
 * 2016年11月23日 15:50:26
 */
@Service("procCommonFinQueueRecordService")
public class ProcCommonFinQueueRecordServiceImpl implements ProcCommonFinQueueRecordService{
	private  Logger logger = LoggerFactory.getLogger(ProcCommonFinQueueRecordServiceImpl.class);
    
	@Autowired
    MongoTemplate mongoTemplate; 
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	SetComMioRecService setComMioRecServiceClient;
	
	private static final int SUBSTR = 225 ; //每次截取字符的个数
	@Override
	public 	com.newcore.orbps.models.pcms.bo.RetInfo procCommonFinQueueRecord(GrpInsurAppl grpInsurAppl) {
		logger.info("开始 首席共保产生财务队列数据封装");
		// 收付费相关信息类
		Map<String,Object> applMap = new HashMap<>();
		applMap.put("applNo", grpInsurAppl.getApplNo());
		MioPlnmioInfo  mioPln = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class, applMap);	
		com.newcore.orbps.models.pcms.bo.RetInfo retInfo = null;
		if(mioPln !=null ){
			//1.抽取：存放从mioLogList中筛选出的FA/S,GF/S,PS/S数据
			List<MioLog> mioLogList = new ArrayList<>();
			for(MioLog mio:mioPln.getMioLogList()){
				String mioType =mio.getMioItemCode()+mio.getMioType();
				if(("FAS".equals(mioType) || "GFS".equals(mioType)  || "PSS".equals(mioType)) && !"CYGB".equals(mio.getRemark())){
					mioLogList.add(mio);
				}
			}
			//2.开始封装财务队列数据
			if(!mioLogList.isEmpty()){
				FinQueueData fq = new FinQueueData();
				fq.setApplNo(grpInsurAppl.getApplNo());				//投保单号
				fq.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());	//管理机构号
				fq.setCgNo(grpInsurAppl.getCgNo());	//合同组号
				fq.setMioLogList(mioLogList);		//实收付流水数组			
				List<String> polCodeList  = new ArrayList<>(); //险种集合
				for(Policy pol:grpInsurAppl.getApplState().getPolicyList()){
					polCodeList.add(pol.getPolCode());
				}
			    fq.setPolCodeList(polCodeList);
			    List<FinTaskData> finTaskDataList = new ArrayList<>(); //财务接口数据集合
				for(MioLog mio:mioLogList){
					FinTaskData finTaskData = new FinTaskData();
					//1财务接口队列数据
					ImBizTask imBizTask = setImBizTask(mio);
					finTaskData.setImBizTask(imBizTask);
					finTaskDataList.add(finTaskData);
					
					List<ImBizSwds> imBizSwdsList = new ArrayList<>(); // 财务接口内容数据
					//2 财务接口内容数据:写拼接如下字符串
					String content= getMioContent(mio);
					int length = content.length();	//字符串总长度
					if(length > SUBSTR){
						int bs =length/SUBSTR;  //倍数
						int ys =length%SUBSTR;  //余数
						for(int i=0;i<bs;i++){
							String con=content.substring(i*SUBSTR, i*SUBSTR+SUBSTR);
							ImBizSwds imBiz = setImBizSwds(mio,imBizTask.getTaskSeq(),con);
							imBizSwdsList.add(imBiz);
						}
						String con = content.substring(length-ys); //剩余字符
						ImBizSwds imBizSwds = setImBizSwds(mio,imBizTask.getTaskSeq(),con);
						imBizSwdsList.add(imBizSwds);
					}else{
						ImBizSwds imBizSwds = setImBizSwds(mio,imBizTask.getTaskSeq(),content);
						imBizSwdsList.add(imBizSwds);
					}
					finTaskData.setImBizSwdsList(imBizSwdsList);
		
					//3 财务接口清单数据
					ImBizTlst imBizTlst = setImBizTlst(mio,imBizTask.getTaskSeq());
					finTaskData.setImBizTlst(imBizTlst);
					
					finTaskDataList.add(finTaskData);
				}
				fq.setFinTaskDataList(finTaskDataList);
				
				//调用保单辅助平台setComMioRecService接口把数据传输到核心
				retInfo =doService(fq);
			}
		}
		return retInfo;
	}
	
	/**
	 * 首席共保财务与财务队列落地服务
	 * @param fq
	 * @return
	 */
	private RetInfo doService(FinQueueData fq) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
//		headerInfo.setOrginSystem("ORBPS");					
//		headerInfo.getRouteInfo().setBranchNo("120000");
		HeaderInfoHolder.setOutboundHeader(headerInfo);	
		return setComMioRecServiceClient.setComMioRecService(fq);
	}

	/**
	 * 根据实收数据获取拼接信息
	 * @param mio
	 * @return A|B|C|*||||
	 */
	private String getMioContent(MioLog mio) {
		StringBuilder sbd = new StringBuilder();
		sbd.append(mio.getCntrNo()).append("|");
		sbd.append(mio.getMioItemCode()).append("|");
		sbd.append(mio.getPolCode()).append("|");
		sbd.append(mio.getMioType()).append("|");
		sbd.append(getFormatDate("1",mio.getPlnmioDate())).append("|");
		sbd.append(mio.getMioCustName() == null ? "cust_name" : mio.getMioCustName()).append("|");
		sbd.append(mio.getMioTxNo()).append("|");
		sbd.append(mio.getReceiptNo()).append("|");
		switch (mio.getMioType()) {
		case "K":
		case "Q":
		case "L":
		case "P": 
			sbd.append(mio.getBankAccNo()).append("|");
			break ;
		default:
			sbd.append("").append("|");
			break;
		}
		sbd.append(mio.getAmnt()).append("|");
		sbd.append(mio.getMgrBranchNo()).append("|");
		sbd.append(mio.getSalesChannel()).append("|");
		sbd.append(mio.getSalesBranchNo()).append("|");
		sbd.append(mio.getSalesNo()).append("|");
		sbd.append(mio.getMioLogId()).append("|");
		sbd.append(getFormatDate("1",mio.getMioDate()));
		return sbd.toString();
	}

	/**
	 * 初始化财务接口队列数据
	 * @param mio
	 * @return ImBizTask
	 */
	private ImBizTask setImBizTask(MioLog mio) {
		ImBizTask imBizTask=new ImBizTask();
		imBizTask.setTaskSeq(0L);		  	//任务号:自增长，从1开始
		imBizTask.setStatus("N");		  	//状态:N
		imBizTask.setAttachedFlag(1); 		//是否附有清单:1
		Date date = new Date();
		imBizTask.setTaskCreateTime(date);	//任务产生时间:系统时间
		imBizTask.setAlterTime(date);		//状态变更时间:系统时间
		imBizTask.setPlnRespTime(date);		//计划响应时间:系统时间
		imBizTask.setRespTime(date);		//实际响应时间:系统时间
		imBizTask.setAskTimes("0");			//催件次数:0
		imBizTask.setRelKeyNo("");			//关联主记录号
		imBizTask.setRelKeyId(0L);			//关联主记录标识
		imBizTask.setTaskFrom("00");		//任务来源:00
		imBizTask.setTaskEvent(mio.getSgNo() == null ? "PS" : "PG");		//事件由来:miolog.sg_no is null )then PS else PG
		imBizTask.setTaskEventOrder("99");	//事件由来排序顺序:99
		imBizTask.setRelEventNo(mio.getSgNo());	//关联事件记录号:miolog.sg_no
		imBizTask.setRelEventId(mio.getMtnId());//关联事件标识:miolog.mtn_id	
		imBizTask.setPolCode(mio.getPolCode());		//险种代码:miolog.polcode
		imBizTask.setMgBranch(mio.getMgrBranchNo());	//管理机构:miolog.Mgbranch
		imBizTask.setServiceDept(Short.parseShort("0"));//服务网点:0
		imBizTask.setAccBranch(mio.getPclkBranchNo());	//受理机构:miolog.getPclkBranchNo
		imBizTask.setAccDept(Short.parseShort("0"));	//受理网点:0
		imBizTask.setAccDate(mio.getMioDate());		//受理日期:miolog.mio_date
		imBizTask.setOclkClerkId(0L);	//操作员标识:0
		imBizTask.setOclkBranchNo(mio.getPclkBranchNo());//操作员机构号:Mio_log.OCLK_BRANCH_NO
		imBizTask.setOclkClerkNo(mio.getPclkNo());	//操作员工号:Mio_log.pclkNo
		imBizTask.setSalesBranchNo(mio.getSalesBranchNo());//销售员机构号:miolog.SALES_BRANCH_NO
		imBizTask.setSalesDeptNo("");	//销售员组号:null
		imBizTask.setSalesNo(mio.getSalesNo());		//销售员号:miolog.sales_no
		imBizTask.setCntrType("");		//保单类别:null
		imBizTask.setSysType("2");		//系统类型:2
		imBizTask.setTaskFlowId(0L);	//工作号:0
		imBizTask.setCntrNo("");		//合同号:null
		imBizTask.setCustNo("");		//客户号:null
		imBizTask.setSalesChannel(mio.getSalesChannel());//销售渠道:miolog.SALES_CHANNEL null
		imBizTask.setExtKeys("41");		//实际扩展键个数:9
		imBizTask.setExtKey0("0");		//扩展键0:0
		imBizTask.setExtKey1("1");		//扩展键1:1
		imBizTask.setExtKey2(getFormatDate("",new Date()));		//扩展键2:系统日期
		imBizTask.setExtKey3("1");		//扩展键3:操作类别 ，写1
		imBizTask.setExtKey4(mio.getMtnItemCode());		//扩展键4:miolog.mtn_item_code
		imBizTask.setExtKey5(mio.getSalesChannel());		//扩展键5:miolog.sales_channel
		imBizTask.setExtKey6(mio.getMioCustNo());		//扩展键6:miolog.mio_cust_no
		imBizTask.setExtKey7(mio.getMioType());		//扩展键7:miolog.mio_type_code
		imBizTask.setExtKey8("");	//扩展键8:null
		imBizTask.setExtKey9("");	//扩展键9:null
		imBizTask.setExtKey10("");	//扩展键10:主险险种代码
		imBizTask.setExtKey11("");	//扩展键11:null
		imBizTask.setExtKey12(mio.getCntrType());	//扩展键12:miolog.cntr_type
		String extKey13="V".equals(mio.getMioType()) ? mio.getSalesNo().substring(0, 4) +mio.getSalesBranchNo().substring(0, 6)+mio.getSalesNo().substring(4, 8) : "";
		imBizTask.setExtKey13(extKey13);	//扩展键13:MioLog.mio_type_code=='V'?MioLog.sales_no(截取前4位)+MioLog.sales_branch_no截取前6位+MioLog.sales_no从第四位开始截取之后四位:null
		imBizTask.setExtKey14(mio.getBankCode());	//扩展键14:MioLog->bank_code
		imBizTask.setExtKey15(mio.getIpsnNo().toString());	//扩展键15:MioLog.ipsn_no
		imBizTask.setExtKey16(mio.getMioItemCode());	//扩展键16:MioLog.mio_item_code
		imBizTask.setExtKey17(getFormatDate("",mio.getPlnmioDate()));	//扩展键17:MioLog.plnmio_date
		imBizTask.setExtKey18(mio.getMioClass().toString());	//扩展键18:MioLog.mio_class
		imBizTask.setExtKey19(mio.getAmnt().toString());	//扩展键19:MioLog.amnt
		imBizTask.setExtKey20(mio.getCurrencyCode());	//扩展键20:MioLog.currency_code
		imBizTask.setExtKey21("");	//扩展键21:null accDate前4位年度+oclkBranchNO+mioLog.mioTxNo这样进行拼接
		imBizTask.setExtKey22(mio.getMioCustName());	//扩展键22：MioLog.mio_cust_name 截断32位
		imBizTask.setExtKey23("");	//扩展键23：默认为空即可
		imBizTask.setExtKey24(getFormatDate("", new Date()));	//扩展键24：系统时间
		imBizTask.setExtKey25("");	//扩展键25：默认为空即可
		imBizTask.setExtKey26("");	//扩展键26：默认为空即可
		imBizTask.setExtKey27("");	//扩展键27：默认为空即可
		imBizTask.setExtKey28("");	//扩展键28：默认为空即可
		imBizTask.setExtKey29("");	//扩展键29：默认为空即可
		imBizTask.setExtKey30("");	//扩展键30：默认为空即可
		imBizTask.setExtKey31("");	//扩展键31：默认为空即可
		imBizTask.setExtKey32("");	//扩展键32：默认为空即可
		imBizTask.setExtKey33("");	//扩展键33：默认为空即可
		imBizTask.setExtKey34("");	//扩展键34：默认为空即可
		imBizTask.setExtKey35("");	//扩展键35：默认为空即可
		imBizTask.setExtKey36("");	//扩展键36：默认为空即可
		imBizTask.setExtKey37("");	//扩展键37：默认为空即可
		imBizTask.setExtKey38("");	//扩展键38：默认为空即可
		imBizTask.setExtKey39("");	//扩展键39：默认为空即可
		imBizTask.setExtKey40("");	//扩展键40：默认为空即可
		return imBizTask;
	}

	/**
	 * 初始化财务接口内容数据
	 * @param mio
	 * @return
	 */
	private ImBizSwds setImBizSwds(MioLog mio,long taskSeq,String content) {
		ImBizSwds imBizSwds=new ImBizSwds();
		imBizSwds.setTlstSeq(taskSeq);   //任务清单号:自动增长
		imBizSwds.setTaskSeq(taskSeq);	 //任务号:对应的imBizTask.taskSeq
		imBizSwds.setAttachedFileType(Short.parseShort("901")); //清单类别:901
		imBizSwds.setSendWords(content); //内容
		imBizSwds.setLineNo(Byte.valueOf("0"));	  //行号
		return imBizSwds;
	}

	/**
	 * 初始化财务接口清单数据
	 * @param mio
	 * @return
	 */
	private ImBizTlst setImBizTlst(MioLog mio,long taskSeq) {
		ImBizTlst imBizTlst = new ImBizTlst();
		imBizTlst.setTlstSeq(taskSeq);   	//任务清单号:自动增长	
		imBizTlst.setTaskSeq(taskSeq);		//任务号:对应的imBizTask.taskSeq
		imBizTlst.setAttachedFileType(Short.parseShort("901")); //清单类别:901
		imBizTlst.setSendDate(new java.sql.Date(new Date().getTime()));		//通知日期:系统时间	
		imBizTlst.setPrtTime(new java.sql.Date(new Date().getTime()));		//打印时间:
		imBizTlst.setRespWord("");				//反馈内容
		imBizTlst.setPlnRespTime(new java.sql.Date(new Date().getTime()));	//要求反馈时间
		imBizTlst.setRespTime(new java.sql.Date(new Date().getTime()));		//实际反馈时间
		imBizTlst.setDataSource("");
		return imBizTlst;
	}
	
	/**
	 * 格式化日期格式
	 * @param type 日期格式:默认[yyyy-MM-dd HH:mm:ss], 1:yyyy-MM-dd
	 * @param date 需要格式化的数据
	 * @return
	 */
	private String getFormatDate(String type,Date date) {
		String format="yyyy-MM-dd HH:mm:ss";
		if("1".equals(type)){
			format="yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
