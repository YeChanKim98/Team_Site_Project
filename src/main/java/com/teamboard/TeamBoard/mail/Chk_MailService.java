package com.teamboard.TeamBoard.mail;

import com.teamboard.TeamBoard.repository.Mail.Chk_MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class Chk_MailService {

    private final Chk_MailRepository chk_mailRepository;

    @Autowired
    public Chk_MailService(Chk_MailRepository chk_mailRepository){
        this.chk_mailRepository = chk_mailRepository;
    }

    public int checkMail(String address, int key){
        return chk_mailRepository.checkMail(address, key);
    }

    public int regist(Chk_Mail mail){
        chk_mailRepository.regist(mail);
        return 1;
    }
}
