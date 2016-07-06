/**
 * PlatformErrorCode.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.service.exception;

/**
 * PlatformErrorCode
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class ServiceErrorCode
{
    
    //system
    public static final String SYSTEM_UNEXPECTED = "SystemUnexpected";
    
    //user
    public static final String NOT_LOGIN = "NotLogin";
    public static final String NO_PERMISSION = "NoPermission";
    public static final String INVALID_ACCESS = "InvalidAccess";
    //public static final String INVALID_DATA_FORMAT = "InvalidDataFormat";
    public static final String DUPLICATE_USER_NAME = "UserNameIsExist";
    public static final String WRONG_PASSWORD = "WrongPassword";
    public static final String INVALID_PASSWORD = "InvalidPassword";
    public static final String USER_NOT_EXIST = "UserNotExist";
    public static final String GET_USER_FAILED = "GetUserFailed";
    public static final String CHECK_NAME_FAILED = "CheckUserNameFailed";
    public static final String CHANGE_PASSWORD_FAILED = "ChangePasswordFailed";
    public static final String LIST_USER_FAILED = "ListUserFailed";
    public static final String CREATE_USER_FAILED = "CreateUserFailed";
    public static final String DELETE_USER_FAILED = "DeleteUserFailed";
    public static final String CAN_NOT_DELETE_SA = "CanNotDeleteSA";
    public static final String SAME_NEW_OLD_PASSWORD = "NewAndOldPasswordIsSame";
    
    //user token
    public static final String GET_USER_BY_TOKEN_FAILED = "GetUserByTokenFailed";
    public static final String GENERATE_USER_TOKEN_FAILED = "GenerateUserTokenFailed";
    public static final String INVALIDE_USER_TOKEN_FAILED = "InvalideUserTokenFailed";
    public static final String CLEAN_EXPIRED_TOKEN_FAILED = "CleanExpiredTokenFailed";
    
    //user permission
    public static final String GRANT_PERMISSION_FAILED = "GrantPermissionFailed";
    public static final String REVOKE_PERMISSION_FAILED = "RevokePermissionFailed";
    
    //permission
    public static final String LIST_PERMISSION_FAILED = "ListPermissionFailed";
      
    //resource
    public static final String PRODUCT_NOT_EXIST = "ProductNotExist";
    public static final String PRODUCT_EXIST = "ProductExist";
    public static final String CREATE_PRODUCT_FAILED = "CreateProductFailed";
    public static final String DELETE_PRODUCT_FAILED = "DeleteProductFailed";
    public static final String MODIFY_PRODUCT_FAILED = "ModifyProductFailed";
    public static final String LOAD_ALL_RESOURCE_FAILED = "LoadAllResourceFailed";
    public static final String INVALID_TIME_UNIT = "InvalidTimeUnit";
    public static final String INVALID_RESOURCE_TYPE = "InvalidResourceType";
    public static final String GET_RESOURCE_FAILED = "GetResourceFailed";
    public static final String RESOURCE_IS_EMPTY = "ResourceIsEmpty";
    
    
    //refund 
    public static final String TRANSFER_REFUND_PAGE_FAILED = "TransferRefundPageFailed";
    
    //Lottery
    public static final String MISSING_BALL = "MissingBall";
    public static final String INVALID_STATE = "InvalidState";
    public static final String NO_PRIVILEGE = "NoPrivilege";
    
    
    
}
