package edu.whut.cs.jee.ucenter.ucenter;

import edu.whut.cs.jee.ucenter.domain.Organization;
import edu.whut.cs.jee.ucenter.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationDao;

    static long organizationId = 1;
    Organization organization = null;

    @Test
    public void test2Insert() {
        organization = new Organization();
        organization.setName("计算机1803班");
        organization.setDescription("计算机18级实验班");
        organization = organizationDao.save(organization);
        organizationId = organization.getId();
        Assert.isTrue(organizationId > 0);
    }

    @Test
    public void testFindByNameLike() {
        String name = "%计算机1203%";
        organization = organizationDao.findByNameLike(name);
        Assert.notNull(organization);
    }

    @Test
    public void testFindWithUserById() {
        Organization organization = organizationDao.findWithUserById(1L);
        Assert.isTrue(organization.getUsers().size() > 0, "message");
    }

    @Test
    public void testFindAll() {
        List<Organization> organizations = organizationDao.findAll();
        Assert.notEmpty(organizations, "size > 0");
    }
}
