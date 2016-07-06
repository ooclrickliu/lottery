/**
 * UserDaoImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.payment.dao.mapper.MapEntryMapper;
import cn.wisdom.lottery.payment.dao.mapper.PermissionMapper;
import cn.wisdom.lottery.payment.dao.vo.Permission;

/**
 * UserDaoImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */

@Repository
public class PermissionDaoImpl implements PermissionDao
{
    @Autowired
    private DaoHelper daoHelper;
    @Autowired
    private PermissionMapper permissionMapper;

    private MapEntryMapper<String, String> entryMapper = new MapEntryMapper<String, String>();

    private static final String SQL_GET_PREFIX = "SELECT id,perm_name,perm_code,perm_desc,is_sa FROM permission ";

    private static final String SQL_GET_PERMISSION_LIST = SQL_GET_PREFIX
            + "WHERE is_sa = false";

    private static final String SQL_GET_PERMISSION_BY_USERID = SQL_GET_PREFIX + "WHERE id "
            + "IN (SELECT ap_perm_id FROM admin_permission WHERE ap_admin_id = ?)";

    private static final String SQL_GET_UNPERMISSION_BY_USERID = SQL_GET_PREFIX + "WHERE id "
            + "NOT IN (SELECT ap_perm_id FROM admin_permission WHERE ap_admin_id = ?)";

    private static final String SQL_GET_INCLUDE_PERMISSION_URL = "SELECT url_name,perm_code FROM permission_url"
            + " WHERE url_type='INCLUDE'";

    private static final String SQL_GET_EXCLUDE_PERMISSION_URL = "SELECT url_name FROM permission_url"
            + " WHERE url_type='EXCLUDE'";

    @Override
    public List<Permission> getAllPermission()
    {

        String errMsg = MessageFormat.format("Failed to query permission list!", (Object[]) null);

        List<Permission> permissions = this.daoHelper.queryForList(SQL_GET_PERMISSION_LIST,
                permissionMapper, errMsg);

        return permissions;
    }

    @Override
    public List<Permission> getPermissionByUserId(int userId)
    {
        String errMsg = MessageFormat.format("Failed to query permission list with user id: {0}!",
                userId);
        List<Permission> permissions = this.daoHelper.queryForList(SQL_GET_PERMISSION_BY_USERID,
                permissionMapper, errMsg, userId);

        return permissions;
    }

    public List<Permission> getUnPermissionByUserId(int userId)
    {
        String errMsg = MessageFormat.format(
                "Failed to query unpermission list with user id: {0}!", userId);
        List<Permission> permissions = this.daoHelper.queryForList(SQL_GET_UNPERMISSION_BY_USERID,
                permissionMapper, errMsg, userId);

        return permissions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.dao.PermissionDao#getIncludePermissionUrlMap()
     */
    @Override
    public List<Entry<String, String>> getIncludePermissionUrlMap()
    {
        String errMsg = "Failed to query exclude permission url list!";
        List<Entry<String, String>> urlMaps = this.daoHelper.queryForList(
                SQL_GET_INCLUDE_PERMISSION_URL, entryMapper, errMsg);

        return urlMaps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.wisdom.lottery.payment.dao.PermissionDao#getExcludePermissionUrlSet()
     */
    @Override
    public List<String> getExcludePermissionUrlList()
    {
        String errMsg = "Failed to query exclude permission url list!";
        List<String> resultList = this.daoHelper.queryForList(SQL_GET_EXCLUDE_PERMISSION_URL,
                String.class, errMsg);

        return resultList;
    }

}