package com.finance.finance_dashboard.repository;

import com.finance.finance_dashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL
"""
    )
    Optional<User> findByActiveEmail(String email);

    @Query("""
SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NOT NULL
"""
    )
    Optional<User> findByDeactivateEmail(String email);

    Optional<User> findByEmail(String email);
}
