package com.newcore.orbps.models.banktrans;

/**
 * 实收表代码
 */
public class MioLogCode {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private MioLogCode(){}
	/**
	 * 核心入账状态[未送]
	 */
	public static final String CORE_STATE_NO = "N";
	/**
	 * 核心入账状态[已送]
	 */
	public static final String CORE_STATE_TO = "T";
	/**
	 * 核心入账状态[入账成功]
	 */
	public static final String CORE_STATE_SUCCESS = "S";
	/**
	 * 核心入账状态[入账失败]
	 */
	public static final String CORE_STATE_FAILED = "F";
	
	public static final String OCLK_BRANCH_NO = "!!!!!!";
	public static final String OCLK_CLERK_NO = "########";
}