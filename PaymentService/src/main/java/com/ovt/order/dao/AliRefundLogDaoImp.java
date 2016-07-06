/**
 * AliRefundLogDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.mapper.AliRefundLogMapper;
import com.ovt.order.dao.vo.AliRefundLog;
import com.ovt.order.dao.vo.RefundLog;

/**
 * AliRefundLogDao
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class AliRefundLogDaoImp implements RefundLogDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private AliRefundLogMapper aliRefundLogMapper;

    private static final String SQL_INSERT_ALI_REFUND_LOG = "INSERT INTO alipay_refund_log"
            + "(batch_no, notify_time, notify_type, notify_id, sign_type, sign, "
            + " success_num, result_details) VALUES"
            + " (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_GET_LOG_LIST = "SELECT id, batch_no, notify_time, "
            + " notify_type, notify_id, sign_type, sign, success_num, result_details"
            + " FROM alipay_refund_log";
    
    private static final String SQL_GET_LOG = "SELECT id, batch_no, notify_time, "
            + " notify_type, notify_id, sign_type, sign, success_num, result_details"
            + " FROM alipay_refund_log WHERE id = ?";

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundLogDao#save(com.ovt.dao.vo.RefundLog)
     */
    @Override
    public long save(final RefundLog refundLog)
    {
        final AliRefundLog aliRefundLog = (AliRefundLog) refundLog;

        Object[] params = new Object[8];
        params[0] = aliRefundLog.getBatchNo();
        params[1] = aliRefundLog.getNotifyTime();
        params[2] = aliRefundLog.getNotifyType();
        params[3] = aliRefundLog.getNotifyId();
        params[4] = aliRefundLog.getSignType();
        params[5] = aliRefundLog.getSign();
        params[6] = aliRefundLog.getSuccessNum();
        params[7] = aliRefundLog.getResultDetails();

        PostSaveHandler handler = new PostSaveHandler()
        {
            @Override
            public void handle(Long id)
            {
                aliRefundLog.setId(id);
            }
        };

        String errMsg = MessageFormat.format(
                "Failed to save refund log, batchNo = {0}",
                aliRefundLog.getBatchNo());

        daoHelper
                .save(SQL_INSERT_ALI_REFUND_LOG, handler, errMsg, true, params);

        return aliRefundLog.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.RefundLogDao#getRefundLogList()
     */
    @Override
    public List<RefundLog> getRefundLogList()
    {
        String errMsg = "Failed to query alipay payment list";

        List<RefundLog> aliRefundLogList = daoHelper.queryForList(
                SQL_GET_LOG_LIST, aliRefundLogMapper, errMsg);

        return aliRefundLogList;
    }

    /* (non-Javadoc)
     * @see com.ovt.order.dao.RefundLogDao#getRefundLog(long)
     */
    @Override
    public RefundLog getRefundLog(long logId)
    {
        
        String errMsg = MessageFormat.format(
                "Failed to query refund log by logId {0}!", logId);
        RefundLog refundLog = daoHelper.queryForObject(SQL_GET_LOG, aliRefundLogMapper,
                errMsg, logId);

        return refundLog;
    }

}
