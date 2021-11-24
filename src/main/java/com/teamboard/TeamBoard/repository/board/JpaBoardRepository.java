package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
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

    // 작성
    public free_Board write(free_Board freeBoard) {
        em.persist(freeBoard);
        return  freeBoard;
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
        return em.createQuery("update free_Board fb set fb.fboard_writer=:fboard_writer, fb.fboard_content=:fboard_content where fb.fboard_num=:fboard_num") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("fboard_writer",writeForm.getFboard_title())
                .setParameter("fboard_content",writeForm.getFboard_content())
                .setParameter("fboard_num",writeForm.getFboard_num())
                .executeUpdate();
    }

    // 조회
    public free_Board view(int fboard_num) {
        // 조회 수 증가
        em.createQuery("update free_Board fb set fb.fboard_view_count=fb.fboard_view_count+1 where fb.fboard_num=:fboard_num")
                .setParameter("fboard_num",fboard_num)
                .executeUpdate();

        return em.createQuery("select fb from free_Board fb where fb.fboard_num=:fboard_num",free_Board.class)
                .setParameter("fboard_num",fboard_num)
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
        String findQuery="select distinct fb from free_Board fb where fb.fboard_writer=:keyword or fb.fboard_title=:keyword or fb.fboard_content=:keyword order by fb.fboard_num desc ";
        if(search_option.equals("writer")){
            findQuery = "select fb from free_Board fb where fb.fboard_writer=:keyword order by fb.fboard_num desc ";
        } else if(search_option.equals("title")){
            findQuery = "select fb from free_Board fb where fb.fboard_title=:keyword order by fb.fboard_num desc ";
        }else if(search_option.equals("content")){
            findQuery = "select fb from free_Board fb where fb.fboard_content=:keyword order by fb.fboard_num desc ";
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
        return  em.createQuery("select fb from free_Board fb order by fb.fboard_num desc",free_Board.class)
                .setFirstResult(start) // 시작부터
                .setMaxResults(10) // 10개씩 출력
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
