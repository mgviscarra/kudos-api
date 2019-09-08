package com.mgvr.kudos.api.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mgvr.kudos.api.model.Kudo;
import com.mgvr.kudos.api.repository.KudoRepository;

public class KudoDao {
	@Autowired
	private KudoRepository kudoRepository;
	
	public List<Kudo> getAllkudos() {
		return kudoRepository.findAll();
	}
	
	public void deleteKudo(Long id) {
		kudoRepository.deleteById(id);
	}
	
	public void createKudo(Kudo kudo) {
		kudoRepository.save(kudo);
	}
}
