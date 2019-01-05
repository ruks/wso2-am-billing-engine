package org.wso2.apim.billing.domain;

import javax.persistence.*;

@Entity
@Table(name = "metered_plan")
public class MeteredPlanProcessor extends PlanProcessor {
    private double feePerRequest;
    private double subscriptionFee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private BillingPlan plan;

    public double getFeePerRequest() {
        return feePerRequest;
    }

    public void setFeePerRequest(double feePerRequest) {
        this.feePerRequest = feePerRequest;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(double subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    @Override
    void process() {

    }

    public BillingPlan getBillingPlan() {
        return plan;
    }

    public void setBillingPlan(BillingPlan billingPlan) {
        this.plan = billingPlan;
    }

}
