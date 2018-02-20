package com.epam.test_generator.config;

import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.UserService;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An application event listener interested in {@link ContextRefreshedEvent} which is raised
 * when an ApplicationContext gets initialized or refreshed.
 */
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * Handle an application event: adds roles from roles.properties
     * file to the database and creates admin role if it doesn't exist
     *
     * @param event
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        final List<Role> rolesFromProperties = roleService.getRolesFromProperties();

        for (Role role : rolesFromProperties) {
            if (roleService.getRoleByName(role.getName()) == null) {
                roleService.addRole(role);
            }
        }
        List<UserDTO> admins = userService.createAdminIfDoesNotExist();
        Set<UserDTO> adminsSet = new HashSet<>(admins);


/** Example for getProjects from Jira
 */
        BasicCredentials creds = new BasicCredentials("Auto_EPM-LSTR_BDD-GE", "Eg2CC6EmGhaWHGQvYh6kVjfkV");
        net.rcarz.jiraclient.JiraClient client = new net.rcarz.jiraclient.JiraClient("https://jirapct.epam.com/jira", creds);
        try {
            client.getProjects().forEach(s -> {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setName(s.getName());
                projectDTO.setJiraKey(s.getKey());
                projectDTO.setUsers(adminsSet);
                projectDTO.setDescription(s.getDescription() == null ? "No description" : s.getDescription());
                projectService.createProjectwithoutPrincipal(projectDTO);
            });
        } catch (JiraException e) {
            e.printStackTrace();
        }


    }
}
