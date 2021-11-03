package com.teamboard.TeamBoard;

import com.teamboard.TeamBoard.repository.user.JpaUserRepository;
import com.teamboard.TeamBoard.repository.user.UserRepository;
import com.teamboard.TeamBoard.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
