package cn.wisdom.lottery.service.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage.WxArticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.api.response.QueryLotteryResponse;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.UserService;

@Service
public class MessageNotifierImpl implements MessageNotifier {

	@Autowired
	private UserService userService;
	
	@Autowired
	private WXService wxService;

	@Autowired
	private AppProperty appProperty;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageNotifierImpl.class.getName());

	private void sendTextMessage(String content, String openid)
	{
		WxMpCustomMessage message = WxMpCustomMessage.TEXT()
				.content(content).toUser(openid).build();
		try {
			wxService.getWxMpService().customMessageSend(message);
		} catch (WxErrorException e) {
			LOGGER.error("Failed to send text messages", e);
		}
	}
	
	private void sendNewsMessage(WxArticle article, String openid)
	{
		WxMpCustomMessage message = WxMpCustomMessage.NEWS().addArticle(article)
				.toUser(openid).build();
		try {
			wxService.getWxMpService().customMessageSend(message);
		} catch (WxErrorException e) {
			LOGGER.error("Failed to send news messages", e);
		}
	}
	
	@Override
	public void notifyMerchantNewPayRequest(Lottery lottery) {
		WxArticle news = new WxArticle();
		news.setTitle("新支付确认");
		news.setPicUrl(appProperty.imgServerUrl + lottery.getPayImgUrl());

		User customer = userService.getUserById(lottery.getOwner());
		String descStr = "购买人: " + customer.getNickName();
		descStr += "\n应付金额: " + lottery.getTotalFee() + "元";

		news.setUrl("http://cai.southwisdom.cn" + "?openid=" + appProperty.defaultOperator + "#/lottery/" + lottery.getId());

		news.setDescription(descStr);
		
		sendNewsMessage(news, appProperty.defaultOperator);
		
	}
	
	@Override
	public void notifyCustomerPaidSuccess(Lottery lottery)
	{
		WxArticle news = new WxArticle();
		news.setTitle("投注成功");
		news.setPicUrl("");

		LotteryPeriod period = lottery.getPeriods().get(0);
		String descStr = lottery.getLotteryType().getTypeName() + " - " + period.getPeriod() + " 期\n";
		descStr += "倍数: " + lottery.getTimes();
		if (lottery.getPeriods().size() > 1) {
			descStr += "        追号: " + lottery.getPeriods().size() + "期";
		}
		descStr += "\n投注号码:\n";
		
		int num = lottery.getNumbers().size();
		for (int i = 0; i < num && i < 5; i++) { // 最多显示5组号码
			descStr += "    "
					+ lottery.getNumbers().get(i).getNumber().replaceAll(",", " ")
					.replaceAll("\\+", " \\+ ") + "\n";
		}
		if (num > 5) {
			descStr += "    ...";
		}
		User user = userService.getUserById(lottery.getOwner());
		news.setUrl("http://cai.southwisdom.cn" + "?openid=" + user.getOpenid() + "#/lottery/" + lottery.getId());
		
//		descStr += news.getUrl();
		news.setDescription(descStr);
		
		sendNewsMessage(news, user.getOpenid());
	}

	@Override
	public void notifyCustomerPaidFail(Lottery lottery) {
		WxArticle news = new WxArticle();
		news.setTitle("支付失败");
		news.setPicUrl("");

		String descStr = "失败原因可能是: \n";
//		String descStr = lottery.getLotteryType().getTypeName() + " - " + period.getPeriod() + " 期\n";
		descStr += "     1.上传的支付凭证无效\n";
		descStr += "     2.支付金额有误";
		
		User user = userService.getUserById(lottery.getOwner());
		news.setUrl("http://cai.southwisdom.cn" + "?openid=" + user.getOpenid() + "#/lottery/" + lottery.getId());
		
		news.setDescription(descStr);
		
		sendNewsMessage(news, user.getOpenid());
	}

	@Override
	public void notifyMerchantDistributed(Lottery lottery, String openid)
	{
		WxArticle news = new WxArticle();
		news.setTitle("新投注通知");
		news.setPicUrl("");
		
		LotteryPeriod period = lottery.getPeriods().get(0);
		String descStr = lottery.getLotteryType().getTypeName() + " - " + period.getPeriod() + "期\n";
		descStr += "倍数: " + lottery.getTimes();
		if (lottery.getPeriods().size() > 1) {
			descStr += "        追号: " + lottery.getPeriods().size() + "期";
		}
		descStr += "\n投注号码:\n";
		
		for (int i = 0; i < lottery.getNumbers().size(); i++) {
			descStr += "    "
					+ lottery.getNumbers().get(i).getNumber().replaceAll(",", " ")
					.replaceAll("\\+", " \\+ ") + "\n";
		}
		descStr += "\n金额: " + lottery.getTotalFee() + "元";
		
		news.setUrl("http://cai.southwisdom.cn" + "?openid=" + openid + "#/lottery/" + lottery.getId());

//		descStr += news.getUrl();
		news.setDescription(descStr);
		
		sendNewsMessage(news, openid);
	}

	@Override
	public void notifyMerchantPrizeInfo(LotteryType lotteryType, PrizeLotterySSQ openInfo, List<Lottery> prizeLotteries) {
		
		if (CollectionUtils.isNotEmpty(prizeLotteries)) {
			Map<Long, List<Lottery>> prizeLotteryMap = this.groupByMerchant(prizeLotteries);
			for (long merchantId : prizeLotteryMap.keySet()) {
				
				Map<String, Integer> prizeInfoSummary = this.sumPrizeInfo(prizeLotteryMap.get(merchantId));
				
				WxArticle news = this.buildMerchantPrizeNotifyMessage(lotteryType, openInfo, prizeInfoSummary);
				
				User merchant = userService.getUserById(merchantId);
				
				if (merchant != null) {
					sendNewsMessage(news, merchant.getOpenid());
				}
			}
		}
		else {
			sendTextMessage("本期(" + openInfo.getPeriod() + ")无中奖！", appProperty.defaultOperator);
		}

	}

	private WxArticle buildMerchantPrizeNotifyMessage(LotteryType lotteryType, PrizeLotterySSQ openInfo, 
			Map<String, Integer> prizeInfoSummary) {
		WxArticle news = new WxArticle();
		String title = lotteryType.getTypeName() + openInfo.getPeriod() + "期中奖结果";
		news.setTitle(title);
		news.setPicUrl("");
		
		String descStr = "开奖号码: " + openInfo.getNumber() + "\n\n";
		if (CollectionUtils.isEmpty(prizeInfoSummary)) {
			descStr += "本店本期无中奖！";
		}
		else {
			descStr += "中奖结果: ";
			for (String rank : prizeInfoSummary.keySet()) {
				descStr += "\n      " + rank + "等奖: " + prizeInfoSummary.get(rank) + " 注";
			}
		}
		
		news.setDescription(descStr);
		news.setUrl("");
		
		return news;
	}

	private Map<String, Integer> sumPrizeInfo(List<Lottery> prizeLotteries) {
		Map<String, Integer> sumPrizeInfoMap = new HashMap<String, Integer>();
		for (Lottery lottery : prizeLotteries) {
			LotteryPeriod lotteryPeriod = lottery.getPeriods().get(0);
			for (String numberId : lotteryPeriod.getPrizeInfoMap().keySet()) {
				Map<String, Integer> numberPrizeInfoMap = lotteryPeriod.getPrizeInfoMap().get(numberId);
				for (String rank : numberPrizeInfoMap.keySet()) {
					Integer sum = sumPrizeInfoMap.get(rank);
					if (sum == null) {
						sum = 0;
					}
					sum += numberPrizeInfoMap.get(rank);
					sumPrizeInfoMap.put(rank, sum);
				}
			}
		}
		return sumPrizeInfoMap;
	}

	private Map<Long, List<Lottery>> groupByMerchant(
			List<Lottery> prizeLotteries) {
		Map<Long, List<Lottery>> map = new HashMap<Long, List<Lottery>>();
		for (Lottery lottery : prizeLotteries) {
			List<Lottery> prizeList = map.get(lottery.getMerchant());
			if (prizeList == null) {
				prizeList = new ArrayList<Lottery>();
				map.put(lottery.getMerchant(), prizeList);
			}
			prizeList.add(lottery);
		}
		return map;
	}

	@Override
	public void notifyCustomerPrizeInfo(List<Lottery> prizeLotteries) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOperatorNewCustomerSubscribed(User customer) {
		
		sendTextMessage("新用户关注 - " + customer.getNickName(), appProperty.defaultOperator);
	}
	
	@Override
	public void notifyMerchantPrintTickets(long merchant, QueryLotteryResponse response) {
		WxArticle news = new WxArticle();
		String title = LotteryType.SSQ.getTypeName() + response.getOpenInfo().getExpect() + "期销售统计";
		news.setTitle(title);
		news.setPicUrl("");
		
		String descStr = "销售量: " + response.getLotteries().size() + " 张\n";
		descStr = "销售额: " + response.getTotalFee() + " 元\n\n";
		descStr = "请点击查看彩票清单，及时打印并上传彩票！\n";
		
		news.setDescription(descStr);
		
		User merchantObj = userService.getUserById(merchant);
		String url = "http://cai.southwisdom.cn?openid=" + merchantObj.getOpenid() + "#/lottery/list";
		news.setUrl(url);
		
		sendNewsMessage(news, merchantObj.getOpenid());
	}

}
