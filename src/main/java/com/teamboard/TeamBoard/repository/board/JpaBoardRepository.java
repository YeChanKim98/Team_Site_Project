package com.teamboard.TeamBoard.repository.board;

import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// DAO
public class JpaBoardRepository implements BoardRepository{

    @PersistenceContext
    private final EntityManager em;
    public JpaBoardRepository(EntityManager em) { this.em = em; }

    // 작성
    @Override
    public free_Board write(free_Board freeBoard) {
        em.persist(freeBoard);
        return  freeBoard;
    }

    // 삭제 : DB이동 -> 조건추가 : num이랑 세션이 가진 작성자랑 같으면 넘어오도록 컨트롤러에 추가
    @Override
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
    @Override
    public int update(WriteForm writeForm) {
        return em.createQuery("update free_Board fb set fb.fboard_writer=:fboard_writer, fb.fboard_content=:fboard_content where fb.fboard_num=:fboard_num") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("fboard_writer",writeForm.getFboard_title())
                .setParameter("fboard_content",writeForm.getFboard_content())
                .setParameter("fboard_num",writeForm.getFboard_num())
                .executeUpdate();
    }

    // 조회
    @Override
    public free_Board view(int fboard_num) {
        return em.createQuery("select fb from free_Board fb where fb.fboard_num=:fboard_num",free_Board.class)
                .setParameter("fboard_num",fboard_num)
                .getResultList().stream().findAny().get();
    }

    // 전체 게시글
    @Override
    public List<free_Board> findAll() {
        return em.createQuery("select fb from free_Board fb",free_Board.class).getResultList();
    }

    // 검색
    @Override
    public List<free_Board> findBoard(String keyword, String std) {
        String findQuery="select distinct fb from free_Board fb where fb.fboard_writer=:keyword or fb.fboard_content=:keyword";
        if(std=="writer"){
            findQuery = "select fb from free_Board fb where fb.fboard_writer=:keyword";
        } else if(std=="content"){
            findQuery = "select fb from free_Board fb where fb.fboard_content=:keyword";
        }
        return em.createQuery(findQuery, free_Board.class)
                .setParameter("keyword",keyword)
                .getResultList();
    }

}
