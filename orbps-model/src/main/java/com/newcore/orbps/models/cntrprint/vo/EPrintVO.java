/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.cntrprint.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author chenyc
 *         Created on 16-8-13
 */
public class EPrintVO  implements Serializable {
  private static final long serialVersionUID = -6926298125055369414L;

  @JSONField(ordinal=1)
  private CommonDataVO commonDatas;

  @JSONField(ordinal=2)
  private PrintDataVO printData;

  public CommonDataVO getCommonDatas() {
    return commonDatas;
  }

  public void setCommonDatas(CommonDataVO commonDatas) {
    this.commonDatas = commonDatas;
  }

  public PrintDataVO getPrintData() {
    return printData;
  }

  public void setPrintData(PrintDataVO printData) {
    this.printData = printData;
  }
}
