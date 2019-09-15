package com.mgvr.kudos.api.controller;

import java.io.IOException;
import java.util.List;

import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.ApiMessages;
import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.KudosApiRoutes;
import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.RabbitmqExchangeName;
import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.RabbitmqRoutingKeys;
import com.mgvr.kudos.api.model.User;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgvr.kudos.api.dao.KudoDao;
import com.mgvr.kudos.api.messaging.Sender;
import com.mgvr.kudos.api.model.Kudo;

@RestController
@RequestMapping(KudosApiRoutes.BASE_ROUTE)
public class KudoController {
	@Autowired
	private KudoDao dao;
	@Autowired
	private Sender sender;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private DirectExchange exchange;
	
	@PostMapping(KudosApiRoutes.POST_KUDO)
	public String saveKudo(@RequestBody Kudo kudo) throws IOException {
		User userFrom = new User();
		userFrom.setRealName(kudo.getFuente());
		User userTo  = new User();
		userTo.setRealName(kudo.getDestino());
		String responseUserFrom =  (String)rabbitTemplate.convertSendAndReceive
				(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_USER_REQUEST, userFrom);
		String responseUserTo =  (String)rabbitTemplate.convertSendAndReceive
				(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_USER_REQUEST, userTo);
		if(responseUserFrom==null || responseUserTo==null){
			return ApiMessages.USERS_DONT_EXIST;
		}
		dao.createKudo(kudo);
		return ApiMessages.CREATED;
	}
	@DeleteMapping(KudosApiRoutes.DELETE_KUDO)
	public String deleteKudo(@PathVariable String id) {
		dao.deleteKudo(Long.parseLong(id));
		return ApiMessages.DELETED;
	}
	@GetMapping(KudosApiRoutes.GET_KUDOS)
	public List<Kudo> getKudos(){
		return dao.getAllkudos();
	}
}
