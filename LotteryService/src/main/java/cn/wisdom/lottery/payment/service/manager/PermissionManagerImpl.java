package cn.wisdom.lottery.payment.service.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.payment.dao.PermissionDao;
import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

@Service
public class PermissionManagerImpl implements PermissionManager
{
    @Autowired
    private PermissionDao permissionDao;
    
    public List<Permission> getAllPermission() throws ServiceException
    {
        
        List<Permission> permissions = null;
        try
        {
            permissions = permissionDao.getAllPermission();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.LIST_PERMISSION_FAILED,
                    "Failed to query permissions list!", e);
        }
        
        return permissions;
        
    } 
    
    public List<Permission> getPermissionByUserId(int userId) throws ServiceException
    {
        List<Permission> permissions = null;
        try
        {
            permissions = permissionDao.getPermissionByUserId(userId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.LIST_PERMISSION_FAILED,
                    "Failed to query permissions list with id " + userId + " !", e);
        }
        return permissions;
    }

    @Override
    public List<Permission> getUnPermissionByUserId(int userId) throws ServiceException
    {
        List<Permission> permissions = null;
        try
        {
            permissions = permissionDao.getUnPermissionByUserId(userId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.LIST_PERMISSION_FAILED,
                    "Failed to query unpermissions list with id " + userId + " !", e);
        }
        return permissions;
    }

}
