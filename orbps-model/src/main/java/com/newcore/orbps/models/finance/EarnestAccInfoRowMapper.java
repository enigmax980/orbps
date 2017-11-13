package com.newcore.orbps.models.finance;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * EarnestAccInfo 对应的mapper实体来
 * Created by liushuaifeng on 2017/2/27 0027.
 */
public class EarnestAccInfoRowMapper implements RowMapper<EarnestAccInfo> {

    @Override
    public EarnestAccInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

        EarnestAccInfo earnestAccInfo = new EarnestAccInfo();
        earnestAccInfo.setAccId(rs.getLong("ACC_ID"));
        earnestAccInfo.setAccNo(rs.getString("ACC_NO"));
        earnestAccInfo.setAccPersonNo(rs.getString("ACC_PERSON_NO"));
        earnestAccInfo.setAccType(rs.getString("ACC_TYPE"));
        earnestAccInfo.setBalance(rs.getBigDecimal("BALANCE"));
        earnestAccInfo.setFrozenBalance(rs.getBigDecimal("FROZEN_BALANCE"));
        return earnestAccInfo;
    }
}
