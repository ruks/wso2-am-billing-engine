package org.wso2.apim.billing.services.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.wso2.apim.billing.dao.PlanDao;
import org.wso2.apim.billing.domain.PlanEntity;
import org.wso2.apim.billing.services.PlanService;
import org.primefaces.component.inputtext.InputText;

/**
 * Service providing service methods to work with user data and entity.
 *
 * @author Arthur Rukshan
 */
public class PlanServiceImpl implements PlanService {

    private PlanDao planDao;

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
            FacesMessage message = constructErrorMessage(null,
                    String.format(getMessageBundle().getString("userExistsMsg"), value));
            getFacesContext().addMessage(event.getComponent().getClientId(), message);
        } else {
            FacesMessage message = constructInfoMessage(null,
                    String.format(getMessageBundle().getString("userAvailableMsg"), value));
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
}
