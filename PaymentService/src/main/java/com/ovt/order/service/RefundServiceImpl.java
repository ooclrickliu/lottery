/**
 * RefundServiceImp.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.common.exception.OVTRuntimeException;
import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.DateTimeUtils;
import com.ovt.common.utils.NumberGeneratorUtil;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.PaymentDao;
import com.ovt.order.dao.RefundDao;
import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_REFUND_LOG;
import com.ovt.order.dao.constant.LoggerConstants;
import com.ovt.order.dao.constant.PayConstants.REFUND_STATUS;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;
import com.ovt.order.dao.vo.Refund;
import com.ovt.order.dao.vo.RefundRequest;
import com.ovt.order.service.alipay.AlipayConfig;
import com.ovt.order.service.alipay.AlipayConstants.Alipay;
import com.ovt.order.service.alipay.AlipaySubmit;
import com.ovt.order.service.alipay.HttpRequest;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * RefundServiceImp
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class RefundServiceImpl implements RefundService
{
    @Autowired
    private RefundDao refundDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private AppPropertiesService appProperties;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LoggerConstants.ALIPAY_NOTIFY_LOGGER);

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.RefundService#doAliRefund(java.util.List)
     */
    @Override
    public String[] doAliRefund(List<RefundRequest> requests)
            throws ServiceException
    {
        String sHtmlText = null;
        String batchNo = NumberGeneratorUtil.generateRefundBatchNo();

        List<Refund> refundList = createRefunds(requests, batchNo);// 向refund表里添加该批次的退款记录

        sHtmlText = generateAlipayRefundHtmlText(refundList, batchNo);

        return new String[] { sHtmlText, batchNo };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.RefundService#doAliRefundRepost(java.lang.String)
     */
    @Override
    public String[] doAliRefundRepost(List<Long> requestIds)
            throws ServiceException
    {
        String sHtmlText = null;
        String newBatchNo = NumberGeneratorUtil.generateRefundBatchNo();

        List<Refund> refundList = refundDao.getRefundsByRequestIds(requestIds);
        List<Long> refundIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(refundList))
        {
            for (Refund refund : refundList)
            {
                refund.setBatchNo(newBatchNo);
                refundIds.add(refund.getId());
            }
        }

        updateRefundsBatchNo(refundIds, newBatchNo);

        sHtmlText = generateAlipayRefundHtmlText(refundList, newBatchNo);

        return new String[] { sHtmlText, newBatchNo };
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.RefundService#updateRefunds(java.util.Map)
     */
    @Override
    public void updateRefunds(List<Refund> refunds) throws ServiceException
    {
        if (CollectionUtils.isNotEmpty(refunds))
        {
            try
            {
                refundDao.update(refunds);
            }
            catch (OVTRuntimeException e)
            {
                throw new ServiceException(
                        ServiceErrorCode.UPDATE_REFUNDS_ERROR,
                        "Failed to update refund list!", e);
            }
        }
    }

    private void updateRefundsBatchNo(List<Long> refundIds, String newBatchNo)
            throws ServiceException
    {
        if (CollectionUtils.isNotEmpty(refundIds))
        {
            try
            {
                refundDao.updateBatchNo(refundIds, newBatchNo);
            }
            catch (OVTRuntimeException e)
            {
                throw new ServiceException(
                        ServiceErrorCode.UPDATE_REFUNDS_ERROR,
                        "Failed to update refund list!", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.RefundService#getRefund(long)
     */
    @Override
    public Refund getRefund(long refundRequestId) throws ServiceException
    {
        Refund refund = null;
        try
        {
            refund = refundDao.getRefund(refundRequestId);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUND_ERROR,
                    "Failed to get refund by refund request id!", e);
        }

        return refund;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.RefundService#getRefundList()
     */
    @Override
    public List<Refund> getRefundList(PageInfo pageInfo)
            throws ServiceException
    {
        List<Refund> refundList = new ArrayList<Refund>();
        try
        {
            refundList = refundDao.getRefundList(pageInfo);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUNDS_ERROR,
                    "Failed to get refund list!", e);
        }

        return refundList;
    }
    
    @Override
    public List<Refund> getRefundListOrderByRefundTime()
            throws ServiceException
    {
        List<Refund> refundList = new ArrayList<Refund>();
        try
        {
            refundList = refundDao.getRefundListOrderByRefundTime();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUNDS_ERROR,
                    "Failed to get refund list!", e);
        }

        return refundList;
    }
    
    @Override
    public List<Refund> getRefundListByTime(PageInfo pageInfo,
            String startTime, String endTime) throws ServiceException
    {
        List<Refund> refundList = new ArrayList<Refund>();
        try
        {
            refundList = refundDao.getRefundListByTime(pageInfo, startTime, endTime);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUNDS_ERROR,
                    "Failed to get refund list!", e);
        }
        return refundList;
    }

    @Override
    public List<Refund> getRefundListByTimeScope(String startTime, String endTime)
            throws ServiceException
    {
        Timestamp startTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(startTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        Timestamp endTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(endTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        List<Refund> refunds = null;

        try
        {
            refunds = refundDao
                    .getRefundListByTimeScope(startTimestamp, endTimestamp);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_REFUNDS_ERROR,
                    "Failed to get refund list by time scope", e);
        }

        return refunds;
    }


    public List<Long> getRefundRequestIdsByBatchNo(String batchNo)
            throws ServiceException
    {
        List<Long> ids = null;
        try
        {
            ids = refundDao.getRefundRequestIdsByBatchNo(batchNo);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(
                    ServiceErrorCode.GET_REFUND_REQUEST_IDS_ERROR,
                    "Failed to get refund request ids by batch no!", e);
        }

        return ids;
    }

    private Map<String, String> getOrderTradeMap(List<RefundRequest> requests)
            throws ServiceException
    {
        Map<String, String> orderTradeMap = new HashMap<String, String>();

        List<String> orderNoList = new ArrayList<String>();
        for (RefundRequest request : requests)
        {
            orderNoList.add(request.getOrderNo());
        }

        try
        {
            orderTradeMap = paymentDao.getTradeNoByOrderNo(orderNoList);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_TRADE_NO_ERROR,
                    "Failed to get order trade no map!", e);
        }

        return orderTradeMap;
    }

    private List<Refund> createRefunds(List<RefundRequest> requests,
            String bacthNo) throws ServiceException
    {
        List<Refund> refundList = new ArrayList<Refund>();
        if (CollectionUtils.isNotEmpty(requests))
        {
            Map<String, String> orderTradeMap = getOrderTradeMap(requests);
            for (RefundRequest request : requests)
            {
                Refund refund = new Refund();
                refund.setRefundRequestId(request.getId());
                refund.setBatchNo(bacthNo);
                refund.setTradeNo(orderTradeMap.get(request.getOrderNo()));
                refund.setRefundState(REFUND_STATUS.APPLYING);
                refund.setRefundFee(request.getRefundFee());

                refundList.add(refund);
            }

            try
            {
                refundDao.save(refundList); // 向refund表里添加该批次的退款记录
            }
            catch (OVTRuntimeException e)
            {
                throw new ServiceException(ServiceErrorCode.SAVE_REFUNDS_ERROR,
                        "Failed to save refund list!", e);
            }
        }

        return refundList;
    }

    private String generateAlipayRefundHtmlText(List<Refund> refundList,
            String batchNo)
    {
        String detailData = generateAlipayDetailData(refundList);

        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put(Alipay.PARAM_NAME_SERVICE, Alipay.REFUND_SERVICE_NAME);
        sParaTemp.put(Alipay.PARAM_NAME_PARTNER, AlipayConfig.partner);
        sParaTemp.put(Alipay.PARAM_NAME_INPUT_CHARSET,
                AlipayConfig.input_charset);
        sParaTemp.put(Alipay.PARAM_NAME_NOTIFY_URL,
                appProperties.getAlipayRefundNotifyUrl());
        sParaTemp
                .put(Alipay.PARAM_NAME_SELLER_EMAIL, AlipayConfig.seller_email);
        sParaTemp.put(Alipay.PARAM_NAME_REFUND_DATE, DataConvertUtils
                .toString(new Timestamp(System.currentTimeMillis())));
        sParaTemp.put(Alipay.PARAM_NAME_BATCH_NO, batchNo);
        sParaTemp.put(Alipay.PARAM_NAME_BATCH_NUM,
                ((Integer) refundList.size()).toString());
        sParaTemp.put(Alipay.PARAM_NAME_DETAIL_DATA, detailData);

        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,
                HttpRequest.METHOD_GET, Alipay.REFUND_CONFIRM_PAGE_OK);
        LOGGER.info("aliRefundRequest: {}", sHtmlText);
        return sHtmlText;
    }

    private String generateAlipayDetailData(List<Refund> refundList)
    {
        StringBuffer detailData = new StringBuffer();
        if (CollectionUtils.isNotEmpty(refundList))
        {
            for (Refund refund : refundList)
            {
                detailData.append(refund.getTradeNo())
                        .append(StringUtils.CARET)
                        .append(refund.getRefundFee())
                        .append(StringUtils.CARET).append(Alipay.REFUND_REASON)
                        .append(StringUtils.HASH);
            }

            detailData.deleteCharAt(detailData.length() - 1);
        }

        return detailData.toString();
    }

    public List<Refund> parseAlipayRefundNotifyParams(Map<String, String> params)
    {
        List<Refund> refundList = new ArrayList<Refund>();

        String batchNo = params.get(ALI_REFUND_LOG.BATCH_NO);
        String detailData = params.get(ALI_REFUND_LOG.RESULT_DETAILS);

        if (StringUtils.isBlank(batchNo) || StringUtils.isBlank(detailData))
        {
            return null;
        }

        String[] refundDetails = detailData.split(StringUtils.HASH);
        if (null != refundDetails)
        {
            for (int i = 0; i < refundDetails.length; i++)
            {
                boolean hasTaxFlag = false;
                String refundDetail = refundDetails[i];
                if (refundDetail.contains(StringUtils.DOLLAR))
                {
                    hasTaxFlag = true;
                    refundDetail = refundDetail.replace(StringUtils.DOLLAR,
                            StringUtils.CARET);
                }

                String[] refundDetailInfos = refundDetail
                        .split(StringUtils.DOUBLE_SLASH + StringUtils.CARET);
                if (null != refundDetailInfos)
                {
                    Refund refund = new Refund();
                    refund.setBatchNo(batchNo);
                    refund.setTradeNo(refundDetailInfos[0]);
                    refund.setRefundFee(Float.valueOf(refundDetailInfos[1]));
                    refund.setRefundState(refundDetailInfos[2]);

                    if (hasTaxFlag)
                    {
                        refund.setRefundTax(Float.valueOf(refundDetailInfos[5]));
                        refund.setRefundTaxState(refundDetailInfos[6]);
                    }
                    refundList.add(refund);
                }
            }
        }

        return refundList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.RefundService#getTradeNoRefundsMapByBatchNo(java
     * .lang.String)
     */
    @Override
    public Map<String, Refund> getTradeNoRefundsMapByBatchNo(String batchNo)
            throws ServiceException
    {
        Map<String, Refund> tradeNoRefundsMap = new HashMap<String, Refund>();
        List<Refund> refunds = null;
        try
        {
            refunds = refundDao.getRefundsByBatchNo(batchNo);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(
                    ServiceErrorCode.GET_REFUND_REQUEST_IDS_ERROR,
                    "Failed to get refund request ids by batch no!", e);
        }

        if (CollectionUtils.isNotEmpty(refunds))
        {
            for (Refund refund : refunds)
            {
                tradeNoRefundsMap.put(refund.getTradeNo(), refund);
            }
        }

        return tradeNoRefundsMap;
    }

}
