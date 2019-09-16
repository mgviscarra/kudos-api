package com.mgvr.kudos.api.controller;

import java.io.IOException;
import java.util.List;

import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.*;
import com.mgvr.kudos.api.model.User;
import com.monitorjbl.json.JsonResult;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.Match;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> saveKudo(@RequestBody Kudo kudo) throws IOException {
		User userFrom = new User();
		userFrom.setRealName(kudo.getFuente());
		User userTo  = new User();
		userTo.setRealName(kudo.getDestino());
		String responseUserFrom =  (String)rabbitTemplate.convertSendAndReceive
				(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_USER_REQUEST, userFrom);
		String responseUserTo =  (String)rabbitTemplate.convertSendAndReceive
				(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_USER_REQUEST, userTo);
		if(responseUserFrom==null || responseUserTo==null){
			return new ResponseEntity<>(ApiMessages.USERS_DONT_EXIST, HttpStatus.CONFLICT);
		}
		dao.createKudo(kudo);
		return new ResponseEntity<>(ApiMessages.CREATED, HttpStatus.OK);
	}
	@DeleteMapping(KudosApiRoutes.DELETE_KUDO)
	public ResponseEntity<String> deleteKudo(@PathVariable String id) {
		dao.deleteKudo(Long.parseLong(id));
		return new ResponseEntity<>(ApiMessages.DELETED, HttpStatus.OK);
	}
	@GetMapping(KudosApiRoutes.GET_KUDOS)
	public ResponseEntity<List<Kudo>> getKudos(){
		List<Kudo> kudos = dao.getAllkudos();
		JsonResult json = JsonResult.instance();
		List<Kudo> listKudos= json.use(JsonView.with(kudos)
				.onClass(User.class, Match.match()
						.exclude(DbFields.FECHA)
				)).returnValue();
		return new ResponseEntity<>(listKudos, HttpStatus.OK);
	}
}
