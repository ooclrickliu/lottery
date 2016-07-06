/**
 * TransferRequestDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.util.List;

import com.ovt.order.dao.vo.TransferRequest;

/**
 * TransferRequestDao
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface TransferRequestDao
{
    /**
     * TransferRequestList is queried by auditor.
     * 
     * @return List<TransferRequest>
     */
    public List<TransferRequest> queryTransferRequestList();
}
