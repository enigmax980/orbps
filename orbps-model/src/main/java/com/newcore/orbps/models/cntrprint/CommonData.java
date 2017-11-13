/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.cntrprint;

import com.newcore.orbps.models.cntrprint.vo.CommonDataVO;
import com.newcore.orbps.models.cntrprint.vo.LabelDataVO;
import com.newcore.orbps.models.cntrprint.vo.LabelVO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Guojunjie Created on 16-9-1
 */
public class CommonData {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonData.class);

    private RegInfoData regInfoData;
    private IndexData indexData;
    private PubData pubData;

    public CommonData() {
        this.regInfoData = new RegInfoData();
        this.indexData = new IndexData();
        this.pubData = new PubData();
    }

    public RegInfoData getRegInfoData() {
        return regInfoData;
    }

    public void setRegInfoData(RegInfoData regInfoData) {
        this.regInfoData = regInfoData;
    }

    public IndexData getIndexData() {
        return indexData;
    }

    public void setIndexData(IndexData indexData) {
        this.indexData = indexData;
    }

    public PubData getPubData() {
        return pubData;
    }

    public void setPubData(PubData pubData) {
        this.pubData = pubData;
    }

    public CommonDataVO toPrintData() {
        CommonDataVO vo = new CommonDataVO();
        vo.setRegInfoData(dealLabelData(this.getRegInfoData()));
        vo.setIndexData(dealLabelData(this.getIndexData()));
        vo.setPubData(dealLabelData(this.getPubData()));
        return vo;
    }

    private LabelVO dealLabelData(Object data) {
        LabelVO labelVO = new LabelVO();
        try {
            Map<String, String> valueMap = BeanUtils.describe(data);
            Iterator<String> iterator = valueMap.keySet().iterator();
            while (iterator.hasNext()) {
                String property = iterator.next();
                if (!"class".equalsIgnoreCase(property)) {
                    String value = valueMap.get(property);
                    labelVO.addData(new LabelDataVO(property, value));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("异常", e);
        }
        return labelVO;
    }
}
