package com.newcore.orbps.models.taskprmy;
/**
 * 清单类型ENUM
 * @author Administrator
 *
 */
public enum DetailListType {
	
	//保单基本信息录入
	INSURAPPL_INPUT("INSURAPPL_BASIC_INPUT", "E"),
	//清单待导入服务 
	IPSN_IMPORT_APPL("IPSN_IMPORT_APPL_SERVICE", "W"),
	//人工审批 
	MANUAL_APPROVAL("MANUAL_APPROVAL", "A"),
	//清单倒入
	IPSNLIST_INTO("IPSN_LIST_INTO", "I"),
	//被保人开户
	INSURED_ACCOUNT("INSURED_ACCOUNT","O"),
	//核保子流程 
	INSURAPPROVAL_PROCESS_ITEM_U("INSUR_APPROVAL_PROCESS_ITEM_U", "U"),
	INSURAPPROVAL_PROCESS_ITEM_Z("INSUR_APPROVAL_PROCESS_ITEM_Z","Z"),
	INSURAPPROVAL_PROCESS_ITEM_R("INSUR_APPROVAL_PROCESS_ITEM_R", "R"),
	//生成应收
	GENERATE_RECEIVABLE("GENERATE_ACCOUNTS_RECEIVABLE", "G"),
	//收费检查
	FEE_CHECK("FEE_CHECK", "C"),
	//保单生效
	POLICY_INFORCE("POLICY_INFORCE", "M"),
	//打印
	PRINT("PRINT", "p"),
	//回执核销
	RECEIPT_VERIFICATION("ORBPS_INSURAPPL_CV_TASK", "V");

	private DetailListType(String detaillistType, String indexName) {
		this.detaillistType = detaillistType;
		this.indexName = indexName;
	}
	
	private String detaillistType;
	private String indexName;

	public String getDetaillistType() {
		return detaillistType;
	}

	public void setDetaillistType(String detaillistType) {
		this.detaillistType = detaillistType;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public static String getDetaillistType(String indexName) {
		for (DetailListType detaillistType : DetailListType.values())
			if ((detaillistType.getIndexName()).equals(indexName)) {
				return detaillistType.getDetaillistType();
			}
		return null;
	}
}
