package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.notice_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final BoardService boardService;
    @Autowired
    public AdminController(BoardService boardService) {
        this.boardService = boardService;
    }


    // 어드민 접근 시도 페이지
    @GetMapping("/Site/Account/Admin/{ConnectUser}")
    public String ConnectAdmin(@PathVariable String ConnectUser){
        if(ConnectUser.equals("YC")||ConnectUser.equals("ES")||ConnectUser.equals("NH")){
            return "/admin/chkAdmin";
        }
        return "redirect:/";
    }

    // 어드민 접근 체크 및 승인
    @PostMapping("/Site/Account/Admin/Main")
    public String ConnectAdmin(@RequestParam String password, HttpServletRequest request){
        HttpSession session = request.getSession();
        String user = (String)session.getAttribute("loginID");
        if(password.equals("0000")){
            System.out.println("HELLO ADMIN-!!");
            if(user==null) session.setAttribute("loginID", "Admin");
            return "/admin/AdminMain";
        }else{
            System.out.println("[어드민 접근 체크]어림없지");
            return "redirect:/";
        }
    }

    @PostMapping("/Admin/postNotice")
    public String WriteNotice(@RequestParam String title ,@RequestParam String content, HttpServletRequest request){
        System.out.println("공지작성 요청자 ID : "+request.getSession().getAttribute("loginID"));
        if(request.getSession().getAttribute("loginID").equals("Admin")){
            notice_Board notice = new notice_Board();
            notice.setNotice_writer("Admin");
            notice.setNotice_title("[공지] " + title);
            notice.setNotice_content(content);
            int res = boardService.writeNotice(notice);
            if(res==1){
                System.out.println("공지 작성 완료!!");
                return "/admin/AdminMain"; // 리턴받고 새로고침하면 이전 이전값을 기억하고 다시 입력됨
            }
            else{
                System.out.println("공지 작성 실패!!");
                return "redirect:/Site/Account/Admin/YC";
            }
        }else return "redirect:/";
    }

}
