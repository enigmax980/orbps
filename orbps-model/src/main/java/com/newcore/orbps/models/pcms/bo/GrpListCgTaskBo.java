package com.newcore.orbps.models.pcms.bo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 团单清单转合同任务表Model.
 */

public class GrpListCgTaskBo implements java.io.Serializable {
	


	

		private static final long serialVersionUID = -8249254334135000418L;

		
		/**
		 * 管理机构
		 */
		@NotNull(message="[管理机构 不能为空]")
		@Length(max=6,message="[管理机构长度不能大于6位]")
		private String mgBranch;
		/**
		 * 险种（第一主险）
		 */
		@NotNull(message="[险种（第一主险 不能为空]")
		@Length(max=8,message="[险种（第一主险长度不能大于8位]")
		private String polCode;
		/**
		 * 投保单号
		 */
		@NotNull(message="[投保单号 不能为空]")
		@Length(max=16,message="[投保单号长度不能大于16位]")
	    private String applNo;
		
		/**
		 * 组合保单号
		 */
		@NotNull(message="[组合保单号不能为空]")
		@Length(max=25,message="[组合保单号长度不能大于25位]")
		private String cgNo;
		
		/**
		 * 批次号
		 */
		@NotNull(message="[批次号 不能为空]")
		private Long batNo;
		
		/**
		 * 清单总量
		 */
		@NotNull(message="[清单总量 不能为空]")
		private Long batchIpsnNum;
		
		
		/**
		 * 总保费
		 */
		@NotNull(message="[总保费不能为空]")
		private Double batchSumPremium;
		
		/**
		 * 总保额
		 */
		@NotNull(message="[总保额 不能为空]")
		private Double batchSumFaceAmnt;
		
	
		
	
		public String getCgNo() {
			return cgNo;
		}
		public void setCgNo(String cgNo) {
			this.cgNo = cgNo;
		}
		
	
		public String getMgBranch() {
			return mgBranch;
		}
		public void setMgBranch(String mgBranch) {
			this.mgBranch = mgBranch;
		}
		public String getPolCode() {
			return polCode;
		}
		public void setPolCode(String polCode) {
			this.polCode = polCode;
		}
		public String getApplNo() {
			return applNo;
		}
		public void setApplNo(String applNo) {
			this.applNo = applNo;
		}
		
		public Long getBatNo() {
			return batNo;
		}
		public void setBatNo(Long batNo) {
			this.batNo = batNo;
		}
		public Long getBatchIpsnNum() {
			return batchIpsnNum;
		}
		public void setBatchIpsnNum(Long batchIpsnNum) {
			this.batchIpsnNum = batchIpsnNum;
		}
		public Double getBatchSumPremium() {
			return batchSumPremium;
		}
		public void setBatchSumPremium(Double batchSumPremium) {
			this.batchSumPremium = batchSumPremium;
		}
		public Double getBatchSumFaceAmnt() {
			return batchSumFaceAmnt;
		}
		public void setBatchSumFaceAmnt(Double batchSumFaceAmnt) {
			this.batchSumFaceAmnt = batchSumFaceAmnt;
		}
	
}
