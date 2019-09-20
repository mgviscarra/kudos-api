package com.mgvr.kudos.api.controller;

import java.io.IOException;
import java.util.List;

import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.*;
import com.mgvr.kudos.api.service.KudoService;
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

import com.mgvr.kudos.api.model.Kudo;

@RestController
@RequestMapping(KudosApiRoutes.BASE_ROUTE)
public class KudoController {

	@Autowired
	KudoService service;

	@PostMapping(KudosApiRoutes.POST_KUDO)
	public ResponseEntity<String> saveKudo(@RequestBody Kudo kudo) throws IOException {
		if(!service.createKudo(kudo)){
			return new ResponseEntity<>(ApiMessages.USERS_DONT_EXIST, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(ApiMessages.CREATED, HttpStatus.OK);
	}
	@DeleteMapping(KudosApiRoutes.DELETE_KUDO)
	public ResponseEntity<String> deleteKudo(@PathVariable String id) {
		service.deleteKudo(id);
		return new ResponseEntity<>(ApiMessages.DELETED, HttpStatus.OK);
	}
	@GetMapping(KudosApiRoutes.GET_KUDOS)
	public ResponseEntity<List<Kudo>> getKudos(){
		List<Kudo> kudos = service.getKudos();
		return new ResponseEntity<>(kudos, HttpStatus.OK);
	}
}
