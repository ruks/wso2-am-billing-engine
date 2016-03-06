package com.example.j2eeapp.services;

import com.example.j2eeapp.domain.InvoiceEntity;
import com.example.j2eeapp.domain.UserEntity;

import java.util.List;

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
	InvoiceEntity createInvoice(UserEntity user);

	List<InvoiceEntity> listInvoices(UserEntity user);

	InvoiceEntity getInvoiceById(UserEntity user, int id);
}
