package cn.wisdom.lottery.service.wx.message;

import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;

@Component
public class WxMpVoiceHandler extends AbstractWxMpHandler {

	private static final String PARAM_USER = "user";

	@Autowired
	private AppProperty appProperty;
	
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
				// not support kf send image to customer
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		
		String content = user.getNickName() + "[" + user.getId() + "] 发的语音:";
		WxMpCustomMessage customTextMessage = WxMpCustomMessage.TEXT().toUser(appProperty.defaultKf).content(content).build();
		wxMpService.customMessageSend(customTextMessage);
		
		WxMpCustomMessage customImageMessage = WxMpCustomMessage.VOICE().toUser(appProperty.defaultKf).mediaId(wxMessage.getMediaId()).build();
		wxMpService.customMessageSend(customImageMessage);
	}

	private boolean isCustomer(String fromUserName) {
		return !StringUtils.equals(fromUserName, appProperty.defaultKf);
	}
}
