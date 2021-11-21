package com.teamboard.TeamBoard.repository.Mail;

import com.teamboard.TeamBoard.mail.Chk_Mail;

public interface Chk_MailRepository {

    int checkMail(String address, int key);
    int regist(Chk_Mail mail);

}
