package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.board.notice_Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

// DAO
@Repository
public class JpaBoardRepository implements BoardRepository{

    @PersistenceContext
    private final EntityManager em;
    public JpaBoardRepository(EntityManager em) { this.em = em; }

    // 글 작성
    public free_Board write(free_Board freeBoard) {
        em.persist(freeBoard);
        return  freeBoard;
    }

    // 공지 작성
    @Override
    public int write_notice(notice_Board noticeBoard) {
        em.persist(noticeBoard);
        return  1;
    }

    // 삭제 : DB이동 -> 조건추가 : num이랑 세션이 가진 작성자랑 같으면 넘어오도록 컨트롤러에 추가
    public int delete(int fboard_num) {

//        삭제할 인스턴스를 받은 후 delete에 옮기고 원본은 삭제
//        Optional<free_Board> target = em.createQuery("select fb from free_Board fb where fb.fboard_num=:fboard_num", free_Board.class)
//                .setParameter("fboard_num",fboard_num)
//                .getResultList().stream().findAny();

        return em.createQuery("delete from free_Board fb where fb.fboard_num=:fboard_num")
                .setParameter("fboard_num",fboard_num)
                .executeUpdate();
    }

    // 수정
    public int update(WriteForm writeForm) {
        System.out.println("게시글 수정 레포지트리 실행");
        System.out.println(writeForm.getFboard_title());
        System.out.println(writeForm.getFboard_content());
        System.out.println(writeForm.getFboard_num());
        return em.createQuery("update free_Board fb set fb.fboard_title=:fboard_title, fb.fboard_content=:fboard_content where fb.fboard_num=:fboard_num")
                .setParameter("fboard_title",writeForm.getFboard_title())
                .setParameter("fboard_content",writeForm.getFboard_content())
                .setParameter("fboard_num",writeForm.getFboard_num())
                .executeUpdate();
    }

    // 일반글 조회
    public free_Board view_free(int fboard_num) {
        // 조회 수 증가 : 현재 범용으로 가져오는 메서드가 없어서 수정시 해당 메서드를 이용하는데, 이때 뷰가 같이 증가함
        em.createQuery("update free_Board fb set fb.fboard_view_count=fb.fboard_view_count+1 where fb.fboard_num=:fboard_num")
                .setParameter("fboard_num",fboard_num)
                .executeUpdate();

        return em.createQuery("select fb from free_Board fb where fb.fboard_num=:fboard_num",free_Board.class)
                .setParameter("fboard_num",fboard_num)
                .getResultList().stream().findAny().get();
    }


    // 공지 조회
    public notice_Board view_notice(int notice_num) {
        // 조회 수 증가
        em.createQuery("update notice_Board nb set nb.notice_view_count=nb.notice_view_count+1 where nb.notice_num=:notice_num")
                .setParameter("notice_num",notice_num)
                .executeUpdate();

        return em.createQuery("select nb from notice_Board nb where nb.notice_num=:notice_num",notice_Board.class)
                .setParameter("notice_num",notice_num)
                .getResultList().stream().findAny().get();
    }


    // 전체 게시글
    public List<free_Board> findAll() {
        return em.createQuery("select fb from free_Board fb",free_Board.class).getResultList();
        // 리소스 낭비가 심하므로 목록에서는 콘텐츠 내용은 제외하고 받아오기

    }

    // 검색
    public List<free_Board> findBoard(String search_option, String keyword, int page) {
        int start = (page-1)*10;
        String findQuery="select distinct fb from free_Board fb where fb.fboard_writer like CONCAT('%',:keyword,'%') or fb.fboard_title like CONCAT('%',:keyword,'%') or fb.fboard_content like CONCAT('%',:keyword,'%') order by fb.fboard_num desc ";
        if(search_option.equals("writer")){
            findQuery = "select fb from free_Board fb where fb.fboard_writer like CONCAT('%',:keyword,'%') order by fb.fboard_num desc ";
        } else if(search_option.equals("title")){
            findQuery = "select fb from free_Board fb where fb.fboard_title like CONCAT('%',:keyword,'%') order by fb.fboard_num desc ";
        }else if(search_option.equals("content")){
            findQuery = "select fb from free_Board fb where fb.fboard_content like CONCAT('%',:keyword,'%') order by fb.fboard_num desc ";
        }
        return em.createQuery(findQuery, free_Board.class)
                .setParameter("keyword",keyword)
                .setFirstResult(start)
                .setMaxResults(10)
                .getResultList();
    }


    // 페이징이 들어간 게시판 메인 뷰
    public List<free_Board> mainView(int page){
        int start = (page-1)*10;
        int showPost = 10;
        if(page == 1) showPost = 5;
        else start-=5;
        return  em.createQuery("select fb from free_Board fb order by fb.fboard_num desc",free_Board.class)
                .setFirstResult(start) // 시작부터
                .setMaxResults(showPost) // 기본 10개씩 출력(단, 첫번째 페이지 최대 5개는 공지글임)
                .getResultList();
    }


    // 타 게시판에서보는 공지글 출력용
    public List<notice_Board> noticeView(){
        return  em.createQuery("select nb from notice_Board nb order by nb.notice_num desc",notice_Board.class)
                .setMaxResults(5) // 기본 10개씩 출력(단, 첫번째 페이지 최대 5개는 공지글임)
                .getResultList();
    }
    
    // 게시글 총 갯수
    public Long post_cnt(){
        return (Long) em.createQuery("select count(fb) from free_Board fb")
                .getSingleResult();
    }

    // 검색 게시글 총 갯수
    public Long search_post_cnt(String search_option, String keyword){
        String findQuery="select count(fb) from free_Board fb where fb.fboard_writer=:keyword or fb.fboard_title=:keyword or fb.fboard_content=:keyword";
        if(search_option.equals("writer")){
            findQuery = "select count(fb) from free_Board fb where fb.fboard_writer=:keyword";
        } else if(search_option.equals("title")){
            findQuery = "select count(fb) from free_Board fb where fb.fboard_title=:keyword";
        }else if(search_option.equals("content")){
            findQuery = "select count(fb) from free_Board fb where fb.fboard_content=:keyword";
        }
        return (Long)em.createQuery(findQuery)
                .setParameter("keyword",keyword)
                .getSingleResult();
    }
}
