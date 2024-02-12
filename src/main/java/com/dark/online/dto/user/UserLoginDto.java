package com.dark.online.dto.user;

import com.dark.online.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private String password;
    private boolean accountVerified;
    private String nickname;
    private Collection<Role> roles;
}
