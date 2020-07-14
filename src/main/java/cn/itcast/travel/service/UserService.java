package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

import java.sql.SQLException;

public interface UserService {
    boolean register(User user) throws SQLException;

    boolean active(String code) throws SQLException;

    User login(User user) throws SQLException;
}
