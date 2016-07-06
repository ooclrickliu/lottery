/**
 * UserManager.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 18, 2015
 */
package cn.wisdom.lottery.payment.service.manager;

import java.util.List;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

/**
 * UserManager
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface UserManager
{
    /**
     * Get user by id.
     * 
     * @param id
     * @return
     * @throws ServiceException
     */
    User getUserById(long id) throws ServiceException;

    /**
     * Get user by email.
     * 
     * @param email
     * @return
     * @throws ServiceException
     */
    User getUserByName(String name) throws ServiceException;

    /**
     * Check if email is usable. ture if never exist, or false if registered by
     * sb already.
     * 
     * @param email
     * @return
     * @throws ServiceException
     */
    boolean checkName(String name) throws ServiceException;

    /**
     * Change password.
     * 
     * @param currentUserId
     * @param newPassword
     * @return
     * @throws ServiceException
     */
    void changePassword(long currentUserId, String newPassword)
            throws ServiceException;

    /**
     * Get user list.
     * 
     * @param pageInfo
     * @return
     * @throws ServiceException
     */
    List<User> getUserList() throws ServiceException;

    /**
     * Create a new user.
     * 
     * @param email
     * @param password
     * @param role
     * @param isCompany
     * @param companyName
     * @return
     * @throws ServiceException
     */
    User createUser(String email, String password) throws ServiceException;

    /**
     * Delete user.
     * 
     * @param email
     * @throws ServiceException
     */
    void deleteUser(int id) throws ServiceException;
    
    public List<User> getUserByPermission(int permId) throws ServiceException;

}
