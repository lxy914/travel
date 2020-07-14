package cn.itcast.travel.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.setting.Setting;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private Setting setting = new Setting("travel.setting");

    @Override
    public boolean register(User user) throws SQLException {
        // 用户名是否存在
        Entity entity = Db.use().get("tab_user", "username", user.getUsername());
        if (entity != null) {
            return false;
        }

        user.setCode(IdUtil.simpleUUID());  // 设置激活码
        user.setStatus("N");    // 设置未激活
        Db.use().insert(Entity.create("tab_user")
                .set("username",user.getUsername())
                .set("password",user.getPassword())
                .set("name",user.getName())
                .set("email",user.getEmail())
                .set("sex",user.getSex())
                .set("telephone",user.getTelephone())
                .set("code",user.getCode())
                .set("status",user.getStatus())
                .set("birthday",user.getBirthday()));
        // 邮件内容
        String content = "<a href='" + setting.getStr("server", "http://localhost:8080") + "/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";

        // 发送邮件
        MailUtil.send(user.getEmail(), "激活邮件",content , false);
        return true;
    }

    @Override
    public boolean active(String code) throws SQLException {
        Entity entity = Db.use().get("tab_user", "code", code);
        if (entity != null) {
            if (entity.getStr("status").equals("Y")) {
                return false;
            }
            Db.use().update(Entity.create().set("status", "Y"), Entity.create("tab_user").set("uid", entity.get("uid")));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) throws SQLException {
        Entity entity = Db.use().get(Entity.create("tab_user").set("username", user.getUsername()).set("password", user.getPassword()));
        if (entity == null) {
            return null;
        } else {
            return entity.toBean(User.class);
        }
    }
}
