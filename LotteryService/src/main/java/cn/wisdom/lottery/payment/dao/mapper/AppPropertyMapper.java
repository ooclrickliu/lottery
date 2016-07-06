/**
 * UserMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.wisdom.lottery.payment.dao.constant.DBConstants;
import cn.wisdom.lottery.payment.dao.vo.AppProperty;

/**
 * UserMapper
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public class AppPropertyMapper implements RowMapper<AppProperty>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    public AppProperty mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        AppProperty appProperty = new AppProperty();
        appProperty.setId(rs.getLong(DBConstants.TABLES.APP_PROPERTY.ID));
        appProperty.setPropName(rs.getString(DBConstants.TABLES.APP_PROPERTY.PROP_NAME));
        appProperty.setPropValue(rs.getString(DBConstants.TABLES.APP_PROPERTY.PROP_VALUE));
        appProperty.setDesc(rs.getString(DBConstants.TABLES.APP_PROPERTY.DESC));

        return appProperty;
    }

}
