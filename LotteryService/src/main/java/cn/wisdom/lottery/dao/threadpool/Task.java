/**
 * OVTTask.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 29, 2015
 */
package cn.wisdom.lottery.dao.threadpool;

import java.text.MessageFormat;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;

/**
 * OVTTask
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[Service] 1.0
 */
public abstract class Task implements Runnable
{
	private Logger logger = LoggerFactory.getLogger(Task.class.getName());
	
	@Override
	public void run() {
		try {
			execute();
		} catch (Throwable e) {
			logger.error(MessageFormat.format("Task execute error, task = {0}", getName()), e);
		}
	}

	protected abstract void execute();
	protected abstract String getName();
}
