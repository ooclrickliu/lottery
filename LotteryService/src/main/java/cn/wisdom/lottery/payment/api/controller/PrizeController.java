package cn.wisdom.lottery.payment.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.wisdom.lottery.payment.api.model.LotteryJsonDocument;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.service.SSQPeriodGenerator;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

@RequestMapping("/prize")
@Controller
public class PrizeController {

	@Autowired
	private SSQPeriodGenerator ssqPeriodGenerator;
	
    @RequestMapping(method = RequestMethod.GET, value = "/generate/period")
	public JsonDocument generateSSQPeriods(int year, String exceptFrom, String exceptTo)
			throws ServiceException {

		Date exceptFromDate = DateTimeUtils.parseDate(exceptFrom, DateTimeUtils.PATTERN_SQL_DATE);
		Date exceptToDate = DateTimeUtils.parseDate(exceptTo, DateTimeUtils.PATTERN_SQL_DATE);;
		ssqPeriodGenerator.generatePeriod(year, exceptFromDate, exceptToDate);

		return LotteryJsonDocument.SUCCESS;
	}
}
