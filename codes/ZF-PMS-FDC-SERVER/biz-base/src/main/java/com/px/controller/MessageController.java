package com.px.controller;

import com.px.message.handler.SystemMessageHandler;
import com.px.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.entity.Message;
import com.px.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 消息
 *
 * @author 品讯科技
 * @since 2023-06-30
 */
@RestController
@RequestMapping("message")
@Slf4j
public class MessageController {

    @Autowired
    private SystemMessageHandler systemMessageHandler;

    @Autowired
    private IMessageService messageService;

    @GetMapping("/notRead")
    public RestResponse<List<Message>> messageRedPoint(){
        List<Message> list = systemMessageHandler.messageRedPoint();
        return RestResponse.data(list);
    }

    /**
     * 更新消息已读状态
     * @param id
     * @return
     */
    @GetMapping("/updateRead/{id}")
    public RestResponse<Boolean> updateRead(@PathVariable("id") Long id){
        Boolean result = systemMessageHandler.readMessage(id);
        return RestResponse.data(result);
    }

    /**
     * 一键已读
     * @return
     */
    @GetMapping("/allRead")
    public RestResponse<Boolean> allRead(){
        Boolean result = messageService.allRead();
        return RestResponse.data(result);
    }
}

