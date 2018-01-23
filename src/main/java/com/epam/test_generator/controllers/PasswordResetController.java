package com.epam.test_generator.controllers;

import com.epam.test_generator.dao.interfaces.PasswordResetTokenDAO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.entities.PasswordResetToken;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.UserService;
import com.epam.test_generator.services.exceptions.JwtTokenMalformedException;
import com.epam.test_generator.services.exceptions.JwtTokenMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PasswordResetController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    PasswordResetTokenDAO passwordResetTokenDAO;

    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity passwordReset(@RequestBody @Valid PasswordResetDTO passwordResetDTO) {
        String token = passwordResetDTO.getToken();
        PasswordResetToken resetToken = passwordResetTokenDAO.findByToken(token);
        User user = resetToken.getUser();
        String updatedPassword = passwordEncoder.encode(passwordResetDTO.getPassword());
        userService.updatePassword(updatedPassword, user.getEmail());
        passwordResetTokenDAO.delete(resetToken);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity displayResetPasswordPage(@RequestParam String token) {

        PasswordResetToken resetToken = passwordResetTokenDAO.findByToken(token);
        if (resetToken == null) {
            throw new JwtTokenMissingException("Could not find password reset token.");
        } else if (resetToken.isExpired()) {
            throw new JwtTokenMalformedException("Token has expired, please request a new password reset.");
        }
        return new ResponseEntity<>(token, HttpStatus.OK);

    }
}
