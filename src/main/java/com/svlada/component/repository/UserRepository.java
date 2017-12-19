package com.svlada.component.repository;

import com.svlada.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u left join fetch u.roles r where u.username=:username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "select count(1) from User u  where u.lastLoginDate between :startDate and :endDate")
    Long countByLastLoginDateDateBetween(@Param("startDate") Date startDate,@Param("endDate")  Date endDate);

    User findOneByOpenId(String openId);
}
