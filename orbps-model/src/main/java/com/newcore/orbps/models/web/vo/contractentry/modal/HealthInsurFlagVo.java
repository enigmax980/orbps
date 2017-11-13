package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;

/**
 * 健康险专项表示vo对象
 * @author jincong
 *
 */
@SuppressWarnings("serial")
public class HealthInsurFlagVo  implements Serializable{

	/**
     * 字典Key
     */
    private String key;
    /**
     * 字典中文描述
     */
    private String description;
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
    
}
