package org.wso2.apim.billing.services.impl;

import org.wso2.apim.billing.clients.APIRESTClient;
import org.wso2.apim.billing.clients.TokenManager;
import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.BillingPlan;
import org.wso2.apim.billing.model.BasicAPIInfo;
import org.wso2.apim.billing.services.APIInfoService;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class APIInfoServiceImpl implements APIInfoService {
    private String publisherUrl;
    private TokenManager tokenManager;
    private APIRESTClient apirestClient;
    private Map<String, List<BasicAPIInfo>> apis;

    public List<String> listApis() throws Exception {
        String token = tokenManager.getToken();
        System.out.println(token);

        apis = apirestClient.getApis(token);
        return new ArrayList<>(apis.keySet());
    }

    public List<BasicAPIInfo> listVersions(String apiName) throws Exception {
        List<BasicAPIInfo> apiInfoList = new ArrayList<>();
        if (apiName != null && !apiName.isEmpty()) {
            apiInfoList = apis.get(apiName);
        }
        return apiInfoList;
    }

    public List<String> listPolicies(BillingPlan billingPlan) throws Exception {

        List<String> policies = new ArrayList<>();
        if (billingPlan != null && billingPlan.getApiID() != null && !billingPlan.getApiID().isEmpty()) {
            String token = tokenManager.getToken();
            System.out.println(token);
            policies = apirestClient.getApiThrottlePolicies(token, billingPlan.getApiID());

            if (billingPlan.getApiName() != null) {
                for (BasicAPIInfo basicAPIInfo : apis.get(billingPlan.getApiName())) {
                    if (billingPlan.getApiID().equals(basicAPIInfo.getId())) {
                        billingPlan.setApiVersion(basicAPIInfo.getVersion());
                        break;
                    }
                }
            }
        }
        return policies;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public APIRESTClient getApirestClient() {
        return apirestClient;
    }

    public void setApirestClient(APIRESTClient apirestClient) {
        this.apirestClient = apirestClient;
    }

    /*
    public static <T> T getBean(final String beanName, final Class<T> clazz) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (T) FacesContext.getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, beanName);
    }*/
}
