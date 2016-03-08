package org.wso2.apim.billing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.wso2.apim.billing.commons.domain.BaseEntity;

/**
 * Entity to hold application user data - first name, last name, etc.
 *
 * @author Arthur Vin
 */
@Entity
@Table(name = "billingplan")
public class PlanEntity extends BaseEntity {
    private static final long serialVersionUID = -8789920463809744548L;

    private String planName;
    private String quota;
    private double feePerRequest;
    private double subscriptionFee;

    public enum PLAN_TYPES {STANDARD, USAGE}

    ;

    @Column(name = "planType", columnDefinition = "VARCHAR(255) default 'STANDARD'")
    private String planType;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
}
