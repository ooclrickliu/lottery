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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.RoleType;
import cn.wisdom.lottery.dao.constant.TicketState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.service.wx.WXService;

@Component
public class WxMpEventHandler implements WxMpMessageHandler {

	@Autowired
	private WXService wxService;

	@Autowired
	private UserService userService;

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private static WxMpXmlOutMessage buildSuccessOutMessage(WxMpXmlMessage wxMessage)
	{
		return WxMpXmlOutMessage.TEXT().content("success")
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
				int period = lottery.getPeriods().get(0);
				LotteryOpenData openInfo = lotteryServiceFacade.getOpenInfo(
						LotteryType.SSQ, period);

				NewsBuilder builder = WxMpXmlOutMessage.NEWS()
						.toUser(wxMessage.getFromUserName())
						.fromUser(wxMessage.getToUserName());

				WxMpXmlOutNewsMessage.Item title = new WxMpXmlOutNewsMessage.Item();
				title.setTitle("您的最新投注记录");
				title.setDescription("");
				title.setPicUrl("");
				title.setUrl("");

				builder.addArticle(title);

				WxMpXmlOutNewsMessage.Item content = new WxMpXmlOutNewsMessage.Item();
				content.setDescription("");
				content.setPicUrl("");
				String titleStr = "双色球" + period + "期\n";
				titleStr += "购买日期: "
						+ DateTimeUtils.formatSqlDateTime(lottery
								.getCreateTime()) + "\n";
				titleStr += "开奖日期" + openInfo.getOpentime();
				titleStr += "中奖结果: " + this.getLotteryState(lottery);
				titleStr += "投注号码: \n";
				for (LotteryNumber lotteryNumber : lottery.getNumbers()) {
					titleStr += "    "
							+ lotteryNumber.getNumber().replaceAll(",", " ")
									.replaceAll("\\+", " \\+ ") + "\n";
				}

				content.setTitle(titleStr);
				
				//TODO
				String url = "?openid=" + wxMessage.getFromUserName();
				content.setUrl(url);

				response = builder.addArticle(content).build();
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

	private String getLotteryState(Lottery lottery) {
		String stateStr = "";

		// 已出奖
		if (lottery.getTicketState() == TicketState.Prized) {
			if (StringUtils.isNotBlank(lottery.getPrizeInfo())
					&& lottery.getPrizeBonus() > 0) {
//				 stateStr += "中奖结果: " + getLotteryState(myLottery);s
				stateStr = "中奖";
			}
			else {
				stateStr = "未中奖";
			}
		}
		// 未出奖
		else {
			stateStr = "未出奖";
		}
		return stateStr;
	}

	private WxMpXmlOutMessage getDrawNotice(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response = null;
		try {
			LotteryOpenData latestOpenInfo = lotteryServiceFacade
					.getLatestOpenInfo(LotteryType.SSQ);

			WxMpXmlOutNewsMessage.Item article = new WxMpXmlOutNewsMessage.Item();
			article = new WxMpXmlOutNewsMessage.Item();
			article.setTitle("双色球" + latestOpenInfo.getExpect() + "期开奖公告");

			String desc = latestOpenInfo.getOpencode().replaceAll("\\+", " \\+ ")
					.replaceAll(",", " ");
			desc += "\n\n";
			desc += "开奖时间 " + latestOpenInfo.getOpentime();
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
			userService.createUser(wxMessage.getFromUserName(), RoleType.CUSTOMER);
		} catch (ServiceException e) {
			logger.error("failed handle subscribe event.", e);
		}
		return buildSuccessOutMessage(wxMessage);
	}

}
