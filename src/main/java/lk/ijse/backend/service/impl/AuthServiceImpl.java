package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.AuthRequestDTO;
import lk.ijse.backend.dto.AuthResponseDTO;
import lk.ijse.backend.dto.RegisterRequestDTO;
import lk.ijse.backend.entity.Role;
import lk.ijse.backend.entity.User;
import lk.ijse.backend.exception.BadRequestException;
import lk.ijse.backend.exception.DuplicateResourceException;
import lk.ijse.backend.exception.UnauthorizedException;
import lk.ijse.backend.repository.RoleRepository;
import lk.ijse.backend.repository.UserRepository;
import lk.ijse.backend.service.AuthService;
import lk.ijse.backend.util.JwtUtil;
import lk.ijse.backend.util.RoleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(AuthRequestDTO authRequest) {
        String email = authRequest.getEmail() != null ? authRequest.getEmail().trim().toLowerCase() : "";
        logger.debug("Processing login attempt for email: '{}'", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    logger.warn("Login authentication failed: Email '{}' not found in database.", email);
                    return new UnauthorizedException("Invalid email or password");
                });

        logger.debug("User retrieved: ID={}, Email='{}', Roles={}", user.getId(), user.getEmail(),
                user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.joining(",")));

        boolean isMatch = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        logger.debug("Password encoder verification for '{}': {}", email, isMatch ? "MATCH SUCCESS" : "MATCH FAILED");

        if (!isMatch) {
            logger.warn("Login authentication failed: Password mismatch for email '{}'", email);
            throw new UnauthorizedException("Invalid email or password");
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );

        String token = jwtUtil.generateToken(userDetails, user.getId(), user.getName());
        List<String> roleNames = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());

        String primaryRole = roleNames.stream()
                .filter(r -> r.contains("ADMIN"))
                .findFirst()
                .orElse(!roleNames.isEmpty() ? roleNames.get(0) : "ROLE_USER");

        logger.info("Login successful for user '{}' (ID: {}, Role: {})", user.getEmail(), user.getId(), primaryRole);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(primaryRole)
                .roles(roleNames)
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        if (registerRequest.getConfirmPassword() == null || !registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm Password do not match");
        }

        String email = registerRequest.getEmail().trim().toLowerCase();
        logger.info("Processing registration attempt for email: '{}'", email);

        if (userRepository.existsByEmail(email)) {
            logger.warn("Registration failed: Email '{}' already exists.", email);
            throw new DuplicateResourceException("An account with email " + email + " already exists");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_USER)));

        User user = User.builder()
                .name(registerRequest.getName().trim())
                .email(email)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone() != null ? registerRequest.getPhone().trim() : null)
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .build();

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: ID={}, Email='{}'", savedUser.getId(), savedUser.getEmail());

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(userRole.getName().name())
        );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(),
                savedUser.getPassword(),
                authorities
        );

        String token = jwtUtil.generateToken(userDetails, savedUser.getId(), savedUser.getName());

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .roles(Collections.singletonList(userRole.getName().name()))
                .build();
    }
}
