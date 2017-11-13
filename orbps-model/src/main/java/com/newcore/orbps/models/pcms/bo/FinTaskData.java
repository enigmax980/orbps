package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;


public class FinTaskData implements Serializable{

	private static final long serialVersionUID = -7151833265040503920L;

	/**实时接口队列对象**/
	@NotNull(message="[实时接口队列对象不能为空]")
	private  ImBizTask  imBizTask;
	
	/**实时接口清单对象**/
	@NotNull(message="[实时接口清单对象不能为空]")
	private  ImBizTlst  imBizTlst ;
	
	/**财务接口内容数据对象集合**/
	@NotNull(message="[财务接口内容数据对象不能为空]")
	private List<ImBizSwds> imBizSwdsList;

	public ImBizTask getImBizTask() {
		return imBizTask;
	}

	public void setImBizTask(ImBizTask imBizTask) {
		this.imBizTask = imBizTask;
	}

	public ImBizTlst getImBizTlst() {
		return imBizTlst;
	}

	public void setImBizTlst(ImBizTlst imBizTlst) {
		this.imBizTlst = imBizTlst;
	}

	public List<ImBizSwds> getImBizSwdsList() {
		return imBizSwdsList;
	}

	public void setImBizSwdsList(List<ImBizSwds> imBizSwdsList) {
		this.imBizSwdsList = imBizSwdsList;
	}
	
	
}
