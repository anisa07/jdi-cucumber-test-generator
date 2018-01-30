package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMissingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private Token token;

    @Mock
    private PasswordResetDTO passwordResetDTO;

    @Mock
    private TokenDAO tokenDAO;

    @Mock
    private User user;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordService sut;

    @Before
    public void setUp() throws Exception {
        when(request.getScheme()).thenReturn("scheme");
        when(request.getServerName()).thenReturn("serverName");
        when(request.getServerPort()).thenReturn(1);
        when(token.getToken()).thenReturn("token");
    }

    @Test
    public void createResetUrl_simpleInputDate_ok() {
        String resetUrlExpected = sut.createResetUrl(request, token);
        String resetUrlActual = "scheme://serverName:1/cucumber/passwordReset?token=token";

        Assert.assertEquals(resetUrlExpected, resetUrlActual);
    }

    @Test
    public void createConfirmUrl_simpleInputDate_ok() {
        String resetUrlExpected = sut.createConfirmUrl(request, token);
        String resetUrlActual = "scheme://serverName:1/cucumber/confirmAccount?token=token";

        Assert.assertEquals(resetUrlExpected, resetUrlActual);
    }

    @Test
    public void passwordReset_simplePasswordResetDTO_ok() {
        when(passwordResetDTO.getToken()).thenReturn("token");
        when(tokenDAO.findByToken(anyString())).thenReturn(token);
        when(token.getUser()).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        sut.passwordReset(passwordResetDTO);
        verify(tokenDAO).delete(token);
    }

    @Test(expected = TokenMissingException.class)
    public void passwordReset_incorrectToken_expectException() {
        when(passwordResetDTO.getToken()).thenReturn("token");
        when(tokenDAO.findByToken(anyString())).thenReturn(null);

        sut.passwordReset(passwordResetDTO);
        verify(tokenDAO).delete(token);
    }

    @Test
    public void getTokenByName_simpleToken_Ok() {
        sut.getTokenByName(anyString());
        verify(tokenDAO).findByToken(anyString());
    }
}