/**
 * UserServiceImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.service;

import java.text.MessageFormat;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DataFormatValidator;
import cn.wisdom.lottery.common.utils.EncryptionUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.UserDao;
import cn.wisdom.lottery.dao.constant.RoleType;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.InvalidDataInputException;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.wx.WXService;

/**
 * UserServiceImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private AccessTokenService accessTokenService;
    
    @Autowired
    private UserDao userDao;

	@Autowired
	private WXService wxService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#getUserByAccessToken(java.lang.String)
     */
    public User getUserByAccessToken(String accessToken) throws ServiceException
    {
    	User user = null;
        long userId = accessTokenService.getUserByAccessToken(accessToken);
        logger.debug("Got userId {} by accessToken {}", userId, accessToken);

		if (userId > 0) {
			user = userDao.getUserById(userId);
		}
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#login(java.lang.String,
     * java.lang.String)
     */
    public String login(String phone, String password) throws ServiceException
    {
		
		User user = userDao.getUserByPhone(phone);
		if (user == null)
        {
            throw new ServiceException(ServiceErrorCode.USER_NOT_EXIST,
                    "User is not exist!");
        }
		
		SessionContext.setCurrentUser(user);

        // check password
        if (!StringUtils.equals(user.getPassword(),
                EncryptionUtils.encrypt(password)))
        {
            throw new ServiceException(ServiceErrorCode.WRONG_PASSWORD,
                    "Password is wrong!");
        }
        
        // generate access token
        String accessToken =
                accessTokenService.generateAccessToken(user.getId());
		
		return accessToken;}

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#logout(java.lang.String)
     */
    public void logout(String accessToken) throws ServiceException
    {
        if (StringUtils.isNotBlank(accessToken))
        {
            accessTokenService.invalidToken(accessToken);
        }
    }

    public String changePassword(String oldPassword, String newPassword)
            throws ServiceException
    {
        // check format
        if (!DataFormatValidator.isValidPassword(oldPassword)
                || !DataFormatValidator.isValidPassword(newPassword))
        {
            throw new InvalidDataInputException(
                    ServiceErrorCode.INVALID_PASSWORD_FORMAT,
                    "Password format is invalid!");
        }

        // new password can't be same as old one
        if (StringUtils.equals(oldPassword, newPassword))
        {
            throw new InvalidDataInputException(
                    ServiceErrorCode.SAME_PASSWORD,
                    "New password can't be same as old one!");
        }

        User currentUser = SessionContext.getCurrentUser();

        // check old password
        if (!StringUtils.equals(currentUser.getPassword(),
                EncryptionUtils.encrypt(oldPassword)))
        {
            throw new InvalidDataInputException(
                    ServiceErrorCode.WRONG_PASSWORD, "Old password is wrong!");
        }

        // change password flow
        String accessToken = changePassword(newPassword, currentUser.getId());

        return accessToken;
    }

    private String changePassword(String newPassword, long userId)
            throws ServiceException
    {
        // update
    	userDao.updatePassword(userId, EncryptionUtils.encrypt(newPassword));

        // invalid all old access tokens of user
        accessTokenService.invalidTokensByUser(userId);

        // generate new access token
        String accessToken =
                accessTokenService.generateAccessToken(userId);
        return accessToken;
    }

	@Override
	public void createUser(String openId, RoleType role) throws ServiceException {
		User user = new User();
		
		user.setRole(role);
		user.setOpenid(openId);
		
		userDao.save(user);
	}

	@Override
	public void updateUserWxInfo(WxMpUser wxMpUser) throws ServiceException {
		User user = new User();
		user.setOpenid(wxMpUser.getOpenId());
		user.setNickName(wxMpUser.getNickname());
		user.setHeadImgUrl(wxMpUser.getHeadImgUrl());

		userDao.updateUserWxInfo(user);
	}

	@Override
	public User getUserByOauthCode(String oauthCode) {
		User user = null;
		try {
			WxMpOAuth2AccessToken oauth2getAccessToken = wxService.getWxMpService().oauth2getAccessToken(oauthCode);
			
//			WxMpUser wxMpUser = wxService.getWxMpService().oauth2getUserInfo(oauth2getAccessToken, null);
			WxMpUser wxMpUser = wxService.getWxMpService().userInfo(oauth2getAccessToken.getOpenId(), null);
			
			user = userDao.getUserByOpenid(wxMpUser.getOpenId());
			if (user != null && !(StringUtils.equals(user.getNickName(), wxMpUser.getNickname()) &&
					StringUtils.equals(user.getHeadImgUrl(), wxMpUser.getHeadImgUrl()))) {
				user.setNickName(wxMpUser.getNickname());
				user.setHeadImgUrl(wxMpUser.getHeadImgUrl());
				user.setCountry(wxMpUser.getCountry());
				user.setProvince(wxMpUser.getProvince());
				user.setCity(wxMpUser.getCity());
				user.setSex(wxMpUser.getSex());
				user.setSubscribeTime(wxMpUser.getSubscribeTime());
				user.setUnionid(wxMpUser.getUnionId());
				
				userDao.updateUserWxInfo(user);
			}
		} catch (WxErrorException e) {
			String errMsg = MessageFormat.format("failed pass wx oauth and get user info, code: [{0}]", oauthCode);
			logger.error(errMsg, e);
		}
		return user;
	}

	@Override
	public User getUserByOpenId(String openId) {
		
		return userDao.getUserByOpenid(openId);
	}

	@Override
	public User getUserById(long userId) {
		return userDao.getUserById(userId);
	}

}
