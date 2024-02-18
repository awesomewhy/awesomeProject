package com.ozon.online.service.impl.role;

import com.ozon.online.entity.Role;
import com.ozon.online.repository.RoleRepository;
import com.ozon.online.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final static String ROLE_USER = "ROLE_USER";
    private final static String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public Optional<Role> getUserRole() {
        return roleRepository.findByName(ROLE_USER);
    }

    @Override
    public Optional<Role> getAdminRole() {
        return roleRepository.findByName(ROLE_ADMIN);
    }
}
