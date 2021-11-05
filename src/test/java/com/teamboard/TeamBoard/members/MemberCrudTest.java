package com.teamboard.TeamBoard.members;

import com.teamboard.TeamBoard.repository.user.UserRepository;
import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트에서 DB에 입력한 값을 테스트 결과를 받은 후 다시 롤백한다 -> 즉, 실제 DB에는 넣지만 테스트를 마치고 다시 빼낸다 -> DB관련 테스트에 있어서 매번 삽입할 값을 바꾸지 않아도 됨
public class MemberCrudTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    //@Commit // 트렌젝셔널의 롤백을 하지 않고 커밋하여 실제 반영할 수 있게하는 어노테이션
    void 맴버가입() {
        // Given : 상황이 주어지고
        User user = new User();
        user.setId("Test_Account");
        user.setPw("pw");
        user.setName("Test_User");
        user.setNick("Tester");

        // When : 실행하면
        String addUser = userService.join(user);

        // Then : 이런 결과가 나와야함
        User user1 = userService.findOneUser("Test_Account").get();
        Assertions.assertThat(user1.getName()).isEqualTo(user.getName());
        System.out.println("User1 RegDate : "+user1.getDate());
    }

//    @Test
//    void 중복테스트() {
//        //Transactional로 인해서 커밋이 안 되고 종료
//        member overMem_1 = new member();
//        overMem_1.setName("overMem");
//
//        member overMem_2 = new member();
//        overMem_2.setName("overMem");
//
//        memberService.join(overMem_1);
//        // Junit에서 예외처리를 확인 할 수 있는 함수를 제공
//        // overMem_2를 Join한 값이 IllegalException클래스에 속하는가?
//        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(overMem_2));

//        직접 값을 비교할 수도 있다
//        try{
//            memberService.join(overMem_2);
//        }catch(IllegalStateException e){
//            // 예외처리로 넘어간 후 받은 에러 메시지가 지정한 에러메시지와 동일하면, 정상작동 : 메시지 대신 에러코드를 사용할 수 있음
//            Assertions.assertThat(e.getMessage()).isEqualTo("Already Exist Account !");
//        }
//    }

}
