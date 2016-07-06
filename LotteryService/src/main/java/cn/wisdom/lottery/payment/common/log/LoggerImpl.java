/**
 * LoggerImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package cn.wisdom.lottery.payment.common.log;

/**
 * LoggerImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Common] 1.0
 */
public class LoggerImpl implements Logger
{

    private org.slf4j.Logger wrappedLogger;

    public LoggerImpl(org.slf4j.Logger wrappedLogger)
    {
        this.wrappedLogger = wrappedLogger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#getName()
     */
    public String getName()
    {
        return this.wrappedLogger.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#trace(java.lang.String)
     */
    public void trace(String msg)
    {
        this.wrappedLogger.trace(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#trace(java.lang.String,
     * java.lang.Throwable)
     */
    public void trace(String msg, Throwable throwable)
    {
        this.wrappedLogger.trace(msg, throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#trace(java.lang.String,
     * java.lang.Object[])
     */
    public void trace(String format, Object... arguments)
    {
        this.wrappedLogger.trace(format, arguments);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#debug(java.lang.String)
     */
    public void debug(String msg)
    {
        this.wrappedLogger.debug(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#debug(java.lang.String,
     * java.lang.Object[])
     */
    public void debug(String format, Object... arguments)
    {
        this.wrappedLogger.debug(format, arguments);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#debug(java.lang.String,
     * java.lang.Throwable)
     */
    public void debug(String msg, Throwable throwable)
    {
        this.wrappedLogger.debug(msg, throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#info(java.lang.String)
     */
    public void info(String msg)
    {
        this.wrappedLogger.info(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#info(java.lang.String, java.lang.Object[])
     */
    public void info(String format, Object... arguments)
    {
        this.wrappedLogger.info(format, arguments);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#info(java.lang.String,
     * java.lang.Throwable)
     */
    public void info(String msg, Throwable throwable)
    {
        this.wrappedLogger.info(msg, throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#warn(java.lang.String)
     */
    public void warn(String msg)
    {
        this.wrappedLogger.warn(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#warn(java.lang.String, java.lang.Object,
     * java.lang.Object)
     */
    public void warn(String format, Object... arguments)
    {
        this.wrappedLogger.warn(format, arguments);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#warn(java.lang.String,
     * java.lang.Throwable)
     */
    public void warn(String msg, Throwable throwable)
    {
        this.wrappedLogger.warn(msg, throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#error(java.lang.String)
     */
    public void error(String msg)
    {
        this.wrappedLogger.error(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#error(java.lang.String,
     * java.lang.Object[])
     */
    public void error(String format, Object... arguments)
    {
        this.wrappedLogger.error(format, arguments);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#error(java.lang.String,
     * java.lang.Throwable)
     */
    public void error(String msg, Throwable throwable)
    {
        this.wrappedLogger.error(msg, throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#entering(java.lang.String,
     * java.lang.String, java.lang.Object[])
     */
    public void entering(String sourceClass, String sourceMethod,
            Object... params)
    {
        this.wrappedLogger.debug("{}.{} ENTRY: {}", sourceClass,
                sourceMethod, params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.common.log.Logger#exiting(java.lang.String,
     * java.lang.String, java.lang.Object)
     */
    public void exiting(String sourceClass, String sourceMethod, Object... result)
    {
        this.wrappedLogger.debug("{}.{} RETURN: {}", sourceClass,
                sourceMethod, result);
    }

}
