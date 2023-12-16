package com.olamiredev.accelepay.repository;

import com.olamiredev.accelepay.enums.UserType;
import com.olamiredev.accelepay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByApiKeyAndUserType(String apiKey, UserType userType);

}
