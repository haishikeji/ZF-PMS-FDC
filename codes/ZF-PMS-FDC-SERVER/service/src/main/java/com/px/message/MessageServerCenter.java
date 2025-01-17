package com.px.message;

import com.px.entity.Message;
import com.px.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2023/6/30
 * created with IntelliJ IDEA.
 */
@Service
public abstract class MessageServerCenter<T> {

    @Autowired
    private IMessageService messageService;

    /**
     * 发送消息
     * @param t
     * @return
     */
    public abstract Boolean sendMessage(T t);

    /**
     * 读取消息
     * @param id
     * @return
     */
    public Boolean readMessage(Long id){
        return messageService.updateRead(id);
    }

    public List<Message> messageRedPoint(){
        return messageService.messageRedPoint();
    }

    /**
     * 销毁消息
     * @param id
     * @return
     */
    public Boolean destroyMessage(Long id){
        return Boolean.TRUE;
    }

}
