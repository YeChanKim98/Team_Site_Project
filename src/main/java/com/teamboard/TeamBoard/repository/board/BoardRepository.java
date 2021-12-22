package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.board.notice_Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository {
    free_Board write(free_Board freeBoard);
    int write_notice(notice_Board noticeBoard);
    List<notice_Board> noticeView(); // 타 게시판 공지 노출용
    int delete(int fboard_num);
    int deleteNotice(int num);
    int update(WriteForm writeForm, String category);
    free_Board view_free(int fboard_num);
    notice_Board view_notice(int fboard_num);
    List<free_Board> findAll();
    List<free_Board> findRange(int start, int end);
    List<free_Board> findBoard(String keyword, String std, int page);
    List<free_Board> mainView(int page); // 나중에 정리할거임
    Long post_cnt();
    Long search_post_cnt(String search_option, String keyword);
}
