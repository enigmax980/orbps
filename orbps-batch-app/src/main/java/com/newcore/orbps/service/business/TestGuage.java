package com.newcore.orbps.service.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

public class TestGuage implements Tasklet {
	@Autowired
	MongoBaseDao mongoBaseDao;

	private String applNo;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		if (mongoBaseDao.findOne(GrpInsurAppl.class, map) == null) {
			throw new BusinessException("当前投保单号无数据");
		}
		return RepeatStatus.FINISHED;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

}
