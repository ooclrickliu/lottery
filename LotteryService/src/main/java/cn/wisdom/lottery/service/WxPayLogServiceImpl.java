package cn.wisdom.lottery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.dao.WxPayLogDao;
import cn.wisdom.lottery.dao.vo.WxPayLog;
import cn.wisdom.lottery.service.exception.ServiceException;

@Service
public class WxPayLogServiceImpl implements WxPayLogService {

	@Autowired
	private WxPayLogDao wxPayLogDao;
	
	@Override
	public void saveWxPayLog(WxPayLog wxPayLog) throws ServiceException {

		try {
			wxPayLogDao.saveWxPayLog(wxPayLog);
		} catch (OVTRuntimeException e) {
			throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
		}
	}

	@Override
	public WxPayLog getWxPayLogByTradeNo(String outTradeNo)
			throws ServiceException {
		WxPayLog wxPayLog = null;
		try {
			wxPayLog = wxPayLogDao.getWxPayLogByTradeNo(outTradeNo);
		} catch (OVTRuntimeException e) {
			throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
		}
		return wxPayLog;
	}

}
