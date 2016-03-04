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

import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.HttpWorkflowResponse;
import org.wso2.carbon.apimgt.impl.workflow.SubscriptionCreationSimpleWorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;

import java.util.List;

public class SubscriptionBillingWorkflow extends SubscriptionCreationSimpleWorkflowExecutor {


    @Override
    public String getWorkflowType() {
        return super.getWorkflowType();
    }

    @Override
    public List<WorkflowDTO> getWorkflowDetails(String status) throws WorkflowException {
        return super.getWorkflowDetails(status);
    }

    @Override
    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        super.execute(workflowDTO);

        HttpWorkflowResponse httpworkflowResponse = new HttpWorkflowResponse();
        httpworkflowResponse.setRedirectUrl("http://localhost:8080/j2eeapplication-0.0.1-SNAPSHOT");
        httpworkflowResponse.setAdditionalParameters("CallbackUrl",
                "http://localhost:9763/store/site/blocks/workflow/workflow-listener/ajax/workflow-listener.jag");
        httpworkflowResponse.setAdditionalParameters("workflowRefId" , workflowDTO.getExternalWorkflowReference());
        httpworkflowResponse.setRedirectConfirmationMsg("You will be redirected to a page to setup your billing " +
                                                                                            "account Information");
        return httpworkflowResponse;
    }

}
