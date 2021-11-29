package com.teamboard.TeamBoard.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@PropertySource("classpath:mail.properties")
public class Config {

    Properties pt=new Properties();

    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;



    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        // 본인 구글 계정 입력 후 '보안 수준이 낮은 접근 허용' 사용으로 변경
        javaMailSender.setUsername("ssy01077@gmail.com");
        javaMailSender.setPassword("P@singW_Gg1");
        javaMailSender.setPort(port);

        pt.put("mail.smtp.socketFactory.port", socketPort); pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls); pt.put("mail.smtp.starttls.required", startlls_required);
        pt.put("mail.smtp.socketFactory.fallback",fallback);
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(pt);
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }


}
