package com.newcore.orbps.models.pcms.bo;


import java.io.Serializable;
import java.util.List;



/**
 * 错误清单信息查询服务返回信息
 */
public class ErrListOutBo implements Serializable {


	private static final long serialVersionUID = -2768261846101514890L;

	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;

	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;
	//数据库查询到的ErrBatchListBo表的数据对象集合
	private List<ErrBatchListBo> errBatchListBoList;

	public ErrListOutBo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public List<ErrBatchListBo> getErrBatchListBoList() {
		return errBatchListBoList;
	}
	public void setErrBatchListBoList(List<ErrBatchListBo> errBatchListBoList) {
		this.errBatchListBoList = errBatchListBoList;
	}





}
