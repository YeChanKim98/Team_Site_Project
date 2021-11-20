package com.teamboard.TeamBoard.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @PostMapping("/mail")
    public String home3(@RequestParam String address, @RequestParam String title, @RequestParam String content, HttpServletRequest req){
        System.out.println("컨트롤러 진입"+address+title+content);
        emailService.sendSimpleMessage(address, title, content);
        System.out.println("발송완료");
        return "home";
    }
}
