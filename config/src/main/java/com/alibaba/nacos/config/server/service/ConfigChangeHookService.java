package com.alibaba.nacos.config.server.service;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.remote.request.ConfigChangeHookRequest;
import com.alibaba.nacos.api.config.remote.response.ConfigChangeHookResponse;
import com.alibaba.nacos.api.remote.response.Response;
import com.alibaba.nacos.common.remote.exception.ConnectionAlreadyClosedException;
import com.alibaba.nacos.config.server.model.ConfigAllInfo;
import com.alibaba.nacos.config.server.remote.ConfigChangeListenContext;
import com.alibaba.nacos.config.server.service.repository.PersistService;
import com.alibaba.nacos.config.server.utils.GroupKey;
import com.alibaba.nacos.core.remote.Connection;
import com.alibaba.nacos.core.remote.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ConfigChangeHookService {
    private static final int RETRY_TIMES = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger (ConfigChangeHookService.class);

    @Autowired
    private ConfigChangeListenContext configChangeListenContext;
    @Autowired
    private ConnectionManager connectionManager;
    @Autowired
    private PersistService persistService;

    public boolean trigRemoteHooks(String dataId, String group, String tenant, String content,
                                   String fileName, String type, ConfigChangeHookRequest.ChangeType changeType) {
        ConfigChangeHookRequest request;
        ConfigType configType = null;
        if (type != null) {
            configType = ConfigType.valueOf (type.toUpperCase ());
        }
        switch (changeType) {
            case DELETE:
                ConfigAllInfo info = persistService.findConfigAllInfo (dataId, group, tenant);
                if (info != null) {
                    configType = ConfigType.valueOf (info.getType ().toUpperCase ());
                }
                request = ConfigChangeHookRequest.delete (configType, dataId, group, tenant);
                break;
            case MODIFY:
                request = ConfigChangeHookRequest.modify (configType, dataId, group, tenant, content);
                break;
            case PUBLISH_RAW:
                request = ConfigChangeHookRequest.publishRaw (configType, dataId, group, tenant, content);
                break;
            case PUBLISH_FILE:
                request = ConfigChangeHookRequest.publishFile (configType, dataId, group, tenant, fileName);
                break;
            default:
                throw new AssertionError ();
        }
        return trigRemoteHooks (dataId, group, tenant, request);
    }

    /**
     * 对listener进行轮询，负载均衡
     */
    private boolean trigRemoteHooks(String dataId, String group, String tenant, ConfigChangeHookRequest request) {
        String key = GroupKey.getKeyTenant (dataId, group, tenant);
        Set<String> listeners = configChangeListenContext.getListeners (key);
        if (listeners == null || listeners.size () == 0) {
            System.out.println ("没有listener");
            return true;
        }

        List<String> list = new ArrayList<> (listeners);
        Collections.shuffle (list);

        for (int i = 0; i < RETRY_TIMES; i++) {
            for (String client : list) {
                Connection connection = connectionManager.getConnection (client);
                if (connection != null) {
                    try {
                        Response response = connection.request (request, 1500L);
                        if (!(response instanceof ConfigChangeHookResponse)) {
                            throw new RuntimeException ("type incorrect");
                        }
                        ConfigChangeHookResponse hookResponse = (ConfigChangeHookResponse) response;
                        if (hookResponse.isValid ()) {
                            LOGGER.info ("valid config group {}, dataId {}, tenant {}",
                                    request.getGroup (), request.getDataId (), request.getTenant ());
                            return true;
                        } else {
                            LOGGER.error ("invalid config", hookResponse.getExp ());
                            return false;
                        }
                    } catch (ConnectionAlreadyClosedException e) {
                        connectionManager.unregister (client);
                        LOGGER.error ("send config error", e);
                    } catch (Exception e) {
                        LOGGER.error ("send config error", e);
                    }
                }
            }
        }

        LOGGER.error ("config listener不可达, group {}, dataId {}, tenant {}", group, dataId, tenant);
        return false;
    }

//    class TriggerHookTask implements Runnable {
//
//
//        ConfigChangeHookRequest request;
//
//        int maxRetryTimes;
//
//        int tryTimes = 0;
//
//        String clientIp;
//
//        String appName;
//
//        Iterator<String> clients;
//
//
//        public TriggerHookTask(ConfigChangeHookRequest request, int maxRetryTimes,
//                               String clientIp, String appName, Connection connection, Set<String> clients) {
//            this.request = request;
//            this.maxRetryTimes = maxRetryTimes;
//            this.clientIp = clientIp;
//            this.appName = appName;
//
//            //打乱
//            List<String> list = new ArrayList<> (clients);
//            Collections.shuffle (list);
//            this.clients = list.iterator ();
//        }
//
//        public boolean isOverTimes() {
//            return maxRetryTimes > 0 && this.tryTimes >= maxRetryTimes;
//        }
//
//        @Override
//        public void run() {
//            tryTimes++;
//            try {
//                connection.asyncRequest (request, new AbstractRequestCallBack (t) {
//
//                    @Override
//                    public Executor getExecutor() {
//                        return executor;
//                    }
//
//                    @Override
//                    public void onResponse(Response response) {
//                        if (response.isSuccess ()) {
//                            requestCallBack.onSuccess ();
//                        } else {
//                            requestCallBack.onFail (new NacosException (response.getErrorCode (), response.getMessage ()));
//                        }
//                    }
//
//                    @Override
//                    public void onException(Throwable e) {
//                        requestCallBack.onFail (e);
//                    }
//                });
//            } catch (ConnectionAlreadyClosedException e) {
//                connectionManager.unregister (connectionId);
//                requestCallBack.onSuccess ();
//            } catch (Exception e) {
//                Loggers.REMOTE_DIGEST
//                        .error ("error to send push response to connectionId ={},push response={}", connectionId,
//                                request, e);
//                requestCallBack.onFail (e);
//            }
//
//            rpcPushService.pushWithCallback (connectionId, request, new AbstractPushCallBack (3000L) {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onFail(Throwable e) {
//                    Loggers.REMOTE_PUSH.warn ("Push fail", e);
//                    push (RpcConfigChangeNotifier.RpcPushTask.this);
//                }
//
//            }, ConfigExecutor.getClientConfigNotifierServiceExecutor ());
//
//        }
//
//    }
}
