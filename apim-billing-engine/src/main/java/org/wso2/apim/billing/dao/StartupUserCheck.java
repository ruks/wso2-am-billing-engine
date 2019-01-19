package org.wso2.apim.billing.dao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.wso2.apim.billing.domain.UserEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class StartupUserCheck {
    private UserDao userDao;
    private String adminUser;
    private String adminPass;
    private String adminRole;

    @PostConstruct
    public void checkOrUpdate() {
        if (!userDao.checkAvailable(adminUser)) {
            System.out.println("Admin User already exist");
            return;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(adminUser);
        userEntity.setPassword(adminPass);
        userEntity.setFirstName("Admin");

        List<String> roles = new ArrayList<String>();
        if (adminRole != null && !adminRole.isEmpty()) {
            adminRole = adminRole.trim();
            String[] roleArr = adminRole.split(",");
            for (String aRole : roleArr) {
                roles.add(aRole.trim());
            }
        }
        userEntity.setRoles(String.join(",", roles));

        try {
            userDao.save(userEntity);
            System.out.println("Admin User added successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }
}
