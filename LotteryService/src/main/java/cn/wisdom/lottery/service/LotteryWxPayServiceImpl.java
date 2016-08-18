package cn.wisdom.lottery.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.wx.WXService;

@Service
public class LotteryWxPayServiceImpl implements LotteryWxPayService
{
    @Autowired
    private WXService wxService;

    @Autowired
    AppProperty appProperty;

    @Override
    public Map<String, String> unifiedOrder(Lottery lottery, String openId,
            String spbillCreateIp, String tradeType, String body)
            throws ServiceException
    {
        Map<String, String> params = new HashMap<String, String>();
        String totalFee =  "" + (int)(lottery.getTotalFee() * 100);
        
        params.put("out_trade_no", lottery.getOrderNo());
        params.put("total_fee", totalFee);
        params.put("body", body);
        params.put("spbill_create_ip", spbillCreateIp);
        params.put("notify_url", appProperty.wxPayNotifyUrl);
        params.put("trade_type", tradeType);
        params.put("openid", openId);

        Map<String, String> retMap = wxService.getWxMpService()
                .getJSSDKPayInfo(params);

        return retMap;
    }
}
