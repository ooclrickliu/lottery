/**
 * RefundDaoImp.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.mapper.RefundMapper;
import com.ovt.order.dao.mapper.RefundOrderMapper;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Refund;

/**
 * RefundDaoImp
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class RefundDaoImp implements RefundDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    private static final String SQL_INSERT_REFUND = "INSERT INTO refund(refund_request_id,"
            + " batch_no, trade_no, refund_state, refund_fee, create_time) VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SQL_UPDATE_REFUND = "UPDATE refund SET refund_fee = ?, "
            + " refund_state = ?, refund_tax = ?, refund_tax_state = ?, refund_time = ?, "
            + " refund_log_id = ? WHERE batch_no = ? AND trade_no = ? ";

    private static final String SQL_GET_REFUND_PREFIX = "SELECT id, refund_request_id, batch_no,"
            + " trade_no, refund_fee, refund_state, refund_tax, refund_tax_state, refund_time,"
            + " refund_log_id, create_time FROM refund";

    private static final String SQL_GET_REFUND_BY_REQUEST_ID = SQL_GET_REFUND_PREFIX
            + " WHERE refund_request_id = ? LIMIT 1";

    private static final String SQL_GET_REFUND_LIST = SQL_GET_REFUND_PREFIX
            + " ORDER BY {0} {1} LIMIT {2}, {3}";
    
    private static final String SQL_GET_REFUND_LIST_ORDER_BY_REFUND_TIME = SQL_GET_REFUND_PREFIX
            + " WHERE refund_state = 'SUCCESS' ORDER BY refund_time ASC";

    private static final String SQL_GET_REFUND_LIST_BY_TIME_SCOPE = SQL_GET_REFUND_PREFIX
            + " WHERE refund_time >= ? AND refund_time <= ? AND refund_state = 'SUCCESS'";

    private static final String SQL_GET_REFUND_LIST_BY_TIME = "SELECT a.id, refund_request_id, order_no,"
            + " a.batch_no, trade_no, a.refund_fee, a.refund_state, refund_tax, refund_tax_state,"
            + " refund_time, refund_log_id, a.create_time FROM `refund` AS a"
            + " LEFT JOIN refund_request AS b ON a.refund_request_id = b.id"
            + " WHERE a.refund_state = \"SUCCESS\" AND a.create_time BETWEEN ? AND ?"
            + " ORDER BY {0} {1} LIMIT {2}, {3}";

    private static final String SQL_GET_REQUEST_ID_BY_BATCH_NO = "SELECT refund_request_id"
            + " FROM refund WHERE batch_no = ? ";

    private static final String SQL_GET_REFUND_LIST_BY_BATCH_NO = SQL_GET_REFUND_PREFIX
            + " WHERE batch_no = ? ";

    private static final String SQL_GET_REFUND_LIST_BY_REQUEST_IDS = SQL_GET_REFUND_PREFIX
            + " WHERE refund_request_id in ({0}) ";

    private static final String SQL_UPDATE_REFUND_LIST_BATCH_NO = "UPDATE refund SET batch_no = ? "
            + " WHERE id in ({0})";

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundDao#save(java.util.List)
     */
    @Override
    public void save(final List<Refund> refunds)
    {
        if (CollectionUtils.isNotEmpty(refunds))
        {
            String errMsg = "Failed to insert refund list";
            daoHelper.saveBatch(SQL_INSERT_REFUND, errMsg,
                    new BatchPreparedStatementSetter()
                    {
                        @Override
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException
                        {
                            Refund refund = refunds.get(i);
                            ps.setLong(1, refund.getRefundRequestId());
                            ps.setString(2, refund.getBatchNo());
                            ps.setString(3, refund.getTradeNo());
                            ps.setString(4, refund.getRefundState());
                            ps.setFloat(5, refund.getRefundFee());
                        }

                        @Override
                        public int getBatchSize()
                        {
                            return refunds.size();
                        }
                    });

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundDao#update(java.util.List)
     */
    @Override
    public void update(final List<Refund> refunds)
    {
        if (CollectionUtils.isNotEmpty(refunds))
        {
            String errMsg = "Failed to update refund list";
            daoHelper.saveBatch(SQL_UPDATE_REFUND, errMsg,
                    new BatchPreparedStatementSetter()
                    {
                        @Override
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException
                        {
                            Refund refund = refunds.get(i);
                            ps.setFloat(1, refund.getRefundFee());
                            ps.setString(2, refund.getRefundState().toString());
                            ps.setFloat(3, refund.getRefundTax());
                            ps.setString(4, refund.getRefundTaxState());
                            ps.setTimestamp(5, refund.getRefundTime());
                            ps.setLong(6, refund.getRefundLogId());
                            ps.setString(7, refund.getBatchNo());
                            ps.setString(8, refund.getTradeNo());
                        }

                        @Override
                        public int getBatchSize()
                        {
                            return refunds.size();
                        }
                    });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundDao#getRefund(long)
     */
    @Override
    public Refund getRefund(long refundRequestId)
    {
        String errMsg = MessageFormat.format(
                "Failed to query refund by request id {0}", refundRequestId);

        Refund refund = daoHelper.queryForObject(SQL_GET_REFUND_BY_REQUEST_ID,
                refundMapper, errMsg, refundRequestId);

        return refund;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundDao#getRefundList()
     */
    @Override
    public List<Refund> getRefundList(PageInfo page)
    {
        String sql = MessageFormat.format(SQL_GET_REFUND_LIST,
                page.getSortBy(), page.getOrder().toString(), DataConvertUtils
                        .toString(page.getPageNo() * page.getPageSize()),
                DataConvertUtils.toString(page.getPageSize()));

        String errMsg = "Failed to query refund list";

        List<Refund> refundList = daoHelper.queryForList(sql, refundMapper,
                errMsg);

        return refundList;
    }
    
    @Override
    public List<Refund> getRefundListOrderByRefundTime()
    {
        String errMsg = "Failed to query refund list";

        List<Refund> refundList = daoHelper.queryForList(SQL_GET_REFUND_LIST_ORDER_BY_REFUND_TIME, refundMapper,
                errMsg);

        return refundList;
    }

    @Override
    public List<Refund> getRefundListByTimeScope(Timestamp startTime,
            Timestamp endTime)
    {
        String errMsg = "Failed to query refund list by time scope";

        List<Refund> refunds = daoHelper.queryForList(
                SQL_GET_REFUND_LIST_BY_TIME_SCOPE, refundMapper, errMsg, startTime,
                endTime);

        return refunds;
    }

    @Override
    public List<Refund> getRefundListByTime(PageInfo page, String startTime,
            String endTime)
    {
        String sql = MessageFormat.format(SQL_GET_REFUND_LIST_BY_TIME, page
                .getSortBy(), page.getOrder().toString(), DataConvertUtils
                .toString(page.getPageNo() * page.getPageSize()),
                DataConvertUtils.toString(page.getPageSize()));

        String errMsg = "Failed to query refund list";

        List<Refund> refundList = daoHelper.queryForList(sql,
                refundOrderMapper, errMsg, startTime, endTime);

        return refundList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundDao#getRefundRequestIdsByBatchNo(java.lang.String)
     */
    @Override
    public List<Long> getRefundRequestIdsByBatchNo(String batchNo)
    {
        String errMsg = MessageFormat.format(
                "Failed to get refund request ids by batch no : {0}", batchNo);

        List<Long> ids = daoHelper.queryForList(SQL_GET_REQUEST_ID_BY_BATCH_NO,
                Long.class, errMsg, batchNo);

        return ids;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.dao.RefundDao#getRefundsByBatchNo(java.lang.String)
     */
    @Override
    public List<Refund> getRefundsByBatchNo(String batchNo)
    {
        String errMsg = MessageFormat.format(
                "Failed to get refund request ids by batch no : {0}", batchNo);

        List<Refund> refunds = daoHelper.queryForList(
                SQL_GET_REFUND_LIST_BY_BATCH_NO, refundMapper, errMsg, batchNo);

        return refunds;
    }

    @Override
    public List<Refund> getRefundsByRequestIds(List<Long> requestIds)
    {
        String sql = MessageFormat.format(SQL_GET_REFUND_LIST_BY_REQUEST_IDS,
                StringUtils.getCSV(requestIds));

        String errMsg = MessageFormat.format(
                "Failed to get refund request by requestIds : {0}", requestIds);

        List<Refund> refunds = daoHelper
                .queryForList(sql, refundMapper, errMsg);

        return refunds;
    }

    @Override
    public void updateBatchNo(List<Long> refundIds, String newBatchNo)
    {
        String sql = MessageFormat.format(SQL_UPDATE_REFUND_LIST_BATCH_NO,
                StringUtils.getCSV(refundIds));

        String errMsg = MessageFormat
                .format("Failed to update refund batchNo by refundIds : {0}",
                        refundIds);

        daoHelper.update(sql, null, errMsg, newBatchNo);
    }

}
