package com.teamboard.TeamBoard.users;

import com.teamboard.TeamBoard.repository.user.UserRepository;
import com.teamboard.TeamBoard.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserRegistTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @Commit
    void chkidover(){
        int res = userService.chkidover("Alex");
        System.out.println("중복 체크 테스트 : "+res);
    }
}
