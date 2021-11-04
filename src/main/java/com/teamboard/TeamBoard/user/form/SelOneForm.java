package com.teamboard.TeamBoard.user.form;

// 단일 ID찾기 값을 담을 임시 객체
// 담는 값이 하나이므로 객체 미사용. 타임리프로 매개변수 전달
public class SelOneForm {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
