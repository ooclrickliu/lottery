/**
 * RefundRequestDaoImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.exception.DaoErrorCode;
import com.ovt.order.dao.exception.DaoException;
import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.mapper.RefundReasonMapper;
import com.ovt.order.dao.mapper.RefundRequestMapper;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.RefundRequest;

/**
 * RefundRequestDaoImpl
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class RefundRequestDaoImpl implements RefundRequestDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    RefundReasonMapper refundReasonMapper;

    private static final String SQL_INSERT_REFUND_REQUEST = "INSERT INTO refund_request(order_no, create_by,"
            + " refund_reason, refund_fee,"
            + " refund_desc, refund_state, create_time) "
            + " VALUES(?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_GET_REFUND_REQUEST_LIST = "SELECT id, order_no, create_by, refund_reason, "
            + " refund_fee, refund_desc, auditor_id, feedback, create_time, refund_state, batch_no, refund_log"
            + " FROM refund_request a";

    private static final String SQL_GET_REFUND_REQUEST_LIST_BY_STATE = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE id = (SELECT MAX(id) FROM refund_request WHERE order_no = a.order_no) AND refund_state = ?"
            + " ORDER BY {0} {1} LIMIT {2}, {3}";

    private static final String SQL_GET_REFUND_REQUEST_LIST_BY_MULTIOPTS = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE id = (SELECT MAX(id) FROM refund_request WHERE order_no = a.order_no) {0}"
            + " ORDER BY {1} {2} LIMIT {3}, {4}";

    private static final String SQL_GET_PROCESSED_REFUND_REQUEST_LIST = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE (refund_state in ({0}))"
            + " ORDER BY {1} {2} LIMIT {3}, {4}";

    private static final String SQL_GET_APPLIED_REFUND_REQUEST_LIST = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE refund_state IN ('REFUND_DOING', 'REFUND_APPLYING', 'REFUND_DONE') AND (order_no = ?)";

    private static final String SQL_GET_REFUND_REQUEST_LIST1 = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE id IN ({0})";

    private static final String SQL_GET_REFUND_REQUEST = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE order_no = ? AND refund_state <> 'REFUND_CANCELED' ORDER BY id DESC LIMIT 1";

    private static final String SQL_GET_REFUND_REQUEST_STATE = "SELECT refund_state FROM refund_request "
            + " WHERE order_no = ? AND refund_state <> 'REFUND_CANCELED'";

    private static final String SQL_GET_REFUND_DONE_REQUEST = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE order_no = ? AND refund_state = 'REFUND_DONE'";

    private static final String SQL_REFUNDING_REQUEST = SQL_GET_REFUND_REQUEST_LIST
            + " WHERE (order_no = ? AND refund_state = 'REFUND_APPLYING')  ORDER BY id desc";

    private static final String SQL_UPDATE_REFUND_REQUEST_LIST_STATE = "UPDATE refund_request SET refund_state = ?"
            + " WHERE id IN ({0})";

    private static final String SQL_UPDATE_REFUND_REQUEST_LIST_STATE1 = "UPDATE refund_request SET refund_state = ?"
            + " WHERE (order_no = ? AND refund_state = ?)";

    private static final String SQL_UPDATE_REFUND_REQUEST_STATE = "UPDATE refund_request SET refund_state = ?"
            + " WHERE (refund_state = 'REFUND_APPLYING' AND order_no IN (SELECT order_no FROM `order` WHERE user_id = ?))";

    private static final String SQL_UPDATE_REFUND_REQUEST_LIST = "UPDATE refund_request SET refund_state = ?, auditor_id = ?, batch_no = ?"
            + " WHERE id IN ({0})";

    private static final String SQL_UPDATE_REFUND_REQUEST_LIST1 = "UPDATE refund_request SET refund_state = ?, auditor_id = ?, feedback = ?"
            + " WHERE id = ?";

    private static final String SQL_UPDATE_REFUND_AUDITOR_ID_BY_BATCH_NO = "UPDATE refund_request SET auditor_id = ?"
            + " WHERE batch_no = ?";

    private static final String SQL_UPDATE_ORDER_REFUND_FEE = "UPDATE `order` AS o INNER JOIN refund_request AS r "
            + " ON o.order_no = r.order_no"
            + " SET o.refunded_fee = o.refunded_fee + ?" + " WHERE r.id = ?";

    private static final String SQL_UPDATE_REFUND_REQUEST_FAIL_RESULT = "UPDATE refund_request SET refund_log = ?, refund_state = 'REFUND_FAILED'"
            + " WHERE id = ?";

    private static final String SQL_UPDATE_REFUND_REQUEST_REPOST = "UPDATE refund_request"
            + " SET refund_fee = ?, refund_state = ?, batch_no = ?, refund_log = ? "
            + " WHERE id = ?";

    @Override
    public RefundRequest saveRefundRequest(final RefundRequest refundRequest)
    {
        Object[] params = new Object[7];
        if (refundRequest == null)
        {
            String errMsg = "The object of 'refundRequest' is null!";
            throw new DaoException(DaoErrorCode.OBJECT_IS_NULL, errMsg);
        }
        params[0] = refundRequest.getOrderNo();
        params[1] = refundRequest.getCreateBy();
        params[2] = refundRequest.getRefundReason();
        params[3] = refundRequest.getRefundFee();
        params[4] = refundRequest.getRefundDesc();
        params[5] = refundRequest.getRefundState().toString();
        params[6] = refundRequest.getCreateTime();

        PostSaveHandler handler = new PostSaveHandler()
        {

            @Override
            public void handle(Long Id)
            {
                refundRequest.setId(Id);

                // order.setCreateTime(new Timestamp(createTime));
                // order.setUpdateTime(new Timestamp(createTime));

                // put into cache
                // order

            }
        };

        String errMsg = MessageFormat.format(
                "Failed to insert refundRequest, orderNo={0}!",
                refundRequest.getOrderNo());

        daoHelper
                .save(SQL_INSERT_REFUND_REQUEST, handler, errMsg, true, params);

        return refundRequest;
    }

    @Override
    public List<RefundRequest> queryAllRefundRequestList()
    {
        String errMsg = "Failed to query reqund request list!";
        Object[] objects = null;
        List<RefundRequest> refundRequests = daoHelper.queryForList(
                SQL_GET_REFUND_REQUEST_LIST, refundRequestMapper, errMsg,
                objects);

        return refundRequests;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList(List<Long> requestIdList)
    {
        String sql = MessageFormat.format(SQL_GET_REFUND_REQUEST_LIST1,
                StringUtils.getCSV(requestIdList));

        String errMsg = "Failed to query refundRequest list";
        List<RefundRequest> refundRequestList = daoHelper.queryForList(sql,
                refundRequestMapper, errMsg);

        return refundRequestList;
    }

    @Override
    public OrderState updateRefundRequestListState(
            Collection<Long> requestListId, OrderState state)
    {
        String sql = MessageFormat.format(SQL_UPDATE_REFUND_REQUEST_LIST_STATE,
                StringUtils.getCSV(requestListId));
        String errMsg = "Failed to update refundRequest list's state!";
        daoHelper.update(sql, null, errMsg, state.toString());
        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.dao.RefundRequestDao#updateAuditorId(java.lang.String,
     * long)
     */
    @Override
    public void updateAuditorId(String batchNo, long auditorId)
    {
        String errMsg = "Failed to update refundRequest list's auditorId!";
        daoHelper.update(SQL_UPDATE_REFUND_AUDITOR_ID_BY_BATCH_NO, null,
                errMsg, auditorId, batchNo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundRequestDao#updateRefundRequestList(java.util.List,
     * com.ovt.dao.contant.OrderState, long)
     */
    @Override
    public OrderState updateRefundRequestList(List<Long> requestListId,
            OrderState state, long auditorId, String batchNo)
    {
        String sql = MessageFormat.format(SQL_UPDATE_REFUND_REQUEST_LIST,
                StringUtils.getCSV(requestListId));
        String errMsg = "Failed to update refundRequest list!";
        daoHelper.update(sql, null, errMsg, state.toString(), auditorId,
                batchNo);
        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundRequestDao#updateRefundRequestList(java.util.List,
     * com.ovt.dao.contant.OrderState, long, java.lang.String)
     */
    @Override
    public OrderState updateRefundRequestListFeedback(
            final List<Long> requestListId, final OrderState state,
            final long auditorId, final String feedback)
    {
        daoHelper.saveBatch(SQL_UPDATE_REFUND_REQUEST_LIST1,
                "Failed to update refundRequest list!",
                new BatchPreparedStatementSetter()
                {

                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException
                    {
                        ps.setString(1, state.toString());
                        ps.setLong(2, auditorId);
                        ps.setString(3, feedback);
                        ps.setFloat(4, requestListId.get(i));
                    }

                    @Override
                    public int getBatchSize()
                    {
                        return requestListId.size();
                    }
                });

        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.dao.RefundRequestDao#queryRefundRequest(java.lang.String)
     */
    @Override
    public List<RefundRequest> queryRefundRequest(String orderNo)
    {
        if (orderNo == null || orderNo.equals(""))
        {
            return null;
        }

        String errMsg = MessageFormat.format(
                "Failed to query order by orderNo {0}!", orderNo);
        List<RefundRequest> refundRequests = daoHelper.queryForList(
                SQL_GET_REFUND_REQUEST, refundRequestMapper, errMsg, orderNo);

        return refundRequests;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.dao.RefundRequestDao#queryRefundRequestState(java.lang.
     * String)
     */
    @Override
    public OrderState queryRefundRequestState(String orderNo)
    {
        if (orderNo == null || orderNo.equals(""))
        {
            return null;
        }

        String errMsg = MessageFormat.format(
                "Failed to query order by orderNo {0}!", orderNo);
        String refundRequestsState = daoHelper.queryForObject(
                SQL_GET_REFUND_REQUEST_STATE, String.class, errMsg, orderNo);

        return OrderState.valueOf(refundRequestsState);
    }

    @Override
    public RefundRequest queryRefundDoneRequestByOrderNo(String orderNo)
    {
        if (orderNo == null || orderNo.equals(""))
        {
            return null;
        }

        String errMsg = MessageFormat.format(
                "Failed to query order by orderNo {0}!", orderNo);
        RefundRequest refundRequest = daoHelper.queryForObject(
                SQL_GET_REFUND_DONE_REQUEST, refundRequestMapper, errMsg,
                orderNo);

        return refundRequest;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList(PageInfo page,
            String requestState)
    {
        String sql = MessageFormat.format(SQL_GET_REFUND_REQUEST_LIST_BY_STATE,
                page.getSortBy(), page.getOrder().toString(), DataConvertUtils
                        .toString(page.getPageNo() * page.getPageSize()),
                DataConvertUtils.toString(page.getPageSize()));
        String errMsg = MessageFormat.format(
                "Failed to query refundRequest list, state = {0}!",
                requestState);

        List<RefundRequest> refundRequestList = daoHelper.queryForList(sql,
                refundRequestMapper, errMsg, requestState);

        return refundRequestList;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList(PageInfo page,
            String orderNo, String requestState, String startTime,
            String endTime)
    {
        String optString = StringUtils.BLANK;
        if (StringUtils.isNotBlank(orderNo))
        {
            optString += StringUtils.SQL_AND + "order_no=" + orderNo;
        }

        if (StringUtils.isNotBlank(requestState))
        {
            optString += StringUtils.SQL_AND + "refund_state=" + requestState;
        }

        if (StringUtils.isNotBlank(startTime)
                && StringUtils.isNotBlank(endTime))
        {
            optString += StringUtils.SQL_AND + "create_time between "
                    + startTime + StringUtils.SQL_AND + endTime;
        }

        String sql = MessageFormat
                .format(SQL_GET_REFUND_REQUEST_LIST_BY_MULTIOPTS,
                        optString,
                        page.getSortBy(),
                        page.getOrder().toString(),
                        DataConvertUtils.toString(page.getPageNo()
                                * page.getPageSize()),
                        DataConvertUtils.toString(page.getPageSize()));
        String errMsg = MessageFormat
                .format("Failed to query refundRequest list, orderNo={0}, state = {1}, startTime={2}, endTime={3}!",
                        orderNo, requestState, startTime, endTime);

        List<RefundRequest> refundRequestList = daoHelper.queryForList(sql,
                refundRequestMapper, errMsg);

        return refundRequestList;
    }

    @Override
    public List<RefundRequest> queryNonProcessRefundRequestList(PageInfo page)
    {
        String sql = MessageFormat.format(SQL_GET_REFUND_REQUEST_LIST_BY_STATE,
                page.getSortBy(), page.getOrder().toString(), DataConvertUtils
                        .toString(page.getPageNo() * page.getPageSize()),
                DataConvertUtils.toString(page.getPageSize()));

        String errMsg = "Failed to query refundRequest list that are not processed!";
        List<RefundRequest> refundRequestList = daoHelper.queryForList(sql,
                refundRequestMapper, errMsg,
                OrderState.REFUND_APPLYING.toString());

        return refundRequestList;
    }

    @Override
    public List<RefundRequest> queryProcessedRefundRequestList(PageInfo page)
    {
        List<String> processedOrderStateList = Arrays.asList(
                OrderState.REFUND_DOING.toString(),
                OrderState.REFUND_REFUSED.toString(),
                OrderState.REFUND_DONE.toString(),
                OrderState.REFUND_FAILED.toString());

        String sql = MessageFormat.format(
                SQL_GET_PROCESSED_REFUND_REQUEST_LIST, StringUtils.getCSV(
                        processedOrderStateList, true), page.getSortBy(), page
                        .getOrder().toString(), DataConvertUtils.toString(page
                        .getPageNo() * page.getPageSize()), DataConvertUtils
                        .toString(page.getPageSize()));

        String errMsg = "Failed to query refundRequest list that are processed!";
        List<RefundRequest> refundRequestList = daoHelper.queryForList(sql,
                refundRequestMapper, errMsg);

        return refundRequestList;
    }

    @Override
    public List<RefundRequest> queryAppliedRefundRequestList(String orderNo)
    {
        String errMsg = MessageFormat.format(
                "Failed to query order by orderNo {0}!", orderNo);

        List<RefundRequest> refundRequestList = daoHelper.queryForList(
                SQL_GET_APPLIED_REFUND_REQUEST_LIST, refundRequestMapper,
                errMsg, orderNo);

        return refundRequestList;
    }

    @Override
    public void updateRefundRequestList(final List<Long> requestListId,
            final OrderState state)
    {
        daoHelper.saveBatch(SQL_UPDATE_REFUND_REQUEST_LIST1,
                "Failed to update refundRequest list!",
                new BatchPreparedStatementSetter()
                {

                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException
                    {
                        ps.setString(1, state.toString());
                        ps.setFloat(2, requestListId.get(i));
                    }

                    @Override
                    public int getBatchSize()
                    {
                        return requestListId.size();
                    }
                });
    }

    @Override
    public void updateRefundRequestState(final List<String> orderNos,
            final OrderState state)
    {

        daoHelper.saveBatch(SQL_UPDATE_REFUND_REQUEST_LIST_STATE1,
                "Failed to update refundRequest list's state!",
                new BatchPreparedStatementSetter()
                {

                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException
                    {
                        ps.setString(1, state.toString());
                        ps.setString(2, orderNos.get(i));
                        ps.setString(3, "REFUND_APPLYING");
                    }

                    @Override
                    public int getBatchSize()
                    {
                        return orderNos.size();
                    }
                });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.dao.RefundRequestDao#queryApplyingRefundRequest(java.lang
     * .String)
     */
    @Override
    public List<RefundRequest> queryRefundingRequest(String orderNo)
    {
        if (orderNo == null || orderNo.equals(""))
        {
            return null;
        }

        String errMsg = MessageFormat
                .format("Failed to query refunding request that is applying by orderNo {0}!",
                        orderNo);
        List<RefundRequest> refundRequests = daoHelper.queryForList(
                SQL_REFUNDING_REQUEST, refundRequestMapper, errMsg, orderNo);

        return refundRequests;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.dao.RefundRequestDao#isRefunding(java.lang.String)
     */
    @Override
    public boolean isRefunding(String orderNo)
    {
        List<RefundRequest> refundRequests = queryRefundingRequest(orderNo);
        boolean result = true;

        if ((refundRequests == null) || (refundRequests.size() == 0))
        {
            result = false;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.dao.RefundRequestDao#updateRefundRequestOrder(long,
     * float)
     */
    @Override
    public void updateOrderRefundedFee(Map<Long, Float> entry)
    {
        final List<Long> idList = new ArrayList<Long>();
        final List<Float> refundFeeList = new ArrayList<Float>();

        Set<Map.Entry<Long, Float>> entrySet = entry.entrySet();

        for (Entry<Long, Float> entry2 : entrySet)
        {
            idList.add(entry2.getKey());
            refundFeeList.add(entry2.getValue());
        }

        daoHelper.saveBatch(SQL_UPDATE_ORDER_REFUND_FEE,
                "Failed to update order's refunded fee!",
                new BatchPreparedStatementSetter()
                {

                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException
                    {
                        ps.setFloat(1, refundFeeList.get(i));
                        ps.setLong(2, idList.get(i));
                    }

                    @Override
                    public int getBatchSize()
                    {
                        return refundFeeList.size();
                    }
                });

    }

    @Override
    public void updateRefundRequestState(String userId, OrderState state)
    {
        String errMsg = MessageFormat
                .format("Failed to update refund request state that is applying for refund, the refund request was send by userId [{0}]!",
                        userId);
        daoHelper.update(SQL_UPDATE_REFUND_REQUEST_STATE, null, errMsg,
                state.toString(), userId);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.dao.RefundRequestDao#updateRefundRequestListDesc(java.util
     * .Map)
     */
    @Override
    public void updateRefundRequestListFailResult(
            final Map<Long, String> refundRequetIdDescMap)
    {
        final List<Long> ids = CollectionUtils.toList(refundRequetIdDescMap
                .keySet());
        daoHelper.saveBatch(SQL_UPDATE_REFUND_REQUEST_FAIL_RESULT,
                "Failed to update order's refunded desc!",
                new BatchPreparedStatementSetter()
                {

                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException
                    {
                        Long id = ids.get(i);
                        ps.setString(1, refundRequetIdDescMap.get(id));
                        ps.setLong(2, id);
                    }

                    @Override
                    public int getBatchSize()
                    {
                        return refundRequetIdDescMap.size();
                    }
                });
    }

    @Override
    public void updateRefundRequest(RefundRequest refundRequest)
    {
        String errMsg = "Failed to update refundRequest list!";
        Object[] params = new Object[5];

        params[0] = refundRequest.getRefundFee();
        params[1] = refundRequest.getRefundState().toString();
        params[2] = refundRequest.getBatchNo();
        params[3] = refundRequest.getRefundLog();
        params[4] = refundRequest.getId();

        daoHelper
                .update(SQL_UPDATE_REFUND_REQUEST_REPOST, null, errMsg, params);
    }

}
