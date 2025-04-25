package ru.andreev.auction.mapper;

import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.entity.User;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User>{

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }
    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setUsername(object.getUsername());
        user.setPassword(object.getPassword());
        user.setEmail(object.getEmail());
        user.setRole(object.getRole());
    }
}
