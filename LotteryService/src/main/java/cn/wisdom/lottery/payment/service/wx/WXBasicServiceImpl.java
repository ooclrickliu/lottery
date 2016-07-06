package cn.wisdom.lottery.payment.service.wx;

import java.text.MessageFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.wx.response.WXResponseHandler;

public class WXBasicServiceImpl implements WXBasicService {

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private WXResponseHandler wxResponseHandler;

	private static final String PARAM_ACCESS_TOKEN = "access_token";

	public static final String API_GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

	@SuppressWarnings("rawtypes")
	@Override
	public String getAccessToken() throws ServiceException {
		String url = MessageFormat.format(API_GET_ACCESS_TOKEN, WXConfig.AppID,
				WXConfig.AppSecret);

		HashMap response = restTemplate.getForObject(url, HashMap.class);

		wxResponseHandler.handleError(response);
		
		//TODO: save token to cache

		return response.get(PARAM_ACCESS_TOKEN).toString();
	}

}
