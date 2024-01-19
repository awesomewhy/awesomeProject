package com.dark.online.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "roles")
@Data
public class Role {
    @Id
    private Long id;
    private ERole role;

    public enum ERole {
        USER,
        ADMIN
    }

}
