package cn.wisdom.lottery.payment.service.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wisdom.lottery.payment.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.payment.dao.UserPermissionDao;
import cn.wisdom.lottery.payment.dao.vo.UserPermission;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

@Service
public class UserPermissionManagerImpl implements UserPermissionManager
{
    @Autowired
    private UserPermissionDao userPermissionDao;

    public void saveUserPermission(UserPermission userPermission)
            throws ServiceException
    {
        try
        {
            this.userPermissionDao.save(userPermission);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GRANT_PERMISSION_FAILED,
                    "Failed to save user permission!", e);
        }
    }

    public void deleteUserPermission(int userId, int permId)
            throws ServiceException
    {
        try
        {
            this.userPermissionDao.delete(userId, permId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.REVOKE_PERMISSION_FAILED,
                    "Failed to delete user permission!", e);
        }
    }

    public void deleteByUserId(int userId) throws ServiceException
    {
        try
        {
            this.userPermissionDao.delete(userId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.REVOKE_PERMISSION_FAILED,
                    "Failed to delete user permission!", e);
        }
    }

}
