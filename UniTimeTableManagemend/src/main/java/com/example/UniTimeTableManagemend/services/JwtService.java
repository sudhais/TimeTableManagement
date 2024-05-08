package com.example.UniTimeTableManagemend.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String extractUsername(String token);
    public String generateToken(UserDetails userDetails);
    public String generateRefreshToken(UserDetails userDetails);
    public boolean isTokenValid(String token, UserDetails userDetails);
}
