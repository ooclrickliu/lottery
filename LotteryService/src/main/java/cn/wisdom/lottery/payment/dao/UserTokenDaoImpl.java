/**
 * UserAccessTokenDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.dao;

import java.sql.Timestamp;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.payment.common.utils.DataConvertUtils;
import cn.wisdom.lottery.payment.dao.vo.UserToken;

/**
 * UserAccessTokenDao
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
@Repository
public class UserTokenDaoImpl implements UserTokenDao
{
    @Autowired
    private DaoHelper daoHelper;

    private static final String SQL_GET_USER = "SELECT at_admin_id FROM admin_token "
            + "WHERE at_admin_token = ? LIMIT 1";

    private static final String SQL_INSERT_TOKEN = "INSERT IGNORE INTO admin_token(at_admin_id, at_admin_token, "
            + "at_expire_time, update_time, create_time) "
            + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_TOKEN = "DELETE FROM admin_token WHERE at_admin_token = ?";

    private static final String SQL_DELETE_TOKEN_BY_USER = "DELETE FROM admin_token WHERE at_admin_id = ?";
    
    private static final String SQL_DELETE_EXPIRED_USER_TOKEN = "DELETE FROM admin_token "
            + "WHERE at_expire_time < CURRENT_TIMESTAMP";
    
    private static final String SQL_UPDATE_EXPIRED_TIME = "UPDATE admin_token "
            + "SET at_expire_time = ?, update_time=CURRENT_TIMESTAMP "
            + "WHERE at_admin_token = ?";
    
    // private static final String SQL_GET_TOKENS_BY_USER =
    // "SELECT at_admin_token FROM admin_token WHERE at_admin_id = ?";

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#getUserByToken(java.lang.String)
     */
    public long getUserByToken(final String accessToken)
    {

        // get from db
        String errMsg = MessageFormat.format(
                "Failed to get user by access token [{0}]", accessToken);
        long userId = DataConvertUtils.toLong(daoHelper
                .queryForObject(SQL_GET_USER, Long.class, errMsg, accessToken));

        return userId;
    }
    
    public void updateTokenExpireTime(final String accessToken, Timestamp expireTime)
    {
        // get from db
        String errMsg = MessageFormat.format(
                "Failed to update expire time of access token [{0}]", accessToken);
        this.daoHelper.update(SQL_UPDATE_EXPIRED_TIME, errMsg, expireTime, accessToken);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#save(java.lang.String,
     * java.lang.String)
     */
    public String save(final UserToken accessToken)
    {

        String errMsg = MessageFormat.format(
                "Failed to insert access token [{0}]!", accessToken.getToken());

        daoHelper.save(SQL_INSERT_TOKEN, errMsg, false, accessToken.getUserId(),
                accessToken.getToken(), accessToken.getExpireTime());

        return accessToken.getToken();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#delete(java.lang.String)
     */
    public void delete(final String accessToken)
    {
        String errMsg = MessageFormat
                .format("Failed to delete access token [{0}]", accessToken);
        daoHelper.update(SQL_DELETE_TOKEN, errMsg, accessToken);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#deleteTokensByUser(long)
     */
    public void deleteTokensByUser(final long userId)
    {
        String errMsg = MessageFormat
                .format("Failed to delete access token of user {0}!", userId);

        daoHelper.update(SQL_DELETE_TOKEN_BY_USER, errMsg, userId);
    }
    
    public void deleteExpiredUserToken()
    {
        String errMsg = MessageFormat
                .format("Failed to delete expired access token!", (Object[])null);

        daoHelper.update(SQL_DELETE_EXPIRED_USER_TOKEN, errMsg);
    }

}
