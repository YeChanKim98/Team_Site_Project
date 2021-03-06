package com.teamboard.TeamBoard.user;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Transactional
public class User {
// Column어노테이션 생략 : DB와 객체내부 변수이름 통일
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid; // 자동, PK
    @NotNull
    private String id; // 입력, not null
    @NotNull
    private String pw; //입력, not null
    @NotNull
    private String name; // 입력, not null
    @NotNull
    private String email; // 입력, not null
    @NotNull
    private String nick; // 입력, not null
    private String phone; // 입력
    private LocalDateTime date; // 게터 세터 없음

    
    // 게터세터는 필요여부 확인 후 삭제
    public int getPid() {
        return pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getDate() { return date; }

    // 시간 자동 생성
    @PrePersist
    public void createDate(){
        this.date = LocalDateTime.now();
    }
}
