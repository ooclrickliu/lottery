package cn.wisdom.lottery.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.model.CurrentPeriodInfo;
import cn.wisdom.lottery.api.model.LotteryJsonDocument;
import cn.wisdom.lottery.api.request.LotteryOrderRequest;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.TicketState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@RequestMapping("/lottery")
@Controller
public class LotteryController {

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;

	///////////////////Customer//////////////////////

    @RequestMapping(method = RequestMethod.GET, value = "/currentPeriod")
    @ResponseBody
	public JsonDocument getCurrentPeriodInfo(@RequestParam String lotteryType)
			throws ServiceException {

		LotteryOpenData currentPeriod = lotteryServiceFacade
				.getCurrentPeriod(LotteryType.valueOf(lotteryType.toUpperCase()));

		CurrentPeriodInfo currentPeriodInfo = new CurrentPeriodInfo(
				currentPeriod);

		return new LotteryJsonDocument(currentPeriodInfo);
	}

    @RequestMapping(method = RequestMethod.POST, value = "/order/create/ssq")
    @ResponseBody
	public JsonDocument submitSSQPrivateOrder(@RequestBody LotteryOrderRequest request)
			throws ServiceException {

		Lottery lottery = new Lottery();
		lottery.setBusinessType(BusinessType.Private);
		lottery.setLotteryType(LotteryType.SSQ);
		lottery.setTimes(request.getTimes()); //倍数
		
		for (String number : request.getNumbers()) {
			lottery.getNumbers().add(new LotteryNumber(number));
		}
		
		//追号
		List<Integer> nextNPeriods = lotteryServiceFacade
				.getNextNPeriods(LotteryType.SSQ, request.getPeriods());
		lottery.setPeriods(nextNPeriods);
		
		lottery.setTicketState(TicketState.UnPaid);

		lottery = lotteryServiceFacade.createPrivateOrder(LotteryType.SSQ,
				lottery);

		return new LotteryJsonDocument(lottery);
	}

    @RequestMapping(method = RequestMethod.POST, value = "/order/paid")
    @ResponseBody
	public JsonDocument onOrderPaid(@RequestParam String orderNo) throws ServiceException {

		User user = SessionContext.getCurrentUser();
		
		lotteryServiceFacade.onPaidSuccess("" + user.getId(), orderNo);
		
		return LotteryJsonDocument.SUCCESS;
	}

    @RequestMapping(method = RequestMethod.GET, value = "/order/detail")
    @ResponseBody
	public JsonDocument viewOrder(@RequestParam String orderNo) throws ServiceException {
		Lottery lottery = lotteryServiceFacade.getLottery(orderNo);

		return new LotteryJsonDocument(lottery);
	}

    @RequestMapping(method = RequestMethod.POST, value = "/order/fetch")
    @ResponseBody
	public JsonDocument fetchTicket(@RequestParam String orderNo) throws ServiceException {
		lotteryServiceFacade.fetchTicket(orderNo);

		return LotteryJsonDocument.SUCCESS;
	}

	////////////////////////Merchant///////////////////////////

    @RequestMapping(method = RequestMethod.POST, value = "/order/print")
    @ResponseBody
	public JsonDocument printTickets(@RequestParam List<String> orderNos) throws ServiceException {
		long userId = SessionContext.getCurrentUser().getId();
		lotteryServiceFacade.printTickets(orderNos, userId);

		return new LotteryJsonDocument();
	}

    @RequestMapping(method = RequestMethod.GET, value = "/query")
    @ResponseBody
	public JsonDocument queryLottery(@RequestParam String lotteryType, 
			@RequestParam int period) throws ServiceException {
		//按中奖金额由大到小排序
		long userId = SessionContext.getCurrentUser().getId();
		List<Lottery> lotteries = lotteryServiceFacade.queryLottery(LotteryType.valueOf(lotteryType), period, userId);
		
		return new LotteryJsonDocument(lotteries);
	}
}
