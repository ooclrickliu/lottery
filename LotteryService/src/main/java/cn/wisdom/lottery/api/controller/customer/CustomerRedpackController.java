package cn.wisdom.lottery.api.controller.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.model.ReceivedRedpackItem;
import cn.wisdom.lottery.api.model.ReceivedRedpackList;
import cn.wisdom.lottery.api.model.RedpackDetailView;
import cn.wisdom.lottery.api.model.RedpackItemView;
import cn.wisdom.lottery.api.model.SentRedpackItem;
import cn.wisdom.lottery.api.model.SentRedpackList;
import cn.wisdom.lottery.api.request.LotteryOrderRequest;
import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PayState;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.service.exception.ServiceException;

@RequestMapping("/redpack/customer")
@Controller
public class CustomerRedpackController {

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST, value = "/send")
	@ResponseBody
	public JsonDocument shareLotteryAsRedpack(@RequestParam long lotteryId,
			@RequestParam int count, @RequestParam String wish) throws ServiceException {

		lotteryServiceFacade.shareLotteryAsRedpack(lotteryId, count, wish);

		return LotteryAPIResult.SUCCESS;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/q")
	@ResponseBody
	public JsonDocument snatchRedpack(@RequestParam long lotteryId)
			throws ServiceException {
		int rate = lotteryServiceFacade.snatchRedpack(lotteryId);

		return new LotteryAPIResult(rate);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/send/list")
	@ResponseBody
	public JsonDocument getSendRedpackList(HttpServletRequest httpRequest)
			throws ServiceException {
		List<Lottery> lotteries = lotteryServiceFacade.getSentRedpackList();
		
		if (lotteries == null) {
			lotteries = Collections.emptyList();
		}
		
		// wrap the list and add summary info.
		SentRedpackList response = toSentRedpackList(lotteries);
		
		return new LotteryAPIResult(response);
	}
	
	private SentRedpackList toSentRedpackList(List<Lottery> lotteries) {
		SentRedpackList response = new SentRedpackList();
		
		User currentUser = SessionContext.getCurrentUser();
		response.setNickName(currentUser.getNickName());
		response.setHeadImgUrl(currentUser.getHeadImgUrl());
		response.setTotalNum(lotteries.size());
		
		int totalFee = 0;
		for (Lottery lottery : lotteries) {
			totalFee += lottery.getTotalFee();
			
			SentRedpackItem item = toSentRedpackItem(lottery);
			response.getItems().add(item);
		}
		
		response.setTotalFee(totalFee);
		
		return response;
	}

	private SentRedpackItem toSentRedpackItem(Lottery lottery) {
		SentRedpackItem item = new SentRedpackItem();
 		
		item.setCount(lottery.getRedpackCount());
		item.setqCount(lottery.getSnatchedNum());
		item.setFee((int) lottery.getTotalFee());
		item.setPrizeState(lottery.getPeriods().get(0).getPrizeState());
		item.setType(lottery.getBusinessType().name());
		item.setSendTime(lottery.getSendTime());
		
		return item;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/q/list")
	@ResponseBody
	public JsonDocument getReceivedRedpackList(HttpServletRequest httpRequest)
			throws ServiceException {
		List<Lottery> lotteries = lotteryServiceFacade.getReceivedRedpackList();

		if (lotteries == null) {
			lotteries = Collections.emptyList();
		}
		
		ReceivedRedpackList response = toReceivedRedpackList(lotteries);
		
		return new LotteryAPIResult(response);
	}

	private ReceivedRedpackList toReceivedRedpackList(List<Lottery> lotteries) {
		ReceivedRedpackList response = new ReceivedRedpackList();

		User currentUser = SessionContext.getCurrentUser();
		response.setNickName(currentUser.getNickName());
		response.setHeadImgUrl(currentUser.getHeadImgUrl());
		response.setTotalNum(lotteries.size());
		
		// user map
		List<Long> userIds = new ArrayList<Long>();
		for (Lottery lottery : lotteries) {
			userIds.add(lottery.getOwner());
		}
		List<User> userList = userService.getUserByIdList(userIds);
		Map<Long, User> userMap = new HashMap<Long, User>();
		for (User user : userList) {
			userMap.put(user.getId(), user);
		}
		
		// item
		int totalBonus = 0;
		for (Lottery lottery : lotteries) {
			LotteryRedpack myRedpack = findMyRedpack(lottery.getRedpacks(), currentUser);
			totalBonus += myRedpack.getPrizeBonus();
			
			ReceivedRedpackItem item = toReceivedRedpackItem(lottery, myRedpack, userMap);
			response.getItems().add(item);
		}
		
		response.setTotalBonus(totalBonus);;
		return response;
	}

	private ReceivedRedpackItem toReceivedRedpackItem(Lottery lottery,
			LotteryRedpack myRedpack, Map<Long, User> userMap) {
		ReceivedRedpackItem item = new ReceivedRedpackItem();
		item.setAcquireTime(myRedpack.getAcquireTime());
		item.setBonus(myRedpack.getPrizeBonus());
		item.setPrizeState(lottery.getPeriods().get(0).getPrizeState());
		item.setRate(myRedpack.getRate());
		
		User sender = userMap.get(lottery.getOwner());
		item.setSenderName(sender.getNickName());
		
		return item;
	}

	private LotteryRedpack findMyRedpack(List<LotteryRedpack> redpacks, User user) {
		for (LotteryRedpack lotteryRedpack : redpacks) {
			if (user.getId() == lotteryRedpack.getUserId()) {
				return lotteryRedpack;
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/detail")
	@ResponseBody
	public JsonDocument viewLottery(@RequestParam long lotteryId)
			throws ServiceException {
		RedpackDetailView response = new RedpackDetailView();
		
		Lottery lottery = lotteryServiceFacade.getLottery(lotteryId);
		if (!BusinessType.isRedpack(lottery.getBusinessType())) {
			throw new ServiceException(ServiceErrorCode.ERROR_BUSINESS_TYPE, "Not public lottery!");
		}

		addSender(response, lottery);
		
		response.setWish(lottery.getWish());
		response.setRedpackNum(lottery.getRedpackCount());
		response.setTotalFee(lottery.getTotalFee());
		response.setNumbers(lottery.getNumbers());
		response.setPeriod(lottery.getPeriods().get(0));
		
		addRedpackItems(response, lottery);

		return new LotteryAPIResult(response);
	}

	private void addRedpackItems(RedpackDetailView response, Lottery lottery) {
		if (CollectionUtils.isNotEmpty(lottery.getRedpacks())) {
			List<Long> userIds = new ArrayList<Long>();
			int maxRate = 0;
			for (LotteryRedpack redpack : lottery.getRedpacks()) {
				userIds.add(redpack.getUserId());
				if (redpack.getRate() > maxRate) {
					maxRate = redpack.getRate();
				}
			}
			List<User> userList = userService.getUserByIdList(userIds);
			Map<Long, User> userMap = new HashMap<Long, User>();
			for (User user : userList) {
				userMap.put(user.getId(), user);
			}
			
			RedpackItemView redpackItemView;
			User user;
			for (LotteryRedpack redpack : lottery.getRedpacks()) {
				user = userMap.get(redpack.getUserId());
				redpackItemView = new RedpackItemView();
				redpackItemView.setRate(redpack.getRate());
				redpackItemView.setAcquireTime(redpack.getAcquireTime());
				redpackItemView.setUserName(user.getNickName());
				redpackItemView.setHeadImgUrl(user.getHeadImgUrl());
				redpackItemView.setBonus(redpack.getPrizeBonus());
				if (redpack.getRate() == maxRate) {
					redpackItemView.setBest(true);
				}
				
				response.getRedpackItems().add(redpackItemView);
			}
		}
	}

	private void addSender(RedpackDetailView response, Lottery lottery) {
		User currentUser = SessionContext.getCurrentUser();
		User sender;
		if (lottery.getOwner() == currentUser.getId()) {
			sender = currentUser;
		}
		else {
			sender = userService.getUserById(lottery.getOwner());
		}
		response.setSenderName(sender.getNickName());
		response.setHeadImgUrl(sender.getHeadImgUrl());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/ssq/create")
	@ResponseBody
	public JsonDocument createSSQRedpackLottery(HttpServletRequest httpRequest,
			@RequestBody LotteryOrderRequest request,
			@RequestParam String tradeType, @RequestParam String body)
			throws ServiceException {
		User user = SessionContext.getCurrentUser();

		Lottery lottery = new Lottery();
		lottery.setCreateBy(SessionContext.getCurrentUser().getId());
		lottery.setOwner(lottery.getCreateBy());
		lottery.setBusinessType(BusinessType.RedPack_Bonus);
		lottery.setLotteryType(LotteryType.SSQ);
		lottery.setPayState(PayState.UnPaid);
		lottery.setTimes(request.getTimes()); // 倍数
		lottery.setRedpackCount(request.getRedpackCount());

		for (String number : request.getNumbers()) {
			lottery.getNumbers().add(new LotteryNumber(number));
		}

		//
		lottery.setPeriodNum(1);
		List<PrizeLotterySSQ> period = lotteryServiceFacade.getNextNPeriods(
				LotteryType.SSQ, 1);
		LotteryPeriod lotteryPeriod = new LotteryPeriod();
		lotteryPeriod.setPeriod(period.get(0).getPeriod());
		lotteryPeriod.setPrizeState(PrizeState.NotOpen);
		lotteryPeriod.setPrizeOpenTime(period.get(0).getOpenTime());

		lottery.getPeriods().add(lotteryPeriod);

		lottery = lotteryServiceFacade.createLottery(LotteryType.SSQ, lottery);

		Map<String, String> wxPayInfoMap = lotteryServiceFacade.unifiedOrder(
				lottery, user.getOpenid(), httpRequest.getHeader("X-Real-IP"),
				tradeType, body);

		return new LotteryAPIResult(wxPayInfoMap);
	}

}
