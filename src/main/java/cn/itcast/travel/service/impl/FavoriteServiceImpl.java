package cn.itcast.travel.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.log.Log;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.service.FavoriteService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private static final Log log=Log.get();
    @Override
    public boolean isFavorite(String rid, int uid) throws SQLException {
        Entity entity = Db.use().queryOne("select * from tab_favorite where rid= ? and uid = ?", rid, uid);
        return entity != null;
    }

    @Override
    public void add(String rid, int uid) throws SQLException {
        Db.use().tx((db)->{
            db.insert(Entity.create("tab_favorite").set("rid", rid).set("uid", uid).set("date", new Date()));
            db.execute("update tab_route set count=count+1 where rid=?", rid);
        });
    }

    @Override
    public PageBean<Entity> findAll(String routeName, int start, int end, int curent, int pageSize) throws SQLException {
        log.info("routeName:{},start:{},end:{},current:{},pageSize:{}",routeName,start,end,curent,pageSize);
        if (curent < 1) {
            curent=1;
        }
        StringBuilder sql1 = new StringBuilder("select * from tab_route where 1=1 ");
        StringBuilder sql2 = new StringBuilder("select count(distinct rid) as count from tab_favorite where rid in (");
        if (StrUtil.isNotEmpty(routeName)) {
            sql1.append(" and rname like '%" + routeName + "%' ");
        }
        if (start != 0 && end == 0) {
            sql1.append(" and price >= "+start);
        }
        if (start == 0 && end != 0) {
            sql1.append(" and price <= "+end);
        }
        if (start != 0 && end != 0) {
            sql1.append(" and price between "+start+" and "+end);
        }
        sql2.append(sql1.toString().replace("*","rid"));
        sql2.append(")");
        sql1.append(" and count>0 order by count desc limit ?,?");
        List<Entity> query = Db.use().query(sql1.toString(), (curent-1)*pageSize, pageSize);
        int totalCount = Convert.toInt(Db.use().queryNumber(sql2.toString()));
        PageBean<Entity> pageBean = new PageBean<>();
        pageBean.setList(query);
        pageBean.setTotalPage(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1);
        pageBean.setTotalCount(totalCount);
        pageBean.setPageSize(pageSize);
        pageBean.setCurrentPage(curent);
        return pageBean;
    }

    @Override
    public PageBean<Entity> myfav(int uid,int curent, int pageSize) throws SQLException {
        List<Entity> entities = Db.use().query("select * from tab_route where rid in (select rid from tab_favorite where uid=?) limit ?,?", uid, (curent - 1) * pageSize, pageSize);
        int totalCount = Db.use().queryNumber("select count(1) as count from tab_route where rid in (select rid from tab_favorite where uid=?)", uid).intValue();
        PageBean<Entity> pageBean = new PageBean<>();
        pageBean.setList(entities);
        pageBean.setTotalPage(totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1);
        pageBean.setTotalCount(totalCount);
        pageBean.setPageSize(pageSize);
        pageBean.setCurrentPage(curent);
        return pageBean;
    }
}
