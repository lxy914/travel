package cn.itcast.travel.web.servlet;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        StaticLog.debug("方法名称：{}",methodName);
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
//            method.setAccessible(true);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) { // 找不到对应方法就返回404
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "页面找不到");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeValue(HttpServletResponse response, Object o) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        JSONUtil.toJsonStr(o,response.getWriter());;
    }

}
