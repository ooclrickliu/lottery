package cn.wisdom.lottery.service.wx.message;

import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@Component
public class WxMpTextHandler extends AbstractWxMpHandler {
	
	private static final char CHAR_AT = '@';

	private static final String PARAM_USER = "user";

	@Autowired
	private AppProperty appProperty;
	
	private static final String menu = "请输入任意有效的双色球期数查询开奖信息，如：2016091";
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private UserService userService;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		WxMpXmlOutMessage response = null;
		
//		response = buildOpenInfoResponse(wxMessage);
		
		buildKfResponse(wxMessage, wxMpService, sessionManager);
		
		return response;
	}
	
	private void buildKfResponse(WxMpXmlMessage wxMessage, WxMpService wxMpService, WxSessionManager sessionManager) {
		try {
			if (isCustomer(wxMessage.getFromUserName())) {
				// redirect to kf
				redirectMessage2Kf(wxMessage, wxMpService, sessionManager);
			}
			else {
				// kf's response
				replyCustomer(wxMessage, wxMpService, sessionManager);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void replyCustomer(WxMpXmlMessage wxMessage,
			WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		String content = wxMessage.getContent();
		if (StringUtils.isNotBlank(content)) {
			content = content.trim();
			if (content.charAt(0) == CHAR_AT) {
				String customerId = content.substring(1, content.indexOf(" "));
				WxSession session = sessionManager.getSession(customerId);
				User user = (User) session.getAttribute(PARAM_USER);
				if (user == null) {
					user = userService.getUserById(DataConvertUtils.toLong(customerId));
					session.setAttribute(PARAM_USER, user);
				}
				
				content = content.substring(content.indexOf(" ") + 1);
				WxMpCustomMessage customMessage = WxMpCustomMessage.TEXT().toUser(user.getOpenid()).content(content).build();
				wxMpService.customMessageSend(customMessage);
			};
		}
		
	}

	private void redirectMessage2Kf(WxMpXmlMessage wxMessage,
			WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		WxSession session = sessionManager.getSession(wxMessage.getFromUserName());
		User user = (User) session.getAttribute(PARAM_USER);
		if (user == null) {
			user = userService.getUserByOpenId(wxMessage.getFromUserName());
			session.setAttribute(wxMessage.getFromUserName(), user);
			
			//reverse session, key = userId
			WxSession reverseSession = sessionManager.getSession(DataConvertUtils.toString(user.getId()));
			if (reverseSession.getAttribute(PARAM_USER) == null) {
				reverseSession.setAttribute(PARAM_USER, user);
			}
		}
		
		String content = user.getNickName() + "[" + user.getId() + "]:\n" + wxMessage.getContent();
		WxMpCustomMessage customMessage = WxMpCustomMessage.TEXT().toUser(appProperty.defaultKf).content(content).build();
		
		wxMpService.customMessageSend(customMessage);
	}

	private boolean isCustomer(String fromUserName) {
		return !StringUtils.equals(fromUserName, appProperty.defaultKf);
	}

	/**
	 * Openinfo response.
	 * 
	 * @param wxMessage
	 * @return
	 */
	private WxMpXmlOutMessage buildOpenInfoResponse(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response;
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
			desc += "开   奖   时  间: " + openInfo.getOpentime();
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
