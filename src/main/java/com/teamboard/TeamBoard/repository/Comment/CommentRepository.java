package com.teamboard.TeamBoard.repository.Comment;

import com.teamboard.TeamBoard.comment.Free_comment;

import java.util.List;

public interface CommentRepository {
    int write(Free_comment free_comment);
    int delete(int fcomment_num, String userId);
    int delete(int fcomment_num);
    Long getCntComment(String option, String keyword);
    public List<Free_comment> comment_view(int target_board);
}
