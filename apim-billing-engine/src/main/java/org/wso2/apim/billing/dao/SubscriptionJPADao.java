package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericJpaDao;
import org.wso2.apim.billing.domain.PackageSubscription;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class SubscriptionJPADao extends GenericJpaDao<PackageSubscription, Long> implements SubscriptionDao {
    public SubscriptionJPADao() {
        super(PackageSubscription.class);
    }

    public boolean getSubscriptions(String user, String apiID, String tier) {
        EntityManager entityManager = getEntityManager();
        String query = "SELECT plan.id FROM package_subscription,billing_package,plan where "
                + "package_subscription.packageID=billing_package.id and billing_package.plan_id=plan.id and "
                + "plan.apiID=? and plan.throttlePolicy=?" + "and package_subscription.user=?";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, apiID);
        nativeQuery.setParameter(2, tier);
        nativeQuery.setParameter(3, user);
        List<Object[]> result = nativeQuery.getResultList();
        return result.size() > 0;
    }

    public boolean getSubscriptionsToModel(String user, long packageId) {
        EntityManager entityManager = getEntityManager();
        String query = "SELECT id FROM package_subscription where packageId=? and user=?";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, packageId);
        nativeQuery.setParameter(2, user);
        List<Object[]> result = nativeQuery.getResultList();
        return result.size() > 0;
    }

    public void removeSubscriptionsToModel(String user, long packageId) {
        EntityManager entityManager = getEntityManager();
        String query = "DELETE FROM package_subscription where packageId=? and user=?";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, packageId);
        nativeQuery.setParameter(2, user);
        nativeQuery.executeUpdate();
    }

    public List<String> getSubscribers() {
        EntityManager entityManager = getEntityManager();
        String query = "SELECT DISTINCT USER FROM package_subscription";
        Query nativeQuery = entityManager.createNativeQuery(query);
        List<String> result = nativeQuery.getResultList();
        return result;
    }
}
