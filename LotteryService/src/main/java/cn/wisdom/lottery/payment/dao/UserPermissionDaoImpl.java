package cn.wisdom.lottery.payment.dao;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import cn.wisdom.lottery.payment.dao.vo.UserPermission;

@Repository
public class UserPermissionDaoImpl implements UserPermissionDao
{
    @Autowired
    private DaoHelper daoHelper;

    private String INSERT_USER_PERMISSION = "INSERT INTO admin_permission "
            + "(ap_admin_id,ap_perm_id,create_time,update_time,update_by) "
            + "VALUES(?, ?, ?, ?, ?)";

    private String DELETE_USER_PERMISSION = "DELETE FROM admin_permission "
            + "WHERE ap_admin_id = ? AND ap_perm_id = ?";

    private String DELETE_BY_USER_ID = "DELETE FROM admin_permission WHERE ap_admin_id = ?";

    public void save(UserPermission userPermission)
    {

        Object[] params = new Object[5];
        params[0] = userPermission.getUserId();
        params[1] = userPermission.getPermId();
        params[2] = userPermission.getCreateTime();
        params[3] = userPermission.getUpdateTime();
        params[4] = userPermission.getUpdateBy();

        String errMsg = MessageFormat.format(
                "Failed insert user permission userId : {0}, permissionId : {1}!",
                userPermission.getUserId(), userPermission.getPermId());

        daoHelper.save(INSERT_USER_PERMISSION, errMsg, true, params);

    }

    @Override
    public void delete(int userId, int permId)
    {
        String errMsg = MessageFormat.format(
                "Failed delete user permission userId : {0}, permissionId : {1}!",
                userId, permId);
        this.daoHelper.update(DELETE_USER_PERMISSION, errMsg, userId, permId);

    }

    @Override
    public void delete(int userId)
    {
        String errMsg = MessageFormat
                .format("Failed delete user permission userId : {0}!", userId);
        this.daoHelper.update(DELETE_BY_USER_ID, errMsg, userId);

    }

}
