/**
 * CookieUtil.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 8, 2015
 */
package cn.wisdom.lottery.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CookieUtil
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class CookieUtil
{

    public static final String KEY_ACCESS_TOKEN = "access_token";
    
    public static final String OAUTH_CODE = "code";
    
    public static final String OPENID = "openid";

    public static final int ONE_HOUR = 3600;

    /**
     * Add a new cookie and sepcify the age.
     * 
     * @param response
     * @param name
     * @param value 10.0.2.15
     * @param age
     */
    public static void addCookie(HttpServletResponse response, String name,
            String value, int age)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(age);
        cookie.setVersion(0);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    /**
     * Add a new cookie and will be deleted when browser exits.
     * 
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name,
            String value)
    {
        addCookie(response, name, value, -1);
    }

    /**
     * Get cookie value.
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request, String name)
    {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals(name))
                {
                    try
                    {
                        value = URLDecoder.decode(cookie.getValue(), "utf-8");
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return value;
    }
}
