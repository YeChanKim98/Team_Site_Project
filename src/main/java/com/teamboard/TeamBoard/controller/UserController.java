package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.mail.Chk_MailService;
import com.teamboard.TeamBoard.mail.EmailService;
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
    private final EmailService emailService;
    private final Chk_MailService chk_MailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService, Chk_MailService chk_mailService) {
        this.userService = userService;
        this.emailService = emailService;
        this.chk_MailService = chk_mailService;
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


    // 계정 찾기
    @GetMapping("/users/FindAccount")
    public String findForm(){
        return "users/FindAccountForm";
    }

    // ID 찾기
    @PostMapping("/users/findAccount/id")
    public String findId(@RequestParam String id_address, @RequestParam String id_name, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String findRes = userService.findId(id_name,id_address);
        String content = id_name+"님의 로그인 ID는 "+findRes+"입니다.";
        if (!findRes.isEmpty()) { // 계정존재시
            emailService.sendSimpleMessage(id_address, "계정 ID 입니다",content);
            return "redirect:/";
        }else{ // 계정없을시
            out.println("<script>alert('이름혹은 이메일을 다시 확인해 주세요');location,href='/';</script>");
            out.flush();
            out.close();
            return "/";
        }
    }

    // PW 찾기
    @PostMapping("/users/findAccount/pw")
    public String findPw(@RequestParam String pw_id, @RequestParam String pw_address, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String findRes = userService.findPw(pw_id,pw_address);
        String content = pw_id+"님의 로그인 PW는 "+findRes+"입니다.";
        if (!findRes.isEmpty()) {
            emailService.sendSimpleMessage(pw_address, "계정 PW 입니다",content);
            return "redirect:/";
        }else{
            out.println("<script>alert('아이디혹은 이메일을 다시 확인해 주세요');location,href='/';</script>");
            out.flush();
            out.close();
            return "/";
        }
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
    @GetMapping({"/users/Update","/users/Update/{updateId}"})
    public String updateForm(@PathVariable(required = false)String updateId, Model model, HttpServletRequest request){
        if(updateId.isEmpty()){
            HttpSession session = request.getSession();
            User user = userService.findOneUser((String) session.getAttribute("loginID")).get();
        }
        User user = userService.findOneUser(updateId).get();
        model.addAttribute("userInfo",user);
//        model.addAttribute("name",user.getName());
//        model.addAttribute("nick",user.getNick());
        return "users/tmp/form/Update";
    }

    //
    @PostMapping("/users/Update")//{updateId}")
    public String update(User user){
        int res  = userService.update(user); // 1성공 0실패
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
