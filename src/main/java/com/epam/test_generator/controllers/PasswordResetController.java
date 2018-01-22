package com.epam.test_generator.controllers;

import com.epam.test_generator.dao.interfaces.PasswordResetTokenDAO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.entities.PasswordResetToken;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PasswordResetController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordResetTokenDAO passwordResetTokenDAO;

    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void passwordReset(@RequestBody @Valid PasswordResetDTO passwordResetDTO) {
        String token = passwordResetDTO.getToken();
        PasswordResetToken resetToken = passwordResetTokenDAO.findByToken(token);
        User user = resetToken.getUser();
        String updatedPassword = passwordEncoder.encode(passwordResetDTO.getPassword());
        userService.updatePassword(updatedPassword,user.getEmail());
        passwordResetTokenDAO.delete(resetToken);


    }

    @GetMapping
    public ResponseEntity displayResetPasswordPage(@RequestParam String token,
                                                   Model model) {

        PasswordResetToken resetToken = passwordResetTokenDAO.findByToken(token);
        if (resetToken == null) {
            model.addAttribute("error", "Could not find password reset token.");
        } else if (resetToken.isExpired()) {
            model.addAttribute("error", "Token has expired, please request a new password reset.");
        } else {
            model.addAttribute("token", resetToken.getToken());
        }

        return new ResponseEntity(HttpStatus.OK);

    }
}
