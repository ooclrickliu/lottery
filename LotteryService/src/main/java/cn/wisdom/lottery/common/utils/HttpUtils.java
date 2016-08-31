/**
 * HttpUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 25, 2015
 */
package cn.wisdom.lottery.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.wisdom.lottery.dao.constant.QueryDirection;
import cn.wisdom.lottery.dao.vo.PageInfo;

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
    
    public static String getParamValue(HttpServletRequest request, String paramName)
    {
        String parmaValue = request.getHeader(paramName);

        if (StringUtils.isBlank(parmaValue))
        {
            parmaValue = request.getParameter(paramName);

            if (StringUtils.isBlank(parmaValue))
            {
                parmaValue = CookieUtil.getCookie(request, paramName);
            }
        }
        
        return parmaValue;
    }
    
    public static PageInfo getPageInfo(HttpServletRequest request)
    {
    	PageInfo pageInfo = new PageInfo();
    	
    	long start = DataConvertUtils.toLong(getParamValue(request, PageInfo.PARAM_START));
    	if (start <= 0) {
			start = Integer.MAX_VALUE;
		}
    	pageInfo.setStart(start);
    	
    	int count = DataConvertUtils.toInt(getParamValue(request, PageInfo.PARAM_COUNT));
    	if (count <= 0) {
			count = 10;
		}
		pageInfo.setCount(count);
		
    	String direction = getParamValue(request, PageInfo.PARAM_DIRECT);
    	if (StringUtils.isBlank(direction)) {
    		pageInfo.setDirection(QueryDirection.DOWN);
		}
    	else {
    		pageInfo.setDirection(QueryDirection.valueOf(direction));
    	}
    	
    	return pageInfo;
    }
}
