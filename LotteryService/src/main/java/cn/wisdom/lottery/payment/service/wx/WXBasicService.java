package cn.wisdom.lottery.payment.service.wx;

import cn.wisdom.lottery.payment.service.exception.ServiceException;

public interface WXBasicService {

	/**
	 * 获取调用基础接口必要的access_token.
	 * 
	 * @return
	 */
	String getAccessToken() throws ServiceException;
}
