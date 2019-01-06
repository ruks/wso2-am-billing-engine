package org.wso2.apim.billing.services;

public interface SubscriptionService {
    boolean checkSubscription(String userID, String apiID, String tier);
}
