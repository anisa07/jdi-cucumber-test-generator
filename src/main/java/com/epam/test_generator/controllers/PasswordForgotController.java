package com.epam.test_generator.controllers;

import com.epam.test_generator.entities.PasswordResetToken;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.PasswordService;
import com.epam.test_generator.services.TokenService;
import com.epam.test_generator.services.UserService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PasswordForgotController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/passwordForgot", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity passwordForgot(@RequestParam @Email String email,
                                         HttpServletRequest request) throws Exception {
        User user = userService.getUserByEmail(email);
        userService.checkUserExist(user);
        PasswordResetToken token = tokenService.createPasswordResetToken(user);
        String resetUrl = passwordService.createResetUrl(request, token);
        emailService.sendResetPasswordMessage(user, resetUrl);

        return new ResponseEntity(HttpStatus.OK);
    }
}
