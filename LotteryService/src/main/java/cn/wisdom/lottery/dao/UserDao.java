/**
 * UserDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.dao;

import java.util.List;

import cn.wisdom.lottery.dao.vo.User;

/**
 * UserDao
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public interface UserDao
{
    /**
     * Add new user.
     * 
     * @param user
     * @return
     */
    long save(User user);

    long saveWithWxInfo(User user);

    /**
     * Get user by id.
     * 
     * @param userId
     * @return
     */
	User getUserById(long userId);

    /**
     * Get user by openid.
     * 
     * @param openId
     * @return
     */
	User getUserByOpenid(String openId);

	/**
	 * Update user wx info.
	 * 
	 * @param user
	 */
	void updateUserWxInfo(User user);

	User getUserByPhone(String phone);

	void updatePassword(long userId, String encrypt);

	void updateSubscribeState(User user);

	List<User> getUserByIdList(List<Long> userIds);
     
}
