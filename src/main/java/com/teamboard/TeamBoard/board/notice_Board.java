package com.teamboard.TeamBoard.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Transactional
public class notice_Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notice_num; // 자동, PK
    private String notice_writer; // 입력, not null
    private String notice_title; // 입력, not null
    private String notice_content=null; // 입력, not null
    private int notice_view_count; // 자동, default 0
    private int notice_comment_count; // 자동, default 0
    private String notice_reg_date;//=null; // 자동


    public int getNotice_num() {
        return notice_num;
    }

    public void setNotice_num(int notice_num) {
        this.notice_num = notice_num;
    }

    public String getNotice_writer() {
        return notice_writer;
    }

    public void setNotice_writer(String notice_writer) {
        this.notice_writer = notice_writer;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public int getNotice_view_count() {
        return notice_view_count;
    }

    public void setNotice_view_count(int notice_view_count) {
        this.notice_view_count = notice_view_count;
    }

    public int getNotice_comment_count() {
        return notice_comment_count;
    }

    public void setNotice_comment_count(int notice_comment_count) {
        this.notice_comment_count = notice_comment_count;
    }

    public String getNotice_reg_date() {
        return notice_reg_date;
    }

    @PrePersist
    public void onPrePersist() {
        this.notice_reg_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
