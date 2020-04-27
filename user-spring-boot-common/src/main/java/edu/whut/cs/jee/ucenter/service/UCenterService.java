package edu.whut.cs.jee.ucenter.service;

import edu.whut.cs.jee.ucenter.domain.Organization;
import edu.whut.cs.jee.ucenter.domain.Role;
import edu.whut.cs.jee.ucenter.domain.User;
import edu.whut.cs.jee.ucenter.exception.BaseException;
import edu.whut.cs.jee.ucenter.exception.NoUserException;
import edu.whut.cs.jee.ucenter.exception.OverflowException;
import edu.whut.cs.jee.ucenter.exception.PasswordErrorException;

import java.util.List;
import java.util.Set;

/**
 * @author qixin
 */
public interface UCenterService {

    Organization enroll(Organization organization, User user) throws OverflowException;

    Organization createOrganization(Organization organization);

    Organization updateOrganization(Organization organization);

    Organization deleteOrganization(Organization organization);

    User createUser(User user) throws BaseException;

    User updateUser(User user) throws BaseException;

    User deleteUser(User user);

    User getUser(long id);

    List<User> getAllUsers();

    Set<User> getUsersByOrganization(long organizationId);

    Organization getOrganization(long id);

    List<Organization> getAllOrganizations();

    List<Role> getAllRoles();

    Role createRole(Role role);

    Role updateRole(Role role);

    Role deleteRole(Role role);

    boolean valid(String userName, String password) throws NoUserException, PasswordErrorException;

    Role getRole(long id);
}
