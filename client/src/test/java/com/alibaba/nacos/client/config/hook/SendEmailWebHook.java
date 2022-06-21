package com.alibaba.nacos.client.config.hook;


import com.alibaba.nacos.api.config.ConfigChangeHook;
import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;

public class SendEmailWebHook implements ConfigChangeHook {
    @Override
    public boolean doFilter(ConfigChangeHookRequest request, ConfigChangeHookResponse response) {
        System.out.printf ("send email dataId=%s,group=%s,tenant=%s,operation=%s,content=%s\n",
                request.getDataId (), request.getGroup (), request.getTenant (),request.getChangeType (),request.getContent ());
        return true;
    }
}
