package com.vcc.ob.repository;

import com.vcc.ob.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(" select u from User u where u.userId = :user_id ")
    User getUserByUserId(@Param("user_id") String userId);

    @Query(" select u from User u where u.userId = :user_id ")
    Optional<User> findUserByUserId(@Param("user_id") String userId);
}
