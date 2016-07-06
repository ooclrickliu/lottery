/**
 * UserDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.payment.dao;

import java.util.List;

import cn.wisdom.lottery.payment.dao.vo.User;

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

//    long deleteUser(User user);

    /**
     * Get user by id.
     * 
     * @param userId
     * @return
     */
    User getUser(long userId);
    
    /**
     * Get user by email.
     * 
     * @param email
     * @return
     */
    User getUserByName(String name);
    
    /**
     * Find if the email already exist.
     * 
     * @param email
     * @return
     */
    boolean isNameExist(String name);
    
    /**
     * Update user password.
     * 
     * @param id
     * @param newPassword
     */
    void update(long userId, String newPassword);
    
    /**
     * Delete user by email.
     * 
     * @param email
     */
    void delete(int id);

    /**
     * Get users list.
     * 
     * @param pageInfo
     */
    List<User> getUserList();

    /**
     * Get user permission by id.
     * 
     * @param userId
     */
    
    List<User> getUserByPermission(int permId);
     
}
