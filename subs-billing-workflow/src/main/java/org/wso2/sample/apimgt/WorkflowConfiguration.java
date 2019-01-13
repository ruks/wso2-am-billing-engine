package org.wso2.sample.apimgt;

public class WorkflowConfiguration {
    private String billingEngineUrl;
    private String apimStoreUrl;
    private static WorkflowConfiguration instance;

    public static WorkflowConfiguration getInstance() {
        if (instance == null) {
            instance = new WorkflowConfiguration();
        }
        return instance;
    }

    public String getBillingEngineUrl() {
        return billingEngineUrl;
    }

    public void setBillingEngineUrl(String billingEngineUrl) {
        this.billingEngineUrl = billingEngineUrl;
    }

    public String getApimStoreUrl() {
        return apimStoreUrl;
    }

    public void setApimStoreUrl(String apimStoreUrl) {
        this.apimStoreUrl = apimStoreUrl;
    }
}
