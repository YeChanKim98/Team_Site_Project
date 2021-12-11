package com.teamboard.TeamBoard.repository.Comment;

import com.teamboard.TeamBoard.comment.Free_comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;
    public JpaCommentRepository(EntityManager em){this.em=em;}

    public int write(Free_comment free_comment){
        em.persist(free_comment);
        em.createQuery("update free_Board fb set fb.fboard_comment_count=fb.fboard_comment_count+1 where fb.fboard_num=:num")
                .setParameter("num",free_comment.getTarget_board())
                .executeUpdate();
        return 1;
    }

    public int delete(int fcomment_num){
        em.createQuery("update free_Board fb set fb.fboard_comment_count=fb.fboard_comment_count-1 where fb.fboard_num=(select fc.target_board from Free_comment fc where fc.fcomment_num=:num)")
        .setParameter("num",fcomment_num)
        .executeUpdate();

        return em.createQuery("delete from Free_comment fc where fc.fcomment_num=:fcomment_num")
                .setParameter("fcomment_num", fcomment_num)
                .executeUpdate();
    }

    public List<Free_comment> comment_view(int target_board){
        return em.createQuery("select fc from Free_comment fc where fc.target_board=:target_board", Free_comment.class)
                .setParameter("target_board", target_board)
                .getResultList();
    }

}