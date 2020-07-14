package cn.itcast.travel.service.impl;

import cn.hutool.db.Db;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<Category> findAll() {
        List<Category> categories=null;
        try {
            categories = Db.use().query("select * from tab_category order by cid", Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
