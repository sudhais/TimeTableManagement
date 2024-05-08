package com.example.UniTimeTableManagemend.services.Impl;

import com.example.UniTimeTableManagemend.dto.*;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.respositories.UserRepository;
import com.example.UniTimeTableManagemend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) throws UserException {

        //get the user by given email
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());

        //check user exists or not
        if(userOptional.isPresent()){
            System.out.println("Already user email " + request.getEmail() + " exist" );
            throw new UserException(UserException.AlreadyExists(request.getEmail()));
        }else{

            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();

            //insert data into the db
            userRepository.insert(user);
            System.out.println("inserted " + user);

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

//            user.setPassword(null);         //setting password null for displaying bcs of security

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
//                    .user(user)
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        var user = userRepository.findUserByEmail(userEmail).orElseThrow();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt = jwtService.generateToken(user);

            AuthenticationResponse refreshTokenResponse = new AuthenticationResponse();

            refreshTokenResponse.setToken(jwt);
            refreshTokenResponse.setRefreshToken(refreshTokenRequest.getToken());

            return refreshTokenResponse;
        }

        return null;

    }
}
