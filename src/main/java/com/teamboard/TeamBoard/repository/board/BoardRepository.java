package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository {
    free_Board write(free_Board freeBoard);
    int delete(int fboard_num);
    int update(WriteForm writeForm);
    free_Board view(int fboard_num);
    List<free_Board> findAll();
    List<free_Board> findBoard(String keyword, String std, int page);
    List<free_Board> mainView(int page); // 나중에 정리할거임
    Long post_cnt();
    Long search_post_cnt(String search_option, String keyword);
}
