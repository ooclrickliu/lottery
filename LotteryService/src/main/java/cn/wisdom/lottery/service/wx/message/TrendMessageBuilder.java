package cn.wisdom.lottery.service.wx.message;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;

@Component
public class TrendMessageBuilder implements MessageBuilder {
	
	@Autowired
	private WxMpEventHandler wxMpEventHandler;

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@PostConstruct
	private void init() {
		wxMpEventHandler.registerMessageBuilder(this);
	}

	@Override
	public WxMpXmlOutMessage buildMessage(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response = null;
		try {

			response = WxMpXmlOutMessage.IMAGE().toUser(wxMessage.getFromUserName())
					.fromUser(wxMessage.getToUserName()).mediaId(media_id)
					.build();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public String getMenuKey() {
		return "trend";
	}

}
