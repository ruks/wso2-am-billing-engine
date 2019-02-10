package org.wso2.apim.billing.services.impl;

import com.google.gson.Gson;
import org.wso2.apim.billing.dao.SubscriptionDao;
import org.wso2.apim.billing.dao.UserDao;
import org.wso2.apim.billing.dao.UserJpaDao;
import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.dao.InvoiceDao;
import org.wso2.apim.billing.domain.UserEntity;
import org.wso2.apim.billing.services.InvoiceService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Service providing service methods to work with user data and entity.
 *
 * @author Arthur Rukshan
 */
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceGenerator invoiceGenerator;
    private InvoiceDao invoiceDao;
    private SubscriptionDao subscriptionDao;
    private String selectedSubscriber;
    private int selectedMonth;
    private UserDao userDao;

    public InvoiceGenerator getInvoiceGenerator() {
        return invoiceGenerator;
    }

    public void setInvoiceGenerator(InvoiceGenerator invoiceGenerator) {
        this.invoiceGenerator = invoiceGenerator;
    }

    public InvoiceDao getInvoiceDao() {
        return invoiceDao;
    }

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public String getSelectedSubscriber() {
        return selectedSubscriber;
    }

    public void setSelectedSubscriber(String selectedSubscriber) {
        this.selectedSubscriber = selectedSubscriber;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
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

    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public InvoiceEntity createInvoice() throws Exception{
        UserEntity user = userDao.loadUserByUserName(selectedSubscriber);
        InvoiceEntity result = invoiceGenerator.process(user, selectedMonth);
        if (result != null) {
            InvoiceEntity entity = invoiceDao.save(result);
            return entity;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when generate invoice", "Sorry!"));
        }
        throw new Exception("Error occurred when generate invoice");
    }

    public List<InvoiceEntity> listInvoices(UserEntity user) {
        return invoiceDao.loadInvoices(user);
    }

    public InvoiceEntity getInvoiceById(UserEntity user, long id) {
        InvoiceEntity jsonEntity = invoiceDao.loadInvoiceByID(user, id);
        InvoiceEntity invoiceEntity = new Gson().fromJson(jsonEntity.getInvoiceJson(), InvoiceEntity.class);
        invoiceEntity.setId(jsonEntity.getId());
        return invoiceEntity;
    }

    public List<String> getSubscribers() {
        return subscriptionDao.getSubscribers();
    }
}
