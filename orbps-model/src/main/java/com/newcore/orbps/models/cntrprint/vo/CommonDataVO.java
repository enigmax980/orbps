package com.newcore.orbps.models.cntrprint.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author chenyc
 *         Created on 16-8-13
 */
public class CommonDataVO implements Serializable {
  private static final long serialVersionUID = -1081964949350408575L;

  @JSONField(ordinal=1)
  private LabelVO regInfoData;

  @JSONField(ordinal=2)
  private LabelVO indexData;

  @JSONField(ordinal=3)
  private LabelVO pubData;

  public LabelVO getRegInfoData() {
    return regInfoData;
  }

  public void setRegInfoData(LabelVO regInfoData) {
    this.regInfoData = regInfoData;
  }

  public LabelVO getIndexData() {
    return indexData;
  }

  public void setIndexData(LabelVO indexData) {
    this.indexData = indexData;
  }

  public LabelVO getPubData() {
    return pubData;
  }

  public void setPubData(LabelVO pubData) {
    this.pubData = pubData;
  }
}
