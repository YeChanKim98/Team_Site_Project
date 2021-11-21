package com.teamboard.TeamBoard;

import com.teamboard.TeamBoard.repository.Mail.Chk_MailRepository;
import com.teamboard.TeamBoard.repository.Mail.JpaChk_MailRepository;
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

    @Bean
    public Chk_MailRepository chk_MailRepository(){return new JpaChk_MailRepository(em);}

}
