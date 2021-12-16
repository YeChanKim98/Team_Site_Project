package com.teamboard.TeamBoard.repository.user;

import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    // 불필요한 메서드 정리
    User join(User user);
    int delete(String id);
    int update(User user);
    int updateNick(String id, String nick);
    int updatePw(String id, String nick);
    int updateMail(String id, String nick);
    Optional<User> findById(String id);
    String findId(String name, String address);
    String findPw(String id, String address);
    List<User> findAll();
}
