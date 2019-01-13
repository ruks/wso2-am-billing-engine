package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericDao;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.BillingPlan;
import org.wso2.apim.billing.domain.PlanEntity;

import java.util.List;

/**
 * Data access object interface to work with User entity database operations.
 *
 * @author Arthur Rukshan
 */
public interface UsagePlanDao extends GenericDao<BillingPlan, Long> {
    List<BillingModel> loadBillingPlans(BillingPlan billingPlan);

    List<BillingModel> loadBillingPlansOfUser(String userName);
}
