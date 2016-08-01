package cn.wisdom.lottery.service.remote;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.JsonUtils;
import cn.wisdom.lottery.service.remote.response.LotteryOpenInfo;

@Service
public class LotteryRemoteServiceImpl implements LotteryRemoteService {

	private RestTemplate restTemplate = new RestTemplate();
	
	private static String URL = "http://f.apiplus.cn/ssq-1.json";
	
	private static Logger logger = LoggerFactory.getLogger(LotteryRemoteServiceImpl.class.getName());
	
	@Override
	public LotteryOpenInfo getLotteryOpenInfo() {
		String json = restTemplate.getForObject(URL, String.class);
		
		LotteryOpenInfo lotteryOpenInfo = null;
		try {
			System.out.println(json);
			lotteryOpenInfo = JsonUtils.fromJson(json, LotteryOpenInfo.class);
			
		} catch (OVTException e) {
			logger.error("Failed to get lottery open info.", e);
		}
		
		return lotteryOpenInfo;
	}

	public static void main(String[] args) {
		LotteryRemoteServiceImpl service = new LotteryRemoteServiceImpl();
		
		service.getLotteryOpenInfo();
	}
}
