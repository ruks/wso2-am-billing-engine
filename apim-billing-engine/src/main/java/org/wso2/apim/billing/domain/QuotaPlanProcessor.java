package org.wso2.apim.billing.domain;

import javax.persistence.*;

//@Entity
//@Table(name = "quota_plan")
public class QuotaPlanProcessor extends PlanProcessor {
    private long allowedQuota;
    private double quotaFee;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "plan_id")
    private BillingPlan plan;

    public long getAllowedQuota() {
        return allowedQuota;
    }

    public void setAllowedQuota(long allowedQuota) {
        this.allowedQuota = allowedQuota;
    }

    public double getQuotaFee() {
        return quotaFee;
    }

    public void setQuotaFee(double quotaFee) {
        this.quotaFee = quotaFee;
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
