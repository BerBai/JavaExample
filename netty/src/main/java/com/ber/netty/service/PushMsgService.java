package com.ber.netty.service;

import com.ber.netty.domain.Message;

/**
 * @Author 鳄鱼儿
 * @Description TODO
 * @date 2022/11/27 17:29
 * @Version 1.0
 */
public interface PushMsgService {

    void push(Message msg);
}
