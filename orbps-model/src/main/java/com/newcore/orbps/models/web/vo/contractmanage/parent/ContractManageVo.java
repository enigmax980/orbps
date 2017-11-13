package com.newcore.orbps.models.web.vo.contractmanage.parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.newcore.orbps.models.web.vo.contractmanage.accountconfig.AccountOperatingTrackVo;
import com.newcore.orbps.models.web.vo.contractmanage.accountconfig.AccountOwnerVo;
import com.newcore.orbps.models.web.vo.contractmanage.contractconfig.ContractComponentListVo;
import com.newcore.orbps.models.web.vo.contractmanage.contractconfig.ContractOperatingTrackVo;
import com.newcore.orbps.models.web.vo.contractmanage.contractconfig.ContractRuleVo;
import com.newcore.orbps.models.web.vo.contractmanage.examineConfig.ExamineRuleVo;

/**
 * 团单面向前端大Vo
 * @author xiaoye
 *
 */
public class ContractManageVo  implements Serializable{
	
	private static final long serialVersionUID = 1434454457L;
	
	/** 团单管理信息 */
	private AccountOwnerVo accountOwnerVo;
	/** 规则添加信息 */
	private RuleAddVo ruleAddVo;
	/** 规则查询信息 */
	private RuleQueryVo ruleQueryVo;
	/** 审核查询信息 */
	private RuleCheckVo ruleCheckVo;
	/** 审核信息 **/
	private ExamineRuleVo examineRuleVo;
	/** account操作轨迹查询信息 */
	private AccountOperatingTrackVo accountOperatingTrackVo;
	/** 构件基本信息 */
	private List<ContractComponentListVo> contractComponentListVos = new ArrayList<ContractComponentListVo>();
	/** 规则管理信息 */
	private ContractRuleVo contractRuleVo;
	/** contract操作轨迹查询信息 */
	private List<ContractOperatingTrackVo> contractOperatingTrackVos = new ArrayList<ContractOperatingTrackVo>();
	/** 详细信息查询examineRulesDetailViewVo */
	private ExamineRulesDetailViewVo examineRulesDetailViewVo;
	/**
	 * @return the accountOwnerVo
	 */
	public AccountOwnerVo getAccountOwnerVo() {
		return accountOwnerVo;
	}
	/**
	 * @param accountOwnerVo the accountOwnerVo to set
	 */
	public void setAccountOwnerVo(AccountOwnerVo accountOwnerVo) {
		this.accountOwnerVo = accountOwnerVo;
	}
	/**
	 * @return the ruleAddVo
	 */
	public RuleAddVo getRuleAddVo() {
		return ruleAddVo;
	}
	/**
	 * @param ruleAddVo the ruleAddVo to set
	 */
	public void setRuleAddVo(RuleAddVo ruleAddVo) {
		this.ruleAddVo = ruleAddVo;
	}
	/**
	 * @return the ruleCheckVo
	 */
	public RuleCheckVo getRuleCheckVo() {
		return ruleCheckVo;
	}
	/**
	 * @param ruleCheckVo the ruleCheckVo to set
	 */
	public void setRuleCheckVo(RuleCheckVo ruleCheckVo) {
		this.ruleCheckVo = ruleCheckVo;
	}
	/**
	 * @return the accountOperatingTrackVo
	 */
	public AccountOperatingTrackVo getAccountOperatingTrackVo() {
		return accountOperatingTrackVo;
	}
	/**
	 * @param accountOperatingTrackVo the accountOperatingTrackVo to set
	 */
	public void setAccountOperatingTrackVo(AccountOperatingTrackVo accountOperatingTrackVo) {
		this.accountOperatingTrackVo = accountOperatingTrackVo;
	}
	/**
	 * @return the contractComponentListVos
	 */
	public List<ContractComponentListVo> getContractComponentListVos() {
		return contractComponentListVos;
	}
	/**
	 * @param contractComponentListVos the contractComponentListVos to set
	 */
	public void setContractComponentListVos(List<ContractComponentListVo> contractComponentListVos) {
		this.contractComponentListVos = contractComponentListVos;
	}
	/**
	 * @return the contractRuleVo
	 */
	public ContractRuleVo getContractRuleVo() {
		return contractRuleVo;
	}
	/**
	 * @param contractRuleVo the contractRuleVo to set
	 */
	public void setContractRuleVo(ContractRuleVo contractRuleVo) {
		this.contractRuleVo = contractRuleVo;
	}
	/**
	 * @return the contractOperatingTrackVos
	 */
	public List<ContractOperatingTrackVo> getContractOperatingTrackVos() {
		return contractOperatingTrackVos;
	}
	/**
	 * @param contractOperatingTrackVos the contractOperatingTrackVos to set
	 */
	public void setContractOperatingTrackVos(List<ContractOperatingTrackVo> contractOperatingTrackVos) {
		this.contractOperatingTrackVos = contractOperatingTrackVos;
	}
	/**
	 * @return the ruleQueryVo
	 */
	public RuleQueryVo getRuleQueryVo() {
		return ruleQueryVo;
	}
	/**
     * @return the examineRulesDetailViewVo
     */
    public ExamineRulesDetailViewVo getExamineRulesDetailViewVo() {
        return examineRulesDetailViewVo;
    }
    /**
     * @param examineRulesDetailViewVo the examineRulesDetailViewVo to set
     */
    public void setExamineRulesDetailViewVo(ExamineRulesDetailViewVo examineRulesDetailViewVo) {
        this.examineRulesDetailViewVo = examineRulesDetailViewVo;
    }
    /**
	 * @param ruleQueryVo the ruleQueryVo to set
	 */
	public void setRuleQueryVo(RuleQueryVo ruleQueryVo) {
		this.ruleQueryVo = ruleQueryVo;
	}
	/**
	 * @return the examineRuleVo
	 */
	public ExamineRuleVo getExamineRuleVo() {
		return examineRuleVo;
	}
	/**
	 * @param examineRuleVo the examineRuleVo to set
	 */
	public void setExamineRuleVo(ExamineRuleVo examineRuleVo) {
		this.examineRuleVo = examineRuleVo;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContractManageVo [accountOwnerVo=" + accountOwnerVo + ", ruleAddVo=" + ruleAddVo + ", ruleQueryVo="
                + ruleQueryVo + ", ruleCheckVo=" + ruleCheckVo + ", examineRuleVo=" + examineRuleVo
                + ", accountOperatingTrackVo=" + accountOperatingTrackVo + ", contractComponentListVos="
                + contractComponentListVos + ", contractRuleVo=" + contractRuleVo + ", contractOperatingTrackVos="
                + contractOperatingTrackVos + ", examineRulesDetailViewVo=" + examineRulesDetailViewVo + "]";
    }
	
    
}
