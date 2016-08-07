package cn.wisdom.lottery.service;

import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

public interface LotteryServiceFacade
{

    // /////////Customer///////////

    LotteryOpenData getCurrentPeriod(LotteryType lotteryType)
            throws ServiceException;

    List<Integer> getNextNPeriods(LotteryType lotteryType, int n)
            throws ServiceException;

    Lottery createLottery(LotteryType ssq, Lottery lottery)
            throws ServiceException;

    LotteryOpenData getLatestOpenInfo(LotteryType lotteryType)
            throws ServiceException;

    LotteryOpenData getOpenInfo(LotteryType lotteryType, int period)
            throws ServiceException;

    void onPaidSuccess(String orderNo, String openid) throws ServiceException;

    Lottery getLottery(long lotteryId) throws ServiceException;

    void fetchTicket(long periodId) throws ServiceException;

    Lottery getMyLatestLottery(String openid) throws ServiceException;
    
    List<Lottery> getLotteries(long owner) throws ServiceException;

    // /////////Merchant///////////

//    void printTickets(List<Long> lotteryIds, long merchantId)
//            throws ServiceException;

    List<Lottery> queryLottery(LotteryType lotteryType, int period,
            long merchantId) throws ServiceException;

    // /////////Other////////////////
    void savePrizeLottery(PrizeLotterySSQ prizeLotterySSQ,
            LotteryType lotteryType) throws ServiceException;

    List<Lottery> getPaidLotteries(LotteryType lotteryType, int period)
            throws ServiceException;
    
//    List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period)
//    		throws ServiceException;

    Map<Long, Map<Integer, Integer>> getPrizeInfo(Lottery lottery,
            PrizeLotterySSQ prizeLotterySSQ) throws ServiceException;

    int getPrizeBonus(Map<Long, Map<Integer, Integer>> prizeInfo);

    void updatePrizeInfo(List<LotteryPeriod> prizeLotteries);

    // /////////WxPay////////////////
    Map<String, String> unifiedOrder(Lottery lottery, String openId,
            String spbillCreateIp, String tradeType, String body)
            throws ServiceException;

    // /////////Task////////////////
	void updatePrizeState(int period, PrizeState prizeState);
}
