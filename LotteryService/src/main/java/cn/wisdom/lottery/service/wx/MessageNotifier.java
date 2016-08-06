package cn.wisdom.lottery.service.wx;

import me.chanjar.weixin.common.exception.WxErrorException;
import cn.wisdom.dao.vo.CreditApply;
import cn.wisdom.dao.vo.CreditPayRecord;
import cn.wisdom.lottery.dao.vo.Lottery;

public interface MessageNotifier {

	void notifyCustomerPaidSuccess(Lottery lottery) throws WxErrorException;

	void notifyUserApplyApproved(CreditApply apply) throws WxErrorException;

	void notifyUserReturnSuccess(CreditApply apply, CreditPayRecord payRecord) throws WxErrorException;

	void notifyUserReturnFailed(CreditApply apply, CreditPayRecord payRecord) throws WxErrorException;
}
