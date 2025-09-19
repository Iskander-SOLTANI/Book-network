package com.iskander.book_network.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name ="Authentication")
public class AuthenticationController {
    private final AutehnticationService autehnticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        autehnticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(autehnticationService.authenticate(request)) ;
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam("token") String token) throws MessagingException {
         autehnticationService.activateAccount(token);
    }
}
