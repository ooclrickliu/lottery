package cn.wisdom.lottery.payment.service.wx;

import me.chanjar.weixin.mp.api.WxMpService;

public interface WXService {

	public WxMpService getWxMpService();
	
	public String getAccessToken();
}
