package com.teamboard.TeamBoard.board;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Transactional
public class free_Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fboard_num; // 자동
    private String fboard_writer; // 입력
    private String fboard_title; // 입력
    private String fboard_content; // 입력
    private int fboard_view_count; // 자동
    private int fboard_comment_counter; // 자동
    private LocalDateTime fboard_reg_date; // 자동
    // 수정일 추가


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

    public int getFboard_comment_counter() {
        return fboard_comment_counter;
    }

    public void setFboard_comment_counter(int fboard_comment_counter) {
        this.fboard_comment_counter = fboard_comment_counter;
    }

    public LocalDateTime getFboard_reg_date() {
        return fboard_reg_date;
    }

    @PrePersist
    public void createDate(){
        this.fboard_reg_date = LocalDateTime.now();
    }
}