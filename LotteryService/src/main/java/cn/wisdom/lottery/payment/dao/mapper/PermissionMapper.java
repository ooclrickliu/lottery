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
import cn.wisdom.lottery.payment.dao.vo.Permission;

/**
 * UserMapper
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public class PermissionMapper implements RowMapper<Permission>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Permission permission = new Permission();
        permission.setId(rs.getInt(DBConstants.TABLES.PERMISSION.ID));
        permission.setPermissionId(rs.getInt(DBConstants.TABLES.PERMISSION.ID));
        permission.setPermissionName(rs.getString(DBConstants.TABLES.PERMISSION.PERM_NAME));
        permission.setPermissionCode(rs.getString(DBConstants.TABLES.PERMISSION.PERM_CODE));
        permission.setPermissionDesc(rs.getString(DBConstants.TABLES.PERMISSION.PERM_DESC));
        permission.setSA(rs.getBoolean(DBConstants.TABLES.PERMISSION.IS_SA));
        return permission;
    }

}
