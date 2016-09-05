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

import cn.wisdom.lottery.api.model.RedpackItemView;
import cn.wisdom.lottery.api.model.RedpackListView;
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

		// TODO: add wish field
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
		
		// TODO: wrap the list and add summary info.
		
		return new LotteryAPIResult(lotteries);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/q/list")
	@ResponseBody
	public JsonDocument getReceivedRedpackList(HttpServletRequest httpRequest)
			throws ServiceException {
		List<Lottery> lotteries = lotteryServiceFacade.getReceivedRedpackList();

		if (lotteries == null) {
			lotteries = Collections.emptyList();
		}
		
		// TODO: wrap the list and add summary info.
		
		return new LotteryAPIResult(lotteries);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/detail")
	@ResponseBody
	public JsonDocument viewLottery(@RequestParam long lotteryId)
			throws ServiceException {
		RedpackListView response = new RedpackListView();
		
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
				if (redpack.getRate() == maxRate) {
					redpackItemView.setBest(true);
				}
				
				response.getRedpackItems().add(redpackItemView);
			}
		}

		return new LotteryAPIResult(response);
	}

	private void addSender(RedpackListView response, Lottery lottery) {
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
