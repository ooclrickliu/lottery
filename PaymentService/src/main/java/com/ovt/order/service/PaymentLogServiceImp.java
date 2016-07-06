/**
 * PaymentLogServiceImp.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.common.exception.OVTRuntimeException;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.AliPaymentLogDaoImp;
import com.ovt.order.dao.AppAliPayemntLogDao;
import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_PAYMENT_LOG;
import com.ovt.order.dao.constant.DBConstants.TABLES.APP_ALI_PAYMENT_LOG;
import com.ovt.order.dao.vo.AliPaymentLog;
import com.ovt.order.dao.vo.AppAliPaymentLog;
import com.ovt.order.dao.vo.PaymentLog;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * PaymentLogServiceImp
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class PaymentLogServiceImp implements PaymentLogService
{
    @Autowired
    private AliPaymentLogDaoImp aliPaymentLogDao;

    @Autowired
    private AppAliPayemntLogDao appAliPayemntLogDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentLogService#saveAliPaymentLog(java.util.Map)
     */
    @Override
    public AliPaymentLog saveAliPaymentLog(Map<String, String> params)
            throws ServiceException
    {
        AliPaymentLog aliPaymentLog = new AliPaymentLog();
        aliPaymentLog.setNotifyTime(Timestamp.valueOf(params
                .get(ALI_PAYMENT_LOG.NOTIFY_TIME)));
        aliPaymentLog.setNotifyType(params.get(ALI_PAYMENT_LOG.NOTIFY_TYPE));
        aliPaymentLog.setNotifyId(params.get(ALI_PAYMENT_LOG.NOTIFY_ID));
        aliPaymentLog.setSignType(params.get(ALI_PAYMENT_LOG.SIGN_TYPE));
        aliPaymentLog.setSign(params.get(ALI_PAYMENT_LOG.SIGN));
        aliPaymentLog.setOutTradeNo(params.get(ALI_PAYMENT_LOG.OUT_TRADE_NO));
        aliPaymentLog.setSubject(params.get(ALI_PAYMENT_LOG.SUBJECT));
        aliPaymentLog.setTradeNo(params.get(ALI_PAYMENT_LOG.TRADE_NO));
        aliPaymentLog.setTradeStatus(params.get(ALI_PAYMENT_LOG.TRADE_STATUS));
        aliPaymentLog.setBuyerId(params.get(ALI_PAYMENT_LOG.BUYER_ID));
        aliPaymentLog.setBuyerEmail(params.get(ALI_PAYMENT_LOG.BUYER_EMAIL));
        aliPaymentLog.setTotalFee(Float.valueOf(params
                .get(ALI_PAYMENT_LOG.TOTAL_FEE)));
        if (params.get(ALI_PAYMENT_LOG.GMT_CREATE) == null)
        {
            aliPaymentLog.setGmtCreate(null);
        }
        else
        {
            aliPaymentLog.setGmtCreate(Timestamp.valueOf(params
                    .get(ALI_PAYMENT_LOG.GMT_CREATE)));
        }

        if (params.get(ALI_PAYMENT_LOG.GMT_PAYMENT) == null)
        {
            aliPaymentLog.setGmtPayment(null);
        }
        else
        {
            aliPaymentLog.setGmtPayment(Timestamp.valueOf(params
                    .get(ALI_PAYMENT_LOG.GMT_PAYMENT)));
        }

        aliPaymentLog.setIsTotalFeeAdjust(params
                .get(ALI_PAYMENT_LOG.IS_TOTAL_FEE_ADJUST));
        aliPaymentLog.setUseCoupon(params.get(ALI_PAYMENT_LOG.USE_COUPON));
        aliPaymentLog.setDiscount(Float.valueOf(params
                .get(ALI_PAYMENT_LOG.DISCOUNT)));
        aliPaymentLog
                .setRefundStatus(params.get(ALI_PAYMENT_LOG.REFUND_STATUS));

        if (params.get(ALI_PAYMENT_LOG.GMT_REFUND) == null)
        {
            aliPaymentLog.setGmtRefund(null);
        }
        else
        {
            aliPaymentLog.setGmtRefund(Timestamp.valueOf(params
                    .get(ALI_PAYMENT_LOG.GMT_REFUND)));
        }

        try
        {
            long id = aliPaymentLogDao.save(aliPaymentLog);
            aliPaymentLog.setId(id);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.SAVE_PAYMENT_LOG_ERROR,
                    "Failed to save alipay payment log!", e);
        }

        return aliPaymentLog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentLogService#getAliPaymentLogList()
     */
    @Override
    public List<AliPaymentLog> getAliPaymentLogList() throws ServiceException
    {
        List<AliPaymentLog> aliPaymentLogList = new ArrayList<AliPaymentLog>();

        List<PaymentLog> paymentLogList = null;
        try
        {
            paymentLogList = aliPaymentLogDao.getPaymentLogList();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENT_LOGS_ERROR,
                    "Failed to get alipay payment log list!", e);
        }

        if (CollectionUtils.isNotEmpty(paymentLogList))
        {
            for (PaymentLog log : paymentLogList)
            {
                aliPaymentLogList.add((AliPaymentLog) log);
            }
        }

        return aliPaymentLogList;
    }

    @Override
    public AliPaymentLog getAliPaymentLog(String logId) throws ServiceException
    {
        AliPaymentLog aliPaymentLog = null;
        try
        {
            aliPaymentLog = (AliPaymentLog) aliPaymentLogDao
                    .getPaymentLog(logId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENT_LOG_ERROR,
                    "Failed to get alipay payment log!", e);
        }

        return aliPaymentLog;

    }

    @Override
    public AppAliPaymentLog saveAppAlipaymentLog(Map<String, String[]> params)
            throws ServiceException
    {
        AppAliPaymentLog appAliPaymentLog = new AppAliPaymentLog();

        Map<String, String> paramMap = processAppAliNotifyParams(params);
        if (CollectionUtils.isNotEmpty(paramMap))
        {
            appAliPaymentLog.setResultStatus(paramMap
                    .get(APP_ALI_PAYMENT_LOG.RESULT_STATUS));
            appAliPaymentLog.setMemo(paramMap.get(APP_ALI_PAYMENT_LOG.MEMO));
            appAliPaymentLog.setPartner(paramMap
                    .get(APP_ALI_PAYMENT_LOG.PARTNER));
            appAliPaymentLog.setSellerId(paramMap
                    .get(APP_ALI_PAYMENT_LOG.SELLER_ID));
            appAliPaymentLog.setOutTradeNo(paramMap
                    .get(APP_ALI_PAYMENT_LOG.OUT_TRADE_NO));
            appAliPaymentLog.setSubject(paramMap
                    .get(APP_ALI_PAYMENT_LOG.SUBJECT));
            appAliPaymentLog.setBody(paramMap.get(APP_ALI_PAYMENT_LOG.BODY));
            appAliPaymentLog.setTotalFee(paramMap
                    .get(APP_ALI_PAYMENT_LOG.TOTAL_FEE));
            appAliPaymentLog.setNotifyUrl(paramMap
                    .get(APP_ALI_PAYMENT_LOG.NOTIFY_URL));
            appAliPaymentLog.setService(paramMap
                    .get(APP_ALI_PAYMENT_LOG.SERVICE));
            appAliPaymentLog.setPaymentType(paramMap
                    .get(APP_ALI_PAYMENT_LOG.PAYMENT_TYPE));
            appAliPaymentLog.setInputCharset(paramMap
                    .get(APP_ALI_PAYMENT_LOG.INPUT_CHARSET));
            appAliPaymentLog.setItBPay(paramMap
                    .get(APP_ALI_PAYMENT_LOG.IT_B_PAY));
            appAliPaymentLog.setSuccess(paramMap
                    .get(APP_ALI_PAYMENT_LOG.SUCCESS));
            appAliPaymentLog.setSignType(paramMap
                    .get(APP_ALI_PAYMENT_LOG.SIGN_TYPE));
            appAliPaymentLog.setSign(paramMap.get(APP_ALI_PAYMENT_LOG.SIGN));

            try
            {
                long id = appAliPayemntLogDao.save(appAliPaymentLog);
                appAliPaymentLog.setId(id);
            }
            catch (OVTRuntimeException e)
            {
                throw new ServiceException(
                        ServiceErrorCode.SAVE_PAYMENT_LOG_ERROR,
                        "Failed to save app alipay payment log!", e);
            }
        }

        return appAliPaymentLog;
    }

    private Map<String, String> processAppAliNotifyParams(
            Map<String, String[]> params)
    {
        Map<String, String> paramMap = new HashMap<String, String>();

        if (CollectionUtils.isNotEmpty(params))
        {
            if (StringUtils.isNotEmpty(params
                    .get(APP_ALI_PAYMENT_LOG.RESULT_STATUS)))
            {
                String resultStatus = params
                        .get(APP_ALI_PAYMENT_LOG.RESULT_STATUS)[0];
                paramMap.put(APP_ALI_PAYMENT_LOG.RESULT_STATUS, resultStatus
                        .replace(StringUtils.QUOTE, StringUtils.BLANK));
            }

            if (StringUtils.isNotEmpty(params.get(APP_ALI_PAYMENT_LOG.MEMO)))
            {
                String memo = params.get(APP_ALI_PAYMENT_LOG.MEMO)[0];
                paramMap.put(APP_ALI_PAYMENT_LOG.MEMO,
                        memo.replace(StringUtils.QUOTE, StringUtils.BLANK));
            }

            if (StringUtils.isNotEmpty(params.get(APP_ALI_PAYMENT_LOG.RESULT)))
            {
                String results = "";
                try
                {
                    results = URLDecoder.decode(
                            params.get(APP_ALI_PAYMENT_LOG.RESULT)[0], "utf-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                String[] resultArray = results.split(StringUtils.AND);
                if (StringUtils.isNotEmpty(resultArray))
                {
                    for (String result : resultArray)
                    {
                        String[] pair = result.split(StringUtils.EQUAL);
                        if (StringUtils.isNotEmpty(pair))
                        {
                            paramMap.put(pair[0].replace(StringUtils.QUOTE,
                                    StringUtils.BLANK), pair[1].replace(
                                    StringUtils.QUOTE, StringUtils.BLANK));
                        }
                    }
                }
            }
        }

        return paramMap;
    }
}
