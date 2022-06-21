package com.alibaba.nacos.api.config;

import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;


public class TestHook implements ConfigChangeHook {

    @Override
    public boolean doFilter(ConfigChangeHookRequest request, ConfigChangeHookResponse response) {
        System.out.println ("hook " + request);
        return true;
    }
}
