/**
 * UserService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.service;

import java.util.List;

import me.chanjar.weixin.mp.bean.result.WxMpUser;
import cn.wisdom.lottery.dao.constant.RoleType;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.exception.ServiceException;

/**
 * UserService
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Service] 1.0
 */
public interface UserService
{
	/**
	 * 保存新关注用户
	 * 
	 * @param openId
	 * @param role TODO
	 * @throws ServiceException
	 */
	void createUser(String openId, RoleType role) throws ServiceException;
	
	/**
	 * 更新用户基本信息, 基本信息通过OAuth2.0获得
	 * 
	 * @param wxMpUser
	 * @throws ServiceException
	 */
	void updateUserWxInfo(WxMpUser wxMpUser) throws ServiceException;

	/**
	 * 查询用户
	 * 
	 * @param openId
	 * @return
	 */
	User getUserByOpenId(String openId);
	
	/**
	 * 通过OAuth获取已关注公众号用户信息
	 * 
	 * @param oauthCode
	 * @return
	 */
	User getSubscribedUserByOauthCode(String oauthCode);
	
	/**
	 * 通过OAuth获取未关注公众号用户信息
	 * 
	 * @param oauthCode
	 * @return
	 */
	User getOuterUserByOauthCode(String oauthCode);

	/**
	 * 通过ID获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	User getUserById(long userId);

	List<User> getUserByIdList(List<Long> userIds);
	
	/**
	 * 通过AccessToken获取用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws ServiceException 
	 */
	User getUserByAccessToken(String accessToken) throws ServiceException;

    /**
     * Login.
     * 
     * @param email
     * @param password
     * @return accessToken
     * @throws ServiceException
     */
    String login(String email, String password) throws ServiceException;

    /**
     * Logout.
     * 
     * @param accessToken
     * @throws ServiceException
     */
    void logout(String accessToken) throws ServiceException;

    /**
     * Change password by user self.
     * 
     * @param oldPassword
     * @param newPassword
     */
    String changePassword(String oldPassword, String newPassword)
            throws ServiceException;

    
}
