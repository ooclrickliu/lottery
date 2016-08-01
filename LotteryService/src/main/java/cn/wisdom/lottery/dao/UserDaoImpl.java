/**
 * UserDaoImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.dao;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.mapper.DaoRowMapper;
import cn.wisdom.lottery.dao.vo.User;

/**
 * UserDaoImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */

@Repository
public class UserDaoImpl implements UserDao
{
    @Autowired
    private DaoHelper daoHelper;

    private static final String SQL_INSERT_USER = "INSERT IGNORE INTO user(openid, role, "
            + "create_time, update_time) "
            + "VALUES(?, ?, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

    private static final String SQL_GET_PREFIX = "SELECT id, openid, role, real_name, phone, card_no, "
            + " create_time, update_time FROM user ";

    private static final String SQL_GET_USER_BY_OPENID = SQL_GET_PREFIX
            + "WHERE openid = ? LIMIT 1";
    
    private static final String SQL_GET_USER = SQL_GET_PREFIX
    		+ "WHERE id = ? LIMIT 1";

    // private static final String SQL_GET_USER_LIST = SQL_GET_PREFIX
    // + "order by {0} {1} LIMIT {2}, {3}";

    private static final String SQL_GET_USER_BY_NAME = SQL_GET_PREFIX
            + "WHERE admin_name = ? LIMIT 1";

    private static final String SQL_UPDATE_PASSWORD = "UPDATE admin SET admin_pwd = ?, update_time = CURRENT_TIMESTAMP "
            + "WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM admin WHERE id = ?";

    private static final String SQL_GET_USER_BY_PERMISSION = "SELECT id,admin_name,admin_pwd,create_time, update_by "
            + "FROM admin " + "WHERE id "
            + "IN (SELECT ap_admin_id FROM admin_permission WHERE ap_perm_id = ?)";
    
    private static final DaoRowMapper<User> userMapper = new DaoRowMapper<User>(User.class);

    public User getUser(final long id)
    {
        if (id <= 0)
        {
            return null;
        }

        // get from db
        String errMsg = MessageFormat.format("Failed to query user by id {0}!",
                id);
        User user = daoHelper.queryForObject(SQL_GET_USER, userMapper, errMsg,
                id);

        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserDao#getUserByEmail(java.lang.String)
     */
    public User getUserByName(final String name)
    {
        String errMsg = MessageFormat.format("Failed query user by name {0}!",
                name);
        User user = daoHelper.queryForObject(SQL_GET_USER_BY_NAME, userMapper,
                errMsg, name);

        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserDao#save(com.ovt.dao.vo.User)
     */
    public long save(final User user)
    {
        Object[] params = new Object[2];
        params[0] = user.getOpenid();
        params[1] = user.getRole().toString();

        String errMsg = MessageFormat.format("Failed insert user, openid={0}!",
                user.getOpenid());

        long id = daoHelper.save(SQL_INSERT_USER, errMsg, true, params);

        return id;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserDao#isEmailExist(java.lang.String)
     */
    public boolean isNameExist(final String email)
    {
        User user = this.getUserByName(email);
        if(user != null && user.getId() != 0)
        {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserDao#update(long, java.lang.String)
     */
    public void update(final long userId, final String newPassword)
    {
        String errMsg = MessageFormat
                .format("Failed to update user [{0}] password !", userId);

        daoHelper.update(SQL_UPDATE_PASSWORD, errMsg, newPassword, userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserDao#delete(java.lang.String)
     */
    public void delete(final int id)
    {
        // query
        final User user = getUser(id);
        if (user == null)
        {
            return;
        }

        String errMsg = MessageFormat.format("Failed to delete user [{0}]!",
                id);

        daoHelper.update(SQL_DELETE, errMsg, user.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.dao.UserDao#getUserList(com.ovt.dao.vo.PageInfo)SQL_GET_USER_LIST
     */
    public List<User> getUserList()
    {
        // String sql =
        // MessageFormat.format(
        // SQL_GET_USER_LIST,
        // pageInfo.getSortBy(),
        // pageInfo.getOrder().toString(),
        // DataConvertUtils.toString(pageInfo.getPageNo()
        // * pageInfo.getPageSize()),
        // DataConvertUtils.toString(pageInfo.getPageSize()));

        String errMsg = MessageFormat.format("Failed to query users list!",
                (Object[]) null);

        List<User> users = daoHelper.queryForList(SQL_GET_PREFIX, userMapper,
                errMsg);

        return users;
    }

    @Override
    public List<User> getUserByPermission(int permId)
    {
        String errMsg = MessageFormat.format(
                "Failed to query users with permission Id : {0}!", permId);
        List<User> users = daoHelper.queryForList(SQL_GET_USER_BY_PERMISSION,
                userMapper, errMsg, permId);

        return users;
    }

	@Override
	public User getUserByOpenid(String openId) {
		User user = null;
		if (StringUtils.isNotBlank(openId))
        {
			// get from db
	        String errMsg = MessageFormat.format("Failed to query user by openid {0}!",
	        		openId);
	        user = daoHelper.queryForObject(SQL_GET_USER_BY_OPENID, userMapper, errMsg,
	        		openId);
        }

        return user;
	}

}