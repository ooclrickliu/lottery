/**
 * UserAccessTokenDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.dao;

import cn.wisdom.lottery.dao.vo.AccessToken;



/**
 * UserAccessTokenDao
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public interface AccessTokenDao
{

    /**
     * Get user by access token.
     * 
     * @param accessToken
     * @return
     */
    long getUserByToken(String accessToken);
    
    /**
     * create new accessToken.
     * 
     * @param accessToken
     * @return
     */
    String save(AccessToken accessToken);

    /**
     * delete accessToken.
     * 
     * @param accessToken
     */
    void delete(String accessToken);

    /**
     * delete all access tokens of user.
     * 
     * @param userId
     */
    void deleteTokensByUser(long userId);
}
