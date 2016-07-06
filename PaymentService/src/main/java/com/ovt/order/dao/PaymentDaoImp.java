/**
 * PaymentDaoImp.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.handler.PostUpdateHandler;
import com.ovt.order.dao.mapper.MapEntryMapper;
import com.ovt.order.dao.mapper.PaymentMapper;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;

/**
 * PaymentDaoImp
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class PaymentDaoImp implements PaymentDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private PaymentMapper paymentMapper;

    private MapEntryMapper<String, String> entryMapper = new MapEntryMapper<String, String>();

    private static final String SQL_INSERT_PAYMENT = "INSERT INTO payment(order_no, pay_no, pay_type,"
            + " pay_source, pay_fee, pay_state, pay_time, pay_log_id, create_time) "
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SQL_UPDATE_PAYMENT = "UPDATE payment SET pay_no = ?, pay_type = ?,"
            + " pay_source = ?, pay_fee = ?, pay_state = ?, pay_time = ?, pay_log_id = ? "
            + " WHERE order_no = ? ";

    private static final String SQL_GET_PREFIX = "SELECT id, order_no, pay_no, pay_type, "
            + "pay_source, pay_fee, pay_state, pay_time, pay_log_id, create_time, "
            + "is_delete FROM payment ";

    private static final String SQL_GET_PAYMENT_BY_ORDER_NO = SQL_GET_PREFIX
            + " WHERE order_no = ? AND is_delete = 0 LIMIT 1";

    private static final String SQL_GET_PAYMENT_LIST = SQL_GET_PREFIX
            + " WHERE is_delete = 0";
    
    private static final String SQL_GET_PAYMENT_LIST_ORDER_BY_PAYTIME = SQL_GET_PREFIX
            + " WHERE pay_state = 'TRADE_SUCCESS' ORDER BY pay_time ASC";

    private static final String SQL_GET_PAYMENT_LIST_BY_TIME_SCOPE = SQL_GET_PREFIX
            + " WHERE pay_time >= ? AND pay_time <= ? AND pay_state = 'TRADE_SUCCESS'";

    private static final String SQL_GET_PAYMENT_LIST_BY_TIME = SQL_GET_PREFIX
            + " WHERE (pay_time between ? and ?)"
            + " ORDER BY {0} {1} LIMIT {2}, {3}";

    private static final String SQL_DELETE_PAYMENT_BY_ORDER_NO = "UPDATE payment SET is_delete = 1"
            + " WHERE order_no = ?";

    private static final String SQL_GET_ORDER_TRADE_BY_ORDER_NO = "SELECT order_no, pay_no "
            + " FROM payment WHERE order_no in ({0}) AND is_delete = 0";
//14612005441003602
    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentDao#save(com.ovt.dao.vo.Payment)
     */
    @Override
    public long save(final Payment payment)
    {
        Object[] params = new Object[8];
        params[0] = payment.getOrderNo();
        params[1] = payment.getPayNo();
        params[2] = payment.getPayType();
        params[3] = payment.getPaySource();
        params[4] = payment.getPayFee();
        params[5] = payment.getPayState();
        params[6] = payment.getPayTime();
        params[7] = payment.getPayLogId();

        PostSaveHandler handler = new PostSaveHandler()
        {
            @Override
            public void handle(Long id)
            {
                payment.setId(id);
            }
        };

        String errMsg = MessageFormat.format(
                "Failed to insert payment, orderNo={0}", payment.getOrderNo());

        daoHelper.save(SQL_INSERT_PAYMENT, handler, errMsg, true, params);

        return payment.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentDao#update(com.ovt.dao.vo.Payment)
     */
    @Override
    public void update(final Payment payment)
    {
        Object[] params = new Object[8];
        params[0] = payment.getPayNo();
        params[1] = payment.getPayType();
        params[2] = payment.getPaySource();
        params[3] = payment.getPayFee();
        params[4] = payment.getPayState();
        params[5] = payment.getPayTime();
        params[6] = payment.getPayLogId();
        params[7] = payment.getOrderNo();

        PostUpdateHandler handler = new PostUpdateHandler()
        {
            @Override
            public void handle()
            {

            }
        };

        System.out.println(params[7]);
        String errMsg = MessageFormat.format(
                "Failed to update payment, orderNo={0}", payment.getOrderNo());

        daoHelper.update(SQL_UPDATE_PAYMENT, handler, errMsg, params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentDao#getPayment(java.lang.String)
     */
    @Override
    public Payment getPayment(final String orderNo)
    {
        String errMsg = MessageFormat.format(
                "Failed to query payment by orderNo={0}", orderNo);

        Payment payment = daoHelper.queryForObject(SQL_GET_PAYMENT_BY_ORDER_NO,
                paymentMapper, errMsg, orderNo);

        return payment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentDao#getPaymentList()
     */
    @Override
    public List<Payment> getPaymentList()
    {
        String errMsg = "Failed to query payment list";

        List<Payment> paymentList = daoHelper.queryForList(
                SQL_GET_PAYMENT_LIST, paymentMapper, errMsg);

        return paymentList;
    }
    
    @Override
    public List<Payment> getPaymentListOrderbyPayTime()
    {
        String errMsg = "Failed to query payment list";

        List<Payment> paymentList = daoHelper.queryForList(
                SQL_GET_PAYMENT_LIST_ORDER_BY_PAYTIME, paymentMapper, errMsg);

        return paymentList;
    }

    @Override
    public List<Payment> getPaymentListByTimeScope(Timestamp startTime,
            Timestamp endTime)
    {
        String errMsg = "Failed to query payment list by time scope";

        List<Payment> paymentList = daoHelper.queryForList(
                SQL_GET_PAYMENT_LIST_BY_TIME_SCOPE, paymentMapper, errMsg, startTime,
                endTime);

        return paymentList;
    }

    @Override
    public List<Payment> getPaymentListByTime(PageInfo page, String startTime,
            String endTime)
    {
        String sql = MessageFormat.format(SQL_GET_PAYMENT_LIST_BY_TIME, page
                .getSortBy(), page.getOrder().toString(), DataConvertUtils
                .toString(page.getPageNo() * page.getPageSize()),
                DataConvertUtils.toString(page.getPageSize()));

        String errMsg = MessageFormat
                .format("Failed to query payment list, startTime = {0}, endTime = {1}!",
                        startTime, endTime);

        List<Payment> paymentList = daoHelper.queryForList(sql, paymentMapper,
                errMsg, startTime, endTime);

        return paymentList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentDao#getTradeNoByOrderNo(java.util.List)
     */
    @Override
    public Map<String, String> getTradeNoByOrderNo(List<String> orderNos)
    {
        String sql = MessageFormat.format(SQL_GET_ORDER_TRADE_BY_ORDER_NO,
                StringUtils.getCSV(orderNos));

        String errMsg = MessageFormat.format(
                "Failed to get payment tradeNo by orderNos={0}", orderNos);

        List<Entry<String, String>> orderTradeEntries = daoHelper.queryForList(
                sql, entryMapper, errMsg);

        Map<String, String> orderTradeMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(orderTradeEntries))
        {
            for (Entry<String, String> entry : orderTradeEntries)
            {
                orderTradeMap.put(entry.getKey(), entry.getValue());
            }
        }

        return orderTradeMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentDao#delete(java.lang.String)
     */
    @Override
    public void delete(String orderNo)
    {
        String errMsg = MessageFormat.format(
                "Failed to delete payment by orderNo={0}", orderNo);

        PostUpdateHandler handler = new PostUpdateHandler()
        {

            @Override
            public void handle()
            {

            }
        };

        daoHelper.update(SQL_DELETE_PAYMENT_BY_ORDER_NO, handler, errMsg,
                orderNo);
    }

}
