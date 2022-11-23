package com.ber.dockerjavademo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ber.dockerjavademo.domain.DockerProp;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

/**
 * @Author 鳄鱼儿
 * @Description TODO
 * @date 2022/11/8 13:43
 * @Version 1.0
 */

@Service
public class DockerClientService {
    @Value("${docker.host}")
    private String HOST;

    @Value("${docker.api_version}")
    private String API_VERSION;
    @Value("${docker.registry_url}")
    private String REGISTRY_URL;

    @Value("${docker.docker_tls_verify}")
    private String DOCKER_TLS_VERIFY;

    /**
     * 连接docker服务器
     *
     * @return
     */
    public DockerClient connect() {
        // 配置docker CLI的一些选项
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerTlsVerify(DOCKER_TLS_VERIFY)
                .withDockerHost(HOST)
                // 与docker版本对应，参考https://docs.docker.com/engine/api/#api-version-matrix
                // 或者通过docker version指令查看api version
                .withApiVersion(API_VERSION)
                .withRegistryUrl(REGISTRY_URL)
                .build();

        // 创建DockerHttpClient
        DockerHttpClient httpClient = new ApacheDockerHttpClient
                .Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        Info info = dockerClient.infoCmd().exec();
        String infoStr = JSONObject.toJSONString(info);
        System.out.println("docker的环境信息如下：=================");
        System.out.println(infoStr);
        return dockerClient;
    }

    /**
     * 拉取镜像
     *
     * @param client
     * @param imageName
     * @return
     * @throws InterruptedException
     */
    public boolean pullImage(DockerClient client, String imageName) {
        boolean isSuccess = false;
        try {
            isSuccess = client.pullImageCmd(imageName)
                    .start()
                    .awaitCompletion(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            return isSuccess;
        }
    }

    /**
     * 查看镜像详细信息
     *
     * @param client
     * @param imageName
     * @return
     */
    public String inspectImage(DockerClient client, String imageName) {
        InspectImageResponse response = client.inspectImageCmd(imageName).exec();
        System.out.println(response.toString());
        System.out.println(JSONObject.toJSONString(response));
        return JSONObject.toJSONString(response);
    }

    /**
     * 删除镜像
     *
     * @param client
     * @param imageName
     */
    public void removeImage(DockerClient client, String imageName) {
        client.removeImageCmd(imageName).exec();
    }

    /**
     * 构建镜像
     *
     * @param client
     * @param dockerProp
     * @return
     */
    public String buildImage(DockerClient client, DockerProp dockerProp) {
        ImmutableSet<String> tag = ImmutableSet.of(dockerProp.getImageName() + ":" + dockerProp.getImageTag());
        String imageId = client.buildImageCmd(new File(dockerProp.getDockerfilePath()))
                .withTags(tag)
                .start()
                .awaitImageId();
        return imageId;
    }

    /**
     * 给镜像打tag
     *
     * @param client
     * @param dockerProp
     */
    public void tagImage(DockerClient client, DockerProp dockerProp) {
        client.tagImageCmd(dockerProp.getImageName(), dockerProp.getRespository(), dockerProp.getTag()).exec();
    }

    /**
     * 加载镜像文件
     *
     * @param client
     * @param inputStream
     */
    public void loadImage(DockerClient client, InputStream inputStream) {
        client.loadImageCmd(inputStream).exec();
    }

    /**
     * 获取镜像列表
     *
     * @param client
     * @return
     */
    public List<Image> imageList(DockerClient client) {
        List<Image> imageList = client.listImagesCmd().withShowAll(true).exec();
        return imageList;
    }

    /**
     * 创建容器
     *
     * @param client
     * @return
     */
    public CreateContainerResponse createContainers(DockerClient client, DockerProp dockerProp) {
        // 端口绑定
        Map<Integer, Integer> portMap = Optional.ofNullable(dockerProp).map(DockerProp::getPartMap).orElse(new HashMap<>());
        Iterator<Map.Entry<Integer, Integer>> iterator = portMap.entrySet().iterator();
        List<PortBinding> portBindingList = new ArrayList<>();
        List<ExposedPort> exposedPortList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            ExposedPort tcp = ExposedPort.tcp(entry.getKey());
            Ports.Binding binding = Ports.Binding.bindPort(entry.getValue());
            PortBinding ports = new PortBinding(binding, tcp);
            portBindingList.add(ports);
            exposedPortList.add(tcp);
        }

        CreateContainerResponse container = client.createContainerCmd(dockerProp.getImageName())
                .withName(dockerProp.getContainerName())
                .withHostConfig(newHostConfig().withPortBindings(portBindingList))
                .withExposedPorts(exposedPortList).exec();

        return container;
    }


    /**
     * 启动容器
     *
     * @param client
     * @param containerId
     */
    public void startContainer(DockerClient client, String containerId) {
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 停止容器
     *
     * @param client
     * @param containerId
     */
    public void stopContainer(DockerClient client, String containerId) {
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     *
     * @param client
     * @param containerId
     */
    public void removeContainer(DockerClient client, String containerId) {
        client.removeContainerCmd(containerId).exec();
    }
}
