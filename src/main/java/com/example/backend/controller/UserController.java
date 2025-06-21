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
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try{
            if(userDTO == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value");
            }
            UserEntity user = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword())) //패스워드가 암호화되어 저장되어야 한다
                    //userController의 엔드 포인트로 들어오는 경우 무조건 일반 유저 권한을 부여한다
                    .role(userDTO.getRole() != null ? userDTO.getRole() : "ROLE_USER")
                    .build();

            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .role(registeredUser.getRole())
                    .build();

            return  ResponseEntity.ok().body(responseUserDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
        UserEntity user = userService.getByCredentials(
                userDTO.getUsername(),
                userDTO.getPassword(),
                passwordEncoder
        );

        if(user != null){
            //토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .role(user.getRole())
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
