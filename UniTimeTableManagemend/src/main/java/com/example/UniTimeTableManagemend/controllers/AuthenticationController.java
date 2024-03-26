package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.dto.AuthenticationRequest;
import com.example.UniTimeTableManagemend.dto.AuthenticationResponse;
import com.example.UniTimeTableManagemend.dto.RegisterRequest;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        }catch (UserException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
