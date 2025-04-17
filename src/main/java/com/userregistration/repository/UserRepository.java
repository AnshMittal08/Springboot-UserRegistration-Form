package com.userregistration.repository;

import com.userregistration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAadhaarNumber(String aadhaarNumber);
    Optional<User> findByAadhaarNumberAndDateOfBirth(String aadhaarNumber, LocalDate dateOfBirth);
}