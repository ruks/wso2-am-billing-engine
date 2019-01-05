package org.wso2.apim.billing.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.codec.binary.StringUtils;
import org.primefaces.component.inputtext.InputText;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.wso2.apim.billing.dao.UserDao;
import org.wso2.apim.billing.domain.UserEntity;
import org.wso2.apim.billing.services.UserService;

/**
 * Service providing service methods to work with user data and entity.
 *
 * @author Arthur Vin
 */
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDao userDao;
    private String userRole;

    /**
     * Create user - persist to database
     *
     * @param userEntity
     * @return true if success
     */
    public boolean createUser(UserEntity userEntity) {
        List<String> roles = new ArrayList<String>();
        if (userRole != null && !userRole.isEmpty()) {
            userRole = userRole.trim();
            String[] roleArr = userRole.split(",");
            for (String aRole : roleArr) {
                roles.add(aRole.trim());
            }
        }
        userEntity.setRoles(String.join(",", roles));
        if (!userDao.checkAvailable(userEntity.getUserName())) {
            FacesMessage message = constructErrorMessage(
                    String.format(getMessageBundle().getString("userExistsMsg"), userEntity.getUserName()), null);
            getFacesContext().addMessage(null, message);

            return false;
        }

        try {
            userDao.save(userEntity);
        } catch (Exception e) {
            FacesMessage message = constructFatalMessage(e.getMessage(), null);
            getFacesContext().addMessage(null, message);

            return false;
        }

        return true;
    }

    /**
     * Check user name availability. UI ajax use.
     *
     * @param ajax event
     * @return
     */
    public boolean checkAvailable(AjaxBehaviorEvent event) {

        InputText inputText = (InputText) event.getSource();
        String value = (String) inputText.getValue();

        boolean available = userDao.checkAvailable(value);

        if (!available) {
            FacesMessage message =
                    constructErrorMessage(null, String.format(getMessageBundle().getString("userExistsMsg"), value));
            getFacesContext().addMessage(event.getComponent().getClientId(), message);
        } else {
            FacesMessage message =
                    constructInfoMessage(null, String.format(getMessageBundle().getString("userAvailableMsg"), value));
            getFacesContext().addMessage(event.getComponent().getClientId(), message);
        }

        return available;
    }

    /**
     * Construct UserDetails instance required by spring security
     */
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserEntity user = userDao.loadUserByUserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format(getMessageBundle().getString("badCredentials"), userName));
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        String userRoles = user.getRoles();
        if (userRoles != null && !userRoles.isEmpty()) {
            userRoles = userRoles.trim();
            String[] roleArr = userRoles.split(",");
            for (String aRole : roleArr) {
                authorities.add(new SimpleGrantedAuthority(aRole));
            }
        }

        User userDetails = new User(user.getUserName(), user.getPassword(), authorities);

        return userDetails;
    }

    /**
     * Retrieves full User record from database by user name
     *
     * @param userName
     * @return UserEntity
     */
    public UserEntity loadUserEntityByUsername(String userName) {
        return userDao.loadUserByUserName(userName);
    }

    protected FacesMessage constructErrorMessage(String message, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail);
    }

    protected FacesMessage constructInfoMessage(String message, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, message, detail);
    }

    protected FacesMessage constructFatalMessage(String message, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_FATAL, message, detail);
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected ResourceBundle getMessageBundle() {
        return ResourceBundle.getBundle("message-labels");
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
