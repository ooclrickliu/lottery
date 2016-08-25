package cn.wisdom.lottery.service.wx.message;

import java.text.MessageFormat;
import java.util.Map;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.constant.RoleType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
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

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
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
			// do nothing
			response = buildSuccessOutMessage(wxMessage);
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

		// 开奖公告
		if (StringUtils.equalsIgnoreCase(menuKey, "draw_notice")) {
			response = getDrawNotice(wxMessage);
		}
		// 我的彩票
		else if (StringUtils.equalsIgnoreCase(menuKey, "my_lottery")) {
			response = getMyLatestLottery(wxMessage);
		}

		return response;
	}
	
	private WxMpXmlOutMessage getMyLatestLottery(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response = null;
		
		try {
			Lottery lottery = lotteryServiceFacade.getMyLatestLottery(wxMessage.getFromUserName());
			
			if (lottery != null) {
				if (lottery.getPeriods().size() == 1) {
					response = build1PeriodResponse(wxMessage, lottery);
				}
				else if (lottery.getPeriods().size() > 1) {
					response = buildMultiPeriodsResponse(wxMessage, lottery);
				}
			}
			else 
			{
				response = WxMpXmlOutMessage.TEXT().content("您没有购买记录!")
						.toUser(wxMessage.getFromUserName())
						.fromUser(wxMessage.getToUserName()).build();
			}
		} catch (ServiceException e) {
			logger.error("Failed to get my lottery.", e);
		}
		return response;
	}

	private WxMpXmlOutMessage buildMultiPeriodsResponse(WxMpXmlMessage wxMessage,
			Lottery lottery) throws ServiceException {
		WxMpXmlOutMessage response;
		
		LotteryOpenData currentPeriod = lotteryServiceFacade.getCurrentPeriod(lottery.getLotteryType());
		
		LotteryPeriod hintPeriod = this.getHintPeriod(lottery, currentPeriod);
		
		LotteryOpenData openInfo = lotteryServiceFacade.getOpenInfo(
				lottery.getLotteryType(), hintPeriod.getPeriod());
		
		NewsBuilder builder = WxMpXmlOutMessage.NEWS()
				.toUser(wxMessage.getFromUserName())
				.fromUser(wxMessage.getToUserName());
		
		WxMpXmlOutNewsMessage.Item news = new WxMpXmlOutNewsMessage.Item();
		news.setTitle("您最近投注记录");
		news.setPicUrl("");
		
		String descStr = lottery.getLotteryType().getTypeName() + " - " + hintPeriod.getPeriod() + " 期\n";
		descStr += "购买日期: "
				+ DateTimeUtils.formatSqlDateTime(lottery
						.getCreateTime()) + "\n";
		
		PayState payState = lottery.getPayState();
		if (payState == PayState.Paid) {
			descStr += "开奖日期: " + openInfo.getOpentime() + "\n";
			descStr += "中奖结果: " + hintPeriod.getPrizeState().getName() + "\n";
		}
		else if (payState == PayState.PaidApproving) {
			descStr += "开奖日期: " + openInfo.getOpentime() + "\n";
			descStr += "支付状态: " + payState.getName() + "\n";
		}
		else if (payState == PayState.PaidFail) {
			descStr += "支付状态: " + payState.getName() + "\n";
		}

		descStr += "追号: 第" + (lottery.getPeriods().indexOf(hintPeriod) + 1) + "/" + lottery.getPeriods().size() + " 期\n";
		descStr += "投注号码:  (×" + lottery.getTimes() + "倍)\n";
		
		int num = lottery.getNumbers().size();
		for (int i = 0; i < num && i < 5; i++) { // 最多显示5组号码
			descStr += "    "
					+ lottery.getNumbers().get(i).getNumber().replaceAll(",", " ")
					.replaceAll("\\+", " \\+ ") + "\n";
		}
		
		if (num > 5) {
			descStr += "    ...";
		}
		
		news.setDescription(descStr);
		
		String url = "http://cai.southwisdom.cn/lottery/list?openid=" + wxMessage.getFromUserName();
		news.setUrl(url);
		
		response = builder.addArticle(news).build();
		return response;
	}
	
	private LotteryPeriod getHintPeriod(Lottery lottery, LotteryOpenData currentPeriod) {
		int current = DataConvertUtils.toInt(currentPeriod.getExpect());
		
		LotteryPeriod startPeriod = lottery.getPeriods().get(0);
		LotteryPeriod endPeriod = lottery.getPeriods().get(lottery.getPeriods().size() - 1);
		int startP = lottery.getPeriods().get(0).getPeriod();
		int endP = lottery.getPeriods().get(lottery.getPeriods().size() - 1).getPeriod();
		
		LotteryPeriod hintPeriod = startPeriod;
		if (endP <= current) {
			hintPeriod = endPeriod;
		}
		else if (startP < current && endP > current) {
			for (LotteryPeriod lotteryPeriod : lottery.getPeriods()) {
				if (lotteryPeriod.getPeriod() == current) {
					hintPeriod = endPeriod;
					break;
				}
			}
		}
		else if (startP >= current) {
			hintPeriod = startPeriod;
		}
		
		return hintPeriod;
	}

	private WxMpXmlOutMessage build1PeriodResponse(WxMpXmlMessage wxMessage,
			Lottery lottery) throws ServiceException {
		WxMpXmlOutMessage response;
		LotteryPeriod period = lottery.getPeriods().get(0);
		LotteryOpenData openInfo = lotteryServiceFacade.getOpenInfo(
				lottery.getLotteryType(), period.getPeriod());
		
		NewsBuilder builder = WxMpXmlOutMessage.NEWS()
				.toUser(wxMessage.getFromUserName())
				.fromUser(wxMessage.getToUserName());
		
		WxMpXmlOutNewsMessage.Item news = new WxMpXmlOutNewsMessage.Item();
		news.setTitle("您最近投注记录");
		news.setPicUrl("");
		
		String descStr = lottery.getLotteryType().getTypeName() + " - " + period.getPeriod() + " 期\n";
		descStr += "购买日期: "
				+ DateTimeUtils.formatSqlDateTime(lottery
						.getCreateTime()) + "\n";
		
		PayState payState = lottery.getPayState();
		if (payState == PayState.Paid) {
			descStr += "开奖日期: " + openInfo.getOpentime() + "\n";
			descStr += "中奖结果: " + period.getPrizeState().getName() + "\n";
		}
		else if (payState == PayState.PaidApproving) {
			descStr += "开奖日期: " + openInfo.getOpentime() + "\n";
			descStr += "支付状态: " + payState.getName() + "\n";
		}
		else if (payState == PayState.PaidFail) {
			descStr += "支付状态: " + payState.getName() + "\n";
		}

		descStr += "投注号码:  (×" + lottery.getTimes() + "倍)\n";
		int num = lottery.getNumbers().size();
		for (int i = 0; i < num && i < 5; i++) { // 最多显示5组号码
			descStr += "    "
					+ lottery.getNumbers().get(i).getNumber().replaceAll(",", " ")
					.replaceAll("\\+", " \\+ ") + "\n";
		}
		
		if (num > 5) {
			descStr += "    ...";
		}
		
		news.setDescription(descStr);
		
		String url = "http://cai.southwisdom.cn/lottery/list?openid=" + wxMessage.getFromUserName();
		news.setUrl(url);
		
		response = builder.addArticle(news).build();
		return response;
	}

	private WxMpXmlOutMessage getDrawNotice(WxMpXmlMessage wxMessage) {
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
			logger.error("Get draw notice failed!", e);
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

}
