package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.mail.Chk_MailService;
import com.teamboard.TeamBoard.user.form.JoinForm;
import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.UserService;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    // 성공 후 반환에 대한 전반적인 재작성 필요
    private final UserService userService;
    private final Chk_MailService chk_MailService;

    @Autowired
    public UserController(UserService userService, Chk_MailService chk_mailService) {
        this.userService = userService;
        chk_MailService = chk_mailService;
    }

    // Join : ID / PW / 이름 / 이메일(인증필요) / 닉네임
    @GetMapping("users/Join")
    public String joinForm(){
        return "users/Regist";
    }

    @PostMapping("users/Join")
    public String join(JoinForm joinForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (chk_MailService.checkAuth(joinForm.getEmail()) == 1) { // 이메일 인증 상태가 1이면
            System.out.println("이메일 인증을 확인하여 가입절차를 실시합니다");
            User user = new User();
            user.setId(joinForm.getId());
            user.setPw(joinForm.getPw());
            user.setName(joinForm.getName());
            user.setNick(joinForm.getNick());
            user.setEmail(joinForm.getEmail());
            String res = userService.join(user);
            if (res.equals("Join Fail")) {
                return "users/Join"; // alert -> 다시 가입 페이지
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("loginID", user.getId());
                return "redirect:/";
            }
        }else{ // 이메일이 인증상태가 아니면
            out.println("<script>alert('이메일 인증을 완료해주세요');history.go(-1);</script>");
            out.flush();
            out.close();
            return "";
        }
    }
    

    // 로그인
    @PostMapping("/users/Login")
    public String login(@RequestParam("id")String id, @RequestParam("pw")String pw,Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        Optional<User> resUser = userService.findOneUser(id);
        if(resUser.isPresent()){
            String findpw = resUser.get().getPw();
            if(pw.equals(findpw)){
                HttpSession session = request.getSession();
                session.setAttribute("loginID", id);
                return "redirect:/";
            }else{
                out.println("<script>alert('아이디혹은 비밀번호를 다시 확인해 주세요');history.go(-1);</script>");
                out.flush();
                out.close();
                return "redirect:/";
            }
        }else{
            out.println("<script>alert('존재하지 않는 계정입니다');history.go(-1);</script>");
            out.flush();
            out.close();
            return "redirect:/";
        }
    }

    // 로그아웃
    @GetMapping("/users/Logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("loginID");
        return "redirect:/";
    }



    // Select All User : 검색 후 바로 출력
    @PostMapping("/users/SelAll")
    @RequestMapping("/users/SelAll")
    public String findAllUser(Model userList){ // 인자가 들어가는 이유
        List<User> users = userService.findAllUser();
        userList.addAttribute("users",users);
        return "users/tmp/SelAll";
    }


    // 아이디 찾기(이메일 이용)
    @GetMapping("/users/SelOne")
    public String findForm(){
        return "users/tmp/form/SelOneForm";
    }


    @PostMapping("/users/SelOne") // 리퀘스트 맵핑?
    public String findOneUser(@RequestParam("address") String address, Model model){ // 리퀘스트 파라미터 어노테이션으로 인해 폼html에서 타임리프 사용
        System.out.println("컨트롤러에서 이메일을 통한 계정 조회 : "+address);
        Optional<User> user = userService.findByMail(address);
        model.addAttribute("input",address); // 확인용 속성추가 : 일단 유지
        model.addAttribute("user",user.get()); // 옵셔널로 감싸져 있으므로 get메소드를 통해서 꺼내줘야함
        return "/users/tmp/SelOne";
    }


    // Delete User : 로그인 상태에서 동작을 가정. 현재 접속중인 세션의 ID를 받아서 삭제
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
    @GetMapping("/users/Update")
    public String updateForm( Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = userService.findOneUser((String) session.getAttribute("loginID")).get(); // 업데이트 테스트용 임시 케이스 -> 원래는 현재 유저 ID를 받아와서 입력
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


















    //test
    @GetMapping("/test")
    public String testhtml(){
        return "test/TestLogin_2";
    }
}
