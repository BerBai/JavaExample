package com.ber.netty;

import com.ber.netty.domain.Message;
import com.ber.netty.service.PushMsgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NettyApplicationTests {
    @Autowired
    private PushMsgService pushMsgService;

    @Test
    void pushMsg() {
        Message message = new Message();
        message.setId("48:b0:2d:07:58:6c");
        message.setMsgType(2);
        message.setContent("test");
        pushMsgService.push(message);
    }

}
