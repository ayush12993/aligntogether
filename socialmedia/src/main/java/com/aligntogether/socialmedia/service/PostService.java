package com.aligntogether.socialmedia.service;

import com.aligntogether.socialmedia.model.login.User;
import com.aligntogether.socialmedia.repository.loginrepo.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.xmlunit.util.Mapper;

import javax.validation.constraints.NotBlank;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PostService {
    @Autowired
    UserRepository userRepository;


    public  String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    public  void post(Authentication authentication,User user) throws JsonProcessingException {
        long id =Authorization(authentication);
        if (userRepository.findById(id).isPresent()){
        {
            user = userRepository.findById(id).get();
            user.setCreatedDate(getCurrentTimeUsingDate());
            userRepository.save(user);
        }}
    }

    public Long Authorization(Authentication authentication) throws JsonProcessingException {
        authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        //new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(authentication.getPrincipal()), User.class);
        User user = objectMapper.readValue(new ObjectMapper().writeValueAsString(authentication.getPrincipal()), User.class);
        return user.getId();
    }

}
