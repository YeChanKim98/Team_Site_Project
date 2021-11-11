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
        int chkRes = ChkOver_Id(user);
        if(chkRes==1) {
            userRepository.join(user);
            return user.getName();
        }
        else{
            return "Join Fail";
        }
    }

    // 모든 유저 출력
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    // id를 통한 유저 삭제
    public int deleteUser(String id){
        return userRepository.delete(id);
    }

    // ID를 통한 유저 확인
    public Optional<User> findOneUser(String id) {
        return userRepository.findById(id); // 결과로 받은 객체에서 id값 출력..?
    }

    // 업데이트
    public int update(UpdateForm user){
        return userRepository.update(user);
    }

    // 추후 통합사용 가능여부 확인
    // ID 중복확인
    private int ChkOver_Id(User user) {
        if(userRepository.findById(user.getId()).isPresent()){
            return -1;
        }
        return 1;
    }

    // 닉네임 중복확인
    private int ChkOver_Nick(User user) {
        if(userRepository.findById(user.getNick()).isPresent()){
            return -2;
        }
        return 1;
    }

    // Email 중복확인
    private int ChkOver_Email(User user) {
        if(userRepository.findById(user.getEmail()).isPresent()){
            return -3;
        }
        return 1;
    }

}

