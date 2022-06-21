package com.alibaba.nacos.client.config.hook;


import com.alibaba.nacos.api.config.ConfigChangeHook;
import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;

public class CheckContentHook implements ConfigChangeHook {
    @Override
    public boolean doFilter(ConfigChangeHookRequest request, ConfigChangeHookResponse response) {
        String content = request.getContent ();
        if (content != null) {
            if (content.contains ("ATTACK!")) {
                System.out.println ("invalid content " + content);
                return false;
            }
        }
        return true;
    }
}
