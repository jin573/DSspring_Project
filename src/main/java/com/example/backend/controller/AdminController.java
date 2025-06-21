package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.UserEntity;
import com.example.backend.security.TokenProvider;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("auth/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDTO adminDTO){
        try{
            if(adminDTO == null || adminDTO.getPassword() == null){
                throw new RuntimeException("Invalid Password value");
            }

            UserEntity adminUser = UserEntity.builder()
                    .username(adminDTO.getUsername())
                    .password(passwordEncoder.encode(adminDTO.getPassword()))
                    .role("ROLE_ADMIN")
                    .build();
            UserEntity registeredUser = userService.create(adminUser);
            UserDTO responseAdminDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .role(registeredUser.getRole())
                    .build();

            return  ResponseEntity.ok().body(responseAdminDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }



    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO adminDTO){
        UserEntity adminUser = userService.getByCredentials(
                adminDTO.getUsername(),
                adminDTO.getPassword(),
                passwordEncoder
        );

        if(adminUser != null && adminUser.getRole().equals("ROLE_ADMIN")){
            //토큰 생성
            final String token = tokenProvider.create(adminUser);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(adminUser.getUsername())
                    .id(adminUser.getId())
                    .role(adminUser.getRole())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }else{
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
