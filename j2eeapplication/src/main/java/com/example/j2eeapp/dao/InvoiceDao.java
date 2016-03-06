package com.example.j2eeapp.dao;

import com.example.j2eeapp.domain.InvoiceEntity;
import com.example.j2eeapp.commons.dao.GenericDao;
import com.example.j2eeapp.domain.UserEntity;

import java.util.List;

/**
 * Data access object interface to work with User entity database operations.
 * 
 * @author Arthur Rukshan
 */
public interface InvoiceDao extends GenericDao<InvoiceEntity, Long> {

	/**
	 * Queries database for user name availability
	 * 
	 * @param invoiceId
	 * @return true if available
	 */
	boolean checkAvailable(int invoiceId);
	
	/**
	 * Queries user by username
	 * 
	 * @param ID
	 * @return User entity
	 */
	InvoiceEntity loadInvoiceByID(UserEntity user, int ID);

	List<InvoiceEntity> loadInvoices(UserEntity user);
}
