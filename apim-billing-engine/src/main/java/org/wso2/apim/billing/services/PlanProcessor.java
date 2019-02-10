package org.wso2.apim.billing.services;

import org.wso2.apim.billing.clients.SPRestClient;
import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.model.PackageFeeModel;

import java.util.List;

public abstract class PlanProcessor {
    protected SPRestClient spRestClient;

    public PlanProcessor(SPRestClient spRestClient) {
        this.spRestClient = spRestClient;
    }

    abstract public PackageFeeModel process(String user, BillingModel billingModel, int selectedMonth);

    protected double getValue(List<BillingAttribute> attributes, String key) {
        for (BillingAttribute billingAttribute : attributes) {
            if (key.equals(billingAttribute.getKey())) {
                return Double.parseDouble(billingAttribute.getValue());
            }
        }
        return 0;
    }
}
