/**
 * RefundLogServiceImp.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.common.exception.OVTRuntimeException;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.order.dao.AliRefundLogDaoImp;
import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_REFUND_LOG;
import com.ovt.order.dao.vo.AliRefundLog;
import com.ovt.order.dao.vo.RefundLog;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * RefundLogServiceImp
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class RefundLogServiceImp implements RefundLogService
{
    @Autowired
    private AliRefundLogDaoImp aliRefundLogDao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.RefundLogService#saveRefundLog(com.ovt.dao.vo.AliRefundLog
     * )
     */
    @Override
    public long saveAliRefundLog(Map<String, String> params)
            throws ServiceException
    {
        AliRefundLog aliRefundLog = new AliRefundLog();
        aliRefundLog.setNotifyTime(Timestamp.valueOf(params
                .get(ALI_REFUND_LOG.NOTIFY_TIME)));
        aliRefundLog.setNotifyType(params.get(ALI_REFUND_LOG.NOTIFY_TYPE));
        aliRefundLog.setNotifyId(params.get(ALI_REFUND_LOG.NOTIFY_ID));
        aliRefundLog.setSignType(params.get(ALI_REFUND_LOG.SIGN_TYPE));
        aliRefundLog.setSign(params.get(ALI_REFUND_LOG.SIGN));
        aliRefundLog.setBatchNo(params.get(ALI_REFUND_LOG.BATCH_NO));
        aliRefundLog.setSuccessNum(params.get(ALI_REFUND_LOG.SUCCESS_NUM));
        aliRefundLog
                .setResultDetails(params.get(ALI_REFUND_LOG.RESULT_DETAILS));

        try
        {
            long id = aliRefundLogDao.save(aliRefundLog);
            aliRefundLog.setId(id);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.SAVE_REFUND_LOG_ERROR,
                    "Failed to save alipay refund log!", e);
        }

        return aliRefundLog.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.RefundLogService#getAliRefundLogList()
     */
    @Override
    public List<AliRefundLog> getAliRefundLogList() throws ServiceException
    {
        List<AliRefundLog> aliRefundLogList = new ArrayList<AliRefundLog>();

        List<RefundLog> refundLogList = null;
        try
        {
            refundLogList = aliRefundLogDao.getRefundLogList();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUND_LOGS_ERROR,
                    "Failed to get alipay refund log list!", e);
        }

        if (CollectionUtils.isNotEmpty(refundLogList))
        {
            for (RefundLog refundLog : refundLogList)
            {
                aliRefundLogList.add((AliRefundLog) refundLog);
            }
        }

        return aliRefundLogList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.RefundLogService#getAliRefundLog(long)
     */
    @Override
    public AliRefundLog getAliRefundLog(long logId) throws ServiceException
    {
        AliRefundLog refundLog = null;
        try
        {
            refundLog = (AliRefundLog) aliRefundLogDao.getRefundLog(logId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUND_LOG_ERROR,
                    "Failed to get alipay refund log!", e);
        }

        return refundLog;
    }

}
