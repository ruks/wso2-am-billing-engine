package org.wso2.apim.billing.bean;

public class APIUsage {
    private String apiName;
    private String version;
    private String subscriptionTier;
    private String applicationName;
    private long successCount;
    private long exceedCount;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(String subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public long getExceedCount() {
        return exceedCount;
    }

    public void setExceedCount(long exceedCount) {
        this.exceedCount = exceedCount;
    }
}
