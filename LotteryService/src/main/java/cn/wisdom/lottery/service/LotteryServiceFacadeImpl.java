package cn.wisdom.lottery.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.api.response.CheckRedpackStateResponse;
import cn.wisdom.lottery.api.response.ValidRedpackLottery;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;
import cn.wisdom.lottery.dao.vo.PageInfo;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@Service
public class LotteryServiceFacadeImpl implements LotteryServiceFacade {

	@Autowired
	private LotteryService lotteryService;

	@Autowired
	private UserService userService;

	@Autowired
	private LotteryPrizeService lotteryPrizeService;

	@Autowired
	private LotteryWxPayService lotteryWxPayService;

	@Override
	public LotteryOpenData getCurrentPeriod(LotteryType lotteryType)
			throws ServiceException {
		return lotteryPrizeService.getCurrentPeriod(lotteryType);
	}

	@Override
	public List<PrizeLotterySSQ> getNextNPeriods(LotteryType lotteryType, int n)
			throws ServiceException {
		return lotteryPrizeService.getNextNPeriods(lotteryType, n);
	}

	@Override
	public Lottery createLottery(LotteryType ssq, Lottery lottery)
			throws ServiceException {
		return lotteryService.createLottery(lottery);
	}

	@Override
	public LotteryOpenData getLatestOpenInfo(LotteryType lotteryType)
			throws ServiceException {

		return lotteryPrizeService.getLatestOpenInfo(lotteryType);
	}

	@Override
	public LotteryOpenData getOpenInfo(LotteryType lotteryType, int period)
			throws ServiceException {

		return lotteryPrizeService.getOpenInfo(lotteryType, period);
	}

	@Override
	public Lottery getLottery(long lotteryId) throws ServiceException {

		return lotteryService.getLottery(lotteryId);
	}

	@Override
	public Lottery getLottery(long lotteryId, boolean queryNumber,
			boolean queryPeriod, boolean queryRedpack) throws ServiceException {

		return lotteryService.getLottery(lotteryId, queryNumber, queryPeriod,
				queryRedpack);
	}

	@Override
	public Lottery getMyLatestLottery(String openid) throws ServiceException {
		User user = userService.getUserByOpenId(openid);

		return lotteryService.getMyLatestLottery(user.getId());
	}

	@Override
	public List<Lottery> getLotteries(long owner, PageInfo pageInfo)
			throws ServiceException {

		return lotteryService.getLotteries(owner, pageInfo);
	}

	@Override
	public void onPaidSuccess(String orderNo, String openid)
			throws ServiceException {
		lotteryService.onPaidSuccess(orderNo, openid);
	}

	@Override
	public void fetchTicket(long periodId) throws ServiceException {
		long userId = SessionContext.getCurrentUser().getId();
		lotteryService.fetchTicket(userId, periodId);
	}

	// //////////////////////////////////
	@Override
	public void confirmPay(long lotteryId) {
		lotteryService.confirmPay(lotteryId);
	}

	@Override
	public void confirmPayFail(long lotteryId) {
		lotteryService.confirmPayFail(lotteryId);
	}

	@Override
	public void printTicket(long periodId) throws ServiceException {
		lotteryService.printTicket(periodId);
	}

	@Override
	public String uploadTicket(long periodId, String ticketImgUrl) {
		return lotteryService.uploadTicket(periodId, ticketImgUrl);
	}

	@Override
	public List<Lottery> queryLottery(LotteryType lotteryType, int period,
			long merchantId) throws ServiceException {
		return lotteryService.queryLottery(lotteryType, period, merchantId);
	}

	@Override
	public Lottery getLotteryByPeriod(long periodId) {

		return lotteryService.getLotteryByPeriod(periodId);
	}

	@Override
	public void savePrizeLottery(PrizeLotterySSQ prizeLotterySSQ,
			LotteryType lotteryType) {

		lotteryPrizeService.savePrizeLottery(prizeLotterySSQ, lotteryType);
	}

