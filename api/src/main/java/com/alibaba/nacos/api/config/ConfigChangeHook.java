package com.alibaba.nacos.api.config;


import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;

/**
 * 应当线程安全
 */
public interface ConfigChangeHook {

    /**
     * @return 是否继续执行下一个验证
     */
    boolean doFilter(ConfigChangeHookRequest request, ConfigChangeHookResponse response);

}
