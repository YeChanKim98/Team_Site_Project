package com.teamboard.TeamBoard.repository.Mail;

import com.teamboard.TeamBoard.mail.Chk_Mail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaChk_MailRepository implements Chk_MailRepository{

    @PersistenceContext
    private final EntityManager em;
    public JpaChk_MailRepository(EntityManager em){this.em = em;}


    @Override
    public int checkMail(String address, int key) {

        return em.createQuery("update Chk_Mail cm set cm.auth=1 where cm.address=:address and cm.key=:key") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("address", address)
                .setParameter("key", key)
                .executeUpdate();
    }

    @Override
    public int regist(Chk_Mail mail){
        System.out.println("Mail.address : "+mail.getAddress());
        System.out.println("Mail.key : "+mail.getKey());
        em.persist(mail);
        return 1;
    }
}
