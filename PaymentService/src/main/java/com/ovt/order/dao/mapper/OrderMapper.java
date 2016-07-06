/**
 * OrderMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.constant.DBConstants.TABLES.ORDER;
import com.ovt.order.dao.vo.Order;

/**
 * OrderMapper
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class OrderMapper implements RowMapper<Order>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Order order = new Order();
        order.setId(rs.getLong(ORDER.ID));
        order.setOrderNo(rs.getString(ORDER.ORDER_NO));
        order.setOrderState(OrderState.valueOf(rs.getString(ORDER.ORDER_STATE)));
        order.setOrderTotalFee(rs.getFloat(ORDER.ORDER_TOTAL_FEE));
        order.setRefundedFee(rs.getFloat(ORDER.REFUNDED_FEE));
        order.setUserId(rs.getString(ORDER.USER_ID));
        order.setCreateBy(rs.getString(ORDER.CREATE_BY));
        order.setCreateTime(rs.getTimestamp(ORDER.CREATE_TIME));
        order.setUpdateTime(rs.getTimestamp(ORDER.UPDATE_TIME));
        order.setOrderRemark(rs.getString(ORDER.ORDER_REMARK));
        order.setIsDelete(rs.getInt(ORDER.IS_DELETE));
        order.setExtra1(rs.getInt(ORDER.EXTRA_1));
        order.setExtra2(rs.getString(ORDER.EXTRA_2));
        order.setExtra3(rs.getTimestamp(ORDER.EXTRA_3));
        // order.setOrderItemList(rs.get); 返回这个不完整的Order之后，再拼接List属性
        return order;
    }

}
