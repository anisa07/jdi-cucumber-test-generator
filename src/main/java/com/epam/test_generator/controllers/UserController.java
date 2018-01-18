package com.epam.test_generator.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.UserService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUserAccount(@RequestBody @Valid LoginUserDTO userDTO) {

        userService.createUser(userDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity resetUserPassword(@RequestParam @Email String email) {

        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException();
        }
        JWTCreator.Builder builder = JWT.create()
                .withIssuer("cucumber")
                .withClaim("id", user.getId())
                .withClaim("CreationDate", new Date());


        return new ResponseEntity(HttpStatus.OK);
    }


}
