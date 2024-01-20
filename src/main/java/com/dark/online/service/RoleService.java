package com.dark.online.service;

import com.dark.online.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getUserRole();

    Optional<Role> getAdminRole();
}
