package com.ber.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 鳄鱼儿
 * @Description Netty配置项
 * @date 2022/11/22 16:34
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {

    /**
     * boss线程数量 默认为cpu线程数*4
     */
    private Integer boss;

    /**
     * worker线程数量 默认为cpu线程数*2
     */
    private Integer worker;

    /**
     * 连接超时时间 默认为30s
     */
    private Integer timeout = 30000;

    /**
     * 服务器主端口 默认9000
     */
    private Integer port = 18023;

    /**
     * 服务器备用端口
     */
    private Integer portSalve = 18026;

    /**
     * 服务器地址 默认为本地
     */
    private String host = "127.0.0.1";

    public Integer getBoss() {
        return boss;
    }

    public void setBoss(Integer boss) {
        this.boss = boss;
    }

    public Integer getWorker() {
        return worker;
    }

    public void setWorker(Integer worker) {
        this.worker = worker;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPortSalve() {
        return portSalve;
    }

    public void setPortSalve(Integer portSalve) {
        this.portSalve = portSalve;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