	@Override
	public List<Lottery> getPaidLotteries(LotteryType lotteryType, int period)
			throws ServiceException {

		return lotteryService.getPaidLotteries(lotteryType, period);
	}

	// @Override
	// public List<Lottery> getPrintedLotteries(LotteryType lotteryType, int
	// period)
	// throws ServiceException
	// {
	// return lotteryService.getPrintedLotteries(lotteryType, period);
	// }

	@Override
	public Map<String, Map<String, Integer>> getPrizeInfo(Lottery lottery,
			PrizeLotterySSQ prizeLotterySSQ) throws ServiceException {
		return lotteryPrizeService.getPrizeInfo(lottery, prizeLotterySSQ);
	}

	@Override
	public int getPrizeBonus(Map<String, Map<String, Integer>> prizeInfo) {
		return lotteryPrizeService.getPrizeBonus(prizeInfo);
	}

	@Override
	public void updatePrizeState(int period, PrizeState prizeState) {

		lotteryService.updatePrizeState(period, prizeState);
	}

	@Override
	public void updatePrizeInfo(List<LotteryPeriod> prizeLotteries, List<LotteryRedpack> prizeLotteryRedpacks) {
		lotteryService.updatePrizeInfo(prizeLotteries, prizeLotteryRedpacks);
	}

	@Override
	public Map<String, String> unifiedOrder(Lottery lottery, String openId,
			String spbillCreateIp, String tradeType, String body)
			throws ServiceException {
		return lotteryWxPayService.unifiedOrder(lottery, openId,
				spbillCreateIp, tradeType, body);
	}

	@Override
	public void shareLotteryAsRedpack(long lotteryId, int count, String wish) throws ServiceException {
		lotteryService.shareLotteryAsRedpack(lotteryId, count, wish);
	}

	@Override
	public List<Lottery> getSentRedpackList() {
		User user = SessionContext.getCurrentUser();
		
		List<Lottery> lotteries = lotteryService.getSentRedpackList(user.getId());
		
		return lotteries;
	}

	@Override
	public List<Lottery> getReceivedRedpackList() {
		User user = SessionContext.getCurrentUser();
		
		List<Lottery> lotteries = lotteryService.getReceivedRedpackList(user.getId());
		
		return lotteries;
	}
	
	@Override
	public CheckRedpackStateResponse checkRedpackState(long lotteryId, long userId) throws ServiceException {
		
		return lotteryService.checkRedpackState(lotteryId, userId);
	}
	
	@Override
	public List<ValidRedpackLottery> getValidRedpackLotteries(long userId) throws ServiceException {
		return lotteryService.getValidRedpackLotteries(userId);
	}

	@Override
	public int snatchRedpack(long lotteryId) throws ServiceException {
		return lotteryService.snatchRedpack(lotteryId);
	}

	@Override
	public void calculateRedpacksPrize(Lottery lottery) {
		lotteryPrizeService.calculateRedpacksPrize(lottery);
	}

	@Override
	public String submitPayRequest(long lotteryId, String payImgUrl) {

		return lotteryService.submitPayRequest(lotteryId, payImgUrl);

	}
	
	@Override
	public void forgetSubmitPayRequest(String openId) {
		lotteryService.forgetSubmitPayRequest(openId);
	}

	@Override
	public List<Lottery> getUnPaidLotteries(long owner) {

		return lotteryService.getUnPaidLotteries(owner);
	}

	@Override
	public void deleteLottery(long owner, long lotteryId) {
		lotteryService.deleteLottery(owner, lotteryId);
	}

	@Override
	public void clearUnpaidLottery() {
		lotteryService.clearUnpaidLottery();
	}
	
	@Override
	public void transferMerchant(int period, long fromMerchant, long toMerchant) {
		
		lotteryService.transferMerchant(period, fromMerchant, toMerchant);
	}
}
