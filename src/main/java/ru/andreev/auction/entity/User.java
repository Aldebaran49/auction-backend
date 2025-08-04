package ru.andreev.auction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"ownedLots", "bids"})
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
    @OneToMany(mappedBy = "owner")
    List<Bid> bids = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    List<Lot> ownedLots = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "winner")
    List<Lot> wonLots = new ArrayList<>();
}
