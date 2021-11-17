package com.teamboard.TeamBoard;

import com.teamboard.TeamBoard.repository.board.BoardRepository;
import com.teamboard.TeamBoard.repository.board.JpaBoardRepository;
import com.teamboard.TeamBoard.repository.user.JpaUserRepository;
import com.teamboard.TeamBoard.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Properties;

@Configuration
public class SpringConfig {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em){
        this.em = em;
    }

    @Bean
    public UserRepository userRepository(){
        return new JpaUserRepository(em);
    }

    @Bean
    public BoardRepository boardRepository(){return new JpaBoardRepository(em);}

    @Bean(name="mailSender")
    public JavaMailSender getJavaMailSender() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.starttls.required", true);
        properties.put("mail.debug", true);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("이메일 주소");
        mailSender.setPassword("비밀번호");
        mailSender.setDefaultEncoding("utf-8");
        mailSender.setJavaMailProperties(properties);
        return mailSender;

    }
}
