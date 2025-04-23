package ru.andreev.auction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String username;

    String password;

    String email;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    List<Bid> bids = new ArrayList<>();
}
