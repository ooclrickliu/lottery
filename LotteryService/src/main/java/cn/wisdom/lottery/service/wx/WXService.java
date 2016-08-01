package cn.wisdom.lottery.service.wx;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;

public interface WXService {

	WxMpService getWxMpService();
	
	WxMpMessageRouter getWxMpMessageRouter();
	
	WxMpInMemoryConfigStorage getWxConfig();
}
