/**
 * OrderItemMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015骞�2鏈�2鏃�
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.ORDER_ITEM;
import com.ovt.order.dao.vo.OrderItem;

/**
 * OrderItemMapper
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class OrderItemMapper implements RowMapper<OrderItem>
{

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public OrderItem mapRow(ResultSet  rs, int rowNum) throws SQLException
    {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong(ORDER_ITEM.ID));
        orderItem.setOrderId(rs.getLong(ORDER_ITEM.ORDER_ID));
        orderItem.setProductNo(rs.getString(ORDER_ITEM.ITEM_NO));
        orderItem.setProductPrice(rs.getFloat(ORDER_ITEM.ITEM_PRICE));
        orderItem.setProductName(rs.getString(ORDER_ITEM.ITEM_NAME));
        orderItem.setProductNum(rs.getInt(ORDER_ITEM.ITEM_NUM));
        return orderItem;
    }
    
}
