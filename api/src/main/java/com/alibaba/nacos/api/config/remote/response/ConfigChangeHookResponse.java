package com.alibaba.nacos.api.config.remote.response;


import com.alibaba.nacos.api.remote.response.Response;

public class ConfigChangeHookResponse extends Response {
    /**
     * 验证结果
     */
    private boolean isValid = true;
    private String validationMsg = "success";
    /**
     * 导致验证失败的异常
     */
    private Throwable exp;

    public ConfigChangeHookResponse setExp(Throwable exp) {
        this.exp = exp;
        return this;
    }

    public ConfigChangeHookResponse setValid(boolean valid) {
        isValid = valid;
        return this;
    }

    public ConfigChangeHookResponse setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
        return this;
    }

    public String getValidationMsg() {
        return validationMsg;
    }

    public Throwable getExp() {
        return exp;
    }

    public boolean isValid() {
        return isValid;
    }
}
