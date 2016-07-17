package cn.wisdom.lottery.payment.service.wx;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface WXService {

	WxMpService getWxMpService();
	
	WxMpMessageRouter getWxMpMessageRouter();
	
	WxMpInMemoryConfigStorage getWxConfig();
	
	WxMpUser getWxMpUserByOauthCode(String oauthCode);
}
