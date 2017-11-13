/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbpsutils.validation;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guojunjie on 16-7-1.
 */
public class JSONUtils {
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    private JSONUtils() {
    }

    public static String toJSON(Object object) {
        try {
            return JSON.toJSONString(object,false);
        } catch (Exception e) {
            LOGGER.warn("转换成JSON出现错误:{}", e);
            return "";
        }
    }

    public static <T> T toObject(String jsonString, Class<T> clazz){
        try {
            return JSON.parseObject(jsonString,clazz);
        } catch (Exception e) {
            LOGGER.warn("转换成对象出现错误:{}", e);
            return null;
        }
    }
}
