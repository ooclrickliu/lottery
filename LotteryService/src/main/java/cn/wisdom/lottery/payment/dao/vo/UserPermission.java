package cn.wisdom.lottery.payment.dao.vo;

public class UserPermission extends BaseEntity
{
    private int userId;
    
    private int permId;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getPermId()
    {
        return permId;
    }

    public void setPermId(int permId)
    {
        this.permId = permId;
    }
    
}
