package cn.wisdom.lottery.dao.vo;

import java.sql.Timestamp;

import cn.wisdom.lottery.dao.annotation.Column;
import cn.wisdom.lottery.dao.constant.PrizeState;

public class LotteryPeriod extends BaseEntity {
	
	@Column("lottery_id")
	private long lotteryId;

	@Column("period")
	private int period;

    @Column("prize_state")
    private String prizeStateValue;
    private PrizeState prizeState;

    @Column("ticket_print_time")
    private Timestamp ticketPrintTime;

    @Column("ticket_fetch_time")
    private Timestamp ticketFetchTime;

    @Column("prize_info")
    private String prizeInfo;

    @Column("prize_bonus")
    private int prizeBonus;

    private boolean fetched;

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

    public PrizeState getPrizeState()
    {
        if (prizeState == null)
        {
        	prizeState = PrizeState.valueOf(prizeStateValue);
        }

        return prizeState;
    }

    public void setPrizeState(PrizeState prizeState)
    {
        this.prizeState = prizeState;
        this.prizeStateValue = prizeState.toString();
    }

    public Timestamp getTicketPrintTime()
    {
        return ticketPrintTime;
    }

    public void setTicketPrintTime(Timestamp ticketPrintTime)
    {
        this.ticketPrintTime = ticketPrintTime;
    }

    public int getPrizeBonus()
    {
        return prizeBonus;
    }

    public void setPrizeBonus(int prizeBonus)
    {
        this.prizeBonus = prizeBonus;
    }

    public Timestamp getTicketFetchTime()
    {
        return ticketFetchTime;
    }

    public void setTicketFetchTime(Timestamp ticketFetchTime)
    {
        this.ticketFetchTime = ticketFetchTime;
        this.fetched = true;
    }

    public boolean isFetched()
    {
        if (!fetched && ticketFetchTime != null)
        {
            fetched = true;
        }

        return fetched;
    }

    public void setFetched(boolean fetched)
    {
        this.fetched = fetched;
    }

    public String getPrizeInfo()
    {
        return prizeInfo;
    }

    public void setPrizeInfo(String prizeInfo)
    {
        this.prizeInfo = prizeInfo;
    }
}
