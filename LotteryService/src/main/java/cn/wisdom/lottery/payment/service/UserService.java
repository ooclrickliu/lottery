/**
 * UserService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.service;

import java.util.List;

import me.chanjar.weixin.mp.bean.result.WxMpUser;
import cn.wisdom.lottery.payment.dao.vo.PermissionGrant;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

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
	 * @throws ServiceException
	 */
	void createUser(String openId) throws ServiceException;
	
	/**
	 * 更新用户基本信息
	 * 
	 * @param wxMpUser
	 * @throws ServiceException
	 */
	void updateUserInfo(WxMpUser wxMpUser) throws ServiceException;

	/**
	 * 查询用户
	 * 
	 * @param openId
	 * @return
	 */
	User getUserByOpenId(String openId);
	
	/////////////////////////////////////////////
	
    /**
     * check email format and unique.
     * 
     * @param email
     * @return
     * @throws ServiceException
     */
    boolean checkName(String name) throws ServiceException;

    /**
     * Get user by access token.
     * 
     * @param accessToken
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
    
    /**
     * Query user list.
     * 
     * @param pageInfo
     */
    List<User> getUserList() throws ServiceException;

    /**
     * Create admin.
     * 
     * @param email
     * @param password
     * @return
     */
    User createUser(String email, String password) throws ServiceException;

    /**
     * Delete user.
     * 
     * @param email
     * @throws ServiceException
     */
    void deleteUser(int id) throws ServiceException;

    List<PermissionGrant> getPermissionByUserId(int userId) throws ServiceException;
    
    void cleanExpiredUserToken() throws ServiceException;

    
}
