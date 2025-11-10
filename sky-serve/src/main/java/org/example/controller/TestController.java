package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.websocket.MyWebSocketHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    MyWebSocketHandle myWebSocketHandle;
    @PostMapping("/notifyWeb")
    public void notifyWeb(){
        log.info("webSockectSession"+MyWebSocketHandle.SESSION_MAP);

        WebSocketSession wsOne = MyWebSocketHandle.SESSION_MAP.get("1111");
        WebSocketSession wsTwo = MyWebSocketHandle.SESSION_MAP.get("2222");
        try {
            wsOne.sendMessage(new TextMessage("你收到消息了（1111发送）"));
            wsTwo.sendMessage(new TextMessage("这是什么玩意（2222）"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/notifyClose")
    public void notifyClose(){
        log.info("webSockectSession"+MyWebSocketHandle.SESSION_MAP);

        WebSocketSession wsOne = MyWebSocketHandle.SESSION_MAP.get("1111");
        try {
            wsOne.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
