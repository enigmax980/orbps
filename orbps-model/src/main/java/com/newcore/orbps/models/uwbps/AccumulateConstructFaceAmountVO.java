package com.newcore.orbps.models.uwbps;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3.
 */
public class AccumulateConstructFaceAmountVO implements Serializable {
    private static final long serialVersionUID = 5841059434982055776L;

    private String iobjName;
    private String sumIobjFaceAmnt;

    public String getIobjName() {
        return iobjName;
    }

    public void setIobjName(String iobjName) {
        this.iobjName = iobjName;
    }

    public String getSumIobjFaceAmnt() {
        return sumIobjFaceAmnt;
    }

    public void setSumIobjFaceAmnt(String sumIobjFaceAmnt) {
        this.sumIobjFaceAmnt = sumIobjFaceAmnt;
    }
}
