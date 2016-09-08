/**
 * UserDaoImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package cn.wisdom.lottery.dao;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.mapper.DaoRowMapper;
import cn.wisdom.lottery.dao.vo.User;

/**
 * UserDaoImpl
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */

@Repository
public class UserDaoImpl implements UserDao
{
    @Autowired
    private DaoHelper daoHelper;

    private static final String SQL_INSERT_USER = "INSERT IGNORE INTO user(openid, role, "
            + "create_time, update_time) "
            + "VALUES(?, ?, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
    
    private static final String SQL_INSERT_USER2 = "INSERT INTO user(openid, role, nick_name, head_img_url, country, province, city, sex, subscribe, subscribe_time, unionid, "
    		+ "create_time, update_time) "
    		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	private static final String SQL_GET_USER_PREFIX = "select * from user ";

    private static final String SQL_GET_USER_BY_OPENID = SQL_GET_USER_PREFIX
            + "WHERE openid = ? LIMIT 1";
    
    private static final String SQL_GET_USER = SQL_GET_USER_PREFIX
    		+ "WHERE id = ? LIMIT 1";
    
    private static final String SQL_GET_USER_BY_IDS = SQL_GET_USER_PREFIX
    		+ "WHERE id in ({0})";

    private static final String SQL_UPDATE_WX_INFO = "UPDATE user SET country = ?, province = ?, city = ?, nick_name = ?, head_img_url = ?, sex = ?, subscribe_time = ?, unionid = ?, update_time = CURRENT_TIMESTAMP "
            + "WHERE openid = ?";
    
    private static final String SQL_GET_USER_BY_PHONE = SQL_GET_USER_PREFIX
    		+ "where phone = ?";
    
	private static final String SQL_UPDATE_USER_PASSWORD = "update user set password = ?, update_time = current_timestamp where id = ?";
	
	private static final String SQL_UPDATE_USER_SUBSCRIBE = "update user set subscribe = ?, update_time = current_timestamp where id = ?";
    
    private static final DaoRowMapper<User> userMapper = new DaoRowMapper<User>(User.class);

    public User getUserById(final long id)
    {
        if (id <= 0)
        {
            return null;
        }

        // get from db
        String errMsg = MessageFormat.format("Failed to query user by id {0}!",
                id);
        User user = daoHelper.queryForObject(SQL_GET_USER, userMapper, errMsg,
                id);

        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.UserDao#save(com.ovt.dao.vo.User)
     */
    public long save(final User user)
    {
        Object[] params = new Object[2];
        params[0] = user.getOpenid();
        params[1] = user.getRole().toString();

        String errMsg = MessageFormat.format("Failed insert user, openid={0}!",
                user.getOpenid());

        long id = daoHelper.save(SQL_INSERT_USER, errMsg, true, params);

        return id;
    }
    
    @Override
    public long saveWithWxInfo(User user) {
    	Object[] params = new Object[11];
        params[0] = user.getOpenid();
        params[1] = user.getRole().toString();
        params[2] = user.getNickName();
        params[3] = user.getHeadImgUrl();
        params[4] = user.getCountry();
        params[5] = user.getProvince();
        params[6] = user.getCity();
        params[7] = user.getSex();
        params[8] = user.isSubscribe();
        params[9] = user.getSubscribeTime();
        params[10] = user.getUnionid();
        
        String errMsg = MessageFormat.format("Failed insert user with wx info, openid={0}!",
                user.getOpenid());

        long id = daoHelper.save(SQL_INSERT_USER2, errMsg, true, params);
        user.setId(id);
        
        return id;
    }

	@Override
	public User getUserByOpenid(String openId) {
		User user = null;
		if (StringUtils.isNotBlank(openId))
        {
			// get from db
	        String errMsg = MessageFormat.format("Failed to query user by openid {0}!",
	        		openId);
	        user = daoHelper.queryForObject(SQL_GET_USER_BY_OPENID, userMapper, errMsg,
	        		openId);
        }

        return user;
	}

	@Override
	public void updateUserWxInfo(User user) {
		String errMsg = MessageFormat
                .format("Failed to update user wx info [{0}] !", user.getId());

        daoHelper.update(SQL_UPDATE_WX_INFO, errMsg, 
        		user.getCountry(), 
        		user.getProvince(), 
        		user.getCity(), 
        		user.getNickName(), 
        		user.getHeadImgUrl(), 
        		user.getSex(), 
        		user.getSubscribeTime(), 
        		user.getUnionid(), 
        		user.getOpenid());
	}
	
	@Override
	public User getUserByPhone(String phone) {

		String errMsg = "Failed to get user by phone: " + phone;
		User user = daoHelper.queryForObject(SQL_GET_USER_BY_PHONE, userMapper,
				errMsg, phone);

		return user;
	}

	@Override
	public void updatePassword(long userId, String newPassword) {

		String errMsg = "Failed to update user password, id: "
				+ userId;
		daoHelper.update(SQL_UPDATE_USER_PASSWORD, errMsg,
				newPassword, userId);
	}
	
	@Override
	public void updateSubscribeState(User user) {

		String errMsg = "Failed to update user subscribe state, openid: "
				+ user.getOpenid();
		daoHelper.update(SQL_UPDATE_USER_SUBSCRIBE, errMsg,
				user.isSubscribe(), user.getId());
	}

	@Override
	public List<User> getUserByIdList(List<Long> userIds) {
		String errMsg = "Failed to get user by ids: " + userIds;
		String sql = MessageFormat.format(SQL_GET_USER_BY_IDS, StringUtils.getCSV(userIds));
		List<User> users = daoHelper.queryForList(sql, userMapper,
				errMsg);

		return users;
	}
}