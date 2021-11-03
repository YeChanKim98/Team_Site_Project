package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.user.form.FindForm;
import com.teamboard.TeamBoard.user.form.JoinForm;
import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.UserService;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Join : ID / PW / 이름 / 이메일(인증필요) / 닉네임
    @GetMapping("users/Join")
    public String joinForm(){
        return "users/tmp/form/JoinForm";
    }

    @PostMapping("users/Join")
    public String join(JoinForm joinForm){
        User user = new User();
        user.setId(joinForm.getId());
        user.setPw(joinForm.getPw());
        user.setName(joinForm.getName());
        user.setNick(joinForm.getNick());
        userService.join(user);
        return "redirect:/";
    }


    // Select All User : 검색 후 바로 출력
    @GetMapping("/users/SelAll")
    public String findAllUser(Model userList){ // 인자가 들어가는 이유
        List<User> users = userService.findAllUser();
        userList.addAttribute("users",users);
        return "users/tmp/SelAll";
    }


    // Select One User : ID / Email 받은 후 결과 리턴
    @GetMapping("/users/SelOne")
    public String findForm(){
        return "users/tmp/SelOneForm";
    }


    @PostMapping("/users/SelOne") // 리퀘스트 맵핑?
    public String findOneUser(@RequestParam("findForm") FindForm findForm, Model model){ // FindForm 제작
        Optional<User> user = userService.findOneUser(findForm.getId());
        model.addAttribute("user",user);
        return "users/tmp/SelOne";
    }


    // Delete User : 로그인 상태에서 동작을 가정. 현재 접속중인 세션의 ID를 받아서 삭제
    @GetMapping("/users/Delete")
    public String delete(){
        return "redirect:/";
    }

    // Update User Data : 로그인이 있는 유저를 기준으로 Update실시
    // 중복데이터 업데이트를 활성화하여 join으로 대체할 수 있는지 확인
    @GetMapping("/users/Update")
    public String list(Model model){
        // 값을 받아와서 모델에 넣어서 전달
        return "users/tmp/Update";
    }

    @PostMapping("/users/Update")
    public String findOneUser(@RequestParam("updateForm") UpdateForm updateForm){ // UpdateForm 제작
        
        return "users/tmp/Update"; // 새로고침. 이때 변경 정보가 반영된 것이 확인 되야함
    }
}
