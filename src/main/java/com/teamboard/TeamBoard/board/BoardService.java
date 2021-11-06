package com.teamboard.TeamBoard.board;

import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.repository.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {this.boardRepository = boardRepository;}

    // 작성
    public void writeBoard(free_Board free_board){
        boardRepository.write(free_board);
    }

    // 삭제
    public int deleteBoard(int fboard_num){
        return boardRepository.delete(fboard_num);
    }

    // 수정
    public int updateBoard(WriteForm writeForm) {
        return boardRepository.update(writeForm);
    }

    // 조회
    public free_Board viewBoard(int fboard_num){
        return boardRepository.view(fboard_num);
    }

    // 검색(전체)
    public List<free_Board> findAllBoard(){
        return boardRepository.findAll();
    }

    // 검색(조건)
    public List<free_Board> findBoard(String keyword, String std){
        return boardRepository.findBoard(keyword, std);
    }

}
