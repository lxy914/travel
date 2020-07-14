package cn.itcast.travel.service;

import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;
import cn.itcast.travel.domain.PageBean;

import java.sql.SQLException;
import java.util.List;

public interface FavoriteService {
    boolean isFavorite(String rid, int uid) throws SQLException;

    void add(String rid, int uid) throws SQLException;

    PageBean<Entity> findAll(String routeName, int start, int end, int curent, int pageSize) throws SQLException;

    PageBean<Entity> myfav(int uid,int curent, int pageSize) throws SQLException;
}
