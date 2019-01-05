package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericJpaDao;
import org.wso2.apim.billing.domain.BillingPlan;

/**
 * Data access object JPA impl to work with User entity database operations.
 *
 * @author Arthur Rukshan
 */
public class UsagePlanJpaDao extends GenericJpaDao<BillingPlan, Long> implements UsagePlanDao {

    public UsagePlanJpaDao() {
        super(BillingPlan.class);
    }
}
