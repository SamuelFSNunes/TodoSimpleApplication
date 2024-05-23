package com.samuelnunes.todosimple.controllers;

import com.samuelnunes.todosimple.models.AuthenticationDTO;
import com.samuelnunes.todosimple.models.LoginResponseDTO;
import com.samuelnunes.todosimple.models.RegisterDTO;
import com.samuelnunes.todosimple.models.User;
import com.samuelnunes.todosimple.repositories.UserRepository;
import com.samuelnunes.todosimple.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.username(),encryptedPassword, data.role());
        this.repository.save(newUser);
        return ResponseEntity.ok(
                String.format(
                        "User was successfully registered!%nUsername: %s%nRole: %s",
                        newUser.getUsername(),
                        newUser.getRole()
                )
        );
    }

}
