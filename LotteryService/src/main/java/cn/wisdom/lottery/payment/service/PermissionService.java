package cn.wisdom.lottery.payment.service;

import java.util.List;

import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

public interface PermissionService
{
    /**
     * 
     * @param permId
     * @return
     * @throws ServiceException
     */
    public List<User> getUserByPermissionId(int permId) throws ServiceException;

    /**
     * 
     * @return
     * @throws ServiceException
     */
    public List<Permission> getPermissionList() throws ServiceException;

    /**
     * 
     * @param userId
     * @param permId
     * @throws ServiceException
     */
    public void grantPermission(int userId, int permId) throws ServiceException;

    /**
     * 
     * @param userId
     * @param permId
     * @throws ServiceException
     */
    public void revokePermission(int userId, int permId)
            throws ServiceException;
}
