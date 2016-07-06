package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.wisdom.lottery.payment.dao.constant.DBConstants.TABLES.LOTTERY_PERIOD;
import cn.wisdom.lottery.payment.dao.vo.LotteryPeriod;

public class LotteryPeriodMapper implements RowMapper<LotteryPeriod> {

	@Override
	public LotteryPeriod mapRow(ResultSet rs, int rowNum) throws SQLException {
		LotteryPeriod lotteryPeriod = new LotteryPeriod();
		lotteryPeriod.setId(rs.getLong(LOTTERY_PERIOD.ID));
		lotteryPeriod.setLotteryId(rs.getLong(LOTTERY_PERIOD.LOTTERY_ID));
		lotteryPeriod.setPeriod(rs.getInt(LOTTERY_PERIOD.PERIOD));
		return lotteryPeriod;
	}

}
