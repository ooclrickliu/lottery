package cn.wisdom.lottery.service.wx;

import cn.wisdom.lottery.dao.vo.Lottery;

public interface MessageNotifier {

	void notifyCustomerPaidSuccess(Lottery lottery, String openid);
	
	void notifyMerchantDistributed(Lottery lottery, String openid);

//	void notifyUserApplyApproved(CreditApply apply) throws WxErrorException;
//
//	void notifyUserReturnSuccess(CreditApply apply, CreditPayRecord payRecord) throws WxErrorException;
//
//	void notifyUserReturnFailed(CreditApply apply, CreditPayRecord payRecord) throws WxErrorException;
}
