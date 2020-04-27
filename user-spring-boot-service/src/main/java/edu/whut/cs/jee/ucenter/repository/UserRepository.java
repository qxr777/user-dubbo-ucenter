package edu.whut.cs.jee.ucenter.repository;

import edu.whut.cs.jee.ucenter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author qixin
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据姓名查找用户
     * @param name
     * @return
     */
    User findByName(String name);

}
