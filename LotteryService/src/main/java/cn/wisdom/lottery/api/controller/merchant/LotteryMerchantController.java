package cn.wisdom.lottery.api.controller.merchant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;

@RequestMapping("/lottery/merchant")
@Controller
public class LotteryMerchantController
{

    @Autowired
    private LotteryServiceFacade lotteryServiceFacade;

//    @RequestMapping(method = RequestMethod.POST, value = "/print")
//    @ResponseBody
//    public JsonDocument printTickets(@RequestParam List<Long> periodIds)
//            throws ServiceException
//    {
//        long userId = SessionContext.getCurrentUser().getId();
//        lotteryServiceFacade.printTickets(periodIds, userId);
//
//        return new LotteryAPIResult();
//    }

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
}
