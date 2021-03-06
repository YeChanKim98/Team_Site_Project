package com.teamboard.TeamBoard.boards;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.repository.board.BoardRepository;
import com.teamboard.TeamBoard.repository.board.JpaBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class BoardCrudTest {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Test
    @Commit
    void 게시글작성() {

        for(int i = 0 ; i < 50 ; i++) {
            free_Board board = new free_Board();

            board.setFboard_writer("Board_Test_Account");
            board.setFboard_title("Test_Post");
            board.setFboard_content("Hello_Post_!");

            free_Board resBoard = boardService.writeBoard(board);
        }
//
//        System.out.println("ResBoard : "+resBoard.getFboard_writer()+"\t"+resBoard.getFboard_title()+"\t"+resBoard.getFboard_content());
//        free_Board compBoard = boardService.viewBoard(7);
//
//        Assertions.assertThat(resBoard.getFboard_title()).isEqualTo(compBoard.getFboard_title());
//        System.out.println("CompBoard : "+compBoard.getFboard_writer()+"\t"+compBoard.getFboard_title()+"\t"+compBoard.getFboard_content());
    }

    @Test
    @Commit
    void 뷰_조회수증가(){
        int fboard_num = 12; // test post number
        boardService.viewBoard_free(fboard_num);
        System.out.println(fboard_num+"번 게시물의 조회수 1증가");

    }

//    @Test
//    void 카운터(){
//        Long res = boardRepository.post_cnt();
//        System.out.println("토탈 포스터 : "+res);
//    }
}
