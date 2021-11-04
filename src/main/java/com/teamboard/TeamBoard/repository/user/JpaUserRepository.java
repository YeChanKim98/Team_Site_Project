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
        // 오토 인크리먼트 재정렬 추가
        return user;
    }

    @Override
//    @Modifying
//    @Transactional
    public void delete(String id) {
        em.createQuery("delete from User u where u.id=:id") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("id",id)
                .executeUpdate();
        // 삭제 후 검증할까....?
    }

    @Override
    public int update(UpdateForm user) {
        //UPDATE [테이블] SET [열] = '변경할값' WHERE [조건]
        int updateRes = em.createQuery("update User u set u.id=:id, u.pw=:pw, u.name=:name,u.email =:email, u.nick=:nick, u.phone=:phone where u.id=:id") // 반환할 객체가 필요 없으므로 User.class는 필요 없음
                .setParameter("id",user.getId())
                .setParameter("pw","Update Pass") // 패스워드 받아와서 변수로 추가
                .setParameter("name",user.getName())
                .setParameter("email",user.getEmail()) // Email 정정시 인증기능 추가
                .setParameter("nick",user.getNick()) // Email 정정시 인증기능 추가
                .setParameter("phone",user.getPhone()) // Email 정정시 인증기능 추가
                .executeUpdate();
        if(updateRes==1){
            System.out.println("회원정보 변경 성공!");
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public Optional<User> findById(String id) {
        Optional<User> user = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id",id)
                .getResultList().stream().findAny();
//        User user = em.find(User.class,id); // 인자로 어떠한 엔티티를 찾을것인지, PK 총 2개의 인자를 넘겨서 select를 실행한다
        return user; // 널 값이 있을 수 있으니
    }


    // PK 기반이 아닌 쿼리는 JPQL을 작성하여 실행해야한다 -> 하지만 이것을 해결한 것이 Spring Data JPA이다. 쿼리문의 작성을 필요로 하지 않는다.
    @Override
    public Optional<User> findByName(String name){//, String email) {
        return em.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name",name)
                .getResultList().stream().findAny();
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList(); // JPQL을 이용
        // 인라인 단축키 : Ctrl + Alt + N
    }
}
