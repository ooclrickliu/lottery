package cn.wisdom.lottery.service.wx;

import java.util.List;

import cn.wisdom.lottery.api.response.QueryLotteryResponse;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.dao.vo.User;

public interface MessageNotifier {

	void notifyCustomerPaidSuccess(Lottery lottery);

	void notifyCustomerPaidFail(Lottery lottery);
	
	void notifyMerchantDistributed(Lottery lottery, String openid);

	void notifyMerchantPrizeInfo(LotteryType lotteryType, PrizeLotterySSQ openInfo, List<Lottery> prizeLotteries);

	void notifyCustomerPrizeInfo(List<Lottery> prizeLotteries);

	void notifyMerchantNewPayRequest(Lottery lottery);

	void notifyOperatorNewCustomerSubscribed(User customer);

	void notifyMerchantPrintTickets(long merchant, QueryLotteryResponse response);

}
