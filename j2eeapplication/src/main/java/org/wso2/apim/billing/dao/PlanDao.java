package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericDao;
import org.wso2.apim.billing.domain.PlanEntity;

import java.util.List;

/**
 * Data access object interface to work with User entity database operations.
 *
 * @author Arthur Rukshan
 */
public interface PlanDao extends GenericDao<PlanEntity, Long> {

    /**
     * Queries database for user name availability
     *
     * @param planName
     * @return true if available
     */
    boolean checkAvailable(String planName);

    /**
     * Queries user by username
     *
     * @param planName
     * @return User entity
     */
    PlanEntity loadPlanByPlanName(String planName);

    List<PlanEntity> loadPlans();
}
