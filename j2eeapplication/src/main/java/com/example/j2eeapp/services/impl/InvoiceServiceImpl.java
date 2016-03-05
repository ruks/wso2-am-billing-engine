package com.example.j2eeapp.services.impl;

import bean.Invoice;
import com.example.j2eeapp.dao.PlanDao;
import com.example.j2eeapp.dao.ThrottleRequestDao;
import com.example.j2eeapp.domain.PlanEntity;
import com.example.j2eeapp.domain.UserEntity;
import com.example.j2eeapp.services.InvoiceService;
import com.example.j2eeapp.services.PlanService;
import org.primefaces.component.inputtext.InputText;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Service providing service methods to work with user data and entity.
 * 
 * @author Arthur Rukshan
 */
public class InvoiceServiceImpl implements InvoiceService {

	private ThrottleRequestDao throttleRequestDao;
	public String selected;

	public ThrottleRequestDao getThrottleRequestDao() {
		return throttleRequestDao;
	}

	public void setThrottleRequestDao(ThrottleRequestDao throttleRequestDao) {
		this.throttleRequestDao = throttleRequestDao;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	protected FacesMessage constructErrorMessage(String message, String detail){
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

	public Invoice createInvoice(UserEntity user) {
		return throttleRequestDao.getCount(selected,user);
	}
}
