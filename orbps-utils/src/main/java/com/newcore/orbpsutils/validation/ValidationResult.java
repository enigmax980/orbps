/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbpsutils.validation;

import java.util.List;

/**
 * @author Guojunjie
 *         Created on 16-8-19
 */
public class ValidationResult {
  private boolean notPass;
  private List<String> causes;

  public boolean isNotPass() {
    return notPass;
  }

  public void setNotPass(boolean notPass) {
    this.notPass = notPass;
  }

  public List<String> getCauses() {
    return causes;
  }

  public void setCauses(List<String> causes) {
    this.causes = causes;
  }
}
