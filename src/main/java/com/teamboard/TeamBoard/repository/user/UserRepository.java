package com.teamboard.TeamBoard.repository.user;

import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User join(User user);
    void delete(String id); // User객체를 넘길 필요가 있는지 확인
    int update(UpdateForm user);
    Optional<User> findById(String id);
    Optional<User> findByName(String name);
    List<User> findAll();

}
