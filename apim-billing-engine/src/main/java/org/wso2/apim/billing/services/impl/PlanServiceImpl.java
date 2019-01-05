package org.wso2.apim.billing.services.impl;

import org.primefaces.component.inputtext.InputText;
import org.wso2.apim.billing.dao.PlanDao;
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

    public List<BillingAttribute> listAttributes(BillingPlan billingPlan) {
        attributes = new ArrayList<>();
        if (billingPlan == null || billingPlan.getCurrentBillingModel() == null) {
            return attributes;
        }
        int i = 0;
        if ("quota".equalsIgnoreCase(billingPlan.getCurrentBillingModel().getPackageType())) {
            attributes.add(new BillingAttribute(i++, "quota", "Quota", null));
            attributes.add(new BillingAttribute(i++, "quotaPrice", "Quota Price", null));
            attributes.add(new BillingAttribute(i++, "excessivePrice", "Excessive Price", null));
        } else if ("metered".equalsIgnoreCase(billingPlan.getCurrentBillingModel().getPackageType())) {
            attributes.add(new BillingAttribute(i++, "pricePerReq", "Price per a request", null));
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

    public boolean createPlan(PlanEntity planEntity) {
        if (!planDao.checkAvailable(planEntity.getPlanName())) {
            FacesMessage message = constructErrorMessage(
                    String.format(getMessageBundle().getString("userExistsMsg"), planEntity.getPlanName()), null);
            getFacesContext().addMessage(null, message);

            return false;
        }

        try {
            planEntity.setPlanType(PlanEntity.PLAN_TYPES.STANDARD.toString());
            planDao.save(planEntity);
        } catch (Exception e) {
            FacesMessage message = constructFatalMessage(e.getMessage(), null);
            getFacesContext().addMessage(null, message);

            return false;
        }

        FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("plan");
        return true;
    }

    public boolean creatBillingePlan(BillingPlan billingPlan) {
        try {
            usagePlanDao.save(billingPlan);
        } catch (Exception e) {
            FacesMessage message = constructFatalMessage(e.getMessage(), "Error occurred persisting plans");
            getFacesContext().addMessage(null, message);
            return false;
        }
        return true;
    }

    public boolean createUsagePlan(PlanEntity planEntity) {
        if (!planDao.checkAvailable(planEntity.getPlanName())) {
            FacesMessage message = constructErrorMessage(
                    String.format(getMessageBundle().getString("userExistsMsg"), planEntity.getPlanName()), null);
            getFacesContext().addMessage(null, message);

            return false;
        }

        try {
            planEntity.setPlanType(PlanEntity.PLAN_TYPES.USAGE.toString());
            planDao.save(planEntity);
        } catch (Exception e) {
            FacesMessage message = constructFatalMessage(e.getMessage(), null);
            getFacesContext().addMessage(null, message);
            return false;
        }
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("plan");
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
}
