package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board write(Board board);
    int delete(int fboard_num);
    int update(int fboard_num);
    List<Board> findAll();
    Board findOne(int fboard_num);
    Board findByContent(String keyword);
    Board findByWriter(String keyword);
}
