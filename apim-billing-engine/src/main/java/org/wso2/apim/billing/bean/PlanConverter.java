/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* WSO2 Inc. licenses this file to you under the Apache License,
* Version 2.0 (the "License"); you may not use this file except
* in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.wso2.apim.billing.bean;

import org.wso2.apim.billing.dao.PlanDao;
import org.wso2.apim.billing.domain.PlanEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class PlanConverter implements Converter {

    private PlanDao planDAO;
    private PlanEntity selectedPlan;
    private String planName;
    private String userName;
    private String quota;
    private String fee;
    private String adFee;
    private String planType;

    public String getAdFee() {
        return adFee;
    }

    public void setAdFee(String adFee) {
        this.adFee = adFee;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public PlanEntity getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(PlanEntity selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    public PlanDao getPlanDAO() {
        return planDAO;
    }

    public void setPlanDAO(PlanDao planDAO) {
        this.planDAO = planDAO;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        PlanEntity plan= planDAO.loadPlanByPlanName(value);
        planName = plan.getPlanName();
        userName = plan.getUserName();
        quota = plan.getQuota();
        fee = plan.getFee();
        adFee = plan.getAdfee();
        planType = plan.getPlanType();

        return plan;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            String name = ((PlanEntity) value).getPlanName();
            if (name == null) {
                return null;
            }
            return name.toString();
        }catch (Exception e){
            return "Select Plan1";
        }

    }
}
