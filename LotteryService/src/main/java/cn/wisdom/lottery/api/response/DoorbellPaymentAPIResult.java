package cn.wisdom.lottery.api.response;

import cn.wisdom.lottery.common.model.JsonDocument;

public class DoorbellPaymentAPIResult extends JsonDocument
{
    public static final JsonDocument SUCCESS = new DoorbellPaymentAPIResult();

    private static final String SERVICE_DOORBELL_PAYMENT = "DoorbellPayment";

    public DoorbellPaymentAPIResult()
    {
        super(SERVICE_DOORBELL_PAYMENT, JsonDocument.STATE_SUCCESS);
    }

    public DoorbellPaymentAPIResult(Object data)
    {
        super(SERVICE_DOORBELL_PAYMENT, data);
    }

    public DoorbellPaymentAPIResult(String errCode)
    {
        super(SERVICE_DOORBELL_PAYMENT, errCode);
    }

}
