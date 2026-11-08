package com.spk.tk_profile_matching.dto;

import com.spk.tk_profile_matching.model.Users.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private String username;
    private Role role;
    private String message;
}