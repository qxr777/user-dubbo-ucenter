package edu.whut.cs.jee.ucenter.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import edu.whut.cs.jee.ucenter.domain.Organization;
import edu.whut.cs.jee.ucenter.domain.Role;
import edu.whut.cs.jee.ucenter.domain.User;
import edu.whut.cs.jee.ucenter.exception.*;
import edu.whut.cs.jee.ucenter.repository.OrganizationRepository;
import edu.whut.cs.jee.ucenter.repository.RoleRepository;
import edu.whut.cs.jee.ucenter.repository.UserRepository;
import edu.whut.cs.jee.ucenter.service.UCenterService;
import edu.whut.cs.jee.ucenter.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author qixin
 */
@org.springframework.stereotype.Service
@Service(timeout = 10000,interfaceClass = UCenterService.class)
@CacheConfig(cacheNames = "uCenterService")
public class UCenterServiceImpl implements UCenterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UCenterServiceImpl.class);

    @Autowired
    private OrganizationRepository organizationDao;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private RoleRepository roleDao;

    @Override
    public Organization enroll(Organization organization, User user) throws OverflowException {

        organization = organizationDao.findById(organization.getId()).get();
        user = userDao.findById(user.getId()).get();
        if(organization.getUsers().size() >= Organization.MAX_NUMBER) {
            throw new OverflowException(user.getName());
        } else{
			user.setOrganization(organization);
			userDao.save(user);
        }
        return organization;
    }

    @Override
    public Organization createOrganization(Organization organization){
        return organizationDao.save(organization);
    }

    @Override
    public Organization updateOrganization(Organization organization){
        return organizationDao.save(organization);
    }

    @Override
    public Organization deleteOrganization(Organization organization){
        organizationDao.delete(organization);
        return organization;
    }

    @Override
    public User createUser(User user) throws BaseException {
        if (!user.getPassword().equals(user.getPassword2())) {
            throw new PasswordMatchException();
        }
        if (userDao.findByName(user.getName()) != null) {
            throw new HaveExistException(user.getName());
        }
        preparedCheck(user);
        MD5 md5 = new MD5();
        user.setPassword(md5.getMD5ofStr(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public User updateUser(User user) throws BaseException {
//		preparedCheck(user);
        return userDao.save(user);
    }

    private void preparedCheck(User user) throws BaseException {
        long organizationId = user.getOrganization().getId();
        Organization organization = organizationDao.findWithUserById(organizationId);
        if (organization.getUsers().size() >= Organization.MAX_NUMBER) {
            throw new OverflowException(organization.getName());
        }
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role != null) {
                    role = roleDao.findById(role.getId()).get();
                    if (role.getUsers().size() >= Role.MAX_NUMBER) {
                        throw new OverflowException(role.getName());
                    }
                }
            }
        }
    }

    @Override
    public User deleteUser(User user) {
        userDao.delete(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long id){
        return userDao.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<User> getUsersByOrganization(long organizationId) {
        Organization organization = organizationDao.findWithUserById(organizationId);
        return organization.getUsers();
    }

    @Override
    @Cacheable(value="organization", key ="#p0")
    @Transactional(readOnly = true)
    public Organization getOrganization(long id){
        return organizationDao.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> getAllOrganizations(){
        return organizationDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles(){
        return roleDao.findAll();
    }

    @Override
    public Role createRole(Role role){
        return roleDao.save(role);
    }

    @Override
    public Role updateRole(Role role){
        return roleDao.save(role);
    }

    @Override
    public Role deleteRole(Role role){
        roleDao.delete(role);
        return role;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean valid(String userName, String password) throws NoUserException, PasswordErrorException {
        User userInDB = userDao.findByName(userName);
        if (userInDB == null) {
            throw new NoUserException();
        }
        MD5 md5 = new MD5();
        if (userInDB != null
                && !userInDB.getPassword()
                .equals(md5.getMD5ofStr(password))) {
            throw new PasswordErrorException();
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRole(long id) {
        return roleDao.findById(id).get();
    }
}
