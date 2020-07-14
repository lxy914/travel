package cn.itcast.travel.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用于封装后端返回前端数据对象
 */
public class ResultInfo implements Serializable {
    private boolean status;//后端返回结果正常为true，发生异常返回false
    private String msg;//发生异常的错误消息
    private Object data;//后端返回结果数据对象


    public ResultInfo() {
    }

    public ResultInfo(boolean status) {
        this.status = status;
    }

    public ResultInfo(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResultInfo(boolean status, String msg,Object data) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
