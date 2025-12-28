package bala.signoff.service;

import bala.signoff.entity.User;
import lombok.Data;


@Data
public class PermissionService
{
    static public User getCurrentLoginUser()
    {
        // to-do: login ~~~
        User user = new User();
        user.setAccount("zjchuang");
        user.setName("Bala");
        return user;
    }
}
