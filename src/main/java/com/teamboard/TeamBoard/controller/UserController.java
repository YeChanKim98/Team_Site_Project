package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.user.form.SelOneForm;
import com.teamboard.TeamBoard.user.form.JoinForm;
import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.UserService;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    // 성공 후 반환에 대한 전반적인 재작성 필요
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

    @PostMapping("/users/login")
    public String login(@RequestParam("id")String id, @RequestParam("pw")String pw,Model model){
        String findpw = userService.findOneUser(id).get().getPw();
        if(pw.equals(findpw)){
            // 세션 생성
            return "redirect:/";
        }else{
            return null; // alert
        }
    }



    // Select All User : 검색 후 바로 출력
    @PostMapping("/users/SelAll")
    @RequestMapping("/users/SelAll")
    public String findAllUser(Model userList){ // 인자가 들어가는 이유
        List<User> users = userService.findAllUser();
        userList.addAttribute("users",users);
        return "users/tmp/SelAll";
    }


    // Select One User : ID 받은 후 결과 리턴
    @GetMapping("/users/SelOne")
    public String findForm(){
        return "users/tmp/form/SelOneForm";
    }


    @PostMapping("/users/SelOne") // 리퀘스트 맵핑?
    public String findOneUser(@RequestParam("id") String id, Model model){ // 리퀘스트 파라미터 어노테이션으로 인해 폼html에서 타임리프 사용
        Optional<User> user = userService.findOneUser(id);
        model.addAttribute("input",id); // 확인용 속성추가 : 일단 유지
        model.addAttribute("user",user.get()); // 옵셔널로 감싸져 있으므로 get메소드를 통해서 꺼내줘야함
        return "/users/tmp/SelOne";
    }


    // Delete User : 로그인 상태에서 동작을 가정. 현재 접속중인 세션의 ID를 받아서s 삭제
    // GetMapping은 임시용. 실제 유저는 버튼클릭 후 인증을 통해서 바로 삭제
    @GetMapping("/users/Delete")
    public String deleteForm(){
        return "/users/tmp/form/DeleteForm";
    }

    @PostMapping("/users/Delete") // 리퀘스트 맵핑?
    public String delete(@RequestParam("id") String id){
        int res = userService.deleteUser(id);
        if (res==1) return "redirect:/users/SelAll";
        else return "Fail";
    }

    // Update User Data : 로그인이 있는 유저를 기준으로 Update실시
    // 중복데이터 업데이트를 활성화하여 join으로 대체할 수 있는지 확인
    @GetMapping("/users/Update/{updateId}")
    public String updateForm(@PathVariable String updateId, Model model){
        User user = userService.findOneUser(updateId).get(); // 업데이트 테스트용 임시 케이스 -> 원래는 현재 유저 ID를 받아와서 입력
        model.addAttribute("id",user.getId());
        model.addAttribute("name",user.getName());
        model.addAttribute("nick",user.getNick());
        return "users/tmp/form/Update";
    }

    @PostMapping("/users/Update/{updateId}")
    public String update(UpdateForm updateForm){
        int res  = userService.update(updateForm); // 1성공 0실패
        if(res==1) System.out.println("계정정보 업데이트 성공!");
        else System.out.println("계정정보 업데이트 실패!");
        return "redirect:/users/SelAll"; // 새로고침. 이때 변경 정보가 반영된 것이 확인 되야함
    }
}
