package cn.wisdom.lottery.service.wx.message;

import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@Component
public class WxMpTextHandler extends AbstractWxMpHandler {
	
	private static final String menu = "请输入任意有效的双色球期数查询开奖信息，如：2016091";
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		WxMpXmlOutMessage response = null;
		
		// handler different keywords
		String keyword = wxMessage.getContent();
		if (isValidPeriod(keyword)) {
			LotteryOpenData openInfo = null;
			try {
				openInfo = lotteryServiceFacade.getOpenInfo(LotteryType.SSQ, DataConvertUtils.toInt(keyword));
			} catch (ServiceException e) {
			}
			
			if (openInfo == null || StringUtils.isBlank(openInfo.getOpencode())) {
				response = this.buildOutMessage(wxMessage, "未查到双色球" + keyword + "期开奖信息!");
				return response;
			}
			WxMpXmlOutNewsMessage.Item article = new WxMpXmlOutNewsMessage.Item();
			article = new WxMpXmlOutNewsMessage.Item();
			article.setTitle("双色球" + openInfo.getExpect() + "期开奖信息");

			String desc = openInfo.getOpencode().replaceAll("\\+", " \\+ ")
					.replaceAll(",", " ");
			desc += "\n\n";
			desc += "开   奖   时  间: " + openInfo.getOpentime() + "\n\n";
			article.setDescription(desc);
			article.setPicUrl("");
			article.setUrl("");

			response = WxMpXmlOutMessage.NEWS().toUser(wxMessage.getFromUserName())
					.fromUser(wxMessage.getToUserName()).addArticle(article)
					.build();
		}
		else {
			response = this.buildOutMessage(wxMessage, menu);
		}
		
		return response;
	}

	private boolean isValidPeriod(String keyword) {
		if (keyword.length() != 7) {
			return false;
		}
		
		int period = DataConvertUtils.toInt(keyword);
		int year = period / 1000;
		
		if (year < 2015 || year > 2016) {
			return false;
		}
		
		return true;
	}

	
}
