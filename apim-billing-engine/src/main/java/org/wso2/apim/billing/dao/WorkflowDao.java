package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.commons.dao.GenericDao;
import org.wso2.apim.billing.domain.SubsWorkflowDTO;

import java.util.List;

public interface WorkflowDao extends GenericDao<SubsWorkflowDTO, Long> {
    SubsWorkflowDTO getWorkflowOfSubscription(String user, String api, String version, String tier);
    List<SubsWorkflowDTO> getPendingWorkflowOfSubscription(String user);
}
