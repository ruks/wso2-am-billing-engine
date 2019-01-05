package org.wso2.apim.billing.services;


import org.wso2.apim.billing.domain.BillingPlan;
import org.wso2.apim.billing.model.BasicAPIInfo;

import java.util.List;

public interface APIInfoService {
    List<String> listApis() throws Exception;
    List<BasicAPIInfo> listVersions(String apiName) throws Exception;
    List<String> listPolicies(BillingPlan billingPlan) throws Exception;
}
