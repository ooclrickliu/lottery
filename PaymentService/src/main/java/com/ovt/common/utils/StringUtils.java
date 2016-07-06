/**
 * StringUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package com.ovt.common.utils;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * StringUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class StringUtils
{
    public static final String CSV_SEPARATOR = ",";

    public static final String SLASH = "/";

    public static final String UNDER_LINE = "_";

    public static final String QUESTION = "?";

    public static final String AND = "&";

    public static final String EQUAL = "=";

    public static final String NEW_LINE = "\n";

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String DOLLAR = "$";

    public static final String BLANK = "";

    public static final String HASH = "#";

    public static final String DOUBLE_SLASH = "\\";

    public static final String CARET = "^";

    public static final String SUCCESS = "SUCCESS";

    public static final String QUOTE = "\"";

    public static final String SINGLE_QUOTE = "\'";

    public static final String SQL_AND = " AND ";

    public static final String SQL_EQUAL = " = ";

    public static boolean isBlank(Object str)
    {
        return str == null || str.toString().trim().length() == 0;
    }

    public static boolean isNotBlank(Object str)
    {
        return !isBlank(str);
    }

    public static boolean isNotEmpty(String[] str)
    {
        return (str != null) && (str.length > 0);
    }

    public static String trim(String str)
    {
        return str == null ? null : str.trim();
    }

    public static boolean equals(String str1, String str2)
    {
        return str1 != null && str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2)
    {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * get CSV string from the collections. use comma as separator. blank items
     * will be skipped.
     * 
     * @param values Collection
     * @return StringBuffer
     */
    public static String getCSV(Collection<?> values)
    {
        return getCSV(values, CSV_SEPARATOR, true);
    }

    public static String getCSV(Collection<?> values, boolean warpSingleQutoa)
    {
        return getCSV(values, CSV_SEPARATOR, true, warpSingleQutoa).toString();
    }

    /**
     * convert the collection into CSV.
     * 
     * @param values
     * @param separators
     * @param ignoreBlank if true blank entries will be skipped
     * 
     * @return StringBuffer
     */
    public static String getCSV(Collection<?> values, String separators,
            boolean ignoreBlank)
    {
        return getCSV(values, separators, ignoreBlank, false).toString();
    }

    private static StringBuffer getCSV(Collection<?> values, String separators,
            boolean ignoreBlank, boolean warpSingleQutoa)
    {
        StringBuffer sb = new StringBuffer();
        if (values == null || values.size() == 0)
        {
            return sb;
        }

        for (Iterator<?> it = values.iterator(); it.hasNext();)
        {
            String value = it.next().toString();
            if (ignoreBlank && isBlank(value))
            {
                continue;
            }

            if (sb.length() > 0)
            {
                sb.append(separators);
            }
            sb.append(warpSingleQutoa ? addSingleQuote(value) : value);
        }

        return sb;
    }

    private static String addSingleQuote(String strSource)
    {
        if (strSource == null)
        {
            strSource = "";
        }
        return "'" + strSource + "'";
    }

    public static String formatNumber(double num, int maxFractionDigits)
    {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(maxFractionDigits);
        nf.setGroupingUsed(false);
        return nf.format(num);
    }

    /**
     * convert '2016-03-05' to web time format '2016/3/5'.
     * 
     * @param timeTitle
     * @return
     */
    public static String toWebTimeFormat(String timeTitle)
    {
        StringBuffer webTimeTitle = new StringBuffer();
        String[] timePart = timeTitle.split("-");
        webTimeTitle.append(timePart[0]);
        for (int i = 1; i < timePart.length; i++)
        {
            webTimeTitle.append("/");
            if (timePart[i].indexOf("0") == 0)
            {
                webTimeTitle.append(timePart[i].replace("0", ""));
            }
            else
            {
                webTimeTitle.append(timePart[i]);
            }
        }
        return webTimeTitle.toString();
    }
}
