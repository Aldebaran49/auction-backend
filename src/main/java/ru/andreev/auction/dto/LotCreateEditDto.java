package ru.andreev.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class LotCreateEditDto {
    @NotBlank(message = "Title should be filled in")
    String title;

    String description;

    BigDecimal price;

    LocalDateTime createdAt;

    LocalDateTime expiredAt;
}
