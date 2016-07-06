package cn.wisdom.lottery.payment.service.wx;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.service.wx.message.LotteryLogHandler;
import cn.wisdom.lottery.payment.service.wx.message.LotteryTextHandler;

@Service
public class WXService {
	
	private WxMpInMemoryConfigStorage wxConfig;

	private WxMpService wxMpService;

	private WxMpMessageRouter wxMpMessageRouter;

	@PostConstruct
	public void init() {
		InputStream is1 = ClassLoader
				.getSystemResourceAsStream("wx-config.xml");
		WxMpWisdomInMemoryConfigStorage config = WxMpWisdomInMemoryConfigStorage
				.fromXml(is1);

		wxConfig = config;
		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);

		WxMpMessageHandler logHandler = new LotteryLogHandler();
		WxMpMessageHandler textHandler = new LotteryTextHandler();

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter.rule().handler(logHandler).next()
				// .rule().msgType(WxConsts.XML_MSG_TEXT).matcher(guessNumberHandler).handler(guessNumberHandler).end()
				.rule().msgType(WxConsts.XML_MSG_TEXT).async(false)
				.content("哈哈").handler(textHandler).end()
		// .rule().async(false).content("图片").handler(imageHandler).end()
		// .rule().async(false).content("oauth").handler(oauth2handler).end()
		;

	}

	public WxMpService getWxMpService() {
		return wxMpService;
	}

	public WxMpMessageRouter getWxMpMessageRouter() {
		return wxMpMessageRouter;
	}

	public WxMpInMemoryConfigStorage getWxConfig() {
		return wxConfig;
	}
}
