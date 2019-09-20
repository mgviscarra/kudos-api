package com.mgvr.kudos.api.service;

import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.DbFields;
import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.RabbitmqExchangeName;
import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.RabbitmqRoutingKeys;
import com.mgvr.kudos.api.dao.KudoDao;
import com.mgvr.kudos.api.messaging.Sender;
import com.mgvr.kudos.api.model.Kudo;
import com.mgvr.kudos.api.model.User;
import com.monitorjbl.json.JsonResult;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.Match;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KudoService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    Sender sender;

    @Autowired
    private KudoDao dao;

    public boolean createKudo(Kudo kudo){
        User userFrom = new User();
        userFrom.setRealName(kudo.getFuente());
        User userTo  = new User();
        userTo.setRealName(kudo.getDestino());
        String responseUserFrom =  (String)rabbitTemplate.convertSendAndReceive
                (RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_USER_REQUEST, userFrom);
        String responseUserTo =  (String)rabbitTemplate.convertSendAndReceive
                (RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_USER_REQUEST, userTo);
        if(responseUserFrom==null || responseUserTo==null){
            return false;
        }
        dao.createKudo(kudo);
        sender.sendMessage(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_STATS_API,kudo);
        return true;
    }

    public void deleteKudo(String id){
        dao.deleteKudo(Long.parseLong(id));
    }

    public List<Kudo> getKudos(){
        List<Kudo> kudos = dao.getAllkudos();
        JsonResult json = JsonResult.instance();
        List<Kudo> listKudos= json.use(JsonView.with(kudos)
                .onClass(User.class, Match.match()
                        .exclude(DbFields.FECHA)
                )).returnValue();
        return listKudos;
    }
}
