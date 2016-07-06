package cn.wisdom.lottery.payment.dao;

import cn.wisdom.lottery.payment.dao.vo.UserPermission;

public interface UserPermissionDao
{
    public void save(UserPermission userPermission);
    
    public void delete(int userId, int permId);
    
    public void delete(int userId);
    
}
