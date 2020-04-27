package edu.whut.cs.jee.ucenter.ucenter;

import edu.whut.cs.jee.ucenter.domain.Organization;
import edu.whut.cs.jee.ucenter.domain.User;
import edu.whut.cs.jee.ucenter.exception.NoUserException;
import edu.whut.cs.jee.ucenter.exception.OverflowException;
import edu.whut.cs.jee.ucenter.exception.PasswordErrorException;
import edu.whut.cs.jee.ucenter.repository.OrganizationRepository;
import edu.whut.cs.jee.ucenter.service.impl.UCenterServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Set;

@SpringBootTest
class UserSpringBootServiceApplicationTests {

    @Autowired
    private UCenterServiceImpl userService;
    @Autowired
    static OrganizationRepository organizationDao;

    @Test
    void contextLoads() {
    }

    @Test
    public void testEnroll() {
        User user = new User();
        user.setId(1);
        Organization organization = new Organization();
        organization.setId(5);
        try {
            organization = userService.enroll(organization, user);
        } catch (OverflowException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.isNull(user.getOrganization());
    }

    @Test
    public void testValid() throws NoUserException, PasswordErrorException {
        String userName = "admin";
        String password = "123456";
        Assert.isTrue(userService.valid(userName, password));
    }

    @Test
    public void testGetUserByOrganization() {
        long organizationId = 1L;
        Set<User> users =  userService.getUsersByOrganization(organizationId);
        Assert.isTrue(users.size() > 0, "message");
    }


}
