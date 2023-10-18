package com.datpd.checkin.repository;

import com.datpd.checkin.entity.TurnHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnHistoryRepository extends JpaRepository<TurnHistoryEntity, Long> {

}
