package com.ber.netty.controller;

import com.ber.netty.domain.Message;
import com.ber.netty.service.PushMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 鳄鱼儿
 * @Description TODO
 * @date 2022/11/27 17:56
 * @Version 1.0
 */

@RestController
@RequestMapping("/")
public class PushController {

    @Autowired
    private PushMsgService pushMsgService;

    @GetMapping("/push")
    public String push() {
        Message message = new Message();
        message.setId("48:b0:2d:07:58:6c");
        message.setMsgType(2);
        message.setContent("test");
        pushMsgService.push(message);
        return "给客户端发送消息";
    }
}
