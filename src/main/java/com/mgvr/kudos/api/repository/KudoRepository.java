package com.mgvr.kudos.api.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.mgvr.kudos.api.model.Kudo;

public interface KudoRepository extends MongoRepository<Kudo, Long> {

}
