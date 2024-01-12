package com.bbc.zuber.repository;

import com.bbc.zuber.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM Users u WHERE u.is_deleted = true", nativeQuery = true)
    Page<User> findAllDeleted(Pageable pageable);

    boolean existsByEmail(String email);

    Optional<User> findByUuid(UUID uuid);
}
