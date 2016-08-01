package cn.wisdom.lottery.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.api.model.LatestOpenInfo;
import cn.wisdom.lottery.api.model.LotteryJsonDocument;
import cn.wisdom.lottery.common.model.JsonDocument;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.SSQPeriodGenerator;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.service.task.LotteryOpenPrizeTask;

@RequestMapping("/prize")
@Controller
public class PrizeController {

	@Autowired
	private SSQPeriodGenerator ssqPeriodGenerator;

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private LotteryOpenPrizeTask lotteryOpenPrizeTask;

	@RequestMapping(method = RequestMethod.GET, value = "/generate/period")
	@ResponseBody
	public JsonDocument generateSSQPeriods(@RequestParam int year,
			@RequestParam String exceptFrom, @RequestParam String exceptTo)
			throws ServiceException {

		Date exceptFromDate = DateTimeUtils.parseDate(exceptFrom,
				DateTimeUtils.PATTERN_SQL_DATE);
		Date exceptToDate = DateTimeUtils.parseDate(exceptTo,
				DateTimeUtils.PATTERN_SQL_DATE);
		;
		ssqPeriodGenerator.generatePeriod(year, exceptFromDate, exceptToDate);

		return LotteryJsonDocument.SUCCESS;
	}

    @RequestMapping(method = RequestMethod.GET, value = "/lastOpen")
    @ResponseBody
	public JsonDocument getLatestOpenInfo(@RequestParam String lotteryType)
			throws ServiceException {

		LotteryOpenData openInfo = lotteryServiceFacade
				.getLatestOpenInfo(LotteryType.valueOf(lotteryType.toUpperCase()));

		LatestOpenInfo latestOpenInfo = new LatestOpenInfo(openInfo);

		return new LotteryJsonDocument(latestOpenInfo);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/open")
	@ResponseBody
	public JsonDocument getRemoteSSQLatestOpenInfo()
					throws ServiceException {
		
		lotteryOpenPrizeTask.openSSQPrize();
		
		return LotteryJsonDocument.SUCCESS;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/reopen")
	@ResponseBody
	public JsonDocument getRemoteSSQLatestOpenInfo(@RequestParam int period)
			throws ServiceException {
		
		lotteryOpenPrizeTask.reopenSSQPrize(period);
		
		return LotteryJsonDocument.SUCCESS;
	}
}
