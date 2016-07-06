/**
 * PermissionConstants.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月11日
 */
package cn.wisdom.lottery.payment.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.dao.PermissionDao;

/**
 * PermissionConstants
 * 
 * @Author jinzhong.zheng
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class PermissionGrantService
{
    @Autowired
    private PermissionDao permissionDao;

    private Map<String, String> permissionIncludeMap;
    
    private Set<String> permissionExcludeUrlSet;

    public Map<String, String> getPermissionIncludeMap()
    {
        if (CollectionUtils.isEmpty(permissionIncludeMap))
        {
            initPermissionIncludeMap();
        }

        return permissionIncludeMap;
    }

    public Set<String> getPermissionExcludeUrlSet()
    {
        if (CollectionUtils.isEmpty(permissionExcludeUrlSet))
        {
            initPermissionExcludeUrlSet();
        }

        return permissionExcludeUrlSet;
    }

    private void initPermissionIncludeMap()
    {
        permissionIncludeMap = new HashMap<String, String>();
        List<Entry<String, String>> urlMaps = permissionDao.getIncludePermissionUrlMap();
        if (CollectionUtils.isNotEmpty(urlMaps))
        {
            for (Entry<String, String> entry : urlMaps)
            {
                permissionIncludeMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void initPermissionExcludeUrlSet()
    {
        permissionExcludeUrlSet = new HashSet<String>();
        List<String> resultList = permissionDao.getExcludePermissionUrlList();
        if (CollectionUtils.isNotEmpty(resultList))
        {
            for (String result : resultList)
            {
                permissionExcludeUrlSet.add(result);
            }
        }
    }
}
