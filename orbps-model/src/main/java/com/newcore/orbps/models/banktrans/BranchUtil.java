package com.newcore.orbps.models.banktrans;

import java.util.Map;
import java.util.HashMap;


public class BranchUtil {
	/**
	 * Add a private constructor to hide the implicit public one.  
	 */
	private BranchUtil(){}
	/**
	 * 获取机构配置
	 * 
	 * @param branchNo
	 *            机构号
	 * @return
	 */
	public static Branch getBranch(String branchNo) {
		String trim = branchNo.substring(0, 4);
		Branch branch = branchMap.get(trim);
		if (branch == null)
			branch = branchMap.get(trim.substring(0, 2));

		return branch;
	}

	protected static final Map<String, Branch> branchMap = new HashMap<>();
	static {
		branchMap.put("11", new Branch("110000", "11", "北京分公司", null, null));
		branchMap.put("12", new Branch("120000", "12", "天津市公司", null, null));
		branchMap.put("1202", new Branch("120221", "1202", "天津分公司", null, null));
		branchMap.put("13", new Branch("130000", "13", "河北分公司", null, null));
		branchMap.put("14", new Branch("140000", "14", "山西分公司", null, null));
		branchMap.put("15", new Branch("150000", "15", "内蒙古分公司", null, null));
		branchMap.put("21", new Branch("210000", "21", "辽宁分公司", "210200", "2102"));
		branchMap.put("2102", new Branch("210200", "2102", "大连分公司", null, null));
		branchMap.put("22", new Branch("220000", "22", "吉林分公司", null, null));
		branchMap.put("23", new Branch("230000", "23", "黑龙江分公司", null, null));
		branchMap.put("31", new Branch("310000", "31", "上海分公司", null, null));
		branchMap.put("32", new Branch("320000", "32", "江苏分公司", null, null));
		branchMap.put("33", new Branch("330000", "33", "浙江分公司", "330200", "3302"));
		branchMap.put("3302", new Branch("330200", "3302", "宁波分公司", null, null));
		branchMap.put("34", new Branch("340000", "34", "安徽分公司", null, null));
		branchMap.put("35", new Branch("350000", "35", "福建分公司", "350200", "3502"));
		branchMap.put("3502", new Branch("350200", "3502", "厦门分公司", null, null));
		branchMap.put("36", new Branch("360000", "36", "江西分公司", null, null));
		branchMap.put("37", new Branch("370000", "37", "山东分公司", "370200", "3702"));
		branchMap.put("3702", new Branch("370200", "3702", "青岛分公司", null, null));
		branchMap.put("41", new Branch("410000", "41", "河南分公司", null, null));
		branchMap.put("42", new Branch("420000", "42", "湖北分公司", null, null));
		branchMap.put("43", new Branch("430000", "43", "湖南分公司", null, null));
		branchMap.put("44", new Branch("440000", "44", "广东分公司", "440200", "4402"));
		branchMap.put("4402", new Branch("440200", "4402", "深圳分公司", null, null));
		branchMap.put("45", new Branch("450000", "45", "广西分公司", null, null));
		branchMap.put("46", new Branch("460000", "46", "海南分公司", null, null));
		branchMap.put("51", new Branch("510000", "51", "四川分公司", null, null));
		branchMap.put("52", new Branch("520000", "52", "贵州分公司", null, null));
		branchMap.put("53", new Branch("530000", "53", "云南分公司", null, null));
		branchMap.put("61", new Branch("610000", "61", "陕西分公司", null, null));
		branchMap.put("62", new Branch("620000", "62", "甘肃分公司", null, null));
		branchMap.put("63", new Branch("630000", "63", "青海分公司", null, null));
		branchMap.put("64", new Branch("640000", "64", "宁厦分公司", null, null));
		branchMap.put("65", new Branch("650000", "65", "新疆分公司", null, null));
		branchMap.put("66", new Branch("660000", "66", "重庆分公司", null, null));
		branchMap.put("85", new Branch("850000", "85", "西藏分公司", null, null));
	}
	public static void main(String[] args) {
		Branch branch = BranchUtil.getBranch("120221");
		System.out.println(branch.getProvBranch());
	}
}