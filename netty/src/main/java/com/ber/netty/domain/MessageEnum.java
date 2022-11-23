package com.ber.netty.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author 鳄鱼儿
 * @Description 消息类型
 * @date 2022/11/22 21:04
 * @Version 1.0
 */

public enum MessageEnum {
    CONNECT(1, "心跳消息"),
    STATE(2, "设备状态");

    public final Integer type;
    public final String content;

    MessageEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    // case中判断使用
    public static MessageEnum getStructureEnum(Message msg) {
        Integer type = Optional.ofNullable(msg)
                .map(Message::getMsgType)
                .orElse(0);
        if (type == 0) {
            return null;
        } else {
            List<MessageEnum> objectEnums = Arrays.stream(MessageEnum.values())
                    .filter((item) -> item.getType() == type)
                    .distinct()
                    .collect(Collectors.toList());
            if (objectEnums.size() > 0) {
                return objectEnums.get(0);
            }
            return null;
        }
    }

    public Integer getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
