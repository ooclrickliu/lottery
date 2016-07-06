/**
 * OVTThreadPoolExecutor.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 29, 2015
 */
package cn.wisdom.lottery.payment.dao.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.dao.constant.LoggerConstants;

/**
 * Global thread pool
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Common] 1.0
 */
@Service
public class OVThreadPoolExecutor
{
    private ExecutorService executorService;

    private Logger logger = LoggerFactory
            .getLogger(LoggerConstants.SYSTEM_LOGGER);

    private static final int THREAD_POOL_SHUTDOWN_TIMEOUT = 10;

    @PostConstruct
    public void start()
    {
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * Graceful shutdown, will wait for timeout then force shut down the pool.
     */
    @PreDestroy
    public void shutDown()
    {
        logger.info("Shutting down thread pool..");

        executorService.shutdown();

        if (!executorService.isTerminated())
        {
            try
            {
                executorService.awaitTermination(THREAD_POOL_SHUTDOWN_TIMEOUT,
                        TimeUnit.SECONDS);
            }
            catch (InterruptedException e)
            {
                logger.error("Shutdown global thread pool failed!", e);
            }
            finally
            {
                executorService.shutdownNow();
            }
        }
        logger.info("Shut down thread pool complete.");
    }

    public Future<?> submitTask(OVTask task)
    {
        if (executorService.isShutdown())
        {
            logger.info(
                    "The global thread pool has been shut down, the task [{}] will be ignored!",
                    task.getDescption());
            return null;
        }

        return executorService.submit(task);
    }
}
