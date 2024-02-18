package com.ozon.online.service;

import com.ozon.online.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getUserRole();

    Optional<Role> getAdminRole();
}
