package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.dto.JiraSettingsDTO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JiraSettingsService {

    @Autowired
    JiraSettingsDAO jiraSettingsDAO;

    @Autowired
    private PasswordEncoder encoder;

    public JiraSettings createJiraSettings(JiraSettingsDTO jiraSettingsDTO) {
        if (jiraSettingsDAO.findByLogin(jiraSettingsDTO.getLogin()) != null) {
            throw new UnauthorizedException(
                "Jira setting with such login:" + jiraSettingsDTO.getLogin() + " already exist!");
        } else {

            final JiraSettings jiraSettings = new JiraSettings(
                jiraSettingsDTO.getUri(),
                jiraSettingsDTO.getLogin(),
                encoder.encode(jiraSettingsDTO.getPassword()));
            jiraSettingsDAO.save(jiraSettings);
            return jiraSettings;
        }
    }

    public void updateJiraSettings(Long id, JiraSettingsDTO jiraSettingsDTO) {
        JiraSettings jiraSettings = jiraSettingsDAO.findById(id);
        checkNotNull(jiraSettings);
        jiraSettings.setLogin(jiraSettingsDTO.getLogin());
        jiraSettings.setPassword( encoder.encode(jiraSettingsDTO.getPassword()));
        jiraSettings.setUri(jiraSettingsDTO.getUri());
        jiraSettingsDAO.save(jiraSettings);
    }

    public List<JiraSettings> getJiraSettings() {
        return jiraSettingsDAO.findAll();
    }

}
