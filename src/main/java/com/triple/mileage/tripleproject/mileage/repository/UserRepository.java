package com.triple.mileage.tripleproject.mileage.repository;

import com.triple.mileage.tripleproject.mileage.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);

    List<User> findAll();
    @Transactional
    @Modifying
    @Query("Update User u Set u.point = :point where u.id = :id")
    int updateUserPoint(Long id, int point);
}
