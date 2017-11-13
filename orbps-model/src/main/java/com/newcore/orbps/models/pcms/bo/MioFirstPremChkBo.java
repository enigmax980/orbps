package com.newcore.orbps.models.pcms.bo;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 短险契约收费检查
 * 
 * @author huangwei
 * @date 20160829
 */
public class MioFirstPremChkBo implements java.io.Serializable{
	
	
	private static final long serialVersionUID = -3309420641520347153L;

	// 检查方式    char（1）
	//	1.检查汇总情况－此情况返回报文只需要返回一个总的收费完成标志即可，不返回明细；
	//    2.检查收费成功明细－此情况会根据应收标志ID进行返回对应收费完成的明细；同时总的所有收费完成标志也会返回。
	//    3.检查收费失败明细－此情况会仅将收费未完成的或失败情况明细清单返回，同时总的所有收费完成标志也会返回。
	//    4.检查所有成功、失败明细－返回全部成功、失败的明细情况，即总的收费完成的标志。
	@NotNull(message="[检查方式不能为空]")
	private String checkFlag;
	
	// 传递内容主体
	private List<MioFirstPremChkFramBo> list;
	
	//数据源
	private String dataSource;

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public List<MioFirstPremChkFramBo> getList() {
		return list;
	}

	public void setList(List<MioFirstPremChkFramBo> list) {
		this.list = list;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
}
