package cn.wisdom.lottery.service.wx.message;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

@Service
public class CommandHandler extends AbstractWxMpHandler {
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private AppProperty appProperty;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		
		int code = DataConvertUtils.toInt(wxMessage.getContent());
		switch (code) {
		case 991:
			transferMerchant();
			break;
		case 992:
			transferMerchantRevert();
			break;

		default:
			break;
		}
		
		return buildSuccessOutMessage(wxMessage);
	}

	private void transferMerchantRevert() {
		try {
			int period = DataConvertUtils.toInt(lotteryServiceFacade.getCurrentPeriod(LotteryType.SSQ).getOpencode());
			lotteryServiceFacade.transferMerchant(period , 19, 2);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	private void transferMerchant() {
		try {
			int period = DataConvertUtils.toInt(lotteryServiceFacade.getCurrentPeriod(LotteryType.SSQ).getOpencode());
			lotteryServiceFacade.transferMerchant(period , 2, 19);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
