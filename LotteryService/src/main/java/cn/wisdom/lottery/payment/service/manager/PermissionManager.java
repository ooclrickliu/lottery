package cn.wisdom.lottery.payment.service.manager;

import java.util.List;

import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

public interface PermissionManager
{
    public List<Permission> getAllPermission() throws ServiceException;
    
    public List<Permission> getPermissionByUserId(int userId) throws ServiceException;
    
    public List<Permission> getUnPermissionByUserId(int userId) throws ServiceException;
}
