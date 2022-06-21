package com.alibaba.nacos.api.config;

import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.List;

public class ConfigChangeHookChainTest extends TestCase {

    public void testDoFilter() throws NoSuchFieldException, IllegalAccessException {
        Field field = ConfigChangeHookChain.class.getDeclaredField ("hooks");
        field.setAccessible (true);
        List<ConfigChangeHook> hooks = (List<ConfigChangeHook>) field.get (ConfigChangeHookChain.INSTANCE);
        System.out.println (hooks);

        ConfigChangeHookChain.INSTANCE.doFilter (null);
    }

}
