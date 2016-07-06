/**
 * UserServiceImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.service;

import java.util.ArrayList;
import java.util.List;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.DataFormatValidator;
import cn.wisdom.lottery.payment.common.utils.EncryptionUtils;
import cn.wisdom.lottery.payment.common.utils.StringUtils;
import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.dao.vo.PermissionGrant;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.InvalidDataInputException;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.manager.PermissionManager;
import cn.wisdom.lottery.payment.service.manager.UserManager;
import cn.wisdom.lottery.payment.service.manager.UserPermissionManager;
import cn.wisdom.lottery.payment.service.manager.UserTokenManager;

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
    private UserTokenManager userAccessTokenManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private UserPermissionManager userPermissionManager;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#getUserByAccessToken(java.lang.String)
     */
    public User getUserByAccessToken(String accessToken) throws ServiceException
    {
        long userId = userAccessTokenManager.getUserByAccessToken(accessToken);
        logger.debug("Got userId {} by accessToken {}", userId, accessToken);

        User user = userManager.getUserById(userId);
        if (user == null)
        {
            throw new ServiceException(ServiceErrorCode.INVALID_ACCESS,
                    "Failed to find user by userId!");
        }
        else
        {
            List<Permission> permission = permissionManager.getPermissionByUserId((int) user
                    .getId());
            user.setPermission(permission);
        }

        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#checkEmail(java.lang.String)
     */
    public boolean checkName(String name) throws ServiceException
    {
        return checkNameUnique(name);
    }

    /**
     * validate email and password format.
     * 
     * @throws InvalidDataInputException
     */
    // private void checkFormat(String email, String password)
    // throws ServiceException
    // {
    // checkNameFormat(email);
    //
    // checkPasswordFormat(password);
    // }

    /**
     * check password format.
     * 
     * @param password
     * @throws InvalidDataInputException
     */
    private void checkPasswordFormat(String password) throws InvalidDataInputException
    {
        if (!DataFormatValidator.isValidPassword(password))
        {
            throw new InvalidDataInputException(ServiceErrorCode.INVALID_PASSWORD,
                    "Password format is invalid!");
        }
    }

    /**
     * check if email is unique.
     * 
     * @param email
     * @return
     * @throws ServiceException
     */
    private boolean checkNameUnique(String name) throws ServiceException
    {
        return userManager.checkName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#login(java.lang.String,
     * java.lang.String)
     */
    public String login(String name, String password) throws ServiceException
    {
        // check args' format
        // checkFormat(name, password);
        User user = userManager.getUserByName(name);
        if (user == null)
        {
            throw new ServiceException(ServiceErrorCode.USER_NOT_EXIST, "User is not exist!");
        }

        // check password
        if (!StringUtils.equals(user.getPassword(), EncryptionUtils.encrypt(password)))
        {
            throw new ServiceException(ServiceErrorCode.WRONG_PASSWORD, "Password is wrong!");
        }

        List<Permission> permission = permissionManager.getPermissionByUserId((int) user.getId());
        user.setPermission(permission);
        // generate access token
        String accessToken = userAccessTokenManager.generateAccessToken(user.getId());
        // put user into session context
        SessionContext.setCurrentUser(user);

        return accessToken;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#logout(java.lang.String)
     */
    public void logout(String accessToken) throws ServiceException
    {
        if (StringUtils.isNotBlank(accessToken))
        {
            userAccessTokenManager.invalidToken(accessToken);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#changePassword(java.lang.String,
     * java.lang.String)
     */
    public String changePassword(String oldPassword, String newPassword) throws ServiceException
    {
        // check format
        if (!DataFormatValidator.isValidPassword(oldPassword)
                || !DataFormatValidator.isValidPassword(newPassword))
        {
            throw new InvalidDataInputException(ServiceErrorCode.INVALID_PASSWORD,
                    "Password format is invalid!");
        }

        // new password can't be same as old one
        if (StringUtils.equals(oldPassword, newPassword))
        {
            throw new InvalidDataInputException(ServiceErrorCode.SAME_NEW_OLD_PASSWORD,
                    "New password can't be same as old one!");
        }

        User currentUser = SessionContext.getCurrentUser();

        // check old password
        if (!StringUtils.equals(currentUser.getPassword(), EncryptionUtils.encrypt(oldPassword)))
        {
            throw new InvalidDataInputException(ServiceErrorCode.WRONG_PASSWORD,
                    "Old password is wrong!");
        }

        // change password flow
        String accessToken = changePassword(newPassword, currentUser.getId());

        return accessToken;
    }

    /**
     * change password flow.
     * 
     * @param newPassword
     * @param currentUser
     * @return
     * @throws ServiceException
     */
    private String changePassword(String newPassword, long currentUserId) throws ServiceException
    {
        // update
        userManager.changePassword(currentUserId, newPassword);

        // invalid all old access tokens of user
        userAccessTokenManager.invalidTokensByUser(currentUserId);

        // generate new access token
        String accessToken = userAccessTokenManager.generateAccessToken(currentUserId);
        return accessToken;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#getUserList(com.ovt.dao.vo.PageInfo)
     */
    public List<User> getUserList() throws ServiceException
    {
        return userManager.getUserList();
    }

    /**
     * create a new user.
     * 
     * @param email
     * @param password
     * @return
     * @throws ServiceException
     * @throws InvalidDataInputException
     */
    public User createUser(String name, String password) throws ServiceException
    {
        // do check
        checkName(name);
        checkPasswordFormat(password);

        // create new user
        return userManager.createUser(name, password);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.UserService#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(int id) throws ServiceException
    {
        // just need check email format
        // checkNameFormat(name);
        List<Permission> permissions = permissionManager.getPermissionByUserId(id);
        for (int i = 0; i < permissions.size(); i++)
        {
            Permission permission = permissions.get(i);
            if (permission.isSA())
            {
                throw new ServiceException(ServiceErrorCode.CAN_NOT_DELETE_SA,
                        "Can't delete super admin");
            }
        }
        userPermissionManager.deleteByUserId(id);
        userManager.deleteUser(id);
    }

    @Override
    public List<PermissionGrant> getPermissionByUserId(int userId) throws ServiceException
    {
        List<PermissionGrant> permissiongrants = new ArrayList<PermissionGrant>();

        List<Permission> permissions1 = permissionManager.getPermissionByUserId(userId);
        List<Permission> permissions2 = permissionManager.getUnPermissionByUserId(userId);

        for (int i = 0; i < permissions2.size(); i++)
        {
            PermissionGrant permissiongrant = new PermissionGrant();
            Permission permission = permissions2.get(i);
            if (!permission.isSA())
            {
                permissiongrant.setPermission(permission);
                permissiongrant.setState("UNGRANT");
                permissiongrants.add(permissiongrant);
            }

        }

        for (int i = 0; i < permissions1.size(); i++)
        {
            PermissionGrant permissiongrant = new PermissionGrant();
            Permission permission = permissions1.get(i);
            if (!permission.isSA())
            {
                permissiongrant.setPermission(permission);
                permissiongrant.setState("GRANTED");
                permissiongrants.add(permissiongrant);
            }
        }

        return permissiongrants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.wisdom.lottery.payment.service.UserService#cleanExpiredUserToken()
     */
    @Override
    public void cleanExpiredUserToken() throws ServiceException
    {
        logger.info("<Manual trigger> Check expire user token task start!");
        userAccessTokenManager.cleanExpiredUserToken();
        logger.info("<Manual trigger> Check expire user token task complete!");
    }

	@Override
	public void createUser(String openId) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserInfo(WxMpUser wxMpUser) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		return null;
	}

}
