package com.ber.dockerjavademo.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 鳄鱼儿
 * @Description Docker相关属性
 * @date 2022/11/8 16:35
 * @Version 1.0
 */

@Data
public class DockerProp {
    private String containerName;
    private String imageName;
    private String imageTag;
    // 端口绑定（宿主机：容器）
    private Map<Integer, Integer> partMap;
    private Map<String, String> pathMap;
    private String env;
    // 挂载分卷
    private List<String> volumes;
    private String dockerfilePath;
    private String respository;
    private String tag;
}
