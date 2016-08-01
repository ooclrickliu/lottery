/**
 * EntityProcessor.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Jan 7, 2016
 */
package cn.wisdom.lottery.dao;

/**
 * EntityProcessor
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface EntityProcessor<T>
{
    void process(T entity);
}
