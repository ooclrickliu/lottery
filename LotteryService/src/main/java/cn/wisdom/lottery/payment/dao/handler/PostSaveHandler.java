/**
 * PostSaveHandler.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 29, 2015
 */
package cn.wisdom.lottery.payment.dao.handler;

/**
 * PostSaveHandler
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface PostSaveHandler
{

    /**
     * handle with db auto generated id after save new entity into db.
     * 
     * @param entityId
     */
    void handle(Long entityId);
}
