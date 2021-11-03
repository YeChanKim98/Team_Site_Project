package com.teamboard.TeamBoard.user;

import com.teamboard.TeamBoard.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // 가입
    public String join(User user) {
        ChkOver(user);
        userRepository.join(user);
        return user.getName();
    }

    // 중복확인
    private void ChkOver(User user) {
        userRepository.findByName(user.getName()) // ifPresent는 값이 있을경우 원하는 동작을 하도록 지정한다
                .ifPresent(m -> {
                    throw new IllegalStateException("Already Exist Account !");
                });
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> findOneUser(String id) {
        return userRepository.findById(id);
    }
}

