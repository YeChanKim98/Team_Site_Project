package com.teamboard.TeamBoard.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;



    public void sendSimpleMessage(String to, String subject, String text) {
//        SimpleMailMessage message = createMessage(to, subject, text);
        try{//예외처리
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setText(text,"UTF-8","html");
            mailSender.send(message);
        }catch(MailException | MessagingException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    // HTML없는 일반 메일
//    private SimpleMailMessage createMessage(String to, String subject, String text){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);//보낼 대상
//        message.setSubject(subject);//제목
//        message.setText(text);//내용
//        return message;
//    }

}