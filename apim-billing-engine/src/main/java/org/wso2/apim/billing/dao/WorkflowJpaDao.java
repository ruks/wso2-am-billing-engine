package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericJpaDao;
import org.wso2.apim.billing.domain.SubsWorkflowDTO;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class WorkflowJpaDao extends GenericJpaDao<SubsWorkflowDTO, Long> implements WorkflowDao {
    public WorkflowJpaDao() {
        super(SubsWorkflowDTO.class);
    }

    @Override
    public List<SubsWorkflowDTO> getWorkflowOfSubscription(String subscriber, String apiName, String apiVersion, String subscriptionTier) {
        List<SubsWorkflowDTO> workflow = null;

        Query query = getEntityManager().createQuery(
                "select u from " + getPersistentClass().getSimpleName() + " u where "
                        + "u.apiName = :apiName "
                        + "and u.apiVersion = :apiVersion "
                        + "and u.subscriptionTier = :subscriptionTier "
                        + "and u.subscriber = :subscriber ")
                .setParameter("apiName", apiName)
                .setParameter("apiVersion", apiVersion)
                .setParameter("subscriptionTier", subscriptionTier)
                .setParameter("subscriber", subscriber);

        try {
            workflow = (List<SubsWorkflowDTO>) query.getResultList();
        } catch (NoResultException e) {
            //do nothing
        }

        return workflow;
    }

    @Override
    public List<SubsWorkflowDTO> getPendingWorkflowOfSubscription(String subscriber) {
        List<SubsWorkflowDTO> workflow = null;

        Query query = getEntityManager().createQuery(
                "select u from " + getPersistentClass().getSimpleName() + " u where "
                        + " u.subscriber = :subscriber ")
                .setParameter("subscriber", subscriber);

        try {
            workflow = (List<SubsWorkflowDTO>) query.getResultList();
        } catch (NoResultException e) {
            //do nothing
        }

        return workflow;
    }
}
