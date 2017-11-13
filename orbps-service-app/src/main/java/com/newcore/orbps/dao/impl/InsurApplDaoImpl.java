package com.newcore.orbps.dao.impl;

import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.dao.api.InsurApplDao;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.supports.models.OrderType;
import com.newcore.supports.models.service.bo.OrderInfo;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.service.api.PageQueryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.*;

@Repository("insurApplDao")
public class InsurApplDaoImpl implements InsurApplDao {


    /**
     * The constant SEARCH_INSUR_APPL_SQL.
     */
    private static final String EXAMPLE_SUMMARY_SQL = "SELECT * FROM T_QUERY_APPL WHERE";

    private static final String EXAMPLE_SUMMARY_COUNT_SQL_NULL = "SELECT COUNT(*) FROM T_QUERY_APPL";

    private static final String EXAMPLE_SUMMARY_COUNT_SQL = "SELECT COUNT(*) FROM T_QUERY_APPL WHERE";

    /**
     * 分页辅助工具
     */
    @Autowired
    PageQueryService pageQueryService;

    /**
     * ; JDBC操作工具
     */
    @Autowired
    JdbcOperations jdbcTemplate;


    /**
     * 判断insurApplVo对象中险种的值是否全部为NULL或者"",如果不为空则返回true
     */
    public boolean judgeObjectIsNull(ResponseVo productVo) {
        boolean flag = false;
        if (null != productVo.getBusiPrdCode() && !"".equals(productVo.getBusiPrdCode())) {
            return true;
        }
        return flag;
    }

    /**
     * 拼接SQL语句
     *
     * @throws ParseException
     */
    public Map<String, List<String>> sqlConditionMap(ResponseVo productVo) {
        List<String> values = new ArrayList<String>();
        List<String> condition = new ArrayList<String>();
        StringBuilder sql = new StringBuilder();
        Map<String, List<String>> sqlMap = new HashMap<String, List<String>>();
        if (null != productVo.getBusiPrdCode() && !"".equals(productVo.getBusiPrdCode())) {
            condition.add(" BUSI_PRD_CODE = ? ");
            values.add(productVo.getBusiPrdCode());
        }

        for (int i = 0; i < condition.size(); i++) {
            if (i != condition.size() - 1) {
                sql.append(condition.get(i) + "AND");
            } else {
                sql.append(condition.get(i));
            }
        }

        sqlMap.put(sql.toString(), values);
        return sqlMap;
    }

    @Override
    public List<ResponseVo> getProductSummaryList(PageQuery<ResponseVo> pageQuery) {
        List<ResponseVo> productVo = null;
        /**
         * 构建排序条件 如果未要求任何排序条件，则添加PRD_CODE作为排序 如果已经存在了排序条件，则需要对排序条件属性做数据库字段转换
         */
        if (CollectionUtils.isEmpty(pageQuery.getOrderInfoList())) {
            OrderInfo pkOrder = new OrderInfo();
            pkOrder.setOrderType(OrderType.ASC.name());
            pkOrder.setColumnName("APPL_ID");
            pageQuery.setOrderInfoList(Collections.singletonList(pkOrder));
        }
        /**
         * 对排序条件属性做数据库字段转换
         */
        else {
            pageQuery.getOrderInfoList()
                    .forEach(orderInfo -> orderInfo.setColumnName(DaoUtils.toColumnName(orderInfo.getColumnName())));
        }

        Map<String, List<String>> conditions = sqlConditionMap(pageQuery.getCondition());
        String whereConditionSql = conditions.keySet().stream().findFirst().get();
        String pageSql = pageQueryService.buildPageQuerySql(EXAMPLE_SUMMARY_SQL + whereConditionSql, pageQuery);
        List<String> whereConditionValues = conditions.get(whereConditionSql);

        productVo = jdbcTemplate.query(pageSql, DaoUtils.createRowMapper(ResponseVo.class),
            whereConditionValues.toArray());
        return productVo;
    }

    // 总记录
    @Override
    public long getProductSummaryCount(ResponseVo queryCondition) {
        if (!judgeObjectIsNull(queryCondition)) {
            return jdbcTemplate.queryForObject(EXAMPLE_SUMMARY_COUNT_SQL_NULL, Long.class);
        } else {
        	Map<String, List<String>> conditions = sqlConditionMap(queryCondition);
            String whereConditionSql = conditions.keySet().stream().findFirst().get();
            List<String> whereConditionValues = conditions.get(whereConditionSql);
            return jdbcTemplate.queryForObject(EXAMPLE_SUMMARY_COUNT_SQL + whereConditionSql, Long.class, whereConditionValues.toArray());
        }
    }
}
