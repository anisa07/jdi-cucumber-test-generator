package com.epam.test_generator.controllers;

import com.epam.test_generator.entities.PasswordResetToken;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.UserService;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
public class PasswordForgotController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;


    @RequestMapping(value = "/passwordForgot", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")

    public void passwordForgot(@RequestParam @Email String email, HttpServletRequest request)
            throws Exception {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UnauthorizedException(
                    "User with email: " + email + " not found.");
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(15);

        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String reset = url + "/reset-password?token=" + token.getToken();
        emailService.sendSimpleMessage(email, "Reset password", reset);


    }
}
