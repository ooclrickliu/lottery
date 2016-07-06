/**
 * LoggerFactory.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package com.ovt.common.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LoggerFactory
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public abstract class LoggerFactory
{

    private static Map<String, Logger> loggerCache = new ConcurrentHashMap<String, Logger>();
    
    private static Object lock = new Object();

    /**
     * Return a logger named according to the name parameter using the
     * statically bound {@link ILoggerFactory} instance.
     *
     * @param name The name of the logger.
     * @return logger
     */
    public static Logger getLogger(String name)
    {
        Logger logger = loggerCache.get(name);
        if (logger != null)
        {
            return logger;
        }

        synchronized (lock)
        {
            logger = loggerCache.get(name);
            if (logger != null)
            {
                return logger;
            }
            
            org.slf4j.Logger wrappedLogger = org.slf4j.LoggerFactory
                    .getLogger(name);
            logger = new LoggerImpl(wrappedLogger);
            loggerCache.put(name, logger);
        }

        return logger;
    }
}
