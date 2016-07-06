/**
 * DBConstants.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.dao.constant;

/**
 * DBConstants
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class DBConstants
{

    public static final class TABLES
    {

        public static class BASE_ENTITY
        {
            public static final String ID = "id";
            public static final String CREATE_TIME = "create_time";
            public static final String UPDATE_TIME = "update_time";
            public static final String UPDATE_BY = "update_by";
        }

        public static final class USER extends BASE_ENTITY
        {
            public static final String U_NAME = "admin_name";
            public static final String U_PASSWORD = "admin_pwd";
        }

        public static final class USER_TOKEN extends BASE_ENTITY
        {
            public static final String U_ID = "admin_id";
            public static final String UAT_TOKEN = "at_admin_token ";
            public static final String UAT_EXPIRE_TIME = "at_expire_time";
        }

        public static final class PERMISSION extends BASE_ENTITY
        {
            public static final String PERM_NAME = "perm_name";
            public static final String PERM_CODE = "perm_code";
            public static final String PERM_DESC = "perm_desc";
            public static final String IS_SA = "is_sa";
        }

        public static final class USER_PERMISSION extends BASE_ENTITY
        {
            public static final String USER_ID = "admin_id";
            public static final String PERM_ID = "perm_id";
        }

        public static final class APP_PROPERTY extends BASE_ENTITY
        {
            public static final String PROP_NAME = "prop_name";
            public static final String PROP_VALUE = "prop_value";
            public static final String DESC = "desc";
        }
        
        public static final class LOTTERY extends BASE_ENTITY
        {
        	public static final String ORDER_NO = "order_no";
        	public static final String LOTTER_TYPE = "lotter_type";
        	public static final String BUSINESS_TYPE = "business_type";
        	public static final String TIMES = "times";
        	public static final String TICKET_STATE = "ticket_state";
        	public static final String OWNER = "owner";
        	public static final String MERCHANT = "merchant";
        	public static final String DISTRIBUTE_TIME = "distribute_time";
        	public static final String TICKET_PRINT_TIME = "ticket_print_time";
        	public static final String TICKET_FETCH_TIME = "ticket_fetch_time";
        	public static final String PRIZE_INFO = "prize_info";
        	public static final String PRIZE_BONUS = "prize_bonus";
        }
        
        public static final class LOTTERY_NUMBER extends BASE_ENTITY
        {
        	public static final String LOTTERY_ID = "lottery_id";
        	public static final String NUMBER = "number";
        }
        
        public static final class LOTTERY_PERIOD extends BASE_ENTITY
        {
        	public static final String LOTTERY_ID = "lottery_id";
        	public static final String PERIOD = "period";
        }
        
        public static final class PRIZE_LOTTERY_SSQ extends BASE_ENTITY
        {
        	public static final String PERIOD = "period";
        	public static final String OPEN_TIME = "open_time";
        	public static final String NUMBER = "number";
        }

    }

}
