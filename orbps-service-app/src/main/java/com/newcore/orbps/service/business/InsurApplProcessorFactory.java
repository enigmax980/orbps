package com.newcore.orbps.service.business;

/**
 * 保单数据处理器工厂
 * @author wangxiao
 * 创建时间：2016年7月20日下午3:43:53
 */
public class InsurApplProcessorFactory {
	
	private static InsurApplProcessorFactory insurApplProcessorFactory;
	private InsurApplProcessorFactory(){};
	public static InsurApplProcessorFactory getInsurApplProcessorFactory(){
		if (insurApplProcessorFactory == null){
			insurApplProcessorFactory =new InsurApplProcessorFactory();
		}
		return insurApplProcessorFactory;
	}
	/**
	 * 生成具体的处理类
	 * @param cntrForm
	 * @return
	 */
	public InsurApplProcessor createProcessor(String cntrForm){
		InsurApplProcessor insurApplProcessor;
		if("G".equals(cntrForm)||"L".equals(cntrForm)){
			insurApplProcessor = new GrpInsurApplProcessor();
		}else{
			insurApplProcessor = new ListInsurProcessor();
		}
		return insurApplProcessor;
	};
}
