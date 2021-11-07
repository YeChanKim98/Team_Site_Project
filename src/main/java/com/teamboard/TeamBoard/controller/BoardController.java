package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 자유게시판 메인
    @GetMapping("freeBoard/view/main") // 페이지 받아서 페이징 기능 추가
    public String freeBoardMain(Model model){
        List<free_Board> boardList = boardService.findAllBoard();
        model.addAttribute("boardList",boardList);
        return "boards/tmp/FreeBoardMain"; // 메인페이지로 이동
    }

    // 작성
    @GetMapping("freeBoard/write/{id}")
    public String freeWrite(){
        return "boards/tmp/WriteForm";
    }

    @PostMapping("freeBoard/write/{id}")
    public String freeWrite(@PathVariable String id, WriteForm writeForm){
        // 리덱터리 리스팅 회피 : writer변수와 세션에서 받은 변수의 값이 일치하지 않으면 메인으로 리다이렉트
        free_Board board = new free_Board();
        board.setFboard_writer(id);
        board.setFboard_title(writeForm.getFboard_title());
        board.setFboard_content(writeForm.getFboard_content());
        boardService.writeBoard(board);
        return "redirect:freeBoard/view/main"; // 게시판 메인으로 이동
    }

    // 삭제
    @PostMapping("freeBoard/delete/{fboard_num}")
    public String freeDelete(@PathVariable int fboard_num){
        boardService.deleteBoard(fboard_num);
        return "redirect:freeBoard/view/main"; // 게시판 메인으로 이동
    }

    // 수정
    @GetMapping("freeBoard/Update/{id}/{fboard_num}")
    public String freeUpdate(@PathVariable String id, @PathVariable int fboard_num, Model model){
        free_Board board = boardService.viewBoard(fboard_num);
        model.addAttribute("fboard_num",fboard_num);
        model.addAttribute("fboard_id",id);
        model.addAttribute("fboard_title",board.getFboard_title());
        model.addAttribute("fboard_content",board.getFboard_content());
        return "redirect:/"; //수성 폼으로  이동
    }

    @PostMapping("freeBoard/Update/{id}/{fboard_num}")
    public String freeUpdate(@PathVariable String id, @PathVariable int fboard_num, WriteForm writeForm){
        boardService.updateBoard(writeForm);
        return "redirect:freeBoard/view/main"; // 게시판 메인으로 이동
    }

    // 조회 : 서비스에서 조횟수 알고리즘 추가
    @GetMapping("freeBoard/view/{fboard_num}")
    public String viewFboard(@PathVariable int fboard_num, Model model){
        free_Board board = boardService.viewBoard(fboard_num);
        model.addAttribute("view",board);
        return"redirect:/"; // 뷰페이지로 이동
    }

    // 게시글 검색

}
