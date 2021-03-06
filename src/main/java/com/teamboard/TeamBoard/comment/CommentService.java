package com.teamboard.TeamBoard.comment;

import com.teamboard.TeamBoard.repository.Comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {this.commentRepository = commentRepository;}

    public int writeComment(Free_comment free_comment){
        return commentRepository.write(free_comment);
    }

    public int deleteComment(int fcomment_num,String userId){
        return commentRepository.delete(fcomment_num,userId);
    }

    public int deleteComment(int fcomment_num){
        return commentRepository.delete(fcomment_num);
    }

    public List<Free_comment> comment_view(int target_board){
        return commentRepository.comment_view(target_board);
    }

    public List<Free_comment> findComment(String search_option, String keyword){
        return commentRepository.findComment(search_option, keyword);
    }

    public long getCntComment(String option, String keyword){
        return commentRepository.getCntComment(option, keyword);
    }
}
