package cn.wisdom.lottery.payment.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.payment.api.response.DoorbellPaymentAPIResult;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.dao.vo.Permission;
import cn.wisdom.lottery.payment.dao.vo.User;
import cn.wisdom.lottery.payment.service.PermissionService;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

@Controller
@RequestMapping("/permission")
public class PermissionController
{
    @Autowired
    private PermissionService permissionService;

    private JsonDocument SUCCESS = DoorbellPaymentAPIResult.SUCCESS;

    @RequestMapping(method = RequestMethod.POST, value = "/grant")
    @ResponseBody
    public JsonDocument grantPermission(@RequestParam int userId,
            @RequestParam int id) throws ServiceException
    {
        permissionService.grantPermission(userId, id);
        return SUCCESS;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/revoke")
    @ResponseBody
    public JsonDocument revokePermission(@RequestParam int userId,
            @RequestParam int id) throws ServiceException
    {
        permissionService.revokePermission(userId, id);
        return SUCCESS;
    }

    /**
     * Get permission list.
     * 
     * @param page
     * @param limit
     * @param sortBy
     * @param order
     * @return
     * @throws ServiceException
     */

    @RequestMapping(method = RequestMethod.GET, value = "/listAll")
    @ResponseBody
    public JsonDocument getPermissionList() throws ServiceException
    {
        List<Permission> permissionList = permissionService.getPermissionList();

        return new DoorbellPaymentAPIResult(permissionList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/user")
    @ResponseBody
    public JsonDocument getUserByPermissionId(@PathVariable int id)
            throws ServiceException
    {
        List<User> userList = permissionService.getUserByPermissionId(id);

        return new DoorbellPaymentAPIResult(userList);
    }

}
