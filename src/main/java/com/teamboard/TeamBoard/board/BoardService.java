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

    // 메인(페이징)
    public List<free_Board> mainView(int page){
        return boardRepository.mainView(page);
    }

    // 메인(공지)
    public List<notice_Board> listNoticeView(){return boardRepository.noticeView();} // 공지사항 게시판을 고려하지 않음

    // 글 작성
    public free_Board writeBoard(free_Board free_board){
        boardRepository.write(free_board);
        return free_board;
    }

    // 공지 작성
    public int writeNotice(notice_Board noticeBoard){
        boardRepository.write_notice(noticeBoard);
        return 1;
    }

    // 검색(전체)
    public List<free_Board> findAllBoard(){
        return boardRepository.findAll();
    }

    // 검색(범위)
    public List<free_Board> findRange(int start, int end){
        return boardRepository.findRange(start, end);
    }

    // 검색(조건)
    public List<free_Board> findBoard(String search_option, String keyword, int page){
        // 데이터 타입 포매팅 후 전송
        return boardRepository.findBoard(search_option ,keyword, page);
    }

    // 게시글 조회
    public free_Board viewBoard_free(int fboard_num){
        return boardRepository.view_free(fboard_num);
    }

    // 공지 조회
    public notice_Board viewBoard_notice(int notice_num){
        return boardRepository.view_notice(notice_num);
    }

    // 수정
    public int updateBoard(WriteForm writeForm, String category) {
        return boardRepository.update(writeForm, category);
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
