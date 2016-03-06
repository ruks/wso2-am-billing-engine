package org.wso2.apim.billing.services.impl;

import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.dao.InvoiceDao;
import org.wso2.apim.billing.dao.ThrottleRequestDao;
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

    private ThrottleRequestDao throttleRequestDao;
    private InvoiceDao invoiceDao;
    public String selected;

    public ThrottleRequestDao getThrottleRequestDao() {
        return throttleRequestDao;
    }

    public void setThrottleRequestDao(ThrottleRequestDao throttleRequestDao) {
        this.throttleRequestDao = throttleRequestDao;
    }

    public InvoiceDao getInvoiceDao() {
        return invoiceDao;
    }

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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

    public InvoiceEntity createInvoice(UserEntity user) {
        InvoiceEntity result = throttleRequestDao.GenerateInvoice(selected, user);
        invoiceDao.save(result);
        return result;
    }

    public List<InvoiceEntity> listInvoices(UserEntity user) {
        return invoiceDao.loadInvoices(user);
    }

    public InvoiceEntity getInvoiceById(UserEntity user, int id) {
        return invoiceDao.loadInvoiceByID(user, id);
    }
}
