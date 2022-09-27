package com.analyst.db.repository;

import com.analyst.db.entity.ClientRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRecordRepository extends MongoRepository<ClientRecord, String> {
    ClientRecord findAllByClientID(String clientID);
}
