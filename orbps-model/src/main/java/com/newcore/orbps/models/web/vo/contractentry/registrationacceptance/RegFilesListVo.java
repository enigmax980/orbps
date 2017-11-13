package com.newcore.orbps.models.web.vo.contractentry.registrationacceptance;

import java.io.Serializable;

/**
 * 资料清单
 * @author jincong
 *
 */
public class RegFilesListVo implements Serializable{
	
	private static final long serialVersionUID = 13242344407L;
	
	/**字段名：资料类型*/
	private String fileType;
		
	/**字段名：资料数目*/
	private int fileNum;

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileNum
     */
    public int getFileNum() {
        return fileNum;
    }

    /**
     * @param fileNum the fileNum to set
     */
    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RegFilesListVo [fileType=" + fileType + ", fileNum=" + fileNum + "]";
    }

}
