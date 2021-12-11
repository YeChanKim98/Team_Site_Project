package com.teamboard.TeamBoard.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CommentControllor {
    private final CommentService commentService;
    @Autowired
    public CommentControllor(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/fboard/{target_board}/comment/write")
    public String writeComment(@PathVariable int target_board, @RequestParam String content, HttpServletRequest request){
        System.out.println("controllor on!!!!!!!!!!");
        HttpSession session = request.getSession();
        String writer = "";
        if (session.getAttribute("LoginID")==null) {
            writer = "anonymous";
        }else{
            writer = session.getAttribute("LoginID").toString();
        }
        Free_comment comment = new Free_comment();
        comment.setFcomment_writer(writer);
        comment.setTarget_board(target_board);
        comment.setFcomment_content(content);

        System.out.println("@@@@@!!!!!"+writer+"/"+target_board+"/"+content);

        int res = commentService.writeComment(comment);
        if(res==0){
            System.out.println("comment fail");
        }
        System.out.println("goViewer : "+target_board);
        return "redirect:/freeBoard/view/"+target_board;
    }

    @GetMapping("fboard/{fboard_num}/comment/delete/{comment_num}")
    public String deleteComment(@PathVariable int target_board, @PathVariable int comment_num, HttpServletRequest request){
        commentService.deleteComment(comment_num);
        return "redirect:freeBoard/view/"+target_board;
    }

}
