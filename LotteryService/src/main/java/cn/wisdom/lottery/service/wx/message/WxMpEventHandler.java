package cn.wisdom.lottery.service.wx.message;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.RoleType;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.wx.MessageNotifier;
import cn.wisdom.lottery.service.wx.WXService;

@Component
public class WxMpEventHandler implements WxMpMessageHandler {

	@Autowired
	private WXService wxService;

	@Autowired
	private UserService userService;

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private MessageNotifier messageNotifyer;
	
	private  Map<String, MessageBuilder> messageBuilderMap = new HashMap<String, MessageBuilder>();

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	public void registerMessageBuilder(MessageBuilder builder)
	{
		messageBuilderMap.put(builder.getMenuKey(), builder);
	}
	
	private static WxMpXmlOutMessage buildSuccessOutMessage(WxMpXmlMessage wxMessage)
	{
		return WxMpXmlOutMessage.TEXT().content("success")
		.toUser(wxMessage.getFromUserName())
		.fromUser(wxMessage.getToUserName()).build();
	}
	
	private static WxMpXmlOutMessage buildOutMessage(WxMpXmlMessage wxMessage, String content)
	{
		return WxMpXmlOutMessage.TEXT().content(content)
				.toUser(wxMessage.getFromUserName())
				.fromUser(wxMessage.getToUserName()).build();
	}
	
	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {

		WxMpXmlOutMessage response = null;

		// 关注
		if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_SUBSCRIBE)) {
			response = handleSubscribe(wxMessage);
		}
		// 取消关注
		else if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_UNSUBSCRIBE)) {
			response = handleUnSubscribe(wxMessage);
		}
		// 点击菜单
		else if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_CLICK)) {
			response = handleMenuClick(wxMessage);
		}

		return response;
	}

	private WxMpXmlOutMessage handleMenuClick(WxMpXmlMessage wxMessage) {
		System.out.println(wxMessage);
		String menuKey = wxMessage.getEventKey();

		WxMpXmlOutMessage response = null;

		MessageBuilder messageBuilder = messageBuilderMap.get(menuKey);
		if (messageBuilder != null) {
			response = messageBuilder.buildMessage(wxMessage);
		}
		return response;
	}

	private WxMpXmlOutMessage handleSubscribe(WxMpXmlMessage wxMessage) {
		logger.info(MessageFormat.format("New user subscribe: {0}", wxMessage.getFromUserName()));
		
		// 保存用户
		try {
			WxMpUser wxMpUser = wxService.getWxMpService().userInfo(wxMessage.getFromUserName(), null);
			
			User user  = new User(wxMpUser);
			user.setRole(RoleType.CUSTOMER);
			userService.createUser(user);
			
			messageNotifyer.notifyOperatorNewCustomerSubscribed(user);
		} 
		catch (WxErrorException e) {
			logger.error("failed get user info from wx.", e);
		} catch (ServiceException e) {
			logger.error("failed save new user.", e);
		}
		
		return buildOutMessage(wxMessage, "欢迎来到千彩慧友公众平台!");
	}

	private WxMpXmlOutMessage handleUnSubscribe(WxMpXmlMessage wxMessage) {
		logger.info(MessageFormat.format("New user subscribe: {0}", wxMessage.getFromUserName()));
		
		User user = userService.getUserByOpenId(wxMessage.getFromUserName());
		
		// 保存用户
		try {
			messageNotifyer.notifyOperatorCustomerUnSubscribe(user);
		} catch (Exception e) {
			logger.error("failed save new user.", e);
		}
		
		return buildOutMessage(wxMessage, "success");
	}

}
