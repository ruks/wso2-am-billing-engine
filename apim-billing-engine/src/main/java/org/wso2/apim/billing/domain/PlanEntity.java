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
    private String userName;
    private String quota;
    private String fee;
    private String adfee;

    public enum PLAN_TYPES {STANDARD, USAGE}

    ;

    @Column(name = "planType", columnDefinition = "VARCHAR(255) default 'STANDARD'") private String planType;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getAdfee() {
        return adfee;
    }

    public void setAdfee(String adfee) {
        this.adfee = adfee;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}
