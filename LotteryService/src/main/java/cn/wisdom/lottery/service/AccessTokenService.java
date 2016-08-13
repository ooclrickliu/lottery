/**
 * UserAccessTokenManager.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.service;

import cn.wisdom.lottery.service.exception.ServiceException;


/**
 * UserAccessTokenManager
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[Service] 1.0
 */
public interface AccessTokenService
{
    
    /**
     * Get user by access token.
     * 
     * @param accessToken
     * @return
     */
    long getUserByAccessToken(String accessToken) throws ServiceException;
    
    /**
     * Generate access token (32 characters) for user.
     * 
     * @param email
     * @return
     */
    String generateAccessToken(long userId) throws ServiceException;

    /**
     * make the access token invalid.
     * 
     * @param accessToken
     */
    void invalidToken(String accessToken) throws ServiceException;
    
    /**
     * make all access tokens of user invalid.
     * 
     * @param userId
     */
    void invalidTokensByUser(long userId) throws ServiceException;
}
