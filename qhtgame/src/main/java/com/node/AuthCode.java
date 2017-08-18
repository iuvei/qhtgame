package com.node;

/**
 * Created by Administrator on 2017/5/11.
 */
public class AuthCode {
    private String tel;
    private Long time;
    private String Code;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;//0：注册验证；1：找回密码验证

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
