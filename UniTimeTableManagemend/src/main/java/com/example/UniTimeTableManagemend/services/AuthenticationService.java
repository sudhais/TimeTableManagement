package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.dto.*;
import com.example.UniTimeTableManagemend.exception.UserException;

public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request) throws UserException;
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
