package com.dxfx.netty.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/11 21:18
 * @description
 */
public class ZookeeperFactory {

    public static CuratorFramework client;

    public static String local = "localhost";

    public static String promote = "192.168.248.128";

    public static String port = "2181";

    public static CuratorFramework create() {
        if (client == null) {
            //客户端尝试连接zookeeper服务端3次
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(promote + ":" + port, retryPolicy);
            client.start();
        }
        return client;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = create();
        client.create().forPath("/netty");
    }

}
