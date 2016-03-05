package com.example.j2eeapp.dao;

import bean.Invoice;
import com.example.j2eeapp.commons.dao.GenericDao;
import com.example.j2eeapp.domain.PlanEntity;

import java.util.List;

/**
 * Data access object interface to work with User entity database operations.
 * 
 * @author Arthur Rukshan
 */
public interface InvoiceDao extends GenericDao<Invoice, Long> {

	/**
	 * Queries database for user name availability
	 * 
	 * @param invoiceId
	 * @return true if available
	 */
	boolean checkAvailable(String invoiceId);
	
	/**
	 * Queries user by username
	 * 
	 * @param ID
	 * @return User entity
	 */
	PlanEntity loadInvoiceByID(String ID);

	List<Invoice> loadInvoices();
}
