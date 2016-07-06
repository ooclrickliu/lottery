package cn.wisdom.lottery.payment.service.wx;

import cn.wisdom.lottery.payment.service.exception.ServiceException;

public interface WXOAuthService {
	
	/**
	 * 通过code换取网页授权access_token
	 * 
	 * @param code
	 * @return
	 * @throws ServiceException
	 */
	String getAccessToken(String code) throws ServiceException;
	
	/**
	 * 由于access_token拥有较短的有效期，当access_token超时后，
	 * 可以使用refresh_token进行刷新，refresh_token有效期为30天，
	 * 当refresh_token失效之后，需要用户重新授权
	 * 
	 * @param refreshToken
	 * @return
	 * @throws ServiceException
	 */
	String refreshAccessToken(String refreshToken) throws ServiceException;
	
	/**
	 * 获取用户基本信息
	 * 
	 * @param accessToken  是OAuth通过code换取的access_token, 而不是微信基础接口调用所需的access_token
	 * @param openId
	 * @return
	 * @throws ServiceException
	 */
	String getUserInfo(String accessToken, String openId) throws ServiceException;
}
