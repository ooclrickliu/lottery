package cn.wisdom.lottery.payment.service.manager;

import cn.wisdom.lottery.payment.dao.vo.UserPermission;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

public interface UserPermissionManager
{

    public void saveUserPermission(UserPermission userPermission)
            throws ServiceException;

    public void deleteUserPermission(int userId, int permId)
            throws ServiceException;

    public void deleteByUserId(int userId) throws ServiceException;
}
