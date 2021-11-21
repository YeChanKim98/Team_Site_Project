package com.teamboard.TeamBoard.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    private final Chk_MailService chk_MailService;
    @Autowired
    public MailController(Chk_MailService chk_MailService, EmailService emailService){
        this.chk_MailService = chk_MailService;
        this.emailService = emailService;
    }


    @PostMapping("/mail")
    public String send(@RequestParam String address, @RequestParam String title, @RequestParam String content, HttpServletRequest req){
        System.out.println("컨트롤러 진입"+address+title+content);
        emailService.sendSimpleMessage(address, title, content);
        System.out.println("발송완료");
        return "home";
    }

    @GetMapping("/chk_mail/{address}")
    //              /chk_mail/입력값
    public String send_chk(@PathVariable String address, HttpServletRequest req){
        System.out.println("인증메일 발송 컨트롤러 진입"+address);

        double randomValue = Math.random();
        int key = (int)(randomValue * 9999) + 1000; // 키 생성 및 초기화 -> 4자리 키 생성
        System.out.println("인증을 위한 생성 -> : "+key);

        Chk_Mail mail = new Chk_Mail();
        mail.setAddress(address);
        mail.setKey(key);
        int res = chk_MailService.regist(mail);
        System.out.println("인증 메일 등록 결과 : "+res);

        String content = "<a href='/check/mail/"+address+"/"+key+"'>인증을 위해 해당 링크를 눌러주세요</a>";
        emailService.sendSimpleMessage(address,"가입인증메일입니다",content);
        System.out.println("인증 메일 발송완료");
        return "home";
    }

    @GetMapping("/check/mail/{address}/{key}")
    public String check(@PathVariable String address, @PathVariable int key){
        System.out.println("인증과정 컨트롤러 진입"+address+"\t"+key);

        int res = chk_MailService.checkMail(address, key);
        if(res==1){
            System.out.println("메일 인증 완료");
            return "home";
        }
        else{
            System.out.println("메일 인증 실패");
            return "home";
        }
    }
}
