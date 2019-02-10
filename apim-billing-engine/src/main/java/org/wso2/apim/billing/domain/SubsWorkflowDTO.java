package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subsWorkflowDTO")
public class SubsWorkflowDTO extends BaseEntity {
    private String workflowRefId;
    private String callbackUrl;
    private String apiName;
    private String apiVersion;
    private String apiId;
    private String subscriptionTier;
    private String application;
    private String subscriber;

    public String getWorkflowRefId() {
        return workflowRefId;
    }

    public void setWorkflowRefId(String workflowRefId) {
        this.workflowRefId = workflowRefId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

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

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(String subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }
}
