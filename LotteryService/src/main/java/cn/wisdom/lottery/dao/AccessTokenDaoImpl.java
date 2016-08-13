/**
 * UserAccessTokenDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.dao;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.dao.vo.AccessToken;

/**
 * UserAccessTokenDao
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
@Repository
public class AccessTokenDaoImpl implements AccessTokenDao
{
    @Autowired
    private DaoHelper daoHelper;

    private static final String SQL_GET_USER =
            "SELECT user_id FROM access_token WHERE access_token = ? and is_delete = 0 LIMIT 1";

    private static final String SQL_INSERT_TOKEN =
            "INSERT IGNORE INTO access_token(user_id, access_token, "
                    + "client_type, expire_time, update_time) "
                    + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_TOKEN =
            "delete from access_token WHERE access_token = ?";

    private static final String SQL_DELETE_TOKEN_BY_USER =
            "delete from access_token WHERE user_id = ?";

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#getUserByToken(java.lang.String)
     */
    public long getUserByToken(final String accessToken)
    {
        // get from db
        String errMsg =
                MessageFormat
                        .format("Failed to get user by access token [{0}]",
                                accessToken);
        long userId =
                DataConvertUtils.toLong(daoHelper.queryForObject(SQL_GET_USER,
                        Long.class, errMsg, accessToken));

        return userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#save(java.lang.String,
     * java.lang.String)
     */
    public String save(AccessToken accessToken)
    {
        String errMsg =
                MessageFormat.format("Failed to insert access token [{0}]!",
                        accessToken.getAccessToken());

        daoHelper.save(SQL_INSERT_TOKEN, errMsg, false,
                accessToken.getUserId(), accessToken.getAccessToken(),
                accessToken.getClientType(), accessToken.getExpireTime());

        return accessToken.getAccessToken();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#delete(java.lang.String)
     */
    public void delete(final String accessToken)
    {
        String errMsg =
                MessageFormat.format("Failed to delete access token [{0}]",
                        accessToken);
        daoHelper.update(SQL_DELETE_TOKEN, errMsg, accessToken);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserAccessTokenDao#deleteTokensByUser(long)
     */
    public void deleteTokensByUser(final long userId)
    {
        // get all tokens
        String errMsg =
                MessageFormat.format(
                        "Failed to delete access token of user {0}!", userId);

        daoHelper.update(SQL_DELETE_TOKEN_BY_USER, errMsg, userId);
    }

}
