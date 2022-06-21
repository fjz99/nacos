package com.alibaba.nacos.client.config.hook;


import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.config.NacosConfigService;
import com.alibaba.nacos.client.config.filter.impl.ConfigResponse;
import com.alibaba.nacos.client.naming.NacosNamingService;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.Executor;

public class HookTest {

    @Test
    public void register2() throws NacosException, InterruptedException {
        final String dataId = "c";
        final String group = "d";
        final int timeout = 3000;
        ConfigResponse response = new ConfigResponse ();
        response.setContent ("aa");
        response.setConfigType ("bb");
        Properties properties = new Properties ();
        properties.setProperty ("serverAddr", "localhost:8848");
        NacosNamingService service = new NacosNamingService (properties);
        System.out.println (service.getAllInstances ("test"));
    }

    @Test
    public void register() throws NacosException, InterruptedException {
        final String dataId = "c";
        final String group = "d";
        final int timeout = 3000;
        ConfigResponse response = new ConfigResponse ();
        response.setContent ("aa");
        response.setConfigType ("bb");
        Properties properties = new Properties ();
        properties.setProperty ("serverAddr", "localhost:8848");
        NacosNamingService service = new NacosNamingService (properties);
        service.registerInstance ("test", "group", "localhost", 7777);
        System.out.println ("ready");
        Thread.sleep (100000000);
    }

    @Test
    public void testHook() throws NacosException, InterruptedException {
        final String dataId = "c";
        final String group = "d";
        final int timeout = 3000;
        ConfigResponse response = new ConfigResponse ();
        response.setContent ("aa");
        response.setConfigType ("bb");
        Properties properties = new Properties ();
        properties.setProperty ("serverAddr", "localhost:8848");
        NacosConfigService service = new NacosConfigService (properties);
        service.addListener (dataId, group, new Listener () {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println ("has有！ " + configInfo);
            }
        });
        System.out.println ("ready");
        Thread.sleep (100000000);
    }
}
