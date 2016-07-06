package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import cn.wisdom.lottery.payment.dao.constant.DBConstants;
import cn.wisdom.lottery.payment.dao.vo.UserPermission;

public class UserPermissionMapper implements RowMapper<UserPermission>
{

    @Override
    public UserPermission mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        UserPermission userPermission = new UserPermission();
        userPermission.setId(rs.getInt(DBConstants.TABLES.USER_PERMISSION.ID));
        userPermission.setUserId(rs.getInt(DBConstants.TABLES.USER_PERMISSION.USER_ID));
        userPermission.setPermId(rs.getInt(DBConstants.TABLES.USER_PERMISSION.PERM_ID));
        userPermission.setCreateTime(rs.getTimestamp(DBConstants.TABLES.USER_PERMISSION.CREATE_TIME));
        userPermission.setUpdateTime(rs.getTimestamp(DBConstants.TABLES.USER_PERMISSION.UPDATE_TIME));
        userPermission.setUpdateBy(rs.getInt(DBConstants.TABLES.USER_PERMISSION.UPDATE_BY));
        return userPermission;
    }

}
