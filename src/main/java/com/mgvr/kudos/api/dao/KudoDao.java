package com.mgvr.kudos.api.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mgvr.kudos.api.model.Kudo;
import com.mgvr.kudos.api.repository.KudoRepository;

@Repository
public class KudoDao {
	@Autowired
	//private KudoRepository kudoRepository;
	private MongoTemplate mongoTemplate;
	
	public List<Kudo> getAllkudos() {
		return mongoTemplate.findAll(Kudo.class);
	}
	
	public void deleteKudo(Long id) {
		Kudo kudo = new Kudo();
		kudo.setId(id);
		mongoTemplate.remove(kudo);
	}
	
	public void createKudo(Kudo kudo) {
		mongoTemplate.save(kudo);
	}
}
