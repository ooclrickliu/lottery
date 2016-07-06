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
import cn.wisdom.lottery.payment.dao.vo.User;

/**
 * UserMapper
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public class UserMapper implements RowMapper<User>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    public User mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        User user = new User();
        user.setId(rs.getLong(DBConstants.TABLES.USER.ID));
        user.setUserName(rs.getString(DBConstants.TABLES.USER.U_NAME));
        user.setPassword(rs.getString(DBConstants.TABLES.USER.U_PASSWORD));
        user.setCreateTime(rs.getTimestamp(DBConstants.TABLES.USER.CREATE_TIME));
        //user.setUpdateTime(rs.getTimestamp(DBConstants.TABLES.USER.UPDATE_TIME));

        return user;
    }

}
