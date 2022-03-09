package com.aligntogether.socialmedia.controller;

import com.aligntogether.socialmedia.model.login.Role;
import com.aligntogether.socialmedia.model.login.User;
import com.aligntogether.socialmedia.payload.request.LoginRequest;
import com.aligntogether.socialmedia.payload.request.SignupRequest;
import com.aligntogether.socialmedia.payload.response.JwtResponse;
import com.aligntogether.socialmedia.payload.response.MessageResponse;
import com.aligntogether.socialmedia.repository.loginrepo.RoleRepository;
import com.aligntogether.socialmedia.repository.loginrepo.UserRepository;
import com.aligntogether.socialmedia.security.jwt.JwtUtils;
import com.aligntogether.socialmedia.security.service.UserDetailsImpl;
import com.aligntogether.socialmedia.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aligntogether.socialmedia.model.login.enums.ERole.*;

@CrossOrigin(origins = "*",maxAge =3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    RoleService roleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        List<String> roles= userDetails.getAuthorities().stream()
                .map(item-> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new  JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignupRequest signupRequest){
        if (userRepository.existsByUsername(signupRequest.getUsername())){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<Role>();

        if (strRoles==null){
            Role userRole = roleRepository.findByName(ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role->{
                switch (role){
                      case "admin":
                        Role adminRole = roleRepository.findByName(ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                        case "mod":
                        Role modRole = roleRepository.findByName(ROLE_MODERATOR)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);
                        break;
                        case "user":
                        Role userRole = roleRepository.findByName(ROLE_USER)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/saveRoles")
    public ResponseEntity<?> saveRoles(){
        roleService.setRole();
        return new ResponseEntity("Role has been saved ", HttpStatus.OK);
    }
}
