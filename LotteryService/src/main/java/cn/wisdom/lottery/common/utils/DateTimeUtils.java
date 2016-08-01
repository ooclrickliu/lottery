/**
 * DateTimeUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 11, 2015
 */
package cn.wisdom.lottery.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DateTimeUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class DateTimeUtils
{
    public static final String PATTERN_SQL_DATE = "yyyy-MM-dd";

    public static final String PATTERN_SQL_DATETIME_FULL = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTERN_YEAR = "yyyy";

    public static final String PATTERN_MONTH = "yyyy-MM";

    public static final long LONG_DAY = 1000 * 24 * 60 * 60;

    public static final int DAY_PER_MONTH = 30;

    public static final int MONTH_PER_YEAR = 12;
    
    public static Date parseDate(String value, String pattern)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        SimpleDateFormat formater = new SimpleDateFormat(pattern, Locale.US);
        try
        {
            Date date = formater.parse(value);
            return date;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get the date, ignore the time.
     * 
     * @param date
     * @return
     */
    public static Date getDateWithoutTime(Date date)
    {
        if (date != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            date = calendar.getTime();
        }
        return date;
    }

    public static String formatSqlDate(Date value)
    {
        return formatDate(value, PATTERN_SQL_DATE, null);
    }

    public static String formatSqlDateTime(Date value)
    {
        return formatDate(value, PATTERN_SQL_DATETIME_FULL, null);
    }

    public static Date getYearStart(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date getYearEnd(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static Date getQuarterStart(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        switch (month)
        {
        case 0:
        case 1:
        case 2:
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            break;
        case 3:
        case 4:
        case 5:
            cal.set(Calendar.MONTH, Calendar.APRIL);
            break;
        case 6:
        case 7:
        case 8:
            cal.set(Calendar.MONTH, Calendar.JULY);
            break;
        case 9:
        case 10:
        case 11:
            cal.set(Calendar.MONTH, Calendar.OCTOBER);
            break;
        default:
            break;
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date getQuarterEnd(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        switch (month)
        {
        case 0:
        case 1:
        case 2:
            cal.set(Calendar.MONTH, Calendar.MARCH);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            break;
        case 3:
        case 4:
        case 5:
            cal.set(Calendar.MONTH, Calendar.JUNE);
            cal.set(Calendar.DAY_OF_MONTH, 30);
            break;
        case 6:
        case 7:
        case 8:
            cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 30);
            break;
        case 9:
        case 10:
        case 11:
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            break;
        default:
            break;
        }
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static Date getMonthStart(Date date)
    {
        Date nextMonthStart = getNextMonthStart(date);
        return addMonths(nextMonthStart, -1);
    }

    public static Date getMonthEnd(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Date month = DateTimeUtils.addMonths(cal.getTime(), 1);
		cal.setTime(month);//将日期设置为新的时间cal.get(Calendar.DAY_OF_MONTH)取month所在的天数
        Date day = DateTimeUtils.addDays(month,
                -(cal.get(Calendar.DAY_OF_MONTH)));
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static Date getDayStart(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);

        return cal.getTime();
    }
    
    public static Date getDayEnd(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static Date getNextMonthStart(Date current)
    {
        return DateTimeUtils.addSeconds(getMonthEnd(current), 1);
    }

    /**
     * format the date with the given pattern.
     * 
     * @param value Date
     * @param pattern String the format pattern, for example,
     *            SimpleDate.PATTERN_US_DATE
     * @param tz TimeZone if null,will use the server's default timezone
     * @return String the formated string
     */
    public static String formatDate(Date value, String pattern, TimeZone tz)
    {

        if (value == null)
        {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat(pattern, Locale.US);
        if (tz != null)
        {
            formater.setTimeZone(tz);
        }
        return formater.format(value);
    }

    /**
     * Add year to the date.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addYears(Date date, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, amount);
        return calendar.getTime();
    }

    /**
     * Get year to the date.
     * 
     * @param date
     * @return
     */
    public static int getYear(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Add month to the date.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMonths(Date date, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }

    /**
     * Get month to the date.
     * 
     * @param date
     * @return
     */
    public static int getMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Add day to the date.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addDays(Date date, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return calendar.getTime();
    }

    /**
     * Get day to the date.
     * 
     * @param date
     * @return
     */
    public static int getDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * Get quarter to the date.
     * 
     * @param date
     * @return
     */
    public static int getQuarter(Date date)
    {
        int month = getMonth(date);
        int quarter = -1;
        switch (month)
        {
        case 1:
        case 2:
        case 3:
            quarter = 1;
            break;
        case 4:
        case 5:
        case 6:
            quarter = 2;
            break;
        case 7:
        case 8:
        case 9:
            quarter = 3;
            break;
        case 10:
        case 11:
        case 12:
            quarter = 4;
            break;
        default:
            break;
        }
        return quarter;
    }

    /**
     * Add hour to the date.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addHours(Date date, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, amount);
        return calendar.getTime();
    }

    /**
     * Add minutes to the date.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinutes(Date date, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, amount);
        return calendar.getTime();
    }

    /**
     * Add second to the date.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addSeconds(Date date, int amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, amount);
        return calendar.getTime();
    }

    public static int dateDiff(Date fromDate, Date toDate)
    {
        long diff = toDate.getTime() - fromDate.getTime();

        return (int) (diff / LONG_DAY) + 1;
    }

    public static int monthDiff(Date fromDate, Date toDate)
    {
        long dateDiff = dateDiff(fromDate, toDate);

        int monthDiff = (int) (dateDiff / DAY_PER_MONTH);

        if (addMonths(fromDate, monthDiff).after(toDate))
        {
            monthDiff--;
        }

        return monthDiff;
    }

    public static Timestamp getCurrentTimestamp()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp toTimestamp(Date date)
    {
        return new Timestamp(date.getTime());
    }

    public static Date toDate(Timestamp timestamp)
    {
        return new Date(timestamp.getTime());
    }

    public static java.sql.Date toSQLDate(java.util.Date date)
    {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Judge whether or not that now date after the date.
     * 
     * @param date
     * @return boolean
     */
    public static boolean isPast(Date date)
    {
        return isAfter(new Date(), date);
    }
    
    /**
     * Judge whether or not that comparedDate after baseDate.
     * 
     * @param comparedDate
     * @param baseDate
     * @return if comparedDate after baseDate, then return 'true'; or return
     *         'false'.
     */
    public static boolean isAfter(Date comparedDate, Date baseDate)
    {
        return comparedDate.after(baseDate);
    }
    
    /**
     * Judge whether or not that comparedDate is not before baseDate.
     * 
     * @param comparedDate
     * @param baseDate
     * @return if comparedDate is not before baseDate, then return 'true'; or return
     *         'false'.
     */
    public static boolean isNotBefore(Date comparedDate, Date baseDate)
    {
        return comparedDate.getTime() >= baseDate.getTime();
    }
    
    /**
     * Judge whether or not that comparedDate is not after baseDate.
     * 
     * @param comparedDate
     * @param baseDate
     * @return if comparedDate is not after baseDate, then return 'true'; or return
     *         'false'.
     */
    public static boolean isNotAfter(Date comparedDate, Date baseDate)
    {
        return comparedDate.getTime() <= baseDate.getTime();
    }

    public static void main(String[] args)
    {
        System.out.println(DateTimeUtils.getYear(new Date()));
        System.out.println(getDayStart(new Date()));
    }
}
