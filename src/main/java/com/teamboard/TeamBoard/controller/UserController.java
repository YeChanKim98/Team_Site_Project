package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.comment.CommentService;
import com.teamboard.TeamBoard.mail.Chk_MailService;
import com.teamboard.TeamBoard.mail.EmailService;
import com.teamboard.TeamBoard.user.form.JoinForm;
import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.UserService;
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
    private final BoardService boardService;
    private final CommentService commentService;
    private final EmailService emailService;
    private final Chk_MailService chk_MailService;
//    private String updateId;
//    private Model model;
//    private HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, BoardService boardService, CommentService commentService, EmailService emailService, Chk_MailService chk_mailService) {
        this.userService = userService;
        this.boardService = boardService;
        this.commentService = commentService;
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
    
    // ID중복체크
    @PostMapping("/users/Join/chkidover/{chkid}")
    @ResponseBody
    public int chkidover(@PathVariable String chkid){
        int res = userService.chkidover(chkid);
        System.out.println("중복 체크 결과 (중복:0 / 비중복:1) : " + res);
        return res;
    }

    // 로그인
    @PostMapping("/users/Login")
    public String login(@RequestParam("id")String id, @RequestParam("pw")String pw, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                out.println("<script>alert('비밀번호를 다시 확인해 주세요');</script>");
                out.flush();
                out.close();
                return "redirect:"+ request.getHeader("Referer");
            }
        }else{
            out.println("<script>alert('존재하지 않는 계정입니다');</script>");
            out.flush();
            out.close();
            return "redirect:"+ request.getHeader("Referer");
        }
    }

    // 로그아웃
    @GetMapping("/users/Logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("loginID");
        return "redirect:/";
    }

    // 마이 페이지 : 기본정보
    @RequestMapping(value = "user/{id}/MyPage/info",method={RequestMethod.GET,RequestMethod.POST})
    public String myPage_info(@PathVariable String id, Model model, HttpServletRequest request){
       if(!request.getSession().getAttribute("loginID").equals(id) || request.getSession().getAttribute("loginID") == null){ // 비로그인 혹은 세션과 접속요청 ID가 다를 경우
           System.out.println("잘못된 접근시도..!(비로그인 혹은 세션불일치)");
           return "/";
       }
        User user = userService.findOneUser(id).get();
        model.addAttribute("userInfo",user);
        model.addAttribute("cntPost",boardService.search_post_cnt("writer", id));
        model.addAttribute("cntComment",commentService.getCntComment("writer",id));
        return "users/MyPage";
    }

    // 마이 페이지 : 정보 수정(유저 업데이트)
    @GetMapping("user/{id}/MyPage/chginfo")
    public String myPage_Change(@PathVariable String id, Model model, HttpServletRequest request){
        if(!request.getSession().getAttribute("loginID").equals(id) || request.getSession().getAttribute("loginID") == null){
            System.out.println("잘못된 접근시도..!(비로그인 혹은 세션불일치)");
            return "/";
        }
        User user = userService.findOneUser(id).get();
        model.addAttribute("user",user);
        return "users/MyPage_chg";
    }

    // 마이 페이지 : 정보 수정(유저 업데이트)
    @PostMapping({"user/{id}/MyPage/chginfo"}) // 첫번째 파라미터는 어드민 및 테스트용
    public String infoChange(@PathVariable String id, @RequestParam String nick, @RequestParam String pw, @RequestParam String email, HttpServletRequest request){
        System.out.println("접근 성공");
        if(!request.getSession().getAttribute("loginID").equals(id) || request.getSession().getAttribute("loginID") == null){
            System.out.println("잘못된 접근시도..!(비로그인 혹은 세션불일치)");
            return "/";
        }

        User user = userService.findOneUser(id).get();

        // 아래사항 JS로 검증. 백엔드 복잡해짐
        if(!user.getNick().isEmpty()){
            int res = userService.updateNick(id, nick); // 업데이트 폼 JS에서 중복 검사를 할 것을 믿고 바로 업데이트 : JS에서 컨트롤러에 요청 -> 해당 아이디가 있는지..? 1반환시 빠꾸 및 정보 변경이 가능하도록 값(아직 없음) 추가
            if(res==0){
                System.out.println("닉네임 업데이트 실패!");
                return "user/"+id+"/MyPage/info";
            }
            System.out.println("닉네임 업데이트 성공!");
        }
        if(!user.getPw().isEmpty()){
            int res = userService.updatePw(id, pw);
            if(res==0){
                System.out.println("패스워드 업데이트 실패!");
                return "user/"+id+"/MyPage/info";
            }
            System.out.println("패스워드 업데이트 성공!");
        }
        if(!user.getEmail().isEmpty()){
            int res = userService.updateMail(id, email);
            if(res==0){
                System.out.println("이메일 업데이트 실패!");
                return "user/"+id+"/MyPage/info";
            }
            System.out.println("이메일 업데이트 성공!");
        }
        return "user/"+id+"/MyPage/info"; // 마이페이지 업데이트 폼으로 돌아감
    }
    




    // Select All User : 검색 후 바로 출력
    @PostMapping("/users/SelAll")
    @RequestMapping("/users/SelAll")
    public String findAllUser(Model userList){
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
            System.out.println("[ID찾기]메일 발송 완료 : "+content);
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
            System.out.println("[PW찾기]메일 발송 완료 : "+content);
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

    @PostMapping("/users/Delete") 
    public String delete(@RequestParam("id") String id){
        int res = userService.deleteUser(id);
        if (res==1) return "redirect:/users/SelAll";
        else return "Fail";
    }

    // 어드민용
    // Update User Data : 로그인이 있는 유저를 기준으로 Update실시
    // 중복데이터 업데이트를 활성화하여 join으로 대체할 수 있는지 확인
    @GetMapping({"/admin/users/Update","/admin/users/Update/{updateId}"})
    public String adminUpdateForm(@PathVariable(required = false)String updateId, Model model, HttpServletRequest request){
        if(updateId.isEmpty()){
            HttpSession session = request.getSession();
            User user = userService.findOneUser((String) session.getAttribute("loginID")).get();
        }
        User user = userService.findOneUser(updateId).get();
        model.addAttribute("userInfo",user);
        return "users/tmp/form/Update";
    }

    // 어드민용
    @PostMapping("/admin/users/Update")//{updateId}")
    public String Adminupdate(User user){
        int res  = userService.update(user); // 1성공 0실패
        if(res==1) System.out.println("계정정보 업데이트 성공!");
        else System.out.println("계정정보 업데이트 실패!");
        return "redirect:/users/SelAll"; // 새로고침. 이때 변경 정보가 반영된 것이 확인 되야함
    }

}
