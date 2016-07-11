package cn.wisdom.lottery.payment.service.wx.message;

import java.util.Map;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.payment.service.UserService;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

import com.ovt.order.util.entity.StringUtils;

@Component
public class WxMpEventHandler implements WxMpMessageHandler {

	@Autowired
	private UserService userService;
	
	private static final String EVENT_KEY_SCAN = "qrscene_";

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {

		// 关注
		if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_SUBSCRIBE)) {
			handleSubscribe(wxMessage);
		}
		// 取消关注
		else if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_UNSUBSCRIBE)) {
			// do nothing
		}
		// 点击菜单
		else if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_CLICK)) {
			handleMenuClick(wxMessage);
		}

		return null;
	}

	private void handleMenuClick(WxMpXmlMessage wxMessage) {
		//
		String menuKey = wxMessage.getEventKey();
		if (StringUtils.equalsIgnoreCase(menuKey, "")) {

		}
	}

	private void handleSubscribe(WxMpXmlMessage wxMessage) {
		// 扫码关注
		if (StringUtils.isNotBlank(wxMessage.getEventKey())
				&& wxMessage.getEventKey().startsWith(EVENT_KEY_SCAN)) {

		} 
		// 关注
		else {
			
		}
		
		// 保存用户
		try {
			userService.createCustomer(wxMessage.getFromUserName());
		} catch (ServiceException e) {
			
		}

	}

}
