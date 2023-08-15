package com.example.wantedassignment.domain.user.service;

import com.example.wantedassignment.domain.user.dto.request.JoinUser;
import com.example.wantedassignment.domain.user.dto.response.user.JoinResultDto;
import com.example.wantedassignment.domain.user.entity.User;
import com.example.wantedassignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final Environment env;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JoinResultDto join(JoinUser joinUserDto) {
        User user = JoinUser.toEntity(joinUserDto, passwordEncoder);
        User savedUser = userRepository.save(user);
        return JoinResultDto.of(true, savedUser.getId());
    }


}
