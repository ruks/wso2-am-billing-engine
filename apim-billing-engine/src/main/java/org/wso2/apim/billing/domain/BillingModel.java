package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "billing_package")
public class BillingModel extends BaseEntity {

    @OneToMany(mappedBy = "billingModel",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BillingAttribute> attributes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private BillingPlan plan;
    private String packageName;
    private String packageType;

    public BillingModel() {
    }

    public List<BillingAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<BillingAttribute> attributes) {
        for (BillingAttribute billingAttribute: attributes) {
            billingAttribute.setBillingModel(this);
        }
        this.attributes = attributes;
    }

    public BillingPlan getPlan() {
        return plan;
    }

    public void setPlan(BillingPlan plan) {
        this.plan = plan;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
}
