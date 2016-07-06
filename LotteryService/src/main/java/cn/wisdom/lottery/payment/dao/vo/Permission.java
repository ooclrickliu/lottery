package cn.wisdom.lottery.payment.dao.vo;

public class Permission extends BaseEntity
{
    
    private int permissionId;
    
    private String permissionName;
    
    private String permissionCode;
    
    private String permissionDesc;
    
    private boolean isSA;

    public String getPermissionCode()
    {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode)
    {
        this.permissionCode = permissionCode;
    }

    public int getPermissionId()
    {
        return permissionId;
    }

    public void setPermissionId(int permissionId)
    {
        this.permissionId = permissionId; 
    }

    public String getPermissionName()
    {
        return permissionName;
    }

    public void setPermissionName(String permissionName)
    {
        this.permissionName = permissionName;
    }

    public String getPermissionDesc()
    {
        return permissionDesc;
    }

    public void setPermissionDesc(String permissionDesc)
    {
        this.permissionDesc = permissionDesc;
    }
    
    public boolean isSA()
    {
        return isSA;
    }

    public void setSA(boolean isSA)
    {
        this.isSA = isSA;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Permission [permissionId=").append(this.permissionName)
               .append(", permissionName=").append(this.permissionDesc)
               .append(", permissionDesc=").append(this.permissionId).append("]");
        return builder.toString();
    }
    
}
