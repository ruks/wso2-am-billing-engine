package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericDao;
import org.wso2.apim.billing.domain.SubsWorkflowDTO;

public interface WorkflowDao extends GenericDao<SubsWorkflowDTO, Long> {
    SubsWorkflowDTO getWorkflowOfSubscription(String user, String api, String version, String tier);
}
