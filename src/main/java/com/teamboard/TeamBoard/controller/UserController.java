package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.comment.CommentService;
import com.teamboard.TeamBoard.comment.Free_comment;
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
                out.println("<script>alert('비밀번호를 다시 확인해 주세요');location.href='/';</script>");
                out.flush();
                out.close();
                return "redirect:"+ request.getHeader("Referer");
            }
        }else{
            out.println("<script>alert('존재하지 않는 계정입니다');location.href='/';</script>");
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
        return "redirect:"+ request.getHeader("Referer");
    }

    // ID중복체크
    @PostMapping("/users/Join/chkidover/{chkid}")
    @ResponseBody
    public int chkidover(@PathVariable String chkid){
        int res = userService.chkidover(chkid);
        System.out.println("중복 체크 결과 (중복:0 / 비중복:1) : " + res);
        return res;
    }




    // 마이 페이지 : 기본정보
    @RequestMapping("user/{id}/MyPage/info")
    public String myPage_info(@PathVariable String id, Model model, HttpServletRequest request){
       if(!request.getSession().getAttribute("loginID").equals(id) || request.getSession().getAttribute("loginID") == null){ // 비로그인 혹은 세션과 접속요청 ID가 다를 경우
           System.out.println("잘못된 접근시도..!(비로그인 혹은 세션불일치)");
           return "/";
       }
        User user = userService.findOneUser(id).get();
        model.addAttribute("userInfo",user);
        model.addAttribute("menu","info");
        model.addAttribute("cntPost",boardService.search_post_cnt("writer", id));
        model.addAttribute("cntComment",commentService.getCntComment("writer",id));
        return "users/MyPage";
    }

    // 마이 페이지 : 정보 수정폼(유저 업데이트)
    @RequestMapping("user/{id}/MyPage/chginfo")
    public String myPage_Change(@PathVariable String id, Model model, HttpServletRequest request){
        System.out.println("여기Request");
        if(!request.getSession().getAttribute("loginID").equals(id) || request.getSession().getAttribute("loginID") == null){
            System.out.println("잘못된 접근시도..!(비로그인 혹은 세션불일치)");
            return "/";
        }
        User user = userService.findOneUser(id).get();
        model.addAttribute("menu","chginfo");
        model.addAttribute("user",user);
        return "users/MyPage";
    }

    // 마이 페이지 : 정보 수정동작(유저 업데이트)
    @PostMapping("user/{id}/MyPage/chginfo") // 첫번째 파라미터는 어드민 및 테스트용
    public String infoChange(@PathVariable String id, @RequestParam String nick, @RequestParam String pw, @RequestParam String email, HttpServletRequest request){
        System.out.println("여기POST");
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
                return "redirect:user/"+id+"/MyPage/info";
            }
            System.out.println("닉네임 업데이트 성공!");
        }
        if(!user.getPw().isEmpty()){
            int res = userService.updatePw(id, pw);
            if(res==0){
                System.out.println("패스워드 업데이트 실패!");
                return "redirect:user/"+id+"/MyPage/info";
            }
            System.out.println("패스워드 업데이트 성공!");
        }
        if(!user.getEmail().isEmpty()){
            int res = userService.updateMail(id, email);
            if(res==0){
                System.out.println("이메일 업데이트 실패!");
                return "redirect:user/"+id+"/MyPage/info";
            }
            System.out.println("이메일 업데이트 성공!");
        }
        System.out.println("정보변경성공 : user/"+id+"/MyPage/info");
        return "redirect:/user/"+id+"/MyPage/info"; // 마이페이지 업데이트 폼으로 돌아감
    }
    
    // 마이페이지 : 내가 쓴 글
    @RequestMapping("user/{id}/MyPage/own")
    public String getOwn(@PathVariable String id, Model model){
        System.out.println("내가 쓴 글 조회 시작");
        List<free_Board> fboardList = boardService.findBoard("writer",id,1);
        if(fboardList.size() > 10) fboardList = fboardList.subList(0,10);

        List<Free_comment> fcommentList = commentService.findComment("writer",id);
        if(fcommentList.size() > 10) fcommentList = fcommentList.subList(0,10);

        model.addAttribute("menu","own");
        model.addAttribute("freeBoard",fboardList);
        model.addAttribute("freeComment",fcommentList);
        System.out.println("내 게시글");
        return "users/MyPage";
    }

    // 마이페이지 : 계정 탈퇴
    @GetMapping("user/{id}/MyPage/withdrawal")
    public String deleteForm_User(Model model){
        model.addAttribute("menu","withdrawal");
        return "users/MyPage";
    }

    // 유저용
    @PostMapping("user/{id}/MyPage/withdrawal")
    public String deleteUser(@RequestParam String pw, HttpServletRequest request){
        HttpSession session = request.getSession();
        String id = session.getAttribute("loginID").toString();
        if(id.equals("Admin")) return "redirect:/";
        User user = userService.findOneUser(id).get();
        if(user.getPw().equals(pw)){
            chk_MailService.deleteMail(user.getEmail()); // 메일 인증정보 삭제
            userService.deleteUser(pw);
            session.removeAttribute("loginID");
            return "redirect:/";
        }else{
            System.out.println("PW불일치");
            return "redirect:/user/"+id+"/MyPage/info";
        }
    }





    // 어드민용 유저 목록 출력
    @RequestMapping("admin/user/userList")
    public String findAllUser(Model userList){
        List<User> users = userService.findAllUser();
        userList.addAttribute("users",users);
        return "admin/AdminMain";
    }

    // 어드민용 유저 업데이트 폼
    @GetMapping("/admin/user/{id}/update")
    public String adminUpdateForm(@PathVariable String id, Model model){
        User user = userService.findOneUser(id).get();
        model.addAttribute("userInfo",user);
        return "admin/userUpdate";
    }

    // 어드민용 유저 업데이트 로직
    @PostMapping("/admin/user/{id}/update")
    public String Adminupdate(User user){
        int res  = userService.update(user);
        if(res==1) System.out.println("계정정보 업데이트 성공!");
        else System.out.println("계정정보 업데이트 실패!");
        return "redirect:/admin/user/userList";
    }

    // 어드민용 유저 삭제
    @RequestMapping("/admin/user/{id}/delete")
    public String AdminDelete(@PathVariable String id){
        chk_MailService.deleteMail(userService.findOneUser(id).get().getEmail()); // 메일 인증정보 삭제
        userService.deleteUser(id);
        return "redirect:/admin/user/userList";
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
            out.println("<script>alert('이름혹은 이메일을 다시 확인해 주세요');location.href='/';</script>");
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
            out.println("<script>alert('아이디혹은 이메일을 다시 확인해 주세요');location.href='/';</script>");
            out.flush();
            out.close();
            return "/";
        }
    }

}
