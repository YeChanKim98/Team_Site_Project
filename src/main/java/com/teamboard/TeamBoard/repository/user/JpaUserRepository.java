package com.teamboard.TeamBoard.repository.user;

import com.teamboard.TeamBoard.user.User;
import com.teamboard.TeamBoard.user.form.UpdateForm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


// DAO
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    private final EntityManager em;
    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public User join(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public int delete(String id) {
        return em.createQuery("delete from User u where u.id=:id") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("id",id)
                .executeUpdate();
    }

    @Override
    public int update(User user) {
        //UPDATE [테이블] SET [열] = '변경할값' WHERE [조건]
        return em.createQuery("update User u set u.id=:id, u.pw=:pw, u.name=:name,u.email =:email, u.nick=:nick, u.phone=:phone where u.id=:id") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("id",user.getId())
                .setParameter("pw","Update Pass")
                .setParameter("name",user.getName())
                .setParameter("email",user.getEmail())
                .setParameter("nick",user.getNick())
                .setParameter("phone",user.getPhone())
                .executeUpdate();
    }


    @Override
    public Optional<User> findById(String id) {
        Optional<User> user = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id",id)
                .getResultList().stream().findAny();
//        User user = em.find(User.class,id); // 인자로 어떠한 엔티티를 찾을것인지, PK 총 2개의 인자를 넘겨서 select를 실행한다
        return user;
    }


    // PK 기반이 아닌 쿼리는 JPQL을 작성하여 실행해야한다 -> 하지만 이것을 해결한 것이 Spring Data JPA이다. 쿼리문의 작성을 필요로 하지 않는다.
    // 이메일 기반 조회
    @Override
    public String findId(String name, String address){
        System.out.println("[레포지토리][진입] 이름, 이메일을 통한 아이디 조회 : "+name+" / "+address);
        return em.createQuery("select u.id from User u where u.name=:name and u.email = :address")
                .setParameter("name",name)
                .setParameter("address",address)
                .getResultList().stream().findAny().get().toString();
    }

    // PW찾기
    @Override
    public String findPw(String id, String address){
        System.out.println("[레포지토리][진입] 아이디, 이메일을 통한 PW 조회 : "+id+" / "+address);
        return em.createQuery("select u.pw from User u where u.id=:id and u.email = :address")
                .setParameter("id",id)
                .setParameter("address",address)
                .getResultList().stream().findAny().get().toString(); // 변수로 빼서 결과 있는지 확인 후 반환
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList(); // JPQL을 이용
        // 인라인 단축키 : Ctrl + Alt + N
    }
}
