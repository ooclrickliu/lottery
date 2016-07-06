package cn.wisdom.lottery.payment.service.wx.response;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

@Component
public class WXResponseHandler {

	private static final String PARAM_ERRCODE = "errcode";

	private static final String PARAM_ERRMSG = "errmsg";
	
	@SuppressWarnings("rawtypes")
	public void handleError(HashMap response) throws ServiceException
	{
		if (CollectionUtils.isEmpty(response)
				|| response.get(PARAM_ERRCODE) != null) {
			throw new ServiceException(response.get(PARAM_ERRCODE).toString(),
					response.get(PARAM_ERRMSG).toString());
		}
	}
}
