package com.newcore.orbps.model.service.dict;

import java.util.Arrays;
import java.util.Optional;

import com.halo.core.exception.BusinessException;

public enum POST_CODE {
	BEIJING("10", "北京"),
	TIANJIN("30", "天津"),
	NEIMENG01("01", "内蒙"),
	NEIMENG02("02", "内蒙"),
	SHANXI03("03", "山西"),
	SHANXI04("04", "山西"),
	HEBEI05("05", "河北"),
	HEBEI06("06", "河北"),
	HEBEI07("07", "河北"),
	LIAONING11("11", "辽宁"),
	LIAONING12("12", "辽宁"),
	JILING("13", "吉林"),
	HEILONGJIANG15("15", "黑龙江"),
	HEILONGJIANG16("16", "黑龙江"),
	SHANGHAI("20", "上海"),
	JIANGSU21("21", "江苏"),
	JIANGSU22("22", "江苏"),
	ANHUI23("23", "安徽"),
	ANHUI24("24", "安徽"),
	SHANDONG25("25", "山东"),
	SHANDONG26("26", "山东"),
	SHANDONG27("27", "山东"),
	ZHEJIANG31("31", "浙江"),
	ZHEJIANG32("32", "浙江"),
	JIANGXI33("33", "江西"),
	JIANGXI34("34", "江西"),
	FUJIAN35("35", "福建"),
	FUJIAN36("36", "福建"),
	CHONGQING("40", "重庆"),
	HUNAN41("41", "湖南"),
	HUNAN42("42", "湖南"),
	HUBEI43("43", "湖北"),
	HUBEI44("44", "湖北"),
	HENAN45("45", "河南"),
	HENAN46("46", "河南"),
	HENAN47("47", "河南"),
	GUANGDONG51("51", "广东"),
	GUANGDONG52("52", "广东"),
	GUANGXI53("53", "广西"),
	GUANGXI54("54", "广西"),
	GUIZHOU55("55", "贵州"),
	GUIZHOU56("56", "贵州"),
	HAINAN("57", "海南"),
	SICHUAN61("61", "四川"),
	SICHUAN62("62", "四川"),
	SICHUAN63("63", "四川"),
	SICHUAN64("64", "四川"),
	YUNNAN65("65", "云南"),
	YUNNAN66("66", "云南"),
	YUNNAN67("67", "云南"),
	SHANXI71("71", "陕西"),
	SHANXI72("72", "陕西"),
	GANSU73("73", "甘肃"),
	GANSU74("74", "甘肃"),
	NINGXIA("75", "宁夏"),
	QINGHAI("81", "青海"),
	XINJIANG83("83", "新疆"),
	XINJIANG84("84", "新疆"),
	XIZANG85("85", "西藏"),
	XIZANG86("86", "西藏"),
	HONGKONG("999077", "香港"),
	MACAO("999078", "澳门");
	
	private String _key;

	private String _description;

	POST_CODE(String key, String description) {
		_key = key;
		_description = description;
	}

	public String getKey() {
		return _key;
	}

	public String getDescription() {
		return _description;
	}

	public static POST_CODE valueOfKey(String key) {
		Optional<POST_CODE> optionalValue = Arrays.asList(POST_CODE.values()).stream()
				.filter(item -> item.getKey().equals(key)).findFirst();
		if (optionalValue.isPresent())
			return optionalValue.get();
		throw new BusinessException("0002","邮编枚举类中不存在该邮编省级代码:" + key);
	}
}
