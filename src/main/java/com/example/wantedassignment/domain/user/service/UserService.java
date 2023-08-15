package com.example.wantedassignment.domain.user.service;

import com.example.wantedassignment.domain.user.dto.request.JoinUser;
import com.example.wantedassignment.domain.user.dto.response.user.JoinResultDto;
import com.example.wantedassignment.domain.user.dto.response.user.UserDto;
import com.example.wantedassignment.domain.user.entity.User;
import com.example.wantedassignment.domain.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final Environment env;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JoinResultDto join(JoinUser joinUserDto) {
        User user = JoinUser.toEntity(joinUserDto, passwordEncoder);
        User savedUser = userRepository.save(user);
        return JoinResultDto.of(true, savedUser.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(userEntity);
    }

    public void matchPassword(final String email, final String password) {
        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        if(!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(email + "이 존재하지 않습니다."));
        return new UserDto(user);
    }

    public Long getUserIdByToken(String token) {
        token = token.replace("Bearer ", "");
        try {
            String userId = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(token).getBody().getSubject();
            return Long.valueOf(userId);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameById(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));
        return user.getName();
    }

    public static final class CustomUserDetails extends User implements UserDetails {
        CustomUserDetails(User user) {
            super(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getRoles());
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections
                    .unmodifiableList(AuthorityUtils.createAuthorityList(super.getRoles()));
        }
        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
