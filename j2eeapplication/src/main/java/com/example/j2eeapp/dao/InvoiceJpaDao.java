package com.example.j2eeapp.dao;

import bean.Invoice;
import com.example.j2eeapp.commons.dao.GenericJpaDao;
import com.example.j2eeapp.domain.PlanEntity;
import org.springframework.util.Assert;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Data access object JPA impl to work with User entity database operations.
 * 
 * @author Arthur Rukshan
 */
public class InvoiceJpaDao extends GenericJpaDao<Invoice, Long> implements InvoiceDao {

	public InvoiceJpaDao() {
		super(Invoice.class);
	}

	/**
	 * Queries database for user name availability
	 * 
	 * @param invoiceId
	 * @return true if available
	 */
	public boolean checkAvailable(String invoiceId) {
		Assert.notNull(invoiceId);
		
		Query query = getEntityManager()
			.createQuery("select count(*) from " + getPersistentClass().getSimpleName() 
					+ " u where u.invoiceNo = :invoiceNo").setParameter("invoiceNo", invoiceId);
		
		Long count = (Long) query.getSingleResult();
		
		return count < 1;
	}

	/**
	 * Queries user by username
	 * 
	 * @param ID
	 * @return User entity
	 */
	public PlanEntity loadInvoiceByID(String ID) {
		Assert.notNull(ID);

		PlanEntity plan = null;
		
		Query query = getEntityManager().createQuery("select u from " + getPersistentClass().getSimpleName()
				+ " u where u.invoiceNo = :invoiceNo").setParameter("invoiceNo", ID);
		
		try {
			plan = (PlanEntity) query.getSingleResult();
		} catch(NoResultException e) {
			//do nothing
		}
		
		return plan;
	}

	/**
	 * Queries user by username
	 *
	 * @return User entity
	 */
	public List<Invoice> loadInvoices() {

		List<Invoice> plans = null;

		Query query = getEntityManager().createQuery("select u from " + getPersistentClass().getSimpleName()
				+ " u");

		try {
			plans =  query.getResultList();
		} catch(NoResultException e) {
			//do nothing
		}
		return plans;
	}
}
