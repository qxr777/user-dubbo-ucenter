package edu.whut.cs.jee.ucenter.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.whut.cs.jee.ucenter.domain.Organization;
import edu.whut.cs.jee.ucenter.domain.User;
import edu.whut.cs.jee.ucenter.exception.BaseException;
import edu.whut.cs.jee.ucenter.service.UCenterService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qixin
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Reference
    private UCenterService uCenterService;   //单体应用时注释掉
//    @Autowired
//    private UCenterServiceImpl uCenterService;     //单体应用时打开注释

    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<User> getAll() {
        return uCenterService.getAllUsers();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User getById(@PathVariable Long id) {
        User user = uCenterService.getUser(id);
        return user;
    }

//    @RequestMapping(path = "/{id}/users", method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public Set<User> getUsersById(@PathVariable Long id) {
//        return uCenterService.getUsersByOrganization(id);
//    }

//    @PostMapping(path = "/", //consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,//consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@ModelAttribute User user) throws BaseException {
        User newUser = uCenterService.createUser(user);
        Organization organization = uCenterService.getOrganization(user.getOrganization().getId());
        newUser.setOrganization(organization);
        return uCenterService.updateUser(newUser);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public User delete(@PathVariable Long id) {
        User user = uCenterService.getUser(id);
        return uCenterService.deleteUser(user);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public User update(@PathVariable Long id, @ModelAttribute User user) throws BaseException {
        User sourceUser = uCenterService.getUser(id);
        BeanUtils.copyProperties(user, sourceUser, "roles","password");
        return uCenterService.updateUser(sourceUser);
    }

}
