package com.mgvr.kudos.api.dao;

import java.util.List;

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
			query.addCriteria(Criteria.where("id").is("1"));
			Update update = new Update();
			update.set("seq", seq.getSeq()+1);
			mongoTemplate.updateFirst(query, update,DatabaseSequence.class );
		}
		
		return seq.getSeq();
    }
}
