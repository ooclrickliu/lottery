package cn.wisdom.lottery.api.response;

import cn.wisdom.lottery.common.model.JsonDocument;

public class LotteryAPIResult extends JsonDocument
{
    public static final JsonDocument SUCCESS = new LotteryAPIResult();

    private static final String SERVICE_LOTTERY = "Lottery";

    public LotteryAPIResult()
    {
        super(SERVICE_LOTTERY, JsonDocument.STATE_SUCCESS);
    }

    public LotteryAPIResult(Object data)
    {
        super(SERVICE_LOTTERY, data);
    }

    public LotteryAPIResult(String errCode)
    {
        super(SERVICE_LOTTERY, errCode);
    }

}
