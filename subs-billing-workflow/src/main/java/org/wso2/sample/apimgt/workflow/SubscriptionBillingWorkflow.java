/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.sample.apimgt.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIProvider;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.SubscriptionWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.*;
import org.wso2.sample.apimgt.BilllingEngineConnector;
import org.wso2.sample.apimgt.WorkflowConfiguration;
import org.wso2.sample.apimgt.impl.BilllingEngineConnectorImpl;

import java.util.List;

public class SubscriptionBillingWorkflow extends WorkflowExecutor {

    private static final Log log = LogFactory.getLog(SubscriptionBillingWorkflow.class);

    @Override
    public String getWorkflowType() {
        return "SubscriptionBillingWorkflow";
    }

    @Override
    public List<WorkflowDTO> getWorkflowDetails(String status) throws WorkflowException {
        return null;
    }

    @Override
    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {
        super.execute(workflowDTO);

        SubscriptionWorkflowDTO subsWorkflowDTO = null;
        boolean isValid = false;

        if (workflowDTO instanceof SubscriptionWorkflowDTO) {
            subsWorkflowDTO = (SubscriptionWorkflowDTO) workflowDTO;
            APIIdentifier identifier = new APIIdentifier(subsWorkflowDTO.getApiProvider(), subsWorkflowDTO.getApiName(),
                    subsWorkflowDTO.getApiVersion());
            try {
                APIProvider apiProvider = APIManagerFactory.getInstance().getAPIProvider("admin");
                API api = apiProvider.getAPI(identifier);
                BilllingEngineConnector connector = new BilllingEngineConnectorImpl();
                isValid = connector.checkSubscription(api.getUUID(), subsWorkflowDTO.getTierName(),
                        subsWorkflowDTO.getSubscriber());
            } catch (APIManagementException e) {
                log.error("Error occurred while accessing Database: " + e.getMessage(), e);
                throw new WorkflowException("Error occurred while accessing Database: " + e.getMessage(), e);
            }
        }

        if (!isValid) {
            String apimStoreUrl = WorkflowConfiguration.getInstance().getBillingEngineUrl();
            HttpWorkflowResponse httpworkflowResponse = new HttpWorkflowResponse();
            httpworkflowResponse.setRedirectUrl(WorkflowConfiguration.getInstance().getBillingEngineUrl());
            httpworkflowResponse.setAdditionalParameters("CallbackUrl",
                    apimStoreUrl + "/site/blocks/workflow/workflow-listener/ajax/workflow-listener.jag");
            httpworkflowResponse.setAdditionalParameters("workflowRefId", workflowDTO.getExternalWorkflowReference());
            httpworkflowResponse.setAdditionalParameters("reDirectUrl", apimStoreUrl);
            httpworkflowResponse.setRedirectConfirmationMsg(
                    "You will be redirected to a page to setup your billing " + "account Information");
            return httpworkflowResponse;
        } else {
            ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
            try {
                apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
                        APIConstants.SubscriptionStatus.UNBLOCKED);
            } catch (APIManagementException e) {
                log.error("Could not complete subscription creation workflow", e);
                throw new WorkflowException("Could not complete subscription creation workflow", e);
            }
        }

        return new GeneralWorkflowResponse();
    }

    @Override
    public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {
        workflowDTO.setUpdatedTime(System.currentTimeMillis());
        super.complete(workflowDTO);
        log.info("Subscription Creation [Complete] Workflow Invoked. Workflow ID : " + workflowDTO
                .getExternalWorkflowReference() + "Workflow State : " + workflowDTO.getStatus());

        ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();

        if (WorkflowStatus.APPROVED.equals(workflowDTO.getStatus())) {
            try {
                apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
                        APIConstants.SubscriptionStatus.UNBLOCKED);
            } catch (APIManagementException e) {
                log.error("Could not complete subscription creation workflow", e);
                throw new WorkflowException("Could not complete subscription creation workflow", e);
            }
        } else if (WorkflowStatus.REJECTED.equals(workflowDTO.getStatus())) {
            try {
                apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
                        APIConstants.SubscriptionStatus.REJECTED);
            } catch (APIManagementException e) {
                log.error("Could not complete subscription creation workflow", e);
                throw new WorkflowException("Could not complete subscription creation workflow", e);
            }
        }
        return new GeneralWorkflowResponse();
    }
}
