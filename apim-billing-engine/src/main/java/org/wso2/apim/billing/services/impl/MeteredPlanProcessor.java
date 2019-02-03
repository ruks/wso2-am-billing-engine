package org.wso2.apim.billing.services.impl;

import org.wso2.apim.billing.bean.APIUsage;
import org.wso2.apim.billing.clients.SPRestClient;
import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.model.PackageFeeModel;
import org.wso2.apim.billing.services.PlanProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MeteredPlanProcessor extends PlanProcessor {

    public MeteredPlanProcessor(SPRestClient spRestClient) {
        super(spRestClient);
    }

    public PackageFeeModel process(String user, BillingModel billingModel) {
        try {
            PackageFeeModel model = new PackageFeeModel();
            List<APIUsage> apiUsage = spRestClient.getUsagePerPackage(user, billingModel);
            APIUsage usage;
            if (!apiUsage.isEmpty()) {
                usage = apiUsage.get(0);
            } else {
                return null;
            }
            long totalRequestCount = usage.getSuccessCount() + usage.getExceedCount();
            long pricePerReq = getValue(billingModel.getAttributes(), "pricePerReq");
            long subscriptionFee = getValue(billingModel.getAttributes(), "subscription");
            long usageFee = totalRequestCount * pricePerReq;
            long totalCost = subscriptionFee + usageFee;
            model.setTotalCost(totalCost);
            List<BillingAttribute> attributes = new ArrayList<>(billingModel.getAttributes());
            attributes.add(new BillingAttribute(0, "usageFee", "Usage Fee", String.valueOf(usageFee)));
            attributes.add(new BillingAttribute(0, "cost", "Cost", String.valueOf(totalCost)));
            attributes.add(0, new BillingAttribute(0, "name", "Name", billingModel.getPackageName()));
            attributes.add(1, new BillingAttribute(0, "type", "Type", billingModel.getPackageType()));
            attributes.add(2, new BillingAttribute(0, "apiName", "API Name", billingModel.getPlan().getApiName()));
            attributes.add(3, new BillingAttribute(0, "apiVersion", "API Version", billingModel.getPlan().getApiVersion()));
            attributes.add(4, new BillingAttribute(0, "policy", "Subscription Policy", billingModel.getPlan().getThrottlePolicy()));
            model.setAttributes(attributes);
            return model;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
