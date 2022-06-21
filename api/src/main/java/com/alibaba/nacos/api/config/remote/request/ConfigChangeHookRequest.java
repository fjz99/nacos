package com.alibaba.nacos.api.config.remote.request;


import com.alibaba.nacos.api.config.ConfigType;

public class ConfigChangeHookRequest extends ConfigChangeNotifyRequest {
    private ChangeType changeType;
    private ConfigType configType;
    private String content;
    private String fileName;

    public ConfigChangeHookRequest() {

    }

    private ConfigChangeHookRequest(ChangeType changeMode, ConfigType configType,
                                    String content, String fileName,
                                    String dataId, String group, String tenant) {
        this.changeType = changeMode;
        this.configType = configType;
        this.content = content;
        this.fileName = fileName;
        this.dataId = dataId;
        this.group = group;
        this.tenant = tenant;
    }

    public static ConfigChangeHookRequest modify(ConfigType configType, String dataId, String group, String tenant, String content) {
        return new ConfigChangeHookRequest (ChangeType.MODIFY, configType, content, null, dataId, group, tenant);
    }

    public static ConfigChangeHookRequest publishFile(ConfigType configType, String dataId, String group, String tenant, String fileName) {
        return new ConfigChangeHookRequest (ChangeType.PUBLISH_FILE, configType, null, fileName, dataId, group, tenant);
    }

    public static ConfigChangeHookRequest publishRaw(ConfigType configType, String dataId, String group, String tenant, String content) {
        return new ConfigChangeHookRequest (ChangeType.PUBLISH_RAW, configType, content, null, dataId, group, tenant);
    }

    public static ConfigChangeHookRequest delete(ConfigType configType, String dataId, String group, String tenant) {
        return new ConfigChangeHookRequest (ChangeType.DELETE, configType, null, null, dataId, group, tenant);
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    public enum ChangeType {
        PUBLISH_RAW, PUBLISH_FILE, MODIFY, DELETE
    }
}
