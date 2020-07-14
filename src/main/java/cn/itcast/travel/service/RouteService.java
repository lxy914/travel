package cn.itcast.travel.service;

import cn.hutool.db.Entity;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.sql.SQLException;

public interface RouteService {
    PageBean<Entity> pageQuery(int cid, int currentPage, int pageSize, String rname) throws SQLException;

    /**
     *
     * @param rid
     * @return
     */
    Entity findOne(int rid) throws SQLException;
}
