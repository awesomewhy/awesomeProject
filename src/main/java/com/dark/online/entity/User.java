package com.dark.online.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(name = "productWithPhoto", attributeNodes = {
        @NamedAttributeNode("avatarId")
})
public class User {
    @Id
    private String id;
    @NotNull
    private String password;
    @Column(unique = true)
    @NotBlank
    @NotNull
    private String nickname;
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal balance;
    @JsonIgnore
    private String secretKey;
    private String username;
    private String surname;
    private LocalDateTime createdAt;
    private boolean accountVerified;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User_Avatar avatarId;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;


    @OneToMany(mappedBy = "sellerId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToMany(mappedBy = "participants")
    private List<Chat> chats;

//    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Chat> chats;

}