package com.epam.test_generator.dao.interfaces;


import com.epam.test_generator.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

}