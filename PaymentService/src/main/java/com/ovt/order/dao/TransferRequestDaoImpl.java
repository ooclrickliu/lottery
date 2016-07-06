/**
 * TransferRequestDaoImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015骞�2鏈�1鏃�
 */
package com.ovt.order.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.order.dao.mapper.TransferRequestMapper;
import com.ovt.order.dao.vo.TransferRequest;

/**
 * TransferRequestDaoImpl
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class TransferRequestDaoImpl implements TransferRequestDao
{
    
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private TransferRequestMapper transferRequestMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.TransferRequestDao#queryTransferRequestList()
     */
    @Override
    public List<TransferRequest> queryTransferRequestList()
    {

        return null;
    }

}
