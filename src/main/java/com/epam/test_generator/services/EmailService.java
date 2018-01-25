package com.epam.test_generator.services;

import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Transactional
@PropertySource("classpath:email.messages.properties")
@Component
public class EmailService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    PasswordService passwordService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Resource
    private Environment environment;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendRegistrationMessage(User user, HttpServletRequest request) {
        Token userConformationToken = tokenService.createToken(user,1440);
        String confirmUrl = passwordService.createConfirmUrl(request, userConformationToken);
        String subject = environment.getProperty("subject.registration.message");
        String text = environment.getProperty("registration.message");
        text = String.format(text, "Maksim", "Stelmakh", "https://www.epam.com/",
                confirmUrl);
        sendSimpleMessage(user.getEmail(), subject, text);
    }

    public void sendResetPasswordMessage(User user, HttpServletRequest request) {
        Token token = tokenService.createToken(user,15);
        String resetUrl = passwordService.createResetUrl(request, token);
        String subject = environment.getProperty("subject.password.message");
        String text = environment.getProperty("reset.password.message");
        text = String.format(text, "Maksim", "Stelmakh", resetUrl,
            javaMailSender.getUsername());
        sendSimpleMessage(user.getEmail(), subject, text);


    }
}