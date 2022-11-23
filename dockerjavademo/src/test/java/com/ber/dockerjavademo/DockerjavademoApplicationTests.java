package com.ber.dockerjavademo;

import com.alibaba.fastjson.JSONObject;
import com.ber.dockerjavademo.service.DockerClientService;
import com.github.dockerjava.api.DockerClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DockerjavademoApplicationTests {

    @Autowired
    private DockerClientService dockerClientService;

    @Test
    void dockerTest() {
        DockerClient dockerClient = dockerClientService.connect();
        dockerClientService.imageList(dockerClient).stream().forEach((item)->{
            JSONObject.toJSONString(item);
            System.out.println(JSONObject.toJSONString(item));
        });
//        System.out.println(dockerClientService.pullImage(dockerClient, "nginx:lest"));
//        dockerClientService.inspectImage(dockerClient, "nginx:latest");
//        dockerClientService.pullImage(dockerClient, "nginx:latest");
//        dockerClientService.createContainers(dockerClient,"nginx","nginx");
//        dockerClientService.startContainer(dockerClient,"nginx");
    }

}
