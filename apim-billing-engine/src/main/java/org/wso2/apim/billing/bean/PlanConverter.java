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
    private String quota;
    private double feePerRequest;
    private double subscriptionFee;
    private String planType;

    public double getFeePerRequest() {
        return feePerRequest;
    }

    public void setFeePerRequest(double feePerRequest) {
        this.feePerRequest = feePerRequest;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(double subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        PlanEntity plan = planDAO.loadPlanByPlanName(value);
        planName = plan.getPlanName();
        quota = plan.getQuota();
        feePerRequest = plan.getFeePerRequest();
        planType = plan.getPlanType();
        subscriptionFee = plan.getSubscriptionFee();
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
