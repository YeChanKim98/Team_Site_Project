package com.teamboard.TeamBoard.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Transactional
public class free_Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fboard_num; // 자동, PK
    private String fboard_writer; // 입력, not null
    private String fboard_title; // 입력, not null
    private String fboard_content=null; // 입력, not null
    private int fboard_view_count; // 자동, default 0
    private int fboard_comment_count; // 자동, default 0
    private String fboard_reg_date;//=null; // 자동


    public int getFboard_num() {
        return fboard_num;
    }

    public String getFboard_writer() {
        return fboard_writer;
    }

    public void setFboard_writer(String fboard_writer) {
        this.fboard_writer = fboard_writer;
    }

    public String getFboard_title() {
        return fboard_title;
    }

    public void setFboard_title(String fboard_title) {
        this.fboard_title = fboard_title;
    }

    public String getFboard_content() {
        return fboard_content;
    }

    public void setFboard_content(String fboard_content) {
        this.fboard_content = fboard_content;
    }

    public int getFboard_view_count() {
        return fboard_view_count;
    }

    public void setFboard_view_count(int fboard_view_count) {
        this.fboard_view_count = fboard_view_count;
    }

    public int getFboard_comment_count() {
        return fboard_comment_count;
    }

    public void setFboard_comment_count(int fboard_comment_counter) {
        this.fboard_comment_count = fboard_comment_counter;
    }

    public String getFboard_reg_date() {
        return fboard_reg_date;
    }

    @PrePersist
    public void onPrePersist() {
        this.fboard_reg_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}