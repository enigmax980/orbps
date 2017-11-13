package com.newcore.orbpsutils.dao.api;

import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import java.util.List;

/**
 * 保单落地监控表数据层操作接口
 * Created by liushuaifeng on 2017/2/14 0014.
 */
public interface TaskCntrDataLandingQueueDao {

    /**
     * 插入一条保单落地监控数据
     * @param taskCntrDataLandingQueue
     * @return
     */
    public Boolean save(TaskCntrDataLandingQueue taskCntrDataLandingQueue);

    /**
     * 根据taskId查询监控表记录
     * @param taskSeq
     * @return
     */
    public TaskCntrDataLandingQueue searchByTaskSeq(Long taskSeq);

    /**
     * 根据投保单保查询监控表记录
     * @param applNo
     * @return
     */
    public TaskCntrDataLandingQueue searchByApplNo(String applNo);

    /**
     * 根据任务状态后去监控表列表
     * @param status
     * @return
     */
    public List<TaskCntrDataLandingQueue> searchByStatus(String status);

    /**
     * 根据投保单号更新taskCntrDataLandingQueue记录
     * @param taskSeq
     * @param taskCntrDataLandingQueue
     * @return
     */
    public Boolean updateByTaskSeq(Long taskSeq, TaskCntrDataLandingQueue taskCntrDataLandingQueue);

    /**
     * 根据taskId,更新监控表记录中的status
     * @param taskSeq
     * @param status
     * @return
     */
    public Boolean updateStatusByTaskSeq(Long taskSeq, String status);

    /**
     * 根据投保单号更新财务落地状态
     * @param applNo
     * @param status
     * @return
     */
    public Boolean updateFinLandFlagByApplNo(String applNo, String status);

    /**
     * 根据投保单号更新被保人落地状态
     * @param applNo
     * @param status
     * @return
     */
    public Boolean updateIpsnLandFlagByApplNo(String applNo, String status);

    /**
     * 根据投保单号删除监控表
     * @param applNo
     * @return
     */
    public Boolean deleteByApplNo(String applNo);

    /**
     * @author huanglong
     * @date 2017年4月7日
     * @param TaskCntrDataLandingQueueDao
     * @return Boolean
     * @content 档案清单补导入，修改落地表，发起被保人清单导入
     */
    public Boolean updateArchiveList(String applNo);
    
    
    /**
     * 根据投保单号更新应收付落地状态
     * @param applNo
     * @param status
     * @return
     */
    public Boolean updatePlnLandFlagByApplNo(String applNo,String status);

}
