package ru.andreev.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class LotCreateEditDto {
    @NotBlank(message = "Title should be filled in")
    String title;

    String description;

    @Positive
    BigDecimal startPrice;

    @Positive
    BigDecimal bidStep;

    @Positive
    BigDecimal blitzPrice;

    LocalDateTime startedAt;

    LocalDateTime expiredAt;

    //Владелец определяется по авторизации
}
