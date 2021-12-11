package com.teamboard.TeamBoard.comment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Transactional
public class Free_comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fcomment_num;
    private int target_board;
    private String fcomment_writer;
    private String fcomment_content;
    private String fcomment_reg_date;

    public int getFcomment_num() {
        return fcomment_num;
    }

    public String getFcomment_writer() {
        return fcomment_writer;
    }

    public String getFcoment_content() {
        return fcomment_content;
    }

    public String getFcomment_reg_date() {
        return fcomment_reg_date;
    }

    public int getTarget_board() {
        return target_board;
    }

    public void setFcomment_num(int fcomment_num) {
        this.fcomment_num = fcomment_num;
    }

    public void setFcomment_writer(String fcomment_writer) {
        this.fcomment_writer = fcomment_writer;
    }

    public void setFcomment_content(String fcomment_content) {
        this.fcomment_content = fcomment_content;
    }

    public void setFcomment_reg_date(String fcomment_reg_date) {
        this.fcomment_reg_date = fcomment_reg_date;
    }

    public void setTarget_board(int target_board) {this.target_board = target_board;}


    @PrePersist
    public void onPrePersist(){
        this.fcomment_reg_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
