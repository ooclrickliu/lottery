package cn.wisdom.lottery.payment.dao.vo;

public class PermissionGrant
{
    private Permission permission;
    
    private String state;

    public Permission getPermission()
    {
        return permission;
    }

    public void setPermission(Permission permission)
    {
        this.permission = permission;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }
    
}
