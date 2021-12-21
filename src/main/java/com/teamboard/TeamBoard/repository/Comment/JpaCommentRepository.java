package com.teamboard.TeamBoard.repository.Comment;

import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.comment.Free_comment;
import lombok.Builder;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;
    public JpaCommentRepository(EntityManager em){this.em=em;}

    @Override
    public int write(Free_comment free_comment){
        em.persist(free_comment);
        em.createQuery("update free_Board fb set fb.fboard_comment_count=fb.fboard_comment_count+1 where fb.fboard_num=:num")
                .setParameter("num",free_comment.getTarget_board())
                .executeUpdate();
        return 1;
    }

    @Override
    public int delete(int fcomment_num,String userId){
        em.createQuery("update free_Board fb set fb.fboard_comment_count=fb.fboard_comment_count-1 where fb.fboard_num=(select fc.target_board from Free_comment fc where fc.fcomment_num=:num)")
        .setParameter("num",fcomment_num)
        .executeUpdate();

        return em.createQuery("delete from Free_comment fc where fc.fcomment_num=:fcomment_num and fc.fcomment_writer=:writer")
                .setParameter("fcomment_num", fcomment_num)
                .setParameter("writer",userId)
                .executeUpdate();
    }

    @Override// get UserID메서드를 따로 만들어서 요청지가 작성자(혹은 비밀번호)에 의한 삭제일 경우 ID를 받아서 인자로 넣어서 실행
    public int delete(int fcomment_num) {
        return em.createQuery("delete from Free_comment fc where fc.target_board=:fcomment_num")
                .setParameter("fcomment_num", fcomment_num)
                .executeUpdate();
    }

    @Override
    public List<Free_comment> comment_view(int target_board){
        return em.createQuery("select fc from Free_comment fc where fc.target_board=:target_board", Free_comment.class)
                .setParameter("target_board", target_board)
                .getResultList();
    }

    public Long getCntComment(String option, String keyword){
        String findQuery="";
        if(option.equals("writer")){
            findQuery = "select count(fc) from Free_comment fc where fc.fcomment_writer=:keyword";
        } else if(option.equals("board")){
            findQuery = "select count(fc) from Free_comment fc where fc.target_board=:keyword";
        }
        return (Long)em.createQuery(findQuery)
                .setParameter("keyword",keyword)
                .getSingleResult();
    }

    @Override
    public List<Free_comment> findComment(String search_option, String keyword) {
        String findQuery="select distinct fc from Free_comment fc where fc.fcomment_writer like CONCAT('%',:keyword,'%') or fc.target_board=:keyword or fc.fcomment_content like CONCAT('%',:keyword,'%') order by fc.fcomment_num desc ";
        if(search_option.equals("writer")){
            findQuery = "select fc from Free_comment fc where fc.fcomment_writer like CONCAT('%',:keyword,'%') order by fc.fcomment_num desc ";
        } else if(search_option.equals("target_board")){
            findQuery = "select fc from Free_comment fc where fc.target_board=:keyword order by fc.fcomment_num desc ";
        }else if(search_option.equals("content")){
            findQuery = "select fc from Free_comment fc where fc.fcomment_content like CONCAT('%',:keyword,'%') order by fc.fcomment_num desc ";
        }
        return em.createQuery(findQuery, Free_comment.class)
                .setParameter("keyword",keyword)
                .getResultList();
    }
}
