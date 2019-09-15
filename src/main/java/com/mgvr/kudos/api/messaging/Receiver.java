package com.mgvr.kudos.api.messaging;

import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.ApiMessages;
import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.RabbitmqQueueNames;
import com.mgvr.kudos.api.dao.KudoDao;
import com.mgvr.kudos.api.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;

public class Receiver {

    @Autowired
    private KudoDao dao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitmqQueueNames.KUDO_RPC_KUDO_DELETE_REQUEST)
    @SendTo(RabbitmqQueueNames.KUDO_RPC_USER_API)
    public String receiveKudoDeleteRequest(User message){
        System.out.println("Recibiendo mensaje: "+message.getRealName());
        dao.deleteKudoByFrom(message.getRealName());
        dao.deleteKudoByTo(message.getRealName());
        return ApiMessages.DELETED;
    }
}
