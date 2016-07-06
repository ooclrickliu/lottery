package cn.wisdom.lottery.payment.service.wx;

import java.text.MessageFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.wx.response.WXResponseHandler;

@Service
@SuppressWarnings("rawtypes")
public class WXOAuthServiceImpl implements WXOAuthService {

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private WXResponseHandler wxResponseHandler;

	private static final String PARAM_ACCESS_TOKEN = "access_token";

	public static final String API_OAUTH_GET_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code&appid={0}&secret={1}&code={2}";
	
	public static final String API_OAUTH_REFRESH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={0}&grant_type=refresh_token&refresh_token={1}";
	
	public static final String API_OAUTH_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";

	@Override
	public String getAccessToken(String code) throws ServiceException {
		String url = MessageFormat.format(API_OAUTH_GET_ACCESS_TOKEN,
				WXConfig.AppID, WXConfig.AppSecret, code);
		
		HashMap response = restTemplate.getForObject(url, HashMap.class);

		wxResponseHandler.handleError(response);
		
		//TODO: save token/openid to cache/database

		return response.get(PARAM_ACCESS_TOKEN).toString();
	}
	
	@Override
	public String refreshAccessToken(String refreshToken)
			throws ServiceException {
		String url = MessageFormat.format(API_OAUTH_REFRESH_ACCESS_TOKEN,
				WXConfig.AppID, refreshToken);
		
		HashMap response = restTemplate.getForObject(url, HashMap.class);

		wxResponseHandler.handleError(response);

		return response.get(PARAM_ACCESS_TOKEN).toString();
	}

	@Override
	public String getUserInfo(String accessToken, String openId)
			throws ServiceException {
		String url = MessageFormat.format(API_OAUTH_GET_USER_INFO,
				accessToken, openId);
		
		HashMap response = restTemplate.getForObject(url, HashMap.class);

		wxResponseHandler.handleError(response);
		
		//TODO: save user_info to our database/cache

		return response.get(PARAM_ACCESS_TOKEN).toString();
	}


}
