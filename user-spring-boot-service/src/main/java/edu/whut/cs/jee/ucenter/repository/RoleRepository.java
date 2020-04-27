package edu.whut.cs.jee.ucenter.repository;

import edu.whut.cs.jee.ucenter.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author qixin
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
