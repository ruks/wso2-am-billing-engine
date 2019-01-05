package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plan")
public class BillingPlan extends BaseEntity {
    private String apiName;
    private String apiID;
    private String apiVersion;
    private String throttlePolicy;
    @OneToMany(mappedBy = "plan",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BillingModel> billingModels = new ArrayList<>();
    @Transient
    private BillingModel currentBillingModel = new BillingModel();

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getThrottlePolicy() {
        return throttlePolicy;
    }

    public void setThrottlePolicy(String throttlePolicy) {
        this.throttlePolicy = throttlePolicy;
    }

    public String getApiID() {
        return apiID;
    }

    public void setApiID(String apiID) {
        this.apiID = apiID;
    }

    public List<BillingModel> getBillingModels() {
        return billingModels;
    }

    public void setBillingModels(List<BillingModel> billingModels) {
        this.billingModels = billingModels;
    }

    public void addBillingModels(BillingModel billingModel) {
        for (BillingAttribute billingAttribute: billingModel.getAttributes()) {
            billingAttribute.setBillingModel(billingModel);
        }
        billingModel.setPlan(this);

        billingModels.add(billingModel);
        currentBillingModel = new BillingModel();
    }

    public BillingModel getCurrentBillingModel() {
        return currentBillingModel;
    }

    public void setCurrentBillingModel(BillingModel currentBillingModel) {
        this.currentBillingModel = currentBillingModel;
    }

}
