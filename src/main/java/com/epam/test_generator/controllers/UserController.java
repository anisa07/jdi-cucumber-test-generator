package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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



}
