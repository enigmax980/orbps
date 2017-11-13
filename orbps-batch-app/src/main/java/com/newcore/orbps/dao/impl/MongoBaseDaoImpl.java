package com.newcore.orbps.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;

/**
 * 对mongodb的操作DAO.
 *
 * @author niuzhihao
 * @dete 2016-8-1 20 :16:18
 */
@Repository("mongoBaseDao")
public class MongoBaseDaoImpl implements MongoBaseDao {

	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(MongoBaseDaoImpl.class);

	/**
	 * spring整合mongodb的主要类，具有大部分增删改查方法
	 */
	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * 查询所有.
	 *
	 * @param Class
	 * @return the list
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public <T> List<T> findAll(Class object) {
		return mongoTemplate.findAll(object);
	}

	/**
	 * 根据条件查询一条
	 *
	 * @param Class
	 *            map
	 * @return the Object
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public Object findOne(Class object, Map<String, Object> map) {
		Query query = new Query();
		for (Map.Entry<String, Object> key : map.entrySet()) {
			query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
		}
		return mongoTemplate.findOne(query, object);
	}

	/**
	 * 根据条件查询多条
	 *
	 * @param Class
	 *            map
	 * @return the List
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public <T> List<T> find(Class object, Map<String, Object> map) {
		Query query = new Query();
		for (Map.Entry<String, Object> key : map.entrySet()) {
			query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
		}
		return mongoTemplate.find(query, object);
	}

	@Override
	public <T> List<T> find(Class object, Query query) {
		return mongoTemplate.find(query, object);
	}

	/**
	 * 根据条件分页查询
	 *
	 * @param Class
	 *            map
	 * @return the list
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public <T> List<T> findByPage(Class object, Map<String, Object> map) {
		Query query = new Query();
		for (Map.Entry<String, Object> key : map.entrySet()) {
			if (key.getKey() != "page" && key.getKey() != "size") {
				query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
			}
		}
		query.skip(((Integer) map.get("page") - 1) * (Integer) map.get("size"));
		query.limit((Integer) map.get("size"));
		return mongoTemplate.find(query, object);
	}

	/**
	 * 添加
	 *
	 * @param object
	 * @return the Object
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public void insert(Object object) {
		mongoTemplate.insert(object);
		logger.debug("save entity: {}", object);
	}

	/**
	 * 批量添加
	 *
	 * @param list
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public void insertAll(List<?> list) {
		try {
			mongoTemplate.insertAll(list);
		} catch (DuplicateKeyException e) {
			logger.info("添加时id重复");
			throw e;
		}
	}

	/**
	 * 根据条件修改一条
	 *
	 * @param Class
	 *            map update
	 * @return the WriteResult
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public int update(Class object, Map<String, Object> map, Update update) {
		Query query = new Query();
		if (map != null && (!map.isEmpty())) {
			for (Map.Entry<String, Object> key : map.entrySet()) {
				query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
			}
			return mongoTemplate.updateFirst(query, update, object).getN();
		} else {
			logger.error("map不能为空，请输入有效参数");
			return 0;
		}
	}

	@Override
	public int updateAll(Class object, Map<String, Object> map, Update update) {
		Query query = new Query();
		if (map != null && (!map.isEmpty())) {
			for (Map.Entry<String, Object> key : map.entrySet()) {
				query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
			}
			return mongoTemplate.updateMulti(query, update, object).getN();
		} else {
			logger.error("map不能为空，请输入有效参数");
			return 0;
		}
	}

	public int updateAll(Class object, Query query, Update update) {
		return mongoTemplate.updateMulti(query, update, object).getN();
	}

	/**
	 * 根据条件删除一条
	 *
	 * @param Class
	 *            map
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public int remove(Class object, Map<String, Object> map) {
		Query query = new Query();
		if (map != null && (!map.isEmpty())) {
			for (Map.Entry<String, Object> key : map.entrySet()) {
				query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
			}
			return mongoTemplate.remove(query, object).getN();
		} else {
			logger.error("map不能为空，请输入有效参数");
			return 0;
		}
	}

	/**
	 * 根据字段查最大值的一条数据
	 *
	 * @param Class
	 *            String
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public Object getMax(Class object, String string) {
		Query query = new BasicQuery("{}").with(new Sort(new Sort.Order(Sort.Direction.DESC, string))).limit(1);
		return mongoTemplate.find(query, object);
	}

	/**
	 * 根据字段查最小值的一条数据
	 *
	 * @param Class
	 *            String
	 * @return void
	 * @author nzh
	 * @date 2016-8-1 20 :16:18
	 */
	@Override
	public Object getMin(Class object, String string) {
		Query query = new BasicQuery("{}").with(new Sort(new Sort.Order(Sort.Direction.ASC, string))).limit(1);
		return mongoTemplate.find(query, object);
	}

