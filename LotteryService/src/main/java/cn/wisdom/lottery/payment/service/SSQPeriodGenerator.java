package cn.wisdom.lottery.payment.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.dao.DaoHelper;

@Service
public class SSQPeriodGenerator {

	@Autowired
	private DaoHelper daoHelper;
	
	private static final String INSERT_SSQ_PERIOD = "insert ignore into prize_lottery_ssq(period, open_time, update_time) "
			+ "values(?, ?, current_timestamp)";
	
	public static void main(String[] args) {
		SSQPeriodGenerator generator = new SSQPeriodGenerator();
		
		Date exceptFrom = DateTimeUtils.parseDate("2015-02-18", DateTimeUtils.PATTERN_SQL_DATE);
		Date exceptTo = DateTimeUtils.parseDate("2015-02-25", DateTimeUtils.PATTERN_SQL_DATE);;
		generator.generatePeriod(2015, exceptFrom, exceptTo);
	}
	
	@SuppressWarnings("deprecation")
	public void generatePeriod(int year, Date exceptFrom, Date exceptTo) {
		Date yearStart = DateTimeUtils.getYearStart(DateTimeUtils.parseDate(""+year, DateTimeUtils.PATTERN_YEAR));
		yearStart.setHours(19);
		
		Date nextYearStart = DateTimeUtils.addYears(yearStart, 1);

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		Object[] args;
		
		Date date = yearStart;
		int period = year * 1000;
		while (date.before(nextYearStart)) {
			if((date.getDay() == 0 || date.getDay() == 2 ||date.getDay() == 4) && !between(date, exceptFrom, exceptTo))
			{
				period++;
				
				args = new Object[2];
				args[0] = period;
				args[1] = date;
				
				batchArgs.add(args);
//				System.out.println(period + "  -  " + DateTimeUtils.formatSqlDate(date));
			}
			
			date = DateTimeUtils.addDays(date, 1);
		}
		
		String errMsg = MessageFormat.format("Failed to genearte period for year [{0}]", year);
		daoHelper.batchUpdate(INSERT_SSQ_PERIOD, batchArgs, errMsg);
	}

	private boolean between(Date date, Date exceptFrom, Date exceptionTo) {
		
		return date.after(exceptFrom) && date.before(exceptionTo);
	}
	
}
