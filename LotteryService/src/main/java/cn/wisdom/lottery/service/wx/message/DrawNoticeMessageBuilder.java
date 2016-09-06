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
public class DrawNoticeMessageBuilder implements MessageBuilder {
	
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
			LotteryOpenData latestOpenInfo = lotteryServiceFacade
					.getLatestOpenInfo(LotteryType.SSQ);
			
			LotteryOpenData currentPeriod = lotteryServiceFacade.getCurrentPeriod(LotteryType.SSQ);

			WxMpXmlOutNewsMessage.Item article = new WxMpXmlOutNewsMessage.Item();
			article = new WxMpXmlOutNewsMessage.Item();
			article.setTitle("双色球" + latestOpenInfo.getExpect() + "期开奖公告");

			String desc = latestOpenInfo.getOpencode().replaceAll("\\+", " \\+ ")
					.replaceAll(",", " ");
			if (StringUtils.isBlank(desc)) {
				desc = "即将开奖, 先喝杯茶, 不要太激动...";
			}
			
			desc += "\n\n";
			desc += "开   奖   时  间: " + latestOpenInfo.getOpentime() + "\n\n";
			desc += "下期开奖时间: " + currentPeriod.getOpentime();
			article.setDescription(desc);
			article.setPicUrl("");
			article.setUrl("");

			response = WxMpXmlOutMessage.NEWS().toUser(wxMessage.getFromUserName())
					.fromUser(wxMessage.getToUserName()).addArticle(article)
					.build();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public String getMenuKey() {
		return "draw_notice";
	}

}
