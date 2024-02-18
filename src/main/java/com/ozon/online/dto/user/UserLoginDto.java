package com.ozon.online.dto.user;

import com.ozon.online.entity.Role;
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
