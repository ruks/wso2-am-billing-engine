package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "billingplan")
public class BillingPlanEntity extends BaseEntity {

    private String planName;
    private String throttlePolicy;
    private double subscriptionFee;
    private double feePerAdditionalRequest;
    private double feePerSingleRequest;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getThrottlePolicy() {
        return throttlePolicy;
    }

    public void setThrottlePolicy(String throttlePolicy) {
        this.throttlePolicy = throttlePolicy;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(double subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    public double getFeePerAdditionalRequest() {
        return feePerAdditionalRequest;
    }

    public void setFeePerAdditionalRequest(double feePerAdditionalRequest) {
        this.feePerAdditionalRequest = feePerAdditionalRequest;
    }

    public double getFeePerSingleRequest() {
        return feePerSingleRequest;
    }

    public void setFeePerSingleRequest(double feePerSingleRequest) {
        this.feePerSingleRequest = feePerSingleRequest;
    }
}
