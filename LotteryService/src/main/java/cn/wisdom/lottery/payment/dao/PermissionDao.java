package cn.wisdom.lottery.payment.dao;

import java.util.List;
import java.util.Map.Entry;

import cn.wisdom.lottery.payment.dao.vo.Permission;

public interface PermissionDao
{
    List<Permission> getAllPermission();
    
    List<Permission> getPermissionByUserId(int userId);
    
    List<Permission> getUnPermissionByUserId(int userId);
    
    List<Entry<String, String>> getIncludePermissionUrlMap();
    
    List<String> getExcludePermissionUrlList();
}