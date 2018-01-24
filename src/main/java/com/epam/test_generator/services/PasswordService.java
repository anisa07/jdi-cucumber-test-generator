package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.PasswordResetTokenDAO;
import com.epam.test_generator.dto.PasswordResetDTO;
import com.epam.test_generator.entities.PasswordResetToken;
import com.epam.test_generator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class PasswordService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    public String createResetUrl(HttpServletRequest request, PasswordResetToken token) {
        String url =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String resetUrl = url + "/cucumber/passwordReset?token=" + token.getToken();
        return resetUrl;
    }
    public String createConfirmUrl(HttpServletRequest request, PasswordResetToken token) {
        String url =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String resetUrl = url + "/cucumber/confirmAccount?token=" + token.getToken();
        return resetUrl;
    }


    public void passwordReset(PasswordResetDTO passwordResetDTO) {
        String token = passwordResetDTO.getToken();
        PasswordResetToken resetToken = passwordResetTokenDAO.findByToken(token);
        User user = resetToken.getUser();
        String updatedPassword = passwordEncoder.encode(passwordResetDTO.getPassword());
        userService.updatePassword(updatedPassword, user.getEmail());
        passwordResetTokenDAO.delete(resetToken);
    }

    public PasswordResetToken getTokenByName(String token){
        return passwordResetTokenDAO.findByToken(token);
    }
}
