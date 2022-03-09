package com.aligntogether.socialmedia.service;

import com.aligntogether.socialmedia.model.login.Role;
import com.aligntogether.socialmedia.model.login.User;
import com.aligntogether.socialmedia.repository.loginrepo.RoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.aligntogether.socialmedia.model.login.enums.ERole.*;

@Service
public class RoleService {
  @Autowired
    RoleRepository roleRepository;
  Role role = new Role();

    public void setRole() {
        user();
        moderator();
        admin();
    }

    public void user(){
      role.setId(1);
      role.setName(ROLE_USER);
      roleRepository.save(role);
  }
    public void moderator(){
        role.setId(2);
        role.setName(ROLE_MODERATOR);
        roleRepository.save(role);
    }
    public void admin(){
        role.setId(3);
        role.setName(ROLE_ADMIN);
        roleRepository.save(role);
    }

    public String Authorization(Authentication authentication) throws JsonProcessingException {
        authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        //new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(authentication.getPrincipal()), User.class);
        User user = objectMapper.readValue(new ObjectMapper().writeValueAsString(authentication.getPrincipal()), User.class);
        return "id "+user.getId()+"\n name "+user.getUsername();
    }
}
