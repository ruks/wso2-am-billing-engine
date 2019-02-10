package org.wso2.apim.billing.dao;

import org.springframework.util.Assert;
import org.wso2.apim.billing.commons.dao.GenericJpaDao;
import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.domain.UserEntity;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Data access object JPA impl to work with User entity database operations.
 *
 * @author Arthur Rukshan
 */
public class InvoiceJpaDao extends GenericJpaDao<InvoiceEntity, Long> implements InvoiceDao {

    public InvoiceJpaDao() {
        super(InvoiceEntity.class);
    }

    /**
     * Queries database for user name availability
     *
     * @param invoiceId
     * @return true if available
     */
    public boolean checkAvailable(int invoiceId) {
        Assert.notNull(invoiceId);

        Query query = getEntityManager().createQuery(
                "select count(*) from " + getPersistentClass().getSimpleName() + " u where u.invoiceNo = :invoiceNo")
                .setParameter("invoiceNo", invoiceId);

        Long count = (Long) query.getSingleResult();

        return count < 1;
    }

    /**
     * Queries user by username
     *
     * @param ID
     * @return User entity
     */
    public InvoiceEntity loadInvoiceByID(UserEntity user, long ID) {
        Assert.notNull(ID);

        InvoiceEntity plan = null;

        Query query = getEntityManager().createQuery(
                "select u from " + getPersistentClass().getSimpleName() + " u where u.id = :id")
                .setParameter("id", ID);

        try {
            plan = (InvoiceEntity) query.getSingleResult();
        } catch (NoResultException e) {
            //do nothing
        }

        return plan;
    }

    /**
     * Queries user by username
     *
     * @return User entity
     */
    public List<InvoiceEntity> loadInvoices(UserEntity user) {

        List<InvoiceEntity> plans = null;
        String sql = "select u from " + getPersistentClass().getSimpleName() + " u where u.userID = :userID";
        Query query = getEntityManager().createQuery(sql).setParameter("userID", user.getUserName());
        try {
            plans = query.getResultList();
        } catch (NoResultException e) {
            //do nothing
        }
        System.out.println("size " + plans.size());
        return plans;
    }
}
