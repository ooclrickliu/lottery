package cn.wisdom.lottery.service.remote.response;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.DateTimeUtils;
import cn.wisdom.lottery.dao.vo.PrizeLottery;

public class LotteryOpenData {

	private String expect;
	
	private String opencode;
	
	private String opentime;
	
	private String opentimestamp;
	
	public LotteryOpenData() 
	{
		
	}
	
	public LotteryOpenData(PrizeLottery prizeLottery) 
	{
		this.setExpect(DataConvertUtils.toString(prizeLottery.getPeriod()));
		this.setOpentime(DateTimeUtils.formatSqlDateTime(prizeLottery.getOpenTime()));
		this.setOpentimestamp("" + prizeLottery.getOpenTime().getTime());
		this.setOpencode(prizeLottery.getNumber());
	}

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

	public String getOpencode() {
		return opencode;
	}

	public void setOpencode(String opencode) {
		this.opencode = opencode;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getOpentimestamp() {
		return opentimestamp;
	}

	public void setOpentimestamp(String opentimestamp) {
		this.opentimestamp = opentimestamp;
	}
}
