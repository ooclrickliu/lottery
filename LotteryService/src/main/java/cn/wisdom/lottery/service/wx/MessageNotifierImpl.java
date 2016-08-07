package cn.wisdom.lottery.service.wx;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage.WxArticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
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
			LOGGER.error("Failed to send text messages", message);
		}
	}
	
	private void sendNewsMessage(WxArticle article, String openid)
	{
		WxMpCustomMessage message = WxMpCustomMessage.NEWS().addArticle(article)
				.toUser(openid).build();
		try {
			wxService.getWxMpService().customMessageSend(message);
		} catch (WxErrorException e) {
			LOGGER.error("Failed to send news messages", message);
		}
	}
	
	@Override
	public void notifyCustomerPaidSuccess(Lottery lottery, String openid)
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
		news.setUrl("http://cai.southwisdom.cn/lottery_detail.html?openid=" + openid);
		
		news.setDescription(descStr);
		
		sendNewsMessage(news, openid);
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
		
		news.setDescription(descStr);
		news.setUrl("http://cai.southwisdom.cn/lottery_detail.html?openid=" + openid);
		
		sendNewsMessage(news, openid);
	}

}
