package com.teamboard.TeamBoard.repository.user;

import com.teamboard.TeamBoard.user.User;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


// DAO
public class JpaUserRepository implements UserRepository {

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
    public User delete(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        User user = em.find(User.class,id); // 인자로 어떠한 엔티티를 찾을것인지, PK 총 2개의 인자를 넘겨서 select를 실행한다
        return Optional.ofNullable(user); // 널 값이 있을 수 있으니
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
