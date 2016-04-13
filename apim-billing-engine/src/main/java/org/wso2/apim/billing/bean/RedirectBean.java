/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* WSO2 Inc. licenses this file to you under the Apache License,
* Version 2.0 (the "License"); you may not use this file except
* in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.wso2.apim.billing.bean;

import org.wso2.apim.billing.domain.UserEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class RedirectBean implements Serializable {

    String reDirectUrl = "account";
    String workflowRefId;
    String CallbackUrl;

    public String getReDirectUrl() {
        return reDirectUrl;
    }

    public void setReDirectUrl(String reDirectUrl) {
        this.reDirectUrl = reDirectUrl;
    }

    public String getWorkflowRefId() {
        return workflowRefId;
    }

    public void setWorkflowRefId(String workflowRefId) {
        this.workflowRefId = workflowRefId;
    }

    public String getCallbackUrl() {
        return CallbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        CallbackUrl = callbackUrl;
    }

    public String getUrl() {
        if (reDirectUrl == null) {
            return "account";
        }
        return reDirectUrl;
    }

    public void activateSubscription(UserEntity userEntity) {
        System.out.println("activateSubscription");
    }
}
