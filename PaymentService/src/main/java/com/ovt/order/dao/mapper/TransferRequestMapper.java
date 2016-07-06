/**
 * TransferRequestmapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月23日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.vo.TransferRequest;

/**
 * TransferRequestmapper
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class TransferRequestMapper implements RowMapper<TransferRequest>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public TransferRequest mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        // TransferRequest transferRequest = new TransferRequest();
        // transferRequest.setId(rs.getLong(arg0));
        return null;
    }

}
