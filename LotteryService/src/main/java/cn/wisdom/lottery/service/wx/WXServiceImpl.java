package cn.wisdom.lottery.service.wx;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.service.wx.message.WxMpEventHandler;
import cn.wisdom.lottery.service.wx.message.WxMpImageHandler;
import cn.wisdom.lottery.service.wx.message.WxMpLogHandler;
import cn.wisdom.lottery.service.wx.message.WxMpTextHandler;
import cn.wisdom.lottery.service.wx.message.WxMpVideoHandler;
import cn.wisdom.lottery.service.wx.message.WxMpVoiceHandler;

@Service
public class WXServiceImpl implements WXService {
	
	private WxMpInMemoryConfigStorage wxConfig;

	private WxMpService wxMpService;

	private WxMpMessageRouter wxMpMessageRouter;
	
	@Autowired
	private WxMpLogHandler logHandler;
	
	@Autowired
	private WxMpTextHandler textHandler;
	
	@Autowired
	private WxMpImageHandler imageHandler;
	
	@Autowired
	private WxMpVoiceHandler voiceHandler;
	
	@Autowired
	private WxMpVideoHandler videoHandler;
	
	@Autowired
	private WxMpEventHandler eventHandler;

	@PostConstruct
	public void init() {
		InputStream is1 = this.getClass().getResourceAsStream("/wx-config.xml");
		WxMpLotteryInMemoryConfigStorage config = WxMpLotteryInMemoryConfigStorage
				.fromXml(is1);

		wxConfig = config;
		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter.rule().handler(logHandler).next()
				// .rule().msgType(WxConsts.XML_MSG_TEXT).matcher(guessNumberHandler).handler(guessNumberHandler).end()
				.rule().msgType(WxConsts.XML_MSG_TEXT).async(false).handler(textHandler).end()
				.rule().msgType(WxConsts.XML_MSG_EVENT).async(false).handler(eventHandler).end()
				.rule().msgType(WxConsts.XML_MSG_IMAGE).async(false).handler(imageHandler).end()
				.rule().msgType(WxConsts.XML_MSG_VOICE).async(false).handler(voiceHandler).end()
				.rule().msgType(WxConsts.XML_MSG_VIDEO).async(false).handler(videoHandler).end()
				.rule().msgType("shortvideo").async(false).handler(videoHandler).end()
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
