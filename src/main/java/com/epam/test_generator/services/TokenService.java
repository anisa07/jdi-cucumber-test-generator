package com.epam.test_generator.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.JwtTokenMalformedException;
import com.epam.test_generator.services.exceptions.JwtTokenMissingException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@PropertySource("classpath:application.properties")
@Transactional(noRollbackFor = UnauthorizedException.class)
public class TokenService {

    @Autowired
    EmailService emailService;
    @Autowired
    PasswordService passwordService;

    @Autowired
    private TokenDAO tokenDAO;

    @Resource
    private Environment environment;

    @Autowired
    private UserService userService;

    public DecodedJWT validate(String token)
            throws IOException {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt_secret")))
                .withIssuer("cucumber")
                .build();
        return verifier.verify(token);
    }


    public String getToken(LoginUserDTO loginUserDTO) {

        User user = userService.getUserByEmail(loginUserDTO.getEmail());
        Builder builder = JWT.create()
                .withIssuer("cucumber")
                .withClaim("id", user.getId());
        try {
            return builder.sign(Algorithm.HMAC256(environment.getProperty("jwt_secret")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public Token createToken(User user, Integer minutes) {
        Token token = new Token();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(minutes);

        return tokenDAO.save(token);
    }


    public void checkToken(String token) {
        Token resetToken = tokenDAO.findByToken(token);
        if (resetToken == null) {
            throw new JwtTokenMissingException("Could not find password reset token.");
        } else if (resetToken.isExpired()) {
            throw new JwtTokenMalformedException(
                    "Token has expired, please request a new password reset.");
        }
    }

    public void checkPassword(LoginUserDTO loginUserDTO, HttpServletRequest request) {
        User user = userService.getUserByEmail(loginUserDTO.getEmail());
        if (user == null) {
            throw new UnauthorizedException(
                    "User with email: " + loginUserDTO.getEmail() + " not found.");
        }

        if (user.isLocked()) {
            throw new UnauthorizedException("User Account is locked!");
        }

        if (!(userService.isSamePasswords(loginUserDTO.getPassword(), user.getPassword()))) {
            int attempts = userService.updateFailureAttempts(user.getId());
            if (user.isLocked()) {
                emailService.sendResetPasswordMessage(user, request);
                throw new UnauthorizedException(
                        "Incorrect password entered " + attempts + " times. "
                                + "User account has been locked! Mail for reset your password was send on your email.");
            }
            throw new UnauthorizedException("Incorrect password! You have " + (UserService.MAX_ATTEMPTS - attempts) + " attempts remaining before your account will be blocked! ");
        }
        userService.invalidateAttempts(user.getId());
    }
}


