package com.px.aspect;

import com.px.dto.OrderReportDTO;
import com.px.dto.WareHouseDTO;
import com.px.enums.ProductionEnum;
import com.px.message.MessageTypeEnum;
import com.px.message.annotation.SendMessage;
import com.px.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

import static com.px.message.MessageTypeEnum.*;

/**
 * 类说明：
 *
 * @author: wucl
 * @since: 2024/5/21
 * created with IntelliJ IDEA.
 */
@Slf4j
@Component
@Aspect
public class SendMessageAOP {

    @Autowired
    private IMessageService messageService;

    @Around("@annotation(sendMessage)")
    public Object sendMessageAround(ProceedingJoinPoint joinPoint, SendMessage sendMessage) throws Throwable {
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        MessageTypeEnum messageTypeEnum = sendMessage.messageType();
        if (messageTypeEnum.equals(MAJOR_DECLARE_REMIND)){
            messageService.packagingMajorDeclareRemind((Long) proceed);
        }
        if (messageTypeEnum.equals(MAJOR_OUT_REPERTORY_REMIND) && (Boolean) proceed){
            Object arg = joinPoint.getArgs()[0];
            Field field = arg.getClass().getDeclaredField("taskData");
            field.setAccessible(true);
            WareHouseDTO wareHouseDTO = (WareHouseDTO) field.get(arg);
            if (wareHouseDTO.getRepertoryState() && !wareHouseDTO.getProductionType().equals(ProductionEnum.STATEMENT.name())){
                messageService.packagingMajorOutRepertoryRemind(wareHouseDTO);
            }
        }
        if (messageTypeEnum.equals(MAJOR_ALLOT_COMMISSION_REMIND1) && (Boolean) proceed){
            Object arg = joinPoint.getArgs()[0];
            List<OrderReportDTO> list = (List<OrderReportDTO>) arg;
            messageService.packagingMajorAllotCommissionRemind(list);
        }
        if (messageTypeEnum.equals(MAJOR_ALLOT_COMMISSION_REMIND2) && (Boolean) proceed){
            Object arg = joinPoint.getArgs()[0];
            OrderReportDTO orderReportDTO = (OrderReportDTO) arg;
            messageService.packagingMajorAllotCommissionRemind1(orderReportDTO);
        }
        return proceed;
    }
}
