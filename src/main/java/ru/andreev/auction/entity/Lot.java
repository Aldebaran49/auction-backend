package ru.andreev.auction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lots")
@EqualsAndHashCode(of = "id") // <<== ВАЖНО: equals и hashCode только по ID
@ToString(exclude = {"owner", "bids"})
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String title;

    String description;

    @Column(name = "start_price", nullable = false)
    BigDecimal startPrice;

    @Column(name = "current_price", nullable = false)
    BigDecimal currentPrice;

    @Column(name = "bid_step", nullable = false)
    BigDecimal bidStep;

    @Column(name = "blitz_price", nullable = true)
    BigDecimal blitzPrice;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "started_at")
    LocalDateTime startedAt;

    @Column(name = "expire_at")
    LocalDateTime expiredAt;

    @Builder.Default
    @OneToMany(mappedBy = "lot")
    List<Bid> bids = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    LotStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    User winner;
}
