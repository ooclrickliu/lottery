package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.wisdom.lottery.payment.dao.constant.DBConstants.TABLES.LOTTERY_NUMBER;
import cn.wisdom.lottery.payment.dao.vo.LotteryNumber;

public class LotteryNumberMapper implements RowMapper<LotteryNumber> {

	@Override
	public LotteryNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
		LotteryNumber lotteryNumber = new LotteryNumber(rs.getString(LOTTERY_NUMBER.NUMBER));
		lotteryNumber.setId(rs.getLong(LOTTERY_NUMBER.ID));
		lotteryNumber.setLotteryId(rs.getLong(LOTTERY_NUMBER.LOTTERY_ID));
		
		return lotteryNumber;
	}

}
