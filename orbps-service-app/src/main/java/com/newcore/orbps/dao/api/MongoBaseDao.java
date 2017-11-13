package com.newcore.orbps.dao.api;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.newcore.orbps.models.opertrace.TraceNode;

/**
 * 对mongodb的操作DAO.
 *
 * @author niuzhihao
 * @dete  2016-8-1 20 :16:18
 */

public interface MongoBaseDao {

	/**
	 * 查询所有.
	 *
	 * @param Class
	 * @return the list
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public  List<?> findAll(Class object);
	/**
	 * 根据条件查询一条
	 *
	 * @param Class  map
	 * @return the Object
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public Object findOne(Class object,Map<String,Object> map);
	/**
	 * 根据条件模糊查询
	 *
	 * @param Class  map
	 * @return the List
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public List<?> find(Class object,Map<String,Object> map);



	public List<?> find(Class object,Query query);
	/**
	 * 根据条件分页查询
	 *
	 * @param Class  map
	 * @return the list
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public List<?> findByPage(Class object,Map<String,Object> map);

	/**
	 * 添加
	 *
	 * @param object
	 * @return the Object
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public void insert(Object object);
	/**
	 * 批量添加
	 *
	 * @param list
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public void insertAll(List<?> list);
	/**
	 * 根据条件修改一条
	 *
	 * @param Class  map   update
	 * @return the WriteResult
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public int update(Class object,Map<String,Object> map,Update update);
	/**
	 * 根据条件修改多条
	 *
	 * @param Class  map   update
	 * @return the WriteResult
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public int updateAll(Class object,Map<String,Object> map,Update update);
	/**
	 * 根据条件删除一条
	 *
	 * @param Class  map   
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */		  
	public int remove(Class object,Map<String,Object> map);
	/**
	 * 根据字段查最大值的一条数据
	 *
	 * @param Class  String   
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */		  
	public Object getMax(Class object,String string);
	/**
	 * 根据字段查最小值的一条数据
	 *
	 * @param Class  String  
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	public Object getMin(Class object,String string);



	/**
	 * @author huanglong
	 * @date 2016年11月22日
	 * @param String,TraceNode
	 * @return Boolean
	 * @content 增加操作轨迹节点函数
	 */
	public Boolean updateOperTrace(String applNo,TraceNode traceNode);
	
	/**
	 * @author huanglong
	 * @date 2016年11月23日
	 * @param MongoBaseDao
	 * @return TraceNode
	 * @content 查询操作轨迹顶层节点
	 */
	public TraceNode getPeekTraceNode(String applNo);
	
	public <T> long count(Class<T> object, Map<String,Object> map);
	/**
	 * 查询影像审批顶层节点
	 * @param applNo
	 * @return
	 */
	public TraceNode getPeekImgApprovalTrace(String applNo);
	/**
	 * 增加影像审批节点
	 */
	public Boolean updateImgApprovalTrace(String applNo,TraceNode traceNode);
}
