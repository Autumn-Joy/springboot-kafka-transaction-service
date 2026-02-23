package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRecordRepository extends CrudRepository<TransactionRecord, Long> {

}
