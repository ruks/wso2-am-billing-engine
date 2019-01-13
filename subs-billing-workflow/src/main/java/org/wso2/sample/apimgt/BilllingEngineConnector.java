package org.wso2.sample.apimgt;

public interface BilllingEngineConnector {
    boolean checkSubscription(String apiId, String tier, String subscriber);
}
