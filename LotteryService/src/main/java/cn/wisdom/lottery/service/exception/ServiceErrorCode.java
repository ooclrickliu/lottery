/**
 * PlatformErrorCode.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.service.exception;

/**
 * PlatformErrorCode
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class ServiceErrorCode {
	
	// ================App===================//
	// system
	public static final String SYSTEM_UNEXPECTED = "SystemUnexpected";

	// user
	public static final String NOT_LOGIN = "NotLogin";

	// ================Console===================//
	public static final String INVALID_ACCESS_TOKEN = "InvalidAccessToken";
	public static final String USER_NOT_EXIST = "UserNotExist";
	public static final String WRONG_PASSWORD = "WrongPassword";
	public static final String SAME_PASSWORD = "SamePassword";
	public static final String INVALID_PASSWORD_FORMAT = "InvalidPasswordFormat";

	// Lottery
	public static final String MISSING_BALL = "MissingBall";
	public static final String INVALID_STATE = "InvalidState";
	public static final String NO_PRIVILEGE = "NoPrivilege";
	public static final String INVALID_REDPACK_COUNT = "InvalidRedpackCount";
	public static final String ERROR_BUSINESS_TYPE = "ErrorBusinessType";
	public static final String REDPACK_EMPTY = "RedpackEmpty";
	public static final String REDPACK_EXPIRED = "RedpackExpired";
	public static final String SNATCHED = "Snatched";
	public static final String CANNOT_SHARE = "CannotShare";

	// WX
	public static final String OAUTH_FAIL = "OAuthFail";
	public static final String NOT_SUBSCRIBE = "NotSubscribe";

}
