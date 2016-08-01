/**
 * Logger.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package cn.wisdom.lottery.common.log;

/**
 * Logger
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Common] 1.0
 */
public interface Logger
{

    /**
     * Return the name of this <code>Logger</code> instance.
     * 
     * @return name of this logger instance
     */
    public String getName();

    /**
     * Log a message at the TRACE level.
     *
     * @param msg the message string to be logged
     * @since 1.4
     */
    public void trace(String msg);

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param throwable the exception (throwable) to log
     * @since 1.4
     */
    public void trace(String msg, Throwable throwable);

    /**
     * Log a message at the TRACE level according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous string concatenation when the logger is
     * disabled for the TRACE level. However, this variant incurs the hidden
     * (and relatively small) cost of creating an <code>Object[]</code> before
     * invoking the method, even if this logger is disabled for TRACE. The
     * variants taking {@link #trace(String, Object) one} and
     * {@link #trace(String, Object, Object) two} arguments exist solely in
     * order to avoid this hidden cost.
     * </p>
     *
     * @param format the format string
     * @param arguments a list of 3 or more arguments
     * @since 1.4
     */
    public void trace(String format, Object... arguments);

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg the message string to be logged
     */
    public void debug(String msg);

    /**
     * Log a message at the DEBUG level according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous string concatenation when the logger is
     * disabled for the DEBUG level. However, this variant incurs the hidden
     * (and relatively small) cost of creating an <code>Object[]</code> before
     * invoking the method, even if this logger is disabled for DEBUG. The
     * variants taking {@link #debug(String, Object) one} and
     * {@link #debug(String, Object, Object) two} arguments exist solely in
     * order to avoid this hidden cost.
     * </p>
     *
     * @param format the format string
     * @param arguments a list of 3 or more arguments
     */
    public void debug(String format, Object... arguments);

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param throwable the exception (throwable) to log
     */
    public void debug(String msg, Throwable throwable);

    /**
     * Log a message at the INFO level.
     *
     * @param msg the message string to be logged
     */
    public void info(String msg);

    /**
     * Log a message at the INFO level according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous string concatenation when the logger is
     * disabled for the INFO level. However, this variant incurs the hidden (and
     * relatively small) cost of creating an <code>Object[]</code> before
     * invoking the method, even if this logger is disabled for INFO. The
     * variants taking {@link #info(String, Object) one} and
     * {@link #info(String, Object, Object) two} arguments exist solely in order
     * to avoid this hidden cost.
     * </p>
     *
     * @param format the format string
     * @param arguments a list of 3 or more arguments
     */
    public void info(String format, Object... arguments);

    /**
     * Log an exception (throwable) at the INFO level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param throwable the exception (throwable) to log
     */
    public void info(String msg, Throwable throwable);

    /**
     * Log a message at the WARN level.
     *
     * @param msg the message string to be logged
     */
    public void warn(String msg);

    /**
     * Log a message at the WARN level according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARN level.
     * </p>
     *
     * @param format the format string
     * @param arguments a list of 3 or more arguments
     */
    public void warn(String format, Object... arguments);

    /**
     * Log an exception (throwable) at the WARN level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param throwable the exception (throwable) to log
     */
    public void warn(String msg, Throwable throwable);

    /**
     * Log a message at the ERROR level.
     *
     * @param msg the message string to be logged
     */
    public void error(String msg);

    /**
     * Log a message at the ERROR level according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous string concatenation when the logger is
     * disabled for the ERROR level. However, this variant incurs the hidden
     * (and relatively small) cost of creating an <code>Object[]</code> before
     * invoking the method, even if this logger is disabled for ERROR. The
     * variants taking {@link #error(String, Object) one} and
     * {@link #error(String, Object, Object) two} arguments exist solely in
     * order to avoid this hidden cost.
     * </p>
     *
     * @param format the format string
     * @param arguments a list of 3 or more arguments
     */
    public void error(String format, Object... arguments);

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param throwable the exception (throwable) to log
     */
    public void error(String msg, Throwable throwable);

    /**
     * Log a method entry.
     * <p>
     * This is a convenience method that can be used to log entry to a method. A
     * LogRecord with message "ENTRY", log level FINER, and the given
     * sourceMethod and sourceClass is logged.
     * <p>
     * 
     * @param sourceClass name of class that issued the logging request
     * @param sourceMethod name of method that is being entered
     * @param params method parameters
     */
    public void entering(String sourceClass, String sourceMethod,
            Object... params);

    /**
     * Log a method return, with result object.
     * <p>
     * This is a convenience method that can be used to log returning from a
     * method. A LogRecord with message "RETURN {0}", log level FINER, and the
     * gives sourceMethod, sourceClass, and result object is logged.
     * <p>
     * 
     * @param sourceClass name of class that issued the logging request
     * @param sourceMethod name of the method
     * @param result Object that is being returned
     */
    public void exiting(String sourceClass, String sourceMethod,
            Object... result);
}
