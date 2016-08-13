/**
 * DataConvertUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.common.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataConvertUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class DataConvertUtils
{
    // checks that an input string is an integer, with an optional +/- sign
    // character.
    private static final Pattern REGEXP_INTEGER = Pattern.compile(
            "^\\s*(\\+|-)?\\d+\\s*$", Pattern.CASE_INSENSITIVE);

    /**
     * convert a string to short value.
     */
    public static long stringToLong(String strValue)
    {
        if (strValue == null)
        {
            return 0;
        }

        strValue = strValue.trim();
        if (strValue.equals(""))
        {
            return (long) 0;
        }
        else if (strValue.indexOf(".") >= 0)
        {
            return (long) Double.parseDouble(strValue);
        }
        else
        {
            return (long) Long.parseLong(strValue);
        }
    }

    /**
     * convert obj to timestamp.
     * 
     * @param value
     * @return
     */
    public static java.sql.Timestamp toTimestamp(Object value)
    {
        if (value == null)
        {
            return null;
        }
        else if (value instanceof java.sql.Timestamp)
        {
            return (java.sql.Timestamp) value;
        }
        else if (value instanceof java.util.Date)
        {
            return new java.sql.Timestamp(((java.util.Date) value).getTime());
        }
        else if (value instanceof java.lang.String)
        {
            return Timestamp.valueOf(value.toString());
        }
        else
        {
            return null;
        }
    }

    /**
     * Convert object to int.
     * 
     * @param value
     * @return
     */
    public static int toInt(Object value)
    {
        return (int) toLong(value);
    }
    
    public static float toFloat(String value)
    {
        float f = 0;
        if (value != null && value.toString().length() != 0)
        {
            try
            {
                f = Float.parseFloat(value);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        
        return f;
    }

    /**
     * Convert object to long.
     * 
     * @param value
     * @return
     */
    public static long toLong(Object value)
    {
        if (value == null || value.toString().length() == 0)
        {
            return 0;
        }
        if (value instanceof Integer)
        {
            return ((Integer) value).longValue();
        }
        else if (value instanceof Long)
        {
            return ((Long) value).longValue();
        }
        else if (value instanceof Short)
        {
            return ((Short) value).longValue();
        }
        else if (value instanceof Float)
        {
            return ((Float) value).longValue();
        }
        else if (value instanceof Double)
        {
            return ((Double) value).longValue();
        }
        else if (value instanceof Boolean)
        {
            return ((Boolean) value) ? 1 : 0;
        }

        if (isInteger(value.toString()))
        {
            return Long.parseLong(value.toString().trim());
        }

        return 0;
    }

    /**
     * check if the string is a long or integer number.
     * 
     * @param str String
     * @return boolean
     */
    public static boolean isInteger(String str)
    {

        if (StringUtils.isBlank(str))
        {
            return false;
        }

        try
        {
            Matcher matcher = REGEXP_INTEGER.matcher(str);
            return matcher.find();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * get the string value of the value object.
     * 
     * @param value
     * @return String the string value
     */
    public static String toString(Object value)
    {

        if (value instanceof String[])
        {
            StringBuffer sb = new StringBuffer();
            for (String s : (String[]) value)
            {
                if (sb.length() > 0)
                {
                    sb.append(StringUtils.CSV_SEPARATOR);
                }
                sb.append(s);
            }

            return sb.toString();
        }

        if (value instanceof Object[])
        {
            StringBuffer sb = new StringBuffer();
            for (Object s : (Object[]) value)
            {
                if (sb.length() > 0)
                {
                    sb.append(StringUtils.CSV_SEPARATOR);
                }
                sb.append((s == null) ? "" : s.toString());
            }

            return sb.toString();
        }

        if (value instanceof Timestamp)
        {
            return DateTimeUtils.formatSqlDateTime((Date) value);
        }

        if (value instanceof Date)
        {
            return DateTimeUtils.formatSqlDate((Date) value);
        }

        return (value == null) ? "" : value.toString();
    }
}