	/**
	 * @return the mongoTemplate
	 */
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	/**
	 * @param mongoTemplate
	 *            the mongoTemplate to set
	 */
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;

	}

	public long count(Class object, Map<String, Object> map) {
		Query query = new Query();
		for (Map.Entry<String, Object> key : map.entrySet()) {
			query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
		}
		return mongoTemplate.count(query, object);
	}

	/**
	 * @author:huanglong
	 * @date 2016年11月22日
	 * @args String applNo,InsurApplOperTrace insurApplOperTrace
	 * @return void
	 * @content:更新操作轨迹
	 */
	@Override
	public Boolean updateOperTrace(String applNo,TraceNode traceNode){
		Assert.notNull(applNo);
		Assert.notNull(traceNode);
		
		/*对轨迹操作InsurApplOperTrace 增加核保节点*/						
		InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(applNo)),InsurApplOperTrace.class);
		/*如果操作轨迹表中没有 该保单号信息  则新建一个*/
		if(null == insurApplOperTrace){
			insurApplOperTrace = new InsurApplOperTrace();
			insurApplOperTrace.setApplNo(applNo);
			insurApplOperTrace.pushTraceNode(traceNode);
			mongoTemplate.insert(insurApplOperTrace);
			return true;
		}
		insurApplOperTrace.pushTraceNode(traceNode);
		Update updateOperTrace = new Update();			
		updateOperTrace.set("operTraceDeque", insurApplOperTrace.getOperTraceDeque());
		mongoTemplate.updateFirst(Query.query(Criteria.where("applNo").is(applNo)), updateOperTrace, InsurApplOperTrace.class);
		return true;    	
	}
	/*
	 * 获取当前操作轨迹节点
	 * */
	@Override
	public TraceNode getPeekTraceNode(String applNo){
		Assert.notNull(applNo);
		InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(applNo)), InsurApplOperTrace.class);
		return insurApplOperTrace.getCurrentTraceNode();
	}	
	
	
	public int removeByQuery(Class object, Map<String, Object> map) {
		Query query = new Query();
		if (map != null && (!map.isEmpty())) {
			for (Map.Entry<String, Object> key : map.entrySet()) {
				if(StringUtils.equals(key.getValue().getClass().getTypeName(), "java.util.ArrayList")){
					query.addCriteria(Criteria.where(key.getKey()).in((ArrayList<Long>)key.getValue()));
				}else{
					query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));
				}
			
			}
			return mongoTemplate.remove(query, object).getN();
		} else {
			logger.error("map不能为空，请输入有效参数");
			return 0;
		}
	}
	
	/*
	 * 获取当前影像审批轨迹节点
	 * */
	@Override
	public TraceNode getPeekImgApprovalTrace(String applNo){
		Assert.notNull(applNo);
		InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(applNo)), InsurApplOperTrace.class);
		return insurApplOperTrace.getCurrentImgApprovalTrace();
	}
	/**
	 * 更新影像审批轨迹
	 */
	@Override
	public Boolean updateImgApprovalTrace(String applNo,TraceNode traceNode){
		Assert.notNull(applNo);
		Assert.notNull(traceNode);
		
		/*对轨迹操作InsurApplOperTrace 增加核保节点*/						
		InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(applNo)),InsurApplOperTrace.class);
		/*如果操作轨迹表中没有 该保单号信息  则新建一个*/
		if(null == insurApplOperTrace){
			insurApplOperTrace = new InsurApplOperTrace();
			insurApplOperTrace.setApplNo(applNo);
			insurApplOperTrace.pushImgApprovalTrace(traceNode);
			mongoTemplate.insert(insurApplOperTrace);
			return true;
		}
		insurApplOperTrace.pushImgApprovalTrace(traceNode);
		Update updateOperTrace = new Update();			
		updateOperTrace.set("imgApprovalTraceDeque", insurApplOperTrace.getImgApprovalTraceDeque());
		mongoTemplate.updateFirst(Query.query(Criteria.where("applNo").is(applNo)), updateOperTrace, InsurApplOperTrace.class);
		return true;    	
	}


}