package cn.wisdom.lottery.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.model.CurrentPeriodInfo;
import cn.wisdom.lottery.api.request.LotteryOrderRequest;
import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.JsonUtils;
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
public class LotteryController
{

    @Autowired
    private LotteryServiceFacade lotteryServiceFacade;

    // /////////////////Customer//////////////////////

    @RequestMapping(method = RequestMethod.GET, value = "/period/current")
    @ResponseBody
    public JsonDocument getCurrentPeriodInfo(@RequestParam String lotteryType)
            throws ServiceException
    {

        LotteryOpenData currentPeriod = lotteryServiceFacade
                .getCurrentPeriod(LotteryType.valueOf(lotteryType.toUpperCase()));

        CurrentPeriodInfo currentPeriodInfo = new CurrentPeriodInfo(
                currentPeriod);

        return new LotteryAPIResult(currentPeriodInfo);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ssq/create")
    @ResponseBody
    public JsonDocument createSSQLottery(HttpServletRequest httpRequest,
            @RequestBody LotteryOrderRequest request,
            @RequestParam String tradeType, @RequestParam String body)
            throws ServiceException
    {
        User user = SessionContext.getCurrentUser();

        Lottery lottery = new Lottery();
        lottery.setBusinessType(BusinessType.Private);
        lottery.setLotteryType(LotteryType.SSQ);
        lottery.setTimes(request.getTimes()); // 倍数

        for (String number : request.getNumbers())
        {
            lottery.getNumbers().add(new LotteryNumber(number));
        }

        // 追号
        List<Integer> nextNPeriods = lotteryServiceFacade.getNextNPeriods(
                LotteryType.SSQ, request.getPeriods());
        lottery.setPeriods(nextNPeriods);
        lottery.setTicketState(TicketState.UnPaid);

        lottery = lotteryServiceFacade.createLottery(LotteryType.SSQ, lottery);

        Map<String, String> wxPayInfoMap = lotteryServiceFacade.unifiedOrder(
                lottery, user.getOpenid(), httpRequest.getRemoteAddr(),
                tradeType, body);

        return new LotteryAPIResult(wxPayInfoMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/paid")
    @ResponseBody
    public JsonDocument onOrderPaid(@RequestParam String orderNo)
            throws ServiceException
    {

        User user = SessionContext.getCurrentUser();

        lotteryServiceFacade.onPaidSuccess("" + user.getId(), orderNo);

        return LotteryAPIResult.SUCCESS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail")
    @ResponseBody
    public JsonDocument viewLottery(@RequestParam String orderNo)
            throws ServiceException
    {
        Lottery lottery = lotteryServiceFacade.getLottery(orderNo);

        return new LotteryAPIResult(lottery);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fetch")
    @ResponseBody
    public JsonDocument fetchTicket(@RequestParam String orderNo)
            throws ServiceException
    {
        lotteryServiceFacade.fetchTicket(orderNo);

        return LotteryAPIResult.SUCCESS;
    }

    // //////////////////////Merchant///////////////////////////

    @RequestMapping(method = RequestMethod.POST, value = "/print")
    @ResponseBody
    public JsonDocument printTickets(@RequestParam List<String> orderNos)
            throws ServiceException
    {
        long userId = SessionContext.getCurrentUser().getId();
        lotteryServiceFacade.printTickets(orderNos, userId);

        return new LotteryAPIResult();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/query")
    @ResponseBody
    public JsonDocument queryLottery(@RequestParam String lotteryType,
            @RequestParam int period) throws ServiceException
    {
        // 按中奖金额由大到小排序
        long userId = SessionContext.getCurrentUser().getId();
        List<Lottery> lotteries = lotteryServiceFacade.queryLottery(
                LotteryType.valueOf(lotteryType), period, userId);

        return new LotteryAPIResult(lotteries);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wxnotify")
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request,
            HttpServletResponse response) throws OVTException
    {
        System.out.println(JsonUtils.toJson(request.getParameterMap()));
        return "failed";
    }
}
