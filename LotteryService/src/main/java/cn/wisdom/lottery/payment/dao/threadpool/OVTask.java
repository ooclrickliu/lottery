/**
 * OVTTask.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 29, 2015
 */
package cn.wisdom.lottery.payment.dao.threadpool;

/**
 * OVTTask
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[Service] 1.0
 */
public interface OVTask extends Runnable
{

    String getDescption();
}
