package com.ovt.order.dao;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.mapper.AppAliPaymentLogMapper;
import com.ovt.order.dao.vo.AppAliPaymentLog;

@Repository
public class AppAliPaymentLogDaoImpl implements AppAliPayemntLogDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private AppAliPaymentLogMapper appAliPaymentLogMapper;

    private static final String SQL_INSERT_APP_ALI_PAYMENT_LOG = "INSERT INTO app_alipay_payment_log"
            + "(resultStatus, memo, partner, seller_id, out_trade_no, subject, body, total_fee, "
            + "notify_url, service, payment_type, _input_charset, it_b_pay, success, sign_type, "
            + "sign, create_time) VALUES"
            + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SQL_GET_LOG_LIST = "SELECT id, resultStatus, memo, partner, seller_id,"
            + " out_trade_no, subject, body, total_fee, notify_url, service, payment_type,"
            + " _input_charset, it_b_pay, success, sign_type, sign, create_time"
            + " FROM app_alipay_payment_log";

    private static final String SQL_GET_LOG = SQL_GET_LOG_LIST
            + " WHERE out_trade_no = ?";

    @Override
    public long save(final AppAliPaymentLog paymentLog)
    {
        Object params[] = new Object[16];
        params[0] = paymentLog.getResultStatus();
        params[1] = paymentLog.getMemo();
        params[2] = paymentLog.getPartner();
        params[3] = paymentLog.getSellerId();
        params[4] = paymentLog.getOutTradeNo();
        params[5] = paymentLog.getTotalFee();
        params[6] = paymentLog.getBody();
        params[7] = paymentLog.getTotalFee();
        params[8] = paymentLog.getNotifyUrl();
        params[9] = paymentLog.getService();
        params[10] = paymentLog.getPaymentType();
        params[11] = paymentLog.getInputCharset();
        params[12] = paymentLog.getItBPay();
        params[13] = paymentLog.getSuccess();
        params[14] = paymentLog.getSignType();
        params[15] = paymentLog.getSign();

        PostSaveHandler handler = new PostSaveHandler()
        {
            @Override
            public void handle(Long id)
            {
                paymentLog.setId(id);
            }
        };

        String errMsg = MessageFormat.format(
                "Failed to save app alipayment log, order_no = {0}",
                paymentLog.getOutTradeNo());

        daoHelper.save(SQL_INSERT_APP_ALI_PAYMENT_LOG, handler, errMsg, true,
                params);

        return paymentLog.getId();
    }

    @Override
    public List<AppAliPaymentLog> getPaymentLogList()
    {
        String errMsg = "Failed to query app alipay payment list";

        List<AppAliPaymentLog> aliPaymentLogList = daoHelper.queryForList(
                SQL_GET_LOG_LIST, appAliPaymentLogMapper, errMsg);

        return aliPaymentLogList;
    }

    @Override
    public AppAliPaymentLog getPaymentLog(String orderNo)
    {
        String errMsg = MessageFormat.format(
                "Failed to get app alipayment log, order_no = {0}", orderNo);

        AppAliPaymentLog paymentLog = daoHelper.queryForObject(SQL_GET_LOG,
                appAliPaymentLogMapper, errMsg, orderNo);

        return paymentLog;
    }

}
