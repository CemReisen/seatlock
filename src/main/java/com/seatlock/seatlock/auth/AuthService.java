package com.seatlock.seatlock.auth;

import com.seatlock.seatlock.common.EmailAlreadyExistsException;
import com.seatlock.seatlock.user.User;
import com.seatlock.seatlock.user.UserRepository;
import java.util.Locale;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seatlock.seatlock.common.InvalidCredentialsException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public User register(String rawEmail, String rawPassword) {
        String email = rawEmail.trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User(email, passwordEncoder.encode(rawPassword));
        try {
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    public String login(String rawEmail, String rawPassword) {
        String email = rawEmail.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user.getId(), user.getRole().name());
    }
}
