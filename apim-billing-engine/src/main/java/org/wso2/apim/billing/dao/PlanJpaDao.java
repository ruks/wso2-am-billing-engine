package org.wso2.apim.billing.dao;

import org.springframework.util.Assert;
import org.wso2.apim.billing.commons.dao.GenericJpaDao;
import org.wso2.apim.billing.domain.PlanEntity;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Data access object JPA impl to work with User entity database operations.
 *
 * @author Arthur Rukshan
 */
public class PlanJpaDao extends GenericJpaDao<PlanEntity, Long> implements PlanDao {

    public PlanJpaDao() {
        super(PlanEntity.class);
    }

    /**
     * Queries database for user name availability
     *
     * @param planName
     * @return true if available
     */
    public boolean checkAvailable(String planName) {
        Assert.notNull(planName);

        Query query = getEntityManager().createQuery(
                "select count(*) from " + getPersistentClass().getSimpleName() + " u where u.planName = :planName")
                .setParameter("planName", planName);

        Long count = (Long) query.getSingleResult();

        return count < 1;
    }

    /**
     * Queries user by username
     *
     * @param planName
     * @return User entity
     */
    public PlanEntity loadPlanByPlanName(String planName) {
        Assert.notNull(planName);

        PlanEntity plan = null;

        Query query = getEntityManager().createQuery(
                "select u from " + getPersistentClass().getSimpleName() + " u where u.planName = :planName")
                .setParameter("planName", planName);

        try {
            plan = (PlanEntity) query.getSingleResult();
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
    public List<PlanEntity> loadPlans() {

        List<PlanEntity> plans = null;

        Query query = getEntityManager().createQuery("select u from " + getPersistentClass().getSimpleName() + " u");
        //				+ " u where u.planName = :planName").setParameter("planName", planName);

        try {
            plans = query.getResultList();
        } catch (NoResultException e) {
            //do nothing
        }
        return plans;
    }
}
