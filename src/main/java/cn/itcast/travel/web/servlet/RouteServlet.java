package cn.itcast.travel.web.servlet;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.log.StaticLog;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        StaticLog.debug("接受的数据：{}",paramMap);
        String currentPageStr = paramMap.get("currentPage");
        String pageSizeStr = paramMap.get("pageSize");
        String cidStr = paramMap.get("cid");
        String rname = paramMap.get("rname");


        int cid = Convert.toInt(cidStr,5);
        int currentPage = Convert.toInt(currentPageStr,1);
        int pageSize = Convert.toInt(pageSizeStr,5);
        PageBean<Entity> pageBean = service.pageQuery(cid, currentPage, pageSize, rname);
        ResultInfo info = new ResultInfo();
        info.setStatus(true);
        info.setData(pageBean);
        writeValue(response, info);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String rid = request.getParameter("rid");
        Entity route = service.findOne(Convert.toInt(rid));
        ResultInfo info = new ResultInfo();
        info.setStatus(true);
        info.setData(route);
        writeValue(response,info);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        ResultInfo info = new ResultInfo();
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            info.setStatus(false);
        } else {
            boolean flag = favoriteService.isFavorite(rid, user.getUid());
            info.setStatus(flag);
        }
        writeValue(response,info);
    }
    public void addFavorite(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo info = new ResultInfo();

        if (user == null) {
            info.setStatus(false);
            info.setMsg("请您先登录");
        } else {
            int uid = user.getUid();
            favoriteService.add(rid, uid);
            info.setStatus(true);
            info.setMsg("收藏成功");
        }

        writeValue(response,info);
    }
}
