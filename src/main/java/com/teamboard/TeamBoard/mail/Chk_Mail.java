package com.teamboard.TeamBoard.mail;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.transaction.Transactional;

@Getter
@Setter
@Entity
@Transactional
public class Chk_Mail {

    @Id
    private String address; // 입력, not null
    @NotNull
    private int key; // 입력, not null
    @ColumnDefault("0")
    private int auth; // 디폴트 값 0

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }
}
