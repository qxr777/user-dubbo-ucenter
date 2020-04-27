package edu.whut.cs.jee.ucenter.repository;

import edu.whut.cs.jee.ucenter.domain.Organization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author qixin
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    /**
     * 按名字进行模糊查询
     * @param name
     * @return
     */
    Organization findByNameLike(String name);

    @EntityGraph(attributePaths = { "users" })
    Organization findWithUserById(Long id);

}
