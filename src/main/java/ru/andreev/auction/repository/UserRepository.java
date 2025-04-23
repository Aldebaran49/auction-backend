package ru.andreev.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreev.auction.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
