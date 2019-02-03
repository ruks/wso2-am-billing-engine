package org.wso2.apim.billing.services.impl;

import org.primefaces.component.inputtext.InputText;
import org.wso2.apim.billing.dao.PlanDao;
import org.wso2.apim.billing.dao.SubscriptionDao;
import org.wso2.apim.billing.dao.UsagePlanDao;
import org.wso2.apim.billing.domain.*;
import org.wso2.apim.billing.services.PlanService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.*;

/**
 * Service providing service methods to work with user data and entity.
 *
 * @author Arthur Rukshan
 */
public class PlanServiceImpl implements PlanService {

    private PlanDao planDao;
    private UsagePlanDao usagePlanDao;
    private List<BillingAttribute> attributes;
    private SubscriptionDao subscriptionDao;

    public List<BillingAttribute> listAttributes(BillingPlan billingPlan) {
        attributes = new ArrayList<>();
        if (billingPlan == null || billingPlan.getCurrentBillingModel() == null) {
            return attributes;
        }
        int i = 0;
        if ("quota".equalsIgnoreCase(billingPlan.getCurrentBillingModel().getPackageType())) {
            attributes.add(new BillingAttribute(i++, "subscription", "Subscription Fee", null));
            attributes.add(new BillingAttribute(i++, "quota", "Allowed Quota", null));
            attributes.add(new BillingAttribute(i++, "quotaPrice", "Package Price", null));
            attributes.add(new BillingAttribute(i++, "excessivePrice", "Price per additional Request", null));
        } else if ("metered".equalsIgnoreCase(billingPlan.getCurrentBillingModel().getPackageType())) {
            attributes.add(new BillingAttribute(i++, "subscription", "Subscription Fee", null));
            attributes.add(new BillingAttribute(i++, "pricePerReq", "Price per a Request", null));
        }
        System.out.println("listAttributes :" + billingPlan.getCurrentBillingModel().getPackageType());
        billingPlan.getCurrentBillingModel().setAttributes(attributes);
        return attributes;
    }

    public List<BillingAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<BillingAttribute> attributes) {
        this.attributes = attributes;
    }

    public boolean createBillingPlan(BillingPlan billingPlan) {
        /*if (!planDao.checkAvailable(billingPlan.)) {
            FacesMessage message = constructErrorMessage(
                    String.format(getMessageBundle().getString("userExistsMsg"), planEntity.getPlanName()), null);
            getFacesContext().addMessage(null, message);

            return false;
        }*/

        try {
            usagePlanDao.save(billingPlan);
        } catch (Exception e) {
            FacesMessage message = constructFatalMessage(e.getMessage(), "Error occurred persisting plans");
            getFacesContext().addMessage(null, message);
            return false;
        }
        return true;
    }

    public boolean checkAvailable(AjaxBehaviorEvent event) {
        InputText inputText = (InputText) event.getSource();
        String value = (String) inputText.getValue();

        boolean available = planDao.checkAvailable(value);

        if (!available) {
            FacesMessage message =
                    constructErrorMessage(null, String.format(getMessageBundle().getString("userExistsMsg"), value));
            getFacesContext().addMessage(event.getComponent().getClientId(), message);
        } else {
            FacesMessage message =
                    constructInfoMessage(null, String.format(getMessageBundle().getString("userAvailableMsg"), value));
            getFacesContext().addMessage(event.getComponent().getClientId(), message);
        }

        return available;
    }

    public PlanEntity loadPlanEntityByPlanName(String planName) {
        return planDao.loadPlanByPlanName(planName);
    }

    public Map<String, Object> loadPlanEntities() {
        LinkedHashMap<String, Object> plans = new LinkedHashMap<String, Object>();
        plans.put("Select Plan", "Select Plan");
        for (PlanEntity plan : planDao.loadPlans()) {
            //            plans.put(plan.getPlanName(), plan.getPlanName());
            plans.put(plan.getPlanName(), plan);
        }
        return plans;
    }

    public Map<String, Object> loadPlanEntitiesString() {
        LinkedHashMap<String, Object> plans = new LinkedHashMap<String, Object>();
        plans.put("Select Plan", "Select Plan");
        for (PlanEntity plan : planDao.loadPlans()) {
            //            plans.put(plan.getPlanName(), plan.getPlanName());
            plans.put(plan.getPlanName(), plan.getPlanName());
        }
        return plans;
    }

    public List<BillingModel> listBillingPlan(String user, BillingPlan billingPlan) {
        try {
            List<BillingModel> billingModels = usagePlanDao.loadBillingPlans(billingPlan);
            for (BillingModel model : billingModels) {
                model.setSubscription(subscriptionDao.getSubscriptionsToModel(user, model.getId()));
            }
            return billingModels;
        } catch (Exception e) {
            FacesMessage message = constructFatalMessage(e.getMessage(), "Error occurred getting plans");
            getFacesContext().addMessage(null, message);
            return Collections.EMPTY_LIST;
        }
    }

    public void subscribe(String user, String id, BillingPlan billingPlan) {
        if(subscriptionDao.getSubscriptions(user, billingPlan.getApiID(), billingPlan.getThrottlePolicy())) {
            FacesMessage message = constructErrorMessage("Already subscribed", "Package subscription already exist.");
            getFacesContext().addMessage(null, message);
            return;
        }
        PackageSubscription packageSubscription = new PackageSubscription();
        packageSubscription.setPackageID(id);
        packageSubscription.setUser(user);
        subscriptionDao.save(packageSubscription);
//        long lid = Long.parseLong(id);
//        String status = subscriptionDao.getSubscriptionsToModel(user, lid) + "";
//        return status;
    }

    public void unSubscribe(String user, String id) {
        long lid = Long.parseLong(id);
        subscriptionDao.removeSubscriptionsToModel(user, lid);
//        String status = subscriptionDao.getSubscriptionsToModel(user, lid) + "";
//        return status;
    }

    protected FacesMessage constructErrorMessage(String message, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail);
    }

    protected FacesMessage constructInfoMessage(String message, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, message, detail);
    }

    protected FacesMessage constructFatalMessage(String message, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_FATAL, message, detail);
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected ResourceBundle getMessageBundle() {
        return ResourceBundle.getBundle("message-labels");
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    public UsagePlanDao getUsagePlanDao() {
        return usagePlanDao;
    }

    public void setUsagePlanDao(UsagePlanDao usagePlanDao) {
        this.usagePlanDao = usagePlanDao;
    }

    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }
}
