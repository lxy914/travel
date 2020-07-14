package cn.itcast.travel.service.impl;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.service.RouteService;

import java.sql.SQLException;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    @Override
    public PageBean<Entity> pageQuery(int cid, int currentPage, int pageSize, String rname) throws SQLException {
        Entity entity = Entity.create("tab_route").set("cid", cid);
        PageResult<Entity> entities;
        if (rname == null||rname.equals("null")) {
            entities = Db.use().page(entity, currentPage - 1, pageSize);
        } else {
            entities = Db.use().page(entity.set("rname", "like %" + rname + "%"), currentPage - 1, pageSize);
        }
        System.out.println(entity);

        PageBean<Entity> pb = new PageBean<>();
        pb.setCurrentPage(entities.getPage()+1);
        pb.setPageSize(entities.getPageSize());
        pb.setTotalCount(entities.getTotal());
        pb.setList(entities);
        pb.setTotalPage(entities.getTotalPage());
        return pb;
    }

    @Override
    public Entity findOne(int rid) throws SQLException {
        Entity entity = Db.use().get("tab_route", "rid", rid);
        if (entity != null) {
            List<Entity> entities = Db.use().findBy("tab_route_img", "rid", rid);
            entity.set("routeImgList", entities);
            Entity seller = Db.use().get("tab_seller", "sid", entity.get("sid"));
            entity.set("seller", seller);
            int count = Db.use().count(Entity.create("tab_favorite").set("rid", rid));
            entity.set("count", count);
        }
        return entity;
    }

}
