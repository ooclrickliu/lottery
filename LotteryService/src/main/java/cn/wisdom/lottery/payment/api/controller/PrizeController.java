package cn.wisdom.lottery.payment.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.payment.api.model.LotteryJsonDocument;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.service.SSQPeriodGenerator;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.task.LotteryOpenPrizeTask;

@RequestMapping("/prize")
@Controller
public class PrizeController {

	@Autowired
	private SSQPeriodGenerator ssqPeriodGenerator;
	
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/latest")
	@ResponseBody
	public JsonDocument getRemoteSSQLatestOpenInfo()
					throws ServiceException {
		
		lotteryOpenPrizeTask.openSSQPrize();
		
		return LotteryJsonDocument.SUCCESS;
	}
}
