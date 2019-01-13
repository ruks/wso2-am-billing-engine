package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericJpaDao;
import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.BillingPlan;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

/**
 * Data access object JPA impl to work with User entity database operations.
 *
 * @author Arthur Rukshan
 */
public class UsagePlanJpaDao extends GenericJpaDao<BillingPlan, Long> implements UsagePlanDao {

    public UsagePlanJpaDao() {
        super(BillingPlan.class);
    }

    @Override
    public List<BillingModel> loadBillingPlans(BillingPlan billingPlan) {
        if (billingPlan == null || billingPlan.getApiID() == null) {
            return Collections.EMPTY_LIST;
        }
        EntityManager entityManager = getEntityManager();
        String query = "SELECT billing_package.id,packageName,packageType, displayName,name,value FROM "
                + "plan,billing_package,billing_attribute where plan.id=billing_package.plan_id and "
                + "billing_package.id=billing_attribute.billing_package_id and apiName=? and "
                + "apiVersion=? and throttlePolicy=? and apiID=?";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, billingPlan.getApiName());
        nativeQuery.setParameter(2, billingPlan.getApiVersion());
        nativeQuery.setParameter(3, billingPlan.getThrottlePolicy());
        nativeQuery.setParameter(4, billingPlan.getApiID());
        List<Object[]> result = nativeQuery.getResultList();
        Map<Long, BillingModel> plans = new HashMap<>();
        for (Object[] row : result) {
            long packageId = ((BigInteger) row[0]).longValue();
            BillingModel billingModel;
            if (plans.containsKey(packageId)) {
                billingModel = plans.get(packageId);
            } else {
                billingModel = new BillingModel();
                billingModel.setId(packageId);
                billingModel.setPackageName((String) row[1]);
                billingModel.setPackageType((String) row[2]);
                billingModel.setAttributes(new ArrayList<>());
                plans.put(packageId, billingModel);
            }
            List<BillingAttribute> attributes = billingModel.getAttributes();
            attributes.add(new BillingAttribute(1, (String) row[4], (String) row[3], (String) row[5]));
        }
        return new ArrayList<>(plans.values());
    }

    @Override
    public List<BillingModel> loadBillingPlansOfUser(String userName) {
        if (userName == null) {
            return Collections.EMPTY_LIST;
        }
        EntityManager entityManager = getEntityManager();
        String query = "SELECT billing_package.id,packageName,packageType, displayName,name,value FROM "
                + "package_subscription,billing_package,billing_attribute,plan where plan.id=billing_package.plan_id "
                + "and billing_package.id=billing_attribute.billing_package_id and "
                + "package_subscription.packageID=billing_package.id and package_subscription.user=?";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, userName);
        List<Object[]> result = nativeQuery.getResultList();
        Map<Long, BillingModel> plans = new HashMap<>();
        for (Object[] row : result) {
            long packageId = ((BigInteger) row[0]).longValue();
            BillingModel billingModel;
            if (plans.containsKey(packageId)) {
                billingModel = plans.get(packageId);
            } else {
                billingModel = new BillingModel();
                billingModel.setId(packageId);
                billingModel.setPackageName((String) row[1]);
                billingModel.setPackageType((String) row[2]);
                billingModel.setAttributes(new ArrayList<>());
                plans.put(packageId, billingModel);
            }
            List<BillingAttribute> attributes = billingModel.getAttributes();
            attributes.add(new BillingAttribute(1, (String) row[4], (String) row[3], (String) row[5]));
        }
        return new ArrayList<>(plans.values());
    }
}
