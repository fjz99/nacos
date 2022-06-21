package com.alibaba.nacos.api.config;


import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ConfigChangeHookChain {

    public static final ConfigChangeHookChain INSTANCE = new ConfigChangeHookChain ();
    private final List<ConfigChangeHook> hooks = new ArrayList<> ();

    private ConfigChangeHookChain() {
        ServiceLoader<ConfigChangeHook> serviceLoader = ServiceLoader.load (ConfigChangeHook.class);
        for (ConfigChangeHook hook : serviceLoader) {
            hooks.add (hook);
        }
    }

    /**
     * @return 是否已存在
     */
    public synchronized boolean addHook(ConfigChangeHook hook) {
        if (hooks.contains (hook))
            return false;
        else {
            hooks.add (hook);
            return true;
        }
    }

    public ConfigChangeHookResponse doFilter(ConfigChangeHookRequest request) {
        ConfigChangeHookResponse response = new ConfigChangeHookResponse ();
        for (ConfigChangeHook hook : hooks) {
            try {
                if (!hook.doFilter (request, response)) {
                    response.setValid (false);
                    break;
                }
            } catch (Throwable e) {
                response.setExp (e);
                response.setValid (false);
                response.setValidationMsg ("client的验证器抛出异常");
                break;
            }
        }
        return response;
    }
}
