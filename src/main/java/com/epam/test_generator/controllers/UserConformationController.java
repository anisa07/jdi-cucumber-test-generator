package com.epam.test_generator.controllers;

import com.epam.test_generator.dao.interfaces.PasswordResetTokenDAO;
import com.epam.test_generator.entities.PasswordResetToken;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.PasswordService;
import com.epam.test_generator.services.TokenService;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserConformationController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/confirmAccount")
    public ResponseEntity<String> displayResetPasswordPage(@RequestParam String token) {
        tokenService.checkToken(token);
        PasswordResetToken tokenByName = passwordService.getTokenByName(token);
        User user = tokenByName.getUser();
        userService.checkUserExist(user);
        user.setLocked(false);
        userService.saveUser(user);
        passwordResetTokenDAO.delete(tokenByName);

        return new ResponseEntity<>("Your account is verified!", HttpStatus.OK);
    }
}
