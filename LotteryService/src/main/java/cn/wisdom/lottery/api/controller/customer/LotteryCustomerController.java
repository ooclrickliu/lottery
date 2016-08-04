package cn.wisdom.lottery.api.controller.customer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.request.LotteryOrderRequest;
import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.JaxbUtil;
import cn.wisdom.lottery.common.utils.JaxbUtil.CollectionWrapper;
import cn.wisdom.lottery.dao.constant.BusinessType;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.TicketState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.dao.vo.WxPayLog;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;

@RequestMapping("/lottery/customer")
@Controller
public class LotteryCustomerController
{

    @Autowired
    private LotteryServiceFacade lotteryServiceFacade;
    
    private static JaxbUtil xml2WxPayLog = new JaxbUtil(WxPayLog.class,
            CollectionWrapper.class);

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
                lottery, user.getOpenid(), httpRequest.getHeader("X-Real-IP"),
                tradeType, body);

        return new LotteryAPIResult(wxPayInfoMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/paid")
    @ResponseBody
    public JsonDocument onOrderPaid(@RequestParam long lotteryId)
            throws ServiceException
    {

        User user = SessionContext.getCurrentUser();

        lotteryServiceFacade.onPaidSuccess("" + user.getId(), lotteryId);

        return LotteryAPIResult.SUCCESS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail")
    @ResponseBody
    public JsonDocument viewLottery(@RequestParam long lotteryId)
            throws ServiceException
    {
        Lottery lottery = lotteryServiceFacade.getLottery(lotteryId);

        return new LotteryAPIResult(lottery);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fetch")
    @ResponseBody
    public JsonDocument fetchTicket(@RequestParam long lotteryId)
            throws ServiceException
    {
        lotteryServiceFacade.fetchTicket(lotteryId);

        return LotteryAPIResult.SUCCESS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public JsonDocument getMyLotteries(@RequestParam int limit) 
    		throws ServiceException
    {
        String openId = SessionContext.getCurrentUser().getOpenid();
        List<Lottery> lotteries = lotteryServiceFacade.getLotteries(openId, limit);

        return new LotteryAPIResult(lotteries);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wxnotify")
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request,
            HttpServletResponse response) throws OVTException, IOException
    {

        String xml = StreamUtils.copyToString(request.getInputStream(),
                Charset.defaultCharset());
        WxPayLog wxPayLog = xml2WxPayLog.fromXml(xml);

        if (wxPayLog.getResultCode().equalsIgnoreCase("SUCCESS"))
        {
            
            return "success";
        }
        else
        {
            return "fail";
        }
    }
}