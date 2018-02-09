package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.config.security.JwtAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;

/**
 * Exception that can be thrown when {@link JwtAuthenticationProvider} API user tries to retrieve user
 * with wrong authentication token. {@link GlobalExceptionController} catches
 * this exception.
 */
public class TokenMalformedException extends AuthenticationException {

    public TokenMalformedException(String s) {
        super(s);
    }
}
