package com.aligntogether.socialmedia.controller;

import com.aligntogether.socialmedia.model.login.Posts;
import com.aligntogether.socialmedia.model.login.User;
import com.aligntogether.socialmedia.repository.loginrepo.UserRepository;
import com.aligntogether.socialmedia.service.PostService;
import com.aligntogether.socialmedia.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth/panel")
public class AuthorizationController {

    @Autowired
    PostService postService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRepository userRepository;

    @Operation(
            summary = "Yo! Start creating Bot Check to see how the Api works! \nThis one only for testing Purposes!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful"),
                    @ApiResponse(responseCode = "404", description = "Unsuccessful"),
                    @ApiResponse(responseCode = "400", description = "UnSuccessful")
            }
    )
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess(Authentication authentication) throws JsonProcessingException {
        return "User Content."+roleService.Authorization(authentication);
    }
    @PostMapping("/user/post")
    @PreAuthorize("hasRole('USER')")
    public String userPost(Authentication authentication, User user, @Valid @RequestBody Posts posts) throws JsonProcessingException {
        long id =postService.Authorization(authentication);
        if (userRepository.findById(id).isPresent()){
            {
                user = userRepository.findById(id).get();

                user.setCreatedDate(postService.getCurrentTimeUsingDate());
          user.setId(posts.getId());
          user.setPosts(posts.getPost());
                userRepository.save(user);
            }}


        return "User Content."+roleService.Authorization(authentication);
    }
    @GetMapping("/user/post")
    @PreAuthorize("hasRole('USER')")
    public String userPost(Authentication authentication,User user) throws JsonProcessingException {
        long id =postService.Authorization(authentication);
        if (userRepository.findById(id).isPresent()){
            {

               return userRepository.findByPosts(id);
            }}


        return "User Content."+roleService.Authorization(authentication);
    }



}
