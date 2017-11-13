package com.newcore.orbps.service.business;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.batch.item.ItemWriter;

import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 
 * @author liushuaifeng
 *
 * 创建时间：2016年8月19日上午10:37:59
 */

@SuppressAjWarnings("unchecked") 
public class IpsnMultiItemWriter<T> implements ItemWriter<T>{
	
	//写代理
	private  List<ItemWriter<? extends T>>  delegates;

	/**
	 * @param delegates the delegates to set
	 */
	public void setDelegates(List<ItemWriter<? extends T>> delegates) {
		this.delegates = delegates;
	}

	@Override
	public void write(List<? extends T> items) throws Exception {

		long start = System.currentTimeMillis();
		//获取正确被保人写者
		ItemWriter ipsnItemWriter = (ItemWriter) delegates.get(0);
		
		//获取问题被保人写者
		ItemWriter errorIpsnItemWriter = (ItemWriter) delegates.get(1);
		
		//正确被保人列表
		List<GrpInsured> grpInsuredList = new ArrayList<GrpInsured>();
		
		//问题被保人列表
		List<ErrorGrpInsured>  errGrpInsuredList = new ArrayList<ErrorGrpInsured>();
		
		for(int i = 0; i < items.size(); i++){
			
			//如果是正确的被保人对象，则放到相应的列表中
			if ("GrpInsured".equals(items.get(i).getClass().getSimpleName()) ) {
				grpInsuredList.add((GrpInsured)items.get(i));
			}else{
				errGrpInsuredList.add((ErrorGrpInsured)items.get(i));
			}
			
		}
		//如果正常的被保人列表非空，则调用相应写者写入数据库；
		if (grpInsuredList.size() > 0) {
			ipsnItemWriter.write(grpInsuredList);
		}
		//如果有问题的被保人列表非空，则调用相应写者写入数据库；
		if (errGrpInsuredList.size() > 0 ) {
			errorIpsnItemWriter.write(errGrpInsuredList);
		}
		long end = System.currentTimeMillis();
		
		long count = grpInsuredList.size() + errGrpInsuredList.size();
		System.out.println("lsf: 本地写入数量： " + count + ", " +  "time: "+(end - start) );
		 
		
	}
	

}
