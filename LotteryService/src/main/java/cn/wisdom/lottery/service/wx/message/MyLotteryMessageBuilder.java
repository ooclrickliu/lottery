package cn.wisdom.lottery.service.wx.message;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@Component
public class MyLotteryMessageBuilder implements MessageBuilder {
	
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
			Lottery lottery = lotteryServiceFacade.getMyLatestLottery(wxMessage
					.getFromUserName());

			if (lottery != null) {
				if (lottery.getPeriods().size() == 1) {
					response = build1PeriodResponse(wxMessage, lottery);
				} else if (lottery.getPeriods().size() > 1) {
					response = buildMultiPeriodsResponse(wxMessage, lottery);
				}
			} else {
				response = WxMpXmlOutMessage.TEXT().content("您没有购买记录!")
						.toUser(wxMessage.getFromUserName())
						.fromUser(wxMessage.getToUserName()).build();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
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
		
		String url = "http://cai.southwisdom.cn?openid=" + wxMessage.getFromUserName() + "#/lottery/list";
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
		
		String url = "http://cai.southwisdom.cn?openid=" + wxMessage.getFromUserName() + "#/lottery/list";
		news.setUrl(url);
		
		response = builder.addArticle(news).build();
		return response;
	}

	@Override
	public String getMenuKey() {
		return "my_lottery";
	}
}
