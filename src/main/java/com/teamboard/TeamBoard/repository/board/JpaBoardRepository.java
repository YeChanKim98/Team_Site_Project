package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Board;
import java.util.List;

public class JpaBoardRepository implements BoardRepository{
    @Override
    public Board write(Board board) {
        return null;
    }

    @Override
    public int delete(int fboard_num) {
        return 0;
    }

    @Override
    public int update(int fboard_num) {
        return 0;
    }

    @Override
    public List<Board> findAll() {
        return null;
    }

    @Override
    public Board findOne(int fboard_num) {
        return null;
    }

    @Override
    public Board findByContent(String keyword) {
        return null;
    }

    @Override
    public Board findByWriter(String keyword) {
        return null;
    }
}
