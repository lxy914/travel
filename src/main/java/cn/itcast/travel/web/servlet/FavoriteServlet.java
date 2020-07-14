package cn.itcast.travel.web.servlet;

import cn.hutool.core.convert.Convert;
import cn.hutool.db.Entity;
import cn.hutool.extra.servlet.ServletUtil;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/favorite/*")
public class FavoriteServlet extends BaseServlet {
    private FavoriteService favoriteService=new FavoriteServiceImpl();
    public void page(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String rname = request.getParameter("rname");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String current = request.getParameter("current");
        String pageSize = request.getParameter("pageSize");
        PageBean<Entity> pageBean = favoriteService.findAll(rname, Convert.toInt(start,0), Convert.toInt(end,0), Convert.toInt(current, 1), Convert.toInt(pageSize, 8));
        ResultInfo info = new ResultInfo();
        info.setStatus(true);
        info.setData(pageBean);
        writeValue(response,info);
    }

    public void myfav(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        ResultInfo info = new ResultInfo();
        User user = (User) request.getSession().getAttribute("user");
        String current = request.getParameter("current");
        String pageSize = request.getParameter("pageSize");
        if (user == null) {
            info.setStatus(false);
            info.setMsg("请您登录");
        } else {
            PageBean<Entity> myfav = favoriteService.myfav(user.getUid(), Convert.toInt(current, 1), Convert.toInt(pageSize, 12));
            info.setData(myfav);
        }
        writeValue(response,info);
    }
}
