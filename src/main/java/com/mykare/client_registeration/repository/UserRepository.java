package com.mykare.client_registeration.repository;

import com.mykare.client_registeration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    @Override
    Optional<User> findById(Integer integer);

    // Find a user by their email
    User findByEmail(String email);

    // Delete a user by their email
    void deleteByEmail(String email);


}
