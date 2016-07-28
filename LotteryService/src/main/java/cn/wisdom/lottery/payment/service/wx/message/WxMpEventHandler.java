package cn.wisdom.lottery.payment.service.wx.message;

import java.util.List;
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

import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.constant.RoleType;
import cn.wisdom.lottery.payment.dao.constant.TicketState;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.dao.vo.LotteryNumber;
import cn.wisdom.lottery.payment.service.LotteryServiceFacade;
import cn.wisdom.lottery.payment.service.UserService;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;
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
	
//	private static final String EVENT_KEY_SCAN = "qrscene_";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {

		logger.info("====== Fired event ======");
		logger.info(wxMessage.toString());
		logger.info("=========================");
		
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
		}
		// 点击菜单
		else if (StringUtils.equalsIgnoreCase(wxMessage.getEvent(),
				WxConsts.EVT_CLICK)) {
			response = handleMenuClick(wxMessage);
		}

		return response;
	}

	private WxMpXmlOutMessage handleMenuClick(WxMpXmlMessage wxMessage) {
		String menuKey = wxMessage.getEventKey();
		
		WxMpXmlOutMessage response = null;
		
		//开奖公�
		if (StringUtils.equalsIgnoreCase(menuKey, "draw_notice")) {
			response = getDrawNotice(wxMessage);
		}
		//我的彩票
		else if (StringUtils.equalsIgnoreCase(menuKey, "my_lottery")) {
			response = getMyLottery(wxMessage);
		}
		
		return response;
//			List<Lottery> lotteries = lotteryServiceFacade.getMyLatestLottery(openid, LotteryType.SSQ);
//			
//			WxMpCustomMessage wxCustomeMessage = new WxMpCustomMessage();
//			if (CollectionUtils.isEmpty(lotteries)) {
		
		try {
			List<Lottery> lotteries = lotteryServiceFacade.getMyLottery(wxMessage.getFromUserName(), LotteryType.SSQ, 1);
			
			if (CollectionUtils.isNotEmpty(lotteries)) {
//				String description = "您没有投注记�";
//			}
//			else {
//				
////				wxService.getWxMpService().customMessageSend(message);
//			}
				
				WxMpXmlOutNewsMessage.Item title = new WxMpXmlOutNewsMessage.Item();
				title.setTitle("您的最新投注记�);
				title.setDescription("");
				title.setPicUrl("");
				title.setUrl("");
				
				builder.addArticle(title);
				
				WxMpXmlOutNewsMessage.Item content = new WxMpXmlOutNewsMessage.Item();
				content.setDescription("");
				content.setPicUrl("");
				content.setUrl("");
				String titleStr = "双色�� + period + "�\n";
				titleStr += "购买日期: " + DateTimeUtils.formatSqlDateTime(myLottery.getCreateTime()) + "\n";
				titleStr += "开奖日� " + openInfo.getOpentime();
				titleStr += "中奖结果: " + getLotteryState(myLottery);
				titleStr += "投注号码: \n";
				for (LotteryNumber lotteryNumber : myLottery.getNumbers()) {
					titleStr += "    " + lotteryNumber.getNumber().replaceAll(",", " ").replaceAll("+", " + ") + "\n";
				}
				
				content.setTitle(titleStr);
				
//				response = builder.addArticle(article).build();
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		return response;
	}

	private String getLotteryState(Lottery lottery) {
		String stateStr = "";
		
		// 已出�
		if (lottery.getTicketState() == TicketState.Prized) {
			if (StringUtils.isNotBlank(lottery.getPrizeInfo()) && lottery.getPrizeBonus() > 0) {
//				stateStr += "中奖结果: " + getLotteryState(myLottery);
				stateStr = "中奖";
			}
		}
		// 未出�
//		lottery.getTicketState().getName() + ((StringUtils.isBlank(lottery.getPrizeInfo())) ? )
		return null;
	}

	private WxMpXmlOutMessage getDrawNotice(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response = null;
		try {
			LotteryOpenData latestOpenInfo = lotteryServiceFacade.getLatestOpenInfo(LotteryType.SSQ);
			
			WxMpXmlOutNewsMessage.Item article = new WxMpXmlOutNewsMessage.Item();
			article = new WxMpXmlOutNewsMessage.Item();
			article.setTitle("双色�� +latestOpenInfo.getExpect() + "期开奖公�);
			
			String desc = latestOpenInfo.getOpencode().replaceAll("+", " + ").replaceAll(",", " ");
			desc += "\n\n";
			desc += "开奖时� " + latestOpenInfo.getOpentime();
			article.setDescription(desc);
			article.setPicUrl("");
			article.setUrl("");
			
			WxMpXmlOutMessage.NEWS().toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).addArticle(article).build();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		return response;
	}

	private WxMpXmlOutMessage handleSubscribe(WxMpXmlMessage wxMessage) {
		// 保存用户
		try {
			userService.createUser(wxMessage.getFromUserName(), RoleType.CUSTOMER);
		} catch (ServiceException e) {
			
		}
		
		return null;

	}

}
