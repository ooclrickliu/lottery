/**
 * UserManagerImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 18, 2015
 */
package cn.wisdom.lottery.payment.service.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.payment.common.utils.EncryptionUtils;
import cn.wisdom.lottery.payment.common.utils.StringUtils;
import cn.wisdom.lottery.payment.dao.UserDao;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

/**
 * UserManagerImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class UserManagerImpl implements UserManager
{
    @Autowired
    private UserDao userDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.manager.UserManager#getUserById(java.lang.String)
     */
    public User getUserById(long id) throws ServiceException
    {
        User user = null;
        try
        {
            user = userDao.getUser(id);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_USER_FAILED,
                    "Failed to find user by id!", e);
        }

        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.manager.UserManager#getUserByEmail(java.lang.String)
     */
    public User getUserByName(String name) throws ServiceException
    {
        User user = null;
        try
        {
            user = userDao.getUserByName(name);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_USER_FAILED,
                    "Failed to find user by name!", e);
        }
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.manager.UserManager#checkEmail(java.lang.String)
     */
    @Override
    public boolean checkName(String name) throws ServiceException
    {
        if (StringUtils.isBlank(name))
        {
            return false;
        }

        boolean exist = false;
        try
        {
            exist = userDao.isNameExist(name);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.CHECK_NAME_FAILED,
                    "Failed to find name!", e);
        }
        if (exist)
        {
            throw new ServiceException(ServiceErrorCode.DUPLICATE_USER_NAME,
                    "Name has been added!");
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.manager.UserManager#changePassword(java.lang.String,
     * long)
     */
    public void changePassword(long userId, String newPassword)
            throws ServiceException
    {
        try
        {
            userDao.update(userId, EncryptionUtils.encrypt(newPassword));
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.CHANGE_PASSWORD_FAILED,
                    "Failed to update user password!", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.manager.UserManager#getUserList(com.ovt.dao.vo.PageInfo)
     */
    public List<User> getUserList() throws ServiceException
    {
        List<User> userList = null;
        try
        {
            userList = userDao.getUserList();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.LIST_USER_FAILED,
                    "Failed to query users list!", e);
        }

        return userList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.manager.UserManager#createUser()
     */
    public User createUser(String name, String password) throws ServiceException
    {
        // create new user
        User user = new User();
        name = StringUtils.trim(name);
        password = StringUtils.trim(password);
        user.setUpdateBy((int) SessionContext.getCurrentUser().getId());

        try
        {
            long id = userDao.save(user);
            user.setId(id);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.CREATE_USER_FAILED,
                    "Failed to save user!", e);
        }

        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.manager.UserManager#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(int id) throws ServiceException
    {
        try
        {
            userDao.delete(id);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.DELETE_USER_FAILED,
                    "Failed to delete user!", e);
        }
    }

    @Override
    public List<User> getUserByPermission(int permId) throws ServiceException
    {
        List<User> users = null;
        try
        {
            users = userDao.getUserByPermission(permId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.LIST_USER_FAILED,
                    "Failed to get user permission!", e);
        }
        return users;
    }

}
