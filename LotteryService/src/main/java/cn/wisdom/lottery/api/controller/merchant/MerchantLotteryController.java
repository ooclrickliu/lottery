package cn.wisdom.lottery.api.controller.merchant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.response.LotteryAPIResult;
import cn.wisdom.lottery.api.response.QueryLotteryResponse;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.common.utils.NumberUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@RequestMapping("/lottery/merchant")
@Controller
public class MerchantLotteryController
{

    @Autowired
    private LotteryServiceFacade lotteryServiceFacade;

    @RequestMapping(method = RequestMethod.GET, value = "/query")
    @ResponseBody
    public JsonDocument queryLottery(@RequestParam String lotteryType,
            @RequestParam int period) throws ServiceException
    {
    	QueryLotteryResponse response = new QueryLotteryResponse();
    	
    	// lottery
        long userId = SessionContext.getCurrentUser().getId();
        List<Lottery> lotteries = lotteryServiceFacade.queryLottery(
                LotteryType.valueOf(lotteryType), period, userId);
        response.setLotteries(lotteries);
        
        // open info
        LotteryOpenData openInfo = lotteryServiceFacade.getOpenInfo(LotteryType.valueOf(lotteryType), period);
        response.setOpenInfo(openInfo);
        
        // prize info
        this.summarize(lotteries, response);

        return new LotteryAPIResult(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/print")
    @ResponseBody
    public JsonDocument printTickets(@RequestParam long periodId)
            throws ServiceException
    {
        lotteryServiceFacade.printTicket(periodId);

        return new LotteryAPIResult();
    }

	private void summarize(List<Lottery> lotteries,
			QueryLotteryResponse response) {
		float totalFee = 0;
		float totalPrize = 0;
		Map<String, Integer> prizeMap = new HashMap<String, Integer>();
		for (Lottery lottery : lotteries) {
			totalFee += lottery.getTotalFee();
			
			LotteryPeriod period = lottery.getPeriods().get(0);
			
			totalPrize += period.getPrizeBonus();
			summarizeMap(prizeMap, period.getPrizeMap());
		}
		
		response.setTotalFee(NumberUtils.formatFloat(totalFee));
		response.setTotalPrize(NumberUtils.formatFloat(totalPrize));
		
		response.setPrizeInfo(prizeMap);
	}

	private void summarizeMap(Map<String, Integer> prizeMap,
			Map<String, Integer> lotteryPrizeMap) {
		Integer count = 0;
		if (CollectionUtils.isNotEmpty(lotteryPrizeMap)) {
			for (String rank : lotteryPrizeMap.keySet()) {
				count = prizeMap.get(rank);
				if (count == null) {
					count = 0;
				}
				
				count += lotteryPrizeMap.get(rank);
				
				prizeMap.put(rank, count);
			}
		}
	}
}
