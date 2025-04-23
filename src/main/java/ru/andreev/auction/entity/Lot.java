package ru.andreev.auction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lots")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String title;

    String description;

    BigDecimal price;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "expire_at")
    LocalDateTime expiredAt;

    @Builder.Default
    @OneToMany(mappedBy = "lot")
    List<Bid> bids = new ArrayList<>();
}
