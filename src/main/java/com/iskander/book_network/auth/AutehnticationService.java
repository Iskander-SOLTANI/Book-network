package com.iskander.book_network.auth;

import com.iskander.book_network.email.EmailService;
import com.iskander.book_network.email.EmailTemplateName;
import com.iskander.book_network.role.RoleRepository;
import com.iskander.book_network.security.JwtService;
import com.iskander.book_network.user.Token;
import com.iskander.book_network.user.TokenRepository;
import com.iskander.book_network.user.User;
import com.iskander.book_network.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutehnticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${spring.application.mailing.frontend-url}")
    private String activationURL;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
        var user = User.builder()
                .userName(request.getUserName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationURL,
                newToken,
                "Account activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(20))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String chracters = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(chracters.length());
            activationCode.append(chracters.charAt(number));
        }
        return activationCode.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        // get the authentication user
        var user = ((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwt = jwtService.generateToken(claims,user);
        return AuthenticationResponse.builder().token(jwt).build();
    }


    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                //todo exception has be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getCreatedAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Token is expired, A new token has been sent to the same email address");
        }
        var user = userRepository.findById(Long.valueOf(savedToken.getUser().getId()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setUpdatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
