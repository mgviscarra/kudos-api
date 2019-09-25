package com.mgvr.kudos.api.dao;

import java.util.List;

import com.mgvr.kudos.api.com.mgvr.kudos.api.constants.DbFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mgvr.kudos.api.model.DatabaseSequence;
import com.mgvr.kudos.api.model.Kudo;

@Repository
public class KudoDao {
	@Autowired
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
		long seq = getNextSequence();
		kudo.setId(seq);
		mongoTemplate.save(kudo);
	}
	
	public long getNextSequence()
    {
		DatabaseSequence seq = mongoTemplate.findById("1", DatabaseSequence.class);
		if(seq==null) {
			seq = new DatabaseSequence();
			seq.setId("1");
			seq.setSeq(1);
			mongoTemplate.save(seq);
		} else {
			Query query = new Query();
			query.addCriteria(Criteria.where(DbFields.ID).is("1"));
			Update update = new Update();
			update.set(DbFields.SEQ, seq.getSeq()+1);
			mongoTemplate.updateFirst(query, update,DatabaseSequence.class );
		}
		
		return seq.getSeq();
    }

	public void deleteKudoByFrom(String from){
		Query query = new Query();
		query.addCriteria(Criteria.where(DbFields.FUENTE).is(from));
		mongoTemplate.remove(query, Kudo.class);
	}

	public void deleteKudoByTo(String to){
		Query query = new Query();
		query.addCriteria(Criteria.where(DbFields.DESTINO).is(to));
		mongoTemplate.remove(query, Kudo.class);
	}

	public List<Kudo> getKudosByRealName(String realName){
		Query query = new Query();
		query.addCriteria(Criteria.where(DbFields.DESTINO).is(realName));
		List<Kudo> kudos = mongoTemplate.find(query, Kudo.class);
		return kudos;
	}

	public List<Kudo> getKudosByFrom(String from){
		Query query = new Query();
		query.addCriteria(Criteria.where(DbFields.FUENTE).is(from));
		List<Kudo> kudos = mongoTemplate.find(query,Kudo.class);
		return kudos;
	}
}
