package cn.itcast.travel.web.servlet;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.log.StaticLog;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Map<String, String> map = ServletUtil.getParamMap(request);
        StaticLog.debug("用户注册的数据的数据：{}",map);
        String check = map.get("code");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            ResultInfo info = new ResultInfo();
            info.setStatus(false);
            info.setMsg("验证码错误");
            writeValue(response, info);
            return;
        }
        User user = ServletUtil.toBean(request, User.class, false);

        boolean flag = service.register(user);
        ResultInfo info = new ResultInfo();
        if (flag) {
            info.setStatus(true);
        } else {
            info.setStatus(false);
            info.setMsg("注册失败");
        }

        writeValue(response, info);
    }

    /**
     * 登录功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String contentType = request.getContentType();
        StaticLog.debug("Content-Type：{}",contentType);
        Map<String, String> map = ServletUtil.getParamMap(request);
        StaticLog.debug("用户登录的数据：{}",map);
        ResultInfo info = new ResultInfo();
        String code = request.getParameter("code");
        String session_code = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        StaticLog.debug("用户传来的验证码：{}，session中的验证码：{}",code,session_code);
        if (session_code.equalsIgnoreCase(code)) {
            User user = ServletUtil.toBean(request, User.class, false);

            User u = service.login(user);

            if (u == null) {
                info.setStatus(false);
                info.setMsg("用户名或密码错误");
            } else if (!u.getStatus().equals("Y")) {
                info.setStatus(false);
                info.setMsg("您尚未激活，请激活");
            } else {
                request.getSession().setAttribute("user", u);
                info.setStatus(true);
            }
        }else{
            info.setStatus(false);
            info.setMsg("验证码错误");
        }


        writeValue(response, info);
    }

    /**
     * 查询单个对象
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");

        ResultInfo info = new ResultInfo();
        if (user != null) {
            user.setPassword(null);
            user.setCode(null);

            info.setStatus(true);
            info.setData(user);
        } else {
            info.setStatus(false);
        }
        writeValue(response, info);
    }

    /**
     * 退出功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        writeValue(response,new ResultInfo(true));
    }

    /**
     * 激活功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String code = request.getParameter("code");
        if (code != null) {
            boolean flag = service.active(code);

            String msg;
            if (flag) {
                // 激活成功
                msg = "<h1>激活成功</h1> 请 <a href='#/login'>登录</a> ";
            } else {
                // 激活失败
                msg = "激活失败，请联系管理员";
            }
            ServletUtil.write(response,msg,"text/html;charset=utf-8");
        }
    }

}
