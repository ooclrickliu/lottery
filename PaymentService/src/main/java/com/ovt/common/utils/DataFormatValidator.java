/**
 * DataFormatValidator.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package com.ovt.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataFormatValidator
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class DataFormatValidator
{

    public static final String REGEXP_WEBSITE_PATTERN =
            "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";

    private static final Pattern REGEXP_WEBSITE_PATTERN_OBJ = Pattern
            .compile(REGEXP_WEBSITE_PATTERN);
    
    public static final String REGEXP_EMAIL_PATTERN =
            "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    
    private static final Pattern REGEXP_EMAIL_PATTERN_OBJ = Pattern
            .compile(REGEXP_EMAIL_PATTERN);
    
    public static final String REGEXP_PASSWORD_PATTERN =
            "^[a-zA-Z0-9_]{6,20}$";
    
    private static final Pattern REGEXP_PASSWORD_PATTERN_OBJ = Pattern
            .compile(REGEXP_PASSWORD_PATTERN);

    public static boolean isValidEmail(String email)
    {
        Matcher matcher = REGEXP_EMAIL_PATTERN_OBJ.matcher(email);
        return matcher.matches();

//        if (!isStrLengthValid(email, 3, 50))
//        {
//            return false;
//        }
//
//        String m0 = "\\(\\)><@,;:\\\\\\\"\\.\\[\\]";
//        String n0 = "[^\\s" + m0 + "]";
//        String j0 = n0 + "+";
//        String o0 = "(" + j0 + ")";
//        Pattern p2 = Pattern.compile("^" + o0 + "\\.?(" + o0 + "\\.?)*$");
//        Pattern ipPattern =
//                Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
//        Pattern p1 = Pattern.compile("^(.+)@(.+)$");
//
//        Matcher m1 = p1.matcher(email);
//        if (!m1.find())
//        {
//            return false;
//        }
//
//        String id = m1.group(1);
//        String domain = m1.group(2);
//
//        for (int i = 0; i < id.length(); i++)
//        {
//            if (id.charAt(i) > 126 || id.charAt(i) < 32)
//            {
//                return false;
//            }
//        }
//
//        Matcher m2 = p2.matcher(id);
//        if (!m2.find())
//        {
//            return false;
//        }
//
//        for (int i = 0; i < domain.length(); i++)
//        {
//            if (domain.charAt(i) > 126 || domain.charAt(i) < 32)
//            {
//                return false;
//            }
//        }
//
//        Matcher m3 = ipPattern.matcher(domain);
//        if (m3.find())
//        {
//            for (int i = 1; i <= 4; i++)
//            {
//                if (Integer.valueOf(m3.group(i)) > 255)
//                {
//                    return false;
//                }
//            }
//        }
//        String[] domainParts = domain.split("\\.");
//        for (int i = 0; i < domainParts.length; i++)
//        {
//            if (!domainParts[i].matches("^" + j0 + "$"))
//            {
//                return false;
//            }
//        }
//
//        return true;
    }

    /**
     * Nick name should be [0-50].
     * 
     * @param nickName
     * @return
     */
    public static boolean isValidNickName(String nickName)
    {
        return isStrLengthValid(nickName, 0, 50);
    }

    public static boolean isValidPhone(String phone)
    {
        return isStrLengthValid(phone, 0, 20);
    }

    /**
     * Password length should be [6-20].
     * 
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password)
    {
        Matcher matcher = REGEXP_PASSWORD_PATTERN_OBJ.matcher(password);
        return matcher.matches();
    }

    /**
     * company name length should be [0-50].
     * 
     * @param companyName can be null
     * @return
     */
    public static boolean isValidCompanyName(String companyName)
    {
        // 0 <= length <= 50
        return isStrLengthValid(companyName, 0, 50);
    }

    /**
     * website length should be [0-50] and match the pattern.
     * 
     * @param website
     * @return
     */
    public static boolean isValidWebsite(String website)
    {
        if (StringUtils.isBlank(website))
        {
            return true;
        }
        
        // 0 <= length <= 50
        if (!isStrLengthValid(website, 0, 50))
        {
            return false;
        }
        Matcher matcher = REGEXP_WEBSITE_PATTERN_OBJ.matcher(website);
        return matcher.find();
    }

    /**
     * Service instance name length should be [1-50].
     * 
     * @param instanceName can be null
     * @return
     */
    public static boolean isValidInstanceName(String instanceName)
    {
        // 1 <= length <= 50
        return isStrLengthValid(instanceName, 1, 50);
    }

    private static boolean isStrLengthValid(String str, int minLen, int maxLen)
    {
        String validString = DataConvertUtils.toString(str);

        return validString.length() >= minLen && validString.length() <= maxLen;
    }

    /**
     * Access token length should be 32.
     * 
     * @param accessToken
     * @return
     */
    public static boolean isValidAccessToken(String accessToken)
    {
        return isStrLengthValid(accessToken, 32, 32);

    }

    /**
     * Check if request is expired.
     * 
     * @param expires
     * @return
     */
    public static boolean isRequestExpired(String expires)
    {
        long expiresTime = DataConvertUtils.toLong(expires);
        long current = System.currentTimeMillis();

        return current > expiresTime;
    }

    /**
     * Signature length should be 28.
     * 
     * @param signature
     * @return
     */
    public static boolean isValidSignature(String signature)
    {
        return isStrLengthValid(signature, 28, 28);
    }

    /**
     * function name length should be [1-50].
     * 
     * @param functionName
     * @return
     */
    public static boolean isValidFunctionName(String functionName)
    {
        return isStrLengthValid(functionName, 1, 50);
    }
    
    /**
     * service access url length should be [1-50].
     * 
     * @param service access url
     * @return
     */
    public static boolean isValidServiceUrl(String serviceUrl)
    {
        return isStrLengthValid(serviceUrl, 1, 50);
    }
}
