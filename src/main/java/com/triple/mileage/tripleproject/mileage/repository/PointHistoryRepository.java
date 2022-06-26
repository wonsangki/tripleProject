package com.triple.mileage.tripleproject.mileage.repository;

import com.triple.mileage.tripleproject.mileage.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAll();
}
