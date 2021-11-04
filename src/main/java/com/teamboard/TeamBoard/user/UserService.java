package com.teamboard.TeamBoard.user;

import com.teamboard.TeamBoard.repository.user.UserRepository;
import com.teamboard.TeamBoard.user.form.UpdateForm;
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

    // ID중복확인
    private void ChkOver(User user) {
        userRepository.findById(user.getId()) // ifPresent는 값이 있을경우 원하는 동작을 하도록 지정한다
                .ifPresent(m -> {
                    // 중복으로 확인시 동작 : 현재는 익셉션
                    throw new IllegalStateException("Already Exist Account !");
                });
    }

    // 모든 유저 출력
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    // id를 통한 유저 삭제
    public void deleteUser(String id){
        userRepository.delete(id);
    }

    // ID를 통한 유저 확인
    public Optional<User> findOneUser(String id) {
        return userRepository.findById(id); // 결과로 받은 객체에서 id값 출력..?
    }

    public int update(UpdateForm user){
        return userRepository.update(user);
    }


}

