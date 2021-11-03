package com.teamboard.TeamBoard.repository.user;

import com.teamboard.TeamBoard.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User join(User user);
    User delete(User user);
    User update(User user);
    Optional<User> findById(String id);
    Optional<User> findByName(String name);
    List<User> findAll();

}
