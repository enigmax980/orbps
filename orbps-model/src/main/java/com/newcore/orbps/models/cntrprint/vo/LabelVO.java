/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbps.models.cntrprint.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenyc
 *         Created on 16-8-13
 */
public class LabelVO  implements Serializable {
  private static final long serialVersionUID = 3347859459941085783L;
  private List<LabelDataVO> label;

  public List<LabelDataVO> getLabel() {
    return label;
  }

  public void setLabel(List<LabelDataVO> label) {
    this.label = label;
  }

  public void addData(LabelDataVO labelDataVO){
    if(null == label){
      label =  new ArrayList<>();
    }
    label.add(labelDataVO);
  }
}
