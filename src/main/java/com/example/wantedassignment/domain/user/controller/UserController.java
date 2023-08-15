package com.example.wantedassignment.domain.user.controller;


import com.example.wantedassignment.domain.user.dto.request.JoinUser;
import com.example.wantedassignment.domain.user.dto.response.user.JoinResultDto;
import com.example.wantedassignment.domain.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<?> joinUser(@RequestBody @Valid JoinUser joinUserDto) {
        try {
            JoinResultDto result = userService.join(joinUserDto);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
