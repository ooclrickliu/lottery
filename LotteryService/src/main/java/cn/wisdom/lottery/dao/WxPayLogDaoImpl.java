package cn.wisdom.lottery.dao;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.dao.mapper.DaoRowMapper;
import cn.wisdom.lottery.dao.vo.WxPayLog;

@Repository
public class WxPayLogDaoImpl implements WxPayLogDao {

	@Autowired
	private DaoHelper daoHelper;
	
	private static final DaoRowMapper<WxPayLog> wxPayLogMapper = new DaoRowMapper<WxPayLog>(WxPayLog.class);
	
	private static final String SQL_INSERT_WX_PALY_LOG = "INSERT IGNORE INTO wx_pay_log(appid,"
			+ " bank_type, cash_fee, fee_type, is_subscribe, mch_id, nonce_str, openid,"
			+ " out_trade_no, result_code, return_code, sign, time_end, total_fee, trade_type, transaction_id)"
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_GET_LOG_BY_TRADE_NO = "SELECT appid, bank_type, cash_fee,"
			+ " fee_type, is_subscribe, mch_id, nonce_str, openid, out_trade_no, result_code,"
			+ " return_code, sign, time_end, total_fee, trade_type, transaction_id"
			+ " from wx_pay_log where out_trade_no = ?";
	
	@Override
	public long saveWxPayLog(WxPayLog wxPayLog) {
		// TODO Auto-generated method stub
		Object[] params = new Object[16];
		params[0] = wxPayLog.getAppId();
		params[1] = wxPayLog.getBankType();
		params[2] = wxPayLog.getCashFee();
		params[3] = wxPayLog.getFeeType();
		params[4] = wxPayLog.getIsSubscribe();
		params[5] = wxPayLog.getMchId();
		params[6] = wxPayLog.getNonceStr();
		params[7] = wxPayLog.getOpenId();
		params[8] = wxPayLog.getOutTradeNo();
		params[9] = wxPayLog.getResultCode();
		params[10] = wxPayLog.getReturnCode();
		params[11] = wxPayLog.getSign();
		params[12] = wxPayLog.getTimeEnd();
		params[13] = wxPayLog.getTotalFee();
		params[14] = wxPayLog.getTradeType();
		params[15] = wxPayLog.getTransactionId();
		
		String errMsg = "Failed insert wxPayLog!";
		
		long id = daoHelper.save(SQL_INSERT_WX_PALY_LOG, errMsg, true, params);
		
		return id;
	}

	@Override
	public WxPayLog getWxPayLogByTradeNo(String outTradeNo) {
		String errMsg = MessageFormat.format("Failed to get wxPayLog by outtradeNo:{0}", outTradeNo);
		
		WxPayLog wxPayLog = daoHelper.queryForObject(SQL_GET_LOG_BY_TRADE_NO, wxPayLogMapper, errMsg, outTradeNo);
		
		return wxPayLog;
	}

}
