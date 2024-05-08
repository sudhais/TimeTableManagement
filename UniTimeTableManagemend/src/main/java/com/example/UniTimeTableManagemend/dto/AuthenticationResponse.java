package com.example.UniTimeTableManagemend.dto;

import com.example.UniTimeTableManagemend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;

    private String refreshToken;
//    private User user;
}
