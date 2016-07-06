package com.ovt.order.service;

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
import com.ovt.common.utils.JaxbUtil;
import com.ovt.common.utils.JaxbUtil.CollectionWrapper;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.AccountCheckErrorDao;
import com.ovt.order.dao.vo.AccountCheckError;
import com.ovt.order.dao.vo.AccountPage;
import com.ovt.order.service.alipay.AlipayConfig;
import com.ovt.order.service.alipay.AlipayConstants.Alipay;
import com.ovt.order.service.alipay.AlipaySubmit;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

@Service
public class AccountQueryServiceImpl implements AccountQueryService
{
    private static JaxbUtil xml2Vo = new JaxbUtil(AccountPage.class,
            CollectionWrapper.class);

    private Logger logger = LoggerFactory
            .getLogger(AccountQueryServiceImpl.class.getName());

    @Autowired
    private AccountCheckErrorDao accountCheckErrorDao;

    private static final String RESPONSE_START = "<response>";
    private static final String RESPONSE_END = "</response>";

    private static final String ERROR_START = "<error>";
    private static final String ERROR_END = "</error>";

    @Override
    public AccountPage getAccountPageByTime(String pageNo, String startTime,
            String endTime) throws ServiceException
    {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put(Alipay.PARAM_NAME_SERVICE,
                Alipay.ACCOUNT_QUERY_SERVICE_NAME);
        sParaTemp.put(Alipay.PARAM_NAME_PARTNER, AlipayConfig.partner);
        sParaTemp.put(Alipay.PARAM_NAME_INPUT_CHARSET,
                AlipayConfig.input_charset);
        sParaTemp.put(Alipay.PARAM_NAME_PAGE_NO, pageNo);
        sParaTemp.put(Alipay.PARAM_NAME_GMT_START_TIME, startTime);
        sParaTemp.put(Alipay.PARAM_NAME_GMT_END_TIME, endTime);

        return doAccountPageQuery(sParaTemp);
    }

    private String getAccountQueryError(String sHtmlText)
    {
        String error = StringUtils.BLANK;
        if (sHtmlText.indexOf(ERROR_START) > 0)
        {
            error = sHtmlText.substring(sHtmlText.indexOf(ERROR_START)
                    + ERROR_START.length(), sHtmlText.indexOf(ERROR_END));
        }
        return error;
    }

    @Override
    public AccountPage getAccountPageByOrderNo(String pageNo, String orderNo)
            throws ServiceException
    {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put(Alipay.PARAM_NAME_SERVICE,
                Alipay.ACCOUNT_QUERY_SERVICE_NAME);
        sParaTemp.put(Alipay.PARAM_NAME_PARTNER, AlipayConfig.partner);
        sParaTemp.put(Alipay.PARAM_NAME_INPUT_CHARSET,
                AlipayConfig.input_charset);
        sParaTemp.put(Alipay.PARAM_NAME_PAGE_NO, pageNo);
        sParaTemp.put(Alipay.PARAM_NAME_ORDER_NO, orderNo);

        return doAccountPageQuery(sParaTemp);
    }

    public AccountPage doAccountPageQuery(Map<String, String> params)
            throws ServiceException
    {
        String sHtmlText = StringUtils.BLANK;
        try
        {
            sHtmlText = AlipaySubmit.buildRequest(StringUtils.BLANK,
                    StringUtils.BLANK, params);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

        if (StringUtils.isBlank(sHtmlText))
        {
            throw new ServiceException(
                    ServiceErrorCode.ACCOUNT_PAGE_QUERY_ERROR,
                    "Alipay server not reply");
        }

        if (sHtmlText.indexOf(RESPONSE_START) < 0)
        {
            throw new ServiceException(
                    ServiceErrorCode.ACCOUNT_PAGE_QUERY_ERROR,
                    getAccountQueryError(sHtmlText));
        }
        String response = sHtmlText.substring(sHtmlText.indexOf(RESPONSE_START)
                + RESPONSE_START.length(), sHtmlText.indexOf(RESPONSE_END));

        AccountPage accountPage = xml2Vo.fromXml(response);

        return accountPage;
    }

    @Override
    public void saveAccountCheckError(Map<String, String> orderErrorMap)
            throws ServiceException
    {
        List<AccountCheckError> accountCheckErrors = new ArrayList<AccountCheckError>();
        if (CollectionUtils.isNotEmpty(orderErrorMap))
        {
            for (String orderNo : orderErrorMap.keySet())
            {
                accountCheckErrors.add(new AccountCheckError(orderNo,
                        orderErrorMap.get(orderNo)));
            }
        }

        if (CollectionUtils.isNotEmpty(accountCheckErrors))
        {
            try
            {
                accountCheckErrorDao.save(accountCheckErrors);
            }
            catch (OVTRuntimeException e)
            {
                throw new ServiceException(
                        ServiceErrorCode.ACCOUNT_CHECK_ERROR_SAVE_ERROR,
                        e.getMessage(), e);
            }
        }
    }

    @Override
    public List<AccountCheckError> getAccountCheckErrorList()
            throws ServiceException
    {
        List<AccountCheckError> accountCheckErrors = null;

        try
        {
            accountCheckErrors = accountCheckErrorDao
                    .getAccountCheckErrorList();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(
                    ServiceErrorCode.ACCOUNT_CHECK_ERROR_QUERY_ERROR,
                    e.getMessage(), e);
        }

        return accountCheckErrors;
    }

    @Override
    public void markAccourCheckErrorRead(long id) throws ServiceException
    {
        try
        {
            accountCheckErrorDao.markAccountCheckErrorAsRead(id);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(
                    ServiceErrorCode.ACCOUNT_CHECK_ERROR_UPDATE_ERROR,
                    e.getMessage(), e);
        }

    }
}
