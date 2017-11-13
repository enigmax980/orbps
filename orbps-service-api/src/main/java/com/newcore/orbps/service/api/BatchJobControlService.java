package com.newcore.orbps.service.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.newcore.orbps.models.para.RetInfo;


/**
 * RESTFul webservice 服务. 保单基本信息录入接口
 * 
 * @author zhoushoubo 创建时间：2017年4月7日下午2:48:53
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BatchJobControlService {

    /**
     * 批量把start、starting状态的批作业改成stopped。
     * @throws NoSuchJobException
     * @throws NoSuchJobExecutionException
     * @throws JobExecutionNotRunningException
     */
	@POST
	@Path("/stopMul")
    public RetInfo stopMultiBatchJob();
    
    /**
     * 
     * 把start、starting状态的批作业改成stopped。
     * @throws NoSuchJobException
     * @throws NoSuchJobExecutionException
     */
	@POST
	@Path("/stopImp")
    public RetInfo stopImportIpsnListBatchJob(String applNo) throws NoSuchJobExecutionException, JobExecutionNotRunningException;
    
    /**
     * 批量重启批作业
     * @throws NoSuchJobException
     * @throws JobParametersInvalidException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     * @throws NoSuchJobExecutionException
     */
	@POST
	@Path("/restart")
    public void restartStoppedBatJob() throws JobParametersInvalidException;

    /**
     * 检查批作业是否停止完毕
     * @return RetInfo
     * @throws NoSuchJobException
     */
	@POST
	@Path("/getS")
    public RetInfo getIsBatchStopped() throws NoSuchJobException;
}
