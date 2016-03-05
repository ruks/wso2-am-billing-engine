package com.example.j2eeapp.services;

import bean.Invoice;
import com.example.j2eeapp.domain.PlanEntity;
import com.example.j2eeapp.domain.UserEntity;

import javax.faces.event.AjaxBehaviorEvent;
import java.util.Map;

/**
 * Service providing service methods to work with user data and entity.
 * 
 * @author Arthur rukshan
 */
public interface InvoiceService {

	/**
	 * Create plan - persist to database
	 * 
	 * @param user
	 * @return true if success
	 */
	Invoice createInvoice(UserEntity user);
}
