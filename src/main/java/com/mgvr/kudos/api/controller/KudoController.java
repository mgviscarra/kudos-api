package com.mgvr.kudos.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgvr.kudos.api.dao.KudoDao;
import com.mgvr.kudos.api.model.Kudo;


@RestController
@RequestMapping("/kudos-api")
public class KudoController {
	@Autowired
	private KudoDao dao;
	
	@PostMapping("/kudo")
	public String saveKudo(@RequestBody Kudo kudo) {
		dao.createKudo(kudo);
		return "success";
	}
	
	@DeleteMapping("/kudo/{id}")
	public String deleteKudo(@PathVariable String id) {
		dao.deleteKudo(Long.parseLong(id));
		return "success";
	}
	
	@GetMapping("/kudo")
	public List<Kudo> getKudos(){
		return dao.getAllkudos();
	}

}
