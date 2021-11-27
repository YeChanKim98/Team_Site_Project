package com.teamboard.TeamBoard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    // 로그인 후 세션아이디가 검색창에 노출되는 경우가 있음 : 가려야함
    public String home(){
        return "home";
    }

}