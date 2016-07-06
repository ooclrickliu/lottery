/**
 * HttpUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 25, 2015
 */
package com.ovt.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class HttpUtils
{

    /**
     * Convert parameters map to http query string, eg. a=123&b=china&c=1,10,30
     * 
     * @param parameterMap
     * @return
     */
    public static String buildQueryString(Map<String, String> parameterMap)
    {
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(parameterMap))
        {
            for (String name : parameterMap.keySet())
            {
                builder.append(name)
                        .append(StringUtils.EQUAL)
                        .append(DataConvertUtils.toString(parameterMap.get(name)))
                        .append(StringUtils.AND);
            }
            
            if (CollectionUtils.isNotEmpty(parameterMap))
            {
                builder.delete(builder.lastIndexOf(StringUtils.AND), builder.length());
            }
        }
        return builder.toString();
    }
    
    /**
     * Convert a query string, eg. ?a=123&b=china&c=1,10,30,  to parameter map.
     * 
     * @param queryStr
     * @return
     */
    public static Map<String, String> buildQueryMap(String queryStr)
    {
        Map<String, String> parameterMap = new HashMap<String, String>();
        if (StringUtils.isBlank(queryStr))
        {
            return parameterMap;
        }
        
        String validStr = queryStr;
        if (validStr.trim().startsWith(StringUtils.QUESTION))
        {
            validStr = validStr.substring(1);
        }
        
        String[] params = validStr.split(StringUtils.AND);
        if (params != null && params.length > 0)
        {
            for (String param : params)
            {
                if (StringUtils.isNotBlank(param))
                {
//                    String[] keyValue = param.split(StringUtils.EQUAL);
//                    if (keyValue != null && keyValue.length == 2)
//                    {
//                        parameterMap.put(keyValue[0], keyValue[1]);
//                    }
                    
                    int eq = param.indexOf(StringUtils.EQUAL);
                    if (eq > 0)
                    {
                        String key =  param.substring(0, eq);
                        
                        if (eq < param.length())
                        {
                            String value = param.substring(eq + 1);
                            parameterMap.put(key, value);
                        }
                    }
                    
                }
            }
        }
        
        return parameterMap;
    }
}
