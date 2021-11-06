package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    
    // 작성
    @GetMapping("freeBoard/{writer}/write")
    public String freeWrite(@PathVariable String writer, WriteForm writeForm){
        free_Board board = new free_Board();
        board.setFboard_writer(writer);
        board.setFboard_title(writeForm.getFboard_title());
        board.setFboard_content(writeForm.getFboard_content());
        boardService.writeBoard(board);
        return ""; // 게시판 메인으로 이동
    }

    // 삭제

    
    // 수정

    // 조회 : 조횟수 알고리즘 추가
    
    // 검색(전체)
    
    // 검색(조건)

}
