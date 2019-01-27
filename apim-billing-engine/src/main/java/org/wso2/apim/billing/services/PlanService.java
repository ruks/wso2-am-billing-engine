package org.wso2.apim.billing.services;

import javax.faces.event.AjaxBehaviorEvent;

import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.BillingPlan;
import org.wso2.apim.billing.domain.PlanEntity;

import java.util.List;
import java.util.Map;

/**
 * Service providing service methods to work with user data and entity.
 *
 * @author Arthur Vin
 */
public interface PlanService {

    /**
     * Check plan name availability. UI ajax use.
     *
     * @param event
     * @return
     */
    boolean checkAvailable(AjaxBehaviorEvent event);

    /**
     * Retrieves full User record from database by plan name
     *
     * @param planName
     * @return PlanEntity
     */
    PlanEntity loadPlanEntityByPlanName(String planName);

    Map<String, Object> loadPlanEntities();

    Map<String, Object> loadPlanEntitiesString();

    List<BillingAttribute> listAttributes(BillingPlan billingPlan);

    boolean createBillingPlan(BillingPlan billingPlan);

    List<BillingModel> listBillingPlan(String user, BillingPlan billingPlan);

    void subscribe(String user, String id, BillingPlan billingPlan);
    void unSubscribe(String user, String id);
}
