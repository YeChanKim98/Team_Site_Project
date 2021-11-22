package com.teamboard.TeamBoard.repository.user;

import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User join(User user); // 리턴 값 void로 해도 됨
    int delete(String id);
    int update(UpdateForm user);
    Optional<User> findById(String id);
    Optional<User> findByMail(String address);
    List<User> findAll();
}
