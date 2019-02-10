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

public class QuotaPlanProcessor extends PlanProcessor {

    public QuotaPlanProcessor(SPRestClient spRestClient) {
        super(spRestClient);
    }

    public PackageFeeModel process(String user, BillingModel billingModel, int selectedMonth) {
        try {
            PackageFeeModel model = new PackageFeeModel();
            List<APIUsage> apiUsage = spRestClient.getUsagePerPackage(user, billingModel, selectedMonth);
            APIUsage usage;
            if (!apiUsage.isEmpty()) {
                usage = apiUsage.get(0);
            } else {
                return null;
            }
            long totalRequestCount = usage.getSuccessCount() + usage.getExceedCount();
            double pricePerReq = getValue(billingModel.getAttributes(), "pricePerReq");
            double subscriptionFee = getValue(billingModel.getAttributes(), "subscriptionFee");
            double usageFee = totalRequestCount * pricePerReq;
            double totalCost = subscriptionFee + usageFee;
            model.setTotalCost(totalCost);
            List<BillingAttribute> attributes = new ArrayList<>();
            attributes.add(new BillingAttribute(0, "usageFee", "Usage Fee", String.valueOf(usageFee)));
            attributes.add(new BillingAttribute(1, "subscriptionFee", "Subscription Fee",
                    String.valueOf(subscriptionFee)));
            model.setAttributes(attributes);
            return model;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
