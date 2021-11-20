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

    // 서비스에서 jpa리포지토리의 개인 메서드를 이용하게 하기위해 빈 똑바로 잡아주기
    
    private final BoardRepository boardRepository;
    // private final BoardRepository JpaboardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {this.boardRepository = boardRepository;}
    // public BoardService(BoardRepository jpaboardRepository) {this.boardRepository = jpaboardRepository;}

    // 메인(페이징)
    public List<free_Board> mainView(int page){
        return boardRepository.mainView(page);
    }

    // 작성
    public free_Board writeBoard(free_Board free_board){
        boardRepository.write(free_board);
        return free_board;
    }

    // 검색(전체)
    public List<free_Board> findAllBoard(){
        return boardRepository.findAll();
    }

    // 검색(조건)
    public List<free_Board> findBoard(String search_option, String keyword, int page){
        // 데이터 타입 포매팅 후 전송
        return boardRepository.findBoard(search_option ,keyword, page);
    }

    // 게시글 조회
    public free_Board viewBoard(int fboard_num){
        return boardRepository.view(fboard_num);
    }

    // 수정
    public int updateBoard(WriteForm writeForm) {
        return boardRepository.update(writeForm);
    }

    // 삭제
    public int deleteBoard(int fboard_num){
        return boardRepository.delete(fboard_num);
    }

    // 전체 게시글 수 반환
    public Long totalPost(){
        return boardRepository.post_cnt();
    }
    public Long search_post_cnt(String search_option, String keyword){
        Long res = boardRepository.search_post_cnt(search_option, keyword);
        return res;
    }


}
