package edu.whut.cs.jee.ucenter.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.whut.cs.jee.ucenter.domain.Organization;
import edu.whut.cs.jee.ucenter.domain.User;
import edu.whut.cs.jee.ucenter.service.UCenterService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author qixin
 */
@RestController
@RequestMapping("/organizations")
public class OrganizationController {
    @Reference
    private UCenterService uCenterService;   //单体应用时注释掉
//    @Autowired
//    private UCenterServiceImpl uCenterService;     //单体应用时打开注释

    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<Organization> getAll() {
        return uCenterService.getAllOrganizations();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Organization getById(@PathVariable Long id) {
        Organization organization = uCenterService.getOrganization(id);
        return organization;
    }

    @RequestMapping(path = "/{id}/users", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<User> getUsersById(@PathVariable Long id) {
        return uCenterService.getUsersByOrganization(id);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Organization create(@RequestBody Organization organization) {
        return uCenterService.createOrganization(organization);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Organization delete(@PathVariable Long id) {
        Organization organization = uCenterService.getOrganization(id);
        return uCenterService.deleteOrganization(organization);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Organization update(@PathVariable Long id, @RequestBody Organization organization) {
        Organization sourceOrganization = uCenterService.getOrganization(id);
        BeanUtils.copyProperties(organization, sourceOrganization, "users");
        return uCenterService.updateOrganization(sourceOrganization);
    }

}
