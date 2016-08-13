/**
 * UserAccessTokenManagerImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.service;

import java.sql.Date;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.EncryptionUtils;
import cn.wisdom.lottery.dao.AccessTokenDao;
import cn.wisdom.lottery.dao.vo.AccessToken;
import cn.wisdom.lottery.dao.vo.AppProperty;
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
public class AccessTokenServiceImpl implements AccessTokenService
{
    @Autowired
    private AccessTokenDao userAccessTokenDao;

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
        try
        {
            userId = userAccessTokenDao.getUserByToken(accessToken);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.INVALID_ACCESS_TOKEN,
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

        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(token);
        accessToken.setUserId(userId);
        accessToken.setClientType("Web");
        accessToken.setExpireTime(new Date(DateTimeUtils.addHours(
                new java.util.Date(), appProperties.cookieAccessTokenHourAge)
                .getTime()));

        try
        {
            userAccessTokenDao.save(accessToken);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.SYSTEM_UNEXPECTED,
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
            throw new ServiceException(ServiceErrorCode.SYSTEM_UNEXPECTED,
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
            throw new ServiceException(ServiceErrorCode.SYSTEM_UNEXPECTED,
                    MessageFormat.format(
                            "Failed delete access tokens of user - [{0}]!",
                            userId), e);
        }
    }

}
