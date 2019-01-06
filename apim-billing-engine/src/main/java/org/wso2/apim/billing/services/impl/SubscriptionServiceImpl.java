package org.wso2.apim.billing.services.impl;

import org.wso2.apim.billing.dao.SubscriptionDao;
import org.wso2.apim.billing.services.SubscriptionService;

public class SubscriptionServiceImpl implements SubscriptionService {
    private SubscriptionDao subscriptionDao;

    public boolean checkSubscription(String userID, String apiID, String tier) {
        return subscriptionDao.getSubscriptions(userID, apiID, tier);
    }

    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }
}
