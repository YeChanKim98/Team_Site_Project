package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.comment.CommentService;
import com.teamboard.TeamBoard.comment.Free_comment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class CommentControllor {
    private final CommentService commentService;
    @Autowired
    public CommentControllor(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/freeBoard/{target_board}/comment/write") // 주소에 게시판 종류 유동적으로 변경
    public String writeComment(@PathVariable int target_board, @RequestParam String write_comment_content, HttpServletRequest request){
        HttpSession session = request.getSession();
        String writer="";
        if (session.getAttribute("loginID")==null) writer = "anonymous";
        else writer = session.getAttribute("loginID").toString();
        System.out.println("Writer : "+writer);
        Free_comment comment = new Free_comment();
        comment.setFcomment_writer(writer);
        comment.setTarget_board(target_board);
        comment.setFcomment_content(write_comment_content);
        int res = commentService.writeComment(comment); // 현재 res사용 X
        return "redirect:/freeBoard/view/"+target_board;
    }

    @GetMapping("/freeBoard/{target_board}/comment/delete/{comment_num}") // 주소에 게시판 종류 유동적으로 변경
    public String deleteComment(@PathVariable int target_board, @PathVariable int comment_num, HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        if(session.getAttribute("loginID")==null) return "redirect:freeBoard/view/"+target_board; // alert..?
        int res = commentService.deleteComment(comment_num,session.getAttribute("loginID").toString());
        if(res==0){
            out.println("<script>alert('잘못된 접근 혹은 삭제 실패입니다');location.href='/freeBoard/view/main/1';</script>");
            out.flush();
            out.close();
        }
        return "redirect:/freeBoard/view/"+target_board;
    }

}
