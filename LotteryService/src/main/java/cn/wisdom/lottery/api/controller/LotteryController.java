package cn.wisdom.lottery.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.model.CurrentPeriodInfo;
import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@RequestMapping("/lottery")
@Controller
public class LotteryController
{

    @Autowired
    private LotteryServiceFacade lotteryServiceFacade;
    
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
}
