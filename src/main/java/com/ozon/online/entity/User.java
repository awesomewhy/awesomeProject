package com.ozon.online.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    private String password;
    @Column(unique = true)
    @NotNull
    private String nickname;
    private BigDecimal balance;
    private String secretKey;
    private String username;
    private String surname;
    private LocalDateTime createdAt;
    private boolean accountVerified;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private UserAvatar avatarId;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token")
    private RefreshToken refreshToken;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    // BAD (will clean)



    @OneToMany(mappedBy = "sellerId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToMany(mappedBy = "sellerId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @ManyToMany(mappedBy = "participants", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chats;

//    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Chat> chats;

//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return accountVerified == user.accountVerified && Objects.equals(id, user.id) && Objects.equals(password, user.password) && Objects.equals(nickname, user.nickname) && Objects.equals(balance, user.balance) && Objects.equals(secretKey, user.secretKey) && Objects.equals(username, user.username) && Objects.equals(surname, user.surname) && Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, nickname, balance, secretKey, username, surname, createdAt, accountVerified);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", balance=" + balance +
                ", secretKey='" + secretKey + '\'' +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", createdAt=" + createdAt +
                ", accountVerified=" + accountVerified +
                '}';
    }
}