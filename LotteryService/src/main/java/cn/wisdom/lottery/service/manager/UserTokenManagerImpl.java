/**
 * UserAccessTokenManagerImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.service.manager;

import java.sql.Timestamp;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.EncryptionUtils;
import cn.wisdom.lottery.dao.UserTokenDao;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.UserToken;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;

/**
 * UserAccessTokenManagerImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class UserTokenManagerImpl implements UserTokenManager
{
    @Autowired
    private UserTokenDao userAccessTokenDao;

    @Autowired
    private AppProperty appProperties;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.UserAccessTokenManager#getUserByAccessToken(java.lang
     * .String)
     */
    public long getUserByAccessToken(String accessToken)
            throws ServiceException
    {
        long userId = 0;
        
        Timestamp expireTime = new Timestamp(DateTimeUtils.addSeconds(
                new java.util.Date(), appProperties.cookieAccessTokenAge)
                .getTime());
        try
        {
            userAccessTokenDao.updateTokenExpireTime(accessToken, expireTime);
            userId = userAccessTokenDao.getUserByToken(accessToken);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_USER_BY_TOKEN_FAILED,
                    MessageFormat.format(
                            "Failed to get user by access token - [{0}]!",
                            accessToken), e);
        }

        return userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.UserAccessTokenManager#generateAccessToken(java.lang.
     * String)
     */
    public String generateAccessToken(long userId) throws ServiceException
    {
        String token = EncryptionUtils.generateUUID();

        UserToken accessToken = new UserToken();
        accessToken.setToken(token);
        accessToken.setUserId(userId);
        accessToken.setExpireTime(new Timestamp(DateTimeUtils.addSeconds(
                new java.util.Date(), appProperties.cookieAccessTokenAge)
                .getTime()));
        
        try
        {
            userAccessTokenDao.save(accessToken);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GENERATE_USER_TOKEN_FAILED,
                    MessageFormat.format("Failed save access token - [{0}]!",
                            accessToken), e);
        }

        return token;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.manager.UserAccessTokenManager#invalidToken(java.lang
     * .String)
     */
    public void invalidToken(String accessToken) throws ServiceException
    {
        try
        {
            userAccessTokenDao.delete(accessToken);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.INVALIDE_USER_TOKEN_FAILED,
                    MessageFormat.format("Failed delete access token - [{0}]!",
                            accessToken), e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.manager.UserAccessTokenManager#invalidTokensByUser(long)
     */
    public void invalidTokensByUser(long userId) throws ServiceException
    {
        try
        {
            userAccessTokenDao.deleteTokensByUser(userId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.INVALIDE_USER_TOKEN_FAILED,
                    MessageFormat.format(
                            "Failed delete access tokens of user - [{0}]!",
                            userId), e);
        }
    }
    
    public void cleanExpiredUserToken() throws ServiceException
    {
        try
        {
            userAccessTokenDao.deleteExpiredUserToken();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.CLEAN_EXPIRED_TOKEN_FAILED,
                            "Failed delete expired user tokens!");
        }
    }

}
