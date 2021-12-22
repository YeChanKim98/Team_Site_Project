package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.board.notice_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final BoardService boardService;

    @Autowired
    public HomeController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<free_Board> freeBoardList = boardService.findRange(0, 8);
        List<notice_Board> noticeBoardList = boardService.listNoticeView().subList(0, 8);
        model.addAttribute("freeBoardList", freeBoardList);
        model.addAttribute("noticeBoardList", noticeBoardList);
        return "home";
    }
}