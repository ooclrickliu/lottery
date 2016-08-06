package cn.wisdom.lottery.service.wx;

import java.text.MessageFormat;
import java.util.List;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
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
			throws WxErrorException {
		WxMpCustomMessage message = WxMpCustomMessage.TEXT()
				.content(content).toUser(openid).build();
		wxService.getWxMpService().customMessageSend(message);
	}
	
	@Override
	public void notifyCustomerPaidSuccess(Lottery lottery)
			throws WxErrorException {
		String content = MessageFormat.format("支付成功!您的投注(金额：{0})已审批通过!",
				creditApply.getAmount());

		User user = userService.getUserById(creditApply.getUserId());
		
		sendTextMessage(content, user.getOpenid());
	}

	@Override
	public void notifyBossNewApply(CreditApply creditApply)
	{

		String content = MessageFormat.format("Boss, 有新借款申请(金额：{0})，请尽快审批!",
				creditApply.getAmount());

		List<String> bossOpenidList = appProperty.getBossOpenidList();
		for (String bossOpenid : bossOpenidList) {
			try {
				sendTextMessage(content, bossOpenid);
			} catch (WxErrorException e) {
				String errMsg = MessageFormat.format("Failed to notify boss [{0}]", bossOpenid);
				LOGGER.error(errMsg, e);
			}
		}
	}

	@Override
	public void notifyUserApplyApproved(CreditApply creditApply) throws WxErrorException {
		String content = MessageFormat.format("您好, 您的借款申请(金额：{0})已审批通过!",
				creditApply.getAmount());

		User user = userService.getUserById(creditApply.getUserId());
		
		sendTextMessage(content, user.getOpenid());
	}

	@Override
	public void notifyUserReturnSuccess(CreditApply apply, CreditPayRecord payRecord) throws WxErrorException {
		
		String content = "";
		
		if (apply.getRemainBase() > 0) {
			content = MessageFormat.format("还款成功！\n\n您借款总额: {0}元 \n应还本金: {1}元 \n利息: {2}元 \n本次还款: {3}元 \n剩余应还本金: {4}元 \n还款时间: {5}",
					apply.getAmount(), payRecord.getCreditBase(), payRecord.getInterest(), 
					payRecord.getReturnedAmount(), apply.getRemainBase(), 
					DateTimeUtils.formatSqlDateTime(payRecord.getReturnTime()));
		}
		else {
			content = MessageFormat.format("还款成功！\n\n您借款总额: {0}元 \n应还本金: {1}元 \n利息: {2}元 \n本次还款: {3}元 \n还款时间: {4} \n\n本单借款已还清!",
					apply.getAmount(), payRecord.getCreditBase(), payRecord.getInterest(), payRecord.getReturnedAmount(),
					DateTimeUtils.formatSqlDateTime(payRecord.getReturnTime()));
		}

		User user = userService.getUserById(apply.getUserId());
		
		sendTextMessage(content, user.getOpenid());
	}

	@Override
	public void notifyUserReturnFailed(CreditApply apply, CreditPayRecord payRecord)
			throws WxErrorException {
		String content = MessageFormat.format("还款失败! \n您于{0} 还款{1}元未成功。",
				DateTimeUtils.formatSqlDateTime(payRecord.getReturnTime()), payRecord.getReturnedAmount());

		User user = userService.getUserById(apply.getUserId());
		
		sendTextMessage(content, user.getOpenid());
	}

}
