package cn.wisdom.lottery.payment.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.dao.vo.UserPermission;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.manager.PermissionManager;
import cn.wisdom.lottery.payment.service.manager.UserManager;
import cn.wisdom.lottery.payment.service.manager.UserPermissionManager;

@Service
public class PermissionServiceImpl implements PermissionService
{
    
    @Autowired
    private PermissionManager permissionManager;
    
    @Autowired
    private UserPermissionManager userPermissionManager;
    @Autowired
    private UserManager userManager;
    
    @Override
    public List<User> getUserByPermissionId(int permId)
            throws ServiceException
    {
        List<User> user = userManager.getUserByPermission(permId);
        
        return user;
    }
    
    @Override
    public void grantPermission(int userId, int permId) throws ServiceException
    {
        Timestamp stamp = new Timestamp((new Date()).getTime());
        UserPermission userPermission = new UserPermission();
        userPermission.setUserId(userId);
        userPermission.setPermId(permId);
        userPermission.setCreateTime(stamp);
        userPermission.setUpdateTime(stamp);
        userPermission.setUpdateBy((int) SessionContext.getCurrentUser().getId());
        userPermissionManager.saveUserPermission(userPermission);
    }
    
    @Override
    public void revokePermission(int userId, int permId) throws ServiceException
    {
        userPermissionManager.deleteUserPermission(userId, permId);
    }
    
    @Override
    public List<Permission> getPermissionList()
            throws ServiceException
    {
        List<Permission> permissions = permissionManager.getAllPermission();        
        return permissions;
    }
    
    
}
