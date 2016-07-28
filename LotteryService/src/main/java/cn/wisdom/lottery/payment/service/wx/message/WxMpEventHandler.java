package cn.wisdom.lottery.payment.service.wx.message;

import java.util.List;
import java.util.Map;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.constant.RoleType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.service.LotteryServiceFacade;
import cn.wisdom.lottery.payment.service.UserService;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.wx.WXService;

import com.ovt.order.util.entity.StringUtils;

@Component
public class WxMpEventHandler implements WxMpMessageHandler {

	@Autowired
	private WXService wxService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
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
		String menuKey = wxMessage.getEventKey();
		
		//开奖公告
		if (StringUtils.equalsIgnoreCase(menuKey, "draw_notice")) {

		}
		//我的彩票
		else if (StringUtils.equalsIgnoreCase(menuKey, "my_lottery")) {
			String openid = wxMessage.getFromUserName();
			
			// 将最近一期的彩票发送给用户
//			List<Lottery> lotteries = lotteryServiceFacade.getMyLatestLottery(openid, LotteryType.SSQ);
//			
//			WxMpCustomMessage wxCustomeMessage = new WxMpCustomMessage();
//			if (CollectionUtils.isEmpty(lotteries)) {
//				wxCustomeMessage.setTitle(title);
//				wxCustomeMessage.setDescription(description);
//				wxCustomeMessage.setContent(content);
//				wxCustomeMessage.setToUser(toUser);
//				wxCustomeMessage.setMsgType(msgType);
//				String description = "您没有投注记录!";
//			}
//			else {
//				
////				wxService.getWxMpService().customMessageSend(message);
//			}
		}
	}

	private void handleSubscribe(WxMpXmlMessage wxMessage) {
//		// 扫码关注
//		if (StringUtils.isNotBlank(wxMessage.getEventKey())
//				&& wxMessage.getEventKey().startsWith(EVENT_KEY_SCAN)) {
//
//		} 
//		// 关注
//		else {
//			
//		}
		
		// 保存用户
		try {
			userService.createUser(wxMessage.getFromUserName(), RoleType.CUSTOMER);
		} catch (ServiceException e) {
			
		}

	}

}
