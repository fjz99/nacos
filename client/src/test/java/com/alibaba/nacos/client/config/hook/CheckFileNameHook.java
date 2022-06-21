package com.alibaba.nacos.client.config.hook;

import com.alibaba.nacos.api.config.ConfigChangeHook;
import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;


public class CheckFileNameHook implements ConfigChangeHook {
    private static final String[] LEGAL_SUFFIX = new String[]{"txt", "properties", "yml", "yaml"};

    @Override
    public boolean doFilter(ConfigChangeHookRequest request, ConfigChangeHookResponse response) {
        String fileName = request.getFileName ();
        if (fileName != null) {
            int index = fileName.indexOf ('.');
            if (index != -1) {
                String suffix = fileName.substring (index + 1);
                for (String legalSuffix : LEGAL_SUFFIX) {
                    if (suffix.equalsIgnoreCase (legalSuffix)) {
                        System.out.println ("legal name " + fileName);
                        return true;
                    }
                }
            }
            System.out.println ("illegal name " + fileName);
            return false;
        }
        return true;
    }
}
