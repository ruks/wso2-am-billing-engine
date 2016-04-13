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
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.SubscriptionWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.*;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.sample.apimgt.dao.BillingDao;

import java.io.File;
import java.util.List;

public class SubscriptionBillingWorkflow extends WorkflowExecutor {

    private static final Log log = LogFactory.getLog(SubscriptionBillingWorkflow.class);

    private String billingEngineUrl;
    private String APIMStoreUrl;

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
        boolean userExists = false;

        if (workflowDTO instanceof SubscriptionWorkflowDTO) {
            subsWorkflowDTO = (SubscriptionWorkflowDTO) workflowDTO;

            try {
                BillingDao billingDao = BillingDao.getInstance();
                userExists = billingDao.userExists(subsWorkflowDTO.getSubscriber());
            } catch (APIManagementException e) {
                log.error("Error occurred while accessing Database: " + e.getMessage(), e);
                throw new WorkflowException("Error occurred while accessing Database: " + e.getMessage(), e);
            }
        }

        if (!userExists) {
            loadDefaultConfig();
            HttpWorkflowResponse httpworkflowResponse = new HttpWorkflowResponse();
            httpworkflowResponse.setRedirectUrl(billingEngineUrl);
            httpworkflowResponse.setAdditionalParameters("CallbackUrl",
                    APIMStoreUrl + "/site/blocks/workflow/workflow-listener/ajax/workflow-listener.jag");
            httpworkflowResponse.setAdditionalParameters("workflowRefId", workflowDTO.getExternalWorkflowReference());
            httpworkflowResponse.setAdditionalParameters("reDirectUrl", APIMStoreUrl);
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

    public void loadDefaultConfig() {
        APIManagerConfiguration configuration = new APIManagerConfiguration();
        String filePath = CarbonUtils.getCarbonHome() + File.separator + "repository" +
                File.separator + "conf" + File.separator + "api-manager.xml";
        try {
            configuration.load(filePath);
        } catch (APIManagementException e) {
            log.error("cannot find the congifuration file at: " + filePath, e);
        }

        billingEngineUrl = configuration.getFirstProperty("billingEngineUrl");
        APIMStoreUrl = configuration.getFirstProperty("APIStore.URL");

    }

}
