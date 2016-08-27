package cn.wisdom.lottery.service.wx.message;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.dao.vo.User;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.UserService;
import cn.wisdom.lottery.service.wx.WXService;

@Component
public class WxMpVideoHandler extends AbstractWxMpHandler {

	private static final String PARAM_USER = "user";

	@Autowired
	private AppProperty appProperty;

	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private WXService wxService;

	private Logger logger = LoggerFactory.getLogger(WxMpVideoHandler.class
			.getName());

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		WxMpXmlOutMessage response = null;

		buildKfResponse(wxMessage, wxMpService, sessionManager);

		return response;
	}

	private void buildKfResponse(WxMpXmlMessage wxMessage,
			WxMpService wxMpService, WxSessionManager sessionManager) {
		try {
			// download the video
			File video = wxService.getWxMpService().mediaDownload(
					wxMessage.getMediaId());

			// convert to images
			if (video != null) {
				long start = System.currentTimeMillis();
				logger.info("Start converting vedio to images...");
				// frameNum = vedio2Images(video);
				logger.info("Complete converting vedio to images.");
				long end = System.currentTimeMillis();

				System.out.println("Extract Images Cost : " + (end - start));

				video.delete();
			}

		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int vedio2Images(File vedioFile) {
		int frameNum = 0;
		File outFile = null;
		FileOutputStream fos = null;

		// String frameDir = getFrameDir("");
		File frames = new File("");

		if ((frames.exists()) && (frames.isDirectory())) {
			File[] arrayOfFile1;
			File[] subFiles = frames.listFiles();
			int j = (arrayOfFile1 = subFiles).length;
			for (int i = 0; i < j; ++i) {
				File subFile = arrayOfFile1[i];

				subFile.delete();
			}
			frames.delete();
		}
		frames.mkdir();
		try {
			FFmpegFrameGrabber ocg = FFmpegFrameGrabber
					.createDefault(vedioFile);
			ocg.start();

			Java2DFrameConverter converter = new Java2DFrameConverter();
			while (true) {
				Frame frame = ocg.grabImage();
				if (frame == null) {
					break;
				}
				BufferedImage bufferedImage = converter.getBufferedImage(frame);
				if (bufferedImage == null) {
					break;
				}

				++frameNum;
				outFile = new File("" + frameNum + ".jpg"); // TODO
				fos = new FileOutputStream(outFile);
				ImageIO.write(bufferedImage, "jpg", fos);
			}

			ocg.stop();
		} catch (Exception e) {
			logger.error("Failed convert video to images!", e);

			close(fos);
		} finally {
			close(fos);
		}
		return frameNum;
	}

	private void close(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (IOException localIOException) {
			}
		}
	}

	private void redirectMessage2Kf(WxMpXmlMessage wxMessage,
			WxMpService wxMpService, WxSessionManager sessionManager)
			throws WxErrorException {
		WxSession session = sessionManager.getSession(wxMessage
				.getFromUserName());
		User user = (User) session.getAttribute(PARAM_USER);
		if (user == null) {
			user = userService.getUserByOpenId(wxMessage.getFromUserName());
			session.setAttribute(wxMessage.getFromUserName(), user);

			// reverse session, key = userId
			WxSession reverseSession = sessionManager
					.getSession(DataConvertUtils.toString(user.getId()));
			if (reverseSession.getAttribute(PARAM_USER) == null) {
				reverseSession.setAttribute(PARAM_USER, user);
			}
		}

		String content = user.getNickName() + "[" + user.getId() + "] 发的语音:";
		WxMpCustomMessage customTextMessage = WxMpCustomMessage.TEXT()
				.toUser(appProperty.defaultKf).content(content).build();
		wxMpService.customMessageSend(customTextMessage);

		WxMpCustomMessage customImageMessage = WxMpCustomMessage.VOICE()
				.toUser(appProperty.defaultKf).mediaId(wxMessage.getMediaId())
				.build();
		wxMpService.customMessageSend(customImageMessage);
	}

}
