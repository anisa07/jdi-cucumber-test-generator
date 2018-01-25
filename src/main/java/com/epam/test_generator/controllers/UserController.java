package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.EmailService;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUserAccount(@RequestBody @Valid LoginUserDTO userDTO, HttpServletRequest request) {

        User user = userService.createUser(userDTO);
        emailService.sendRegistrationMessage(user, request);
        return new ResponseEntity(HttpStatus.OK);
    }


}
