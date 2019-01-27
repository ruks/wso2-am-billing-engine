package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericDao;
import org.wso2.apim.billing.domain.PackageSubscription;

public interface SubscriptionDao extends GenericDao<PackageSubscription, Long> {
    boolean getSubscriptions(String user, String apiID, String tier);
    boolean getSubscriptionsToModel(String user, long packageId);
    void removeSubscriptionsToModel(String user, long packageId);
}