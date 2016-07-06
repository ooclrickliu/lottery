package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.wisdom.lottery.payment.dao.constant.DBConstants.TABLES.PRIZE_LOTTERY_SSQ;
import cn.wisdom.lottery.payment.dao.vo.PrizeLottery;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;

public class PrizeLotteryMapper {
	
	public class SSQPrizeInfoMapper implements RowMapper<PrizeLotterySSQ> {

		@Override
		public PrizeLotterySSQ mapRow(ResultSet rs, int index)
				throws SQLException {
			PrizeLotterySSQ prizeLotterySSQ = new PrizeLotterySSQ();
			prizeLotterySSQ.setPeriod(rs.getInt(PRIZE_LOTTERY_SSQ.PERIOD));
			prizeLotterySSQ.setOpenTime(rs.getTimestamp(PRIZE_LOTTERY_SSQ.OPEN_TIME));
			prizeLotterySSQ.setCreateTime(rs.getTimestamp(PRIZE_LOTTERY_SSQ.CREATE_TIME));
			prizeLotterySSQ.setUpdateTime(rs.getTimestamp(PRIZE_LOTTERY_SSQ.UPDATE_TIME));
			prizeLotterySSQ.setNumber(rs.getString(PRIZE_LOTTERY_SSQ.NUMBER));
			
			return prizeLotterySSQ;
		}
	}
	
	public class SSQPeriodInfoMapper implements RowMapper<PrizeLottery> {
		
		@Override
		public PrizeLottery mapRow(ResultSet rs, int index)
				throws SQLException {
			PrizeLottery prizeLottery = new PrizeLottery();
			prizeLottery.setPeriod(rs.getInt(PRIZE_LOTTERY_SSQ.PERIOD));
			prizeLottery.setOpenTime(rs.getTimestamp(PRIZE_LOTTERY_SSQ.OPEN_TIME));
			prizeLottery.setCreateTime(rs.getTimestamp(PRIZE_LOTTERY_SSQ.CREATE_TIME));
			prizeLottery.setUpdateTime(rs.getTimestamp(PRIZE_LOTTERY_SSQ.UPDATE_TIME));
			
			return prizeLottery;
		}
	}

}
